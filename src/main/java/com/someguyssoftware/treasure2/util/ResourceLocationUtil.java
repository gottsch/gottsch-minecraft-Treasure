/**
 * 
 */
package com.someguyssoftware.treasure2.util;

import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.util.ResourceLocation;

/**
 * @author Mark Gottschling on Dec 20, 2020
 *
 */
public class ResourceLocationUtil {

	public static ResourceLocation create(String name) {
		return hasDomain(name) ? new ResourceLocation(name) : new ResourceLocation(Treasure.MODID, name);
	}
	
	public static boolean hasDomain(String name) {
		return name.indexOf(":") >= 0;
	}
}
