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

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.adornment.AdornmentSize;
import com.someguyssoftware.treasure2.adornment.TreasureAdornments;
import com.someguyssoftware.treasure2.block.TreasureBlocks;
import com.someguyssoftware.treasure2.capability.ICharmableCapability;
import com.someguyssoftware.treasure2.enums.AdornmentType;
import com.someguyssoftware.treasure2.item.Adornment;
import com.someguyssoftware.treasure2.item.CharmItem;
import com.someguyssoftware.treasure2.item.TreasureItems;
import com.someguyssoftware.treasure2.material.TreasureCharmableMaterials;

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
public class CharmingBenchContainer extends Container {
	private static final int MAIN_OUTPUT_SLOT = 0;
	private static final int RUNE_OUTPUT_SLOT = 0;
	
	// TODO redo this into several inventories for each type of input/output
	
	private IInventory inputSlots;
	private IInventory outputSlot;
	
	private IInventory charmInputSlots;
	private IInventory[] charmOutputSlots;
	
	private IInventory runeInputSlots;
	private IInventory runeOutputSlot;
	
	private BlockPos selfPosition; // ?
	private World world;
	public int maximumCost;
	public int materialCost;

	/**
	 * Client-side constructor
	 */
	public CharmingBenchContainer(InventoryPlayer playerInventory, World world, EntityPlayer player) {
		this(playerInventory, world, BlockPos.ORIGIN, player);
	}

	/**
	 * 
	 * @param playerInventory
	 * @param world
	 * @param pos
	 * @param player
	 */
	public CharmingBenchContainer(InventoryPlayer playerInventory, World world, BlockPos pos, EntityPlayer player) {
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
			
		// rune modifier input slot
		
//		this.addSlotToContainer(new Slot(this.removeGemOutputSlot, 5, 145, 37) {
//			@Override
//			public boolean isItemValid(ItemStack stack) {
//				return false;
//			}
//			@Override
//			public boolean canTakeStack(EntityPlayer player) {
//				return (player.capabilities.isCreativeMode
//						|| player.experienceLevel >= CharmingBenchContainer.this.maximumCost)
//						&& CharmingBenchContainer.this.maximumCost > 0 && this.getHasStack();
//			}
//			@Override
//			public ItemStack onTake(EntityPlayer player, ItemStack stack) {
//				if (!player.capabilities.isCreativeMode) {
//					player.addExperienceLevel(-CharmingBenchContainer.this.maximumCost);
//				}
//				// clear input slots
//				CharmingBenchContainer.this.inputSlots.setInventorySlotContents(2, ItemStack.EMPTY);
//				CharmingBenchContainer.this.maximumCost = 0;
//				playUseSound();
//				return stack;
//			}
//		});
//		this.addSlotToContainer(new Slot(this.removeAdornmentOutputSlot, 6, 145, 56) {
//			@Override
//			public boolean isItemValid(ItemStack stack) {
//				return false;
//			}
//			@Override
//			public boolean canTakeStack(EntityPlayer player) {
//				return (player.capabilities.isCreativeMode
//						|| player.experienceLevel >= CharmingBenchContainer.this.maximumCost)
//						&& CharmingBenchContainer.this.maximumCost > 0 && this.getHasStack();
//			}
//			@Override
//			public ItemStack onTake(EntityPlayer player, ItemStack stack) {
//				if (!player.capabilities.isCreativeMode) {
//					player.addExperienceLevel(-CharmingBenchContainer.this.maximumCost);
//				}
//				// clear input slots
//				CharmingBenchContainer.this.inputSlots.setInventorySlotContents(3, ItemStack.EMPTY);
//				CharmingBenchContainer.this.maximumCost = 0;
//				playUseSound();
//				return stack;
//			}
//		});
		
		// runestone output slot
		
		
		// add inventory slots
		for (int yIndex = 0; yIndex < 3; ++yIndex) {
			for (int xIndex = 0; xIndex < 9; ++xIndex) {
				this.addSlotToContainer(
						new Slot(playerInventory, xIndex + yIndex * 9 + 9, 8 + xIndex * 18, 84 + yIndex * 18));
			}
		}

		// add player slots
		for (int xIndex = 0; xIndex < 9; ++xIndex) {
			this.addSlotToContainer(new Slot(playerInventory, xIndex, 8 + xIndex * 18, 142));
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
				CharmingBenchContainer.this.onCraftMatrixChanged(this);
			}
		};
		
