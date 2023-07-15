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
package mod.gottsch.forge.treasure2.core.block.entity;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import mod.gottsch.forge.gottschcore.enums.IRarity;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.api.TreasureApi;
import mod.gottsch.forge.treasure2.core.block.AbstractTreasureChestBlock;
import mod.gottsch.forge.treasure2.core.block.effects.IChestEffects;
import mod.gottsch.forge.treasure2.core.config.Config;
import mod.gottsch.forge.treasure2.core.enums.Rarity;
import mod.gottsch.forge.treasure2.core.generator.chest.IChestGenerator;
import mod.gottsch.forge.treasure2.core.inventory.StandardChestContainerMenu;
import mod.gottsch.forge.treasure2.core.lock.LockState;
import mod.gottsch.forge.treasure2.core.particle.TreasureParticles;
import mod.gottsch.forge.treasure2.core.registry.ChestGeneratorRegistry;
import mod.gottsch.forge.treasure2.core.util.LangUtil;
import mod.gottsch.forge.treasure2.core.world.feature.FeatureType;
import mod.gottsch.forge.treasure2.core.world.feature.IFeatureType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

/**
 * 
 * @author Mark Gottschling on Nov 10, 2022
 *
 */
public abstract class AbstractTreasureChestBlockEntity extends BlockEntity implements ITreasureChestBlockEntity, IChestEffects, MenuProvider, Nameable {

	private static final String LOCK_STATES_TAG = "lockStates";
	private static final String FACING_TAG = "facing";
	private static final String SEALED_TAG = "sealed";
	private static final String LOOT_TABLE_TAG = "lootTable";
	private static final String MIMIC_TAG = "mimic";

	private static final String GENERATION_CONTEXT_TAG = "generationContext";
	private static final String LOOT_RARITY_TAG = "lootRarity";
	private static final String FEATURE_TYPE_TAG = "featureType";


	/*
	 * The inventory of the chest
	 */
	private final ItemStackHandler itemHandler = createHandler();
	private LazyOptional<IItemHandler> instanceHandler = LazyOptional.empty();

	/*
	 * A list of lockStates the chest has. The list should be the size of the max
	 * allowed for the chestType.
	 */
	private List<LockState> lockStates;

	/*
	 * The FACING index value of the TreasureChestBlock
	 */
	private Direction facing;

	/*
	 * A flag to indicate if the chest has been opened for the first time
	 */
	private boolean sealed;

	/*
	 * Properties detailing how the tile entity was generated
	 */
	private GenerationContext generationContext;

	/*
	 * The loot table assigned to this block entity
	 */
	private ResourceLocation lootTable;

	/*
	 * The mimic assigned to this block entity
	 */
	private ResourceLocation mimic;
	
	/*
	 * The custom name of this block entity
	 */
	private Component name;

	/*
	 * Vanilla properties for controlling the lid
	 */
	/** The current angle of the lid (between 0 and 1) */
	public float lidAngle;
	/** The angle of the lid last tick */
	public float prevLidAngle;

	/*
	 * Server updated properties
	 */
	/** The number of players currently using this chest */
	public int openCount;
	/** Server sync counter (once per 20 ticks) */
	public int ticksSinceSync;

