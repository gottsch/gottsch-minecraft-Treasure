/**
 * 
 */
package com.someguyssoftware.treasure2.loot;

import net.minecraft.world.storage.loot.RandomValueRange;

/**
 * @author Mark Gottschling on Dec 1, 2020
 *
 */
public class LootPoolShell {
    private RandomValueRange rolls;
    private RandomValueRange bonusRolls;
    private String name;
    
    public LootPoolShell() {}
    
	public RandomValueRange getRolls() {
		return rolls;
	}
	public void setRolls(RandomValueRange rolls) {
		this.rolls = rolls;
	}
	public RandomValueRange getBonusRolls() {
		return bonusRolls;
	}
	public void setBonusRolls(RandomValueRange bonusRolls) {
		this.bonusRolls = bonusRolls;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "LootPoolShell [rolls=" + rolls + ", bonusRolls=" + bonusRolls + ", name=" + name + "]";
	}
}
