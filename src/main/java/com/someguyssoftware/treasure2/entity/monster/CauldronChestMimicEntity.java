/**
 *
 */
package com.someguyssoftware.treasure2.entity.monster;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;

/**
 * Cauldron chest mimic. Size and attributes match the 1.20.1 mimic.
 *
 * @author Mark Gottschling on 10/16/2026
 */
public class CauldronChestMimicEntity extends MimicEntity {
	public static int MOB_ID = 5;

	/**
	 *
	 * @param world
	 */
	public CauldronChestMimicEntity(World world) {
		super(world);
		this.setSize(0.875F, 0.875F);
	}

	/**
	 *
	 */
	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40.0D);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(45.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.22D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5.5D);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(10.0D);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(10.0D);
	}

	/**
	 *
	 * @param fixer
	 */
	public static void registerFixes(DataFixer fixer) {
		EntityLiving.registerFixesMob(fixer, CauldronChestMimicEntity.class);
	}
}
