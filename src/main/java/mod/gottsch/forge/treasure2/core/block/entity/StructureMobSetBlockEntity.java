package mod.gottsch.forge.treasure2.core.block.entity;

import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.util.ModUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

/**
 * This class is meant to be used in Structure NBTs only as a marker
 * for where a ProximityMobSetSpawnBlock block will be placed.
 * @author by Mark Gottschling on 8/14/2025
 */
public class StructureMobSetBlockEntity extends BlockEntity {
    public static final String MOBSET = "mobSet";
    public static final String MOBSETS = "mobSets";
    public static final String PROXIMITY = "proximity";

    private ResourceLocation mobSet;
    private int proximity;

    public StructureMobSetBlockEntity(BlockPos pos, BlockState state) {
        super(TreasureBlockEntities.STRUCTURE_MOB_SET.get(), pos, state);
    }

    public StructureMobSetBlockEntity(BlockEntityType<?> entityType, BlockPos pos, BlockState state) {
        super(entityType, pos, state);
    }

    @Override
    public void load(CompoundTag tag) {
        try {
            super.load(tag);
            try {
                // read the custom name
                if (tag.contains(MOBSET)) {
                    this.mobSet = ModUtil.asLocation(tag.getString(MOBSET));
                }
                if (tag.contains(PROXIMITY)) {
                    this.proximity = tag.getInt(PROXIMITY);
                }
            } catch (Exception e) {
                Treasure.LOGGER.error("error reading StructureMobSetBlockEntity properties from tag:", e);
            }
        } catch(Exception e) {
            Treasure.LOGGER.error(e);
            throw e;
        }
    }

    /**
     *
     */
    @Override
    public void saveAdditional(CompoundTag nbt) {
        try {
            super.saveAdditional(nbt);

            Optional.ofNullable(getMobSet())
                    .ifPresent(mobSet -> nbt.putString(MOBSET, getMobSet().toString()));

            nbt.putInt(PROXIMITY, this.proximity);

        } catch(Exception e) {
            Treasure.LOGGER.error(e);
            throw e;
        }
    }

    public ResourceLocation getMobSet() {
        return mobSet;
    }

    public void setMobSet(ResourceLocation mobSet) {
        this.mobSet = mobSet;
    }

    public int getProximity() {
        return proximity;
    }

    public void setProximity(int proximity) {
        this.proximity = proximity;
    }
}
