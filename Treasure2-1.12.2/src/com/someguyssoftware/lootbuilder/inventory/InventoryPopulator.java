/**
 * 
 */
package com.someguyssoftware.lootbuilder.inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.someguyssoftware.gottschcore.inventory.InventoryUtil;
import com.someguyssoftware.gottschcore.item.util.ItemUtil;
import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.gottschcore.random.RandomWeightedCollection;
import com.someguyssoftware.lootbuilder.db.DbManager;
import com.someguyssoftware.lootbuilder.model.LootContainer;
import com.someguyssoftware.lootbuilder.model.LootContainerHasGroup;
import com.someguyssoftware.lootbuilder.model.LootGroupHasItem;
import com.someguyssoftware.lootbuilder.model.LootItem;
import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;

/**
 * @author Mark Gottschling on Jan 20, 2018
 *
 */
public class InventoryPopulator {
	private static final String POTION_ITEM_NAME = "minecraft:potion";
	private static final String SPLASH_POTION_ITEM_NAME = "minecraft:splash_potion";
	private static final String LINGERING_POTION_ITEM_NAME = "minecraft:lingering_potion";
	
	/**
	 * 
	 * @param inventory
	 * @param container
	 */
	public void populate(IInventory inventory, LootContainer container) {
		Random random = new Random();
		List<LootContainerHasGroup> containerGroups = DbManager.getInstance().getGroupsByContainer(container.getId());
		if (containerGroups == null || containerGroups.size() == 0) {
			Treasure.logger.warn("Container {} does not contain any groups.", container.getName());
			return;
		}
		Treasure.logger.debug("# groups for container: {}", containerGroups.size());
		// map of container groups
//		Map<String, LootContainerHasGroup> groups = new HashMap<>();

		// determine # of groups to choose from using the container's group  min and max properties
		int numGroups = RandomHelper.randomInt(random, container.getMinGroups(), container.getMaxGroups());
		Treasure.logger.debug("Selecting {} groups.", numGroups);
		
		/*
		 *  load groups and seperate into regular and special collections
		 *  regular groups are loaded into a RandomWeightedCollection and
		 *  special groups are loaded into a List
		 */
		List<LootContainerHasGroup> specialGroups = new ArrayList<>();
		RandomWeightedCollection<LootContainerHasGroup> groups = new RandomWeightedCollection<>();
		Treasure.logger.debug("Selecting groups from container {}", container.getName());
		for (LootContainerHasGroup g : containerGroups) {
			Treasure.logger.debug("Mapping group {}:{}:{}", g.getId(), g.getGroup().getName(), g.getWeight());
			if (g.getSpecial()) {
				Treasure.logger.debug("Adding group {} to SPECIAL list.", g.getGroup().getName());
				specialGroups.add(g);
			}
			else {
				Treasure.logger.debug("Adding group {} to regular rw collection.", g.getGroup().getName());
				groups.add(g.getWeight() < 1 ? 1 : g.getWeight(), g);
			}
		}
		
		Treasure.logger.debug("specialGroups.size: {}", specialGroups.size());
		Treasure.logger.debug("groups.size: {}", groups.size());
		
		// randomly select groups and add to map
		// NOTE this method allows multiples of the same group to be added.
//		for (int i = 0; i < numGroups; i++) {
//			LootContainerHasGroup g = groupCol.next();
//			Treasure.logger.debug("Selected group: {}", g);
//			// add to the map
//			groups.put(g.getGroup().getName(), g);
//		}
//		for (int i = 0; i < numGroups; i++) {
//			int select = RandomHelper.randomInt(random, 1, containerGroups.size());
//			Treasure.logger.debug("Size of group: {}", containerGroups.size());
//			Treasure.logger.debug("Index of group: {}", select-1);
//			// get the container group
//			LootContainerHasGroup g = containerGroups.get(select-1);
//			Treasure.logger.debug("Selected group: {}", g);
//			// add to the map
//			groups.put(g.getGroup().getName(), g);
//			// remove from the list
//			containerGroups.remove(select-1);
//		}
		
		// determine the total number of items to add to the container
		int totalNumItemsToAdd = RandomHelper.randomInt(random, container.getMinItems(), container.getMaxItems());
		Treasure.logger.debug("Total # of items to select: {}", totalNumItemsToAdd);
		int itemsAdded = 0;
		boolean addMoreItems = true;
		List<Integer> slots = null;

		// get a list of available slots
		slots = InventoryUtil.getAvailableSlots(inventory);
		if (slots == null || slots.isEmpty()) {
			Treasure.logger.warn("Slots is null or empty.");
			return;
		}		
		
		// first select items from the special groups list
		for (LootContainerHasGroup cg : specialGroups) {
			Treasure.logger.debug("Selecting SPECIAL group: {}", cg);			
			itemsAdded += addGroupItems(random, inventory, slots, cg, totalNumItemsToAdd-itemsAdded);
			if (itemsAdded >= totalNumItemsToAdd || slots == null || slots.size() == 0) break;
		}

		// process regular groups
		while (addMoreItems) {
//			// check if the inventory has slots available
//			slots = InventoryUtil.getAvailableSlots(inventory);
//			if (slots == null || slots.isEmpty()) {
//				Treasure.logger.warn("Slots is null or empty.");
//				return;
//			}
//			
			// fetch a group
			LootContainerHasGroup cg = groups.next();
			Treasure.logger.debug("Selecting group: {}", cg);	
			itemsAdded += addGroupItems(random, inventory, slots, cg, totalNumItemsToAdd-itemsAdded);
//			
//			// fetch the group items
//			List<LootGroupHasItem> groupItems = DbManager.getInstance().getItemsByGroup(cg);
//
//			// add items to weighted collection
//			RandomWeightedCollection<LootGroupHasItem> col = new RandomWeightedCollection<>();
//			for (LootGroupHasItem g : groupItems) {
//				Treasure.logger.debug("Adding weight {} for item {}", g.getWeight(), g);
//				col.add(g.getWeight(), g);
//			}
//			
//			// determine # of items from group to add
//			int numOfItems = RandomHelper.randomInt(random, cg.getMin(), cg.getMax());
//			Treasure.logger.debug("num of items: {}, from group: {}", numOfItems, cg.getGroup().getName());
//			
//			ItemStack stack = null;
//			// add a selection of items to the chest
//			for (int i = 0; i < numOfItems; i++) {
//				LootGroupHasItem gi = col.next();
//				stack = toItemStack(random,  gi);
//				if (stack == null) {
//					continue;
//				}
//				// add stack to inventory
//				if (stack != null) {
//					InventoryUtil.addItemToInventory(inventory, stack, random, slots);
//					if (slots == null || slots.size() == 0) {
//						break;
//					}
//				}
//				itemsAdded++;
				
			if (itemsAdded >= totalNumItemsToAdd || slots == null || slots.size() == 0) {
				addMoreItems = false;
			}
			
//				if(itemsAdded >= slots.size() || itemsAdded >= totalNumItemsToAdd) {
//					addMoreItems = false;
//					break;
//				}
//			}
		}
		
		// for all selected groups, fetch the items
//		for (Entry<String, LootContainerHasGroup> cg : groups.entrySet()) {
//			// check if the inventory has slots available
//			List<Integer> slots = InventoryUtil.getAvailableSlots(inventory);
//			if (slots == null || slots.isEmpty()) {
//				Treasure.logger.warn("Slots is null or empty.");
//				return;
//			}
//			List<LootGroupHasItem> groupItems = DbManager.getInstance().getItemsByGroup(cg.getValue());
//			Treasure.logger.debug("Selecting {} items for group {}", groupItems.size(), cg.getValue());
//			// add group items to weighted collection
//			RandomWeightedCollection<LootGroupHasItem> col = new RandomWeightedCollection<>();
//			for (LootGroupHasItem g : groupItems) {
//				Treasure.logger.debug("Adding weight {} for item {}", g.getWeight(), g);
//				col.add(g.getWeight(), g);
//			}
//
//			// determine # of items from group to add
//			int numOfItems = RandomHelper.randomInt(random, cg.getValue().getMin(), cg.getValue().getMax());
//			Treasure.logger.debug("num of items: {}, from group: {}", numOfItems, cg.getKey());
//			
//			ItemStack stack = null;
//			// add a selection of items to the chest
//			for (int i = 0; i < numOfItems; i++) {
//				LootGroupHasItem gi = col.next();
//				stack = toItemStack(random,  gi);
//				if (stack == null) {
//					continue;
//				}
//				// add stack to inventory
//				if (stack != null) {
//					InventoryUtil.addItemToInventory(inventory, stack, random, slots);
//					if (slots == null || slots.size() == 0) {
//						break;
//					}
//				}
//			}			
//		}
	}
	
