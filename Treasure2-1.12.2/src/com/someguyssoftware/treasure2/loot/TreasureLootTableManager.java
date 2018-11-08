/**
 * 
 */
package com.someguyssoftware.treasure2.loot;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Deque;
import java.util.HashSet;

import javax.annotation.Nullable;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Queues;
import com.google.common.collect.Sets;
import com.google.common.io.Files;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.loot.conditions.TreasureLootCondition;
import com.someguyssoftware.treasure2.loot.conditions.TreasureLootConditionManager;
import com.someguyssoftware.treasure2.loot.functions.TreasureLootFunction;
import com.someguyssoftware.treasure2.loot.functions.TreasureLootFunctionManager;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LootingLevelEvent;

/**
 * A wrapper for vanilla LootTableManager that allows loading of resources from
 * a custom folder.
 * 
 * @author Mark Gottschling on Nov 6, 2018
 *
 */
public class TreasureLootTableManager {

	private static final Gson GSON_INSTANCE = (new GsonBuilder()).registerTypeAdapter(RandomValueRange.class, new RandomValueRange.Serializer())
			.registerTypeAdapter(TreasureLootPool.class, new TreasureLootPool.Serializer()).registerTypeAdapter(TreasureLootTable.class, new TreasureLootTable.Serializer())
			.registerTypeHierarchyAdapter(TreasureLootEntry.class, new TreasureLootEntry.Serializer())
			.registerTypeHierarchyAdapter(TreasureLootFunction.class, new TreasureLootFunctionManager.Serializer())
			.registerTypeHierarchyAdapter(TreasureLootCondition.class, new TreasureLootConditionManager.Serializer())
			.registerTypeHierarchyAdapter(TreasureLootContext.EntityTarget.class, new TreasureLootContext.EntityTarget.Serializer()).create();
	private final LoadingCache<ResourceLocation, TreasureLootTable> myRegisteredLootTables = CacheBuilder.newBuilder()
			.<ResourceLocation, TreasureLootTable>build(new TreasureLootTableManager.Loader());
	private final File baseFolder;

	private static ThreadLocal<Deque<TreasureLootTableContext>> lootContext = new ThreadLocal<Deque<TreasureLootTableContext>>();

	/**
	 * 
	 * @return
	 */
	private static TreasureLootTableContext getLootTableContext() {
		TreasureLootTableContext ctx = lootContext.get().peek();

		if (ctx == null)
			throw new JsonParseException("Invalid call stack, could not grab json context!"); // Should I throw this? Do we care about custom deserializers outside the
																								// manager?

		return ctx;
	}

	/**
	 * @param folder
	 */
	public TreasureLootTableManager(File folder) {
		Treasure.logger.debug("Constructing TreasureLootTableManager");
		// set this class' versino of baseFolder and reload this class' tables
		this.baseFolder = folder;
		// this.reloadLootTables();
	}

	// @Override
	public TreasureLootTable getLootTableFromLocation(ResourceLocation location) {
//		Treasure.logger.info("Getting loot table from location -> {}", location);
//		Treasure.logger.info("registeredLootTables -> {}", this.myRegisteredLootTables);
		return this.myRegisteredLootTables.getUnchecked(location);
	}

	// @Override
	public void reloadLootTables() {
		Treasure.logger.debug("Reloading Loot Tables.");
		this.myRegisteredLootTables.invalidateAll();

		for (ResourceLocation resourcelocation : LootTableList.getAll()) {
			this.getLootTableFromLocation(resourcelocation);
		}
	}

	class Loader extends CacheLoader<ResourceLocation, TreasureLootTable> {
		private Loader() {
		}

		public TreasureLootTable load(ResourceLocation location) throws Exception {
			if (location.getResourcePath().contains(".")) {
				Treasure.logger.debug("Invalid loot table name '{}' (can't contain periods)", (Object) location);
				return TreasureLootTable.EMPTY_LOOT_TABLE;
			} else {
				Treasure.logger.debug("loading loot table from location -> {}", location);
				TreasureLootTable loottable = this.loadLootTable(location);

				if (loottable == null) {
					Treasure.logger.debug("custom location null, loading loot table from builtin -> {}", location);
					loottable = this.loadBuiltinLootTable(location);
				}

				if (loottable == null) {
					loottable = TreasureLootTable.EMPTY_LOOT_TABLE;
					Treasure.logger.warn("Couldn't find resource table {}", (Object) location);
				}

				return loottable;
			}
		}

