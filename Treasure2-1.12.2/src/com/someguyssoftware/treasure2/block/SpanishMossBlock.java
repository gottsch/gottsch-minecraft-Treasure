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
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Mark Gottschling on Jul 25, 2018
 *
 */
public class SpanishMossBlock extends BlockBush {

	/**
	 * 
	 */
	public SpanishMossBlock(String modID, String name) {
		setRegistryName(modID, name);
		setUnlocalizedName(getRegistryName().toString());
	}

	/**
	 * NOTE randomDisplayTick is on the client side only. The server is not keeping track of any
	 * particles NOTE cannot control the number of ticks per randomDisplayTick() call - it is not
	 * controlled by tickRate()
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

		if (RandomHelper.checkProbability(random, 75D)) {
			return;
		}

		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();

		// initial positions - has a spread area of up to 1.5 blocks
		double xPos = x;// (x + 0.5F) + (random.nextFloat() * 3.0) - 1.5F;
		double yPos = y;
		// + state.getBoundingBox(world, pos).maxY; // + 1.0;
		double zPos = z;// (z + 0.5F) + (random.nextFloat() * 3.0) - 1.5F;
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
			public void doPlayerCollisions(World world) {
			}
		};
		mistParticle.init();

		Minecraft.getMinecraft().effectRenderer.addEffect(mistParticle);
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

}
