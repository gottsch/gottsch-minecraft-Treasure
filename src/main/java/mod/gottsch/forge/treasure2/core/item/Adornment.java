/* 
 * This file is part of  Treasure2.
 * Copyright (c) 2021, Mark Gottschling (gottsch)
 * 
 * All rights reserved.
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
package mod.gottsch.forge.treasure2.core.item;

import java.util.List;
import java.util.Optional;

import com.someguyssoftware.gottschcore.item.ModItem;

import mod.gottsch.forge.treasure2.core.Treasure;
import mod.gottsch.forge.treasure2.core.adornment.AdornmentSize;
import mod.gottsch.forge.treasure2.core.adornment.TreasureAdornmentRegistry;
import mod.gottsch.forge.treasure2.core.capability.AdornmentCapabilityProvider;
import mod.gottsch.forge.treasure2.core.capability.CharmableCapabilityStorage;
import mod.gottsch.forge.treasure2.core.capability.DurabilityCapabilityStorage;
import mod.gottsch.forge.treasure2.core.capability.ICharmableCapability;
import mod.gottsch.forge.treasure2.core.capability.IDurabilityCapability;
import mod.gottsch.forge.treasure2.core.capability.IRunestonesCapability;
import mod.gottsch.forge.treasure2.core.capability.RunestonesCapabilityStorage;
import mod.gottsch.forge.treasure2.core.capability.TreasureCapabilities;
import mod.gottsch.forge.treasure2.core.enums.AdornmentType;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

/**
 * @author Mark Gottschling on Dec 20, 2020
 *
 */
public class Adornment extends ModItem {
	private static final CharmableCapabilityStorage CAPABILITY_STORAGE = new CharmableCapabilityStorage();
	private static final RunestonesCapabilityStorage RUNESTONES_STORAGE = new RunestonesCapabilityStorage();
	private static final DurabilityCapabilityStorage DURABILITY_STORAGE = new DurabilityCapabilityStorage();


	private AdornmentType type;
	private AdornmentSize size;

	public Adornment(AdornmentType type, Item.Properties properties) {
		this(type, TreasureAdornmentRegistry.STANDARD, properties);
	}

	public Adornment(AdornmentType type, AdornmentSize size, Item.Properties properties) {
		super(properties.tab(TreasureItemGroups.ADORNMENTS_TAB).stacksTo(1));
		setType(type);
		setSize(size);
	}

	/**
	 * 
	 * @param modID
	 * @param name
	 * @param type
	 */
	@Deprecated
	public Adornment(String modID, String name, AdornmentType type, Item.Properties properties) {
		this(modID, name, type, TreasureAdornmentRegistry.STANDARD, properties);
	}
	/**
	 * 
	 * @param modID
	 * @param name
	 * @param type
	 * @param size
	 */
	@Deprecated
	public Adornment(String modID, String name, AdornmentType type, AdornmentSize size, Item.Properties properties) {
		super(modID, name, properties.tab(TreasureItemGroups.ADORNMENTS_TAB).stacksTo(1));
		setType(type);
		setSize(size);
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
		return new AdornmentCapabilityProvider();
	}

	/**
	 * Convenience method.
	 * @param stack
	 * @return
	 */
	public Optional<ICharmableCapability> getCap(ItemStack stack) {
		return stack.getCapability(TreasureCapabilities.CHARMABLE).resolve();
	}

	public Optional<IDurabilityCapability> getDurabilityCap(ItemStack stack) {
		return stack.getCapability(TreasureCapabilities.DURABILITY).resolve();
	}

	public Optional<IRunestonesCapability> getRunestonesCap(ItemStack stack) {
		return stack.getCapability(TreasureCapabilities.RUNESTONES).resolve();
	}