		@Nullable
		private TreasureLootTable loadLootTable(ResourceLocation resource) {
//			Treasure.logger.debug("baseFolder -> {}", TreasureLootTableManager.this.baseFolder);

			if (TreasureLootTableManager.this.baseFolder == null) {
				return null;
			} else {
				File file1 = new File(new File(TreasureLootTableManager.this.baseFolder, resource.getResourceDomain()), resource.getResourcePath() + ".json");
//				Treasure.logger.debug("file -> {}", file1.getAbsolutePath());
				if (file1.exists()) {
//					Treasure.logger.debug("file exists!");
					if (file1.isFile()) {
//						Treasure.logger.debug("file is a file!");
						String s;

						try {
							s = Files.toString(file1, StandardCharsets.UTF_8);
						} catch (IOException ioexception) {
							Treasure.logger.warn("Couldn't load loot table {} from {}", resource, file1, ioexception);
							return TreasureLootTable.EMPTY_LOOT_TABLE;
						}

						try {
							// return
							// net.minecraftforge.common.ForgeHooks.loadLootTable(TreasureLootTableManager.GSON_INSTANCE,
							// resource, s, true, TreasureLootTableManager.this);
							// return null;
							// TODO change
							return TreasureLootTableManager.hookLoadLootTable(TreasureLootTableManager.GSON_INSTANCE, resource, s, true, TreasureLootTableManager.this);

						} catch (IllegalArgumentException | JsonParseException jsonparseexception) {
							Treasure.logger.error("Couldn't load loot table {} from {}", resource, file1, jsonparseexception);
							return TreasureLootTable.EMPTY_LOOT_TABLE;
						}
					} else {
						Treasure.logger.warn("Expected to find loot table {} at {} but it was a folder.", resource, file1);
						return TreasureLootTable.EMPTY_LOOT_TABLE;
					}
				} else {
					return null;
				}
			}
		}

		@Nullable
		private TreasureLootTable loadBuiltinLootTable(ResourceLocation resource) {
			URL url = TreasureLootTableManager.class.getResource("/assets/" + resource.getResourceDomain() + "/loot_tables/" + resource.getResourcePath() + ".json");

			if (url != null) {
				String s;

				try {
					s = Resources.toString(url, StandardCharsets.UTF_8);
				} catch (IOException ioexception) {
					Treasure.logger.warn("Couldn't load loot table {} from {}", resource, url, ioexception);
					return TreasureLootTable.EMPTY_LOOT_TABLE;
				}

				try {
					// return
					// net.minecraftforge.common.ForgeHooks.loadLootTable(TreasureLootTableManager.GSON_INSTANCE,
					// resource, s, false, TreasureLootTableManager.this);
					// return null;
					return TreasureLootTableManager.hookLoadLootTable(TreasureLootTableManager.GSON_INSTANCE, resource, s, false, TreasureLootTableManager.this);

				} catch (JsonParseException jsonparseexception) {
					Treasure.logger.error("Couldn't load loot table {} from {}", resource, url, jsonparseexception);
					return TreasureLootTable.EMPTY_LOOT_TABLE;
				}
			} else {
				return null;
			}
		}
	}

	@Nullable
	public static TreasureLootTable hookLoadLootTable(Gson gson, ResourceLocation name, String data, boolean custom, TreasureLootTableManager lootTableManager) {
		Deque<TreasureLootTableContext> que = lootContext.get();
		if (que == null) {
			que = Queues.newArrayDeque();
			lootContext.set(que);
		}

		TreasureLootTable ret = null;
		try {
			que.push(new TreasureLootTableContext(name, custom));
			ret = gson.fromJson(data, TreasureLootTable.class);
			que.pop();
		} catch (JsonParseException e) {
			que.pop();
			throw e;
		}

		// if (!custom)
		// ret = ForgeEventFactory.loadLootTable(name, ret, lootTableManager);

		if (ret != null)
			ret.freeze();

		return ret;
	}

