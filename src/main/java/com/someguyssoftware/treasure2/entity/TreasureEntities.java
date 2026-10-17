/**
 * 
 */
package com.someguyssoftware.treasure2.entity;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.entity.monster.BoundSoulEntity;
import com.someguyssoftware.treasure2.entity.monster.CardboardBoxMimicEntity;
import com.someguyssoftware.treasure2.entity.monster.CauldronChestMimicEntity;
import com.someguyssoftware.treasure2.entity.monster.CrateChestMimicEntity;
import com.someguyssoftware.treasure2.entity.monster.MilkCrateMimicEntity;
import com.someguyssoftware.treasure2.entity.monster.VanillaChestMimicEntity;
import com.someguyssoftware.treasure2.entity.monster.MimicEntity;
import com.someguyssoftware.treasure2.entity.monster.MoldyCrateChestMimicEntity;
import com.someguyssoftware.treasure2.entity.monster.PirateMimicEntity;
import com.someguyssoftware.treasure2.entity.monster.VikingChestMimicEntity;
import com.someguyssoftware.treasure2.entity.monster.WoodMimicEntity;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;

/**
 * @author Mark Gottschling on Aug 18, 2018
 *
 */
public class TreasureEntities {

	/**
	 * 
	 * @author Mark Gottschling on Aug 18, 2018
	 *
	 */
	@Mod.EventBusSubscriber(modid = Treasure.MODID)
	public static class RegistrationHandler {

		/**
		 * 
		 * @param event
		 */
		@SubscribeEvent
		public static void registerEntities(final RegistryEvent.Register<EntityEntry> event) {
			// register the entities
			EntityEntry entry = EntityEntryBuilder.create().entity(WoodMimicEntity.class)
					.id(new ResourceLocation("treasure2:wood_mimic"), WoodMimicEntity.MOB_ID).name("wood_mimic")
					.egg(0xFFFFFF, 0xAAAAAA).tracker(64, 1, true).build();
			event.getRegistry().register(entry);

			EntityEntry pirateMimic = EntityEntryBuilder.create().entity(PirateMimicEntity.class)
					.id(new ResourceLocation("treasure2:pirate_mimic"), PirateMimicEntity.MOB_ID).name("pirate_mimic")
					.egg(0xFFFFFF, 0x221F6D).tracker(64, 1, true).build();
			event.getRegistry().register(pirateMimic);

			EntityEntry boundSoul = EntityEntryBuilder.create().entity(BoundSoulEntity.class)
					.id(new ResourceLocation("treasure2:bound_soul"), BoundSoulEntity.MOB_ID).name("bound_soul")
					.egg(0xCCC333, 0x221F6D).tracker(64, 2, true).build();
			event.getRegistry().register(boundSoul);

			// the newer mimics. names, sizes, attributes and egg colours match 1.20.1.
			event.getRegistry().registerAll(
					mimic(VikingChestMimicEntity.class, TreasureConfig.VIKING_CHEST_MIMIC_ID, VikingChestMimicEntity.MOB_ID, 0x642e1e, 0x753c27),
					mimic(CauldronChestMimicEntity.class, TreasureConfig.CAULDRON_CHEST_MIMIC_ID, CauldronChestMimicEntity.MOB_ID, 0x6e5c30, 0x4a4a4a),
					mimic(CrateChestMimicEntity.class, TreasureConfig.CRATE_CHEST_MIMIC_ID, CrateChestMimicEntity.MOB_ID, 0x6e5c60, 0x434343),
					mimic(MoldyCrateChestMimicEntity.class, TreasureConfig.MOLDY_CRATE_CHEST_MIMIC_ID, MoldyCrateChestMimicEntity.MOB_ID, 0x635360, 0x464646),
					mimic(CardboardBoxMimicEntity.class, TreasureConfig.CARDBOARD_BOX_MIMIC_ID, CardboardBoxMimicEntity.MOB_ID, 0x6f5e60, 0x404040),
					mimic(MilkCrateMimicEntity.class, TreasureConfig.MILK_CRATE_MIMIC_ID, MilkCrateMimicEntity.MOB_ID, 0x965738, 0x773e28),
					mimic(VanillaChestMimicEntity.class, TreasureConfig.VANILLA_CHEST_MIMIC_ID, VanillaChestMimicEntity.MOB_ID, 0x8f691d, 0xab792d));

			// add data fixers
			DataFixer dataFixer = new DataFixer(1343);
			dataFixer = new net.minecraftforge.common.util.CompoundDataFixer(dataFixer);
			WoodMimicEntity.registerFixesWoodenMimic(dataFixer);
			PirateMimicEntity.registerFixesPirateMimic(dataFixer);
			BoundSoulEntity.registerFixesBoundSoul(dataFixer);
			VikingChestMimicEntity.registerFixes(dataFixer);
			CauldronChestMimicEntity.registerFixes(dataFixer);
			CrateChestMimicEntity.registerFixes(dataFixer);
			MoldyCrateChestMimicEntity.registerFixes(dataFixer);
			CardboardBoxMimicEntity.registerFixes(dataFixer);
			MilkCrateMimicEntity.registerFixes(dataFixer);
			VanillaChestMimicEntity.registerFixes(dataFixer);
		}

		/**
		 * A mimic entity entry with a spawn egg, tracked like the wood and pirate mimics.
		 */
		private static EntityEntry mimic(Class<? extends MimicEntity> mimic, String name, int id, int eggPrimary, int eggSecondary) {
			return EntityEntryBuilder.create().entity(mimic)
					.id(new ResourceLocation(Treasure.MODID, name), id).name(name)
					.egg(eggPrimary, eggSecondary).tracker(64, 1, true).build();
		}
	}
}
