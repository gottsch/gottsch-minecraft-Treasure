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
import mod.gottsch.forge.gottschcore.random.WeightedCollection;
import mod.gottsch.forge.gottschcore.size.IntegerRange;
import mod.gottsch.forge.gottschcore.spatial.Coords;
import mod.gottsch.forge.gottschcore.spatial.ICoords;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.mobset.MobSetDataRegistry;
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
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class ProximityMobSetSpawnerBlockEntity extends AbstractProximityBlockEntity {
    public static final String MOBSET_NAME = "mobSetName";
    public static final String MOBSET_NAMES = "mobSetNames";
    public static final String MIN_MOBS = "minMobs";
    public static final String MAX_MOBS = "maxMobs";

    private static final ResourceLocation DEFAULT_MOB = ModUtil.asLocation("minecraft:zombie");

    private ResourceLocation mobSetName;
    private List<ResourceLocation> mobSetNames;
    private IntegerRange mobSizeRange;

    /**
     * @param pos
     * @param state
     */
    public ProximityMobSetSpawnerBlockEntity(BlockPos pos, BlockState state) {
        super(TreasureBlockEntities.PROXIMITY_MOBSET_SPAWNER_ENTITY_TYPE.get(), pos, state);
    }

    public ProximityMobSetSpawnerBlockEntity(BlockPos pos, BlockState state, double proximity) {
        super(TreasureBlockEntities.PROXIMITY_MOBSET_SPAWNER_ENTITY_TYPE.get(), pos, state, proximity);
    }

    public void load(CompoundTag tag) {
        super.load(tag);

        try {
            Optional.ofNullable(tag.getString(MOBSET_NAME))
                    .filter(str -> !str.isEmpty())
                    .map(ModUtil::asLocation)
                    .ifPresent(this::setMobSetName);

            if (tag.contains(MOBSET_NAMES, Tag.TAG_LIST)) {
                ListTag listTag = tag.getList(MOBSET_NAMES, Tag.TAG_STRING);
                listTag.stream()
                        .map(Tag::getAsString)
                        .filter(str -> !str.isEmpty())
                        .map(ModUtil::asLocation)
                        .forEach(this.getMobSetNames()::add);
            }

            int min = tag.contains(MIN_MOBS) ? tag.getInt(MIN_MOBS) : 1;
            int max = tag.contains(MAX_MOBS) ? tag.getInt(MAX_MOBS) : 1;
            this.mobSizeRange = new IntegerRange(min, max);

            /*
             * load the proximity again AFTER super.load() because GottschCore 2.5.0
             * has a bug in which it cannot find the proximity value because it is attempting
             * to load it incorrectly:
             *    if (tag.contains("proximity", 8)) {... }
             * in this case, the value of 8 represents a StringTag, but proximity is a DoubleTag.
             */
            if (tag.contains(PROXIMITY_TAG)) {
                setProximity(tag.getDouble(PROXIMITY_TAG));
            }

        } catch (Exception e) {
            Treasure.LOGGER.error("error reading ProximityMobSetSpawnerBlockEntity properties from tag:", e);
        }
    }

    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        try {
            if (getMobSetName() != null) {
                tag.putString(MOBSET_NAME, getMobSetName().toString());
            }
            if (getMobSetNames() != null && !getMobSetNames().isEmpty()) {
                ListTag list = new ListTag();
                getMobSetNames().forEach(name -> {
                    list.add(StringTag.valueOf(name.toString()));
                });
                tag.put(MOBSET_NAMES, list);
            }
            tag.putInt(MIN_MOBS, this.getMobSizeRange().getMin());
            tag.putInt(MAX_MOBS, this.getMobSizeRange().getMax());


        } catch (Exception e) {
            Treasure.LOGGER.error(e);
            throw e;
        }
    }

    private void defaultMobSpawnerSettings() {
        setMobSetName(new ResourceLocation("minecraft", "small_zombie_group"));
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

            while (players.hasNext()) {
                Player player = (Player) players.next();
                double distanceSq = player.distanceToSqr((double) this.getBlockPos().getX(), (double) this.getBlockPos().getY(), (double) this.getBlockPos().getZ());
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
        if (world.isClientSide()) {
            return;
        }

        ServerLevel level = (ServerLevel) world;
        int numberOfMobs = RandomHelper.randomInt(random, this.getMobSizeRange().getMin(), this.getMobSizeRange().getMax());

        MobSetDataRegistry.get(getMobSetName()).ifPresent(data -> {
            WeightedCollection<Integer, ResourceLocation> collection = new WeightedCollection<>();
            data.getMobs().forEach(weightedMob -> collection.add(weightedMob.weight(), weightedMob.id()));

            // for the number of mobs
            for (int i = 0; i < numberOfMobs; i++) {
                ResourceLocation mobName = Optional.ofNullable(collection.next()).orElse(DEFAULT_MOB);

                EntityType.byString(mobName.toString()).ifPresentOrElse(entityType -> {
                            Entity mob = entityType.create(level);
                            if (mob instanceof Mob) {
                                ForgeEventFactory.onFinalizeSpawn((Mob) mob, level, level.getCurrentDifficultyAt(getBlockPos()), MobSpawnType.EVENT, null, null);
                            }
                            ModUtil.SpawnEntityHelper.spawn(level, random, entityType, mob, blockCoords);
                        },
                        () -> {
                            Treasure.LOGGER.debug("unable to get entityType -> {}", mobName);
                            collection.remove(mobName);
                        });
                Optional<EntityType<?>> entityType = EntityType.byString(mobName.toString());
            }
        });
        // TODO this doesn't account for a wrong mobSet ID - do we ignore or use a default list?
        this.selfDestruct();
    }

    private void selfDestruct() {
        Treasure.LOGGER.debug("self-destructing @ {}", this.getBlockPos());
        this.setDead(true);
        this.getLevel().setBlock(this.getBlockPos(), Blocks.AIR.defaultBlockState(), 3);
        this.getLevel().removeBlockEntity(this.getBlockPos());
    }

    public ResourceLocation getMobSetName() {
        return mobSetName;
    }

    public void setMobSetName(ResourceLocation mobSetName) {
        this.mobSetName = mobSetName;
    }

    public List<ResourceLocation> getMobSetNames() {
        return mobSetNames != null ? mobSetNames : (mobSetNames = new ArrayList<>());
    }

    public void setMobSetNames(List<ResourceLocation> mobSetNames) {
        this.mobSetNames = mobSetNames;
    }

    public IntegerRange getMobSizeRange() {
        return mobSizeRange;
    }

    public void setMobSizeRange(IntegerRange mobSizeRange) {
        this.mobSizeRange = mobSizeRange;
    }
}
