/*
 * This file is part of  Treasure2.
 * Copyright (c) 2022 Mark Gottschling (gottsch)
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
import static mod.gottsch.forge.treasure2.core.capability.TreasureCapabilities.RUNESTONES;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import mod.gottsch.forge.treasure2.core.Treasure;
import mod.gottsch.forge.treasure2.core.adornment.TreasureAdornmentRegistry;
import mod.gottsch.forge.treasure2.core.block.TreasureBlocks;
import mod.gottsch.forge.treasure2.core.capability.ICharmableCapability;
import mod.gottsch.forge.treasure2.core.capability.IRunestonesCapability;
import mod.gottsch.forge.treasure2.core.capability.InventoryType;
import mod.gottsch.forge.treasure2.core.charm.ICharmEntity;
import mod.gottsch.forge.treasure2.core.charm.TreasureCharms;
import mod.gottsch.forge.treasure2.core.item.Adornment;
import mod.gottsch.forge.treasure2.core.item.CharmItem;
import mod.gottsch.forge.treasure2.core.item.RunestoneItem;
import mod.gottsch.forge.treasure2.core.item.TreasureItems;
import mod.gottsch.forge.treasure2.core.material.TreasureCharmableMaterials;
import mod.gottsch.forge.treasure2.core.rune.IRuneEntity;
import mod.gottsch.forge.treasure2.core.rune.TreasureRunes;
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
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
 */
public class CharmingTableContainer extends Container {
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

	private BlockPos selfPosition;
	private World world;
	public int maximumCost;
	public int materialCost;

	/*
	 * Called from setup.
	 */
	public static CharmingTableContainer create(int windowID, PlayerInventory playerInventory, PacketBuffer extraData) {
		return new CharmingTableContainer(windowID, TreasureContainers.CHARMING_TABLE_CONTAINER_TYPE, playerInventory, 
				playerInventory.player.level, playerInventory.player.blockPosition(), playerInventory.player);
	}

	/**
	 * Client side
	 * @param windowId
	 * @param playerInventory
	 */
	public CharmingTableContainer(int windowId, PlayerInventory playerInventory, World world, BlockPos pos, PlayerEntity player) {
		this(windowId, TreasureContainers.CHARMING_TABLE_CONTAINER_TYPE, playerInventory, world, pos, player);
	}

	/**
	 * Server side
	 * @param windowId
	 * @param containerType
	 * @param playerInventory
	 */
	protected CharmingTableContainer(int windowId, ContainerType<?> containerType, PlayerInventory playerInventory, World world, BlockPos pos, PlayerEntity player) {
		super(containerType, windowId);

		this.selfPosition = pos;
		this.world = world;

		try {
			setupSlots();
			setupCharmSlots();
			setupRuneSlots();
		}
		catch(Exception e) {
			Treasure.LOGGER.debug("error:", e);
		}

		// add player slots (0-8)
		for (int xIndex = 0; xIndex < 9; ++xIndex) {
			this.addSlot(new Slot(playerInventory, xIndex, 8 + xIndex * 18, 232));
		}

		// add inventory slots (9-26)
		for (int yIndex = 0; yIndex < 3; ++yIndex) {
			for (int xIndex = 0; xIndex < 9; ++xIndex) {
				this.addSlot(
						new Slot(playerInventory, xIndex + yIndex * 9 + 9, 8 + xIndex * 18, 174 + yIndex * 18));
			}
		}
	}