	/**
	 * 
	 * @param random
	 * @param inventory
	 * @param slots
	 * @param cg
	 * @param maxItems
	 * @return
	 */
	public int addGroupItems(Random random, IInventory inventory, List<Integer> slots, LootContainerHasGroup cg, int maxItems) {
		int itemsAdded = 0;
		
		// fetch the group items
		List<LootGroupHasItem> groupItems = DbManager.getInstance().getItemsByGroup(cg);

		// add items to weighted collection
		RandomWeightedCollection<LootGroupHasItem> col = new RandomWeightedCollection<>();
		for (LootGroupHasItem g : groupItems) {
			Treasure.logger.debug("Adding weight {} for item {}", g.getWeight(), g);
			col.add(g.getWeight(), g);
		}
		
		// determine # of items from group to add
		int numOfItems = RandomHelper.randomInt(random, cg.getMin(), cg.getMax());
		Treasure.logger.debug("num of items: {}, from group: {}", numOfItems, cg.getGroup().getName());
		
		ItemStack stack = null;
		// add a selection of items to the chest
		for (int i = 0; i < numOfItems; i++) {
			LootGroupHasItem gi = col.next();
			stack = toItemStack(random,  gi);
			if (stack == null) {
				continue;
			}
			// add stack to inventory
			if (stack != null) {
				InventoryUtil.addItemToInventory(inventory, stack, random, slots);
				if (slots == null || slots.size() == 0) {
					break;
				}
			}
			itemsAdded++;
			
			if((maxItems - itemsAdded) > slots.size() || itemsAdded >= maxItems) {
				break;
			}
		}		
		return itemsAdded;
	}

