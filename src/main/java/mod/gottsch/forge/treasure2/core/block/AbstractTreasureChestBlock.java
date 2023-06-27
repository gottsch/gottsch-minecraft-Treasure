/*
 * This file is part of  Treasure2.
 * Copyright (c) 2018 Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.core.block;


import java.lang.reflect.Constructor;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

import mod.gottsch.forge.gottschcore.enums.IRarity;
import mod.gottsch.forge.gottschcore.spatial.Coords;
import mod.gottsch.forge.gottschcore.spatial.Heading;
import mod.gottsch.forge.gottschcore.spatial.ICoords;
import mod.gottsch.forge.gottschcore.spatial.Rotate;
import mod.gottsch.forge.gottschcore.world.WorldInfo;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.block.entity.AbstractTreasureChestBlockEntity;
import mod.gottsch.forge.treasure2.core.block.entity.ITreasureChestBlockEntity;
import mod.gottsch.forge.treasure2.core.entity.monster.Mimic;
import mod.gottsch.forge.treasure2.core.enums.Rarity;
import mod.gottsch.forge.treasure2.core.lock.ILockSlot;
import mod.gottsch.forge.treasure2.core.lock.LockLayout;
import mod.gottsch.forge.treasure2.core.lock.LockState;
import mod.gottsch.forge.treasure2.core.network.MimicSpawnS2C;
import mod.gottsch.forge.treasure2.core.network.TreasureNetworking;
import mod.gottsch.forge.treasure2.core.registry.ChestRegistry;
import mod.gottsch.forge.treasure2.core.registry.DimensionalGeneratedCache;
import mod.gottsch.forge.treasure2.core.registry.GeneratedCache;
import mod.gottsch.forge.treasure2.core.registry.support.GeneratedChestContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * @author Mark Gottschling on Sep 16, 2018
 *
 */
public class AbstractTreasureChestBlock extends BaseEntityBlock implements ITreasureChestBlock {


	private static final VoxelShape CHEST = Block.box(1, 0, 1, 15, 14, 15);


	/*
	 * an array of VoxelShape shapes for the bounding box
	 */
	private VoxelShape[] bounds = new VoxelShape[4];

	/*
	 *  the class of the tileEntityClass this BlockChest should use.
	 */
	private final Class<?> blockEntityClass;

	/*
	 *  an instance of the blockEntity defined by blockEntityClass
	 */
	private final AbstractTreasureChestBlockEntity blockEntityInstance;

	/*
	 * the type of chest
	 */
	private LockLayout lockLayout;

	/**
	 * 
	 * @param properties
	 */
	public AbstractTreasureChestBlock(Class<? extends AbstractTreasureChestBlockEntity> be, LockLayout lockLayout, Properties properties) {
		super(properties);
		this.blockEntityClass = be;
		this.lockLayout = lockLayout;
		this.blockEntityInstance = newInstanceBlockEntity(new BlockPos(0,0,0), null);

		setBounds(
				new VoxelShape[] {
						CHEST, 	// N
						CHEST,  	// E
						CHEST,  	// S
						CHEST		// W
				});		

		// set the default state
		registerDefaultState(
				this.stateDefinition.any()
				.setValue(FACING, Direction.NORTH)
				.setValue(DISCOVERED, true)
				);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {

		AbstractTreasureChestBlockEntity chestTileEntity = null;
		try {
			chestTileEntity = newInstanceBlockEntity(pos, state);

			// setup lock states
			List<LockState> lockStates = new LinkedList<>();

			for (int i = 0; i < lockLayout.getSlots().length; i++) {
				LockState lockState = new LockState();
				lockState.setSlot(lockLayout.getSlots()[i]);
				// add in order of slot indexes
				lockStates.add(lockState.getSlot().getIndex(), lockState);
			}
			chestTileEntity.setLockStates(lockStates);
			Treasure.LOGGER.info("AbstractTreasureChestBlock | newBlockEntity | lockStates -> {}", chestTileEntity.getLockStates());
			Treasure.LOGGER.info("AbstractTreasureChestBlock | newBlockEntity | tileEntity -> {} @ {}", chestTileEntity, chestTileEntity.getBlockPos());
		}
		catch(Exception e) {
			Treasure.LOGGER.error(e);
		}
		return chestTileEntity;
	}

	/**
	 * 
	 * @param pos
	 * @param state
	 * @return
	 */
	protected AbstractTreasureChestBlockEntity newInstanceBlockEntity(BlockPos pos, BlockState state) {
		/*
		 *  construct a new instance of the block entity.
		 *  ensure to use BlockPos.class and not pos.getClass() for the Class<?> type variable
		 *  because when this method is called from load, a MutableBlockPos is passed in, 
		 *  and reflection will not be able to locate the constructor because it has a different signature, 
		 *  and this will save from having every concrete Chest class needing to implement a separate 
		 *  constructor for the MutableBlockPos.
		 */
		try {
			Class<?>[] type = { BlockPos.class, BlockState.class };
			Constructor<?> cons = getBlockEntityClass().getConstructor(type);			
			return (AbstractTreasureChestBlockEntity) cons.newInstance(pos, state);
		}
		catch(Exception e) {
			Treasure.LOGGER.error(e);
			return null;
		}
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		if (level.isClientSide()) {
			return (lvl, pos, blockState, t) -> {
				if (t instanceof ITreasureChestBlockEntity entity) { // test and cast
					entity.tickClient();
					entity.tickParticle();
				}
			};
		}
		else {
			//			return (lvl, pos, blockState, t) -> {
			//				if (t instanceof ITreasureChestBlockEntity entity) { // test and cast
			//					entity.tickServer();
			//				}
			//			};
			return null;
		}
	}

	/**
	 * 
	 */
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder
		.add(FACING)
		.add(DISCOVERED);
	}

