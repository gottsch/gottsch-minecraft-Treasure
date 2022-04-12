/**
 * 
 */
package com.someguyssoftware.treasure2.tileentity;

import java.util.Random;

import com.someguyssoftware.gottschcore.GottschCore;
import com.someguyssoftware.gottschcore.measurement.Quantity;
import com.someguyssoftware.gottschcore.positional.Coords;
import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
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
	private Double spawnRange = 5D;
	
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
	public void readFromNBT(NBTTagCompound sourceTag) {
		super.readFromNBT(sourceTag);
		try {
			// read the custom name
			if (sourceTag.hasKey("mobName", 8)) {
				this.mobName = new ResourceLocation(sourceTag.getString("mobName"));
			}
			else {
                // select a random mob
				this.mobName = DungeonHooks.getRandomDungeonMob(new Random());
			}
			if (getMobName() == null || StringUtils.isNullOrEmpty(getMobName().toString())) {
				defaultMobSpawnerSettings();
				return;
			}
			
			int min = 1;
			int max = 1;
			if (sourceTag.hasKey("mobNumMin")) {
				min = sourceTag.getInteger("mobNumMin");
			}
			if (sourceTag.hasKey("mobNumMax")) {
				min = sourceTag.getInteger("mobNumMax");
			}
			this.mobNum = new Quantity(min, max);
			
			if (sourceTag.hasKey("spawnRange")) {
				Double spawnRange = sourceTag.getDouble("spawnRange");
				setSpawnRange(spawnRange);
			}
			
		}
		catch(Exception e) {
			Treasure.LOGGER.error("Error reading ProximitySpanwer properties from NBT:",  e);
		}
	}
	
	/**
	 * 
	 */
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound sourceTag) {
        super.writeToNBT(sourceTag);
        if (getMobName() == null || StringUtils.isNullOrEmpty(getMobName().toString())) {        	
            defaultMobSpawnerSettings();
        }
	    sourceTag.setString("mobName", getMobName().toString());
	    sourceTag.setInteger("mobNumMin", getMobNum().getMinInt());
	    sourceTag.setInteger("mobNumMax", getMobNum().getMaxInt());
	    sourceTag.setDouble("spawnRange", getSpawnRange());
	    
	    return sourceTag;
	}
    
    /**
     * 
     */
    private void defaultMobSpawnerSettings() {
        setMobName(new ResourceLocation("minecraft", "zombie"));
        setMobNum(new Quantity(1, 1));
        setSpawnRange(5.0D);
    }

    ///// TEMP
	@Override
	public void update() {
        if (WorldInfo.isClientSide()) {
        	return;
        }      
        
    	// get all players within range
        EntityPlayer player = null;

        boolean isTriggered = false;         
        double proximitySq = getProximity() * getProximity();
        if (proximitySq < 1) proximitySq = 1;
        
        // for each player
        for (int playerIndex = 0; playerIndex < getWorld().playerEntities.size(); ++playerIndex) {
            player = (EntityPlayer)getWorld().playerEntities.get(playerIndex);
            // get the distance
            double distanceSq = player.getDistanceSq(this.getPos().add(0.5D, 0.5D, 0.5D));
            if (this.getMobName().getResourcePath().equals("bound_soul")) {
            Treasure.LOGGER.debug("PTE for mob -> {} @ -> {}, proximity -> {}, distance -> {}, triggered -> {}, dead -> {}, result -> {}", this.getMobName(),
            		this.pos, proximitySq, distanceSq, isTriggered, this.isDead(), (!isTriggered && !this.isDead() && (distanceSq < proximitySq)) ? "met" : "not met");
            }
            if (!isTriggered && !this.isDead() && (distanceSq < proximitySq)) {
            	Treasure.LOGGER.debug("PTE proximity was met.");
            	isTriggered = true;
            	// exectute action
            	execute(this.getWorld(), new Random(), new Coords(this.getPos()), new Coords(player.getPosition()));

            	// NOTE: does not self-destruct that is up to the execute action to perform
            }
            
            if (this.isDead()) break;
        }
	}
    ///////
    
	/**
	 * 
	 */
	@Override
	public void execute(World world, Random random, Coords blockCoords, Coords playerCoords) {
		
    	int mobCount = RandomHelper.randomInt(random, getMobNum().getMinInt(), getMobNum().getMaxInt());

    	for (int i = 0; i < mobCount; i++) {
            Entity entity = EntityList.createEntityByIDFromName(getMobName(), world);
            if (entity == null) {
            	Treasure.LOGGER.debug("unable to create entity -> {}", getMobName());
            	selfDestruct();
            	return;
            }
            
            double x = (double)blockCoords.getX() + (world.rand.nextDouble() - world.rand.nextDouble()) * getSpawnRange() + 0.5D;
            double y = (double)(blockCoords.getY());// + world.rand.nextInt(3) - 1);
            double z = (double)blockCoords.getZ() + (world.rand.nextDouble() - world.rand.nextDouble()) * getSpawnRange() + 0.5D;
            entity.setLocationAndAngles(x, y, z, MathHelper.wrapDegrees(world.rand.nextFloat() * 360.0F), 0.0F);

            Treasure.LOGGER.debug("entity instanceof EntityLiving -> {}", (entity instanceof EntityLiving));
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