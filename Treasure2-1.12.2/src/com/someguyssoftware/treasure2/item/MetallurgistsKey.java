/**
 * 
 */
package com.someguyssoftware.treasure2.item;

import com.someguyssoftware.treasure2.enums.Category;

/**
 * 
 * @author Mark Gottschling on Jan 17, 2018
 *
 */
public class MetallurgistsKey extends KeyItem {

	/**
	 * 
	 * @param modID
	 * @param name
	 */
	public MetallurgistsKey(String modID, String name) {
		super(modID, name);
	}
	
	/**
	 * 
	 */
	@Override
	public boolean unlock(LockItem lockItem) {
		boolean isUnlocked = super.unlock(lockItem);
		
		if (!isUnlocked) {
			if (lockItem.getCategory() == Category.METALS) isUnlocked = true;
		}
		
		return isUnlocked;
	}
}
