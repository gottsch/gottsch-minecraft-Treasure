/*
 * This file is part of  Treasure2.
 * Copyright (c) 2022 Mark Gottschling (gottsch)
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Treasure2.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */
package mod.gottsch.forge.treasure2.core.material;

import java.util.EnumMap;
import java.util.function.Supplier;

import mod.gottsch.forge.treasure2.Treasure;
import net.minecraft.Util;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * 
 * @author Mark Gottschling on Sep 16, 2022
 *
 */
public enum TreasureArmorMaterial implements StringRepresentable, ArmorMaterial {

	PATCH("eye_patch", 5, Util.make(new EnumMap<>(ArmorItem.Type.class), 
			(protectionMap) -> {
				protectionMap.put(ArmorItem.Type.BOOTS, 1);
				protectionMap.put(ArmorItem.Type.LEGGINGS, 2);
				protectionMap.put(ArmorItem.Type.CHESTPLATE, 3);
				protectionMap.put(ArmorItem.Type.HELMET, 1);
			}), 
			15, SoundEvents.ARMOR_EQUIP_LEATHER, 0.0F, 0.0F, 
			() -> Ingredient.of(Items.IRON_INGOT)
		);

	public static final StringRepresentable.EnumCodec<TreasureArmorMaterial> CODEC = StringRepresentable.fromEnum(TreasureArmorMaterial::values);

	private static final EnumMap<ArmorItem.Type, Integer> HEALTH_FUNCTION_FOR_TYPE = Util.make(new EnumMap<>(ArmorItem.Type.class), (healthMap) -> {
		healthMap.put(ArmorItem.Type.BOOTS, 13);
		healthMap.put(ArmorItem.Type.LEGGINGS, 15);
		healthMap.put(ArmorItem.Type.CHESTPLATE, 16);
		healthMap.put(ArmorItem.Type.HELMET, 11);
	});

	private final String name;
	private final int durabilityMultiplier;
	private final EnumMap<ArmorItem.Type, Integer> protectionFunctionForType;
	private final int enchantmentValue;
	private final SoundEvent sound;
	private final float toughness;
	private final float knockbackResistance;
	private final Ingredient repairIngredient;

	private TreasureArmorMaterial(String name, int durability, EnumMap<ArmorItem.Type, Integer> protectionMap, int enchantment, 
			SoundEvent sound, float toughness, float knockbackResistance, Supplier<Ingredient> repair) {
		this.name = name;
		this.durabilityMultiplier = durability;
		this.protectionFunctionForType = protectionMap;
		this.enchantmentValue = enchantment;
		this.sound = sound;
		this.toughness = toughness;
		this.knockbackResistance = knockbackResistance;
		this.repairIngredient = repair.get();
	}

	@Override
	public int getDurabilityForType(ArmorItem.Type armorType) {
		return HEALTH_FUNCTION_FOR_TYPE.get(armorType) * this.durabilityMultiplier;
	}

	@Override
	public int getDefenseForType(ArmorItem.Type armorType) {
		return this.protectionFunctionForType.get(armorType);
	}

	@Override
	public int getEnchantmentValue() {
		return this.enchantmentValue;
	}

	@Override
	public SoundEvent getEquipSound() {
		return this.sound;
	}

	@Override
	public Ingredient getRepairIngredient() {
		return this.repairIngredient;
	}

	@OnlyIn(Dist.CLIENT)
	public String getName() {
		return Treasure.MODID + ":" + this.name;
	}

	@Override
	public float getToughness() {
		return this.toughness;
	}

	@Override
	public float getKnockbackResistance() {
		return this.knockbackResistance;
	}

	@Override
	public String getSerializedName() {
		return this.name;
	}
}
