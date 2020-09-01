/**
 * 
 */
package com.someguyssoftware.treasure2.particle;

import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author Mark Gottschling on Feb 15, 2020 Inserts custom texture into the
 *         blocks+items texture sheet.
 */
public class MistTextureStitcher {
	@SubscribeEvent
	public void stitcherEventPre(TextureStitchEvent.Pre event) {

		ResourceLocation mistResourceLocation = new ResourceLocation(Treasure.MODID, "particle/mist_particle");
		ResourceLocation mistResourceLocation2 = new ResourceLocation(Treasure.MODID, "particle/mist_particle2");
		ResourceLocation mistResourceLocation3 = new ResourceLocation(Treasure.MODID, "particle/mist_particle3");
		ResourceLocation mistResourceLocation4 = new ResourceLocation(Treasure.MODID, "particle/mist_particle4");

		ResourceLocation poisonMistResourceLocation = new ResourceLocation(Treasure.MODID,
				"particle/poison_mist_particle");
		ResourceLocation poisonMistResourceLocation2 = new ResourceLocation(Treasure.MODID,
				"particle/poison_mist_particle2");
		ResourceLocation poisonMistResourceLocation3 = new ResourceLocation(Treasure.MODID,
				"particle/poison_mist_particle3");
		ResourceLocation poisonMistResourceLocation4 = new ResourceLocation(Treasure.MODID,
				"particle/poison_mist_particle4");

		ResourceLocation witherMistResourceLocation = new ResourceLocation(Treasure.MODID,
				"particle/wither_mist_particle");
		ResourceLocation witherMistResourceLocation2 = new ResourceLocation(Treasure.MODID,
				"particle/wither_mist_particle2");
		ResourceLocation witherMistResourceLocation3 = new ResourceLocation(Treasure.MODID,
				"particle/wither_mist_particle3");
		ResourceLocation witherMistResourceLocation4 = new ResourceLocation(Treasure.MODID,
				"particle/wither_mist_particle4");

//		ResourceLocation willWispResourceLocation = new ResourceLocation(Treasure.MODID,
//				"particle/will_o_the_wisp_particle");

		event.getMap().registerSprite(mistResourceLocation);
		event.getMap().registerSprite(mistResourceLocation2);
		event.getMap().registerSprite(mistResourceLocation3);
		event.getMap().registerSprite(mistResourceLocation4);

		event.getMap().registerSprite(poisonMistResourceLocation);
		event.getMap().registerSprite(poisonMistResourceLocation2);
		event.getMap().registerSprite(poisonMistResourceLocation3);
		event.getMap().registerSprite(poisonMistResourceLocation4);

		event.getMap().registerSprite(witherMistResourceLocation);
		event.getMap().registerSprite(witherMistResourceLocation2);
		event.getMap().registerSprite(witherMistResourceLocation3);
		event.getMap().registerSprite(witherMistResourceLocation4);

//		event.getMap().registerSprite(willWispResourceLocation);
	}
}
