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
package com.someguyssoftware.treasure2.inventory;

import static com.someguyssoftware.treasure2.capability.TreasureCapabilities.CHARMABLE;
import static com.someguyssoftware.treasure2.capability.TreasureCapabilities.RUNESTONES;

import java.util.Optional;
import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;

import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.adornment.AdornmentSize;
import com.someguyssoftware.treasure2.adornment.TreasureAdornments;
import com.someguyssoftware.treasure2.block.TreasureBlocks;
import com.someguyssoftware.treasure2.capability.ICharmableCapability;
import com.someguyssoftware.treasure2.capability.IRunestonesCapability;
import com.someguyssoftware.treasure2.capability.InventoryType;
import com.someguyssoftware.treasure2.capability.TreasureCapabilities;
import com.someguyssoftware.treasure2.charm.CharmEntity;
import com.someguyssoftware.treasure2.charm.ICharmEntity;
import com.someguyssoftware.treasure2.charm.TreasureCharmRegistry;
import com.someguyssoftware.treasure2.enums.AdornmentType;
import com.someguyssoftware.treasure2.item.Adornment;
import com.someguyssoftware.treasure2.item.CharmItem;
import com.someguyssoftware.treasure2.item.RunestoneItem;
import com.someguyssoftware.treasure2.item.TreasureItems;
import com.someguyssoftware.treasure2.item.TreasureToolItem;
import com.someguyssoftware.treasure2.material.TreasureCharmableMaterials;
import com.someguyssoftware.treasure2.runestone.IRunestoneEntity;
import com.someguyssoftware.treasure2.runestone.TreasureRunes;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

/**
 * **** Container Slots ****
 * 0 = adornment input
 * 1 = +charm input
 * 2 = adornment + charm output
 * 3-6 = existing charm input (read-only)
 * 7-10 = charm modifier input
 * 11-14 = modified charm output
 * 15 = rune input
 * 16 = rune remove input
 * 17 = modified rune output
 * 
 * **** Input Slots ****
 * 
 * **** Output Slots ****
 * 0 = adornment + charm output
 * 1-4 = modified charm output
 * 5 = modified rune output
 * 
 * @param playerIn
 * @return
 */
public class CharmingTableContainer extends Container {

	// TODO redo this into several inventories for each type of input/output

	private IInventory inputSlots;
	private IInventory outputSlot;

	private IInventory charmInputSlots1;
	private IInventory charmInputSlots2;
	private IInventory[] charmOutputSlots;

	/*
	 * NOTE separated rune input slots into 2 inventories since the first is read-only and doesn't update the matrix on change.
	 */
	private IInventory runeInputSlot1;
	private IInventory runeInputSlot2;
	private IInventory runeOutputSlot;

	private BlockPos selfPosition; // ?
	private World world;
	public int maximumCost;
	public int materialCost;

	/**
	 * Client-side constructor
	 */
	public CharmingTableContainer(InventoryPlayer playerInventory, World world, EntityPlayer player) {
		this(playerInventory, world, BlockPos.ORIGIN, player);
	}

	/**
	 * 
	 * @param playerInventory
	 * @param world
	 * @param pos
	 * @param player
	 */
	public CharmingTableContainer(InventoryPlayer playerInventory, World world, BlockPos pos, EntityPlayer player) {
		try {
			setupSlots();
			setupCharmSlots();
			setupRuneSlots();
		}
		catch(Exception e) {
			Treasure.logger.debug("error:", e);
		}

		this.selfPosition = pos;
		this.world = world;

		// add player slots (0-8)
		for (int xIndex = 0; xIndex < 9; ++xIndex) {
			this.addSlotToContainer(new Slot(playerInventory, xIndex, 8 + xIndex * 18, 232));
		}

		// add inventory slots (9-26)
		for (int yIndex = 0; yIndex < 3; ++yIndex) {
			for (int xIndex = 0; xIndex < 9; ++xIndex) {
				this.addSlotToContainer(
						new Slot(playerInventory, xIndex + yIndex * 9 + 9, 8 + xIndex * 18, 174 + yIndex * 18));
			}
		}
	}