	/**
	 * 
	 */
	@Override
	public boolean isFoil(ItemStack stack) {
		Optional<ICharmableCapability> cap = getCap(stack);
		if (cap.isPresent() && cap.get().isCharmed()) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void appendHoverText(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
		super.appendHoverText(stack, world, tooltip, flag);
		tooltip.add(new TranslationTextComponent("tooltip.charmable.usage.adornment").withStyle(TextFormatting.GOLD).withStyle(TextFormatting.ITALIC));

		// add the durability tooltips
		stack.getCapability(TreasureCapabilities.DURABILITY).ifPresent(cap -> {
			cap.appendHoverText(stack, world, tooltip, flag);
		});

		// add charmables tooltips
		stack.getCapability(TreasureCapabilities.CHARMABLE).ifPresent(cap -> {
			cap.appendHoverText(stack, world, tooltip, flag);
		});		

		// add runestones tooltips
		stack.getCapability(TreasureCapabilities.RUNESTONES).ifPresent(cap -> {
			cap.appendHoverText(stack, world, tooltip, flag);
		});

		stack.getCapability(TreasureCapabilities.CHARMABLE).ifPresent(cap -> {
			if (cap.getSourceItem() == Items.AIR.getRegistryName() || cap.getSourceItem() == null) {
				tooltip.add(new TranslationTextComponent("tooltip.adornment.upgradable").withStyle(TextFormatting.WHITE).withStyle(TextFormatting.ITALIC));
			}
		});
	}

	/**
	 * NOTE getNBTShareTag() and readNBTShareTag() are required to sync item capabilities server -> client. I needed this when holding charms in hands and then swapping hands
	 * or having the client update when the Anvil GUI is open.
	 */
	@Override
	public CompoundNBT getShareTag(ItemStack stack) {
		//		Treasure.logger.debug("writing share tag");
		// read cap -> write nbt
		CompoundNBT charmableTag;
		charmableTag = (CompoundNBT) CAPABILITY_STORAGE.writeNBT(
				TreasureCapabilities.CHARMABLE,
				getCap(stack).get(),
				null);
		CompoundNBT runestonesTag;
		runestonesTag = (CompoundNBT) RUNESTONES_STORAGE.writeNBT(
				TreasureCapabilities.RUNESTONES,
				getRunestonesCap(stack).get(),
				null);		
		CompoundNBT durabilityTag = (CompoundNBT) DURABILITY_STORAGE.writeNBT(
				TreasureCapabilities.DURABILITY,
				getDurabilityCap(stack).get(),
				null);

		CompoundNBT tag = new CompoundNBT();
		tag.put("charmable", charmableTag);
		tag.put("runestones", runestonesTag);
		tag.put("durability", durabilityTag);

		return tag;
	}

	@Override
	public void readShareTag(ItemStack stack, CompoundNBT nbt) {
		super.readShareTag(stack, nbt);
		Treasure.LOGGER.debug("reading share tag");
		if (nbt == null) {
			Treasure.LOGGER.debug("nbt is null - how?");
			return;
		}
		// read nbt -> write key item
		if (nbt.contains("charmable")) {
			CompoundNBT tag = nbt.getCompound("charmable");
			CAPABILITY_STORAGE.readNBT(
					TreasureCapabilities.CHARMABLE, 
					getCap(stack).get(), 
					null,
					tag);
		}
		if (nbt.contains("runestones")) {
			CompoundNBT tag = nbt.getCompound("runestones");
			RUNESTONES_STORAGE.readNBT(
					TreasureCapabilities.RUNESTONES, 
					getRunestonesCap(stack).get(), 
					null,
					tag);
		}
		if (nbt.contains("durability")) {
			CompoundNBT tag = nbt.getCompound("durability");
			DURABILITY_STORAGE.readNBT(
					TreasureCapabilities.DURABILITY, 
					getDurabilityCap(stack).get(), 
					null,
					tag);
		}
	}

	/**
	 * Queries the percentage of the 'Durability' bar that should be drawn.
	 *
	 * @param stack The current ItemStack
	 * @return 0.0 for 100% (no damage / full bar), 1.0 for 0% (fully damaged / empty bar)
	 */
	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		if (stack.getCapability(TreasureCapabilities.DURABILITY).isPresent()) {
			IDurabilityCapability cap = getDurabilityCap(stack).get();
			if (cap.isInfinite()) {
				return 0D;
			}
			else {
				return (double)stack.getDamageValue() / (double) cap.getDurability();
			}
		}
		else {
			return (double)stack.getDamageValue() / (double)stack.getMaxDamage();
		}
	}

	/**
	 * Adornments are not repairable using vanilla methods.
	 */	
	@Override
	public boolean isRepairable(ItemStack stack) {
		return false;
	}

	/**
	 * Adornments are not repairable using vanilla methods.
	 */
	@Override
	public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
		return false;
	}

	public AdornmentType getType() {
		return type;
	};

	private void setType(AdornmentType type) {
		this.type = type;
	}

	public AdornmentSize getSize() {
		return size;
	}

	private void setSize(AdornmentSize size) {
		this.size = size;
	}
}
