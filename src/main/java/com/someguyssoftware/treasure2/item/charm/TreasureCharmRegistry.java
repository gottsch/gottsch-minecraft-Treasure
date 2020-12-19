import java.util.Map;
import java.util.Optional;

public class TreasureCharmRegistry {
    private static final Map<ResourceLocation, ICharm> REGISTRY = new HashMap<>();

    /**
     * 
     * @param modID
     * @param charm
     */
    public static void register(String modID, ICharm charm) {
        ResourceLocation key = new ResourceLocation(modID, charm.getName().toLowerCase());
        if (!REGISTRY.containsKey(key)) {
            REGISTRY.put(key, charm);
        }
    }

    /**
     * 
     * @param modID
     * @param name
     * @return
     */
    public static Optional<ICharm> get(String modID, String name) {
        ResourceLocation key = new ResourceLocation(modID, name.toLowerCase());
        if (REGISTRY.containsKey(key)) {
            return Optional.of(REGISTRY.get(key));
        }
        return Optional.empty();
    }
}