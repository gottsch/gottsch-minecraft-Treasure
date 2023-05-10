package mod.gottsch.forge.treasure2.core.item.weapon;

import java.util.List;
import java.util.UUID;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

/**
 * 
 * @author Mark Gottschling May 8, 2023
 *
 */
public class Sword extends SwordItem {
	protected static final UUID EXTRA_ATTACK_DAMAGE_UUID = UUID.nameUUIDFromBytes("treasure2:extra_attack_damage".getBytes());
	
	/**
	 * 
	 * @param tier
	 * @param damageModifier
	 * @param speedModifier
	 * @param properties
	 */
	public Sword(Tier tier, float damageModifier, float speedModifier, Item.Properties properties) {
		super(tier, (int)Math.floor(damageModifier), speedModifier, properties);
		// override the attack damage
		
		// TODO will have to use reflection to get access to the original attributeModifiers and rebuild
		if (damageModifier != getDamage()) {
			float remainderAttackDamage = (damageModifier - getDamage())+ tier.getAttackDamageBonus();
	
			getDefaultAttributeModifiers(EquipmentSlot.MAINHAND)
				.get(Attributes.ATTACK_DAMAGE)
				.add(new AttributeModifier(EXTRA_ATTACK_DAMAGE_UUID, "Extra Weapon modifier", (double)remainderAttackDamage, AttributeModifier.Operation.ADDITION));
		}
	}

	@Override
	public Component getName(ItemStack itemStack) {
		if (isUnique()) {
			return new TranslatableComponent(this.getDescriptionId(itemStack)).withStyle(ChatFormatting.YELLOW);
		} else {
			return new TranslatableComponent(this.getDescriptionId(itemStack));
		}
	}
	
	/**
	 * Determines whether the weapon is "unique" or named. ex. The Black Sword, The
	 * Sword of Omens.
	 * 
	 * @return
	 */
	public boolean isUnique() {
		return false;
	}
	
	@Override
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flag) {
		appendHoverExtras(stack, worldIn, tooltip, flag);
	}
	
	/**
	 * 
	 * @param stack
	 * @param level
	 * @param tooltip
	 * @param flag
	 */
	public  void appendHoverExtras(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
		
	}
}
