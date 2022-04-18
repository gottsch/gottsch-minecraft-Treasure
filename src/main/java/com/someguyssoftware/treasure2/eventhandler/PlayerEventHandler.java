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
package com.someguyssoftware.treasure2.eventhandler;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import com.someguyssoftware.gottschcore.mod.IMod;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.charm.CharmContext;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.item.IWishable;
import com.someguyssoftware.treasure2.item.TreasureItems;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.GetCollisionBoxesEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

/**
 * @author Mark Gottschling on Apr 26, 2018
 *
 */
public class PlayerEventHandler {
	private static final String FIRST_JOIN_NBT_KEY = "treasure2.firstjoin";
	private static final String PATCHOULI_MODID = "patchouli";
	private static final String PATCHOULI_GUIDE_BOOK_ID = "patchouli:guide_book";
	private static final String PATCHOULI_GUIDE_TAG_ID = "patchouli:book";
	private static final String TREASURE2_GUIDE_TAG_VALUE = "treasure2:guide";

	// reference to the mod.
	private IMod mod;

	/**
	 * 
	 */
	public PlayerEventHandler(IMod mod) {
		setMod(mod);
	}

	@SubscribeEvent
	public void addToInventory(PlayerEvent.PlayerLoggedInEvent event) {
		// check if config is enabled
		if (!TreasureConfig.MOD.enableSpecialRewards) {
			return;
		}

		if (event.player.isCreative()) {
			return;
		}

		NBTTagCompound playerData = event.player.getEntityData();
		NBTTagCompound persistentNbt;
		if (!playerData.hasKey(EntityPlayer.PERSISTED_NBT_TAG)) {
			playerData.setTag(EntityPlayer.PERSISTED_NBT_TAG, (persistentNbt = new NBTTagCompound()));
		} else {
			persistentNbt = playerData.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
		}

		// check if during the correct timeframe for Gottsch's Ring of the Moon
		if (LocalDate.now().getMonth() != Month.DECEMBER || Year.now().getValue() != 2021) {
			return;			
		}
		else if (!persistentNbt.hasKey(FIRST_JOIN_NBT_KEY)) {
			persistentNbt.setBoolean(FIRST_JOIN_NBT_KEY, true);
			// add all items to players inventory on first join
			ItemStack ring = new ItemStack(TreasureItems.GOTTSCHS_RING_OF_MOON, 1);
			event.player.inventory.addItemStackToInventory(ring);
		}
	}

	//	TEMP remove until patchouli book is complete.
	//    @SubscribeEvent
	public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
		// check if config is enabled
		if (!TreasureConfig.MOD.enableStartingBook) {
			return;
		}

		if (event.player.isCreative()) {
			return;
		}

		NBTTagCompound data = event.player.getEntityData();
		NBTTagCompound persistent;
		if (!data.hasKey(EntityPlayer.PERSISTED_NBT_TAG)) {
			data.setTag(EntityPlayer.PERSISTED_NBT_TAG, (persistent = new NBTTagCompound()));
		} else {
			persistent = data.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
		}

		// check if the patchouli mod is installed
		if (Loader.isModLoaded(PATCHOULI_MODID)) {
			if (!persistent.hasKey(FIRST_JOIN_NBT_KEY)) {
				persistent.setBoolean(FIRST_JOIN_NBT_KEY, true);
				// create the book
				Item guideBook = Item.REGISTRY.getObject(new ResourceLocation(PATCHOULI_GUIDE_BOOK_ID));
				ItemStack stack = new ItemStack(guideBook);
				if (!stack.hasTagCompound()) {
					NBTTagCompound tag = new NBTTagCompound();
					tag.setString(PATCHOULI_GUIDE_TAG_ID, TREASURE2_GUIDE_TAG_VALUE);
					stack.setTagCompound(tag);
				}
				event.player.inventory.addItemStackToInventory(stack);
			}
		}
	}

	/**
	 * 
	 * @param event
	 */
	@SubscribeEvent
	public void onTossCoinEvent(ItemTossEvent event) {
		if (WorldInfo.isClientSide(event.getPlayer().world)) {
			return;
		}
		Treasure.LOGGER.debug("{} tossing item -> {}", event.getPlayer().getName(), event.getEntityItem().getItem().getDisplayName());
		Item item = event.getEntityItem().getItem().getItem();
		if (item instanceof IWishable) {
			ItemStack stack = event.getEntityItem().getItem();
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setString(IWishable.DROPPED_BY_KEY, EntityPlayer.getUUID(event.getPlayer().getGameProfile()).toString());
			stack.setTagCompound(nbt);			
		}		
	}
	
	// TODO add tooltip "wishable" to Diamond and Emerald when the Wishable strategy is created/cleaned up
	
	/**
	 * @return the mod
	 */
	public IMod getMod() {
		return mod;
	}

	/**
	 * @param mod the mod to set
	 */
	public void setMod(IMod mod) {
		this.mod = mod;
	}

	public enum CharmedType {
		CHARM,
		FOCUS,
		ADORNMENT
	}
}
