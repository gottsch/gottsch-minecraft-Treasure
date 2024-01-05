/**
 * 
 */
package mod.gottsch.forge.treasure2.core.item;

import java.util.function.Supplier;

import net.minecraft.item.IItemTier;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;

/**
 * @author Mark Gottschling on Jan 15, 2021
 *
 */
public enum TreasureItemTier implements IItemTier {
	SKULL(2, 1800, 9.0F, 4.0F, 15, () -> {
		return Ingredient.of(Items.SKELETON_SKULL);
	});

	private final int level;
	private final int uses;
	private final float speed;
	private final float attackDamageBonus;
	private final int enchantmentValue;
	private final LazyValue<Ingredient> repairIngredient;

	private TreasureItemTier(int harvestLevelIn, int maxUsesIn, float speedIn, float attackDamageBonusIn, int enchantmentValueIn, Supplier<Ingredient> repairIngredientIn) {
		this.level = harvestLevelIn;
		this.uses = maxUsesIn;
		this.speed = speedIn;
		this.attackDamageBonus = attackDamageBonusIn;
		this.enchantmentValue = enchantmentValueIn;
		this.repairIngredient = new LazyValue<>(repairIngredientIn);
	}

	@Override
	public int getUses() {
		return uses;
	}

	@Override
	public float getSpeed() {
		return speed;
	}

	@Override
	public float getAttackDamageBonus() {
		return attackDamageBonus;
	}

	@Override
	public int getLevel() {
		return level;
	}

	@Override
	public int getEnchantmentValue() {
		return enchantmentValue;
	}

	@Override
	public Ingredient getRepairIngredient() {
		return this.repairIngredient.get();
	}

}
