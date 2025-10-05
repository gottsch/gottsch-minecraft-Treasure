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
package mod.gottsch.forge.treasure2.core.registry;

import com.google.common.collect.Maps;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.rarity.IRarity;
import mod.gottsch.forge.treasure2.core.rarity.RarityTagAssociation;
import mod.gottsch.forge.treasure2.core.rarity.TreasureRarities;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITag;

import java.util.*;

/**
 * @author by Mark Gottschling on 8/26/2025
 */
public enum RarityTagAssociationRegistry {
    INSTANCE;

    public static final String ITEM = "item";
    public static final String BLOCK = "block";
    public static final String WISHABLE = "wishable";
    public static final String KEYS = "key";
    public static final String LOCKS = "lock";
    public static final String CHEST = "chest";

    private static final Map<String, List<RarityTagAssociation>> ITEM_MAP = Maps.newHashMap();
    private static final Map<String, List<RarityTagAssociation>> BLOCK_MAP = Maps.newHashMap();

    public static class Items {
        public static void clear() {
            ITEM_MAP.clear();
        }

        public static void register(String key, RarityTagAssociation itemAssociation) {
            ITEM_MAP.computeIfAbsent(key, k -> new ArrayList<>()).add(itemAssociation);
        }

        public static List<RarityTagAssociation> register(String key, List<RarityTagAssociation> itemAssociations) {
            return Optional.ofNullable(ITEM_MAP.put(key.trim().toLowerCase(), itemAssociations)).orElse(new ArrayList<>());
        }

        public static List<RarityTagAssociation> getAssociation(String key) {
            return Optional.ofNullable(ITEM_MAP.get(key.trim().toLowerCase())).orElse(new ArrayList<>());
        }
    }

    /**
     * helper method to get Chest (Block) associations.
     * @return
     */
    public static List<RarityTagAssociation> getChestAssociation() {
        return Blocks.getAssociation(CHEST);
    }

    public static Optional<TagKey<Block>> getChestTagKey(IRarity rarity) {
        return RarityTagAssociationRegistry.getChestAssociation().stream()
                .filter(association -> TreasureRarities.getRarityByName(association.rarityId())
                        .filter(rarity::equals)
                        .isPresent())
                .findFirst()
                .map(RarityTagAssociation::getBlockTag);
    }

    public static List<Block> getChestBlocks(IRarity rarity) {
//
//        Optional<TagKey<Block>> tagKey2 = getChestTagKey(rarity);
//        ITag<Block> tag = ForgeRegistries.BLOCKS.tags().getTag(tagKey2.get());
//        List<Block> blocks = tag.stream().toList();

        return getChestTagKey(rarity)
                .map(tagKey -> {
                    var a = ForgeRegistries.BLOCKS.tags().getTag(tagKey).stream().toList();
                    Treasure.LOGGER.debug("list of chests by rarity {} -> {}", rarity, a);
                    return a;
//                    ForgeRegistries.BLOCKS.tags().getTag(tagKey).stream().toList()
                })
                .orElse(Collections.emptyList());
    }

    /**
     * finds the rarity for a given block based on the rarity tag associations.
     * this method requires a HolderLookup.Provider to get the block's registry holder.
     * @param block The Block to check for rarity.
     * @param registries The HolderLookup.Provider to get the block's holder.
     * @return An Optional containing the IRarityEntry if found, otherwise an empty Optional.
     */
    public static Optional<IRarity> getChestRarity(final Block block, final HolderLookup.Provider registries) {
        // get the list of rarity associations for the chests.
        List<RarityTagAssociation> associations = getChestAssociation();

        // get the Holder for the block from the registry.
        Optional<Holder<Block>> blockHolderOptional = registries.lookup(Registries.BLOCK)
                .flatMap(lookup -> lookup.get(block.builtInRegistryHolder().key()));

        if (blockHolderOptional.isPresent()) {
            Holder<Block> blockHolder = blockHolderOptional.get();

            // iterate through the associations to find a match.
            for (RarityTagAssociation association : associations) {
                TagKey<Block> tagKey = association.getBlockTag();

                // use the is() method on the Holder to check if the block is in the tag.
                // this is the correct and efficient way to check for tag membership.
                if (blockHolder.is(tagKey)) {
                    // if a match is found, return the corresponding rarity.
                    return TreasureRarities.getRarityByName(association.rarityId());
                }
            }
        }

        return Optional.empty();
    }

