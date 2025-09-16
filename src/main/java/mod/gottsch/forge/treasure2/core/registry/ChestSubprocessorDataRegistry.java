package mod.gottsch.forge.treasure2.core.registry;

import com.google.common.collect.Maps;
import mod.gottsch.forge.treasure2.core.rarity.IRarityEntry;
import mod.gottsch.forge.treasure2.core.structure.templatesystem.data.ChestSubprocessorData;
import mod.gottsch.forge.treasure2.core.world.feature.IFeatureType;

import java.util.Map;
import java.util.Optional;

/**
 * @author by Mark Gottschling on 8/28/2025
 */
public enum ChestSubprocessorDataRegistry {
    INSTANCE;

    private static final Map<IFeatureType, Map<IRarityEntry, ChestSubprocessorData>> MAP = Maps.newHashMap();

    public static void clear() {
        MAP.clear();
    }

    public static void register(IFeatureType type, IRarityEntry rarity,  ChestSubprocessorData data) {
        MAP.computeIfAbsent(type, m ->Maps.newHashMap())
//                .computeIfAbsent(rarity, k -> new ArrayList<>())
                .put(rarity, data);
//                .add(data);
    }

//    public static void register(IFeatureType type, IRarityEntry rarity, List<ChestSubprocessorData> data) {
//        MAP.computeIfAbsent(type, k -> Maps.newHashMap())
//                .computeIfAbsent(rarity, k -> new ArrayList<>())
//                .addAll(data);
//    }

    public static Optional<ChestSubprocessorData> getAssociation(IFeatureType key, IRarityEntry rarity) {
//        Map<IRarityEntry, List<ChestSubprocessorData>> innerMap = MAP.get(key);
//        if (innerMap == null) {
//            return Collections.emptyList(); // Or return null, depending on your desired behavior
//        }
//        return innerMap.getOrDefault(rarity, Collections.emptyList());
        return Optional.ofNullable(MAP.get(key))
                .map(innerMap -> innerMap.get(rarity));
    }

}
