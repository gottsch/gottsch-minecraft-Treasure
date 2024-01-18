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

import mod.gottsch.forge.gottschcore.world.WorldInfo;
import mod.gottsch.forge.treasure2.core.sound.TreasureSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

/**
 * 
 * @author Mark Gottschling on Jun 6, 2023
 *
 */
public abstract class Mimic extends Monster {
	private static final EntityDataAccessor<Boolean> ACTIVE = SynchedEntityData.defineId(PirateChestMimic.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Float> DATA_AMOUNT = SynchedEntityData.defineId(PirateChestMimic.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Boolean >TARGET = SynchedEntityData.defineId(PirateChestMimic.class, EntityDataSerializers.BOOLEAN);
	
	/*
		MC 1.18.2: net/minecraft/world/entity/Mob.lootTable
		Name: cd => f_21355_ => lootTable
		Side: BOTH
		AT: public net.minecraft.world.entity.Mob f_21355_ # lootTable
		Type: net/minecraft/resources/ResourceLocation
	 */
	private static final String SRG_LOOT_TABLE = "f_21355_";
	
	private float amount;
	/** Flag whether lid is opening (or closing */
	public boolean isOpening = true;
	
	/**
	 * 
	 * @param entityType
	 * @param level
	 */
	protected Mimic(EntityType<? extends Monster> entityType, Level level) {
		super(entityType, level);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, false));
		this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
		
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
	}

	// TODO refactor so that the amount does not have to be sent to the client side
	// OR once active, amount doesn't have to be sent.
	@Override
	public void aiStep() {
		if (!WorldInfo.isClientSide(level())) {
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
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putBoolean("active", this.entityData.get(ACTIVE));
		tag.putFloat("amount", getAmount());
	}	

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag); 
		if (tag.contains("active")) {
			this.entityData.set(ACTIVE, tag.getBoolean("active"));
		}
		if (tag.contains("amount")) {
			this.setAmount(tag.getFloat("amount"));
		}
	}
	
	@Override
	public MobType getMobType() {
		return MobType.UNDEFINED;
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
	
	public void setLootTable(ResourceLocation lootTable) {
		ObfuscationReflectionHelper.setPrivateValue(Mob.class, this, lootTable, SRG_LOOT_TABLE);
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
