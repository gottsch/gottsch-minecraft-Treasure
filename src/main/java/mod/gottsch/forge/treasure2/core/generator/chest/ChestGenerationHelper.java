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
package mod.gottsch.forge.treasure2.core.generator.chest;

import mod.gottsch.forge.gottschcore.random.RandomHelper;
import mod.gottsch.forge.gottschcore.spatial.Coords;
import mod.gottsch.forge.gottschcore.spatial.ICoords;
import mod.gottsch.forge.gottschcore.world.WorldInfo;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.block.entity.AbstractTreasureChestBlockEntity;
import mod.gottsch.forge.treasure2.core.cache.TreasureChestCache;
import mod.gottsch.forge.treasure2.core.cache.data.TreasureChestCacheData;
import mod.gottsch.forge.treasure2.core.config.Config;
import mod.gottsch.forge.treasure2.core.loot.TreasureLootTableTypes;
import mod.gottsch.forge.treasure2.core.rarity.IRarity;
import mod.gottsch.forge.treasure2.core.rarity.RarityOrder;
import mod.gottsch.forge.treasure2.core.rarity.TreasureRarities;
import mod.gottsch.forge.treasure2.core.registry.LootTableRegistry;
import mod.gottsch.forge.treasure2.core.registry.RarityOrderRegistry;
import mod.gottsch.forge.treasure2.core.util.LangUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.ItemStackHandler;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.IntStream;

/**
 *
 * @author by Mark Gottschling on 9/3/2025
 */
public class ChestGenerationHelper {
    public static final String TREASURE_POOL_NAME = "treasure";
    private static final Predicate<LootPool> IS_TREASURE_POOL = pool -> TREASURE_POOL_NAME.equalsIgnoreCase(pool.getName());
    private static final Predicate<LootPool> IS_FILLER_POOL = IS_TREASURE_POOL.negate();

