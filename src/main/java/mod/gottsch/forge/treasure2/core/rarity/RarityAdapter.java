package mod.gottsch.forge.treasure2.core.rarity;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import mod.gottsch.forge.gottschcore.enums.IRarity;
import mod.gottsch.forge.treasure2.core.enums.Rarity;
import mod.gottsch.forge.treasure2.core.enums.SpecialRarity;

import java.util.Map;

/**
 * NOTE this class will be removed after all IRarity are replaced with IRarityEntry
 * @author by Mark Gottschling on 8/26/2025
 */
public enum RarityAdapter {
    INSTANCE;

    // TEMP
    /// ///////////////
    // adapter map for legacy IRarity methods. this will be removed when all IRarity is removed from Mod
    private final static BiMap<IRarityEntry, IRarity> RARITY_ADAPTER = HashBiMap.create();

    static {
        RARITY_ADAPTER.put(TreasureRarities.COMMON.get(), Rarity.COMMON);
        RARITY_ADAPTER.put(TreasureRarities.UNCOMMON.get(), Rarity.UNCOMMON);
        RARITY_ADAPTER.put(TreasureRarities.SCARCE.get(), Rarity.SCARCE);
        RARITY_ADAPTER.put(TreasureRarities.RARE.get(), Rarity.RARE);
        RARITY_ADAPTER.put(TreasureRarities.EPIC.get(), Rarity.EPIC);
        RARITY_ADAPTER.put(TreasureRarities.LEGENDARY.get(), Rarity.LEGENDARY);
        RARITY_ADAPTER.put(TreasureRarities.MYTHICAL.get(), Rarity.MYTHICAL);
        RARITY_ADAPTER.put(TreasureRarities.SKULL.get(), SpecialRarity.SKULL);
        RARITY_ADAPTER.put(RarityEntry.NONE, Rarity.NONE);
    }
    /// /////////////////

    public static IRarity get(IRarityEntry rarityEntry) {
        return RARITY_ADAPTER.get(rarityEntry);
    }

    public static IRarityEntry get(IRarity rarity) {
        return RARITY_ADAPTER.inverse().get(rarity);
    }
}
