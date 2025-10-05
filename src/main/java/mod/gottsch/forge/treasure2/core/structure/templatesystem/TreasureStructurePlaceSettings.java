package mod.gottsch.forge.treasure2.core.structure.templatesystem;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;

/**
 * @author by Mark Gottschling on 8/29/2025
 */
public class TreasureStructurePlaceSettings extends StructurePlaceSettings {

    private final ResourceLocation dimension;

    public TreasureStructurePlaceSettings(ResourceLocation dimension) {
        this.dimension = dimension;
    }

    public ResourceLocation getDimension() {
        return this.dimension;
    }
}
