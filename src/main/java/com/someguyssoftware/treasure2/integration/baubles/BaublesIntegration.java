/*
 * This file is part of  Treasure2.
 * Copyright (c) 2021, Mark Gottschling (gottsch)
 * 
 * All rights reserved.
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Treasure2.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */
package com.someguyssoftware.treasure2.integration.baubles;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.someguyssoftware.treasure2.capability.AdornmentCapabilityProvider;
import com.someguyssoftware.treasure2.capability.CharmableCapability;
import com.someguyssoftware.treasure2.capability.DurabilityCapability;
import com.someguyssoftware.treasure2.capability.ICharmableCapability;
import com.someguyssoftware.treasure2.capability.IDurabilityCapability;
import com.someguyssoftware.treasure2.capability.IMagicsInventoryCapability;
import com.someguyssoftware.treasure2.capability.IRunestonesCapability;
import com.someguyssoftware.treasure2.capability.MagicsInventoryCapability;
import com.someguyssoftware.treasure2.capability.RunestonesCapability;
import com.someguyssoftware.treasure2.capability.TreasureCapabilities;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.enums.AdornmentType;
import com.someguyssoftware.treasure2.item.Adornment;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.cap.BaublesCapabilities;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.fml.common.Loader;

/**
 * This class was derived from Wizardry's Baubles integration {@link electroblob.wizardry.integration.baubles}
 * and AncientSpellcraft {@link com.windanesz.ancientspellcraft.integration.baubles}.
 * @author Electroblob, WinDanesz
 * @author Mark Gottschling (gottsch) on Dec 18, 2021
 *
 */
public class BaublesIntegration {
	public static final String BAUBLES_MOD_ID = "baubles";
	public static final List<Integer> BAUBLES_SLOTS = Lists.newArrayList(); //Arrays.asList(0, 1, 2, 5, 6);
	public static final Map<AdornmentType, BaubleType> TREASURE_BAUBLE_TYPE_MAP = new EnumMap<>(AdornmentType.class);
	private static boolean baublesLoaded;

	/**
	 * 
	 */
	public static void init() {
		baublesLoaded = Loader.isModLoaded(BAUBLES_MOD_ID);

		if (!isEnabled()) {
			return;
		}

		/*
		 * Bauble Slots
		 * --------------
		 * AMULET = 0;
		 * RING = 1 | 2;
		 * BELT = 3;
		 * TRINKET = 0-6; ie anywhere
		 * HEAD = 4;
		 * BODY = 5;
		 * CHARM = 6;
		 */

		// BaubleType returns a primitive int array. need to convert to an List<Integer>
		BAUBLES_SLOTS.addAll((IntStream.of(BaubleType.AMULET.getValidSlots()).boxed().collect(Collectors.toList())));
		BAUBLES_SLOTS.addAll((IntStream.of(BaubleType.RING.getValidSlots()).boxed().collect(Collectors.toList())));
		BAUBLES_SLOTS.addAll((IntStream.of(BaubleType.BODY.getValidSlots()).boxed().collect(Collectors.toList()))); // for pocketwatch or bracelet
		BAUBLES_SLOTS.addAll((IntStream.of(BaubleType.CHARM.getValidSlots()).boxed().collect(Collectors.toList())));

		// map  Treasure2 Adornments types to Bauble types in order to equip adornments/charms		
		TREASURE_BAUBLE_TYPE_MAP.put(AdornmentType.RING, BaubleType.RING);
		TREASURE_BAUBLE_TYPE_MAP.put(AdornmentType.NECKLACE, BaubleType.AMULET);
		TREASURE_BAUBLE_TYPE_MAP.put(AdornmentType.BRACELET, BaubleType.BODY);
		TREASURE_BAUBLE_TYPE_MAP.put(AdornmentType.POCKET, BaubleType.BODY);
		// FUTURE
		// TREASURE_BAUBLE_TYPE_MAP.put(AdornmentType.EARRING, BaubleType.HEAD);
		
		// TODO how to map Charm to BaubleType.CHARM? Maybe not until the redo in v2.0
	}

	/**
	 * 
	 * @return
	 */
	public static boolean isEnabled() {
		return TreasureConfig.INTEGRATION.enableBaubles && baublesLoaded;
	}

	/**
	 * Return true if the given item is equipped in any bauble slot.
	 * @param player The player whose inventory is to be checked.
	 * @param item The item to check for.
	 * @return true if the given item is equipped in a bauble slot, false otherwise.
	 */
	public static boolean isBaubleEquipped(EntityPlayer player, Item item) {
		return BaublesApi.isBaubleEquipped(player, item) >= 0;
	}
	
	/**
	 * Returns a list of adornment stacks equipped of the given types.
	 *
	 * @param player The player whose inventory is to be checked.
	 */
	public static List<Adornment> getEquippedAdornments(EntityPlayer player) {
		List<Adornment> adornments = new ArrayList<>();

		// check each Baubles slot
		BAUBLES_SLOTS.forEach(slot -> {
			ItemStack stack = BaublesApi.getBaublesHandler(player).getStackInSlot(slot);
			if (stack.getItem() instanceof Adornment)
				adornments.add((Adornment) stack.getItem());
		});
		return adornments;
	}

