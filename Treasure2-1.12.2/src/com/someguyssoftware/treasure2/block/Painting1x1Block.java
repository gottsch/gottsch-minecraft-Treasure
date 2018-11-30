/**
 * 
 */
package com.someguyssoftware.treasure2.block;

import com.someguyssoftware.treasure2.enums.Rarity;

import net.minecraft.block.material.Material;

/**
 * @author Mark Gottschling on Nov 8, 2018
 *
 */
public class Painting1x1Block extends AbstractPaintingBlock {
	
	/**
	 * 
	 * @param modID
	 * @param name
	 * @param material
	 * @param paintingName
	 * @param collectionName
	 */
	public Painting1x1Block(String modID, String name, Material material) {
		super(modID, name, material);
//			,
//			String paintingName, String collectionName) {
//		super(modID, name, material, paintingName, collectionName);
	}
	
	/**
	 * 
	 * @param modID
	 * @param name
	 * @param material
	 * @param rarity
	 */
	public Painting1x1Block(String modID, String name, Material material, Rarity rarity) {
		super(modID, name, material, rarity);
	}
}