	/**
	 * 
	 * @param random
	 * @param groupItem
	 * @return
	 */
	public static ItemStack toItemStack(Random random, LootGroupHasItem groupItem) {
		ItemStack stack = null;
		
		// check if poition, then build potion and return
		String itemName = groupItem.getItem().getMod().getPrefix() + ":" + groupItem.getItem().getMcName();
		if (itemName.equalsIgnoreCase(POTION_ITEM_NAME)
				|| itemName.equalsIgnoreCase(SPLASH_POTION_ITEM_NAME)
				|| itemName.equalsIgnoreCase(LINGERING_POTION_ITEM_NAME)) {
			stack = toPotion(groupItem.getItem());
			return stack;
		}
		
		// get the item from the item registry
		Treasure.logger.debug("Get item using id {}", groupItem.getItem().getMod().getPrefix() + ":" + groupItem.getItem().getMcName());
		Item item = toItem(groupItem.getItem().getMod().getPrefix() + ":" + groupItem.getItem().getMcName());
		
		if (item == null) {
			Treasure.logger.warn("Unable to convert LootItem to minecraft item: ", groupItem.getItem().getName());
			return null;
		}
		// determine size of stack
		int size = RandomHelper.randomInt(random, groupItem.getMin(), groupItem.getMax());
		// get the damage value. If greater than 0, create an item stack can call the proper method.
		// (for things like dyes (lapis) and potions that have multiple items per)
		if (groupItem.getItem().getDamage() > 0) {
			// create an itemStack
			stack = new ItemStack(item, size, groupItem.getItem().getDamage());
		}
		else {
			stack = new ItemStack(item, size);
		}
		
		// add enchantments
		int enchants = 0;
		if (groupItem.getMaxEnchants() > 0) {
			enchants = RandomHelper.randomInt(random,
					groupItem.getMinEnchants(),
					groupItem.getMaxEnchants());
//				Treasure.logger.debug("Adding enchantments");
			for (int i = 0; i < enchants; i++) {	
				stack = ItemUtil.addEnchantment(stack);
			}
		}	
		// add specified enchantments
//		if (randomItem.getEnchants().getEnchantments() != null &&
//				randomItem.getEnchants().getEnchantments().size() > 0) {
//			for (ChestItemEnchantment e : randomItem.getEnchants().getEnchantments()) {
//				// TOD create method to add enchantment based on NAME to the ItemUtil
//			}
//		}
		
		// return stack
		return stack;
	}
	
	/**
	 * 
	 * @return
	 */
	public static Item toItem(String itemName) {
		try {
			Item item = ItemUtil.getItemFromName(itemName);			
			return item;
		}
		catch(Exception e) {
			Treasure.logger.error("toItem error:", e);
			return null;
		}
	}
	
	/**
	 * 
	 * @param lootItem
	 * @return
	 */
	public static ItemStack toPotion(LootItem lootItem) {
		try {
			Item item = ItemUtil.getItemFromName(lootItem.getMod().getPrefix() + ":" + lootItem.getMcName());
			PotionType type = PotionType.getPotionTypeForName(lootItem.getName());
			ItemStack stack = PotionUtils.addPotionToItemStack(new ItemStack(item), type);
			return stack;
		}
		catch(Exception e) {
			Treasure.logger.error("toPotion error:", e);
			return null;
		}
	}
}
