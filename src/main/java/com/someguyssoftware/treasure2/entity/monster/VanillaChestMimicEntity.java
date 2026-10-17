package com.someguyssoftware.treasure2.entity.monster;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;

/**
 * Vanilla chest mimic. Size and attributes match the 1.20.1 mimic.
 *
 * @author Mark Gottschling on 10/16/2026
 */
public class VanillaChestMimicEntity extends MimicEntity {
	public static int MOB_ID = 10;

	/**
	 *
	 * @param world
	 */
	public VanillaChestMimicEntity(World world) {
		super(world);
		this.setSize(1.0F, 1.0F);
	}

	/**
	 *
	 */
	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(40.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.20D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5.0D);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(3.0D);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(2.0D);
	}

	/**
	 *
	 * @param fixer
	 */
	public static void registerFixes(DataFixer fixer) {
		EntityLiving.registerFixesMob(fixer, VanillaChestMimicEntity.class);
	}
}
