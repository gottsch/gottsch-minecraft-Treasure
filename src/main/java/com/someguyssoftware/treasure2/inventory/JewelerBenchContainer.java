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
import com.someguyssoftware.treasure2.adornment.TreasureAdornmentRegistry;
import com.someguyssoftware.treasure2.block.TreasureBlocks;
import com.someguyssoftware.treasure2.capability.ICharmableCapability;
import com.someguyssoftware.treasure2.enums.AdornmentType;
import com.someguyssoftware.treasure2.item.Adornment;
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
 * 
 * @param playerIn
 * @return
 */
public class JewelerBenchContainer extends Container {
	private final IInventory outputSlot;
	private final IInventory removeGemOutputSlot;
	private final IInventory removeAdornmentOutputSlot;
	private final IInventory inputSlots;
	private BlockPos selfPosition; // ?
	private World world;
	private EntityPlayer player;
	public int maximumCost;
	public int materialCost;
	private String repairedItemName;

	/**
	 * Client-side constructor
	 */
	public JewelerBenchContainer(InventoryPlayer playerInventory, World world, EntityPlayer player) {
		this(playerInventory, world, BlockPos.ORIGIN, player);
	}

	/**
	 * 
	 * @param playerInventory
	 * @param world
	 * @param pos
	 * @param player
	 */
	public JewelerBenchContainer(InventoryPlayer playerInventory, World world, BlockPos pos, EntityPlayer player) {
		this.outputSlot = new InventoryCraftResult();
		this.removeGemOutputSlot = new InventoryCraftResult();
		this.removeAdornmentOutputSlot = new InventoryCraftResult();

		this.inputSlots = new InventoryBasic("Repair", true, 4) {
			@Override
			public void markDirty() {
				super.markDirty();
				JewelerBenchContainer.this.onCraftMatrixChanged(this);
			}
		};

		this.selfPosition = pos;
		this.world = world;
		this.player = player;

		// add input slot(s)
		this.addSlotToContainer(new Slot(this.inputSlots, 0, 38, 18) {
			@Override
			public boolean isItemValid(ItemStack stack) {
				return (stack.getItem() instanceof Adornment);
			}
		});
		
		this.addSlotToContainer(new Slot(this.inputSlots, 1, 87, 18) {
			@Override
			public boolean isItemValid(ItemStack stack) {
				return (stack.getItem() instanceof Adornment || 
						TreasureCharmableMaterials.isSourceItemRegistered(stack.getItem().getRegistryName()));
			}
		});

		// keep adornment slot
		this.addSlotToContainer(new Slot(this.inputSlots, 2, 38, 37) {
			@Override
			public boolean isItemValid(ItemStack stack) {
				return (stack.getItem() instanceof Adornment);
			}
		});
		
		// remove gem input
		this.addSlotToContainer(new Slot(this.inputSlots, 3, 38, 56) {
			@Override
			public boolean isItemValid(ItemStack stack) {
				return (stack.getItem() instanceof Adornment);
			}
		});

		// add output slot(s)
		this.addSlotToContainer(new Slot(this.outputSlot, 4, 145, 18) {
			// make the slot read-only
			@Override
			public boolean isItemValid(ItemStack stack) {
				return false;
			}

			@Override
			public boolean canTakeStack(EntityPlayer player) {
				return (player.capabilities.isCreativeMode
						|| player.experienceLevel >= JewelerBenchContainer.this.maximumCost)
						&& JewelerBenchContainer.this.maximumCost > 0 && this.getHasStack();
			}

			@Override
			public ItemStack onTake(EntityPlayer player, ItemStack stack) {
				if (!player.capabilities.isCreativeMode) {
					player.addExperienceLevel(-JewelerBenchContainer.this.maximumCost);
				}

				JewelerBenchContainer.this.inputSlots.setInventorySlotContents(0, ItemStack.EMPTY);

				// reduce the size of the source item
				if (JewelerBenchContainer.this.materialCost > 0) {
					ItemStack itemStack = JewelerBenchContainer.this.inputSlots.getStackInSlot(1);

					if (!itemStack.isEmpty() && itemStack.getCount() > JewelerBenchContainer.this.materialCost) {
						itemStack.shrink(JewelerBenchContainer.this.materialCost);
						JewelerBenchContainer.this.inputSlots.setInventorySlotContents(1, itemStack);
					} else {
						JewelerBenchContainer.this.inputSlots.setInventorySlotContents(1, ItemStack.EMPTY);
					}
				} else {
					JewelerBenchContainer.this.inputSlots.setInventorySlotContents(1, ItemStack.EMPTY);
				}				
				JewelerBenchContainer.this.maximumCost = 0;
				playUseSound();
				return stack;
			}
		});
		
		this.addSlotToContainer(new Slot(this.removeGemOutputSlot, 5, 145, 37) {
			@Override
			public boolean isItemValid(ItemStack stack) {
				return false;
			}
			@Override
			public boolean canTakeStack(EntityPlayer player) {
				return (player.capabilities.isCreativeMode
						|| player.experienceLevel >= JewelerBenchContainer.this.maximumCost)
						&& JewelerBenchContainer.this.maximumCost > 0 && this.getHasStack();
			}
			@Override
			public ItemStack onTake(EntityPlayer player, ItemStack stack) {
				if (!player.capabilities.isCreativeMode) {
					player.addExperienceLevel(-JewelerBenchContainer.this.maximumCost);
				}
				// clear input slots
				JewelerBenchContainer.this.inputSlots.setInventorySlotContents(2, ItemStack.EMPTY);
				JewelerBenchContainer.this.maximumCost = 0;
				playUseSound();
				return stack;
			}
		});
		this.addSlotToContainer(new Slot(this.removeAdornmentOutputSlot, 6, 145, 56) {
			@Override
			public boolean isItemValid(ItemStack stack) {
				return false;
			}
			@Override
			public boolean canTakeStack(EntityPlayer player) {
				return (player.capabilities.isCreativeMode
						|| player.experienceLevel >= JewelerBenchContainer.this.maximumCost)
						&& JewelerBenchContainer.this.maximumCost > 0 && this.getHasStack();
			}
			@Override
			public ItemStack onTake(EntityPlayer player, ItemStack stack) {
				if (!player.capabilities.isCreativeMode) {
					player.addExperienceLevel(-JewelerBenchContainer.this.maximumCost);
				}
				// clear input slots
				JewelerBenchContainer.this.inputSlots.setInventorySlotContents(3, ItemStack.EMPTY);
				JewelerBenchContainer.this.maximumCost = 0;
				playUseSound();
				return stack;
			}
		});

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
		// update to row (repair/upgrade row)
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
    				&&	TreasureCharmableMaterials.isSourceItemRegistered(itemStack2.getItem().getRegistryName())) {
    			//Treasure.logger.debug("left is adornment and right is gem!");
    			ICharmableCapability cap = itemStack.getCapability(CHARMABLE, null);
    			if (cap.getSourceItem().equals(Items.AIR.getRegistryName())) {
    				this.materialCost = 1;

    				// build the output item, duplicating the left stack (adornment) with the right stack as the source item
    				Optional<Adornment> adornment = TreasureAdornmentRegistry.getAdornment(itemStack, itemStack2);
    				Treasure.LOGGER.debug("adornment -> {}", adornment.get().getRegistryName());
    				if (adornment.isPresent()) {
    					ItemStack outputStack = TreasureAdornmentRegistry.copyStack(itemStack, new ItemStack(adornment.get()));
    					ICharmableCapability outputCap = outputStack.getCapability(CHARMABLE, null);
    					outputCap.setHighestLevel(cap.getHighestLevel());
    					// update the output slot
    					this.outputSlot.setInventorySlotContents(0, outputStack);
    				}
    			}
    		}            
        }
        
        ItemStack removeGemItemStack = this.inputSlots.getStackInSlot(2);
        if (removeGemItemStack.isEmpty()) {
            this.removeGemOutputSlot.setInventorySlotContents(0, ItemStack.EMPTY);
        }
        // is the input an adornment with a gem/source
        else if ((removeGemItemStack.getItem() instanceof Adornment) 
				&&	removeGemItemStack.hasCapability(CHARMABLE, null)
				&&	TreasureCharmableMaterials.isSourceItemRegistered(
						removeGemItemStack.getCapability(CHARMABLE, null).getSourceItem())) {
        	
        	this.maximumCost = 1;
        	// get the original base adornment (no gem)
        	Adornment adornment = (Adornment)removeGemItemStack.getItem();
        	Optional<Adornment> baseAdornment = TreasureAdornmentRegistry.get(
        			adornment.getType(), 
        			adornment.getSize(), 
        			removeGemItemStack.getCapability(CHARMABLE, null).getBaseMaterial(),
        			Items.AIR.getRegistryName());
        	if (baseAdornment.isPresent()) {
        		this.removeGemOutputSlot.setInventorySlotContents(0, new ItemStack(baseAdornment.get()));
        	}
        }
        
        ItemStack removeAdornmentItemStack = this.inputSlots.getStackInSlot(3);
        if (removeAdornmentItemStack.isEmpty()) {
            this.removeAdornmentOutputSlot.setInventorySlotContents(0, ItemStack.EMPTY);
        }
        // is the input an adornment with a gem/source
        else if ((removeAdornmentItemStack.getItem() instanceof Adornment) 
				&&	removeAdornmentItemStack.hasCapability(CHARMABLE, null)
				&&	TreasureCharmableMaterials.isSourceItemRegistered(
						removeAdornmentItemStack.getCapability(CHARMABLE, null).getSourceItem())) {
        	this.maximumCost = 1;
        	// get the original gem
        	Item gem = ForgeRegistries.ITEMS.getValue(removeAdornmentItemStack.getCapability(CHARMABLE, null).getSourceItem());
        	// update the output
        	this.removeAdornmentOutputSlot.setInventorySlotContents(0, new ItemStack(gem));
        }
	}

	@Override
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);

		if (!WorldInfo.isClientSide(world)) {
			this.clearContainer(player, this.world, this.inputSlots);
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
		return ItemStack.EMPTY;
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
	}
}
