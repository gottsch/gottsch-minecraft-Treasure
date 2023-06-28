package mod.gottsch.forge.treasure2.core.item.weapon;

import java.util.List;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.Multimap;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

/**
 * This wrapper class allows float values for damage.
 * @author Mark Gottschling May 8, 2023
 *
 */
public class Sword extends SwordItem implements IWeapon {
	private float criticalChance;
	private float criticalDamage;
	
	/*
		MC 1.18.2: net/minecraft/world/item/SwordItem.defaultModifiers
		Name: b => f_43267_ => defaultModifiers
		Side: BOTH
		AT: public net.minecraft.world.item.SwordItem f_43267_ # defaultModifiers
		Type: com/google/common/collect/Multimap
	 */
	private static final String DEFAULT_MODIFIERS_SRG_NAME = "f_43267_";

	/**
	 * 
	 * @param tier
	 * @param damageModifier
	 * @param speedModifier
	 * @param properties
	 */
	public Sword(Tier tier, float damageModifier, float speedModifier, Item.Properties properties) {
		this(tier, damageModifier, speedModifier, 0f, 0f, properties);
	}
	
	/**
	 * 
	 * @param tier
	 * @param damageModifier
	 * @param speedModifier
	 * @param criticalChance
	 * @param criticalDamage
	 * @param properties
	 */
	public Sword(Tier tier, float damageModifier, float speedModifier, float criticalChance, float criticalDamage, Item.Properties properties) {
		super(tier, (int)Math.floor(damageModifier), speedModifier, properties);

		this.criticalChance = criticalChance;
		this.criticalDamage = criticalDamage;
		
		// override the parent class modifiers		
		Object reflectDefaultModifiers = ObfuscationReflectionHelper.getPrivateValue(SwordItem.class, this, DEFAULT_MODIFIERS_SRG_NAME);
		if (reflectDefaultModifiers instanceof Multimap) {
			float attackDamage =damageModifier + tier.getAttackDamageBonus();
			Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
			builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", (double)attackDamage, AttributeModifier.Operation.ADDITION));
			builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", (double)speedModifier, AttributeModifier.Operation.ADDITION));
			reflectDefaultModifiers = builder.build();
		}
	}

	@Override
	public Component getName(ItemStack itemStack) {
		if (isUnique()) {
			return Component.translatable(this.getDescriptionId(itemStack)).withStyle(ChatFormatting.YELLOW);
		} else {
			return Component.translatable(this.getDescriptionId(itemStack));
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flag) {
		appendStats(stack, worldIn, tooltip, flag);
		appendHoverExtras(stack, worldIn, tooltip, flag);
	}

	public float getCriticalChance() {
		return criticalChance;
	}

	public void setCriticalChance(float criticalChance) {
		this.criticalChance = criticalChance;
	}

	public float getCriticalDamage() {
		return criticalDamage;
	}

	public void setCriticalDamage(float criticalDamage) {
		this.criticalDamage = criticalDamage;
	}
}
