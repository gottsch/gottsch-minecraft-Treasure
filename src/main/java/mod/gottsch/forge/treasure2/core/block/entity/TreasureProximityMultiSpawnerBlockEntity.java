/*
 * This file is part of Treasure2.
 * Copyright (c) 2025 Mark Gottschling (gottsch)
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the Open Software Licence 3.0.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Open Software Licence 3.0 for more details.
 *
 * You should have received a copy of the Open Software Licence
 * along with Treasure2. If not, see <https://www.tldrlegal.com/license/open-software-licence-3-0>.
 */
package mod.gottsch.forge.treasure2.core.block.entity;

import mod.gottsch.forge.gottschcore.block.entity.AbstractProximityBlockEntity;
import mod.gottsch.forge.gottschcore.random.RandomHelper;
import mod.gottsch.forge.gottschcore.size.IntegerRange;
import mod.gottsch.forge.gottschcore.spatial.Coords;
import mod.gottsch.forge.gottschcore.spatial.ICoords;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.util.ModUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.DungeonHooks;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 *
 */
public class TreasureProximityMultiSpawnerBlockEntity extends AbstractProximityBlockEntity {
    private static final String MOB_NAMES = "mobNames";
    private static final String MOB_NAME = "mobName";
    private static final String MIN_MOBS = "minMobs";
    private static final String MAX_MOBS = "maxMobs";

    private List<ResourceLocation> mobNames;
    private IntegerRange mobSizeRange;

    /**
     *
     * @param pos
     * @param state
     */
    public TreasureProximityMultiSpawnerBlockEntity(BlockPos pos, BlockState state) {
        super(TreasureBlockEntities.TREASURE_PROXIMITY_MULTI_SPAWNER_ENTITY_TYPE.get(), pos, state);
    }

    public TreasureProximityMultiSpawnerBlockEntity(BlockPos pos, BlockState state, double proximity) {
        super(TreasureBlockEntities.TREASURE_PROXIMITY_MULTI_SPAWNER_ENTITY_TYPE.get(), pos, state, proximity);
    }

    public void load(CompoundTag tag) {
        super.load(tag);

        try {
            if (tag.contains(MOB_NAMES)) {
                ListTag names = tag.getList(MOB_NAMES, Tag.TAG_STRING);
                names.forEach(name -> {
                    getMobNames().add(ModUtil.asLocation(((StringTag)name).getAsString()));
                });
            } else {
                EntityType<?> entityType = DungeonHooks.getRandomDungeonMob(this.level.random);
                if (entityType != null) {
                    getMobNames().add(EntityType.getKey(entityType));
                } else {
                    this.defaultMobSpawnerSettings();
                    return;
                }
            }

            int min = 1;
            int max = 1;
            if (tag.contains(MIN_MOBS)) {
                min = tag.getInt(MIN_MOBS);
            }
            if (tag.contains(MAX_MOBS)) {
                max = tag.getInt(MAX_MOBS);
            }
            this.mobSizeRange = new IntegerRange(min, max);

        } catch (Exception e) {
            Treasure.LOGGER.error("error reading TreasureProximityMultiSpawnerBlockEntity properties from tag:", e);
        }
    }

    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (getMobNames().isEmpty()) {
            this.defaultMobSpawnerSettings();
        }

        ListTag names = new ListTag();
        getMobNames().forEach(name -> {
            StringTag nameTag = StringTag.valueOf(name.toString());
            names.add(nameTag);
        });
        tag.put(MOB_NAMES, names);

        tag.putInt(MIN_MOBS, this.getMobSizeRange().getMin());
        tag.putInt(MAX_MOBS, this.getMobSizeRange().getMax());
    }

    private void defaultMobSpawnerSettings() {
        getMobNames().add((new ResourceLocation("minecraft", "zombie")));
        this.setMobSizeRange(new IntegerRange(1, 1));
        this.setProximity(5.0);
    }

    /**
     *
     */
    public void tickServer() {
        if (!this.level.isClientSide()) {
            boolean isTriggered = false;
            double proximitySq = this.getProximity() * this.getProximity();
            if (proximitySq < 1.0) {
                proximitySq = 1.0;
            }

            Iterator players = this.getLevel().players().iterator();

            while(players.hasNext()) {
                Player player = (Player)players.next();
                double distanceSq = player.distanceToSqr((double)this.getBlockPos().getX(), (double)this.getBlockPos().getY(), (double)this.getBlockPos().getZ());
                if (!isTriggered && !this.isDead() && distanceSq < proximitySq) {
                    Treasure.LOGGER.debug("proximity @ -> {} was met.", (new Coords(this.getBlockPos())).toShortString());
                    isTriggered = true;
                    Treasure.LOGGER.debug("proximity pos -> {}", this.getBlockPos());
                    this.execute(this.level, this.level.getRandom(), new Coords(this.getBlockPos()), new Coords(player.blockPosition()));
                }

                if (this.isDead()) {
                    break;
                }
            }

        }
    }

    public void execute(Level world, RandomSource random, ICoords blockCoords, ICoords playerCoords) {
        if (!world.isClientSide()) {
            ServerLevel level = (ServerLevel)world;
            int numberOfMobs = RandomHelper.randomInt(random, this.getMobSizeRange().getMin(), this.getMobSizeRange().getMax());

            // for the number of mobs
            for(int x = 0; x < numberOfMobs; ++x) {
                // randomly select a mob from the list
                ResourceLocation mobName = getMobNames().get(random.nextInt(getMobNames().size()));
                Optional<EntityType<?>> entityType = EntityType.byString(mobName.toString());
                if (entityType.isEmpty()) {
                    Treasure.LOGGER.debug("unable to get entityType -> {}", mobName);
                    continue;
                }
                Entity mob = ((EntityType<?>)entityType.get()).create(level);
                ModUtil.SpawnEntityHelper.spawn(level, random, entityType.get(), mob, blockCoords);
            }
            this.selfDestruct();
        }
    }

    private void selfDestruct() {
        Treasure.LOGGER.debug("self-destructing @ {}", this.getBlockPos());
        this.setDead(true);
        this.getLevel().setBlock(this.getBlockPos(), Blocks.AIR.defaultBlockState(), 3);
        this.getLevel().removeBlockEntity(this.getBlockPos());
    }

    public List<ResourceLocation> getMobNames() {
        if (mobNames == null) {
            mobNames = new ArrayList<>();
        }
        return mobNames;
    }

    public void setMobNames(List<ResourceLocation> mobNames) {
        this.mobNames = mobNames;
    }

    public IntegerRange getMobSizeRange() {
        return mobSizeRange;
    }

    public void setMobSizeRange(IntegerRange mobSizeRange) {
        this.mobSizeRange = mobSizeRange;
    }
}
