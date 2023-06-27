
package mod.gottsch.forge.treasure2.datagen;

import mod.gottsch.forge.treasure2.Treasure;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * 
 * @author Mark Gottschling on Nov 6, 2022
 *
 */
@Mod.EventBusSubscriber(modid = Treasure.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        if (event.includeServer()) {
            generator.addProvider(true, new Recipes(generator));
//            generator.addProvider(new TutLootTables(generator));
        	TreasureBlockTagsProvider blockTags = new TreasureBlockTagsProvider(generator, event.getExistingFileHelper());
            generator.addProvider(true, blockTags);
            generator.addProvider(true, new TreasureItemTagsProvider(generator, blockTags, event.getExistingFileHelper()));
            generator.addProvider(true, new TreasureBiomeTagsProvider(generator, event.getExistingFileHelper()));

        }
        if (event.includeClient()) {
        	 generator.addProvider(true, new BlockStates(generator, event.getExistingFileHelper()));
            generator.addProvider(true, new ItemModelsProvider(generator, event.getExistingFileHelper()));
            generator.addProvider(true, new LanguageGen(generator, "en_us"));
        }
    }
}