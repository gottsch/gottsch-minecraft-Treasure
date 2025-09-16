package mod.gottsch.forge.treasure2.core.registry;

import com.google.common.collect.Maps;
import mod.gottsch.forge.treasure2.core.rarity.IRarityEntry;
import mod.gottsch.forge.treasure2.core.rarity.RarityTagAssociation;
import mod.gottsch.forge.treasure2.core.rarity.TreasureRarities;
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

    public static Optional<TagKey<Block>> getChestTagKey(IRarityEntry rarity) {
        return RarityTagAssociationRegistry.getChestAssociation().stream()
                .filter(association -> TreasureRarities.getRarityByName(association.rarityId())
                        .filter(rarity::equals)
                        .isPresent())
                .findFirst()
                .map(RarityTagAssociation::getBlockTag);
    }

    public static List<Block> getChestBlocks(IRarityEntry rarity) {
//
//        Optional<TagKey<Block>> tagKey2 = getChestTagKey(rarity);
//        ITag<Block> tag = ForgeRegistries.BLOCKS.tags().getTag(tagKey2.get());
//        List<Block> blocks = tag.stream().toList();

        return getChestTagKey(rarity)
                .map(tagKey -> ForgeRegistries.BLOCKS.tags().getTag(tagKey).stream().toList())
                .orElse(Collections.emptyList());
    }

    /**
     * helper method to get Key (Item) associations.
     * @return
     */
    public static List<RarityTagAssociation> getKeysAssociation() {
        return Items.getAssociation(KEYS);
    }

    public static Optional<TagKey<Item>> getKeysTagKey(IRarityEntry rarity) {
        return RarityTagAssociationRegistry.getKeysAssociation().stream()
                .filter(association -> TreasureRarities.getRarityByName(association.rarityId())
                        .filter(rarity::equals)
                        .isPresent())
                .findFirst()
                .map(RarityTagAssociation::getItemTag);
    }

    public static List<Item> getKeyItems(IRarityEntry rarity) {

        Optional<TagKey<Item>> keysTagKey = getKeysTagKey(rarity);
        ITag<Item> tag = ForgeRegistries.ITEMS.tags().getTag(keysTagKey.get());
        List<Item> items = tag.stream().toList();

        return getKeysTagKey(rarity)
                .map(tagKey -> ForgeRegistries.ITEMS.tags().getTag(tagKey).stream().toList())
                .orElse(Collections.emptyList());
    }

    /**
     * helper method to get Key (Item) associations.
     * @return
     */
    public static List<RarityTagAssociation> getLocksAssociation() {
        return Items.getAssociation(LOCKS);
    }

    public static Optional<TagKey<Item>> getLocksTagKey(IRarityEntry rarity) {
        return RarityTagAssociationRegistry.getLocksAssociation().stream()
                .filter(association -> TreasureRarities.getRarityByName(association.rarityId())
                        .filter(rarity::equals)
                        .isPresent())
                .findFirst()
                .map(RarityTagAssociation::getItemTag);
    }

    public static List<Item> getLockItems(IRarityEntry rarity) {

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
