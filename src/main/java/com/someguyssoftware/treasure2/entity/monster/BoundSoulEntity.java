/**
 * 
 */
package com.someguyssoftware.treasure2.entity.monster;

import java.util.Random;

import javax.annotation.Nullable;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.particle.AbstractMistParticle;
import com.someguyssoftware.treasure2.particle.MistParticle;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIZombieAttack;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Mark Gottschling on Feb 23, 2020
 *
 */
public class BoundSoulEntity extends EntityZombie {
	private static final ResourceLocation BOUND_SOUL_LOOT_TABLE_LOCATION = new ResourceLocation(Treasure.MODID,
			"entities/bound_soul");
	public static final int MOB_ID = 3;

	/**
	 * 
	 * @param worldIn
	 */
	public BoundSoulEntity(World worldIn) {
		super(worldIn);
		this.setCanPickUpLoot(true);
	}

	/**
	 * 
	 * @param worldIn
	 * @param homePos
	 */
	public BoundSoulEntity(World worldIn, BlockPos homePos) {
		this(worldIn);
		this.setHomePosAndDistance(homePos, 10);
	}

	/**
	 * 
	 */
	@Override
	protected void initEntityAI() {
		this.tasks.addTask(0, new EntityAISwimming(this));
		if (this.hasHome()) {
			this.tasks.addTask(2, new EntityAIMoveTowardsRestriction(this, 1.0D));
		}
		this.tasks.addTask(3, new EntityAIZombieAttack(this, 1.0D, false));
		this.tasks.addTask(7, new EntityAIWanderAvoidWater(this, 1.0D));
		this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(8, new EntityAILookIdle(this));
		this.applyEntityAI();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected void applyEntityAI() {
		this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(22.0D);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(20.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.24D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(3.0D);
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();

		// create a mist particle
		if (!this.isServerWorld()) {
			spawnMist();
		}

		// regeneration
		TileEntity homeTileEntity = this.world.getTileEntity(getHomePosition());
		if (homeTileEntity == null)
			return;

		if (!this.isDead && this.ticksExisted % 20 == 0 && this.getHealth() > 0
				&& this.getHealth() < this.getMaxHealth()) {

			this.setHealth(this.getHealth() + 1.0F);
		}
	}

	@SideOnly(Side.CLIENT)
	private void spawnMist() {
		if (this.ticksExisted % 4 == 0) {
			Random random = new Random();
			AbstractMistParticle mistParticle = new MistParticle(world,
					this.getPosition().getX() + (random.nextFloat() * 0.5 - 0.25),
					this.getPosition().getY() + (random.nextFloat() * 0.25),
					this.getPosition().getZ() + (random.nextFloat() * 0.5 - 0.25), 0, 0, 0, null);
			// reduce the max age - don't want it too misty around entity
			mistParticle.setMaxAge(160);
			mistParticle.init();
			Minecraft.getMinecraft().effectRenderer.addEffect(mistParticle);
		}
	}

	@Override
	@Nullable
	protected ResourceLocation getLootTable() {
		return BOUND_SOUL_LOOT_TABLE_LOCATION;
	}

	@Override
	public boolean getCanSpawnHere() {
		return this.world.getDifficulty() != EnumDifficulty.PEACEFUL;
	}

	/**
	 * 
	 */
	@Override
	protected boolean shouldBurnInDay() {
		return false;
	}

	/**
	 * Determines if an entity can be despawned, used on idle far away entities
	 */
	protected boolean canDespawn() {
		return false;
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		// save homePosition and maximum distance
		NBTTagCompound coords = new NBTTagCompound();
		NBTTagInt x = new NBTTagInt(getHomePosition().getX());
		NBTTagInt y = new NBTTagInt(getHomePosition().getY());
		NBTTagInt z = new NBTTagInt(getHomePosition().getZ());
		coords.setTag("x", x);
		coords.setTag("y", y);
		coords.setTag("z", z);
		compound.setTag("homePosition", coords);
		compound.setTag("maximumHomeDistance", new NBTTagInt((int) getMaximumHomeDistance()));
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		NBTTagCompound coords = compound.getCompoundTag("homePosition");
		int x = coords.getInteger("x");
		int y = coords.getInteger("y");
		int z = coords.getInteger("z");

		int distance = coords.getInteger("maximumHomeDistance");
		distance = distance == 0 ? 10 : distance;
		setHomePosAndDistance(new BlockPos(x, y, z), distance);
	}

	/**
	 * 
	 * @param fixer
	 */
	public static void registerFixesBoundSoul(DataFixer fixer) {
		EntityLiving.registerFixesMob(fixer, BoundSoulEntity.class);
	}
}
