/**
 * 
 */
package com.someguyssoftware.treasure2.block;

import java.util.Map;

import com.someguyssoftware.treasure2.enums.FogHeight;

import net.minecraft.block.material.Material;

/**
 * 
 * @author Mark Gottschling on May 8, 2018
 *
 */
public class PoisonFogBlock extends FogBlock {
    
	/**
	 * 
	 * @param modID
	 * @param name
	 * @param material
	 * @param map
	 */
	public PoisonFogBlock(String modID, String name, Material material, Map<FogHeight, FogBlock> map) {
		super(modID, name, material, map);
	}	
}
