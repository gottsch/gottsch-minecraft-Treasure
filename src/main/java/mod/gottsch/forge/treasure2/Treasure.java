package mod.gottsch.forge.treasure2;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.electronwill.nightconfig.core.CommentedConfig;

import mod.gottsch.forge.treasure2.core.block.TreasureBlocks;
import mod.gottsch.forge.treasure2.core.block.entity.TreasureBlockEntities;
import mod.gottsch.forge.treasure2.core.config.Config;
import mod.gottsch.forge.treasure2.core.entity.TreasureEntities;
import mod.gottsch.forge.treasure2.core.inventory.TreasureContainers;
import mod.gottsch.forge.treasure2.core.item.TreasureItems;
import mod.gottsch.forge.treasure2.core.particle.TreasureParticles;
import mod.gottsch.forge.treasure2.core.setup.ClientSetup;
import mod.gottsch.forge.treasure2.core.setup.CommonSetup;
import mod.gottsch.forge.treasure2.core.setup.Registration;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.IConfigSpec;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

/**
 * 
 * @author Mark Gottschling on Aug 11, 2020
 *
 */
@Mod(value = Treasure.MODID)
public class Treasure {
	// logger
	public static Logger LOGGER = LogManager.getLogger(Treasure.MODID);

	// constants
	public static final String MODID = "treasure2";

	private static final String CHESTS_CONFIG_VERSION = "1.18.2-v1";
	
	public static Treasure instance;

	/**
	 * 
	 */
	public Treasure() {
		Treasure.instance = this;
		Config.register();
		// create the default config
		createServerConfig(Config.CHESTS_CONFIG_SPEC, "chests", CHESTS_CONFIG_VERSION);
		
		// register the deferred registries
		TreasureBlocks.register();
		TreasureItems.register();
		TreasureBlockEntities.register();
		TreasureContainers.register();
		TreasureParticles.register();
		TreasureEntities.register();
		
		// register the setup method for mod loading
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		// register 'ModSetup::init' to be called at mod setup time (server and client)
		modEventBus.addListener(CommonSetup::init);
		modEventBus.addListener(this::config);

        // register 'ClientSetup::init' to be called at mod setup time (client only)
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> modEventBus.addListener(ClientSetup::init)); 	

	}
	
	/*
	 * 
	 */
	private static void createServerConfig(ForgeConfigSpec spec, String suffix, String version) {
		String fileName = "treasure2-" + suffix + "-" + version + ".toml";
		ModLoadingContext.get().registerConfig(Type.SERVER, spec, fileName);
		File defaults = new File(FMLPaths.GAMEDIR.get() + "/defaultconfigs/" + fileName);

		if (!defaults.exists()) {
			try {
				FileUtils.copyInputStreamToFile(
						Objects.requireNonNull(Treasure.class.getClassLoader().getResourceAsStream(fileName)),
						defaults);
			} catch (IOException e) {
				LOGGER.error("Error creating default config for " + fileName);
			}
		}
	}
	
	/**
	 * 
	 * @param event
	 */
//    private void interModComms(InterModEnqueueEvent event) {
//        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.CHARM.getMessageBuilder().build());
//        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.NECKLACE.getMessageBuilder().build());
//        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.RING.getMessageBuilder().build());
//        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.BRACELET.getMessageBuilder().build());
//        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.BELT.getMessageBuilder().build());
//    }

	/**
	 * On a config event.
	 * @param event
	 */
	private void config(final ModConfigEvent event) {
		if (event.getConfig().getModId().equals(MODID)) {
			if (event.getConfig().getType() == Type.SERVER) {
				IConfigSpec<?> spec = event.getConfig().getSpec();
				// get the toml config data
				CommentedConfig commentedConfig = event.getConfig().getConfigData();

				if (spec == Config.CHESTS_CONFIG_SPEC) {
					// transform/copy the toml into the config
					Config.transform(commentedConfig);
//					EchelonManager.build();					
				} 
			}
		}
	}
}
