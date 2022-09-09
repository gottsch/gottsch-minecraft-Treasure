
package com.someguyssoftware.treasure2.datagen;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.item.TreasureItems;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

/**
 * 
 * @author Mark Gottschling on Sep 9, 2022
 *
 */
public class ItemModelsProvider extends ItemModelProvider {

    public ItemModelsProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Treasure.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {

    	// topaz rings
        singleTexture(TreasureItems.ADORNMENT_ITEMS.get(modLoc("topaz_iron_ring")).getRegistryName().getPath(),        		
        		mcLoc("treasure2:item/adornment"), "layer0", modLoc("item/adornments/topaz_iron_ring"));
        singleTexture(TreasureItems.ADORNMENT_ITEMS.get(modLoc("topaz_copper_ring")).getRegistryName().getPath(),        		
        		mcLoc("treasure2:item/adornment"), "layer0", modLoc("item/adornments/topaz_copper_ring"));
        singleTexture(TreasureItems.ADORNMENT_ITEMS.get(modLoc("topaz_silver_ring")).getRegistryName().getPath(),        		
        		mcLoc("treasure2:item/adornment"), "layer0", modLoc("item/adornments/topaz_silver_ring"));
        singleTexture(TreasureItems.ADORNMENT_ITEMS.get(modLoc("topaz_gold_ring")).getRegistryName().getPath(),        		
        		mcLoc("treasure2:item/adornment"), "layer0", modLoc("item/adornments/topaz_gold_ring"));
        
        singleTexture(TreasureItems.ADORNMENT_ITEMS.get(modLoc("great_topaz_iron_ring")).getRegistryName().getPath(),        		
        		mcLoc("treasure2:item/adornment"), "layer0", modLoc("item/adornments/great_topaz_iron_ring"));
        singleTexture(TreasureItems.ADORNMENT_ITEMS.get(modLoc("great_topaz_copper_ring")).getRegistryName().getPath(),        		
        		mcLoc("treasure2:item/adornment"), "layer0", modLoc("item/adornments/great_topaz_copper_ring"));
        singleTexture(TreasureItems.ADORNMENT_ITEMS.get(modLoc("great_topaz_silver_ring")).getRegistryName().getPath(),        		
        		mcLoc("treasure2:item/adornment"), "layer0", modLoc("item/adornments/great_topaz_silver_ring"));
        singleTexture(TreasureItems.ADORNMENT_ITEMS.get(modLoc("great_topaz_gold_ring")).getRegistryName().getPath(),        		
        		mcLoc("treasure2:item/adornment"), "layer0", modLoc("item/adornments/great_topaz_gold_ring"));
        
        singleTexture(TreasureItems.ADORNMENT_ITEMS.get(modLoc("great_topaz_blood_ring")).getRegistryName().getPath(),        		
        		mcLoc("treasure2:item/adornment"), "layer0", modLoc("item/adornments/great_topaz_blood_ring"));
        singleTexture(TreasureItems.ADORNMENT_ITEMS.get(modLoc("great_topaz_black_ring")).getRegistryName().getPath(),        		
        		mcLoc("treasure2:item/adornment"), "layer0", modLoc("item/adornments/great_topaz_black_ring"));
        
        // topaz necklaces
        singleTexture(TreasureItems.ADORNMENT_ITEMS.get(modLoc("topaz_iron_necklace")).getRegistryName().getPath(),        		
        		mcLoc("treasure2:item/adornment"), "layer0", modLoc("item/adornments/topaz_iron_necklace"));
        singleTexture(TreasureItems.ADORNMENT_ITEMS.get(modLoc("topaz_copper_necklace")).getRegistryName().getPath(),        		
        		mcLoc("treasure2:item/adornment"), "layer0", modLoc("item/adornments/topaz_copper_necklace"));
        singleTexture(TreasureItems.ADORNMENT_ITEMS.get(modLoc("topaz_silver_necklace")).getRegistryName().getPath(),        		
        		mcLoc("treasure2:item/adornment"), "layer0", modLoc("item/adornments/topaz_silver_necklace"));
        singleTexture(TreasureItems.ADORNMENT_ITEMS.get(modLoc("topaz_gold_necklace")).getRegistryName().getPath(),        		
        		mcLoc("treasure2:item/adornment"), "layer0", modLoc("item/adornments/topaz_gold_necklace"));
        
        singleTexture(TreasureItems.ADORNMENT_ITEMS.get(modLoc("great_topaz_iron_necklace")).getRegistryName().getPath(),        		
        		mcLoc("treasure2:item/adornment"), "layer0", modLoc("item/adornments/great_topaz_iron_necklace"));
        singleTexture(TreasureItems.ADORNMENT_ITEMS.get(modLoc("great_topaz_copper_necklace")).getRegistryName().getPath(),        		
        		mcLoc("treasure2:item/adornment"), "layer0", modLoc("item/adornments/great_topaz_copper_necklace"));
        singleTexture(TreasureItems.ADORNMENT_ITEMS.get(modLoc("great_topaz_silver_necklace")).getRegistryName().getPath(),        		
        		mcLoc("treasure2:item/adornment"), "layer0", modLoc("item/adornments/great_topaz_silver_necklace"));
        singleTexture(TreasureItems.ADORNMENT_ITEMS.get(modLoc("great_topaz_gold_necklace")).getRegistryName().getPath(),        		
        		mcLoc("treasure2:item/adornment"), "layer0", modLoc("item/adornments/great_topaz_gold_necklace"));
        singleTexture(TreasureItems.ADORNMENT_ITEMS.get(modLoc("great_topaz_blood_necklace")).getRegistryName().getPath(),        		
        		mcLoc("treasure2:item/adornment"), "layer0", modLoc("item/adornments/great_topaz_blood_necklace"));
        singleTexture(TreasureItems.ADORNMENT_ITEMS.get(modLoc("great_topaz_black_necklace")).getRegistryName().getPath(),        		
        		mcLoc("treasure2:item/adornment"), "layer0", modLoc("item/adornments/great_topaz_black_necklace"));
        
        // topaz bracelets
        singleTexture(TreasureItems.ADORNMENT_ITEMS.get(modLoc("topaz_iron_bracelet")).getRegistryName().getPath(),        		
        		mcLoc("treasure2:item/adornment"), "layer0", modLoc("item/adornments/topaz_iron_bracelet"));
        singleTexture(TreasureItems.ADORNMENT_ITEMS.get(modLoc("topaz_copper_bracelet")).getRegistryName().getPath(),        		
        		mcLoc("treasure2:item/adornment"), "layer0", modLoc("item/adornments/topaz_copper_bracelet"));
        singleTexture(TreasureItems.ADORNMENT_ITEMS.get(modLoc("topaz_silver_bracelet")).getRegistryName().getPath(),
        		mcLoc("treasure2:item/adornment"), "layer0", modLoc("item/adornments/topaz_silver_bracelet"));
        singleTexture(TreasureItems.ADORNMENT_ITEMS.get(modLoc("topaz_gold_bracelet")).getRegistryName().getPath(),        		
        		mcLoc("treasure2:item/adornment"), "layer0", modLoc("item/adornments/topaz_gold_bracelet"));
        
        singleTexture(TreasureItems.ADORNMENT_ITEMS.get(modLoc("great_topaz_iron_bracelet")).getRegistryName().getPath(),        		
        		mcLoc("treasure2:item/adornment"), "layer0", modLoc("item/adornments/great_topaz_iron_bracelet"));
        singleTexture(TreasureItems.ADORNMENT_ITEMS.get(modLoc("great_topaz_copper_bracelet")).getRegistryName().getPath(),        		
        		mcLoc("treasure2:item/adornment"), "layer0", modLoc("item/adornments/great_topaz_copper_bracelet"));
        singleTexture(TreasureItems.ADORNMENT_ITEMS.get(modLoc("great_topaz_silver_bracelet")).getRegistryName().getPath(),        		
        		mcLoc("treasure2:item/adornment"), "layer0", modLoc("item/adornments/great_topaz_silver_bracelet"));
        singleTexture(TreasureItems.ADORNMENT_ITEMS.get(modLoc("great_topaz_gold_bracelet")).getRegistryName().getPath(),        		
        		mcLoc("treasure2:item/adornment"), "layer0", modLoc("item/adornments/great_topaz_gold_bracelet"));
        
        singleTexture(TreasureItems.ADORNMENT_ITEMS.get(modLoc("great_topaz_blood_bracelet")).getRegistryName().getPath(),        		
        		mcLoc("treasure2:item/adornment"), "layer0", modLoc("item/adornments/great_topaz_blood_bracelet"));
        singleTexture(TreasureItems.ADORNMENT_ITEMS.get(modLoc("great_topaz_black_bracelet")).getRegistryName().getPath(),        		
        		mcLoc("treasure2:item/adornment"), "layer0", modLoc("item/adornments/great_topaz_black_bracelet"));
    }
}
