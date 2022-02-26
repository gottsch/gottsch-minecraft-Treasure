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
public class CharmingTableContainer extends Container {

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

		// add inventory slots
		for (int yIndex = 0; yIndex < 3; ++yIndex) {
			for (int xIndex = 0; xIndex < 9; ++xIndex) {
				this.addSlotToContainer(
						new Slot(playerInventory, xIndex + yIndex * 9 + 9, 8 + xIndex * 18, 174 + yIndex * 18));
			}
		}

		// add player slots
		for (int xIndex = 0; xIndex < 9; ++xIndex) {
			this.addSlotToContainer(new Slot(playerInventory, xIndex, 8 + xIndex * 18, 232));
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
				return (stack.getItem() instanceof CharmItem);
			}
		});

		// adornment + charm output slot
		this.addSlotToContainer(new Slot(this.outputSlot, 2, 134, 18) {
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
		this.charmInputSlots = new InventoryBasic("Charm", true, 8) {
			@Override
			public void markDirty() {
				super.markDirty();
				CharmingTableContainer.this.onCraftMatrixChanged(this);
			}
		};

		// current charm/runestone input slots - read only
		for (int i = 0; i < 4; i++) {
			this.addSlotToContainer(new Slot(this.charmInputSlots, 3 + i, 27, 51 + (i * 18)) {
				@Override
				public boolean isItemValid(ItemStack stack) {
					return false;
				}
			});
		}
		// charm modifier input slot
		for (int i = 0; i < 4; i++) {
			this.addSlotToContainer(new Slot(this.charmInputSlots, 7 + i, 76, 51 + (i * 18)) {
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
			this.addSlotToContainer(new Slot(this.charmOutputSlots[i], 11 + i, 134, 51 + (i * 18)) {
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
	}

	private void setupRuneSlots() {
		this.runeOutputSlot = new InventoryCraftResult();

		// initialize slots
		this.runeInputSlots = new InventoryBasic("Rune", true, 2) {
			@Override
			public void markDirty() {
				super.markDirty();
				CharmingTableContainer.this.onCraftMatrixChanged(this);
			}
		};

		// rune input slot
		this.addSlotToContainer(new Slot(this.runeInputSlots, 15, 27, 18) {
			@Override
			public boolean isItemValid(ItemStack stack) {
				return false;
			}
		});

		// rune modifier (ie add/remove)
		this.addSlotToContainer(new Slot(this.runeInputSlots, 16, 27, 18) {
			@Override
			public boolean isItemValid(ItemStack stack) {
				return (stack.getItem() instanceof RunestoneItem || stack.getItem() == TreasureItems.TREASURE_TOOL);
			}
		});

		// rune output
		this.addSlotToContainer(new Slot(this.runeOutputSlot, 17, 134, 18) {
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
	 * 
	 */
	protected void clearAllSlots() {
		CharmingTableContainer.this.inputSlots.clear();
		CharmingTableContainer.this.charmInputSlots.clear();
		CharmingTableContainer.this.runeInputSlots.clear();
		CharmingTableContainer.this.outputSlot.clear();
		for (int i = 0; i < CharmingTableContainer.this.charmOutputSlots.length; i++) {
			CharmingTableContainer.this.inputSlots.setInventorySlotContents(i, ItemStack.EMPTY);
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
		if (inventory == this.inputSlots || inventory == this.charmInputSlots || inventory == this.runeInputSlots) {
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
		}
		else {
			/*
			 * setup
			 */
			this.maximumCost = 1;

			// populate the charms and runestone if any
			ICharmableCapability charmableCap = itemStack.getCapability(TreasureCapabilities.CHARMABLE, null);
			if (charmableCap != null) {
				// less than 5 socket charms can fit into container gui
				if (charmableCap.getCharmEntities().get(InventoryType.SOCKET).size() <= 4) {
					int index = 0;
					ResourceLocation resource = new ResourceLocation(Treasure.MODID, "gold_charm");
					for (ICharmEntity charm : charmableCap.getCharmEntities().get(InventoryType.SOCKET)) {
						this.charmOutputSlots[index].setInventorySlotContents(0,  new ItemStack(TreasureItems.ITEMS.get(resource)));
					}
				}
				else {
					// TODO select 4 charms with the least charge
				}
			}

			// get the inputs
			ItemStack itemStack2 = this.inputSlots.getStackInSlot(1);

			// add gem check
			if (itemStack.hasCapability(CHARMABLE, null) && itemStack2.hasCapability(CHARMABLE, null)
					&& itemStack.getCapability(CHARMABLE, null).isSocketable()
					&& itemStack2.getCapability(CHARMABLE, null).isBindable()) {

				//Treasure.logger.debug("left is adornment and right is gem!");
				ICharmableCapability cap = itemStack.getCapability(CHARMABLE, null);
				// check that there is room to add charms
				if (cap.getCharmEntities().get(InventoryType.SOCKET).size() < cap.getMaxSocketSize()) {
					// build the output item, add the charm to the adornment
					Optional<ItemStack> outStack = TreasureAdornments.transferCapabilities(itemStack2, itemStack, InventoryType.INNATE, InventoryType.SOCKET);
					if (outStack.isPresent()) {
						if (outStack.get().hasCapability(RUNESTONES, null)) {
							outStack.get().getCapability(RUNESTONES, null).getEntities(InventoryType.SOCKET).forEach(entity -> {
								Treasure.logger.debug("binding charm: is applied -> {}", entity.isApplied());
								Treasure.logger.debug("binding charm: applied to -> {}", entity.getAppliedTo());
								Treasure.logger.debug("binding charm: applying runestone -> {} to entity -> {}", entity.getRunestone(), entity);
								entity.getRunestone().apply(outStack.get(), entity);
								Treasure.logger.debug("binding charm: after apply: is applied -> {}", entity.isApplied());
								Treasure.logger.debug("binding charm: after apply: applied to -> {}", entity.getAppliedTo());
								outStack.get().getCapability(CHARMABLE, null).getCharmEntities().forEach((type2, charm) -> {
									Treasure.logger.debug("binding charm: entity -> {}, mana -> {}, max mana -> {}", charm.getCharm().getName().toString(), charm.getMana(), charm.getMaxMana());
								});
							});
						}
						this.outputSlot.setInventorySlotContents(0, outStack.get());    					
					}
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
		if (this.world.getBlockState(selfPosition).getBlock() != TreasureBlocks.CHARMING_TABLE) {
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