	/**
	 * 
	 */
	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.ENTITYBLOCK_ANIMATED;
	}

	/**
	 * Called just after the player places a block.
	 */
	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		// face the block towards the player
		level.setBlock(pos, state.setValue(FACING, placer.getDirection().getOpposite()), 3);

		BlockEntity blockEntity = level.getBlockEntity(pos);
		if (blockEntity != null && blockEntity instanceof AbstractTreasureChestBlockEntity) {
			AbstractTreasureChestBlockEntity be = (AbstractTreasureChestBlockEntity)blockEntity;

			boolean isDirty = false;
			Heading previousChestHeading = Heading.NORTH;

			// set the custom name if any
			if (stack.hasCustomHoverName()) {
				be.setCustomName(stack.getHoverName());
			}

			// TODO should this change that the chest block item stack have ItemHandler capability?
			// load inventory from item stack into block entity
			if (stack.hasTag()) {
				be.loadFromItem(stack.getTag());
				isDirty = true;

				// capture the previous (persisted in the item) facing direction of the block entity
				// NOTE this determines the previous orientation of the locks
				previousChestHeading = Heading.fromDirection(be.getFacing());
			}

			// get the direction the block is facing currently
			Heading heading = Heading.fromDirection(placer.getDirection().getOpposite());

			// rotate the lock states on the block entity
			isDirty |= rotateLockStates(level, new Coords(pos), previousChestHeading.getRotation(heading));

			Treasure.LOGGER.debug("new lock states ->");
			for (LockState ls : be.getLockStates()) {
				Treasure.LOGGER.debug(ls);
			}

			// update the facing on the block entity
			be.setFacing(placer.getDirection().getOpposite());

			if (isDirty) {
				// update the client
				be.sendUpdates();
			}
		}
	}

	/**
	 * 
	 */
	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
			InteractionHand hand, BlockHitResult result) {
		
		if (WorldInfo.isClientSide(level)) {
			return InteractionResult.SUCCESS;
		}

		Treasure.LOGGER.debug("using treasure chest...");

		AbstractTreasureChestBlockEntity blockEntity = (AbstractTreasureChestBlockEntity) level.getBlockEntity(pos);
		if (blockEntity != null) {
			// check for mimic
			if (blockEntity.getMimic() != null) {
				spawnMimic(level, level.getRandom(), state, pos, player);
			}
			else if (!blockEntity.isLocked()) {			
				// if not discovered then set as discovered
				if (!state.getValue(DISCOVERED)) {
					// mark as discovered
					blockEntity = discovered(blockEntity, state, level, pos, player);
				}			
				NetworkHooks.openScreen((ServerPlayer) player, blockEntity, pos);
			}		
		}
		return InteractionResult.SUCCESS;
	}

	/**
	 * 
	 * @param level
	 * @param random
	 * @param state
	 * @param pos
	 * @param player
	 */
	protected void spawnMimic(Level level, RandomSource random, BlockState state, BlockPos pos, Player player) {
		AbstractTreasureChestBlockEntity blockEntity = (AbstractTreasureChestBlockEntity) level.getBlockEntity(pos);
		EntityType<?> entityType = ForgeRegistries.ENTITY_TYPES.getHolder(blockEntity.getMimic()).get().value();

		// remove the block entity
		level.removeBlock(pos, true);

		// calculate yRot
		float yRot = switch(state.getValue(FACING)) {
		case DOWN, UP, SOUTH -> 0f;
		case NORTH -> 180f;
		case EAST -> -90f;
		case WEST -> 90f;
		default -> 0f;
		};

		Mob mob = spawn((ServerLevel)level, level.getRandom(), entityType, pos, player, yRot);
		if (mob != null) {
			// update the loot table in the mimic
			((Mimic)mob).setLootTable(blockEntity.getLootTable());			
			// update client
			MimicSpawnS2C message = new MimicSpawnS2C(mob.getId(), yRot);
			TreasureNetworking.channel.send(PacketDistributor.TRACKING_ENTITY.with(() -> mob), message);
		}
	}

	/**
	 * 
	 * @param level
	 * @param randomSource
	 * @param owner
	 * @param pos
	 * @param target
	 * @return
	 */
	protected Mob spawn(ServerLevel level, RandomSource randomSource, EntityType<?> entityType, BlockPos pos, LivingEntity target, float yRot) {
		double spawnX = pos.getX() + 0.5;
		double spawnY = pos.getY();
		double spawnZ = pos.getZ() + 0.5;
		if (!WorldInfo.isClientSide(level)) {
			SpawnPlacements.Type placement = SpawnPlacements.getPlacementType(entityType);
			if (NaturalSpawner.isSpawnPositionOk(placement, level, pos, entityType)) {
				Mob mob = (Mob)entityType.create(level);
				mob.setPos(spawnX, spawnY, spawnZ);
				mob.setTarget(target);
				mob.setYRot(yRot);
				mob.yRotO = yRot;
				level.addFreshEntityWithPassengers(mob);
				return mob;			
			}
		}
		return null;
	}

	/**
	 * 
	 * @param blockEntity
	 * @param state
	 * @param level
	 * @param pos
	 * @param player
	 * @return
	 */
	public AbstractTreasureChestBlockEntity discovered(AbstractTreasureChestBlockEntity blockEntity, BlockState state, Level level, BlockPos pos, Player player) {
		if (level.isClientSide()) {
			return blockEntity;
		}

		// save chest BE to tag
		CompoundTag tag = new CompoundTag();
		blockEntity.saveAdditional(tag);

		// save chest state
		BlockState oldState = state;

		// TODO need to check for ITreasureChestProxy or need a multiple block flag.
		
		// place new chest with old state, but different light
		level.setBlockAndUpdate(pos, oldState.getBlock()
				.defaultBlockState()
				.setValue(AbstractTreasureChestBlock.FACING, oldState.getValue(AbstractTreasureChestBlock.FACING))
				.setValue(AbstractTreasureChestBlock.DISCOVERED, true));

		// load be from item
		AbstractTreasureChestBlockEntity newBlockEntity = (AbstractTreasureChestBlockEntity) level.getBlockEntity(pos);
		newBlockEntity.loadProperties(tag);
		newBlockEntity.sendUpdates();

		// update chest context discovered in the chest cache
		ResourceLocation dimension = (level.dimensionType().effectsLocation());
		if (newBlockEntity.getGenerationContext() != null) {
			Treasure.LOGGER.debug("attempting to get chest cache for dimension -> {}, featureType -> {}", dimension, newBlockEntity.getGenerationContext().getFeatureType());
			GeneratedCache<GeneratedChestContext> cache = DimensionalGeneratedCache.getChestGeneratedCache(dimension, newBlockEntity.getGenerationContext().getFeatureType());
			if (cache != null) {
				Optional<GeneratedChestContext> context = cache.get(newBlockEntity.getGenerationContext().getLootRarity(), new Coords(pos).toShortString());
				if (context.isPresent()) {
					context.get().setDiscovered(true);
					Treasure.LOGGER.debug("updating chest in cache to discovered -> {}", pos.toShortString());
				}
			}
		}

		return newBlockEntity;
	}

	/**
	 * 
	 */
	@Override
	public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest,
			FluidState fluid) {

		if (WorldInfo.isClientSide(level)) {
			return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
		}	
		Treasure.LOGGER.debug("chest destroyed by player....!");
		breakChest(state, level, pos, player);

		return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
	}

	/**
	 * 
	 */
	@Override
	public void onBlockExploded(BlockState state, Level level, BlockPos pos, Explosion explosion) {
		if (WorldInfo.isClientSide(level)) {
			super.onBlockExploded(state, level, pos, explosion);
			return;
		}
		Treasure.LOGGER.debug("chest exploded....!");
		breakChest(state, level ,pos, null);

		super.onBlockExploded(state, level, pos, explosion);
	}

	/**
	 * NOTE breakChest() needs to be called from onDestroyedByPlayer() and onBlockExploded() instead of
	 * onRemove() because of the why the light emission is handled. When the chest is replaced to recalculate the light
	 * emission we don't want the chest to drop itself.
	 * @param state
	 * @param level
	 * @param pos
	 * @param player
	 * @return
	 */
	public boolean breakChest(BlockState state, Level level, BlockPos pos, Player player) {		
		BlockEntity blockEntity = level.getBlockEntity(pos);
		if (blockEntity != null) {

			if (blockEntity instanceof AbstractTreasureChestBlockEntity) {
				AbstractTreasureChestBlockEntity be = (AbstractTreasureChestBlockEntity)blockEntity;

				// unlocked!
				if (!be.isLocked()) {
					// drop the be's inventory
					be.dropContents(level, pos);

					// spawn chest item into the world
					ItemStack chestItem = new ItemStack(Item.byBlock(this), 1);
					Containers.dropItemStack(level, (double) pos.getX(), (double) pos.getY(), (double) pos.getZ(),
							chestItem);

					// write the properties to the nbt
					if (!chestItem.hasTag()) {
						chestItem.setTag(new CompoundTag());
					}
					be.saveProperties(chestItem.getTag());
				}
				else {
					if (WorldInfo.isServerSide(level)) {
						ItemStack chestItem = new ItemStack(Item.byBlock(this), 1);
						// give the chest a tag compound
						CompoundTag tag = new CompoundTag();
						be.saveAdditional(tag);
						chestItem.setTag(tag);
						Containers.dropItemStack(level, (double) pos.getX(), (double) pos.getY(), (double) pos.getZ(),
								chestItem);
					}
				}
			}
		}		
		return true;
	}

	/**
	 * 
	 */
	//	@Override
	//	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
	//		if (WorldInfo.isClientSide(level)) {
	//			return;
	//		}		
	//		Treasure.LOGGER.debug("removing chest....!");
	//		
	//		
	//
	//			// remove the tile entity
	//			level.removeBlockEntity(pos);
	//		}
	//		else {
	//			super.onRemove(state, level, pos, newState, isMoving);
	//		}
	//	}

	/**
	 * 
	 * @param level
	 * @param pos
	 * @param rotate
	 * @return
	 */
	public boolean  rotateLockStates(CommonLevelAccessor level, ICoords coords, Rotate rotate) {
		boolean hasRotated = false;
		boolean shouldRotate = false;
		if (rotate != Rotate.NO_ROTATE) {
			shouldRotate = true;
		}
		//		Treasure.LOGGER.debug("rotate to:" + rotate);

		AbstractTreasureChestBlockEntity be = null;
		BlockEntity blockEntity = level.getBlockEntity(coords.toPos());
		if (blockEntity != null && blockEntity instanceof AbstractTreasureChestBlockEntity) {
			be = (AbstractTreasureChestBlockEntity) blockEntity;
		}
		else {
			return false;
		}

		try {
			for (LockState lockState : be.getLockStates()) {
				if (lockState != null && lockState.getSlot() != null) {
					//	Treasure.LOGGER.debug("original lock state:" + lockState);
					// if a rotation is needed
					if (shouldRotate) {
						ILockSlot newSlot = lockState.getSlot().rotate(rotate);
						//	Treasure.LOGGER.debug("new slot position:" + newSlot);
						lockState.setSlot(newSlot);
						// set the flag to indicate the lockStates have rotated
						hasRotated = true;
					}
				}
			}
		}
		catch(Exception e) {
			Treasure.LOGGER.error("error updating lock states: ", e);
		}
		return hasRotated;
	}

	/**
	 * 
	 */
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		switch(state.getValue(FACING)) {
		default:
		case NORTH:
			return bounds[0];
		case EAST:
			return bounds[1];
		case SOUTH:
			return bounds[2];
		case WEST:
			return bounds[3];
		}
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockState blockState = this.defaultBlockState().setValue(FACING,
				context.getHorizontalDirection().getOpposite());
		return blockState;
	}

	@Override
	public Class<?> getBlockEntityClass() {
		return blockEntityClass;
	}

	@Override
	public LockLayout getLockLayout() {
		return lockLayout;
	}

	public void setLockLayout(LockLayout layout) {
		this.lockLayout = layout;
	}

	/**
	 * Wrapper for call to the ChestRegistry and handles null values.
	 */
	@Override
	public IRarity getRarity() {
		IRarity rarity = ChestRegistry.getRarity(this);
		if (rarity == null) {
			return Rarity.NONE;
		}
		return rarity;
	}

	@Override
	public VoxelShape[] getBounds() {
		return bounds;
	}

	@Override
	public AbstractTreasureChestBlock setBounds(VoxelShape[] bounds) {
		this.bounds = bounds;
		return this;
	}

	public AbstractTreasureChestBlockEntity getBlockEntityInstance() {
		return blockEntityInstance;
	}
}