	/**
	 * 
	 */
	private void setupSlots() {
		this.outputSlot = new InventoryCraftResult();

		// initialize slots
		this.inputSlots = new InventoryBasic("Repair", true, 2) {
			@Override
			public void markDirty() {
				super.markDirty();
				CharmingTableContainer.this.onCraftMatrixChanged(this);
			}
		};

		// adornment input slot
		this.addSlotToContainer(new Slot(this.inputSlots, 0, 27, 18) {
			@Override
			public boolean isItemValid(ItemStack stack) {
				return (stack.getItem() instanceof Adornment);
			}
		});

		// charm to add input slot
		this.addSlotToContainer(new Slot(this.inputSlots, 1, 76, 18) {
			@Override
			public boolean isItemValid(ItemStack stack) {
				return (stack.getItem() instanceof CharmItem || stack.getItem() instanceof RunestoneItem);
			}
		});

		// adornment + charm output slot
		this.addSlotToContainer(new Slot(this.outputSlot, 2, 134, 18) { //2
			// make the slot read-only
			@Override
			public boolean isItemValid(ItemStack stack) {
				return false;
			}

			@Override
			public boolean canTakeStack(EntityPlayer player) {
				return (player.capabilities.isCreativeMode
						|| player.experienceLevel >= CharmingTableContainer.this.maximumCost)
						&& CharmingTableContainer.this.maximumCost > 0 && this.getHasStack();
			}

			@Override
			public ItemStack onTake(EntityPlayer player, ItemStack stack) {
				return myOnTake(player, stack);
			}
		});
	}

	/**
	 * 
	 */
	private void setupCharmSlots() {
		this.charmInputSlots1 = new InventoryBasic("Charm", true, 4);
		this.charmInputSlots2 = new InventoryBasic("Charm2", true, 4) {
			@Override
			public void markDirty() {
				super.markDirty();
				CharmingTableContainer.this.onCraftMatrixChanged(this);
			}
		};

		// current charm/runestone input slots - read only
		for (int i = 0; i < 4; i++) {
			this.addSlotToContainer(new Slot(this.charmInputSlots1, i, 27, 51 + (i * 19)) {
				@Override
				public boolean isItemValid(ItemStack stack) {
					return false;
				}
				@Override
				public boolean canTakeStack(EntityPlayer player) {
					return false;
				}
			});
		}
		// charm modifier (remove) input slot
		for (int i = 0; i < 4; i++) {
			this.addSlotToContainer(new Slot(this.charmInputSlots2, i, 76, 51 + (i * 19)) {
				@Override
				public boolean isItemValid(ItemStack stack) {
					return (stack.getItem() == TreasureItems.TREASURE_TOOL
							|| 	TreasureCharmableMaterials.isSourceItemRegistered(stack.getItem().getRegistryName()));
				}
			});
		}
		// charm output slots
		this.charmOutputSlots = new InventoryCraftResult[4];
		for (int i = 0; i < 4; i++) {
			this.charmOutputSlots[i] = new InventoryCraftResult();
			this.addSlotToContainer(new Slot(this.charmOutputSlots[i], 0, 134, 51 + (i * 19)) {
				@Override
				public boolean isItemValid(ItemStack stack) {
					return false;
				}

				@Override
				public boolean canTakeStack(EntityPlayer player) {
					return (player.capabilities.isCreativeMode
							|| player.experienceLevel >= CharmingTableContainer.this.maximumCost)
							&& CharmingTableContainer.this.maximumCost > 0 && this.getHasStack();
				}

				@Override
				public ItemStack onTake(EntityPlayer player, ItemStack stack) {
					CharmingTableContainer.this.charmInputSlots2.setInventorySlotContents(this.slotNumber, ItemStack.EMPTY);
					return myOnTake(player, stack);
				}
			});
		}
	}

