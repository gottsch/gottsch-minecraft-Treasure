package mod.gottsch.forge.treasure2.core.item.weapon;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.Multimap;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

/**
 * Base sword (Longsword = vanilla sword) class.
 * A duplicate of vanilla SwordItem except that it can take a float.
 * instead of an int for the speedModifier.
 * (Can't simply extend SwordItem because then SwordItem.super() would have to be called.
 * 
 * @author Mark Gottschling May 6, 2023
 *
 */
public class _Sword extends AbstractWeapon {
	   private final float attackDamage;
	   private final Multimap<Attribute, AttributeModifier> defaultModifiers;

	   public _Sword(Tier tier, float damageModifier, float speedModifier, Item.Properties properties) {
	      super(tier, properties);
	      this.attackDamage = damageModifier + tier.getAttackDamageBonus();
	      Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
	      builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", (double)this.attackDamage, AttributeModifier.Operation.ADDITION));
	      builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", (double)speedModifier, AttributeModifier.Operation.ADDITION));
	      this.defaultModifiers = builder.build();
	   }
	   
	   public float getDamage() {
		      return this.attackDamage;
		   }

		   public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
		      return !player.isCreative();
		   }

		   public float getDestroySpeed(ItemStack itemStack, BlockState state) {
		      if (state.is(Blocks.COBWEB)) {
		         return 15.0F;
		      } else {
		         Material material = state.getMaterial();
		         return material != Material.PLANT && material != Material.REPLACEABLE_PLANT && !state.is(BlockTags.LEAVES) && material != Material.VEGETABLE ? 1.0F : 1.5F;
		      }
		   }

		   public boolean hurtEnemy(ItemStack stack, LivingEntity p_43279_, LivingEntity p_43280_) {
		      stack.hurtAndBreak(1, p_43280_, (p_43296_) -> {
		         p_43296_.broadcastBreakEvent(EquipmentSlot.MAINHAND);
		      });
		      return true;
		   }

		   public boolean mineBlock(ItemStack p_43282_, Level p_43283_, BlockState p_43284_, BlockPos p_43285_, LivingEntity p_43286_) {
		      if (p_43284_.getDestroySpeed(p_43283_, p_43285_) != 0.0F) {
		         p_43282_.hurtAndBreak(2, p_43286_, (p_43276_) -> {
		            p_43276_.broadcastBreakEvent(EquipmentSlot.MAINHAND);
		         });
		      }

		      return true;
		   }

		   public boolean isCorrectToolForDrops(BlockState p_43298_) {
		      return p_43298_.is(Blocks.COBWEB);
		   }

		   public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
		      return slot == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(slot);
		   }

		   @Override
		   public boolean canPerformAction(ItemStack stack, net.minecraftforge.common.ToolAction toolAction) {
		      return net.minecraftforge.common.ToolActions.DEFAULT_SWORD_ACTIONS.contains(toolAction);
		   }
}
