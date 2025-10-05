package mod.gottsch.forge.treasure2.core.mobset;

import com.google.common.collect.Maps;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.Optional;

/**
 * @author by Mark Gottschling on 9/18/2025
 */
// TODO rename this to MobSetRegistry later when MobSetRegistry is removed
    // TODO also move it to the registry package
public enum MobSetDataRegistry {
    INSTANCE;

    private static Map<ResourceLocation, MobSetData> REGISTRY = Maps.newHashMap();

    public static Optional<MobSetData> register(MobSetData data) {
        return Optional.ofNullable(REGISTRY.put(data.getId(), data));
    }

    public static Optional<MobSetData> get(ResourceLocation id) {
        return Optional.ofNullable(REGISTRY.get(id));
    }

    public static void clear() {
        REGISTRY.clear();
    }
}
