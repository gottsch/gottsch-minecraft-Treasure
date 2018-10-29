/**
 * 
 */
package com.someguyssoftware.treasure2.loot;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.annotation.Nullable;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.LootTableManager;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;

/**
 * @author Mark Gottschling on Oct 28, 2018
 *
 */
public class TreasureLootTableManager extends LootTableManager {
	
    private static final Gson GSON_INSTANCE = (
    		new GsonBuilder())
    		.registerTypeAdapter(RandomValueRange.class, new RandomValueRange.Serializer())
    		.registerTypeAdapter(LootPool.class, new LootPool.Serializer())
    		.registerTypeAdapter(LootTable.class, new LootTable.Serializer())
    		.registerTypeHierarchyAdapter(LootEntry.class, new LootEntry.Serializer()) // TODO change this guy
    		.registerTypeHierarchyAdapter(LootFunction.class, new LootFunctionManager.Serializer())
    		.registerTypeHierarchyAdapter(LootCondition.class, new LootConditionManager.Serializer())
    		.registerTypeHierarchyAdapter(LootContext.EntityTarget.class, new LootContext.EntityTarget.Serializer())
    		.create();

    private final LoadingCache<ResourceLocation, LootTable> registeredLootTables = CacheBuilder.newBuilder().<ResourceLocation, LootTable>build(new TreasureLootTableManager.Loader());
    private final File baseFolder;
    
    /*
     * Reference to the build-in vanilla LootTableManager
     */
    private LootTableManager lootTableManager;
    
	/**
	 * 
	 * @param folder
	 */
	public TreasureLootTableManager(LootTableManager lootTableManager, File folder) {
		super(folder);
		this.lootTableManager = lootTableManager;
        this.baseFolder = folder;
        this.reloadLootTables();
	}

	/**
	 * 
	 */
    public LootTable getLootTableFromLocation(ResourceLocation ressources) {
        return this.registeredLootTables.getUnchecked(ressources);
    }

    /**
     * 
     */
    public void reloadLootTables() {
        this.registeredLootTables.invalidateAll();

        for (ResourceLocation resourcelocation : LootTableList.getAll()) {
            this.getLootTableFromLocation(resourcelocation);
        }
    }
    
	/**
	 * 
	 * @author Mark Gottschling on Oct 28, 2018
	 *
	 */
    class Loader extends CacheLoader<ResourceLocation, LootTable> {
        private Loader() {  }

        /**
         * 
         */
        public LootTable load(ResourceLocation resourceLocation) throws Exception {
            if (resourceLocation.getResourcePath().contains(".")) {
                Treasure.logger.debug("Invalid loot table name '{}' (can't contain periods)", (Object)resourceLocation);
                return LootTable.EMPTY_LOOT_TABLE;
            }
            else {
                LootTable lootTable = this.loadLootTable(resourceLocation);

                /*
                 * couldn't load the resource location
                 */
                if (lootTable == null) {
                    lootTable = TreasureLootTableManager.this.lootTableManager.getLootTableFromLocation(resourceLocation);
                }

                if (lootTable == null) {
                    lootTable = LootTable.EMPTY_LOOT_TABLE;
                    Treasure.logger.warn("Couldn't find resource table {}", (Object)resourceLocation);
                }
                return lootTable;
            }
        }

        /**
         * 
         * @param resource
         * @return
         */
        @Nullable
        private LootTable loadLootTable(ResourceLocation resource) {
            if (TreasureLootTableManager.this.baseFolder == null) {
                return null;
            }
            else {
                File file1 = new File(new File(TreasureLootTableManager.this.baseFolder, resource.getResourceDomain()), resource.getResourcePath() + ".json");

                if (file1.exists()) {
                    if (file1.isFile()) {
                        String s;

                        try {
                            s = Files.toString(file1, StandardCharsets.UTF_8);
                        }
                        catch (IOException e){
                            Treasure.logger.warn("Couldn't load loot table {} from {}", resource, file1, e);
                            return LootTable.EMPTY_LOOT_TABLE;
                        }

                        try {
                            return net.minecraftforge.common.ForgeHooks.loadLootTable(TreasureLootTableManager.GSON_INSTANCE, resource, s, true, TreasureLootTableManager.this);
                        }
                        catch (IllegalArgumentException | JsonParseException e) {
                            Treasure.logger.error("Couldn't load loot table {} from {}", resource, file1, e);
                            return LootTable.EMPTY_LOOT_TABLE;
                        }
                    }
                    else {
                        Treasure.logger.warn("Expected to find loot table {} at {} but it was a folder.", resource, file1);
                        return LootTable.EMPTY_LOOT_TABLE;
                    }
                }
                else
                {
                    return null;
                }
            }
        }
    }
}
