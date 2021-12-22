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
import com.someguyssoftware.treasure2.capability.CharmInventoryCapability;
import com.someguyssoftware.treasure2.capability.ICharmInventoryCapability;
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
		TREASURE_BAUBLE_TYPE_MAP.put(AdornmentType.AMULET, BaubleType.AMULET);
		TREASURE_BAUBLE_TYPE_MAP.put(AdornmentType.BRACELET, BaubleType.BODY);
		TREASURE_BAUBLE_TYPE_MAP.put(AdornmentType.POCKET, BaubleType.BODY);

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
	 * @return True if the given item is equipped in a bauble slot, false otherwise.
	 */
	public static boolean isBaubleEquipped(EntityPlayer player, Item item) {
		return BaublesApi.isBaubleEquipped(player, item) >= 0;
	}
	
	/**
	 * Returns a list of artefact stacks equipped of the given types. <i>This method does not check whether artefacts
	 * have been disabled in the config! {ItemNewArtefact#getActiveArtefacts(EntityPlayer, ItemNewArtefact.AdditionalType...)}
	 * should be used instead of this method in nearly all cases.</i>
	 *
	 * @param player The player whose inventory is to be checked.
	 * @param types  Zero or more artefact types to check for. If omitted, searches for all types.
	 * @return A list of equipped artefact {@code ItemStacks}.
	 */
	// This could return all ItemStacks, but if an artefact type is given this doesn't really make sense.
	public static List<Adornment> getEquippedAdornments(EntityPlayer player) {

		List<Adornment> adornments = new ArrayList<>();


		// TODO check each Baubles slot
		//			for (int slot : ARTEFACT_TYPE_MAP.get(type).getValidSlots()) {
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
	public static class AdornmentProvider extends BaubleProvider implements ICapabilitySerializable<NBTTagCompound> {
		private final ICharmInventoryCapability charm;
		
		/**
		 * 
		 * @param adornmntType
		 */
		public AdornmentProvider(AdornmentType adornmentType) {
			super(adornmentType);
			charm = new CharmInventoryCapability();
		}
		
		/**
		 * 
		 * @param capability
		 */
		public AdornmentProvider(AdornmentType adornmentType, ICharmInventoryCapability capability) {
			super(adornmentType);
			charm = capability;
		}
		
		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
			if (capability == BaublesCapabilities.CAPABILITY_ITEM_BAUBLE) {
				return true;
			}
			else if (capability == TreasureCapabilities.CHARM_INVENTORY) {
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
			else if (capability == TreasureCapabilities.CHARM_INVENTORY) {
				return TreasureCapabilities.CHARM_INVENTORY.cast(this.charm);
			}
			return null;
		}

		@Override
		public NBTTagCompound serializeNBT() {
			NBTTagCompound tag = (NBTTagCompound)TreasureCapabilities.CHARM_INVENTORY.getStorage().writeNBT(TreasureCapabilities.CHARM_INVENTORY, charm, null);
			return tag;
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt) {
			TreasureCapabilities.CHARM_INVENTORY.getStorage().readNBT(TreasureCapabilities.CHARM_INVENTORY, charm, null, nbt);
		}
	}
}
