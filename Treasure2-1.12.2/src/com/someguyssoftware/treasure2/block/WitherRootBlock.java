/**
 * 
 */
package com.someguyssoftware.treasure2.block;

import java.util.Random;

import com.someguyssoftware.gottschcore.block.CardinalDirectionFacadeBlock;
import com.someguyssoftware.gottschcore.positional.Coords;
import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.item.TreasureItems;
import com.someguyssoftware.treasure2.particle.AbstractMistParticle;
import com.someguyssoftware.treasure2.particle.WitherMistParticle;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * 
 * @author Mark Gottschling on Apr 7, 2018
 *
 */
public class WitherRootBlock extends CardinalDirectionFacadeBlock implements ITreasureBlock, IMistSupport {
	public static final PropertyBool ACTIVATED = PropertyBool.create("activated");

	/*
	 * An array of AxisAlignedBB bounds for the bounding box
	 */
	AxisAlignedBB[] bounds = new AxisAlignedBB[4];

	/**
	 * 
	 * @param modID
	 * @param name
	 */
	public WitherRootBlock(String modID, String name) {
		super(modID, name, Material.WOOD);
		setSoundType(SoundType.WOOD);
//		setCreativeTab(Treasure.TREASURE_TAB);
		setHardness(3.0F);
		setBounds(new AxisAlignedBB[] { new AxisAlignedBB(0.25F, 0F, 0F, 0.75F, 0.5F, 1F), // N
				new AxisAlignedBB(0F, 0F, 0F, 1F, 0.5F, 1F), // E
				new AxisAlignedBB(0F, 0F, 0F, 1F, 0.5F, 1F), // S
				new AxisAlignedBB(0F, 0F, 0F, 1F, 0.5F, 1F) // W)
		});
		this.setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(ACTIVATED,
				Boolean.valueOf(false)));
	}

	/**
	 * 
	 */
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING, ACTIVATED });
	}

	/**
	 * NOTE randomDisplayTick is on the client side only. The server is not keeping
	 * track of any particles NOTE cannot control the number of ticks per
	 * randomDisplayTick() call - it is not controlled by tickRate()
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random random) {
		if (WorldInfo.isServerSide(world)) {
			return;
		}

		if (!TreasureConfig.WORLD_GEN.getGeneralProperties().enablePoisonFog) {
			return;
		}

		if (!state.getValue(ACTIVATED)) {
			return;
		}

		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();

		boolean isCreateParticle = checkTorchPrevention(world, random, x, y, z);
		if (!isCreateParticle) {
			return;
		}

		double xPos = (x + 0.5D) + (random.nextFloat() * 2.0D) - 1D;
		double yPos = y + 0.125;
		double zPos = (z + 0.5D) + (random.nextFloat() * 2.0D) - 1D;

		// initial velocities
		double velocityX = 0;
		double velocityY = 0;
		double velocityZ = 0;

		AbstractMistParticle mistParticle = new WitherMistParticle(world, xPos, yPos, zPos, velocityX, velocityY,
				velocityZ, new Coords(pos));

		// remember to init!
		mistParticle.init();
		Minecraft.getMinecraft().effectRenderer.addEffect(mistParticle);
	}

	/**
	 * 
	 */
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		if (state.getValue(FACING) == EnumFacing.NORTH) {
			return bounds[EnumFacing.NORTH.getHorizontalIndex()];
		} else if (state.getValue(FACING) == EnumFacing.SOUTH) {
			return bounds[EnumFacing.SOUTH.getHorizontalIndex()];
		} else if (state.getValue(FACING) == EnumFacing.EAST) {
			return bounds[EnumFacing.EAST.getHorizontalIndex()];
		} else if (state.getValue(FACING) == EnumFacing.WEST) {
			return bounds[EnumFacing.WEST.getHorizontalIndex()];
		} else {
			return bounds[EnumFacing.NORTH.getHorizontalIndex()];
		}
	}

	/**
	 * Drops WitherRootItem or a stick.
	 */
	@Override
	public Item getItemDropped(IBlockState state, Random random, int fortune) {

		if (RandomHelper.checkProbability(random, TreasureConfig.WITHER_TREE.witherRootItemGenProbability)) {
			return TreasureItems.WITHER_ROOT_ITEM;
		}
		return Items.STICK;
	}

	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	@Override
	public IBlockState getStateFromMeta(int meta) {
		IBlockState state = super.getStateFromMeta(meta);
		return state.withProperty(ACTIVATED, Boolean.valueOf((meta & 8) > 0));
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	@Override
	public int getMetaFromState(IBlockState state) {
		int meta = 0;
		meta = super.getMetaFromState(state);
		if (state.getValue(ACTIVATED))
			meta += 8;
		return meta;
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
	public void setBounds(AxisAlignedBB[] bounds) {
		this.bounds = bounds;
	}
}