    // TODO could move to LootGenerator as it would be the "base" loot class.
    //  this helper class is more specialized.
    /**
     * selects a random loot table resource location from the registry based on the given rarity.
     *
     * @param random the random source.
     * @param rarity the rarity entry to filter loot tables by.
     * @return an Optional containing the ResourceLocation, or empty if none are found.
     */
    public static Optional<ResourceLocation> randomLootTable(RandomSource random, IRarity rarity) {
        List<ResourceLocation> lootTables = LootTableRegistry.getLootTableIds(TreasureLootTableTypes.CHESTS.get(), rarity);
        if (lootTables.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(lootTables.get(random.nextInt(lootTables.size())));
    }


    /**
     * main method to fill a treasure chest with loot. it selects a loot table, generates items,
     * injects additional items from other tables, and places them into the chest's inventory.
     *
     * @param level       the server level where the chest is located.
     * @param random      yhe random source for generation.
     * @param blockEntity the block entity of the chest.
     * @param rarity      the rarity of the chest.
     * @param player      the player context, can be null.
     */
    public static void fillChest(final ServerLevel level, RandomSource random, final BlockEntity blockEntity, IRarity rarity, Player player) {
        if (!(blockEntity instanceof AbstractTreasureChestBlockEntity chestBlockEntity)) {
            return;
        }

        // guard against a missing generation context (e.g. sealed with no rarity ever recorded) so
        // loot generation degrades to a sane default instead of NPE-ing partway through and leaving
        // the chest permanently empty.
        if (rarity == null) {
            Treasure.LOGGER.warn("chest at -> {} has no loot rarity recorded, defaulting to common", chestBlockEntity.getBlockPos());
            rarity = TreasureRarities.COMMON.get();
        }
        final IRarity finalRarity = rarity;

        chestBlockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(itemHandler -> {
            // 1. select a loot table for the chest
            Optional<ResourceLocation> lootTableIdOpt = selectLootTable(chestBlockEntity, random, finalRarity);
            if (lootTableIdOpt.isEmpty()) {
                Treasure.LOGGER.warn("could not determine a loot table for chest at -> {}", chestBlockEntity.getBlockPos());
                return;
            }
            ResourceLocation lootTableId = lootTableIdOpt.get();

            LootTable lootTable = LootTableRegistry.getLootTable(level, lootTableId);
            if (lootTable == LootTable.EMPTY) {
                Treasure.LOGGER.warn("unable to load lootTable -> {}", lootTableId);
                return;
            }
            Treasure.LOGGER.debug("selected loot table -> {} for chest at -> {}", lootTableId, chestBlockEntity.getBlockPos());

            // 2. prepare for loot generation
            LootContext lootContext = createLootContext(level, chestBlockEntity.getBlockPos(), player, lootTableId);

            // 3. generate loot from the primary table
            List<ItemStack> treasureStacks = new ArrayList<>();
            List<ItemStack> fillerStacks = new ArrayList<>();
            generateLootFromPools(lootTable, lootContext, treasureStacks, fillerStacks);
            int originalTreasureSize = treasureStacks.size();
            int originalFillerSize = fillerStacks.size();

            // 4. inject loot from other registered tables
            addInjectedLoot(level, finalRarity, lootContext, treasureStacks, fillerStacks);
            Treasure.LOGGER.debug("total treasure items: {}, fill items: {}", treasureStacks.size(), fillerStacks.size());

            // 5. add a treasure map to the inventory
            // TODO should only add a treasure map to core OR have to get the order set ie core, speciality, etc.
            // TODO orders should be housed in TreasureRarities
            // TODO if an ordering cannot be found then don't add a map
            addTreasureMap(level, random, (ItemStackHandler) itemHandler, Coords.of(chestBlockEntity.getBlockPos()), finalRarity);

            // 6. populate the chest inventory
            populateInventory((ItemStackHandler) itemHandler, random, treasureStacks, originalTreasureSize);
            populateInventory((ItemStackHandler) itemHandler, random, fillerStacks, originalFillerSize);
        });
    }

    /**
     * determines the loot table to use, preferring the one set on the chest entity, otherwise selecting a random one.
     */
    private static Optional<ResourceLocation> selectLootTable(AbstractTreasureChestBlockEntity chest, RandomSource random, IRarity rarity) {
        return Optional.ofNullable(chest.getLootTable()).or(() -> randomLootTable(random, rarity));
    }

    // TODO this could be moved to LootGenerator
    /**
     * creates the LootContext needed for loot generation.
     */
    public static LootContext createLootContext(ServerLevel level, BlockPos position, Player player, ResourceLocation lootTableId) {
        LootParams.Builder paramsBuilder = new LootParams.Builder(level)
                .withParameter(LootContextParams.ORIGIN, new Vec3(position.getX(), position.getY(), position.getZ()));
        if (player != null) {
            paramsBuilder.withLuck(player.getLuck()).withParameter(LootContextParams.THIS_ENTITY, player);
        }
        LootParams params = paramsBuilder.create(LootContextParamSets.CHEST);
        return new LootContext.Builder(params).create(lootTableId);
    }

    /**
     * generates items from a loot table's pools, sorting them into treasure and filler lists.
     */
    public static void generateLootFromPools(LootTable lootTable, LootContext context, List<ItemStack> treasureOut, List<ItemStack> fillerOut) {
        for (LootPool pool : lootTable.pools) {
            if (IS_TREASURE_POOL.test(pool)) {
                Treasure.LOGGER.debug("generating loot from treasure pool -> {}", pool.getName());
                pool.addRandomItems(treasureOut::add, context);
            } else {
                Treasure.LOGGER.debug("generating loot from filler pool -> {}", pool.getName());
                pool.addRandomItems(fillerOut::add, context);
            }
        }
    }

    /**
     * finds and adds loot from injectable loot tables based on rarity.
     */
    public static void addInjectedLoot(ServerLevel level, IRarity rarity, LootContext context, List<ItemStack> treasureOut, List<ItemStack> fillerOut) {
        List<ResourceLocation> injectLootTables = LootTableRegistry.getLootTableIds(TreasureLootTableTypes.INJECTS.get(), rarity)
                .stream()
                .filter(s -> s.getPath().contains(TreasureLootTableTypes.CHESTS.get().getName()))
                .toList();

        if (injectLootTables.isEmpty()) return;

        Treasure.LOGGER.debug("found {} injectable loot tables for rarity -> {}", injectLootTables.size(), rarity.getName());
        treasureOut.addAll(getItemsFromLootTables(level, injectLootTables, context, IS_TREASURE_POOL));
        fillerOut.addAll(getItemsFromLootTables(level, injectLootTables, context, IS_FILLER_POOL));
    }

    /**
     * shuffles and places the generated loot into the inventory, respecting the original item counts from the primary loot table.
     */
    private static void populateInventory(ItemStackHandler inventory, RandomSource random, List<ItemStack> itemStacks, int sizeLimit) {
        // shuffle and add items
        Collections.shuffle(itemStacks, new Random());
        fillSlots(inventory, random, itemStacks.stream().limit(sizeLimit).toList());
    }

    /**
     * attempts to add a treasure map to the chest's inventory if conditions are met.
     *
     * @param level       the server-side level.
     * @param random      the random source.
     * @param inventory   the chest's inventory.
     * @param chestCoords the coordinates of the chest being filled.
     * @param rarity      the rarity of the chest being filled.
     */
    public static void addTreasureMap(ServerLevel level, RandomSource random, ItemStackHandler inventory, ICoords chestCoords, IRarity rarity) {
        // 1. check if there is space and if maps are enabled and pass a probability check
        if (getEmptySlotsRandomized(inventory, random).isEmpty()
                || !Config.SERVER.maps.enableMaps.get()
                || !RandomHelper.checkProbability(random, Config.SERVER.maps.mapProbability.get())) {
            return;
        }

        // 2. determine the rarity of the chest the map will point to
        IRarity mapRarity = getBoostedRarity(rarity, getRarityBoostAmount()).orElse(rarity);
        Treasure.LOGGER.debug("attempting to generate a treasure map of rarity {} in a {} chest.", mapRarity.getName(), rarity.getName());

        // 3. find a valid, undiscovered, un-charted chest location of the target rarity
        findRandomMapTarget(level, random, mapRarity)
                // filter out the current chest
                .filter(map -> !map.getCoords().equals(chestCoords))
                .ifPresent(targetContext -> {
            Treasure.LOGGER.debug("found map target: {}", targetContext);

            // 4. create the map item and add it to the inventory
            ItemStack mapStack = createMap(level, targetContext.getCoords(), mapRarity, (byte) 4);
            getEmptySlotsRandomized(inventory, random).stream().findFirst().ifPresent(slot -> {
                inventory.setStackInSlot(slot, mapStack);
                targetContext.setChartedFrom(chestCoords);

                // TODO reseach why this flag is being performed. it should already been set on using the chest for the first time.
                //  this is probably legacy code from when chests were filled at time of generation.
                // 5. update context: mark target as 'charted' and this chest as 'discovered'
                updateSourceChestContext(level, chestCoords, rarity);
            });
        });
    }

    /**
     * creates a treasure map ItemStack pointing to a specific location.
     *
     * @param level  the server-side level.
     * @param coords the target coordinates for the map.
     * @param rarity the rarity, used for the map's name.
     * @param zoom   the zoom level of the map.
     * @return a configured treasure map as an ItemStack.
     */
    public static ItemStack createMap(ServerLevel level, ICoords coords, IRarity rarity, byte zoom) {
        ItemStack itemStack = MapItem.create(level, coords.getX(), coords.getZ(), zoom, true, true);
        MapItem.renderBiomePreviewMap(level, itemStack);
        MapItemSavedData.addTargetDecoration(itemStack, coords.toPos(), "+", MapDecoration.Type.RED_X);
        itemStack.setHoverName(Component.translatable(LangUtil.screen("treasure_map." + rarity.getName())));
        return itemStack;
    }

    /**
     * finds a random, valid chest generation context to be used as a treasure map target.
     */
    private static Optional<TreasureChestCacheData> findRandomMapTarget(ServerLevel level, RandomSource random, IRarity targetRarity) {
        List<TreasureChestCacheData> cache = TreasureChestCache.getCache();

        if (cache.isEmpty()) {
            return Optional.empty();
        }

        List<TreasureChestCacheData> potentialTargets = cache.stream()
                .filter(spawn -> WorldInfo.getDimension(level).equals(spawn.getDimensionName()))
                .filter(spawn -> !spawn.isDiscovered() && !spawn.isCharted())
                .toList();

        if (potentialTargets.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(potentialTargets.get(random.nextInt(potentialTargets.size())));
    }

    /**
     * updates the source chest's context to mark it as discovered.
     */
    private static void updateSourceChestContext(ServerLevel level, ICoords chestCoords, IRarity rarity) {
        List<TreasureChestCacheData> cache = TreasureChestCache.getCache();

        if (cache.isEmpty()) return;

        Optional<TreasureChestCacheData> target = cache.stream()
                .filter(spawn -> WorldInfo.getDimension(level).equals(spawn.getDimensionName()))
                .filter(spawn -> !spawn.getCoords().equals(chestCoords))
                .findFirst();

        target.ifPresent(spawn -> spawn.setDiscovered(true));
    }

    public static Optional<IRarity> getBoostedRarity(IRarity rarity, int amount) {
        Treasure.LOGGER.debug("boosted amount -> {}", amount);
        // check the registry(s) for the rarity
        List<RarityOrder> rarityOrders = new ArrayList<>(RarityOrderRegistry.getCore());
        // TODO sort the list of RarityOrders into a List of Rarity
        rarityOrders.sort(RarityOrder.BY_ORDER);
//        List<IRarity> sortedRarities = rarityOrders.stream()
//                // sort by the 'order' field comparator in ascending order.
//                .sorted(RarityOrder.BY_ORDER)
//                // map the RarityOrder to an Optional<IRarity> using the resource location.
//                .map(order -> TreasureRarities.getRarityByName(order.rarity()))
//                // filter out any entries that didn't resolve to an IRarity.
//                .filter(Optional::isPresent)
//                // unwrap the Optional to get the IRarity object.
//                .map(Optional::get)
//                // collect the results into a List<IRarity>.
//                .toList(); // or .collect(Collectors.toList()) for older Java versions

//        Treasure.LOGGER.debug("sorted rarity order -> {}", sortedRarities);
        int index = getIndex(rarityOrders, rarity);
        Treasure.LOGGER.debug("index of current rarity {} -> {}", rarity, index);

        // check if the rarity was found and if there's an element after it.
        if (index != -1 && index + amount < rarityOrders.size()) {
            RarityOrder rarityOrder = rarityOrders.get(index + amount);
            Treasure.LOGGER.debug("boosted rarity order -> {}", rarityOrder);
            Optional<IRarity> boostedRarity = TreasureRarities.getRarityByName(rarityOrder.rarity());
//            return TreasureRarities.getRarityByName(rarityOrder.rarity());
            Treasure.LOGGER.debug("boosted rarity -> {}", boostedRarity.orElse(TreasureRarities.UNKNOWN.get()));
            return boostedRarity;
        }

        return Optional.empty();
    }

    /**
     * @param rarity the rarity to search by
     * @return
     */
    private static int getIndex(List<RarityOrder> rarityOrders, IRarity rarity) {
        OptionalInt firstIndex = IntStream.range(0, rarityOrders.size())
                .filter(i -> TreasureRarities.getRarityByName(rarityOrders.get(i).rarity())
                        .orElse(TreasureRarities.SCARCE.get())
                        .equals(rarity))
                .findFirst();

        return firstIndex.orElse(-1);
    }

    /**
     * determines how much to boost the rarity for a treasure map target.
     * higher boost values are rarer.
     *
     * @return the amount to boost the rarity code by.
     */
    public static int getRarityBoostAmount() {
        double probability = RandomHelper.randomDouble(0, 100);
        if (probability < 5.0) return 3;  // 5% chance for +3 boost
        if (probability < 15.0) return 2; // 15% chance for +2 boost
        return 1;                         // 80% chance for +1 boost
    }

    /**
     * retrieves items from a list of loot tables based on a pool predicate.
     *
     * @param level          the server level.
     * @param lootTableNames the list of loot table resource locations.
     * @param lootContext    the context for loot generation.
     * @param predicate      the predicate to filter loot pools.
     * @return a list of generated ItemStacks.
     */
    private static List<ItemStack> getItemsFromLootTables(ServerLevel level, List<ResourceLocation> lootTableNames, LootContext lootContext, Predicate<LootPool> predicate) {
        List<ItemStack> itemStacks = new ArrayList<>();
        for (ResourceLocation lootTableName : lootTableNames) {
            LootTable lootTable = LootTableRegistry.getLootTable(level, lootTableName);
            if (lootTable != LootTable.EMPTY) {
                lootTable.pools.stream()
                        .filter(predicate)
                        .forEach(pool -> pool.addRandomItems(itemStacks::add, lootContext));
            }
        }
        return itemStacks;
    }

    /**
     * places a list of items into random empty slots in the provided inventory.
     *
     * @param inventory the inventory to fill.
     * @param random    the random source.
     * @param items     the list of items to add.
     */
    private static void fillSlots(ItemStackHandler inventory, RandomSource random, List<ItemStack> items) {
        List<Integer> emptySlots = getEmptySlotsRandomized(inventory, random);
        if (emptySlots.isEmpty()) return;

        for (ItemStack itemstack : items) {
            if (emptySlots.isEmpty()) {
                Treasure.LOGGER.warn("ran out of inventory space while filling chest.");
                return;
            }
            if (!itemstack.isEmpty()) {
                inventory.setStackInSlot(emptySlots.remove(emptySlots.size() - 1), itemstack);
            }
        }
    }

    /**
     * Gets a shuffled list of indices corresponding to empty slots in an inventory.
     *
     * @param inventory The inventory to check.
     * @param random    The random source for shuffling.
     * @return A list of shuffled empty slot indices.
     */
    private static List<Integer> getEmptySlotsRandomized(ItemStackHandler inventory, RandomSource random) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < inventory.getSlots(); ++i) {
            if (inventory.getStackInSlot(i).isEmpty()) {
                list.add(i);
            }
        }
        Collections.shuffle(list, new Random());
        return list;
    }
}