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
package mod.gottsch.forge.treasure2.core.wishable;

import mod.gottsch.forge.gottschcore.spatial.ICoords;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.item.TreasureItems;
import mod.gottsch.forge.treasure2.core.loot.TreasureLootGenerators;
import mod.gottsch.forge.treasure2.core.loot.TreasureLootTableTypes;
import mod.gottsch.forge.treasure2.core.rarity.IRarity;
import mod.gottsch.forge.treasure2.core.rarity.TreasureRarities;
import mod.gottsch.forge.treasure2.core.registry.LootTableRegistry;
import mod.gottsch.forge.treasure2.core.registry.RarityTagAssociationRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.*;
import java.util.function.Predicate;

/**
 * @author by Mark Gottschling on 8/25/2025
 */
public class WishableHandler implements IWishableHandler {

    public Optional<ItemStack> generateLoot(Level level, Random random, ItemEntity itemEntity, ICoords coords) {
        // determine item rarity with a safe fallback.
        IRarity rarity = TreasureWishables.getRarity(itemEntity.getItem().getItem())
                .orElse(TreasureRarities.COMMON.get());

        // get loot tables, returning a default item if none are found.
        List<ResourceLocation> lootTableNames = getLootTables(rarity);
        if (lootTableNames.isEmpty()) {
            return Optional.of(getDefaultLootKey(random, rarity));
        }

        // attempt to get the player who dropped the item.
        Optional<Player> playerOptional = getPlayerFromItem(level, itemEntity);
        playerOptional.ifPresent(p -> Treasure.LOGGER.debug("coin dropped by player -> {}", p.getName()));

        // select a random loot table and get the corresponding LootTable.
        ResourceLocation lootTableName = lootTableNames.get(random.nextInt(lootTableNames.size()));
        LootTable table = LootTableRegistry.getLootTable((ServerLevel) level, lootTableName);

        // generate loot context from the player.
        LootContext lootContext = getLootContext(level, playerOptional.orElse(null), coords);

        // process main loot pools and inject additional loot.
        List<ItemStack> generatedItems = new ArrayList<>();
        table.pools.forEach(pool -> {
            Treasure.LOGGER.debug("processing pool -> {}", pool.getName());
            LootPool lootPool = table.getPool(pool.getName());
            lootPool.addRandomItems(generatedItems::add, lootContext);
        });

        injectLoot(level, random, generatedItems, rarity, lootContext);

        // log possible loot items for debugging.
        if (Treasure.LOGGER.isDebugEnabled()) {
            generatedItems.forEach(stack -> Treasure.LOGGER.debug("possible loot item -> {}", stack.getItem()));
        }

        // Select and return a single random item from the generated list, or empty if no items.
        return generatedItems.isEmpty()
                ? Optional.empty()
                : Optional.of(generatedItems.get(random.nextInt(generatedItems.size())));
    }

    /**
     * safely gets a Player from an ItemEntity's owner UUID.
     * @return an Optional containing the Player, or empty if not found or an exception occurs.
     */
    private Optional<Player> getPlayerFromItem(Level world, ItemEntity itemEntity) {
        if (itemEntity.getOwner() == null) {
            return Optional.empty();
        }
        try {
            UUID playerUuid = itemEntity.getOwner().getUUID();
            return Optional.ofNullable(world.getPlayerByUUID(playerUuid));
        } catch (Exception e) {
            // Catch silently as per original logic
            return Optional.empty();
        }
    }

    /**
     * @param rarity the rarity of the loot tables to retrieve.
     * @return
     */
    public List<ResourceLocation> getLootTables(IRarity rarity) {
        return LootTableRegistry.getLootTableIds(TreasureLootTableTypes.WISHABLES.get(), rarity);
    }

    /**
     *
     * @param random
     * @param itemEntity
     * @return
     */
    public ItemStack getDefaultLootKey (Random random, ItemEntity itemEntity) {
        IRarity rarity = TreasureWishables.getRarity(itemEntity.getItem().getItem()).orElse(TreasureRarities.COMMON.get());
        return getDefaultLootKey(random, rarity);
    }

    /**
     *
     * @param random
     * @param rarity
     * @return
     */
    public ItemStack getDefaultLootKey(Random random, IRarity rarity) {
        List<Item> keys = RarityTagAssociationRegistry.getKeyItems(rarity);
        Item selectedKey = keys.isEmpty()
                ? TreasureItems.THIEFS_LOCK_PICK.get()
                : keys.get(random.nextInt(keys.size()));

        return new ItemStack(selectedKey);
    }

    /**
     *
     * @param level
     * @param player
     * @return
     */
    public LootContext getLootContext(Level level, Player player, ICoords coords) {
        LootParams.Builder lootParamsBuilder = new LootParams.Builder((ServerLevel) level)
                .withParameter(LootContextParams.ORIGIN, coords.toVec3());

        Optional.ofNullable(player)
                .ifPresent(p -> lootParamsBuilder.withLuck(p.getLuck()).withParameter(LootContextParams.THIS_ENTITY, p));

        LootParams params = lootParamsBuilder.create(LootContextParamSets.CHEST);
        return new LootContext.Builder(params).create(null);
    }

    /**
     *
     * @param level
     * @param random
     * @param itemStacks
     * @param rarity
     * @param lootContext
     */
    public void injectLoot(Level level, Random random, List<ItemStack> itemStacks, IRarity rarity, LootContext lootContext) {
        List<ResourceLocation> injectLootTableNames = LootTableRegistry.getLootTableIds(TreasureLootTableTypes.WISHABLES.get(), rarity);

        if (!injectLootTableNames.isEmpty()) {
            Treasure.LOGGER.debug("size of injectable tables -> {}", injectLootTableNames.size());
            itemStacks.addAll(getInjectedLootItems(level, random, injectLootTableNames, lootContext, p -> {
                return !p.getName().equalsIgnoreCase(TreasureLootGenerators.TREASURE_POOL);
            }));
        }
    }

    public List<ItemStack> getInjectedLootItems(Level level, Random random, List<ResourceLocation> lootTableNames,
                                                LootContext lootContext, Predicate<LootPool> predicate) {

        List<ItemStack> itemStacks = new ArrayList<>();

        lootTableNames.forEach(injectLootTableName -> {
            Treasure.LOGGER.debug("injectable resource -> {}", injectLootTableName);

            LootTable injectLootTable = LootTableRegistry.getLootTable((ServerLevel) level, injectLootTableName);

            if (injectLootTable != LootTable.EMPTY) {
                injectLootTable.pools.stream()
                        .filter(predicate)
                        .forEach(lootPool -> lootPool.addRandomItems(itemStacks::add, lootContext));

                Treasure.LOGGER.debug("size of item stacks after inject -> {}", itemStacks.size());
            }
        });

        return itemStacks;
    }
}