	/**
	 * 
	 * @author Mark Gottschling on Nov 7, 2018
	 *
	 */
	private static class TreasureLootTableContext {
		public final ResourceLocation name;
		private final boolean vanilla;
		public final boolean custom;
		public int poolCount = 0;
		public int entryCount = 0;
		private HashSet<String> entryNames = Sets.newHashSet();

		private TreasureLootTableContext(ResourceLocation name, boolean custom) {
			this.name = name;
			this.custom = custom;
			this.vanilla = "minecraft".equals(this.name.getResourceDomain());
		}

		private void resetPoolCtx() {
			this.entryCount = 0;
			this.entryNames.clear();
		}

		public String validateEntryName(@Nullable String name) {
			if (name != null && !this.entryNames.contains(name)) {
				this.entryNames.add(name);
				return name;
			}

			if (!this.vanilla)
				throw new JsonParseException(
						"Loot Table \"" + this.name.toString() + "\" Duplicate entry name \"" + name + "\" for pool #" + (this.poolCount - 1) + " entry #" + (this.entryCount - 1));

			int x = 0;
			while (this.entryNames.contains(name + "#" + x))
				x++;

			name = name + "#" + x;
			this.entryNames.add(name);

			return name;
		}
	}

	/**
	 * 
	 * @param json
	 * @param type
	 * @return
	 */
	public static String readLootEntryName(JsonObject json, String type) {
		TreasureLootTableContext ctx = TreasureLootTableManager.getLootTableContext();
		ctx.entryCount++;

		if (json.has("entryName"))
			return ctx.validateEntryName(JsonUtils.getString(json, "entryName"));

		if (ctx.custom)
			return "custom#" + json.hashCode(); // We don't care about custom ones modders shouldn't be editing them!

		String name = null;
		if ("item".equals(type))
			name = JsonUtils.getString(json, "name");
		else if ("loot_table".equals(type))
			name = JsonUtils.getString(json, "name");
		else if ("empty".equals(type))
			name = "empty";

		return ctx.validateEntryName(name);
	}

	// NOTE useless method
	public static TreasureLootEntry deserializeJsonLootEntry(String type, JsonObject json, int weight, int quality, TreasureLootCondition[] conditions) {
		return null;
	}

	public static String getLootEntryType(TreasureLootEntry entry) {
		return null;
	} // Companion to above function

	/**
	 * 
	 * @param target
	 * @param killer
	 * @param cause
	 * @return
	 */
	public static int getLootingLevel(Entity target, @Nullable Entity killer, DamageSource cause) {
		int looting = 0;
		if (killer instanceof EntityLivingBase) {
			looting = EnchantmentHelper.getLootingModifier((EntityLivingBase) killer);
		}
		if (target instanceof EntityLivingBase) {
			looting = getLootingLevel((EntityLivingBase) target, cause, looting);
		}
		return looting;
	}

	/**
	 * 
	 * @param target
	 * @param cause
	 * @param level
	 * @return
	 */
	public static int getLootingLevel(EntityLivingBase target, DamageSource cause, int level) {
		LootingLevelEvent event = new LootingLevelEvent(target, cause, level);
		MinecraftForge.EVENT_BUS.post(event);
		return event.getLootingLevel();
	}

	public static String readPoolName(JsonObject json) {
		TreasureLootTableContext ctx = getLootTableContext();
		ctx.resetPoolCtx();

		if (json.has("name"))
			return JsonUtils.getString(json, "name");

		if (ctx.custom)
			return "custom#" + json.hashCode(); // We don't care about custom ones modders shouldn't be editing them!

		ctx.poolCount++;

		if (!ctx.vanilla)
			throw new JsonParseException("Loot Table \"" + ctx.name.toString() + "\" Missing `name` entry for pool #" + (ctx.poolCount - 1));

		return ctx.poolCount == 1 ? "main" : "pool" + (ctx.poolCount - 1);
	}
}
