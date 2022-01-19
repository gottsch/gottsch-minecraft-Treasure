package com.someguyssoftware.treasure2.capability;

import java.util.List;

import com.google.common.collect.Multimap;
import com.someguyssoftware.treasure2.runestone.IRunestone;
import com.someguyssoftware.treasure2.runestone.IRunestoneEntity;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IRunestonesCapability /*extends IMagicsInventorySupportCapability*/ {

	boolean isBindable();

	void setBindable(boolean bindable);

	boolean isSocketable();

	void setSocketable(boolean socketable);

	boolean hasRunestone();

	void appendHoverText(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn);
	
	void clear();
	int getCurrentSize(InventoryType type);
	int getMaxSize(InventoryType type);
	
	boolean contains(IRunestone charm);
	
	/**
	 * @param type
	 * @param entity
	 */
	void add(InventoryType type, IRunestoneEntity entity);

	List<IRunestoneEntity> getEntities(InventoryType type);

	/**
	 * Copy of all runestone entities. Not attached to underlying map.
	 * @return
	 */
	Multimap<InventoryType, IRunestoneEntity> getEntitiesCopy();

	void copyTo(ItemStack stack);
	
	void transferTo(ItemStack stack, InventoryType sourceType, InventoryType destType);
}