		// adornment input slot
		this.addSlotToContainer(new Slot(this.inputSlots, 0, 38, 18) {
			@Override
			public boolean isItemValid(ItemStack stack) {
				return (stack.getItem() instanceof Adornment);
			}
		});
	
		// charm to add input slot
		this.addSlotToContainer(new Slot(this.inputSlots, 1, 38, 18) {
			@Override
			public boolean isItemValid(ItemStack stack) {
				return (stack.getItem() instanceof CharmItem);
			}
		});
		
		// adornment + charm output slot
		this.addSlotToContainer(new Slot(this.outputSlot, MAIN_OUTPUT_SLOT, 145, 18) {
			// make the slot read-only
			@Override
			public boolean isItemValid(ItemStack stack) {
				return false;
			}

			@Override
			public boolean canTakeStack(EntityPlayer player) {
				return (player.capabilities.isCreativeMode
						|| player.experienceLevel >= CharmingBenchContainer.this.maximumCost)
						&& CharmingBenchContainer.this.maximumCost > 0 && this.getHasStack();
			}

			@Override
			public ItemStack onTake(EntityPlayer player, ItemStack stack) {
				if (!player.capabilities.isCreativeMode) {
					player.addExperienceLevel(-CharmingBenchContainer.this.maximumCost);
				}

				clearAllSlots(0);
				CharmingBenchContainer.this.maximumCost = 0;
				playUseSound();
				return stack;
			}
		});
	}

	private void setupCharmSlots() {
		this.charmInputSlots = new InventoryBasic("Charm", true, 8) {
			@Override
			public void markDirty() {
				super.markDirty();
				CharmingBenchContainer.this.onCraftMatrixChanged(this);
			}
		};
		
		// current charm/runestone input slots - read only
		for (int i = 0; i < 4; i++) {
			this.addSlotToContainer(new Slot(this.charmInputSlots, 0 + i, 38, 37 + (i * 18)) {
				@Override
				public boolean isItemValid(ItemStack stack) {
					return false;
				}
			});
		}
		// charm modifier input slot
		for (int i = 0; i < 4; i++) {
			this.addSlotToContainer(new Slot(this.charmInputSlots, 4 + i, 50, 37 + (i * 18)) {
				@Override
				public boolean isItemValid(ItemStack stack) {
					return (stack.getItem() instanceof CharmItem
							|| stack.getItem() == TreasureItems.TREASURE_TOOL
							|| 	TreasureCharmableMaterials.isSourceItemRegistered(stack.getItem().getRegistryName()));
				}
			});
		}
		// charm output slots
		this.charmOutputSlots = new InventoryCraftResult[4];
		for (int i = 0; i < 4; i++) {
			this.charmOutputSlots[i] = new InventoryCraftResult();
			this.addSlotToContainer(new Slot(this.charmOutputSlots[i], 0, 50, 37 + (i * 18)) {
				@Override
				public boolean isItemValid(ItemStack stack) {
					return (stack.getItem() instanceof CharmItem
							|| stack.getItem() == TreasureItems.TREASURE_TOOL
							|| 	TreasureCharmableMaterials.isSourceItemRegistered(stack.getItem().getRegistryName()));
				}
			});
		}
	}
	
	private void setupRuneSlots() {
		// TODO Auto-generated method stub
		
	}
	
	protected void clearAllSlots(int inventoryIndex) {
//		CharmingBenchContainer.this.inputSlots.clear();
//		for (int i = 0; i < CharmingBenchContainer.this.outputSlots.length; i++) {
//			if (i != inventoryIndex) {
//				CharmingBenchContainer.this.inputSlots.setInventorySlotContents(i, ItemStack.EMPTY);
//			}
//		}
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
		if (inventory == this.inputSlots) {
			this.updateOutput();
		}
	}

	/**
	 * Main method
	 */
	public void updateOutput() {
		this.maximumCost = 0;
		// update to row (adornment + charm row)
		ItemStack itemStack = this.inputSlots.getStackInSlot(0);
        if (itemStack.isEmpty()) {
            this.outputSlot.setInventorySlotContents(0, ItemStack.EMPTY);
        }
        else {
            this.maximumCost = 1;
            ItemStack itemStack2 = this.inputSlots.getStackInSlot(1);
            
            // add gem check
            if ((itemStack.getItem() instanceof Adornment) 
    				&&	itemStack.hasCapability(CHARMABLE, null)
    				&&	itemStack2.getItem() instanceof CharmItem) {
            	
            	// TODO check that there is room to add charms

    			//Treasure.logger.debug("left is adornment and right is gem!");
    			ICharmableCapability cap = itemStack.getCapability(CHARMABLE, null);
    			if (cap.getCharmEntities().size() < cap.getMaxSocketSize()) {
    				this.materialCost = 1;

    				// build the output item, add the charm to the adornment

    			}
    		}            
        }
        
//        ItemStack removeGemItemStack = this.inputSlots.getStackInSlot(2);
//        if (removeGemItemStack.isEmpty()) {
//            this.removeGemOutputSlot.setInventorySlotContents(0, ItemStack.EMPTY);
//        }
//        // is the input an adornment with a gem/source
//        else if ((removeGemItemStack.getItem() instanceof Adornment) 
//				&&	removeGemItemStack.hasCapability(CHARMABLE, null)
//				&&	TreasureCharmableMaterials.isSourceItemRegistered(
//						removeGemItemStack.getCapability(CHARMABLE, null).getSourceItem())) {
//        	
//        	this.maximumCost = 1;
//        	// get the original base adornment (no gem)
//        	Adornment adornment = (Adornment)removeGemItemStack.getItem();
//        	Optional<Adornment> baseAdornment = TreasureAdornments.get(
//        			adornment.getType(), 
//        			adornment.getSize(), 
//        			removeGemItemStack.getCapability(CHARMABLE, null).getBaseMaterial(),
//        			Items.AIR.getRegistryName());
//        	if (baseAdornment.isPresent()) {
//        		this.removeGemOutputSlot.setInventorySlotContents(0, new ItemStack(baseAdornment.get()));
//        	}
//        }
        
//        ItemStack removeAdornmentItemStack = this.inputSlots.getStackInSlot(3);
//        if (removeAdornmentItemStack.isEmpty()) {
//            this.removeAdornmentOutputSlot.setInventorySlotContents(0, ItemStack.EMPTY);
//        }
//        // is the input an adornment with a gem/source
//        else if ((removeAdornmentItemStack.getItem() instanceof Adornment) 
//				&&	removeAdornmentItemStack.hasCapability(CHARMABLE, null)
//				&&	TreasureCharmableMaterials.isSourceItemRegistered(
//						removeAdornmentItemStack.getCapability(CHARMABLE, null).getSourceItem())) {
//        	this.maximumCost = 1;
//        	// get the original gem
//        	Item gem = ForgeRegistries.ITEMS.getValue(removeAdornmentItemStack.getCapability(CHARMABLE, null).getSourceItem());
//        	// update the output
//        	this.removeAdornmentOutputSlot.setInventorySlotContents(0, new ItemStack(gem));
//        }
	}

	@Override
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);

		if (!WorldInfo.isClientSide(world)) {
			this.clearContainer(player, this.world, this.inputSlots);
			// TODO clear all the inventories
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		if (this.world.getBlockState(selfPosition).getBlock() != TreasureBlocks.JEWELER_BENCH) {
			return false;
		} else {
			return playerIn.getDistanceSq((double) this.selfPosition.getX() + 0.5D,
					(double) this.selfPosition.getY() + 0.5D, (double) this.selfPosition.getZ() + 0.5D) <= 64.0D;
		}
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (index == 2) {
				if (!this.mergeItemStack(itemstack1, 3, 39, true)) {
					return ItemStack.EMPTY;
				}

				slot.onSlotChange(itemstack1, itemstack);
			} else if (index != 0 && index != 1) {
				if (index >= 3 && index < 39 && !this.mergeItemStack(itemstack1, 0, 2, false)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.mergeItemStack(itemstack1, 3, 39, false)) {
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}

			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(playerIn, itemstack1);
		}

		return itemstack;
	}
}
