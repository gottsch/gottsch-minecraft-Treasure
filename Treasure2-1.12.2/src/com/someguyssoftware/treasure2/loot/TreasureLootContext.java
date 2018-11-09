/**
 * 
 */
package com.someguyssoftware.treasure2.loot;

import java.io.IOException;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.collect.Sets;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.WorldServer;

/**
 * @author Mark Gottschling on Nov 7, 2018
 *
 */
public class TreasureLootContext {

    private final float luck;
    private final WorldServer world;
    private final TreasureLootTableManager lootTableManager;
    @Nullable
    private final Entity lootedEntity;
    @Nullable
    private final EntityPlayer player;
    @Nullable
    private final DamageSource damageSource;
    private final Set<TreasureLootTable> lootTables = Sets.<TreasureLootTable>newLinkedHashSet();

    
	/**
	 * 
	 */
	public TreasureLootContext(float luckIn, 
			WorldServer worldIn, 
			TreasureLootTableManager lootTableManagerIn, 
			@Nullable Entity lootedEntityIn, 
			@Nullable EntityPlayer playerIn, 
			@Nullable DamageSource damageSourceIn) {
        this.luck = luckIn;
        this.world = worldIn;
        this.lootTableManager = lootTableManagerIn;
        this.lootedEntity = lootedEntityIn;
        this.player = playerIn;
        this.damageSource = damageSourceIn;
	}

	  @Nullable
	    public Entity getLootedEntity()
	    {
	        return this.lootedEntity;
	    }

	    @Nullable
	    public Entity getKillerPlayer()
	    {
	        return this.player;
	    }

	    @Nullable
	    public Entity getKiller() {
	        return this.damageSource == null ? null : this.damageSource.getTrueSource();
	    }

	    public boolean addLootTable(TreasureLootTable lootTableIn) {
	        return this.lootTables.add(lootTableIn);
	    }

	    public void removeLootTable(TreasureLootTable lootTableIn) {
	        this.lootTables.remove(lootTableIn);
	    }

	    public TreasureLootTableManager getLootTableManager() {
	        return this.lootTableManager;
	    }

	    public float getLuck() {
	        return this.luck;
	    }

	    @Nullable
	    public Entity getEntity(TreasureLootContext.EntityTarget target)
	    {
	        switch (target)
	        {
	            case THIS:
	                return this.getLootedEntity();
	            case KILLER:
	                return this.getKiller();
	            case KILLER_PLAYER:
	                return this.getKillerPlayer();
	            default:
	                return null;
	        }
	    }

	    public WorldServer getWorld() {
	        return world;
	    }

	    /**
	     * 
	     * @return
	     */
	    public int getLootingModifier() {
	        return TreasureLootTableManager.getLootingLevel(getLootedEntity(), getKiller(), damageSource);
	    }

	    public static class Builder {
	            private final WorldServer world;
	            private float luck;
	            private Entity lootedEntity;
	            private EntityPlayer player;
	            private DamageSource damageSource;

	            public Builder(WorldServer worldIn)
	            {
	                this.world = worldIn;
	            }

	            public TreasureLootContext.Builder withLuck(float luckIn)
	            {
	                this.luck = luckIn;
	                return this;
	            }

	            public TreasureLootContext.Builder withLootedEntity(Entity entityIn)
	            {
	                this.lootedEntity = entityIn;
	                return this;
	            }

	            public TreasureLootContext.Builder withPlayer(EntityPlayer playerIn)
	            {
	                this.player = playerIn;
	                return this;
	            }

	            public TreasureLootContext.Builder withDamageSource(DamageSource dmgSource) {
	                this.damageSource = dmgSource;
	                return this;
	            }

	            public TreasureLootContext build() {
	                return new TreasureLootContext(this.luck, this.world, TreasureLootTables.lootTableManager, this.lootedEntity, this.player, this.damageSource);
	            }
	        }

	    /**
	     * 
	     * @author Mark Gottschling on Nov 7, 2018
	     *
	     */
	    public static enum EntityTarget {
	        THIS("this"),
	        KILLER("killer"),
	        KILLER_PLAYER("killer_player");

	        private final String targetType;

	        private EntityTarget(String type) {
	            this.targetType = type;
	        }

	        public static TreasureLootContext.EntityTarget fromString(String type) {
	            for (TreasureLootContext.EntityTarget lootcontext$entitytarget : values()) {
	                if (lootcontext$entitytarget.targetType.equals(type)) {
	                    return lootcontext$entitytarget;
	                }
	            }

	            throw new IllegalArgumentException("Invalid entity target " + type);
	        }

	        /**
	         * 
	         * @author Mark Gottschling on Nov 7, 2018
	         *
	         */
	        public static class Serializer extends TypeAdapter<TreasureLootContext.EntityTarget> {
	                public void write(JsonWriter jsonWriter, TreasureLootContext.EntityTarget entityTarget) throws IOException {
	                    jsonWriter.value(entityTarget.targetType);
	                }

	                public TreasureLootContext.EntityTarget read(JsonReader p_read_1_) throws IOException {
	                    return TreasureLootContext.EntityTarget.fromString(p_read_1_.nextString());
	                }
	            }
	    }
}
