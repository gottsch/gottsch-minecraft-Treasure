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
package mod.gottsch.forge.treasure2.core.inventory;

import static mod.gottsch.forge.treasure2.core.capability.TreasureCapabilities.CHARMABLE;

import java.util.Optional;

import com.someguyssoftware.gottschcore.world.WorldInfo;

import mod.gottsch.forge.treasure2.core.Treasure;
import mod.gottsch.forge.treasure2.core.adornment.TreasureAdornmentRegistry;
import mod.gottsch.forge.treasure2.core.block.TreasureBlocks;
import mod.gottsch.forge.treasure2.core.capability.ICharmableCapability;
import mod.gottsch.forge.treasure2.core.item.Adornment;
import mod.gottsch.forge.treasure2.core.material.CharmableMaterial;
import mod.gottsch.forge.treasure2.core.material.TreasureCharmableMaterials;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * 
 * @author Mark Gottschling on Sep 12, 2022
 *
 */
public class JewelerBenchContainer extends Container {
	private final IInventory outputSlot;
	private final IInventory removeGemOutputSlot;
	private final IInventory removeAdornmentOutputSlot;
	private final IInventory inputSlots;
	private BlockPos selfPosition; // ?
	private World world;
	private PlayerEntity player;
	public int maximumCost;
	public int materialCost;
	private String repairedItemName;

	/*
	 * Called from setup.
	 */
	public static JewelerBenchContainer create(int windowId, PlayerInventory playerInventory, PacketBuffer extraData) {
		return new JewelerBenchContainer(windowId, TreasureContainers.JEWELER_BENCH_CONTAINER_TYPE, playerInventory, 
				playerInventory.player.level, playerInventory.player.blockPosition(), playerInventory.player);
	}
	
	/**
	 * Client-side constructor
	 */
	public JewelerBenchContainer(int windowId, PlayerInventory playerInventory, World world, BlockPos pos, PlayerEntity player) {
		this(windowId, TreasureContainers.JEWELER_BENCH_CONTAINER_TYPE, playerInventory, world, pos, player);

	}

