/**
 * 
 */
package com.someguyssoftware.treasure2.block;

import java.util.Random;

import com.someguyssoftware.gottschcore.block.CardinalDirectionFacadeBlock;
import com.someguyssoftware.gottschcore.positional.Coords;
import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.particle.AbstractMistParticle;
import com.someguyssoftware.treasure2.particle.BillowingMistParticle;
import com.someguyssoftware.treasure2.particle.MistParticle;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * 
 * @author Mark Gottschling on Jan 29, 2018
 *
 */
public class GravestoneBlock extends CardinalDirectionFacadeBlock
		implements ITreasureBlock, IMistSupport {

	/*
	 * An array of AxisAlignedBB bounds for the bounding box
	 */
	AxisAlignedBB[] bounds = new AxisAlignedBB[4];

	/**
	 * 
	 * @param modID
	 * @param name
	 * @param material
	 */
	public GravestoneBlock(String modID, String name, Material material) {
		super(modID, name, material);
		setSoundType(SoundType.STONE);
		setCreativeTab(Treasure.TREASURE_TAB);
		setHardness(3.0F);

		setBoundingBox(new AxisAlignedBB(0F, 0F, 0F, 1F, 1F, 1F), // N
				new AxisAlignedBB(0F, 0F, 0F, 1F, 1F, 1F), // E
				new AxisAlignedBB(0F, 0F, 0F, 1F, 1F, 1F), // S
				new AxisAlignedBB(0F, 0F, 0F, 1F, 1F, 1F) // W
		);
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

		if (!TreasureConfig.WORLD_GEN.getGeneralProperties().enableFog) {
			return;
		}

		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();

		boolean isCreateParticle = checkTorchPrevention(world, random, x, y, z);
		if (!isCreateParticle) {
			return;
		}

		// initial positions - has a spread area of up to 1.5 blocks
		double xPos = (x + 0.5D) + (random.nextFloat() * 3.0) - 1.5D;
		double yPos = y;
		// + state.getBoundingBox(world, pos).maxY; // + 1.0;
		double zPos = (z + 0.5D) + (random.nextFloat() * 3.0) - 1.5D;
		// initial velocities
		double velocityX = 0;
		double velocityY = 0;
		double velocityZ = 0;

		// create particle
		AbstractMistParticle mistParticle = null;

		if (RandomHelper.checkProbability(random, 80)) {
			mistParticle = new MistParticle(world, xPos, yPos, zPos, velocityX, velocityY, velocityZ, new Coords(pos));
		} else {
			mistParticle = new BillowingMistParticle(world, xPos, yPos, zPos, velocityX, velocityY, velocityZ,
					new Coords(pos));
		}
		mistParticle.init();

		Minecraft.getMinecraft().effectRenderer.addEffect(mistParticle);
	}

	/*
	 * This is for updateTick()
	 */
	@Override
	public int tickRate(World worldIn) {
		return 10;
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
	 * @return the bounds
	 */
	public AxisAlignedBB[] getBounds() {
		return bounds;
	}

	/**
	 * @param bounds the bounds to set
	 * @return
	 */
	public GravestoneBlock setBounds(AxisAlignedBB[] bounds) {
		this.bounds = bounds;
		return this;
	}
}