	/**
	 * 
	 */
	private void setupSlots() {
		this.outputSlot = new CraftResultInventory();

		// initialize slots
		this.inputSlots = new Inventory(2) {
			@Override
			public void setChanged() {
				super.setChanged();
				CharmingTableContainer.this.slotsChanged(this);
			}
		};

		// adornment input slot
		this.addSlot(new Slot(this.inputSlots, 0, 27, 18) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return (stack.getItem() instanceof CharmItem) || (stack.getItem() instanceof Adornment);
			}
		});

		// charm to add input slot
		this.addSlot(new Slot(this.inputSlots, 1, 76, 18) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return (stack.getItem() instanceof CharmItem || stack.getItem() instanceof RunestoneItem
						|| TreasureCharmableMaterials.isSourceItemRegistered(stack.getItem().getRegistryName()));
			}
		});

		// adornment + charm output slot
		this.addSlot(new Slot(this.outputSlot, 2, 134, 18) { //2
			// make the slot read-only
			@Override
			public boolean mayPlace(ItemStack stack) {
				return false;
			}

			@Override
			public boolean mayPickup(PlayerEntity player) {
				return (player.isCreative()
						|| player.experienceLevel >= CharmingTableContainer.this.maximumCost)
						&& CharmingTableContainer.this.maximumCost > 0 && this.hasItem();
			}

			@Override
			public ItemStack onTake(PlayerEntity player, ItemStack stack) {
				if (!player.isCreative()) {
					player.giveExperiencePoints(-CharmingTableContainer.this.maximumCost);
				}
				clearSlotsWhenOutputTaken();
				CharmingTableContainer.this.maximumCost = 0;
				playUseSound();
				return stack;
			}
		});
	}

	/**
	 * 
	 */
	private void setupCharmSlots() {
		this.charmInputSlots1 = new Inventory(4);
		this.charmInputSlots2 = new Inventory(4) {
			@Override
			public void setChanged() {
				super.setChanged();
				CharmingTableContainer.this.slotsChanged(this);
			}
		};

		// current charm/runestone input slots - read only
		for (int i = 0; i < 4; i++) {
			this.addSlot(new Slot(this.charmInputSlots1, i, 27, 51 + (i * 19)) {
				@Override
				public boolean mayPlace(ItemStack stack) {
					return false;
				}
				@Override
				public boolean mayPickup(PlayerEntity player) {
					return false;
				}
			});
		}
		// charm modifier (remove) input slot
		for (int i = 0; i < 4; i++) {
			this.addSlot(new Slot(this.charmInputSlots2, i, 76, 51 + (i * 19)) {
				@Override
				public boolean mayPlace(ItemStack stack) {
					return (stack.getItem() == TreasureItems.TREASURE_TOOL.get()
							|| 	TreasureCharmableMaterials.isSourceItemRegistered(stack.getItem().getRegistryName()));
				}
			});
		}

		// charm output slots
		this.charmOutputSlots = new CraftResultInventory[4];
		for (int i = 0; i < 4; i++) {
			this.charmOutputSlots[i] = new MultilineInventoryCraftResult(i);
			this.addSlot(new Slot(this.charmOutputSlots[i], 0, 134, 51 + (i * 19)) {
				@Override
				public boolean mayPlace(ItemStack stack) {
					return false;
				}

				@Override
				public boolean mayPickup(PlayerEntity player) {
					return (player.isCreative()
							|| player.experienceLevel >= CharmingTableContainer.this.maximumCost)
							&& CharmingTableContainer.this.maximumCost > 0 && this.hasItem();
				}

				@Override
				public ItemStack onTake(PlayerEntity player, ItemStack stack) {
					Treasure.LOGGER.debug("attempting to take itemstack from slot # -> {}", this.getSlotIndex());
					if (!player.isCreative()) {
						player.giveExperiencePoints(-CharmingTableContainer.this.maximumCost);
					}
					clearSlotsWhenCharmOutputTaken(((MultilineInventoryCraftResult)this.container).getLine());
					CharmingTableContainer.this.maximumCost = 0;
					playUseSound();
					return stack;
				}
			});
		}
	}

	/**
	 * 
	 */
	private void setupRuneSlots() {
		this.runeOutputSlot = new CraftResultInventory();

		// initialize slots
		this.runeInputSlot1 = new Inventory(1);
		this.runeInputSlot2 = new Inventory(1) {
			@Override
			public void setChanged() {
				super.setChanged();
				CharmingTableContainer.this.slotsChanged(this);
			}
		};

		// rune input slot
		this.addSlot(new Slot(this.runeInputSlot1, 0, 27, 142) { //15
			@Override
			public boolean mayPlace(ItemStack stack) {
				return false;
			}
			@Override
			public boolean mayPickup(PlayerEntity player) {
				return false;
			}
		});

		// rune modifier (ie add/remove)
		this.addSlot(new Slot(this.runeInputSlot2, 0, 76, 142) { //16
			@Override
			public boolean mayPlace(ItemStack stack) {
				return stack.getItem() == TreasureItems.TREASURE_TOOL.get();
			}
		});

		// rune output
		this.addSlot(new Slot(this.runeOutputSlot, 0, 134, 142) { //17
			// make the slot read-only
			@Override
			public boolean mayPlace(ItemStack stack) {
				return false;
			}

			@Override
			public boolean mayPickup(PlayerEntity player) {
				return (player.isCreative()
						|| player.experienceLevel >= CharmingTableContainer.this.maximumCost)
						&& CharmingTableContainer.this.maximumCost > 0 && this.hasItem();
			}

			@Override
			public ItemStack onTake(PlayerEntity player, ItemStack stack) {
				if (!player.isCreative()) {
					player.giveExperiencePoints(-CharmingTableContainer.this.maximumCost);
				}
				clearSlotsWhenRunesOutputTaken();
				CharmingTableContainer.this.maximumCost = 0;
				playUseSound();
				return stack;
			}
		});
	}

	/**
	 * NOTE don't clear the charm, runestone secondary inputs en masse.
	 */
	protected void clearAllSlots() {
		CharmingTableContainer.this.inputSlots.clearContent();
		CharmingTableContainer.this.charmInputSlots1.clearContent();	
		CharmingTableContainer.this.charmInputSlots2.clearContent();	
		CharmingTableContainer.this.runeInputSlot1.clearContent();
		CharmingTableContainer.this.runeInputSlot2.clearContent();
		CharmingTableContainer.this.outputSlot.clearContent();
		for (int i = 0; i < CharmingTableContainer.this.charmOutputSlots.length; i++) {
			CharmingTableContainer.this.charmOutputSlots[i].clearContent();
		}
		CharmingTableContainer.this.runeOutputSlot.clearContent();
	}

	/**
	 * 
	 */
	protected void clearSlotsWhenOutputTaken() {
		CharmingTableContainer.this.inputSlots.setItem(0, ItemStack.EMPTY);
		// reduce the size of the source item
		if (CharmingTableContainer.this.materialCost > 0) {
			ItemStack itemStack = CharmingTableContainer.this.inputSlots.getItem(1);

			if (!itemStack.isEmpty() && itemStack.getCount() > CharmingTableContainer.this.materialCost) {
				itemStack.shrink(CharmingTableContainer.this.materialCost);
				CharmingTableContainer.this.inputSlots.setItem(1, itemStack);
			} else {
				CharmingTableContainer.this.inputSlots.setItem(1, ItemStack.EMPTY);
			}
		} else {
			CharmingTableContainer.this.inputSlots.setItem(1, ItemStack.EMPTY);
		}

		CharmingTableContainer.this.outputSlot.clearContent();

		CharmingTableContainer.this.charmInputSlots1.clearContent();
		for (int i = 0; i < CharmingTableContainer.this.charmOutputSlots.length; i++) {
			CharmingTableContainer.this.charmOutputSlots[i].clearContent();
		}

		CharmingTableContainer.this.runeInputSlot1.clearContent();
		CharmingTableContainer.this.runeOutputSlot.clearContent();
	}

	/**
	 * 
	 * @param index
	 */
	protected void clearSlotsWhenCharmOutputTaken(int index) {
		Treasure.LOGGER.debug("clearing charm on output taken, index -> {}", index);
		Treasure.LOGGER.debug("material cost -> {}", this.materialCost);
		CharmingTableContainer.this.inputSlots.setItem(0, ItemStack.EMPTY);
		CharmingTableContainer.this.outputSlot.clearContent();

		CharmingTableContainer.this.charmInputSlots1.clearContent();
		// reduce the size of the source item
		if (CharmingTableContainer.this.materialCost > 0) {
			ItemStack itemStack = CharmingTableContainer.this.charmInputSlots2.getItem(index);
			if (!itemStack.isEmpty() && itemStack.getCount() > CharmingTableContainer.this.materialCost) {
				itemStack.shrink(CharmingTableContainer.this.materialCost);
				CharmingTableContainer.this.charmInputSlots2.setItem(index, itemStack);
			} else {
				CharmingTableContainer.this.charmInputSlots2.setItem(index, ItemStack.EMPTY);
			}
		} else {
			CharmingTableContainer.this.charmInputSlots2.setItem(index, ItemStack.EMPTY);
		}
		for (int i = 0; i < CharmingTableContainer.this.charmOutputSlots.length; i++) {
			CharmingTableContainer.this.charmOutputSlots[i].clearContent();
		}

		CharmingTableContainer.this.runeInputSlot1.clearContent();
		CharmingTableContainer.this.runeOutputSlot.clearContent();
	}

	/**
	 * 
	 */
	protected void clearSlotsWhenRunesOutputTaken() {
		CharmingTableContainer.this.inputSlots.setItem(0, ItemStack.EMPTY);
		CharmingTableContainer.this.outputSlot.clearContent();

		CharmingTableContainer.this.charmInputSlots1.clearContent();
		for (int i = 0; i < CharmingTableContainer.this.charmOutputSlots.length; i++) {
			CharmingTableContainer.this.charmOutputSlots[i].clearContent();
		}

		CharmingTableContainer.this.runeInputSlot1.clearContent();
		CharmingTableContainer.this.runeInputSlot2.clearContent();
		// reduce the size of the source item
		if (CharmingTableContainer.this.materialCost > 0) {
			ItemStack itemStack = CharmingTableContainer.this.runeInputSlot2.getItem(0);
			if (!itemStack.isEmpty() && itemStack.getCount() > CharmingTableContainer.this.materialCost) {
				itemStack.shrink(CharmingTableContainer.this.materialCost);
				CharmingTableContainer.this.runeInputSlot2.setItem(0, itemStack);
			} else {
				CharmingTableContainer.this.runeInputSlot2.setItem(0, ItemStack.EMPTY);
			}
		} else {
			CharmingTableContainer.this.runeInputSlot2.setItem(0, ItemStack.EMPTY);
		}
		CharmingTableContainer.this.runeOutputSlot.clearContent();
	}

	@Override
	public boolean stillValid(PlayerEntity player) {
		if (this.world.getBlockState(selfPosition).getBlock() != TreasureBlocks.CHARMING_TABLE) {
			return false;
		} else {
			return player.distanceToSqr((double) this.selfPosition.getX() + 0.5D,
					(double) this.selfPosition.getY() + 0.5D, (double) this.selfPosition.getZ() + 0.5D) <= 64.0D;
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
		if (inventory == this.inputSlots || inventory == this.charmInputSlots2 || inventory == this.runeInputSlot2) {
			this.updateOutput();
		}
	}

	/**
	 * Main method
	 */
	public void updateOutput() {
		this.maximumCost = 0;
		this.materialCost = 1;

		// check if the adornment slot has an item
		ItemStack itemStack = this.inputSlots.getItem(0);
		if (itemStack.isEmpty()) {
			this.outputSlot.setItem(0, ItemStack.EMPTY);
			// clear charms
			for (int i = 0; i < 4; i++) {
				this.charmInputSlots1.setItem(i, ItemStack.EMPTY);
				this.charmOutputSlots[i].setItem(0, ItemStack.EMPTY);
			}
			// clear runestone
			this.runeInputSlot1.setItem(0, ItemStack.EMPTY);
			this.runeOutputSlot.setItem(0, ItemStack.EMPTY);
		}
		else {
			/*
			 * setup
			 */
			this.maximumCost = 1;

			// get the inputs
			ItemStack itemStack2 = this.inputSlots.getItem(1);
			if (itemStack2.isEmpty()) {
				this.outputSlot.setItem(0, ItemStack.EMPTY);
			}

			// populate the charms and runestone if input is an adornment
			if (itemStack.getItem() instanceof Adornment) {
				ICharmableCapability charmableCap = itemStack.getCapability(CHARMABLE).map(c -> c).orElse(null);
				if (charmableCap != null) {
					if (charmableCap.getCharmEntities().get(InventoryType.SOCKET).size() > 0) {
						// less than 5 socket charms can fit into container gui
						if (charmableCap.getCharmEntities().get(InventoryType.SOCKET).size() <= 4) {
							int index = 0;
							ResourceLocation resource = new ResourceLocation(Treasure.MODID, "gold_charm");
							for (ICharmEntity charm : charmableCap.getCharmEntities().get(InventoryType.SOCKET)) {
								// TODO calculate the correct charm to use
								ItemStack charmStack = new ItemStack(TreasureItems.ALL_ITEMS.get(resource));
								// duplicate the charm entity
								ICharmEntity entity = charm.getCharm().createEntity(charm);
								// update the new charmStack with the duplicated charm
								charmStack.getCapability(CHARMABLE).ifPresent(cap -> {
									((List<ICharmEntity>)cap.getCharmEntities().get(InventoryType.INNATE)).set(0, entity);
								});										
								this.charmInputSlots1.setItem(index, charmStack);
								index++;
							}
						}
						else {
							// TODO select 4 charms with the least charge
						}
					}
				}

				// populate the runestones
				IRunestonesCapability runestoneCap = itemStack.getCapability(RUNESTONES).map(cap -> cap).orElse(null);
				if (runestoneCap != null) {
					Treasure.LOGGER.debug("has runestone cap, socket size -> {}", runestoneCap.getEntities(InventoryType.SOCKET).size());
					if (runestoneCap.getEntities(InventoryType.SOCKET).size() > 0) {
						Treasure.LOGGER.debug("has runestone -> {}", runestoneCap.getEntities(InventoryType.SOCKET).get(0).getRunestone());
						Optional<Item> item = TreasureRunes.getItem(runestoneCap.getEntities(InventoryType.SOCKET).get(0).getRunestone());
						// NOTE if item isn't found, something went wrong
						if (item.isPresent()) {
							Treasure.LOGGER.debug("has runestone item -> {}", item.get().getRegistryName());
							this.runeInputSlot1.setItem(0, new ItemStack(item.get()));
						}
					}
				}
			}
			/////

			for (int i = 0; i < 4; i++) {
				if (this.charmInputSlots1.getItem(i).isEmpty() ||
						this.charmInputSlots2.getItem(i).isEmpty()) {
					this.charmOutputSlots[i].setItem(0, ItemStack.EMPTY);
				}
			}

			ItemStack runeStack = this.runeInputSlot1.getItem(0);
			if (runeStack.isEmpty()) {
				this.runeOutputSlot.setItem(0, ItemStack.EMPTY);
			}
			ItemStack runeStack2 = this.runeInputSlot2.getItem(0);
			if (runeStack2.isEmpty()) {
				this.runeOutputSlot.setItem(0, ItemStack.EMPTY);
			}

			/*
			 *  add charm check
			 */
			if (itemStack.getCapability(CHARMABLE).map(cap -> cap.isSocketable()).orElse(false)
					&& itemStack2.getCapability(CHARMABLE, null).map(cap -> cap.isBindable()).orElse(false)) {

				ICharmableCapability cap = itemStack.getCapability(CHARMABLE).map(c -> c).orElse(null);
				if (cap != null) {
					// check that they charm type doesn't already exist on the adornment
					if (cap.hasCharmType(itemStack2, itemStack, InventoryType.INNATE, InventoryType.SOCKET)) {
						// TODO nice-to-have, flag to display message indicating the charm type already exists on dest.
						return;
					}
					// check that there is room to add charms
					if (cap.getCharmEntities().get(InventoryType.SOCKET).size() < cap.getMaxSocketSize()) {
						// build the output item, add the charm to the adornment
						Optional<ItemStack> outStack = TreasureAdornmentRegistry.transferCapabilities(itemStack2, itemStack, InventoryType.INNATE, InventoryType.SOCKET);
						if (outStack.isPresent()) {
							outStack.get().getCapability(RUNESTONES).ifPresent(outCap -> {
								outCap.getEntities(InventoryType.SOCKET).forEach(entity -> {
									entity.getRunestone().apply(outStack.get(), entity);
								});
							});
							this.outputSlot.setItem(0, outStack.get());    					
						}
					}
				}
			}

			/*
			 * add charm book (imbuing) check
			 */
			if (itemStack.getCapability(CHARMABLE).map(cap -> cap.isImbuable()).orElse(false)
					&& itemStack2.getCapability(CHARMABLE).map(cap -> cap.isImbuing()).orElse(false)) {
				ICharmableCapability cap = itemStack.getCapability(CHARMABLE).map(c -> c).orElse(null);
				if (cap != null) {
					// check that they charm type doesn't already exist on the adornment
					if (cap.hasCharmType(itemStack2, itemStack, InventoryType.INNATE, InventoryType.IMBUE)) {
						return;
					}
					// check that there is room to add charms
					if (cap.getCharmEntities().get(InventoryType.IMBUE).size() < cap.getMaxImbueSize()) {
						// build the output item, add the charm book to the adornment
						Optional<ItemStack> outStack = TreasureAdornmentRegistry.transferCapabilities(itemStack2, itemStack, InventoryType.INNATE, InventoryType.IMBUE);
						if (outStack.isPresent()) {
							outStack.get().getCapability(RUNESTONES).ifPresent(outCap -> {
								outCap.getEntities(InventoryType.SOCKET).forEach(entity -> {
									entity.getRunestone().apply(outStack.get(), entity);
								});
							});
							this.outputSlot.setItem(0, outStack.get());    					
						}
					}  
				}
			}

			/*
			 *  add runestone check. uses the assumption that runestones only contain 1 rune entity
			 */
			else if (itemStack.getCapability(RUNESTONES).map(cap -> cap.isSocketable()).orElse(false)
					&& itemStack2.getCapability(RUNESTONES).map(cap -> cap.isBindable()).orElse(false)) {

				IRunestonesCapability cap = itemStack.getCapability(RUNESTONES).map(c -> c).orElseThrow(() -> new IllegalStateException());
				// check that there is room to add charms
				if (cap.getEntities(InventoryType.SOCKET).size() < cap.getMaxSize(InventoryType.SOCKET)) {
					Optional<ItemStack> stack = TreasureAdornmentRegistry.transferCapabilities(itemStack2, itemStack, InventoryType.INNATE, InventoryType.SOCKET);
					if (stack.isPresent()) {
						AtomicBoolean isStackValid = new AtomicBoolean(true);
						stack.get().getCapability(RUNESTONES).ifPresent(stackCap -> {
							stackCap.getEntities(InventoryType.SOCKET).forEach(entity -> {
								if (!entity.getRunestone().isValid(stack.get())) {
									isStackValid.set(false);
									return;
								}
								entity.getRunestone().apply(stack.get(), entity);
							});
						});
						if (isStackValid.get()) {
							this.outputSlot.setItem(0, stack.get());
						}
					}
				}
			}

			/*
			 * recharge charm check
			 * input1 = charm, input2 = source item
			 */
			else if (itemStack.getCapability(CHARMABLE, null).map(cap -> cap.isBindable()).orElse(false)
					&& TreasureCharmableMaterials.isSourceItemRegistered(itemStack2.getItem().getRegistryName())) {
				// make a copy of CharmItem
				ItemStack stack = TreasureCharms.copyStack(itemStack, itemStack);
				ICharmableCapability cap = stack.getCapability(CHARMABLE).map(c -> c).orElse(null);
				// get the charm
				if (cap != null) {
					ICharmEntity entity = ((List<ICharmEntity>) cap.getCharmEntities().get(InventoryType.INNATE)).get(0);
					if (entity.getRecharges() > 0 && entity.getMana() < entity.getMaxMana()) {
						entity.setRecharges(entity.getRecharges() - 1);
						entity.setMana(entity.getMaxMana());
						// TODO apply runes
						this.outputSlot.setItem(0, stack);
					}
				}
			}
			/*
			 *  remove/recharge adornment charm check
			 */
			else if (!this.charmInputSlots1.isEmpty() && !this.charmInputSlots2.isEmpty()) {
				// now check if the inputs line up and using correct items
				for (int i = 0; i < 4; i++) {
					// recharge check
					if (!this.charmInputSlots1.getItem(i).isEmpty()
							&& !this.charmInputSlots2.getItem(i).isEmpty()
							&& TreasureCharmableMaterials.isSourceItemRegistered(this.charmInputSlots2.getItem(i).getItem().getRegistryName())) {

						// make a copy of the adornment stack
						ItemStack stack = TreasureAdornmentRegistry.copyStack(itemStack, itemStack);
						ICharmableCapability cap = stack.getCapability(CHARMABLE).map(c -> c).orElse(null);

						// get the charm
						if (cap != null) {
							ICharmEntity entity = ((List<ICharmEntity>) cap.getCharmEntities().get(InventoryType.SOCKET)).get(i);
							if (entity.getRecharges() > 0 && entity.getMana() < entity.getMaxMana()) {
								entity.setRecharges(entity.getRecharges() - 1);
								entity.setMana(entity.getMaxMana());
								// TODO apply runes
								this.charmOutputSlots[i].setItem(0, stack);
							}
						}
					}
					else if (!this.charmInputSlots1.getItem(i).isEmpty()
							&& !this.charmInputSlots2.getItem(i).isEmpty()
							&& this.charmInputSlots2.getItem(i).getItem() == TreasureItems.TREASURE_TOOL.get()) {

						// make a copy of the adornment stack
						ItemStack stack = itemStack.copy();
						ItemStack newStack = TreasureAdornmentRegistry.copyStack(itemStack, stack);
						ICharmableCapability cap = newStack.getCapability(CHARMABLE).map(c -> c).orElse(null);
						if (cap != null) {
							// remove the charm
							cap.remove(InventoryType.SOCKET, i);
							// apply any runes
							newStack.getCapability(RUNESTONES).ifPresent(stackCap -> {
								stackCap.getEntities(InventoryType.SOCKET).forEach(entity -> {
									entity.getRunestone().apply(newStack, entity);
								});
							});
							this.charmOutputSlots[i].setItem(0, newStack);
						}						
					}
				}
			}
			// remove runestone check
			else if (runeStack != ItemStack.EMPTY && (runeStack2.getItem() == TreasureItems.TREASURE_TOOL.get())) {
				// make a copy of the adornment stack
				ItemStack stack = itemStack.copy();
				stack = TreasureAdornmentRegistry.copyStack(itemStack, stack);
				IRunestonesCapability cap = stack.getCapability(RUNESTONES).map(c -> c).orElse(null);
				if (cap != null) {
					// get the rune entity
					IRuneEntity entity = runeStack.getCapability(RUNESTONES).map(c -> c.getEntities(InventoryType.INNATE).get(0)).orElse(null);
					// remove the entity from adornment
					if (entity != null && cap.remove(InventoryType.SOCKET, entity)) {
						// undo the effects
						entity.getRunestone().undo(stack, entity);
					}
				}				
				this.runeOutputSlot.setItem(0, stack);
			}
		}
	}

	@Override
	public void removed(PlayerEntity player) {
		super.removed(player);

		if (!player.level.isClientSide) {
			this.clearContainer(player, this.world, this.inputSlots);
			// clear all the inventories
			clearAllSlots();
		}
	}
	
	@Override
	public ItemStack quickMoveStack(PlayerEntity player, int slot) {
	      ItemStack itemStack = ItemStack.EMPTY;
	      return itemStack;
	}
}
