/**
 * 
 */
package com.someguyssoftware.treasure2.capability;

import static com.someguyssoftware.treasure2.Treasure.LOGGER;

import java.util.List;
import java.util.Optional;

import com.someguyssoftware.treasure2.rune.IRune;
import com.someguyssoftware.treasure2.rune.IRuneEntity;
import com.someguyssoftware.treasure2.rune.Rune;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
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
	public INBT writeNBT(Capability<IRunestonesCapability> capability, IRunestonesCapability instance, Direction side) {
		CompoundNBT mainTag = new CompoundNBT();
		try {
			/*
			 * save runestones
			 */
			for (InventoryType type : InventoryType.values()) {
				//		Treasure.logger.debug("saving runestones -> {}", instance.getEntities(type).size());
				List<IRuneEntity> entityList = (List<IRuneEntity>) instance.getEntities(type);
				if (entityList != null && !entityList.isEmpty()) {
					ListNBT listNbt = new ListNBT();
					for (IRuneEntity entity : entityList) {
						CompoundNBT entityNbt = new CompoundNBT();
						listNbt.add(entity.save(entityNbt));						
					}
					mainTag.put(type.name(), listNbt);
				}
			}

			mainTag.putBoolean(BINDABLE_TAG, instance.isBindable());
			mainTag.putBoolean(SOCKETABLE_TAG, instance.isSocketable());
		} catch (Exception e) {
			LOGGER.error("Unable to write state to NBT:", e);
		}
		return mainTag;
	}

	@Override
	public void readNBT(Capability<IRunestonesCapability> capability, IRunestonesCapability instance, Direction side, INBT nbt) {
		if (nbt instanceof CompoundNBT) {
			CompoundNBT tag = (CompoundNBT) nbt;
			try {
				// clear the list
				instance.clear();
				for (InventoryType type : InventoryType.values()) {
					// load the charm entities
					if (tag.contains(type.name())) {
						//					Treasure.logger.debug("loading type -> {}", type.name());
						ListNBT listNbt = tag.getList(type.name(), 10);
						listNbt.forEach(e -> {
							//						Treasure.logger.debug("loading element -> {}", e);
							/*
							 * load the runestone first.
							 * need to the load the runestone prior to RunestoneEntity.load() because the RunestoneEntity instance needs to be
							 *  created first using the Runestone, then the entity can be loaded.
							 */
							Optional<IRune> runestone = Rune.load((CompoundNBT) ((CompoundNBT)e).getCompound(IRuneEntity.RUNESTONE));
							if (!runestone.isPresent()) {
								return;
							}
							//						Treasure.logger.debug("runestone -> {}", runestone.get());
							// create an entity
							IRuneEntity entity = runestone.get().createEntity();
							//						Treasure.logger.debug("created entity -> {}", entity);
							// load entity
							entity.load((CompoundNBT)e);
							//						Treasure.logger.debug("loaded entity -> {}", entity);
							// add the entity to the list
							instance.add(type, entity);
							//						Treasure.logger.debug("size of runestones -> {}", instance.getEntities(type));
						});
					}
				}

				if (tag.contains(BINDABLE_TAG)) {
					instance.setBindable(tag.getBoolean(BINDABLE_TAG));
				}

				if (tag.contains(SOCKETABLE_TAG)) {
					instance.setSocketable(tag.getBoolean(SOCKETABLE_TAG));
				}
			} catch (Exception e) {
				LOGGER.error("Unable to write state to NBT:", e);
			}
		}		
	}
}