	/**
	 * 
	 * @param type
	 * @param pos
	 * @param state
	 */
	public AbstractTreasureChestBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		setFacing(Direction.NORTH.get3DDataValue());
	}

	/**
	 * 
	 */
	@Override
	public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player playerEntity) {
		Treasure.LOGGER.debug("is chest sealed -> {}", this.isSealed());
		if (this.isSealed()) {
			this.setSealed(false);
//			Treasure.LOGGER.debug("chest gen type -> {}", this.getGenerationContext().getChestGeneratorType()); 
//			IChestGeneratorType chestGeneratorType = this.getGenerationContext().getChestGeneratorType();
			IRarity rarity = this.getGenerationContext().getLootRarity();
			Optional<IChestGenerator> chestGenerator = ChestGeneratorRegistry.get(rarity);
			if (chestGenerator.isPresent()) {
				Treasure.LOGGER.debug("chest gen  -> {}", chestGenerator.get().getClass().getSimpleName());
				// fill the chest with loot
				chestGenerator.get().fillChest(getLevel(), getLevel().getRandom(), this, this.getGenerationContext().getLootRarity(), playerEntity);
			}
			else {
				Treasure.LOGGER.warn("treasure chest at -> {} does not reference a valid generator -> {}", this.worldPosition, chestGenerator.get().getClass().getSimpleName());
			}
		}
		return createChestContainerMenu(windowId, playerInventory, playerEntity);
	}

	/**
	 * 
	 * @param windowId
	 * @param playerInventory
	 * @param playerEntity
	 * @return
	 */
	public AbstractContainerMenu createChestContainerMenu(int windowId, Inventory playerInventory, Player playerEntity) {
		return new StandardChestContainerMenu(windowId, this.worldPosition, playerInventory, playerEntity);
	}

	/**
	 * 
	 * @return
	 */
	private ItemStackHandler createHandler() {
		return new ItemStackHandler(getInventorySize()) {

			@Override
			protected void onContentsChanged(int slot) {
				// To make sure the BE persists when the chunk is saved later we need to
				// mark it dirty every time the item handler changes
				setChanged();
			}

			@Override
			public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
				return !isLocked();
			}

			@Nonnull
			@Override
			public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
				if (isLocked()) {
					return ItemStack.EMPTY;
				}
				return super.insertItem(slot, stack, simulate);
			}
			
			@Override
			public @NotNull ItemStack getStackInSlot(int slot) {
				if (isLocked()) {
					return ItemStack.EMPTY;
				}
				return super.getStackInSlot(slot);
			}
			
			@Override
			public void setStackInSlot(int slot, @NotNull ItemStack stack) {
				if (isLocked()) {
					return;
				}
				super.setStackInSlot(slot, stack);
			}
		};
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if (cap == ForgeCapabilities.ITEM_HANDLER) {
			return instanceHandler.cast();
		}
		return super.getCapability(cap, side);
	}

	/**
	 * 
	 * @param level
	 * @param pos
	 */
	public void dropContents(Level level, BlockPos pos) {
		Optional<IItemHandler> handler = 	getCapability(ForgeCapabilities.ITEM_HANDLER).map(h -> h);
		if (handler.isPresent()) {
			int numberOfSlots = handler.get().getSlots();
			for (int i = 0; i < numberOfSlots; i++) {
				ItemStack stack = handler.get().getStackInSlot(i);
				if (stack != null && stack != ItemStack.EMPTY) {
					Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack);
				}
			}
		}
	}

	/**
	 * 
	 */
	@Override
	public void tickClient() {
		this.prevLidAngle = this.lidAngle;
		if (this.openCount > 0 && this.lidAngle == 0.0F) {
			doChestOpenEffects(level, null, getBlockPos());
//			this.playSound(SoundEvents.CHEST_OPEN);
		}

		if (this.openCount == 0 && this.lidAngle > 0.0F || this.openCount > 0 && this.lidAngle < 1.0F) {
			float f2 = this.lidAngle;

			if (this.openCount > 0) {
				this.lidAngle += 0.1F;
			} else {
				this.lidAngle -= 0.1F;
			}

			if (this.lidAngle > 1.0F) {
				this.lidAngle = 1.0F;
			}

			//float f3 = 0.5F;
			if (this.lidAngle < 0.5F && f2 >= 0.5F) {
				doChestCloseEffects(level, null, getBlockPos());
//				this.playSound(SoundEvents.CHEST_CLOSE);
			}

			if (this.lidAngle < 0.0F) {
				this.lidAngle = 0.0F;
			}
		}	
	}

	@Override
	public void tickParticle() {
		if (Config.SERVER.effects.enableUndiscoveredEffects.get()
				&& !getBlockState().getValue(AbstractTreasureChestBlock.DISCOVERED)) {

			// TODO move this to IChestEffects

			if (getLevel().getGameTime() % 10 == 0) {
				RandomSource random = getLevel().getRandom();
				for(int k = 0; k < 5; ++k) {
					SimpleParticleType coinParticle;
					int x = k % 3;
					if (x == 0) {
						coinParticle = TreasureParticles.COPPER_COIN_PARTICLE.get();
					} else if (x == 1) {
						coinParticle = TreasureParticles.SILVER_COIN_PARTICLE.get();
					} else {
						coinParticle = TreasureParticles.GOLD_COIN_PARTICLE.get();
					}
					getLevel().addParticle(coinParticle, 
							(double)getBlockPos().getX() + 0.5D + random.nextDouble() / 3.0D * (double)(random.nextBoolean() ? 1 : -1), 
							(double)getBlockPos().getY() + random.nextDouble() + random.nextDouble(),
							(double)getBlockPos().getZ() + 0.5D + random.nextDouble() / 3.0D * (double)(random.nextBoolean() ? 1 : -1),
							0.0D, 0.07D, 0.0D);
				}
			}
		}
	}


	@Override
	public void tickServer() {
	}
	
	/**
	 * 
	 * @param sound
	 */
	protected void playSound(SoundEvent sound) {
		double d0 = (double)getBlockPos().getX() + 0.5D;
		double d1 = (double)getBlockPos().getY() + 0.5D;
		double d2 = (double)getBlockPos().getZ() + 0.5D;
		// TODO ensure that other players can hear the chest opening as it is playLocalSound()
		level.playLocalSound(d0, d1, d2, sound, SoundSource.BLOCKS, 1.0F, 1.0F, false);
	}

	@Override
	public void onLoad() {
		super.onLoad();
		instanceHandler = LazyOptional.of(() -> itemHandler);
	}

	@Override
	public void invalidateCaps() {
		super.invalidateCaps();
		instanceHandler.invalidate();
	}

	@Override
	public void saveAdditional(CompoundTag tag) {
		tag.put("inventory", itemHandler.serializeNBT());
		// write lock states
		saveLockStates(tag);
		saveProperties(tag);
		super.saveAdditional(tag);
	}

	/**
	 * 
	 * @param tag
	 * @return
	 */
	public CompoundTag saveLockStates(CompoundTag tag) {
		try {
			// write lock states
			if (getLockStates() != null && !getLockStates().isEmpty()) {
				ListTag list = new ListTag();
				// write custom tile entity properties
				for (LockState state : getLockStates()) {
					Treasure.LOGGER.trace("saving lock state:" + state);
					CompoundTag stateTag = new CompoundTag();
					state.save(stateTag);
					list.add(stateTag);
				}
				tag.put(LOCK_STATES_TAG, list);
			}
		} catch (Exception e) {
			Treasure.LOGGER.error("error writing LockStates to nbt:", e);
		}
		return tag;
	}

	public CompoundTag saveProperties(CompoundTag tag) {
		try {
			// write facing
			tag.putInt(FACING_TAG, getFacing().get3DDataValue());
			tag.putBoolean(SEALED_TAG, isSealed());
			if (getLootTable() != null) {
				tag.putString(LOOT_TABLE_TAG, getLootTable().toString());
			}

			if (getMimic() != null) {
				tag.putString(MIMIC_TAG, getMimic().toString());
			}
			if (getGenerationContext() != null) {
				CompoundTag contextTag = new CompoundTag();
				contextTag.putString(LOOT_RARITY_TAG, getGenerationContext().getLootRarity().getName());
				contextTag.putString(FEATURE_TYPE_TAG, getGenerationContext().getFeatureType().getName());
				tag.put(GENERATION_CONTEXT_TAG, contextTag);
			}
		} catch (Exception e) {
			Treasure.LOGGER.error("error writing Properties to nbt:", e);
		}
		return tag;
	}

	/**
	 * 
	 */
	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		try {
			itemHandler.deserializeNBT(tag.getCompound("inventory"));
			loadLockStates(tag);
			loadProperties(tag);
		} catch (Exception e) {
			Treasure.LOGGER.error("error reading to nbt:", e);
		}		
	}

	/**
	 * Same as load but does not call super.load()
	 * @param tag
	 */
	public void loadFromItem(CompoundTag tag) {
		try {
			itemHandler.deserializeNBT(tag.getCompound("inventory"));
			loadLockStates(tag);
			loadProperties(tag);
		} catch (Exception e) {
			Treasure.LOGGER.error("error reading to nbt:", e);
		}
	}

	/**
	 * 
	 * @param tag
	 */
	public void loadLockStates(CompoundTag tag) {
		try {
			// read the lockstates
			if (tag.contains(LOCK_STATES_TAG)) {
				//	Treasure.LOGGER.info("Has lockStates");
				if (this.getLockStates() != null) {
					//	Treasure.LOGGER.info("size of internal lockstates:" + this.getLockStates().size());
				} else {
					this.setLockStates(new LinkedList<LockState>());
					//	Treasure.LOGGER.info("created lockstates:" + this.getLockStates().size());
				}

				List<LockState> states = new LinkedList<LockState>();
				ListTag list = tag.getList(LOCK_STATES_TAG, Tag.TAG_COMPOUND);
				for (int i = 0; i < list.size(); i++) {
					CompoundTag c = list.getCompound(i);
					LockState lockState = LockState.load(c);
					states.add(lockState.getSlot().getIndex(), lockState);
					//	Treasure.LOGGER.info("Read NBT lockstate:" + lockState);
				}
				// update the tile entity
				setLockStates(states);
			}
		} catch (Exception e) {
			Treasure.LOGGER.error("error reading Lock States from nbt:", e);
		}
	}

	public void loadProperties(CompoundTag tag) {
		try {
			// read the facing
			if (tag.contains(FACING_TAG)) {
				this.setFacing(tag.getInt(FACING_TAG));
			}
			if (tag.contains(SEALED_TAG)) {
				this.setSealed(tag.getBoolean(SEALED_TAG));
			}
			if (tag.contains(LOOT_TABLE_TAG)) {
				if (!tag.getString(LOOT_TABLE_TAG).isEmpty()) {
					this.setLootTable(new ResourceLocation(tag.getString(LOOT_TABLE_TAG)));
				}
			}
			if (tag.contains(MIMIC_TAG)) {
				if (!tag.getString(MIMIC_TAG).isEmpty()) {
					this.setMimic(new ResourceLocation(tag.getString(MIMIC_TAG)));
				}
			}
			if (tag.contains(GENERATION_CONTEXT_TAG)) {
				CompoundTag contextTag = tag.getCompound(GENERATION_CONTEXT_TAG);
				Optional<IRarity> rarity = Optional.empty();
				if (contextTag.contains(LOOT_RARITY_TAG)) {
					rarity = TreasureApi.getRarity(contextTag.getString(LOOT_RARITY_TAG));
				}
				Optional<IFeatureType> featureType = Optional.empty();
				if (contextTag.contains(FEATURE_TYPE_TAG)) {
					featureType = TreasureApi.getFeatureType(contextTag.getString(FEATURE_TYPE_TAG));
				}

				AbstractTreasureChestBlockEntity.GenerationContext generationContext = 
						this.new GenerationContext(
								rarity.orElse(Rarity.NONE), 
								featureType.orElse(FeatureType.UNKNOWN));
				this.setGenerationContext(generationContext);
			}	
		} catch (Exception e) {
			Treasure.LOGGER.error("error reading Properties from nbt:", e);
		}
	}

	@Override
	public boolean isLocked() {
		return hasLocks();
	}

	/**
	 * 
	 */
	@Override
	public boolean hasLocks() {
		// TODO TEMP do this for now. should have another property numActiveLocks so
		// that the renderer doesn't keep calling this
		if (getLockStates() == null || getLockStates().isEmpty()) {
			return false;
		}
		for (LockState state : getLockStates()) {
			if (state.getLock() != null)
				return true;
		}
		return false;
	}

	@Override
	public Component getName() {
		return this.name != null ? this.name : this.getDefaultName();
	}

	@Override
	public Component getDisplayName() {
		return this.getName();
	}

	@Override
	public Component getDefaultName() {
		return Component.translatable(LangUtil.screen("default_chest.name"));
	}

	@Override
	public Component getCustomName() {
		return this.name;
	}

	public void setCustomName(Component name) {
		this.name = name;
	}

	/**
	 * Sync client and server states
	 */
	@Override
	public void sendUpdates() {		
		BlockState blockState = level.getBlockState(getBlockPos());
		level.sendBlockUpdated(getBlockPos(), blockState, blockState, 3);
		setChanged();
	}

	@Override
	public CompoundTag getUpdateTag() {
		CompoundTag tag = super.getUpdateTag();
		saveAdditional(tag);
		return tag;
	}

	@Override
	public void handleUpdateTag(CompoundTag tag) {
		if (tag != null) {
			load(tag);
		}
	}

	@Nullable
	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
		CompoundTag tag = pkt.getTag();
		handleUpdateTag(tag);
	}

	@Override
	public void setRemoved() {
		super.setRemoved();
		instanceHandler.invalidate();
	}

	@Override
	public List<LockState> getLockStates() {
		return lockStates;
	}

	@Override
	public void setLockStates(List<LockState> lockStates) {
		this.lockStates = lockStates;
	}

	@Override
	public Direction getFacing() {
		return facing;
	}	
	public void setFacing(Direction facing) {
		this.facing = facing;
	}
	@Override
	public void setFacing(int facingIndex) {
		this.facing = Direction.from3DDataValue(facingIndex);		
	}

	@Override
	public boolean isSealed() {
		return sealed;
	}
	@Override
	public void setSealed(boolean sealed) {
		this.sealed = sealed;
	}

	@Override
	public ResourceLocation getLootTable() {
		return lootTable;
	}

	@Override
	public void setLootTable(ResourceLocation lootTable) {
		this.lootTable = lootTable;
	}


	@Override
	public GenerationContext getGenerationContext() {
		return generationContext;
	}

	@Override
	public void setGenerationContext(GenerationContext generationContext) {
		this.generationContext = generationContext;
	}
	
	@Override
	public ResourceLocation getMimic() {
		return mimic;
	}

	@Override
	public void setMimic(ResourceLocation mimic) {
		this.mimic = mimic;
	}

	/*
	 * TODO think of something better here. There already is ChestGeneratedContext
	 */
	public class GenerationContext {
		/*
		 * The rarity level of the loot that the chest will contain
		 */
		private IRarity lootRarity;
		private IFeatureType featureType;

		public GenerationContext(IRarity rarity, IFeatureType featureType) {
			this.lootRarity = rarity;
			this.featureType = featureType;
		}
		
		public GenerationContext(ResourceLocation lootTable, IRarity rarity, IFeatureType featureType) {
			AbstractTreasureChestBlockEntity.this.lootTable = lootTable;
			this.lootRarity = rarity;
			this.featureType = featureType;
		}

		public IRarity getLootRarity() {
			return lootRarity;
		}

		public ResourceLocation getLootTable() {
			return AbstractTreasureChestBlockEntity.this.lootTable;
		}

		public IFeatureType getFeatureType() {
			return featureType;
		}

		public void setFeatureType(IFeatureType type) {
			this.featureType = type;
		}
	}

}
