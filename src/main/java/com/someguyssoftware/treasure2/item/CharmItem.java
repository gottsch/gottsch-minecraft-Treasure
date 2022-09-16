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
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
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
	 * @param properties
	 */
	public CharmItem(Properties properties) {
		super(properties.tab(TreasureItemGroups.TREASURE_ITEM_GROUP).stacksTo(1));
	}
	
	/**
	 * 
	 * @param modID
	 * @param name
	 * @param properties
	 */
	@Deprecated
	public CharmItem(String modID, String name, Properties properties) {
		super(modID, name, properties.tab(TreasureItemGroups.TREASURE_ITEM_GROUP).stacksTo(1));
	}


	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
		// TODO create new CharmItemCapProvider which includes POUCHABLE cap (not everything that is charmable is pouchable)
		Treasure.LOGGER.debug("{} item initiating caps", stack.getItem().getRegistryName().toString());
		CharmableCapabilityProvider provider =  new CharmableCapabilityProvider();
		return provider;
	}
	
	/**
	 * 
	 */
	@Override
	public void appendHoverText(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
		// charmable info
		tooltip.add(new TranslationTextComponent("tooltip.charmable.usage").withStyle(TextFormatting.GOLD, TextFormatting.ITALIC));
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
		return stack.getCapability(TreasureCapabilities.CHARMABLE, null).orElseThrow(IllegalStateException::new);
	}
	
	/**
	 * 
	 */
	@Override
	public boolean isFoil(ItemStack stack) {
		ICharmableCapability cap = getCap(stack);
		if (cap.isCharmed()) {
			return true;
		}
		else {
			return false;
		}
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
    public CompoundNBT getShareTag(ItemStack stack) {
		CompoundNBT charmableTag;
		charmableTag = (CompoundNBT) CAPABILITY_STORAGE.writeNBT(
				TreasureCapabilities.CHARMABLE,
				stack.getCapability(TreasureCapabilities.CHARMABLE).map(cap -> cap).orElse(null),
				null);
		
		return charmableTag;
//        CompoundNBT nbt = stack.getOrCreateTag();
//        ICharmableCapability cap = stack.getCapability(CHARMABLE).orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!"));
//
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
//						CompoundNBT entityNbt = new CompoundNBT();
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
//			nbt.putInt(MAX_SOCKET_SIZE, cap.getMaxSocketSize());
//			nbt.putString(BASE_MATERIAL, cap.getBaseMaterial().toString());
//			nbt.putString(SOURCE_ITEM, cap.getSourceItem().toString());
//			
//		} catch (Exception e) {
//			Treasure.LOGGER.error("Unable to write state to NBT:", e);
//		}
//		return nbt;
    }

    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundNBT nbt) {
        super.readShareTag(stack, nbt);

        if (nbt instanceof CompoundNBT) {
	       CAPABILITY_STORAGE.readNBT(
	    		   TreasureCapabilities.CHARMABLE, 
					stack.getCapability(TreasureCapabilities.CHARMABLE).map(cap -> cap).orElse(null), null, nbt);
        }
    }
//        super.readShareTag(stack, nbt);
//
//		if (nbt instanceof CompoundNBT) {
//			 ICharmableCapability cap = stack.getCapability(CHARMABLE).orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!"));
//
//			CompoundNBT tag = (CompoundNBT) nbt;
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
//						Optional<ICharm> charm = Charm.load((CompoundNBT) ((CompoundNBT)e).get(CHARM));
//						if (!charm.isPresent()) {
//							return;
//						}
//						// create an entity
//						ICharmEntity entity = charm.get().createEntity();
//						
//						// load entity
//						entity.load((CompoundNBT)e);
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
//		}
//    }
}