	/**
	 * 
	 */
	private void setupRuneSlots() {
		this.runeOutputSlot = new InventoryCraftResult();

		// initialize slots
		this.runeInputSlot1 = new InventoryBasic("Rune", true, 1);
		this.runeInputSlot2 = new InventoryBasic("Rune2", true, 1) {
			@Override
			public void markDirty() {
				super.markDirty();
				CharmingTableContainer.this.onCraftMatrixChanged(this);
			}
		};

		// rune input slot
		this.addSlotToContainer(new Slot(this.runeInputSlot1, 0, 27, 142) { //15
			@Override
			public boolean isItemValid(ItemStack stack) {
				return false;
			}
			@Override
			public boolean canTakeStack(EntityPlayer player) {
				return false;
			}
		});

		// rune modifier (ie add/remove)
		this.addSlotToContainer(new Slot(this.runeInputSlot2, 0, 76, 142) { //16
			@Override
			public boolean isItemValid(ItemStack stack) {
				return stack.getItem() == TreasureItems.TREASURE_TOOL
						|| 	TreasureCharmableMaterials.isSourceItemRegistered(stack.getItem().getRegistryName());
			}
		});

		// rune output
		this.addSlotToContainer(new Slot(this.runeOutputSlot, 0, 134, 142) { //17
			// make the slot read-only
			@Override
			public boolean isItemValid(ItemStack stack) {
				return false;
			}

			@Override
			public boolean canTakeStack(EntityPlayer player) {
				return (player.capabilities.isCreativeMode
						|| player.experienceLevel >= CharmingTableContainer.this.maximumCost)
						&& CharmingTableContainer.this.maximumCost > 0 && this.getHasStack();
			}

			@Override
			public ItemStack onTake(EntityPlayer player, ItemStack stack) {
				// clear the runestone secondary input
				CharmingTableContainer.this.runeInputSlot2.clear();
				return myOnTake(player, stack);
			}
		});
	}

	/**
	 * 
	 * @param player
	 * @param stack
	 * @return
	 */
	public ItemStack myOnTake(EntityPlayer player, ItemStack stack) {
		if (!player.capabilities.isCreativeMode) {
			player.addExperienceLevel(-CharmingTableContainer.this.maximumCost);
		}

		clearAllSlots();
		CharmingTableContainer.this.maximumCost = 0;
		playUseSound();
		return stack;
	}

	/**
	 * NOTE don't clear the charm, runestone secondary inputs en masse.
	 */
	protected void clearAllSlots() {
		CharmingTableContainer.this.inputSlots.clear();
		CharmingTableContainer.this.charmInputSlots1.clear();
		CharmingTableContainer.this.runeInputSlot1.clear();
		CharmingTableContainer.this.outputSlot.clear();
		for (int i = 0; i < CharmingTableContainer.this.charmOutputSlots.length; i++) {
			Treasure.logger.debug("clearing charm input slots[{}]...", i);
			CharmingTableContainer.this.charmOutputSlots[i].clear();
		}
		CharmingTableContainer.this.runeOutputSlot.clear();
	}

