/**
 * 
 */
package com.someguyssoftware.treasure2.capability;

import static com.someguyssoftware.treasure2.Treasure.logger;

import java.util.List;
import java.util.Optional;

import com.someguyssoftware.treasure2.runestone.IRunestone;
import com.someguyssoftware.treasure2.runestone.IRunestoneEntity;
import com.someguyssoftware.treasure2.runestone.Runestone;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

/**
 * 
 * @author Mark Gottschling on Jan 15, 2022
 *
 */
public class RunestonesCapabilityStorage implements Capability.IStorage<IRunestonesCapability> {
    private static final String BINDABLE_TAG = "bindable";
	private static final String SOCKETABLE_TAG = "socketable";
    
	@Override
	public NBTBase writeNBT(Capability<IRunestonesCapability> capability, IRunestonesCapability instance, EnumFacing side) {
		NBTTagCompound mainTag = new NBTTagCompound();
		try {
			/*
			 * save runestones
			 */
				for (InventoryType type : InventoryType.values()) {
//		Treasure.logger.debug("saving runestones -> {}", instance.getEntities(type).size());
				List<IRunestoneEntity> entityList = (List<IRunestoneEntity>) instance.getEntities(type);
				if (entityList != null && !entityList.isEmpty()) {
					NBTTagList listNbt = new NBTTagList();
					for (IRunestoneEntity entity : entityList) {
						NBTTagCompound entityNbt = new NBTTagCompound();
						listNbt.appendTag(entity.save(entityNbt));						
					}
					mainTag.setTag(type.name(), listNbt);
				}
			}
			
			mainTag.setBoolean(BINDABLE_TAG, instance.isBindable());
			mainTag.setBoolean(SOCKETABLE_TAG, instance.isSocketable());
		} catch (Exception e) {
			logger.error("Unable to write state to NBT:", e);
		}
		return mainTag;
	}

	@Override
	public void readNBT(Capability<IRunestonesCapability> capability, IRunestonesCapability instance, EnumFacing side, NBTBase nbt) {
		if (nbt instanceof NBTTagCompound) {
			NBTTagCompound tag = (NBTTagCompound) nbt;
			try {
			// clear the list
			instance.clear();
			for (InventoryType type : InventoryType.values()) {
				// load the charm entities
				if (tag.hasKey(type.name())) {
//					Treasure.logger.debug("loading type -> {}", type.name());
					NBTTagList listNbt = tag.getTagList(type.name(), 10);
					listNbt.forEach(e -> {
//						Treasure.logger.debug("loading element -> {}", e);
						/*
						 * load the runestone first.
						 * need to the load the runestone prior to RunestoneEntity.load() because the RunestoneEntity instance needs to be
						 *  created first using the Runestone, then the entity can be loaded.
						 */
						Optional<IRunestone> runestone = Runestone.load((NBTTagCompound) ((NBTTagCompound)e).getCompoundTag(IRunestoneEntity.RUNESTONE));
						if (!runestone.isPresent()) {
							return;
						}
//						Treasure.logger.debug("runestone -> {}", runestone.get());
						// create an entity
						IRunestoneEntity entity = runestone.get().createEntity();
//						Treasure.logger.debug("created entity -> {}", entity);
						// load entity
						entity.load((NBTTagCompound)e);
//						Treasure.logger.debug("loaded entity -> {}", entity);
						// add the entity to the list
						instance.add(type, entity);
//						Treasure.logger.debug("size of runestones -> {}", instance.getEntities(type));
					});
				}
			}
			
			if (tag.hasKey(BINDABLE_TAG)) {
				instance.setBindable(tag.getBoolean(BINDABLE_TAG));
			}
			
			if (tag.hasKey(SOCKETABLE_TAG)) {
				instance.setSocketable(tag.getBoolean(SOCKETABLE_TAG));
			}
		} catch (Exception e) {
			logger.error("Unable to write state to NBT:", e);
		}
		}		
	}
}
