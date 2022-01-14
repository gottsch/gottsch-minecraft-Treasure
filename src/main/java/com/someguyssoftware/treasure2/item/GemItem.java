package com.someguyssoftware.treasure2.item;

import com.someguyssoftware.gottschcore.item.ModItem;
import com.someguyssoftware.treasure2.Treasure;

/**
 * @author Mark Gottschling on May 14, 2020
 *
 */
@Deprecated
public class GemItem extends ModItem implements IPouchable {
	
	/**
	 * 
	 */
	public GemItem (String modID, String name)	 {
		super();
		this.setItemName(modID, name);
		setCreativeTab(Treasure.TREASURE_TAB);
	}

}
