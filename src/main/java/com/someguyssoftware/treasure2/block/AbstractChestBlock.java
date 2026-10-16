/**
 * 
 */
package com.someguyssoftware.treasure2.block;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

import com.someguyssoftware.gottschcore.block.AbstractModContainerBlock;
import com.someguyssoftware.gottschcore.loot.LootTableShell;
import com.someguyssoftware.gottschcore.enums.Rotate;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.chest.ILockSlot;
import com.someguyssoftware.treasure2.chest.TreasureChestType;
import com.someguyssoftware.treasure2.entity.monster.MimicEntity;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.lock.LockState;
import com.someguyssoftware.treasure2.registry.MimicRegistry;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;
import com.someguyssoftware.treasure2.tileentity.ITreasureChestTileEntity;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Mark Gottschling on Sep 16, 2018
 *
 */
public abstract class AbstractChestBlock extends AbstractModContainerBlock implements ITreasureBlock {
	public static final PropertyEnum<EnumFacing> FACING = PropertyDirection.create("facing", EnumFacing.class);
	/*
	 *  the class of the tileEntityClass this BlockChest should use.
	 */
	private Class<?> tileEntityClass;

	/*
	 * the concrete object of the tile entity
	 */
	private ITreasureChestTileEntity tileEntity;
	
	/*
	 * the type of chest
	 */
	private TreasureChestType chestType;

	/*
	 * the rarity of the chest
	 */
	private Rarity rarity;
	
	/*
	 * An array of AxisAlignedBB bounds for the bounding box
	 */
	private AxisAlignedBB[] bounds = new AxisAlignedBB[4];
	
	/**
	 * 
	 */
	public AbstractChestBlock(String modID, String name, Material material, Class<? extends ITreasureChestTileEntity> te, TreasureChestType type, Rarity rarity) {
		this(modID, name, material);
		setTileEntityClass(te);
		setChestType(type);
		setRarity(rarity);
		setCreativeTab(Treasure.TREASURE_TAB);
		
		// set the default bounds
		setBounds(
			new AxisAlignedBB[] {new AxisAlignedBB(0F, 0F, 0F, 1F, 1F, 1F), 	// N
			new AxisAlignedBB(0F, 0F, 0F, 1F, 1F, 1F),  	// E
			new AxisAlignedBB(0F, 0F, 0F, 1F, 1F, 1F),  	// S
			new AxisAlignedBB(0F, 0F, 0F, 1F, 1F, 1F)	// W
			});
		
		// set the tile entity reference
		try {
			setTileEntity((ITreasureChestTileEntity)getTileEntityClass().newInstance());
		}
		catch(Exception e) {
			Treasure.LOGGER.warn("Unable to create reference ITreasureChestTileEntity object.");
		}

	}

	/**
	 * 
	 * @param modID
	 * @param name
	 * @param material
	 */
	protected AbstractChestBlock(String modID, String name, Material material) {
		super(modID, name, material);
	}
	
	/* (non-Javadoc)
	 * @see net.minecraft.block.ITileEntityProvider#createNewTileEntity(net.minecraft.world.World, int)
	 */
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		ITreasureChestTileEntity chestTileEntity = null;
		try {
			chestTileEntity = (ITreasureChestTileEntity) getTileEntityClass().newInstance();

			// setup lock states
			List<LockState> lockStates = new LinkedList<>();

			for (int i = 0; i < chestType.getSlots().length; i++) {
				LockState lockState = new LockState();
				lockState.setSlot(chestType.getSlots()[i]);
				// add in order of slot indexes
				lockStates.add(lockState.getSlot().getIndex(), lockState);
			}
			chestTileEntity.setLockStates(lockStates);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return (TileEntity) chestTileEntity;
	}

