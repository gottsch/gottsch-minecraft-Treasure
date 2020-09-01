/**
 * 
 */
package com.someguyssoftware.treasure2.tileentity;

import java.util.Random;

import com.someguyssoftware.gottschcore.measurement.Quantity;
import com.someguyssoftware.gottschcore.positional.Coords;
import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.DungeonHooks;

/**
 * @author Mark Gottschling on Jan 17, 2019
 *
 */
public class ProximitySpawnerTileEntity extends AbstractProximityTileEntity {
	private ResourceLocation mobName;
	private Quantity mobNum;
	private Double spawnRange = 1D;
	
	/**
	 * 
	 */
	public ProximitySpawnerTileEntity() {
	}

	/**
	 * @param proximity
	 */
	public ProximitySpawnerTileEntity(double proximity) {
		super(proximity);
	}

	/**
	 * 
	 */
	@Override
	public void readFromNBT(NBTTagCompound parentNBT) {
		super.readFromNBT(parentNBT);
		try {
			// read the custom name
			if (parentNBT.hasKey("mobName", 8)) {
				this.mobName = new ResourceLocation(parentNBT.getString("mobName"));
			}
			else {
                // select a random mob
                // TODO check if this return as a ResourceLocation string
				this.mobName = new ResourceLocation(DungeonHooks.getRandomDungeonMob(new Random()));
			}
			
			int min = 1;
			int max = 1;
			if (parentNBT.hasKey("mobNumMin")) {
				min = parentNBT.getInteger("mobNumMin");
			}
			if (parentNBT.hasKey("mobNumMax")) {
				min = parentNBT.getInteger("mobNumMax");
			}
			this.mobNum = new Quantity(min, max);
			
			if (parentNBT.hasKey("spawnRange")) {
				Double spawnRange = parentNBT.getDouble("spawnRange");
				setSpawnRange(spawnRange);
			}
			
		}
		catch(Exception e) {
			Treasure.logger.error("Error reading ProximitySpanwer properties from NBT:",  e);
		}
	}
	
	/**
	 * 
	 */
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        if (StringUtils.isEmpty(getMobName())) {
            defaultMobSpawnerSettings();
        }
	    tag.setString("mobName", getMobName().toString());
	    tag.setInteger("mobNumMin", getMobNum().getMinInt());
	    tag.setInteger("mobNumMax", getMobNum().getMaxInt());
	    tag.setDouble("spawnRange", getSpawnRange());
	    return tag;
	}
    
    /**
     * 
     */
    private void defaultMobSpawnerSettings() {
        setMobName(new ResourceLocation("minecraft", "zombie"));
        getMobNum().setMinInt(1);
        getMobNum().setMaxInt(1);
        setSpawnRange(5.0D);
    }

	/**
	 * 
	 */
	@Override
	public void execute(World world, Random random, Coords blockCoords, Coords playerCoords) {
		
    	int mobCount = RandomHelper.randomInt(random, getMobNum().getMinInt(), getMobNum().getMaxInt());

    	for (int i = 0; i < mobCount; i++) {
            Entity entity = EntityList.createEntityByIDFromName(getMobName(), world);
            if (entity == null) {
            	Treasure.logger.debug("unable to create entity -> {}", getMobName());
            	selfDestruct();
            	return;
            }
            
            double x = (double)blockCoords.getX() + (world.rand.nextDouble() - world.rand.nextDouble()) * getSpawnRange() + 0.5D;
            double y = (double)(blockCoords.getY());// + world.rand.nextInt(3) - 1);
            double z = (double)blockCoords.getZ() + (world.rand.nextDouble() - world.rand.nextDouble()) * getSpawnRange() + 0.5D;
            entity.setLocationAndAngles(x, y, z, MathHelper.wrapDegrees(world.rand.nextFloat() * 360.0F), 0.0F);

            if (entity instanceof EntityLiving) {
            	EntityLiving entityLiving = (EntityLiving)entity;
            	if (entityLiving.getCanSpawnHere() && entityLiving.isNotColliding()) {                
	                entityLiving.rotationYawHead = entityLiving.rotationYaw;
	                entityLiving.renderYawOffset = entityLiving.rotationYaw;
	                entityLiving.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(entityLiving)), (IEntityLivingData)null);
	                world.spawnEntity(entity);
	                entityLiving.playLivingSound();
            	}
            }
    	}    	
    	// self destruct
    	selfDestruct();
	}

	/**
	 * 
	 */
	private void selfDestruct() {
		this.setDead(true);
		this.getWorld().setBlockToAir(getPos());
		this.getWorld().removeTileEntity(getPos());
	}

	/**
	 * @return the mobName
	 */
	public ResourceLocation getMobName() {
		return mobName;
	}

	/**
	 * @param mobName the mobName to set
	 */
	public void setMobName(ResourceLocation mobName) {
		this.mobName = mobName;
	}

	/**
	 * @return the mobNum
	 */
	public Quantity getMobNum() {
		return mobNum;
	}

	/**
	 * @param mobNum the mobNum to set
	 */
	public void setMobNum(Quantity mobNum) {
		this.mobNum = mobNum;
	}

	public Double getSpawnRange() {
		return spawnRange;
	}

	public void setSpawnRange(Double spawnRange) {
		this.spawnRange = spawnRange;
	}

}