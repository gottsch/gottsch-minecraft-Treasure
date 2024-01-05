/*
 * This file is part of  Treasure2.
 * Copyright (c) 2023 Mark Gottschling (gottsch)
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Treasure2.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */
package mod.gottsch.forge.treasure2.core.entity.monster;

import com.someguyssoftware.gottschcore.world.WorldInfo;

import mod.gottsch.forge.treasure2.core.sound.TreasureSounds;
import net.minecraft.block.BlockState;
import net.minecraft.command.impl.data.EntityDataAccessor;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

/**
 * 
 * @author Mark Gottschling on Jun 6, 2023
 *
 */
public abstract class Mimic extends MonsterEntity {
	private static final DataParameter<Boolean> ACTIVE = EntityDataManager.defineId(Mimic.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Float> DATA_AMOUNT = EntityDataManager.defineId(Mimic.class, DataSerializers.FLOAT);
	private static final DataParameter<Boolean >TARGET = EntityDataManager.defineId(Mimic.class, DataSerializers.BOOLEAN);
	
	/*
		MC 1.18.2: net/minecraft/world/entity/Mob.lootTable
		Name: cd => f_21355_ => lootTable
		Side: BOTH
		AT: public net.minecraft.world.entity.Mob f_21355_ # lootTable
		Type: net/minecraft/resources/ResourceLocation
	 */

	
	/* !moj lootTable 1.16.5 */
	/*
	  	MC 1.16.5: net/minecraft/world/entity/Mob.lootTable
		Name: bu => field_184659_bA => lootTable
		Side: BOTH
		AT: public net.minecraft.entity.MobEntity field_184659_bA # lootTable
		Type: net/minecraft/resources/ResourceLocation
	 
	 */
	private static final String SRG_LOOT_TABLE = "field_184659_bA";
	
	private float amount;
	/** Flag whether lid is opening (or closing */
	public boolean isOpening = true;
	
	/**
	 * 
	 * @param entityType
	 * @param level
	 */
	protected Mimic(EntityType<? extends MonsterEntity> entityType, World level) {
		super(entityType, level);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new SwimGoal(this));
		this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, false));
		this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
		this.goalSelector.addGoal(6, new LookRandomlyGoal(this));
		
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
	}
	
	@Override
	public void aiStep() {
		if (!WorldInfo.isClientSide(level)) {
			if (!isActive() && amount < 1F) {
				amount += 0.05;
				setAmount(amount);
				
				if (amount > 1F) {
					amount = 1F;
					setActive(true);
				}
			}
			else {
				if (amount <= 0F) {
					amount = 0F;
					isOpening = true;
				}
				if (amount >=1F) {
					amount = 1F;
					isOpening = false;
				}
				
				if (isOpening) {
					amount += 0.1F;
				}
				else {
					amount -= 0.1F;
				}
				setAmount(amount);
			}
			
			if (this.getTarget() != null) {
				setHasTarget(true);
			} else {
				setHasTarget(false);
			}
		}		
		super.aiStep();
	}
	
	@Override
	public void addAdditionalSaveData(CompoundNBT tag) {
		super.addAdditionalSaveData(tag);
		tag.putBoolean("active", this.entityData.get(ACTIVE));
		tag.putFloat("amount", getAmount());
	}	

	@Override
	public void readAdditionalSaveData(CompoundNBT tag) {
		super.readAdditionalSaveData(tag); 
		if (tag.contains("active")) {
			this.entityData.set(ACTIVE, tag.getBoolean("active"));
		}
		if (tag.contains("amount")) {
			this.setAmount(tag.getFloat("amount"));
		}
	}
	
	@Override
	public CreatureAttribute getMobType() {
		return CreatureAttribute.UNDEFINED;
	}
	
	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.WOODEN_DOOR_OPEN;
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.CHEST_CLOSE;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return TreasureSounds.AMBIENT_MIMIC.get();
	}
	
	/**
	 * 
	 */
	@Override
	protected void playStepSound(BlockPos pols, BlockState state) {
		// (sound, volumn, pitch)
		this.playSound(SoundEvents.CHEST_LOCKED, 0.15F, 1.0F);
	}
	
	@Override
	public void playAmbientSound() {
		this.playSound(getAmbientSound(), 0.10F, 0.80F);
	}
	
	/**
	 * Set the loot table for the mob
	 */
	public void setLootTable(ResourceLocation lootTable) {
		ObfuscationReflectionHelper.setPrivateValue(MobEntity.class, this, lootTable, SRG_LOOT_TABLE);
	}
	
	@Override
   protected boolean shouldDropLoot() {
	      return true;
	}
	
	@Override
	protected void dropFromLootTable(DamageSource source, boolean p_213354_2_) {
		super.dropFromLootTable(source, p_213354_2_);
	}
	
	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(ACTIVE, false);
		this.entityData.define(DATA_AMOUNT, 0.0F);
		this.entityData.define(TARGET, false);
	}
	
	public boolean hasTarget() {
		return this.entityData.get(TARGET);
	}
	public void setHasTarget(boolean target) {
		this.entityData.set(TARGET, target);
	}
	
	public boolean isActive() {
		return this.entityData.get(ACTIVE);
	}
	
	public void setActive(boolean active) {
		this.entityData.set(ACTIVE, active);
	}
	
	public float getAmount() {
		return this.entityData.get(DATA_AMOUNT);
	}

	public void setAmount(float amount) {
		this.entityData.set(DATA_AMOUNT, amount);
	}
}
