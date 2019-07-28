package com.someguyssoftware.treasure2.generator.structure;

import java.util.Random;

import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.gottschcore.world.gen.structure.GottschTemplate;
import com.someguyssoftware.treasure2.world.gen.structure.IStructureInfo;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;

/**
 * 
 * @author Mark Gottschling on Jul 6, 2019
 *
 */
public interface IStructureGenerator {

	/**
	 * 
	 * @param world
	 * @param random
	 * @param surfaceCoords
	 * @param spawnCoords
	 * @return
	 */
	public IStructureInfo generate(World world, Random random, GottschTemplate template, PlacementSettings settings, ICoords spawnCoords);

	/**
	 * NOTE not 100% sure that this  belongs here
	 * @param coords
	 * @param entranceCoords
	 * @param size
	 * @param placement
	 * @return
	 */
	public static ICoords alignEntranceToCoords(ICoords coords, ICoords entranceCoords, BlockPos size, PlacementSettings placement) {
		ICoords startCoords = null;
		// NOTE work with rotations only for now
		
		// first offset coords by entrance
		startCoords = coords.add(-entranceCoords.getX(), 0, -entranceCoords.getZ());
		
		// make adjustments for the rotation. REMEMBER that pits are 2x2
		switch (placement.getRotation()) {
		case CLOCKWISE_90:
			startCoords = startCoords.add(1, 0, 0);
			break;
		case CLOCKWISE_180:
			startCoords = startCoords.add(1, 0, 1);
			break;
		case COUNTERCLOCKWISE_90:
			startCoords = startCoords.add(0, 0, 1);
			break;
		default:
			break;
		}
		return startCoords;
	}

}