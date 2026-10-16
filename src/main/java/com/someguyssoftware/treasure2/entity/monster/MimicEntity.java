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
import com.someguyssoftware.treasure2.sound.TreasureSounds;

import io.netty.buffer.ByteBuf;
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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

/**
 *
 * @author Mark Gottschling on Sep 16, 2018
 *
 */
public abstract class MimicEntity extends EntityMob implements IEntityAdditionalSpawnData {
	/**
	 * Whether the mimic has been roused. The lid chomps while this is true and stays shut while false.
	 * Set when the mimic springs from a chest, or the first time it has a target (ex. spawned from an egg).
	 */
	private static final DataParameter<Boolean> ACTIVE = EntityDataManager.createKey(MimicEntity.class, DataSerializers.BOOLEAN);
	/** Whether the mimic currently has an attack target. */
	private static final DataParameter<Boolean> TARGET = EntityDataManager.createKey(MimicEntity.class, DataSerializers.BOOLEAN);
	/** How far the mimic has opened up (0 to 1). Saved and synced like 1.20.1's, for the Phase 3 models. */
	private static final DataParameter<Float> AMOUNT = EntityDataManager.createKey(MimicEntity.class, DataSerializers.FLOAT);

	private static final String ACTIVE_TAG = "active";
	private static final String AMOUNT_TAG = "amount";

	/** The current angle of the lid (between 0 and 1) */
	public float lidAngle = 0F;
	/** The angle of the lid last tick */
	public float prevLidAngle = 0F;
	/** Flag whether lid is opening (or closing */
	public boolean isOpening = true;

	private static final String CHEST_LOOT_TABLE_TAG = "chestLootTable";
	/** The loot table of the chest this mimic disguised itself as. The mimic drops the chest's full loot roll. */
	private ResourceLocation chestLootTable;


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

		prevLidAngle = lidAngle;
		if (isActive()) {
			// chomp
			if (lidAngle <= 0F) {
				lidAngle = 0.0F;
				isOpening = true;
			}
			if (lidAngle >= 1.0F) {
				lidAngle = 1.0F;
				isOpening = false;
			}

			if (isOpening) {
				lidAngle += 0.1F;
			} else {
				lidAngle -= 0.1F;
			}
		}
		else {
			// close the lid and sit still like a chest
			lidAngle = Math.max(0.0F, lidAngle - 0.1F);
			isOpening = true;
		}
	}

	/**
	 *
	 */
	@Override
	public void onLivingUpdate() {
		if (!world.isRemote) {
			boolean target = getAttackTarget() != null;
			if (target != hasTarget()) {
				setHasTarget(target);
			}
			if (target && !isActive()) {
				setActive(true);
			}
		}
		super.onLivingUpdate();
	}

	/**
	 *
	 */
	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(ACTIVE, false);
		this.dataManager.register(TARGET, false);
		this.dataManager.register(AMOUNT, 0.0F);
	}

	/**
	 * The client spawn packet carries rotationYaw, but not the body yaw, so the client's body starts at 0 (south)
	 * and turns round. Send the yaw and set every rotation from it on the client.
	 */
	@Override
	public void writeSpawnData(ByteBuf buffer) {
		buffer.writeFloat(rotationYaw);
	}

	@Override
	public void readSpawnData(ByteBuf buffer) {
		float yaw = buffer.readFloat();
		rotationYaw = yaw;
		prevRotationYaw = yaw;
		rotationYawHead = yaw;
		prevRotationYawHead = yaw;
		renderYawOffset = yaw;
		prevRenderYawOffset = yaw;
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

	@Override
	protected SoundEvent getAmbientSound() {
		return TreasureSounds.AMBIENT_MIMIC;
	}

	/**
	 * Quieter and lower than the default ambient volume and pitch, and silent while non-active (sitting like a chest).
	 * NOTE 1.20.1 plays it at 0.10, which is barely audible in 1.12 next to normal sounds.
	 */
	@Override
	public void playLivingSound() {
		if (!isActive()) {
			return;
		}
		SoundEvent sound = getAmbientSound();
		if (sound != null) {
			this.playSound(sound, 0.30F, 0.80F);
		}
	}

	/**
	 *
	 */
	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setBoolean(ACTIVE_TAG, isActive());
		compound.setFloat(AMOUNT_TAG, getAmount());
		if (chestLootTable != null) {
			compound.setString(CHEST_LOOT_TABLE_TAG, chestLootTable.toString());
		}
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		if (compound.hasKey(ACTIVE_TAG)) {
			setActive(compound.getBoolean(ACTIVE_TAG));
		}
		if (compound.hasKey(AMOUNT_TAG)) {
			setAmount(compound.getFloat(AMOUNT_TAG));
		}
		if (compound.hasKey(CHEST_LOOT_TABLE_TAG, 8) && !compound.getString(CHEST_LOOT_TABLE_TAG).isEmpty()) {
			chestLootTable = new ResourceLocation(compound.getString(CHEST_LOOT_TABLE_TAG));
		}
	}

	public boolean isActive() {
		return this.dataManager.get(ACTIVE);
	}

	public void setActive(boolean active) {
		this.dataManager.set(ACTIVE, active);
	}

	public boolean hasTarget() {
		return this.dataManager.get(TARGET);
	}

	public void setHasTarget(boolean target) {
		this.dataManager.set(TARGET, target);
	}

	public float getAmount() {
		return this.dataManager.get(AMOUNT);
	}

	public void setAmount(float amount) {
		this.dataManager.set(AMOUNT, amount);
	}

	public ResourceLocation getChestLootTable() {
		return chestLootTable;
	}

	public void setChestLootTable(ResourceLocation chestLootTable) {
		this.chestLootTable = chestLootTable;
	}

	/**
	 * The chest's loot table if the mimic came from a chest, otherwise a random scarce loot table.
	 */
	@Override
	protected ResourceLocation getLootTable() {
		if (chestLootTable != null) {
			return chestLootTable;
		}
		ResourceLocation lootTableResourceLocation = selectLootTableResourceLocation(getRNG(), Rarity.SCARCE);

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
		return location == null ? null : location.getResourceLocation();
	}	
}
