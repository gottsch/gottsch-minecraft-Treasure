/**
 *
 */
package com.someguyssoftware.treasure2.entity.monster;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;

/**
 * Viking chest mimic. Size and attributes match the 1.20.1 mimic.
 *
 * @author Mark Gottschling on 10/16/2026
 */
public class VikingChestMimicEntity extends MimicEntity {
	public static int MOB_ID = 4;

	/**
	 *
	 * @param world
	 */
	public VikingChestMimicEntity(World world) {
		super(world);
		this.setSize(0.875F, 0.875F);
	}

	/**
	 *
	 */
	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(32.0D);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(40.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.20D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5.0D);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(8.0D);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(10.0D);
	}

	/**
	 *
	 * @param fixer
	 */
	public static void registerFixes(DataFixer fixer) {
		EntityLiving.registerFixesMob(fixer, VikingChestMimicEntity.class);
	}
}