	/**
	 * 
	 * @param player
	 * @param slot
	 * @return
	 */
	public static ItemStack getStackInSlot(EntityPlayer player, int slot) {
		return BaublesApi.getBaublesHandler(player).getStackInSlot(slot);
	}
	
	/**
	 * Bauble Capability Provider
	 */
	@SuppressWarnings("unchecked")
	public static class BaubleProvider implements ICapabilityProvider {

		protected BaubleType type;

		public BaubleProvider(AdornmentType adornmntType){
			this.type = TREASURE_BAUBLE_TYPE_MAP.get(adornmntType);
		}

		@Override
		public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing){
			return capability == BaublesCapabilities.CAPABILITY_ITEM_BAUBLE;
		}

		@Override
		public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing){
			// this lambda expression is an implementation of the entire IBauble interface
			return hasCapability(capability, facing) ? (T)(IBauble)itemStack -> type : null;
		}
	}
	
	/**
	 * Sub-class Bauble Capability Provider to include required Adornment capabilities (CharmInventoryCapability)
	 */
	public static class BaubleAdornmentCapabilityProvider extends BaubleProvider implements ICapabilitySerializable<NBTTagCompound> {
//		private final IMagicsInventoryCapability magicsCap;
		private final ICharmableCapability charmableCap;
		private final IRunestonesCapability runestonesCap;
		private final IDurabilityCapability durabilityCap;
		// TODO add IPouchableCapability
		
		private AdornmentCapabilityProvider adornmentCapabilityProvider;
		
		/**
		 * 
		 * @param adornmntType
		 */
		public BaubleAdornmentCapabilityProvider(AdornmentType adornmentType) {
			super(adornmentType);
//			charm = new CharmInventoryCapability();
//			this.magicsCap = new MagicsInventoryCapability(1, 1, 1);
			this.charmableCap = new CharmableCapability(0, 0, 0);
//			this.runestonesCap = new RunestonesCapability(magicsCap);
			this.runestonesCap = new RunestonesCapability(0, 0, 0);
			this.durabilityCap = new DurabilityCapability();
			
			this.adornmentCapabilityProvider = new AdornmentCapabilityProvider(charmableCap, runestonesCap, durabilityCap);
		}
		
		/**
		 * 
		 * @param charmable
		 */
//		public AdornmentProvider(AdornmentType adornmentType, ICharmInventoryCapability capability) {
//			super(adornmentType);
//			charm = capability;
//		}
		
		public BaubleAdornmentCapabilityProvider(AdornmentType adornmentType, ICharmableCapability charmable) {
			super(adornmentType);
//			this.magicsCap = charmable.getMagicsCap();
			this.charmableCap = charmable;
//			this.runestonesCap = new RunestonesCapability(magicsCap);
			this.runestonesCap = new RunestonesCapability(0, 0, 0);
			this.durabilityCap = new DurabilityCapability();
			
			this.adornmentCapabilityProvider = new AdornmentCapabilityProvider(charmableCap, runestonesCap, durabilityCap);
		}
		
		public BaubleAdornmentCapabilityProvider(AdornmentType adornmentType, ICharmableCapability charmable, IDurabilityCapability durability) {
			super(adornmentType);
//			this.magicsCap = charmable.getMagicsCap();
			this.charmableCap = charmable;
//			this.runestonesCap = new RunestonesCapability(magicsCap);
			this.runestonesCap = new RunestonesCapability(0, 0, 0);
			this.durabilityCap = durability;
			
			this.adornmentCapabilityProvider = new AdornmentCapabilityProvider(charmableCap, runestonesCap, durabilityCap);
		}
		
		public BaubleAdornmentCapabilityProvider(AdornmentType adornmentType, ICharmableCapability charmable, IRunestonesCapability runestonesCapability, IDurabilityCapability durability) {
			super(adornmentType);
//			this.magicsCap = charmable.getMagicsCap();
			this.charmableCap = charmable;
			this.runestonesCap = runestonesCapability;
			this.durabilityCap = durability;
			
			this.adornmentCapabilityProvider = new AdornmentCapabilityProvider(charmableCap, runestonesCap, durabilityCap);
		}
		
		
		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
			if (capability == BaublesCapabilities.CAPABILITY_ITEM_BAUBLE) {
				return true;
			}
			else if (adornmentCapabilityProvider.hasCapability(capability, facing)) {
				return true;
			}
			return false;
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
			if (capability == BaublesCapabilities.CAPABILITY_ITEM_BAUBLE) {
				return hasCapability(capability, facing) ? (T)(IBauble)itemStack -> type : null;
			}
			else {
				return adornmentCapabilityProvider.getCapability(capability, facing);
			}
		}
		
		@Override
		public NBTTagCompound serializeNBT() {
			return adornmentCapabilityProvider.serializeNBT();
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt) {
			adornmentCapabilityProvider.deserializeNBT(nbt);
		}
	}
}