	/**
	 * 
	 */
	protected void playUseSound() {
		world.playSound((EntityPlayer) null, selfPosition.getX() + 0.5, selfPosition.getY() + 0.5D, selfPosition.getZ() + 0.5, SoundEvents.BLOCK_ANVIL_USE,
				SoundCategory.BLOCKS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
	}

	@Override
	public void onCraftMatrixChanged(IInventory inventory) {
		super.onCraftMatrixChanged(inventory);
		if (inventory == this.inputSlots || inventory == this.charmInputSlots1 || inventory == this.runeInputSlot2) {
			this.updateOutput();
		}
	}

	/**
	 * Main method
	 */
	public void updateOutput() {
		this.maximumCost = 0;

		// check if the adornment slot has an item
		ItemStack itemStack = this.inputSlots.getStackInSlot(0);
		if (itemStack.isEmpty()) {
			this.outputSlot.setInventorySlotContents(0, ItemStack.EMPTY);
			// clear charms
			for (int i = 0; i < 4; i++) {
				this.charmInputSlots1.setInventorySlotContents(i, ItemStack.EMPTY);
				this.charmOutputSlots[i].setInventorySlotContents(0, ItemStack.EMPTY);
			}
			// clear runestone
			this.runeInputSlot1.setInventorySlotContents(0, ItemStack.EMPTY);
			this.runeOutputSlot.setInventorySlotContents(0, ItemStack.EMPTY);
		}
		else {
			/*
			 * setup
			 */
			this.maximumCost = 1;

			// get the inputs
			ItemStack itemStack2 = this.inputSlots.getStackInSlot(1);
			if (itemStack2.isEmpty()) {
				this.outputSlot.setInventorySlotContents(0, ItemStack.EMPTY);
			}

			// populate the charms and runestone if any
			ICharmableCapability charmableCap = itemStack.getCapability(TreasureCapabilities.CHARMABLE, null);
			if (charmableCap != null) {
				if (charmableCap.getCharmEntities().get(InventoryType.SOCKET).size() > 0) {
					// less than 5 socket charms can fit into container gui
					if (charmableCap.getCharmEntities().get(InventoryType.SOCKET).size() <= 4) {
						int index = 0;
						ResourceLocation resource = new ResourceLocation(Treasure.MODID, "gold_charm");
						for (ICharmEntity charm : charmableCap.getCharmEntities().get(InventoryType.SOCKET)) {
							// TODO calculate the correct charm to use
							this.charmInputSlots1.setInventorySlotContents(index,  new ItemStack(TreasureItems.ITEMS.get(resource)));
							index++;
						}
					}
					else {
						// TODO select 4 charms with the least charge
					}
				}
			}

			// populate the runestones
			IRunestonesCapability runestoneCap = itemStack.getCapability(RUNESTONES, null);
			if (runestoneCap != null) {
				Treasure.logger.debug("populating runestone has cap");
				if (runestoneCap.getEntities(InventoryType.SOCKET).size() > 0) {
					Treasure.logger.debug("populating runestone has runestone");
					Optional<Item> item = TreasureRunes.getItem(runestoneCap.getEntities(InventoryType.SOCKET).get(0).getRunestone());
					// NOTE if item isn't found, something went wrong
					if (item.isPresent()) {
						Treasure.logger.debug("populating runestone has item -> {}", item.get().getRegistryName());
						this.runeInputSlot1.setInventorySlotContents(0, new ItemStack(item.get()));
					}
				}
			}

			for (int i = 0; i < 4; i++) {
				if (this.charmInputSlots1.getStackInSlot(i).isEmpty() ||
						this.charmInputSlots2.getStackInSlot(i).isEmpty()) {
					this.charmOutputSlots[i].setInventorySlotContents(0, ItemStack.EMPTY);
				}
			}
			
			ItemStack runeStack = this.runeInputSlot1.getStackInSlot(0);
			if (runeStack.isEmpty()) {
				this.runeOutputSlot.setInventorySlotContents(0, ItemStack.EMPTY);
			}
			Treasure.logger.debug("rune slot1 -> {}", runeStack.getDisplayName());
			ItemStack runeStack2 = this.runeInputSlot2.getStackInSlot(0);
			if (runeStack2.isEmpty()) {
				this.runeOutputSlot.setInventorySlotContents(0, ItemStack.EMPTY);
			}
			Treasure.logger.debug("rune slot2 -> {}", runeStack2.getDisplayName());

			// add gem check
			if (itemStack.hasCapability(CHARMABLE, null) && itemStack2.hasCapability(CHARMABLE, null)
					&& itemStack.getCapability(CHARMABLE, null).isSocketable()
					&& itemStack2.getCapability(CHARMABLE, null).isBindable()) {

				ICharmableCapability cap = itemStack.getCapability(CHARMABLE, null);
				// check that there is room to add charms
				if (cap.getCharmEntities().get(InventoryType.SOCKET).size() < cap.getMaxSocketSize()) {
					// build the output item, add the charm to the adornment
					Optional<ItemStack> outStack = TreasureAdornments.transferCapabilities(itemStack2, itemStack, InventoryType.INNATE, InventoryType.SOCKET);
					if (outStack.isPresent()) {
						if (outStack.get().hasCapability(RUNESTONES, null)) {
							outStack.get().getCapability(RUNESTONES, null).getEntities(InventoryType.SOCKET).forEach(entity -> {
								entity.getRunestone().apply(outStack.get(), entity);
							});
						}
						this.outputSlot.setInventorySlotContents(0, outStack.get());    					
					}
				}            
			}
			// add runestone check
			else if (itemStack.hasCapability(RUNESTONES, null) && itemStack2.hasCapability(RUNESTONES, null)
					&& itemStack.getCapability(RUNESTONES, null).isSocketable()
					&& itemStack2.getCapability(RUNESTONES, null).isBindable()) {

				Treasure.logger.debug("adding runestone check");
				IRunestonesCapability cap = itemStack.getCapability(RUNESTONES, null);
				// check that there is room to add charms
				if (cap.getEntities(InventoryType.SOCKET).size() < cap.getMaxSize(InventoryType.SOCKET)) {
					Optional<ItemStack> stack = TreasureAdornments.transferCapabilities(itemStack2, itemStack, InventoryType.INNATE, InventoryType.SOCKET);
					if (stack.isPresent()) {
						stack.get().getCapability(RUNESTONES, null).getEntities(InventoryType.SOCKET).forEach(entity -> {
							entity.getRunestone().apply(stack.get(), entity);
						});
						this.outputSlot.setInventorySlotContents(0, stack.get());    	
					}
				}
			}
			// remove charm check
			else if (!this.charmInputSlots1.isEmpty() && !this.charmInputSlots2.isEmpty()) {
				// now check if the inputs line up
				for (int i = 0; i < 4; i++) {
					if (!this.charmInputSlots1.getStackInSlot(i).isEmpty() &&
							!this.charmInputSlots2.getStackInSlot(i).isEmpty()) {
						// make a copy of the adornment stack
						ItemStack stack = itemStack.copy();
						ItemStack newStack = TreasureAdornments.copyStack(itemStack, stack);
						ICharmableCapability cap = newStack.getCapability(CHARMABLE, null);
						// remove the charm
						cap.remove(InventoryType.SOCKET, i);
						// apply any runes
						if (newStack.hasCapability(RUNESTONES, null)) {
							newStack.getCapability(RUNESTONES, null).getEntities(InventoryType.SOCKET).forEach(entity -> {
								entity.getRunestone().apply(newStack, entity);
							});
						}
						this.charmOutputSlots[i].setInventorySlotContents(0, newStack);
					}
				}
			}
			// remove runestone check
			else if (runeStack != ItemStack.EMPTY && (runeStack2.getItem() == TreasureItems.TREASURE_TOOL)) {
				// make a copy of the adornment stack
				ItemStack stack = itemStack.copy();
				stack = TreasureAdornments.copyStack(itemStack, stack);
				IRunestonesCapability cap = stack.getCapability(RUNESTONES, null);
				if (cap != null) {
					// get the rune entity
					IRunestoneEntity entity = runeStack.getCapability(RUNESTONES, null).getEntities(InventoryType.INNATE).get(0);
					// remove the entity from adornment
					if (cap.remove(InventoryType.SOCKET, entity)) {
						// undo the effects
						entity.getRunestone().undo(stack, entity);
					}
				}				
				this.runeOutputSlot.setInventorySlotContents(0, stack);
			}
		}
	}

	@Override
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);

