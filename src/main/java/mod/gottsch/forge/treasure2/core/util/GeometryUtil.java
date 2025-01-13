/*
 * This file is part of  Treasure2.
 * Copyright (c) 2023 Mark Gottschling (gottsch)
 *
 * All rights reserved.
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
package mod.gottsch.forge.treasure2.core.util;

import mod.gottsch.forge.gottschcore.spatial.Coords;
import mod.gottsch.forge.gottschcore.spatial.ICoords;
import mod.gottsch.forge.gottschcore.spatial.Rotate;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Rotation;

/**
 * TODO move to GottschCore
 */

/**
 *
 * @author Mark Gottschling on Oct 13, 2023
 *
 */
public class GeometryUtil {

    /**
     * NOTE this method is standard math cartesian plane, where
     * N/Up = +y, E/right = +x, S/down = -y, and W/left = -x
     *
     * 90° clockwise rotation: (x,y) becomes (y,−x)
     * 90° counterclockwise rotation: (x,y) becomes (−y,x)
     * 180° clockwise and counterclockwise rotation: (x,y) becomes (−x,−y)
     * 270° clockwise rotation: (x,y) becomes (−y,x)
     * 270° counterclockwise rotation: (x,y) becomes (y,−x)
     * /

     /**
     * @param coords
     * @param rotation
     * @return
     */
    public static ICoords rotate(ICoords coords, Rotation rotation) {
        return switch(rotation) {
            case CLOCKWISE_90 -> new Coords(coords.getZ(), coords.getY(), -coords.getX());
            case CLOCKWISE_180 -> new Coords(-coords.getX(), coords.getY(), -coords.getZ());
            case COUNTERCLOCKWISE_90 -> new Coords(-coords.getZ(), coords.getY(), coords.getX());
            case NONE -> coords;
            default -> coords;
        };
    }

    public static ICoords rotate(ICoords coords, Rotate rotate) {
        return switch(rotate) {
            case ROTATE_90 -> rotate(coords, Rotation.CLOCKWISE_90);
            case ROTATE_180 -> rotate(coords, Rotation.CLOCKWISE_180);
            case ROTATE_270 -> rotate(coords, Rotation.COUNTERCLOCKWISE_90);
            case NO_ROTATE -> coords;
            default -> coords;
        };
    }

    /**
     * NOTE this method uses Minecraft's horizontal flip plane where
     * N/up = -y/z, E/right = +x, S/down = +y/z, and W/left = -x
     *
     * 90° clockwise rotation: (x,y) becomes (-y,x)
     * 180° clockwise and counterclockwise rotation: (x,y) becomes (−x,−y)
     * 270° clockwise rotation: (x,y) becomes (y,-x)
     *
     * @param coords
     * @param rotation
     * @return
     */
    public static ICoords mcRotate(ICoords coords, Rotation rotation) {
        return switch(rotation) {
            case CLOCKWISE_90 -> new Coords(-coords.getZ(), coords.getY(), coords.getX());
            case CLOCKWISE_180 -> new Coords(-coords.getX(), coords.getY(), -coords.getZ());
            case COUNTERCLOCKWISE_90 -> new Coords(coords.getZ(), coords.getY(), -coords.getX());
            case NONE -> coords;
            default -> coords;
        };
    }

    /**
     *  TODO this could move to GenUtil
     * @param random
     * @param min
     * @param max
     * @return
     */
    public static ICoords generateRandomCoordsByRadius(RandomSource random, int min, int max) {
        if (random == null) {
            random = RandomSource.create();
        }

        // generate a random radius between min and max
        double radius = random.nextDouble() * (max - min) + min;
        // generate a random angle in radians
        double angle = random.nextDouble() * 2 * Math.PI;
        // calculate x and z coordinates
        double x = radius * Math.cos(angle);
        double z = radius * Math.sin(angle);

        return new Coords((int)x, 0, (int)z);
    }
}