	/**
	 * 
	 * @param playerInventory
	 * @param world
	 * @param pos
	 * @param player
	 */
	public JewelerBenchContainer(int windowId, ContainerType<?> containerType, PlayerInventory playerInventory, World world, BlockPos pos, PlayerEntity player) {
		super(containerType, windowId);
		
		this.outputSlot = new CraftResultInventory();
		this.removeGemOutputSlot = new CraftResultInventory();
		this.removeAdornmentOutputSlot = new CraftResultInventory();

		this.inputSlots = new Inventory(4) {
			@Override
			public void setChanged() {
				super.setChanged();
				JewelerBenchContainer.this.slotsChanged(this);
			}
		};

		this.selfPosition = pos;
		this.world = world;
		this.player = player;

		// add input slot(s)
		this.addSlot(new Slot(this.inputSlots, 0, 38, 18) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return (stack.getItem() instanceof Adornment);
			}
		});
		
		this.addSlot(new Slot(this.inputSlots, 1, 87, 18) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return (stack.getItem() instanceof Adornment || 
						TreasureCharmableMaterials.isSourceItemRegistered(stack.getItem().getRegistryName()));
			}
		});

		// keep adornment slot
		this.addSlot(new Slot(this.inputSlots, 2, 38, 37) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return (stack.getItem() instanceof Adornment);
			}
		});
		
		// remove gem input
		this.addSlot(new Slot(this.inputSlots, 3, 38, 56) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return (stack.getItem() instanceof Adornment);
			}
		});

		// add output slot(s)
		this.addSlot(new Slot(this.outputSlot, 4, 145, 18) {
			// make the slot read-only
			@Override
			public boolean mayPlace(ItemStack stack) {
				return false;
			}

			@Override
			public boolean mayPickup(PlayerEntity player) {
				return (player.isCreative()
						|| player.experienceLevel >= JewelerBenchContainer.this.maximumCost)
						&& JewelerBenchContainer.this.maximumCost > 0 && this.hasItem();
			}

			@Override
			public ItemStack onTake(PlayerEntity player, ItemStack stack) {
				if (!player.isCreative()) {
					player.giveExperiencePoints(-JewelerBenchContainer.this.maximumCost);
				}

				JewelerBenchContainer.this.inputSlots.setItem(0, ItemStack.EMPTY);

				// reduce the size of the source item
				if (JewelerBenchContainer.this.materialCost > 0) {
					ItemStack itemStack = JewelerBenchContainer.this.inputSlots.getItem(1);

					if (!itemStack.isEmpty() && itemStack.getCount() > JewelerBenchContainer.this.materialCost) {
						itemStack.shrink(JewelerBenchContainer.this.materialCost);
						JewelerBenchContainer.this.inputSlots.setItem(1, itemStack);
					} else {
						JewelerBenchContainer.this.inputSlots.setItem(1, ItemStack.EMPTY);
					}
				} else {
					JewelerBenchContainer.this.inputSlots.setItem(1, ItemStack.EMPTY);
				}				
				JewelerBenchContainer.this.maximumCost = 0;
				playUseSound();
				return stack;
			}
		});
		
		this.addSlot(new Slot(this.removeGemOutputSlot, 5, 145, 37) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return false;
			}
			@Override
			public boolean mayPickup(PlayerEntity player) {
				return (player.isCreative()
						|| player.experienceLevel >= JewelerBenchContainer.this.maximumCost)
						&& JewelerBenchContainer.this.maximumCost > 0 && this.hasItem();
			}
			@Override
			public ItemStack onTake(PlayerEntity player, ItemStack stack) {
				if (!player.isCreative()) {
					player.giveExperiencePoints(-JewelerBenchContainer.this.maximumCost);
				}
				// clear input slots
				JewelerBenchContainer.this.inputSlots.setItem(2, ItemStack.EMPTY);
				JewelerBenchContainer.this.maximumCost = 0;
				playUseSound();
				return stack;
			}
		});
		this.addSlot(new Slot(this.removeAdornmentOutputSlot, 6, 145, 56) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return false;
			}
			@Override
			public boolean mayPickup(PlayerEntity player) {
				return (player.isCreative()
						|| player.experienceLevel >= JewelerBenchContainer.this.maximumCost)
						&& JewelerBenchContainer.this.maximumCost > 0 && this.hasItem();
			}
			@Override
			public ItemStack onTake(PlayerEntity player, ItemStack stack) {
				if (!player.isCreative()) {
					player.giveExperiencePoints(-JewelerBenchContainer.this.maximumCost);
				}
				// clear input slots
				JewelerBenchContainer.this.inputSlots.setItem(3, ItemStack.EMPTY);
				JewelerBenchContainer.this.maximumCost = 0;
				playUseSound();
				return stack;
			}
		});

		// add inventory slots
		for (int yIndex = 0; yIndex < 3; ++yIndex) {
			for (int xIndex = 0; xIndex < 9; ++xIndex) {
				this.addSlot(
						new Slot(playerInventory, xIndex + yIndex * 9 + 9, 7 + xIndex * 18, 84 + yIndex * 18));
			}
		}

		// add player slots
		for (int xIndex = 0; xIndex < 9; ++xIndex) {
			this.addSlot(new Slot(playerInventory, xIndex, 7 + xIndex * 18, 142));
		}
	}
	
	/**
	 * 
	 */
	protected void playUseSound() {
		world.playSound((PlayerEntity) null, selfPosition.getX() + 0.5, selfPosition.getY() + 0.5D, selfPosition.getZ() + 0.5, SoundEvents.ANVIL_USE,
				SoundCategory.BLOCKS, 0.5F, world.random.nextFloat() * 0.1F + 0.9F);
	}

	@Override
	public void slotsChanged(IInventory inventory) {
		super.slotsChanged(inventory);
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
		ItemStack itemStack = this.inputSlots.getItem(0);
        if (itemStack.isEmpty()) {
            this.outputSlot.setItem(0, ItemStack.EMPTY);
        }
        else {
            this.maximumCost = 1;
            ItemStack itemStack2 = this.inputSlots.getItem(1);
            
            // add gem check
            if (itemStack2.isEmpty()) {
            	this.outputSlot.setItem(0, ItemStack.EMPTY);
            }
            else if ((itemStack.getItem() instanceof Adornment) 
    				&&	itemStack.getCapability(CHARMABLE).isPresent()
    				&&	TreasureCharmableMaterials.isSourceItemRegistered(itemStack2.getItem().getRegistryName())) {
//    			Treasure.LOGGER.debug("left is adornment and right is gem/source item!");
    			
    			ICharmableCapability cap = itemStack.getCapability(CHARMABLE).resolve().orElse(null);
    			if (cap != null && cap.getSourceItem().equals(Items.AIR.getRegistryName())) {

        			// get the base and source materials
        			Optional<CharmableMaterial> baseMaterial = TreasureCharmableMaterials.getBaseMaterial(cap.getBaseMaterial());
        			Optional<CharmableMaterial> sourceMaterial = TreasureCharmableMaterials.getSourceItem(itemStack2.getItem().getRegistryName());

        			if (baseMaterial.get().acceptsAffixer(itemStack2) && sourceMaterial.get().canAffix(itemStack)) {
        				this.materialCost = 1;

        				// build the output item, duplicating the left stack (adornment) with the right stack as the source item
        				Optional<Adornment> adornment = TreasureAdornmentRegistry.getAdornment(itemStack, itemStack2);
        				if (adornment.isPresent()) {
        					ItemStack outputStack = TreasureAdornmentRegistry.copyStack(itemStack, new ItemStack(adornment.get()));
        					outputStack.getCapability(CHARMABLE).ifPresent(outputCap -> {
        						outputCap.setHighestLevel(cap.getHighestLevel());
        					});    					
        					// update the output slot
        					this.outputSlot.setItem(0, outputStack);
        				}
        				else {
        					Treasure.LOGGER.debug("couldn't find adornment in the registry for base -> {} and source -> {}", baseMaterial.get().getName(), sourceMaterial.get().getName());
        				}
        			}  				
    			}
    		}            
        }
        
        ItemStack removeGemItemStack = this.inputSlots.getItem(2);
        if (removeGemItemStack.isEmpty()) {
            this.removeGemOutputSlot.setItem(0, ItemStack.EMPTY);
        }
        // is the input an adornment with a gem/source
        else if ((removeGemItemStack.getItem() instanceof Adornment) 
				&&	removeGemItemStack.getCapability(CHARMABLE).isPresent()
				&&	TreasureCharmableMaterials.isSourceItemRegistered(
						removeGemItemStack.getCapability(CHARMABLE).map(c -> c.getSourceItem()).orElse(null) )) {
        	
        	this.maximumCost = 1;
        	// get the original base adornment (no gem)
        	Adornment adornment = (Adornment)removeGemItemStack.getItem();
        	Optional<Adornment> baseAdornment = TreasureAdornmentRegistry.get(
        			adornment.getType(), 
        			adornment.getSize(), 
        			removeGemItemStack.getCapability(CHARMABLE).map(c2 -> c2.getBaseMaterial()).orElse(null),
        			Items.AIR.getRegistryName());
        	if (baseAdornment.isPresent()) {
        		this.removeGemOutputSlot.setItem(0, new ItemStack(baseAdornment.get()));
        	}
        }
        
        ItemStack removeAdornmentItemStack = this.inputSlots.getItem(3);
        if (removeAdornmentItemStack.isEmpty()) {
            this.removeAdornmentOutputSlot.setItem(0, ItemStack.EMPTY);
        }
        // is the input an adornment with a gem/source
        else if ((removeAdornmentItemStack.getItem() instanceof Adornment) 
				&&	removeAdornmentItemStack.getCapability(CHARMABLE).isPresent()
				&&	TreasureCharmableMaterials.isSourceItemRegistered(
						removeAdornmentItemStack.getCapability(CHARMABLE).map(c3 -> c3.getSourceItem()).orElse(null) )) {
        	this.maximumCost = 1;
        	// get the original gem
        	Item gem = ForgeRegistries.ITEMS.getValue(removeAdornmentItemStack.getCapability(CHARMABLE).map(c -> c.getSourceItem()).orElse(null));
        	// update the output
        	this.removeAdornmentOutputSlot.setItem(0, new ItemStack(gem));
        }
	}

	@Override
	public void removed(PlayerEntity player) {
		super.removed(player);

		if (!WorldInfo.isClientSide(world)) {
			this.clearContainer(player, this.world, this.inputSlots);
		}
	}

	@Override
	public boolean stillValid(PlayerEntity playerIn) {
		if (this.world.getBlockState(selfPosition).getBlock() != TreasureBlocks.JEWELER_BENCH) {
			return false;
		} else {
			return playerIn.distanceToSqr((double) this.selfPosition.getX() + 0.5D,
					(double) this.selfPosition.getY() + 0.5D, (double) this.selfPosition.getZ() + 0.5D) <= 64.0D;
		}
	}
	
	public ItemStack quickMoveStack(PlayerEntity player, int slot) {
	      ItemStack itemStack = ItemStack.EMPTY;
	      return itemStack;
	}
}
