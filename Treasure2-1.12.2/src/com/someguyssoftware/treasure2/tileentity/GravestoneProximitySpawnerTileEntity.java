/**
 * 
 */
package com.someguyssoftware.treasure2.tileentity;

import java.util.Random;

import com.someguyssoftware.gottschcore.measurement.Quantity;
import com.someguyssoftware.gottschcore.positional.Coords;
import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.entity.monster.BoundSoulEntity;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

/**
 * @author Mark Gottschling on Jan 17, 2019
 *
 */
public class GravestoneProximitySpawnerTileEntity extends ProximitySpawnerTileEntity {
	private boolean hasEntity;

	/**
	 * 
	 */
	public GravestoneProximitySpawnerTileEntity() {
		setProximity(20D);
		setMobName(new ResourceLocation(Treasure.MODID, "bound_soul"));
		setMobNum(new Quantity(1, 1));
		setSpawnRange(2D);
//		setHasEntity(false);
		// TEMP
		setHasEntity(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.minecraft.util.ITickable#update()
	 */
	@Override
	public void update() {
		// only update if block stat HAS_ENTITY = true
//		boolean hasEntity = this.world.getBlockState(getPos()).getValue(GravestoneBlock.HAS_ENTITY);
		boolean hasEntity = hasEntity();
		if (hasEntity) {
			super.update();
		}
	}

	/**
	 * 
	 */
	@Override
	public void execute(World world, Random random, Coords blockCoords, Coords playerCoords) {
		int mobCount = RandomHelper.randomInt(random, getMobNum().getMinInt(), getMobNum().getMaxInt());

		for (int i = 0; i < mobCount; i++) {
			BoundSoulEntity entity = (BoundSoulEntity) EntityList.createEntityByIDFromName(getMobName(), world);
			if (entity == null) {
				Treasure.logger.debug("unable to create entity -> {}", getMobName());
				selfDestruct();
				return;
			}
			entity.setHomePosAndDistance(blockCoords.toPos(), 10);

			double x = (double) blockCoords.getX() + (world.rand.nextDouble() * getSpawnRange()) - getSpawnRange() / 2
					+ 0.5D;
			double y = (double) (blockCoords.getY());// + world.rand.nextInt(3) - 1);
			double z = (double) blockCoords.getZ() + (world.rand.nextDouble() * getSpawnRange()) - getSpawnRange() / 2
					+ 0.5D;
			entity.setLocationAndAngles(x, y, z, MathHelper.wrapDegrees(world.rand.nextFloat() * 360.0F), 0.0F);

			if (entity instanceof EntityLiving) {
				EntityLiving entityLiving = (EntityLiving) entity;
				if (entityLiving.getCanSpawnHere() && entityLiving.isNotColliding()) {
					entityLiving.rotationYawHead = entityLiving.rotationYaw;
					entityLiving.renderYawOffset = entityLiving.rotationYaw;
					entityLiving.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(entityLiving)),
							(IEntityLivingData) null);
					world.spawnEntity(entity);
					entityLiving.playLivingSound();
				}
				// turn off hasEntity
				setHasEntity(false);
			}
		}
		// self destruct
		selfDestruct();
	}

	/**
	 * 
	 */
	private void selfDestruct() {
		Treasure.logger.debug("self destructing.");
		this.setDead(true);
	}

	/**
	 * 
	 */
	@Override
	public void readFromNBT(NBTTagCompound parentNBT) {
		super.readFromNBT(parentNBT);
		try {
			// read the custom name
			if (parentNBT.hasKey("hasEntity", 8)) {
				this.hasEntity = parentNBT.getBoolean("hasEntity");
				Treasure.logger.debug("value of nbt entity -> {}", parentNBT.getBoolean("hasEntity"));
			}
		} catch (Exception e) {
			Treasure.logger.error("Error reading AbstractProximity properties from NBT:", e);
		}
	}

	/**
	 * 
	 */
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setBoolean("hasEntity", hasEntity());
		return tag;
	}

	public boolean hasEntity() {
		return this.hasEntity;
	}

	public void setHasEntity(boolean hasEntity) {
		this.hasEntity = hasEntity;
	}
}