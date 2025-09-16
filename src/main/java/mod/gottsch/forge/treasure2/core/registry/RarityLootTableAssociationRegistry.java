package mod.gottsch.forge.treasure2.core.registry;

import mod.gottsch.forge.treasure2.core.loot.ILootTableTypes;
import mod.gottsch.forge.treasure2.core.loot.TreasureLootTableTypes;
import mod.gottsch.forge.treasure2.core.rarity.IRarityEntry;
import mod.gottsch.forge.treasure2.core.rarity.RarityLootTableAssociation;
import mod.gottsch.forge.treasure2.core.rarity.RarityTagAssociation;
import mod.gottsch.forge.treasure2.core.rarity.TreasureRarities;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import org.apache.commons.compress.utils.Lists;

import java.util.List;
import java.util.Optional;

/**
 * @author by Mark Gottschling on 8/31/2025
 */
public enum RarityLootTableAssociationRegistry {
    INSTANCE;

    //    private static final Map<IRarityEntry, RarityLootTableAssociation> MAP = Maps.newHashMap();
    private static final List<RarityLootTableAssociation> LIST = Lists.newArrayList();

    public static synchronized void clear() {
        LIST.clear();
    }

    public static synchronized void register(RarityLootTableAssociation data) {
        LIST.add(data);
    }

    public static synchronized List<RarityLootTableAssociation> getAssociations() {
        return List.copyOf(LIST);
    }

    /**
     * given the type and rarity returns the loot table id/name.
     * @param type
     * @param rarity
     * @return
     */
    public static synchronized Optional<ResourceLocation> getLootTableId(ILootTableTypes type, IRarityEntry rarity) {
        return RarityLootTableAssociationRegistry.getAssociations().stream()
                // transform association's rarityId into a RarityEntry class and compare
                .filter(association -> TreasureRarities.getRarityByName(association.rarityId())
                        .filter(rarity::equals)
                        .isPresent())
                .filter( association -> TreasureLootTableTypes.getTypeByName(association.lootTableId())
                        .filter(type::equals)
                        .isPresent())
                .findFirst()
                .map(RarityLootTableAssociation::lootTableId);
    }
}