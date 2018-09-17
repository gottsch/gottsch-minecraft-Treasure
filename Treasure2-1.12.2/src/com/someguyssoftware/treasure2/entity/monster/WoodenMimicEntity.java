/**
 * 
 */
package com.someguyssoftware.treasure2.entity.monster;

import net.minecraft.entity.EntityLiving;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;

/**
 * 
 * @author Mark Gottschling on Aug 18, 2018
 *
 */
public class WoodenMimicEntity extends MimicEntity {
	public static int MOB_ID = 1;

	/**
	 * 
	 * @param world
	 */
	public WoodenMimicEntity(World world) {
		super(world);
		this.setSize(0.9F, 0.9F);
	}

	/**
	 * 
	 */
	protected void entityInit() {
		super.entityInit();
	}

	/**
	 * 
	 * @param fixer
	 */
	public static void registerFixesWoodenMimic(DataFixer fixer) {
		EntityLiving.registerFixesMob(fixer, WoodenMimicEntity.class);
	}
	
	@Override
	protected ResourceLocation getLootTable() {
		// TODO Auto-generated method stub
		return super.getLootTable();
	}
}
