/**
 * 
 */
package com.someguyssoftware.treasure2.item;

import net.minecraft.item.Item;

import com.someguyssoftware.gottschcore.item.ModItem;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.enums.Coins;

/**
 * 
 * @author Mark Gottschling on Sep 13, 2014
 *
 */
public class CoinItem extends ModItem {

	public static final int MAX_STACK_SIZE = 8;
	private Coins coin;
	
	/**
	 * 
	 */
	public CoinItem (String modID, String name)	 {
		super();
		this.setItemName(modID, name);	
		this.setMaxStackSize(MAX_STACK_SIZE);
		this.setCreativeTab(Treasure.TREASURE_TAB);
		// set the coin to gold by default
		this.coin = Coins.GOLD;
	}
	
	/**
	 * 
	 * @param coin
	 */
	public CoinItem(String modID, String name, Coins coin) {
		this(modID, name);
		this.setCoin(coin);
	}
	
	/**
	 * @return the coin
	 */
	public Coins getCoin() {
		return coin;
	}
	/**
	 * @param coin the coin to set
	 */
	public CoinItem setCoin(Coins coin) {
		this.coin = coin;
		return this;
	}
}