    /**
     * helper method to get Key (Item) associations.
     * @return
     */
    public static List<RarityTagAssociation> getKeysAssociation() {
        return Items.getAssociation(KEYS);
    }

    public static Optional<TagKey<Item>> getKeysTagKey(IRarity rarity) {
        return RarityTagAssociationRegistry.getKeysAssociation().stream()
                .filter(association -> TreasureRarities.getRarityByName(association.rarityId())
                        .filter(rarity::equals)
                        .isPresent())
                .findFirst()
                .map(RarityTagAssociation::getItemTag);
    }

    public static List<Item> getKeyItems(IRarity rarity) {

        Optional<TagKey<Item>> keysTagKey = getKeysTagKey(rarity);
        ITag<Item> tag = ForgeRegistries.ITEMS.tags().getTag(keysTagKey.get());
        List<Item> items = tag.stream().toList();

        return getKeysTagKey(rarity)
                .map(tagKey -> ForgeRegistries.ITEMS.tags().getTag(tagKey).stream().toList())
                .orElse(Collections.emptyList());
    }

    public static Optional<IRarity> getItemRarity(final Item item, final String type, final HolderLookup.Provider registries) {
        // get the list of rarity associations for the chests.
        List<RarityTagAssociation> associations = Items.getAssociation(type);

        // get the Holder for the block from the registry.
        Optional<Holder<Item>> holderOptional = registries.lookup(Registries.ITEM)
                .flatMap(lookup -> lookup.get(item.builtInRegistryHolder().key()));

        if (holderOptional.isPresent()) {
            Holder<Item> itemHolder = holderOptional.get();

            // iterate through the associations to find a match.
            for (RarityTagAssociation association : associations) {
                TagKey<Item> tagKey = association.getItemTag();

                // use the is() method on the Holder to check if the block is in the tag.
                // this is the correct and efficient way to check for tag membership.
                if (itemHolder.is(tagKey)) {
                    // if a match is found, return the corresponding rarity.
                    return TreasureRarities.getRarityByName(association.rarityId());
                }
            }
        }

        return Optional.empty();

    }
    public static Optional<IRarity> getKeyRarity(final Item item, final HolderLookup.Provider registries) {
        return getItemRarity(item, KEYS, registries);
    }

    public static Optional<IRarity> getLockRarity(final Item item, final HolderLookup.Provider registries) {
        return getItemRarity(item, LOCKS, registries);
    }

    /**
     * helper method to get Key (Item) associations.
     * @return
     */
    public static List<RarityTagAssociation> getLocksAssociation() {
        return Items.getAssociation(LOCKS);
    }

    public static Optional<TagKey<Item>> getLocksTagKey(IRarity rarity) {
        return RarityTagAssociationRegistry.getLocksAssociation().stream()
                .filter(association -> TreasureRarities.getRarityByName(association.rarityId())
                        .filter(rarity::equals)
                        .isPresent())
                .findFirst()
                .map(RarityTagAssociation::getItemTag);
    }

    public static List<Item> getLockItems(IRarity rarity) {

//        Optional<TagKey<Item>> locksTagKey = getKeysTagKey(rarity);
//        ITag<Item> tag = ForgeRegistries.ITEMS.tags().getTag(locksTagKey.get());
//        List<Item> items = tag.stream().toList();

        return getLocksTagKey(rarity)
                .map(tagKey -> ForgeRegistries.ITEMS.tags().getTag(tagKey).stream().toList())
                .orElse(Collections.emptyList());
    }

    /**
     * generic class that works with Blocks
     */
    public static class Blocks {
        public static void clear() {
            BLOCK_MAP.clear();
        }

        public static void register(String key, RarityTagAssociation association) {
            BLOCK_MAP.computeIfAbsent(key, k -> new ArrayList<>()).add(association);
        }

        public static List<RarityTagAssociation> getAssociation(String key) {
            return Optional.ofNullable(BLOCK_MAP.get(key.trim().toLowerCase())).orElse(new ArrayList<>());
        }
    }
}
