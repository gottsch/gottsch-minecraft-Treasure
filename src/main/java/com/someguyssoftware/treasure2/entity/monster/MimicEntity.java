/**
 * 
 */
package com.someguyssoftware.treasure2.entity.monster;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.someguyssoftware.gottschcore.loot.LootTableShell;
import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.loot.TreasureLootTableRegistry;
import com.someguyssoftware.treasure2.sound.TreasureSounds;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
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
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
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

	/** Ticks since the mimic woke (capped), so the models can play their open animation. Not saved. */
	private int activeTicks = 0;
	private static final int MAX_ACTIVE_TICKS = 100;

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
		// while non-active, sit still like a chest (blocks moving and looking; targeting still runs)
		this.tasks.addTask(1, new EntityAIDormant(this));
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
			activeTicks = Math.min(activeTicks + 1, MAX_ACTIVE_TICKS);
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
			// close the lid and sit still like a chest, squared up to a chest facing (on both sides, so the client's
			// interpolated body yaw can't drift off it)
			lidAngle = Math.max(0.0F, lidAngle - 0.1F);
			isOpening = true;
			activeTicks = 0;
			faceLikeChest();
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

	/**
	 * Squares the mimic up to the nearest of the four chest facings (N, E, S, W).
	 */
	public void faceLikeChest() {
		float yaw = Math.round(MathHelper.wrapDegrees(rotationYaw) / 90.0F) * 90.0F;
		rotationYaw = yaw;
		prevRotationYaw = yaw;
		rotationYawHead = yaw;
		prevRotationYawHead = yaw;
		renderYawOffset = yaw;
		prevRenderYawOffset = yaw;
		rotationPitch = 0.0F;
		prevRotationPitch = 0.0F;
	}

	/**
	 * A spawn egg gives the mimic a random yaw - square it up like a placed chest.
	 */
	@Override
	@Nullable
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
		livingdata = super.onInitialSpawn(difficulty, livingdata);
		if (!isActive()) {
			faceLikeChest();
		}
		return livingdata;
	}

	/**
	 * Keeps a non-active mimic still and squared up like a chest. Uses all the movement, look and jump mutex bits, so
	 * wandering, looking around and attacking can't run until the mimic becomes active (when it gets a target).
	 */
	static class EntityAIDormant extends EntityAIBase {
		private final MimicEntity mimic;

		EntityAIDormant(MimicEntity mimic) {
			this.mimic = mimic;
			setMutexBits(7);
		}

		@Override
		public boolean shouldExecute() {
			return !mimic.isActive();
		}

		@Override
		public void startExecuting() {
			mimic.getNavigator().clearPath();
		}

		@Override
		public void updateTask() {
			mimic.getNavigator().clearPath();
			mimic.faceLikeChest();
		}
	}

	public void setActive(boolean active) {
		this.dataManager.set(ACTIVE, active);
	}

	public int getActiveTicks() {
		return activeTicks;
	}

	/**
	 * Using a non-active mimic wakes it, whoever the player is. Waking doesn't target the player - targeting is left to
	 * the AI, which ignores creative and spectator players.
	 */
	@Override
	protected boolean processInteract(EntityPlayer player, EnumHand hand) {
		if (!isActive()) {
			if (!world.isRemote) {
				setActive(true);
			}
			return true;
		}
		return super.processInteract(player, hand);
	}

	/**
	 * Being hurt by anything wakes a non-active mimic.
	 */
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (!world.isRemote && !isActive() && !isEntityInvulnerable(source)) {
			setActive(true);
		}
		return super.attackEntityFrom(source, amount);
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
