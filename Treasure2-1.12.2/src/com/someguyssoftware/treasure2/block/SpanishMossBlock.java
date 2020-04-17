/**
 * 
 */
package com.someguyssoftware.treasure2.block;

import java.util.Random;

import com.someguyssoftware.gottschcore.positional.Coords;
import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.particle.AbstractMistParticle;
import com.someguyssoftware.treasure2.particle.PoisonMistParticle;

import net.minecraft.block.BlockBush;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Mark Gottschling on Jul 25, 2018
 *
 */
public class SpanishMossBlock extends BlockBush {
	public static final PropertyBool ACTIVATED = PropertyBool.create("activated");

	/**
	 * 
	 */
	public SpanishMossBlock(String modID, String name) {
		// TODO it is wood so I didn't have to update GottschCore.get..SurfaceCoods() to check for PLANTS.
		super(Material.WOOD);
		setRegistryName(modID, name);
		setUnlocalizedName(getRegistryName().toString());
		this.setDefaultState(blockState.getBaseState().withProperty(ACTIVATED, Boolean.valueOf(false)));
	}

	/**
	 * 
	 */
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { ACTIVATED });
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

		if (!state.getValue(ACTIVATED)) {
			return;
		}

		if (RandomHelper.checkProbability(random, 75D)) {
			return;
		}

		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();

		// initial positions - has a spread area of up to 1.5 blocks
		double xPos = x;
		double yPos = y;
		double zPos = z;
		// initial velocities
		double velocityX = 0;
		double velocityY = 0;
		double velocityZ = 0;

		AbstractMistParticle mistParticle = new PoisonMistParticle(world, xPos, yPos, zPos, velocityX, velocityY,
				velocityZ, new Coords(pos)) {
			@Override
			public float provideGravity() {
				return 0.0001F;
			}

			@Override
			public float provideAlpha() {
				return DEFAULT_PARTICLE_ALPHA;
			}

			@Override
			public float provideMaxScale() {
				return 10F;
			}

			@Override
			public void doPlayerCollisions(World world) {
			}
		};
		mistParticle.init();

		Minecraft.getMinecraft().effectRenderer.addEffect(mistParticle);
	}

	public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos) {
		return true;
	}
	
	/**
	 * Checks if this block can be placed exactly at the given position.
	 */
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
//        Cube cube = new Cube(worldIn, new Coords(pos.up()));
//        return super.canPlaceBlockAt(worldIn, pos) && !cube.isAir() && !cube.isReplaceable();
		return true;
	}

	/**
	 * 
	 */
	public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state) {
//        if (state.getBlock() == this) {//Forge: This function is called during world gen and placement, before this block is set, so if we are not 'here' then assume it's the pre-check.
//            Cube cube = new Cube(worldIn, new Coords(pos.up()));
//            return super.canPlaceBlockAt(worldIn, pos) && !cube.isAir() && !cube.isReplaceable();
//        }
		return this.canSustainBush(worldIn.getBlockState(pos.down()));
	}

	/**
	 * Return true if the block can sustain a Bush
	 */
	public boolean canSustainBush(IBlockState state) {
		return true;
	}

	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(ACTIVATED, Boolean.valueOf((meta & 1) > 0));
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	@Override
	public int getMetaFromState(IBlockState state) {
		int meta = 0;
		if (state.getValue(ACTIVATED))
			meta = 1;
		return meta;
	}
}
