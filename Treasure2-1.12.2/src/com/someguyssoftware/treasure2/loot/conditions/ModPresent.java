package com.someguyssoftware.treasure2.loot.conditions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.someguyssoftware.treasure2.loot.TreasureLootContext;

import java.util.Random;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;

/**
 * A condition that checks for the existance of a mod.
 * 
 * @author Mark Gottschling on Nov 7, 2018
 *
 */
public class ModPresent implements TreasureLootCondition {
	private final String modID;

	public ModPresent(String modID) {
		this.modID = modID;
	}

	/**
	 * 
	 */
	public boolean testCondition(Random rand, TreasureLootContext context) {
		if (Loader.isModLoaded(this.modID))
			return true;
		return false;
	}

	/**
	 * 
	 * @author Mark Gottschling on Nov 7, 2018
	 *
	 */
	public static class Serializer extends TreasureLootCondition.Serializer<ModPresent> {
		public Serializer() {
			super(new ResourceLocation("treasure2:mod_present"), ModPresent.class);
		}

		public void serialize(JsonObject json, ModPresent value, JsonSerializationContext context) {
			json.addProperty("modid", String.valueOf(value.modID));
		}

		public ModPresent deserialize(JsonObject json, JsonDeserializationContext context) {
			return new ModPresent(JsonUtils.getString(json, "modid"));
		}
	}

}