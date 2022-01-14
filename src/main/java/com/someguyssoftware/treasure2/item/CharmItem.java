/*
 * This file is part of  Treasure2.
 * Copyright (c) 2021, Mark Gottschling (gottsch)
 * 
 * All rights reserved.
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Treasure2.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */
package com.someguyssoftware.treasure2.item;

import java.util.List;

import javax.annotation.Nullable;

import com.someguyssoftware.gottschcore.item.ModItem;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.capability.CharmableCapabilityProvider;
import com.someguyssoftware.treasure2.capability.CharmableCapabilityStorage;
import com.someguyssoftware.treasure2.capability.ICharmableCapability;
import com.someguyssoftware.treasure2.capability.TreasureCapabilities;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

/**
 * @author Mark Gottschling on Aug 17, 2021
 *
 */
public class CharmItem extends ModItem {
	private static final CharmableCapabilityStorage CAPABILITY_STORAGE = new CharmableCapabilityStorage();

	/**
	 * 
	 * @param modID
	 * @param name
	 * @param properties
	 */
	public CharmItem(String modID, String name) {
		super(modID, name);
		this.setMaxStackSize(1);
		this.setCreativeTab(Treasure.TREASURE_TAB);
	}


	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
		// TODO create new CharmItemCapProvider which includes POUCHABLE cap (not everything that is charmable is pouchable)
		Treasure.logger.debug("{} item initiating caps", stack.getItem().getRegistryName().toString());
		CharmableCapabilityProvider provider =  new CharmableCapabilityProvider();
		return provider;
	}
	
	/**
	 * 
	 */
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		// charmable info
		tooltip.add(TextFormatting.GOLD + "" + TextFormatting.ITALIC + I18n.translateToLocal("tooltip.charmable.usage"));
		ICharmableCapability cap = getCap(stack);
		if (cap.isCharmed()) {
			cap.appendHoverText(stack, worldIn, tooltip, flagIn);
		}
	}
	
	/**
	 * Convenience method.
	 * @param stack
	 * @return
	 */
	public ICharmableCapability getCap(ItemStack stack) {
		if (stack.hasCapability(TreasureCapabilities.CHARMABLE, null)) {
		return stack.getCapability(TreasureCapabilities.CHARMABLE, null);
		}
		else {
			throw new IllegalStateException();
		}
	}
	
	/**
	 * 
	 */
	@Override
	public boolean hasEffect(ItemStack stack) {
		ICharmableCapability cap = getCap(stack);
		if (cap.isCharmed()) {
			return true;
		}
		return false;
	}
	
	// ///////////////////////////////////////////////////////////
