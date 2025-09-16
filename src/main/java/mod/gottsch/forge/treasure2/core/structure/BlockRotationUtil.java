package mod.gottsch.forge.treasure2.core.structure;


import mod.gottsch.forge.gottschcore.spatial.Coords;
import mod.gottsch.forge.gottschcore.spatial.ICoords;
import mod.gottsch.forge.gottschcore.world.gen.structure.PlacementSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.Rotation;

/**
 * @author by Mark Gottschling on 9/10/2025
 */
public class BlockRotationUtil {
    public static BlockPos rotateAroundPivot(BlockPos pos, BlockPos pivot, Rotation rotation) {
        // Step 1: Translate the position relative to the pivot
        int relativeX = pos.getX() - pivot.getX();
        int relativeZ = pos.getZ() - pivot.getZ();

        // Step 2: Apply the rotation to the relative coordinates
        int rotatedX;
        int rotatedZ;

        switch (rotation) {
            case CLOCKWISE_90:
                rotatedX = -relativeZ;
                rotatedZ = relativeX;
                break;
            case CLOCKWISE_180:
                rotatedX = -relativeX;
                rotatedZ = -relativeZ;
                break;
            case COUNTERCLOCKWISE_90:
                rotatedX = relativeZ;
                rotatedZ = -relativeX;
                break;
            case NONE:
            default:
                rotatedX = relativeX;
                rotatedZ = relativeZ;
                break;
        }

        // Step 3: Translate the rotated coordinates back to the world
        return new BlockPos(pivot.getX() + rotatedX, pos.getY(), pivot.getZ() + rotatedZ);
    }

    public static BlockPos transformStartCoords(final BlockPos pos, final BlockPos size, final Rotation rotation) {
        return transformStartCoords(pos, new Vec3i(size.getX(), size.getY(), size.getZ()), rotation);
    }

    public static BlockPos transformStartCoords(final BlockPos pos, final Vec3i size,
        final Rotation rotation) {
        ICoords spawnCoords = null;
        int x = 0;
        int z = 0;
        switch (rotation) {
            case NONE:
                x = pos.getX();
                z = pos.getZ();
                break;
            case CLOCKWISE_90:
                x = pos.getX() - (size.getZ() - 1);
                z = pos.getZ();
                break;
            case CLOCKWISE_180:
                // this works fine for calculating the start pos, but not actual rotating
                x = pos.getX() - (size.getX() - 1);
                z = pos.getZ() - (size.getZ() - 1);
                // TODO this is not right
//                x = pos.getX();
//                z = pos.getZ();
                break;
            case COUNTERCLOCKWISE_90:
                x = pos.getX();
                z = pos.getZ() - (size.getX() - 1);
                break;
            default:
                break;
        }
        return new BlockPos(x, pos.getY(), z);
    }
}
