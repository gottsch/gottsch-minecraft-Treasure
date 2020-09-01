/**
 * 
 */
package com.someguyssoftware.treasure2.tileentity;

import java.util.Random;

import com.someguyssoftware.gottschcore.positional.Coords;

import net.minecraft.world.World;

/**
 * @author Mark Gottschling on Jan 17, 2019
 *
 */
public interface IProximityTileEntity {

	void execute(World world, Random random, Coords blockCoords, Coords playerCoords);

	double getProximity();

	void setProximity(double proximity);

}