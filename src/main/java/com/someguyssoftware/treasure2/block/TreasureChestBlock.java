/**
 * 
 */
package com.someguyssoftware.treasure2.block;

import com.someguyssoftware.gottschcore.spatial.Coords;
import com.someguyssoftware.gottschcore.spatial.Heading;
import com.someguyssoftware.gottschcore.spatial.ICoords;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.chest.TreasureChestType;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.pathfinding.PathType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

// TODO most of everything in here can go in AbstractChestBlock
/**
 * @author Mark Gottschling on Jan 9, 2018
 *
 */
public class TreasureChestBlock extends AbstractChestBlock<AbstractTreasureChestTileEntity> {

	//	private static final VoxelShape AABB = Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 14.0D, 14.0D, 14.0D);

	/**
	 * 
	 * @param modID
	 * @param name
	 * @param tileEntity
	 * @param type
	 */
	public TreasureChestBlock(String modID, String name, Class<? extends AbstractTreasureChestTileEntity> tileEntity,
			TreasureChestType type, Rarity rarity) {
		this(modID, name, tileEntity, type, rarity, Block.Properties.create(Material.WOOD));
	}

	/**
	 * 
	 * @param modID
	 * @param name
	 * @param material
	 * @param tileEntity
	 * @param type
	 */
	public TreasureChestBlock(String modID, String name, Class<? extends AbstractTreasureChestTileEntity> tileEntity, TreasureChestType type, Rarity rarity, Block.Properties properties) {
		super(modID, name, tileEntity, type, rarity, properties);
	}


