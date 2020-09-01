/**
 * 
 */
package com.someguyssoftware.treasure2.entity.monster;

import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;

/**
 * 
 * @author Mark Gottschling on Jan 28, 2020
 *
 */
public class PirateMimicEntity extends WoodMimicEntity {
	public static int MOB_ID = 2;


	/**
	 * 
	 * @param world
	 */
	public PirateMimicEntity(World world) {
		super(world);
		this.setSize(0.9F, 0.9F);
	}

	/**
	 * 
	 * @param fixer
	 */
	public static void registerFixesPirateMimic(DataFixer fixer) {
		EntityLiving.registerFixesMob(fixer, PirateMimicEntity.class);
	}
}
