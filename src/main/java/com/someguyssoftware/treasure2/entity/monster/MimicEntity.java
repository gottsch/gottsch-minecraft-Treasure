/**
 * 
 */
package com.someguyssoftware.treasure2.entity.monster;

import java.util.List;
import java.util.Random;

import com.someguyssoftware.gottschcore.loot.LootTableShell;
import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.loot.TreasureLootTableRegistry;

import net.minecraft.block.Block;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * 
 * @author Mark Gottschling on Sep 16, 2018
 *
 */
public abstract class MimicEntity extends EntityMob {
	/** The current angle of the lid (between 0 and 1) */
	public float lidAngle = 0F;
	/** The angle of the lid last tick */
	public float prevLidAngle = 0F;
	/** Flag whether lid is opening (or closing */
	public boolean isOpening = true;


	/**
	 * 
	 * @param world
	 */
	public MimicEntity(World world) {
		super(world);
		this.setSize(0.9F, 0.9F);
	}

	/**
	 * 
	 */
	protected void initEntityAI() {
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIAttackMelee(this, 1.0D, false));
		this.tasks.addTask(7, new EntityAIWanderAvoidWater(this, 1.0D));
		this.tasks.addTask(8, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[0]));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
	}

	/**
	 * 
	 */
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(45.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.20D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(35.0D);
	}

	/**
	 * 
	 */
	@Override
	public void onUpdate() {
		super.onUpdate();
		
		if (lidAngle <= 0F) {
			lidAngle = 0.0F;
			isOpening = true;
		}
		if (lidAngle >= 1.0F) {
			lidAngle = 1.0F;
			isOpening = false;
		}
			
		// update the lidAngle here
		if (isOpening) {
			lidAngle += 0.1F;
		} else {
			lidAngle -= 0.1F;
		}

	}

	/**
	 * 
	 */
	protected void entityInit() {
		super.entityInit();
	}

	/**
	 * 
	 */
	@Override
	protected boolean canDespawn() {
		return false;
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.BLOCK_WOODEN_DOOR_OPEN;
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	protected SoundEvent getDeathSound() {
		return SoundEvents.BLOCK_CHEST_CLOSE;
	}

	/**
	 * 
	 */
	protected void playStepSound(BlockPos pos, Block block) {
		this.playSound(SoundEvents.BLOCK_CHEST_LOCKED, 0.15F, 1.0F);
	}
	
	/**
	 * 
	 */
	@Override
	protected ResourceLocation getLootTable() {
		ResourceLocation lootTableResourceLocation = selectLootTableResourceLocation(new Random(), Rarity.SCARCE);

		if (lootTableResourceLocation == null) {
			Treasure.LOGGER.warn("Unable to select a lootTable resource location.");
			return null;
		}
		Treasure.LOGGER.debug("Selected loot table resource location -> {}", lootTableResourceLocation.toString());
		return lootTableResourceLocation;
	}
	
	/**
	 * 
	 * @param random
	 * @param chestRarity
	 * @return
	 */
	protected ResourceLocation selectLootTableResourceLocation(Random random, final Rarity chestRarity) {
		LootTableShell location = null;

		// select the loot table by rarity
		List<LootTableShell> tableShells = TreasureLootTableRegistry.getLootTableMaster().getLootTableByRarity(Rarity.SCARCE);
		
		// select a random location from the list
		if (tableShells != null && !tableShells.isEmpty()) {
			int index = 0;		
			/*
			 * get a random container
			 */
			if (tableShells.size() == 1) {
				location = tableShells.get(0);
			}
			else {
				index = RandomHelper.randomInt(random, 0, tableShells.size()-1);
				location = tableShells.get(index);
			}
			Treasure.LOGGER.debug("Selected resource location index --> {}", index);
		}
		return location.getResourceLocation();
	}	
}