		if (!WorldInfo.isClientSide(world)) {
			this.clearContainer(player, this.world, this.inputSlots);
			// clear all the inventories
			clearAllSlots();
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		if (this.world.getBlockState(selfPosition).getBlock() != TreasureBlocks.CHARMING_TABLE) {
			return false;
		} else {
			return playerIn.getDistanceSq((double) this.selfPosition.getX() + 0.5D,
					(double) this.selfPosition.getY() + 0.5D, (double) this.selfPosition.getZ() + 0.5D) <= 64.0D;
		}
	}

	//	@Override
	//	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
	//		ItemStack itemstack = ItemStack.EMPTY;
	//		Slot slot = this.inventorySlots.get(index);
	//
	//		if (slot != null && slot.getHasStack()) {
	//			ItemStack itemstack1 = slot.getStack();
	//			itemstack = itemstack1.copy();
	//
	//			if (index == 2) {
	//				if (!this.mergeItemStack(itemstack1, 3, 39, true)) {
	//					return ItemStack.EMPTY;
	//				}
	//
	//				slot.onSlotChange(itemstack1, itemstack);
	//			} else if (index != 0 && index != 1) {
	//				if (index >= 3 && index < 39 && !this.mergeItemStack(itemstack1, 0, 2, false)) {
	//					return ItemStack.EMPTY;
	//				}
	//			} else if (!this.mergeItemStack(itemstack1, 3, 39, false)) {
	//				return ItemStack.EMPTY;
	//			}
	//
	//			if (itemstack1.isEmpty()) {
	//				slot.putStack(ItemStack.EMPTY);
	//			} else {
	//				slot.onSlotChanged();
	//			}
	//
	//			if (itemstack1.getCount() == itemstack.getCount()) {
	//				return ItemStack.EMPTY;
	//			}
	//
	//			slot.onTake(playerIn, itemstack1);
	//		}
	//
	//		return itemstack;
	//	}
}
