/*
 * This file is part of Treasure2.
 * Copyright (c) 2025 Mark Gottschling (gottsch)
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the Open Software Licence 3.0.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Open Software Licence 3.0 for more details.
 *
 * You should have received a copy of the Open Software Licence
 * along with Treasure2. If not, see <https://www.tldrlegal.com/license/open-software-licence-3-0>.
 */
package mod.gottsch.forge.treasure2.core.item;

import mod.gottsch.forge.treasure2.core.rarity.TreasureRarities;
import mod.gottsch.forge.treasure2.core.registry.RarityTagAssociationRegistry;
import mod.gottsch.forge.treasure2.core.util.LangUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

/**
 * @author Mark Gottschling on Feb 2, 2018
 */
public class SkeletonKey extends KeyItem {

    public SkeletonKey(Item.Properties properties) {
        this(properties, DEFAULT_MAX_USES);
    }

    /**
     * @param properties
     */
    public SkeletonKey(Item.Properties properties, int durability) {
        super(properties, durability);

        // add the default fitsLock predicates
        addFitsLock((level, lock) -> {
            return RarityTagAssociationRegistry.getLockRarity(lock, level.registryAccess())
                    .map(rarity ->
                            lock.getCategory() != KeyLockCategory.WITHER
                                    &&
                                    (rarity ==TreasureRarities.COMMON.get()
                                            || rarity == TreasureRarities.UNCOMMON.get()
                                            || rarity == TreasureRarities.SCARCE.get()
                                            || rarity == TreasureRarities.RARE.get())
                    )
                    .orElse(false);
        });
    }

    /**
     * Format: (Additions)
     * <p>
     * Specials: [text] [color=gold]
     */
    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public void appendHoverSpecials(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable(LangUtil.tooltip("key_lock.specials")));

        Component specials = Component.translatable(LangUtil.tooltip("key_lock.skeleton_key.specials"));
        for (String s : specials.getString().split("~")) {
            tooltip.add(Component.literal(LangUtil.INDENT2)
                    .append(Component.literal(s).withStyle(ChatFormatting.GOLD, ChatFormatting.ITALIC)));
        }
    }
}
