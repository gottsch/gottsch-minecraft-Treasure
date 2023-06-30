package mod.gottsch.forge.treasure2;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.electronwill.nightconfig.core.CommentedConfig;

import mod.gottsch.forge.treasure2.core.block.TreasureBlocks;
import mod.gottsch.forge.treasure2.core.block.entity.TreasureBlockEntities;
import mod.gottsch.forge.treasure2.core.cache.FeatureCaches;
import mod.gottsch.forge.treasure2.core.config.Config;
import mod.gottsch.forge.treasure2.core.config.StructureConfiguration;
import mod.gottsch.forge.treasure2.core.entity.TreasureEntities;
import mod.gottsch.forge.treasure2.core.inventory.TreasureContainers;
import mod.gottsch.forge.treasure2.core.item.TreasureItems;
import mod.gottsch.forge.treasure2.core.loot.modifier.TreasureLootModifiers;
import mod.gottsch.forge.treasure2.core.particle.TreasureParticles;
import mod.gottsch.forge.treasure2.core.registry.DimensionalGeneratedCache;
import mod.gottsch.forge.treasure2.core.registry.RarityLevelWeightedChestGeneratorRegistry;
import mod.gottsch.forge.treasure2.core.setup.ClientSetup;
import mod.gottsch.forge.treasure2.core.setup.CommonSetup;
import mod.gottsch.forge.treasure2.core.sound.TreasureSounds;
import mod.gottsch.forge.treasure2.core.util.ModUtil;
import mod.gottsch.forge.treasure2.core.world.feature.TreasureConfiguredFeatures;
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
	private static final String STRUCTURES_CONFIG_VERSION = "1.18.2-v1";
	
	public static Treasure instance;

	/**
	 * 
	 */
	public Treasure() {
		Treasure.instance = this;
		Config.register();
		// create the default configs
		createServerConfig(Config.CHESTS_CONFIG_SPEC, "chests", CHESTS_CONFIG_VERSION);
		createServerConfig(Config.STRUCTURE_CONFIG_SPEC, "structures", STRUCTURES_CONFIG_VERSION);
		
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		
		// register the deferred registries
		TreasureBlocks.register(modEventBus);
		TreasureItems.register(modEventBus);
		TreasureBlockEntities.register(modEventBus);
		TreasureContainers.register(modEventBus);
		TreasureParticles.register(modEventBus);
		TreasureEntities.register(modEventBus);
		TreasureConfiguredFeatures.register(modEventBus);
		TreasureSounds.register(modEventBus);
		TreasureLootModifiers.register(modEventBus);
		
		// register the setup method for mod loading
		
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
	
					// init generated chest registry
					LOGGER.debug("reading in chests config...");
					DimensionalGeneratedCache.initialize();
					RarityLevelWeightedChestGeneratorRegistry.initialize();
				} 
				else if (spec == Config.STRUCTURE_CONFIG_SPEC) {
					Optional<StructureConfiguration> structConfig = Config.transformStructureConfiguration(commentedConfig);
//					if (structConfig.isPresent()) {
//						TreasureTemplateRegistry.registerAccesslists(structConfig.get().getWell());
//					}
				}
				else if (spec == Config.SERVER_SPEC) {
					/*
					 *  update registered block/item properties using reflection now that
					 *  the config is loaded.
					 *  ex. WealthItem.maxStackSize
					 */
					ModUtil.setItemMaxStackSize(TreasureItems.COPPER_COIN.get(), Config.SERVER.wealth.wealthMaxStackSize.get());
					ModUtil.setItemMaxStackSize(TreasureItems.SILVER_COIN.get(), Config.SERVER.wealth.wealthMaxStackSize.get());
					ModUtil.setItemMaxStackSize(TreasureItems.GOLD_COIN.get(), Config.SERVER.wealth.wealthMaxStackSize.get());
					ModUtil.setItemMaxStackSize(TreasureItems.TOPAZ.get(), Config.SERVER.wealth.wealthMaxStackSize.get());
					ModUtil.setItemMaxStackSize(TreasureItems.ONYX.get(), Config.SERVER.wealth.wealthMaxStackSize.get());
					ModUtil.setItemMaxStackSize(TreasureItems.SAPPHIRE.get(), Config.SERVER.wealth.wealthMaxStackSize.get());
					ModUtil.setItemMaxStackSize(TreasureItems.RUBY.get(), Config.SERVER.wealth.wealthMaxStackSize.get());
					ModUtil.setItemMaxStackSize(TreasureItems.WHITE_PEARL.get(), Config.SERVER.wealth.wealthMaxStackSize.get());
					ModUtil.setItemMaxStackSize(TreasureItems.BLACK_PEARL.get(), Config.SERVER.wealth.wealthMaxStackSize.get());
					
					ModUtil.setItemDurability(TreasureItems.WOOD_KEY.get(), Config.SERVER.keysAndLocks.woodKeyMaxUses.get());
					ModUtil.setItemDurability(TreasureItems.STONE_KEY.get(), Config.SERVER.keysAndLocks.stoneKeyMaxUses.get());
					ModUtil.setItemDurability(TreasureItems.LEAF_KEY.get(), Config.SERVER.keysAndLocks.leafKeyMaxUses.get());
					ModUtil.setItemDurability(TreasureItems.EMBER_KEY.get(), Config.SERVER.keysAndLocks.emberKeyMaxUses.get());
					ModUtil.setItemDurability(TreasureItems.LIGHTNING_KEY.get(), Config.SERVER.keysAndLocks.lightningKeyMaxUses.get());
					ModUtil.setItemDurability(TreasureItems.IRON_KEY.get(), Config.SERVER.keysAndLocks.ironKeyMaxUses.get());
					ModUtil.setItemDurability(TreasureItems.GOLD_KEY.get(), Config.SERVER.keysAndLocks.goldKeyMaxUses.get());
					ModUtil.setItemDurability(TreasureItems.METALLURGISTS_KEY.get(), Config.SERVER.keysAndLocks.metallurgistsKeyMaxUses.get());
					
					ModUtil.setItemDurability(TreasureItems.DIAMOND_KEY.get(), Config.SERVER.keysAndLocks.diamondKeyMaxUses.get());
					ModUtil.setItemDurability(TreasureItems.EMERALD_KEY.get(), Config.SERVER.keysAndLocks.emeraldKeyMaxUses.get());
					ModUtil.setItemDurability(TreasureItems.TOPAZ_KEY.get(), Config.SERVER.keysAndLocks.topazKeyMaxUses.get());
					ModUtil.setItemDurability(TreasureItems.ONYX_KEY.get(), Config.SERVER.keysAndLocks.onyxKeyMaxUses.get());
					ModUtil.setItemDurability(TreasureItems.RUBY_KEY.get(), Config.SERVER.keysAndLocks.rubyKeyMaxUses.get());
					ModUtil.setItemDurability(TreasureItems.SAPPHIRE_KEY.get(), Config.SERVER.keysAndLocks.sapphireKeyMaxUses.get());
					ModUtil.setItemDurability(TreasureItems.JEWELLED_KEY.get(), Config.SERVER.keysAndLocks.jewelledKeyMaxUses.get());
					
					ModUtil.setItemDurability(TreasureItems.SPIDER_KEY.get(), Config.SERVER.keysAndLocks.spiderKeyMaxUses.get());
					ModUtil.setItemDurability(TreasureItems.WITHER_KEY.get(), Config.SERVER.keysAndLocks.witherKeyMaxUses.get());
					
					ModUtil.setItemDurability(TreasureItems.SKELETON_KEY.get(), Config.SERVER.keysAndLocks.skeletonKeyMaxUses.get());
					
					ModUtil.setItemDurability(TreasureItems.PILFERERS_LOCK_PICK.get(), Config.SERVER.keysAndLocks.pilferersLockPickMaxUses.get());
					ModUtil.setItemDurability(TreasureItems.THIEFS_LOCK_PICK.get(), Config.SERVER.keysAndLocks.thiefsLockPickMaxUses.get());
					
					// TODO do for wealth items as well
//					WealthItem.super(properties.stacksTo(Config.SERVER.wealth.wealthMaxStackSize.get()));
					
					// TODO same with blocks and lighting
					
					FeatureCaches.initialize();
				}
			}
		}
	}
}
