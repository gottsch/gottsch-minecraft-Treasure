/**
 * 
 */
package com.someguyssoftware.treasure2.item;

import com.someguyssoftware.gottschcore.item.ModItem;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.enums.Category;
import com.someguyssoftware.treasure2.enums.Rarity;

/**
 * TODO Should we use LockItem and KeyItem and just make Items that extend an AbstractLock with the methods we need?
 * Maybe.... we do need an abstract class anyway for some state data to save, such as lifespan, usage etc.
 * !No! keep separation of item and lock/key states, esp locks. when a lock item is in inventory or on ground, there is no need
 * to save it's data to NBT as it is irrelevant.
 * 
 * @author Mark Gottschling on Jan 11, 2018
 *
 */
public class KeyItem extends ModItem {
	/*
	 * The category that the key belongs to
	 */
	private Category category;
	
	/*
	 * The rarity of the key
	 */
	private Rarity rarity;	

	private boolean craftable;
	
	/*
	 * Can the key break attempting to unlock a lock.
	 */
	private boolean breakable;
	
	// TODO add durability - ItemEntity property
	// TODO	
	
	// TODO need a LockMessage - an enum to indicate the action that resulted from unlock, ex. SUCCESS, FAIL, DESTROY, NO_FIT, etc.
	// instead of just a true/false result.
	

	/**
	 * 
	 * @param modID
	 * @param name
	 */
	public KeyItem(String modID, String name) {
		setItemName(modID, name);
		setCreativeTab(Treasure.TREASURE_TAB);
		setCategory(Category.BASIC);
		setRarity(Rarity.COMMON);
		setBreakable(true);
		setCraftable(false);		
	}

	/**
	 * 
	 * @param lockItem
	 * @return
	 */
	public boolean unlock(LockItem lockItem) {	
		if (lockItem.acceptsKey(this)) return true;
		// TODO add the check against probably of failure
		return false;
	}
	
	/**
	 * @return the rarity
	 */
	public Rarity getRarity() {
		return rarity;
	}

	/**
	 * @param rarity the rarity to set
	 */
	public KeyItem setRarity(Rarity rarity) {
		this.rarity = rarity;
		return this;
	}

	/**
	 * @return the craftable
	 */
	public boolean isCraftable() {
		return craftable;
	}

	/**
	 * @param craftable the craftable to set
	 */
	public KeyItem setCraftable(boolean craftable) {
		this.craftable = craftable;
		return this;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "KeyItem [rarity=" + rarity + ", craftable=" + craftable + "]";
	}

	/**
	 * @return the category
	 */
	public Category getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public KeyItem setCategory(Category category) {
		this.category = category;
		return this;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isBreakable() {
		return breakable;
	}
	
	/**
	 * 
	 * @param breakable
	 */
	public KeyItem setBreakable(boolean breakable) {
		this.breakable = breakable;
		return this;
	}
}
