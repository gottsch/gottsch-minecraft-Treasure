/**
 * 
 */
package com.someguyssoftware.treasure2.tileentity;

import java.util.Random;

import com.someguyssoftware.gottschcore.measurement.Quantity;
import com.someguyssoftware.gottschcore.positional.Coords;
import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.entity.monster.BoundSoulEntity;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
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
		setSpawnRange(3D);
		setHasEntity(false);
		// TEMP/DEBUG
//		setHasEntity(true);
	}

	/**
	 * NOTE this was not working when calling the super.update()
	 */
	@Override
	public void update() {
		// TODO somehow the update is getting called right away and setting the
		// hasEntity to fall before
		// player is in range.
		boolean hasEntity = hasEntity();
		if (hasEntity && TreasureConfig.WORLD_GEN.getMarkerProperties().isGravestoneSpawnMobAllowed) {
//			super.update();

			// this is copied fromt he abstract
			if (WorldInfo.isClientSide()) {
				return;
			}

			// get all players within range
			EntityPlayer player = null;

			boolean isTriggered = false;
			double proximitySq = getProximity() * getProximity();
			if (proximitySq < 1)
				proximitySq = 1;

			// for each player
			for (int playerIndex = 0; playerIndex < getWorld().playerEntities.size(); ++playerIndex) {
				player = (EntityPlayer) getWorld().playerEntities.get(playerIndex);
				// get the distance
				double distanceSq = player.getDistanceSq(this.getPos().add(0.5D, 0.5D, 0.5D));
				if (!isTriggered && !this.isDead() && (distanceSq < proximitySq)) {
					Treasure.logger.debug("PTE proximity was met.");
					isTriggered = true;
					// exectute action
					execute(this.getWorld(), new Random(), new Coords(this.getPos()), new Coords(player.getPosition()));
					// NOTE: does not self-destruct that is up to the execute action to perform
				}
				if (this.isDead())
					break;
			}
		}
	}

	/**
	 * 
	 */
	@Override
	public void execute(World world, Random random, Coords blockCoords, Coords playerCoords) {
		Treasure.logger.debug("executing");
		int mobCount = RandomHelper.randomInt(random, getMobNum().getMinInt(), getMobNum().getMaxInt());

		for (int i = 0; i < mobCount; i++) {
			BoundSoulEntity entity = (BoundSoulEntity) EntityList.createEntityByIDFromName(getMobName(), world);
			if (entity == null) {
				Treasure.logger.debug("unable to create entity -> {}", getMobName());
				selfDestruct();
				return;
			}
			entity.setHomePosAndDistance(blockCoords.toPos(), 10);

			if (entity instanceof EntityLiving) {
				double x = (double) blockCoords.getX() + (world.rand.nextDouble() * getSpawnRange())
						- getSpawnRange() / 2 + 0.5D;
				double y = (double) (blockCoords.getY());// + world.rand.nextInt(3) - 1);
				double z = (double) blockCoords.getZ() + (world.rand.nextDouble() * getSpawnRange())
						- getSpawnRange() / 2 + 0.5D;
				entity.setLocationAndAngles(x, y, z, MathHelper.wrapDegrees(world.rand.nextFloat() * 360.0F), 0.0F);
				Treasure.logger.debug("attempted location for bound soul spawn -> {} {} {}", x, y, z);

				EntityLiving entityLiving = (EntityLiving) entity;
				// TODO attempt 3 times to spawn
				for (int tries = 0; tries < 3; tries++) {
					if (!entityLiving.isNotColliding()) {
						Treasure.logger.debug("collision - no spawn");
					}
					if (entityLiving.getCanSpawnHere() && entityLiving.isNotColliding()) {
						Treasure.logger.debug("spawned bound soul");
						entityLiving.rotationYawHead = entityLiving.rotationYaw;
						entityLiving.renderYawOffset = entityLiving.rotationYaw;
						entityLiving.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(entityLiving)),
								(IEntityLivingData) null);
						world.spawnEntity(entity);
						entityLiving.playLivingSound();
						break;
					}
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