//	private static final String SOURCE = "source";
//	private static final String EXECUTING = "executing";
//	private static final String BINDABLE = "bindable";
//	private static final String INNATE = "innate";
//	private static final String MAX_INNATE_SIZE = "maxInnateSize";
//	private static final String IMBUABLE = "imbuable";
//	private static final String IMBUING = "imbuing";
//	private static final String MAX_IMBUE_SIZE = "maxImbueSize";
//	private static final String SOCKETABLE = "socketable";
//	private static final String MAX_SOCKET_SIZE = "maxSocketSize";
//	private static final String BASE_MATERIAL = "baseMaterial";
//	private static final String SOURCE_ITEM = "sourceItem";
//	private static final String CHARM = "charm";
	
	/**
	 * NOTE getShareTag() and readShareTag() are required to sync item capabilities server -> client. I needed this when holding charms in hands and then swapping hands.
	 */
	@Override
    public NBTTagCompound getNBTShareTag(ItemStack stack) {
		NBTTagCompound charmableTag;
		charmableTag = (NBTTagCompound) CAPABILITY_STORAGE.writeNBT(
				TreasureCapabilities.CHARMABLE,
				stack.getCapability(TreasureCapabilities.CHARMABLE, null),
				null);
		
		return charmableTag;
//        // TODO is there a way to call CharmableCapabilityStorage.write() from here?  This is an exact duplicate
//		try {
//			/*
//			 * save charm cap inventories
//			 */
//			// create a new list nbt for each inventory type
//			for (int index = 0; index < cap.getCharmEntities().length; index++) {
//				List<ICharmEntity> entityList = cap.getCharmEntities()[index];
//				if (entityList != null && !entityList.isEmpty()) {
//					ListNBT listNbt = new ListNBT();
//					for (ICharmEntity entity : entityList) {
//						NBTTagCompound entityNbt = new NBTTagCompound();
//						listNbt.add(entity.save(entityNbt));						
//					}
//					nbt.put(InventoryType.getByValue(index).name(), listNbt);
//				}
//			}
//			
//			/*
//			 * save charm cap properties
//			 */
//			nbt.putBoolean(SOURCE, cap.isSource());
//			nbt.putBoolean(EXECUTING, cap.isExecuting());;
//			nbt.putBoolean(BINDABLE, cap.isBindable());
//			
//			nbt.putBoolean(INNATE, cap.isInnate());
//			nbt.putInt(MAX_INNATE_SIZE, cap.getMaxInnateSize());
//			
//			nbt.putBoolean(IMBUABLE, cap.isImbuable());
//			nbt.putBoolean(IMBUING, cap.isImbuing());			
//			nbt.putInt(MAX_IMBUE_SIZE, cap.getMaxImbueSize());
//			
//			nbt.putBoolean(SOCKETABLE, cap.isSocketable());
//			nbt.putInt(MAX_SOCKET_SIZE, cap.getMaxSocketsSize());
//			nbt.putString(BASE_MATERIAL, cap.getBaseMaterial().toString());
//			nbt.putString(SOURCE_ITEM, cap.getSourceItem().toString());
//			
//		} catch (Exception e) {
//			Treasure.LOGGER.error("Unable to write state to NBT:", e);
//		}
//		return nbt;
    }

    @Override
    public void readNBTShareTag(ItemStack stack, @Nullable NBTTagCompound nbt) {
        super.readNBTShareTag(stack, nbt);

        if (nbt instanceof NBTTagCompound) {
//        	NBTTagCompound tag = nbt.getCompoundTag("charmable");
	       CAPABILITY_STORAGE.readNBT(
	    		   TreasureCapabilities.CHARMABLE, 
					stack.getCapability(TreasureCapabilities.CHARMABLE, null), 
					null,
					nbt);
        }
        
//		if (nbt instanceof NBTTagCompound) {
//			 ICharmableCapability cap = stack.getCapability(CHARMABLE).orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!"));
//
//			NBTTagCompound tag = (NBTTagCompound) nbt;
//			for (InventoryType type : InventoryType.values()) {
//				// clear the list
//				cap.getCharmEntities()[type.getValue()].clear();				
//				/*
//				 *  load the list
//				 */
//				if (tag.contains(type.name())) {
//					ListNBT listNbt = tag.getList(type.name(), 10);
//					listNbt.forEach(e -> {
//						// load the charm
//						Optional<ICharm> charm = Charm.load((NBTTagCompound) ((NBTTagCompound)e).get(CHARM));
//						if (!charm.isPresent()) {
//							return;
//						}
//						// create an entity
//						ICharmEntity entity = charm.get().createEntity();
//						
//						// load entity
//						entity.load((NBTTagCompound)e);
//						
//						// add the entity to the list
//						cap.getCharmEntities()[type.getValue()].add(entity);
//					});
//				}
//				
//				// load cap properties
//				if (tag.contains(SOURCE)) {
//					cap.setSource(tag.getBoolean(SOURCE));
//				}
//				if (tag.contains(EXECUTING)) {
//					cap.setExecuting(tag.getBoolean(EXECUTING));
//				}
//				
//				if (tag.contains(BINDABLE)) {
//					cap.setBindable(tag.getBoolean(BINDABLE));
//				}
//				
//				if (tag.contains(INNATE)) {
//					cap.setInnate(tag.getBoolean(INNATE));
//				}				
//				if (tag.contains(MAX_INNATE_SIZE)) {
//					cap.setMaxInnateSize(tag.getInt(MAX_INNATE_SIZE));
//				}
//				
//				if (tag.contains(IMBUABLE)) {
//					cap.setImbuable(tag.getBoolean(IMBUABLE));
//				}				
//				if (tag.contains(MAX_IMBUE_SIZE)) {
//					cap.setMaxImbueSize(tag.getInt(MAX_IMBUE_SIZE));
//				}
//				if (tag.contains(IMBUING)) {
//					cap.setImbuing(tag.getBoolean(IMBUING));
//				}	
//				
//				if (tag.contains(SOCKETABLE)) {
//					cap.setSocketable(tag.getBoolean(SOCKETABLE));
//				}				
//				if (tag.contains(MAX_SOCKET_SIZE)) {
//					cap.setMaxSocketsSize(tag.getInt(MAX_SOCKET_SIZE));
//				}
//				if (tag.contains(BASE_MATERIAL)) {
//					cap.setBaseMaterial(ModUtils.asLocation(tag.getString(BASE_MATERIAL))); //BaseMaterial.valueOf(tag.getString(BASE_MATERIAL).toUpperCase()));
//				}
//				
//				if (tag.contains(SOURCE_ITEM)) {
//					cap.setSourceItem(ModUtils.asLocation(tag.getString(SOURCE_ITEM)));
//				}
//			}
    }
}