	/**
	 * 
	 */
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return this.getBounds()[0];
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		BlockState blockState = this.getDefaultState().with(FACING,
				context.getPlacementHorizontalFacing().getOpposite());
		return blockState;
	}

	/**
	 * 
	 */
	@Override
	public boolean isNormalCube(BlockState state, IBlockReader world, BlockPos pos) {
		return false;
	}

	/**
	 * Called just after the player places a block.
	 */
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		Treasure.LOGGER.debug("Placing chest from item");

		boolean shouldRotate = false;
		boolean shouldUpdate = false;
		boolean forceUpdate = false;
		AbstractTreasureChestTileEntity tcte = null;
		Heading oldPersistedChestDirection = Heading.NORTH;

		// face the block towards the player (there isn't really a front)
		worldIn.setBlockState(pos, state.with(FACING, placer.getHorizontalFacing().getOpposite()), 3);
		TileEntity te = worldIn.getTileEntity(pos);
		if (te != null && te instanceof AbstractTreasureChestTileEntity) {
			// get the backing tile entity
			tcte = (AbstractTreasureChestTileEntity) te;

			// set the name of the chest
			if (stack.hasDisplayName()) {
				tcte.setCustomName(stack.getDisplayName());
			}

			// read in nbt
			if (stack.hasTag()) {
				tcte.readFromItemStackNBT(stack.getTag());
				forceUpdate = true;

				// get the old tcte facing direction
				oldPersistedChestDirection = Heading.fromDirection(tcte.getFacing()); // TODO might be byHorizontalIndex

				// dump stack NBT
				if (Treasure.LOGGER.isDebugEnabled()) {
					dump(stack.getTag(), new Coords(pos), "STACK ITEM -> CHEST NBT");
				}
			}

			// get the direction the block is facing.
			Heading direction = Heading.fromDirection(placer.getHorizontalFacing().getOpposite());

			// rotate the lock states
			shouldUpdate = rotateLockStates(worldIn, pos, oldPersistedChestDirection.getRotation(direction)); // old ->
			// Direction.NORTH
			// //

			//			Treasure.LOGGER.debug("New lock states ->");
			//			for (LockState ls : tcte.getLockStates()) {
			//				Treasure.LOGGER.debug(ls);
			//			}

			// update the TCTE facing
			tcte.setFacing(placer.getHorizontalFacing().getOpposite().getIndex());
		}
		if ((forceUpdate || shouldUpdate) && tcte != null) {
			// update the client
			tcte.sendUpdates();
		}
	}

	/**
	 * 
	 */
	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player,
			Hand handIn, BlockRayTraceResult hit) {

		AbstractTreasureChestTileEntity tileEntity = (AbstractTreasureChestTileEntity) world.getTileEntity(pos);

		// exit if on the client
		if (WorldInfo.isClientSide(world)) {
			return ActionResultType.SUCCESS;
		}

		boolean isLocked = false;
		// determine if chest is locked
		if (!tileEntity.hasLocks()) {
			// get the container provider
			INamedContainerProvider namedContainerProvider = this.getContainer(state, world, pos);			
			// open the chest
			NetworkHooks.openGui((ServerPlayerEntity)player, namedContainerProvider, (packetBuffer)->{});
			// NOTE: (packetBuffer)->{} is just a do-nothing because we have no extra data to send
		}

		return ActionResultType.SUCCESS;
	}


	@Override
	public void onPlayerDestroy(IWorld worldIn, BlockPos pos, BlockState state) {
		Treasure.LOGGER.info("block destroyed by player. should happen after block is broken/replaced");
		super.onPlayerDestroy(worldIn, pos, state);
	}


	@Override
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		Treasure.LOGGER.debug("Breaking block....!");

		TileEntity tileEntity = worldIn.getTileEntity(pos);
		AbstractTreasureChestTileEntity te = null;
		if (tileEntity instanceof AbstractTreasureChestTileEntity) {
			te = (AbstractTreasureChestTileEntity)tileEntity;
		}

		if (te != null) {
			// unlocked!
			if (!te.hasLocks()) {
				/*
				 * spawn inventory items
				 */
				InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory) te);

				/*
				 * spawn chest item
				 */
				ItemStack chestItem = new ItemStack(Item.getItemFromBlock(this), 1);
				Treasure.LOGGER.debug("Item being created from chest -> {}", chestItem.getItem().getRegistryName());
				InventoryHelper.spawnItemStack(worldIn, (double) pos.getX(), (double) pos.getY(), (double) pos.getZ(),
						chestItem);

				/*
				 * write the properties to the nbt
				 */
				if (!chestItem.hasTag()) {
					chestItem.setTag(new CompoundNBT());
				}
				te.writePropertiesToNBT(chestItem.getTag());
			} else {
				Treasure.LOGGER.debug("[BreakingBlock] ChestConfig is locked, save locks and items to NBT");

				/*
				 * spawn chest item
				 */

				if (WorldInfo.isServerSide(worldIn)) {
					ItemStack chestItem = new ItemStack(Item.getItemFromBlock(this), 1);

					// give the chest a tag compound
					//					Treasure.LOGGER.debug("[BreakingBlock]Saving chest items:");

					CompoundNBT nbt = new CompoundNBT();
					nbt = te.write(nbt);
					chestItem.setTag(nbt);

					InventoryHelper.spawnItemStack(worldIn, (double) pos.getX(), (double) pos.getY(),
							(double) pos.getZ(), chestItem);

					// TEST log all items in item
					//					NonNullList<ItemStack> items = NonNullList.<ItemStack>withSize(27, ItemStack.EMPTY);
					//					ItemStackHelper.loadAllItems(chestItem.getTagCompound(), items);
					//					for (ItemStack stack : items) {
					//						Treasure.LOGGER.debug("[BreakingBlock] item in chest item -> {}", stack.getDisplayName());
					//					}
				}
			}

			// remove the tile entity
			worldIn.removeTileEntity(pos);
		}
		else {
			// default to regular block break;
			//			super.breakBlock(worldIn, pos, state);
			//			worldIn.destroyBlock()
			//			super.onBlockHarvested(worldIn, pos, state, player);
			super.onReplaced(state, worldIn, pos, newState, isMoving);
		}
	}

	// replaces getItemDropped
	public static void spawnDrops(BlockState state, World worldIn, BlockPos pos) {
		return;
	}

	@Override
	public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
		return false;
	}

	/**
	 * Handled by breakBlock()
	 */
	//	@Override
	//	public Item getItemDropped(BlockState state, Random rand, int fortune) {
	//		return null;
	//	}

	/**
	 * 
	 * @param tagCompound
	 */
	private void dump(CompoundNBT tag, ICoords coords, String title) {
		//		ChestNBTPrettyPrinter printer = new ChestNBTPrettyPrinter();
		//		SimpleDateFormat formatter = new SimpleDateFormat("yyyymmdd");
		//
		//		String filename = String.format("chest-nbt-%s-%s.txt", formatter.format(new Date()),
		//				coords.toShortString().replaceAll(" ", "-"));
		//
		//		Path path = Paths.get(TreasureConfig.LOGGING.folder, "dumps").toAbsolutePath();
		//		try {
		//			Files.createDirectories(path);
		//		} catch (IOException e) {
		//			Treasure.LOGGER.error("Couldn't create directories for dump files:", e);
		//			return;
		//		}
		//		String s = printer.print(tag, Paths.get(path.toString(), filename), title);
		//		Treasure.LOGGER.debug(s);
	}


}