	/**
	 * 
	 */
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT_MIPPED;
	}

	// used by the renderer to control lighting and visibility of other blocks.
	// set to false because this block doesn't fill the entire 1x1x1 space
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	// used by the renderer to control lighting and visibility of other blocks, also by
	// (eg) wall or fence to control whether the fence joins itself to this block
	// set to false because this block doesn't fill the entire 1x1x1 space
	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
		return false;
	}
	
	/**
	 * Render using a TESR.
	 */
	@Override
	public EnumBlockRenderType getRenderType(IBlockState iBlockState) {
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}
	
	/**
	 * 
	 * @param world
	 * @param pos
	 * @param rotate
	 * @return
	 */
	public boolean  rotateLockStates(World world, BlockPos pos, Rotate rotate) {
		boolean hasRotated = false;
		boolean shouldRotate = false;
		if (rotate != Rotate.NO_ROTATE) shouldRotate = true;
//		Treasure.logger.debug("Rotate to:" + rotate);
		
		ITreasureChestTileEntity tcte = null;
		TileEntity te = world.getTileEntity(pos);
		if (te != null && te instanceof ITreasureChestTileEntity) {
			// get the backing tile entity
			tcte = (ITreasureChestTileEntity) te;
		}
		else {
			return false;
		}
		
		try {
			for (LockState lockState : tcte.getLockStates()) {
				if (lockState != null && lockState.getSlot() != null) {
//					Treasure.logger.debug("Original lock state:" + lockState);
					// if a rotation is needed
					if (shouldRotate) {
						ILockSlot newSlot = lockState.getSlot().rotate(rotate);
//						Treasure.logger.debug("New slot position:" + newSlot);
						lockState.setSlot(newSlot);
						// set the flag to indicate the lockStates have rotated
						hasRotated = true;
					}
				}
			}
		}
		catch(Exception e) {
			Treasure.LOGGER.error("Error updating lock states: ", e);
		}
		return hasRotated;
	}
	
	/**
	 * 
	 * @param state
	 * @return
	 */
	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}
	
	/**
	 * 
	 */
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		if (state.getValue(FACING) == EnumFacing.NORTH) {
			return bounds[EnumFacing.NORTH.getHorizontalIndex()];
		}
		else if (state.getValue(FACING) == EnumFacing.SOUTH) {
			return bounds[EnumFacing.SOUTH.getHorizontalIndex()];
		}
		else if (state.getValue(FACING) == EnumFacing.EAST) {
			return bounds[EnumFacing.EAST.getHorizontalIndex()];
		}
		else if (state.getValue(FACING) == EnumFacing.WEST) {
			return bounds[EnumFacing.WEST.getHorizontalIndex()];
		}
		else {		
			return bounds[EnumFacing.NORTH.getHorizontalIndex()];
		}
	}
	
	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	@Override
	public IBlockState getStateFromMeta(int meta) {
		EnumFacing enumfacing = EnumFacing.getFront(meta);

		if (enumfacing.getAxis() == EnumFacing.Axis.Y) {
			enumfacing = EnumFacing.NORTH;
		}
		return this.getDefaultState().withProperty(FACING, enumfacing);
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	@Override
	public int getMetaFromState(IBlockState state) {
		return ((EnumFacing)state.getValue(FACING)).getIndex();
	}

	/**
	 * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed
	 * blockstate.
	 */
	@Override
	public IBlockState withRotation(IBlockState state, Rotation rot) {
		return state.withProperty(FACING, rot.rotate((EnumFacing)state.getValue(FACING)));
	}

	/**
	 * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed
	 * blockstate.
	 */
	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
		return state.withRotation(mirrorIn.toRotation((EnumFacing)state.getValue(FACING)));
	}
	
	/**
	 * 
	 */
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {FACING});
	}
	
	/**
	 * @return the tileEntityClass
	 */
	public Class<?> getTileEntityClass() {
		return tileEntityClass;
	}

	/**
	 * @param tileEntityClass the tileEntityClass to set
	 */
	public void setTileEntityClass(Class<?> tileEntityClass) {
		this.tileEntityClass = tileEntityClass;
	}

	/**
	 * @return the tileEntity
	 */
	public ITreasureChestTileEntity getTileEntity() {
		return tileEntity;
	}

	/**
	 * @param tileEntity the tileEntity to set
	 */
	public AbstractChestBlock setTileEntity(ITreasureChestTileEntity tileEntity) {
		this.tileEntity = tileEntity;
		return this;
	}

	/**
	 * @return the chestType
	 */
	public TreasureChestType getChestType() {
		return chestType;
	}

	/**
	 * @param chestType the chestType to set
	 */
	public AbstractChestBlock setChestType(TreasureChestType chestType) {
		this.chestType = chestType;
		return this;
	}

	/**
	 * @return the rarity
	 */
	public Rarity getRarity() {
		return rarity;
	}

	/**
	 * @param rarity the rarity to set
	 */
	public AbstractChestBlock setRarity(Rarity rarity) {
		this.rarity = rarity;
		return this;
	}

	/**
	 * @return the bounds
	 */
	public AxisAlignedBB[] getBounds() {
		return bounds;
	}

	/**
	 * @param bounds the bounds to set
	 */
	public AbstractChestBlock  setBounds(AxisAlignedBB[] bounds) {
		this.bounds = bounds;
		return this;
	}

	/**
	 * Replaces the chest with its mimic. The mimic is always the one registered for this chest block in
	 * MimicRegistry, so it matches the chest. Anything stored in the chest is dropped, and the mimic
	 * carries the chest's loot table and drops its full roll when killed.
	 *
	 * @param world
	 * @param pos
	 * @param state
	 * @param te the chest's tile entity
	 * @param player the player that triggered the mimic, or null to target the nearest player
	 * @param removeBlock false when the block is already being broken
	 * @return true if a mimic was spawned
	 */
	protected boolean spawnMimic(World world, BlockPos pos, IBlockState state, AbstractTreasureChestTileEntity te,
			@Nullable EntityPlayer player, boolean removeBlock) {
		Optional<ResourceLocation> mimicName = MimicRegistry.getMimic(this);
		Entity entity = mimicName.isPresent() ? EntityList.createEntityByIDFromName(mimicName.get(), world) : null;
		if (!(entity instanceof MimicEntity)) {
			// flagged as a mimic, but this chest has no (valid) mimic - treat it as a normal chest
			Treasure.LOGGER.warn("Chest {} @ {} is flagged as a mimic, but has no registered mimic -> {}", getRegistryName(), pos, mimicName.orElse(null));
			te.setMimic(false);
			te.markDirty();
			return false;
		}
		MimicEntity mimic = (MimicEntity) entity;

		float yaw;
		switch (state.getValue(FACING)) {
		case NORTH:
			yaw = 180.0F;
			break;
		case EAST:
			yaw = -90.0F;
			break;
		case WEST:
			yaw = 90.0F;
			break;
		default:
			yaw = 0.0F;
		}
		mimic.setLocationAndAngles(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, yaw, 0.0F);
		mimic.rotationYawHead = yaw;
		mimic.renderYawOffset = yaw;
		mimic.setChestLootTable(getMimicLootTable(world, te));

		if (player == null) {
			player = world.getClosestPlayer(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, 16.0D, false);
		}
		if (player != null && !player.isCreative() && !player.isSpectator()) {
			mimic.setAttackTarget(player);
		}

		// drop anything stored in the chest, then remove the tile entity BEFORE the block, so that
		// breakBlock() neither drops the chest as an item nor spawns the mimic again.
		InventoryHelper.dropInventoryItems(world, pos, (IInventory) te);
		te.setMimic(false);
		world.removeTileEntity(pos);
		if (removeBlock) {
			world.setBlockToAir(pos);
		}

		world.spawnEntity(mimic);
		return true;
	}

	/**
	 * The chest's loot table, or one selected the same way the chest would have when opened.
	 * Null lets the mimic fall back to its own loot table.
	 */
	@Nullable
	private ResourceLocation getMimicLootTable(World world, AbstractTreasureChestTileEntity te) {
		if (te.getLootTable() != null) {
			return te.getLootTable();
		}
		if (te.getGenerationContext() != null && te.getGenerationContext().getChestGeneratorType() != null) {
			Optional<LootTableShell> shell = te.getGenerationContext().getChestGeneratorType().getChestGenerator()
					.selectLootTable2(world.rand, te.getGenerationContext().getLootRarity());
			if (shell.isPresent()) {
				return shell.get().getResourceLocation();
			}
		}
		return null;
	}
}
