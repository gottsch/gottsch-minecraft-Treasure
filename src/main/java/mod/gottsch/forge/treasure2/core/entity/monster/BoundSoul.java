/**
 * 
 */
package mod.gottsch.forge.treasure2.core.entity.monster;

import java.util.Random;

import mod.gottsch.forge.gottschcore.random.RandomHelper;
import mod.gottsch.forge.gottschcore.world.WorldInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FleeSunGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RestrictSunGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biome.BiomeCategory;

// TODO update with Bound Soul AI
/**
 * NOTE: 	mob.level.getBrightness(LightLayer.BLOCK, pos), mob.level.getMaxLocalRawBrightness(pos));
 * @author Mark Gottschling on Apr 12, 2022
 *
 */
public class BoundSoul extends Monster {

	private static final int NORMAL_DIFFICULTY_SECONDS = 10;
	private static final int HARD_DIFFICULTY_SECONDS = 20;
	private static final int WEAKNESS_SECONDS = 5;

	private boolean flee;

	/**
	 * 
	 * @param entityType
	 * @param level
	 */
	public BoundSoul(EntityType<? extends Monster> entityType, Level level) {
		super(entityType, level);
	}

	protected void registerGoals() {
		this.goalSelector.addGoal(2, new RestrictSunGoal(this));
		this.goalSelector.addGoal(3, new FleeSunGoal(this, 1.0D));
		this.goalSelector.addGoal(3,  new ShadowFleeGoal<>(this, Player.class, 6.0F, 1.2D, 1.2D));
		this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, false));
		this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));

		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
	}

	/**
	 * 
	 * @return
	 */
	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes()
				.add(Attributes.ATTACK_DAMAGE, 2.0D)
				.add(Attributes.MAX_HEALTH)
				.add(Attributes.KNOCKBACK_RESISTANCE, 1.0)
				.add(Attributes.MOVEMENT_SPEED, 0.28F);                
	}

	public MobType getMobType() {
		return MobType.UNDEAD;
	}
	
	@Override
	public void aiStep() {
		// set on fire if in sun
		boolean flag = this.isSunBurnTick();
		if (flag) {
			this.setSecondsOnFire(4);
		}
		super.aiStep();
	}

	// TODO change difficulty seconds to ticks pulled in from Config
	@Override
	public boolean doHurtTarget(Entity target) {
		if (super.doHurtTarget(target)) {
			if (target instanceof Player) {
				int seconds = 0;
				if (this.level.getDifficulty() == Difficulty.NORMAL) {
					seconds = NORMAL_DIFFICULTY_SECONDS;
				} else if (this.level.getDifficulty() == Difficulty.HARD) {
					seconds = HARD_DIFFICULTY_SECONDS;
				}
				// inflict blindness and/or weakness
				ItemStack helmetStack = ((Player)target).getItemBySlot(EquipmentSlot.HEAD);
				if (helmetStack.isEmpty() || helmetStack.getItem() != Items.GOLDEN_HELMET) {
					if (RandomHelper.checkProbability(random, 90)) {
						if (seconds > 0) {
							((LivingEntity)target).addEffect(new MobEffectInstance(MobEffects.BLINDNESS, seconds * 20, 0), this);
						}
					}
				}
				if (RandomHelper.checkProbability(random, 20)) {
					if (seconds > 0) {
						((LivingEntity)target).addEffect(new MobEffectInstance(MobEffects.WEAKNESS, WEAKNESS_SECONDS * 20, 0), this);
					}
				}
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 */
	@Override
	public boolean hurt(DamageSource damageSource, float amount) {
		if (WorldInfo.isClientSide(this.level)) {
			return false;
		}
		
		this.flee = true;
		
		if (damageSource.getEntity() != null && damageSource.getEntity() instanceof Player) {
			ItemStack heldStack = ((Player)damageSource.getEntity()).getItemInHand(InteractionHand.MAIN_HAND);
			if (!heldStack.isEmpty() && heldStack.getItem() == Items.GOLDEN_SWORD) {
				// increase damage to that of iron sword
				amount += 2.0F;
				// negate the weakness from the strike power of the sword
				if (((Player)damageSource.getEntity()).hasEffect(MobEffects.WEAKNESS)) {
					amount += MobEffects.WEAKNESS.getAttributeModifierValue(0, null);
				}
			}
			// TODO add shadow sword condition
			else {
				amount = 1.0F;
			}
		}
		return super.hurt(damageSource, amount);
	}

	/**
	 * 
	 * @author Mark Gottschling on Apr 13, 2022
	 *
	 * @param <T>
	 */
	public static class ShadowFleeGoal<T extends LivingEntity> extends AvoidEntityGoal<T> {

		/**
		 * 
		 * @param mob
		 * @param target
		 * @param maxDistance
		 * @param walkSpeedModifier
		 * @param sprintSpeedModifier
		 */
		public ShadowFleeGoal(PathfinderMob mob, Class<T> target, float maxDistance, double walkSpeedModifier,
				double sprintSpeedModifier) {
			super(mob, target, maxDistance, walkSpeedModifier, sprintSpeedModifier);

		}

		@Override
		public boolean canUse() {
			if (((BoundSoul)mob).flee) {
				if(super.canUse()) {
					return true;
				}
			}
			return false;
		}

		@Override
		public void stop() {
			((BoundSoul)mob).flee = false;
			this.toAvoid = null;
		}

		@Override
		public boolean canContinueToUse() {
			return !this.pathNav.isDone();
		}
	}
}
