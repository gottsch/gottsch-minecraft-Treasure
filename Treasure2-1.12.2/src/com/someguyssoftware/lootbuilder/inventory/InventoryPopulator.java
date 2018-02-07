/**
 * 
 */
package com.someguyssoftware.lootbuilder.inventory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import com.someguyssoftware.gottschcore.inventory.InventoryUtil;
import com.someguyssoftware.gottschcore.item.util.ItemUtil;
import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.gottschcore.random.RandomWeightedCollection;
import com.someguyssoftware.lootbuilder.db.DbManager;
import com.someguyssoftware.lootbuilder.model.LootContainer;
import com.someguyssoftware.lootbuilder.model.LootContainerHasGroup;
import com.someguyssoftware.lootbuilder.model.LootGroupHasItem;
import com.someguyssoftware.treasure2.Treasure;

import javafx.scene.Group;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * @author Mark Gottschling on Jan 20, 2018
 *
 */
public class InventoryPopulator {

	public void populate(IInventory inventory, LootContainer container) {
		Random random = new Random();
		List<LootContainerHasGroup> containerGroups = DbManager.getInstance().getGroupsByContainer(container.getId());
		if (containerGroups == null || containerGroups.size() == 0) {
			Treasure.logger.warn("Container {} does not contain any groups.", container.getName());
			return;
		}
		
		// map of container groups
		Map<String, LootContainerHasGroup> groups = new HashMap<>();

		// determine # of groups to choose from using the container's group  min and max properties
		int numGroups = RandomHelper.randomInt(random, container.getMinGroups(), container.getMaxGroups());
		Treasure.logger.debug("Selecting {} groups.", numGroups);
		
		// randomly select groups and add to map
		for (int i = 0; i < numGroups; i++) {
			// TODO change this - nned to load all the groups into the RandomProbabilityCollection
			int select = RandomHelper.randomInt(random, 1, containerGroups.size());
			Treasure.logger.debug("Size of group: {}", containerGroups.size());
			Treasure.logger.debug("Index of group: {}", select-1);
			// get the container group
			LootContainerHasGroup g = containerGroups.get(select-1);
			Treasure.logger.debug("Selected group: {}", g);
			// add to the map
			groups.put(g.getGroup().getName(), g);
			// remove from the list
			containerGroups.remove(select-1);
		}
		
		// for all selected groups, fetch the items
		for (Entry<String, LootContainerHasGroup> cg : groups.entrySet()) {
			// check if the inventory has slots available
			List<Integer> slots = InventoryUtil.getAvailableSlots(inventory);
			if (slots == null || slots.isEmpty()) {
				Treasure.logger.warn("Slots is null or empty.");
				return;
			}
			List<LootGroupHasItem> groupItems = DbManager.getInstance().getItemsByGroup(cg.getValue());
			Treasure.logger.debug("Selecting {} items for group {}", groupItems.size(), cg.getValue());
			// add group items to weighted collection
			RandomWeightedCollection<LootGroupHasItem> col = new RandomWeightedCollection<>();
			for (LootGroupHasItem g : groupItems) {
				Treasure.logger.debug("Adding weight {} for item {}", g.getWeight(), g);
				col.add(g.getWeight(), g);
			}
			
			// TODO change to min/max of container
			// determine # of items from group to add
			int numOfItems = RandomHelper.randomInt(random, 0, groupItems.size());
			Treasure.logger.debug("num of items: {}, from group: {}", numOfItems, cg.getKey());
			
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
			}			
		}
	}
	
	/**
	 * 
	 * @param random
	 * @param groupItem
	 * @return
	 */
	public ItemStack toItemStack(Random random, LootGroupHasItem groupItem) {
		ItemStack stack = null;
		
		// check if poition, then build potion and return
//		if (chestItem.getName().equalsIgnoreCase(POTION_ITEM_NAME)
//				|| chestItem.getName().equalsIgnoreCase(SPLASH_POTION_ITEM_NAME)
//				|| chestItem.getName().equalsIgnoreCase(LINGERING_POTION_ITEM_NAME)) {
//			stack = toPotion(chestItem);
//			return stack;
//		}
		
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
			// TODO update addEnchantments with new enchantments and for shield
			for (int i = 0; i < enchants; i++) {	
				stack = ItemUtil.addEnchantment(stack);
			}
		}	
		// add specified enchantments
//		if (randomItem.getEnchants().getEnchantments() != null &&
//				randomItem.getEnchants().getEnchantments().size() > 0) {
//			for (ChestItemEnchantment e : randomItem.getEnchants().getEnchantments()) {
//				// TODO create method to add enchantment based on NAME to the ItemUtil
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
}
