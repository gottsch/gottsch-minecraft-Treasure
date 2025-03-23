/*
 * This file is part of  Treasure2.
 * Copyright (c) 2018 Mark Gottschling (gottsch)
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
/*
 * Error NTB Tag = Null
 * ArrayIndexOutOfBoundsException:
 * NullPointerException:
 * Fix - Patch
 */


package mod.gottsch.forge.treasure2.core.item;

import mod.gottsch.forge.gottschcore.enums.IRarity;
import mod.gottsch.forge.gottschcore.random.RandomHelper;
import mod.gottsch.forge.gottschcore.world.WorldInfo;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.block.AbstractTreasureChestBlock;
import mod.gottsch.forge.treasure2.core.block.ITreasureChestBlockProxy;
import mod.gottsch.forge.treasure2.core.block.entity.AbstractTreasureChestBlockEntity;
import mod.gottsch.forge.treasure2.core.block.entity.ITreasureChestBlockEntity;
import mod.gottsch.forge.treasure2.core.capability.DurabilityCapability;
import mod.gottsch.forge.treasure2.core.capability.IDurabilityHandler;
import mod.gottsch.forge.treasure2.core.capability.TreasureCapabilities;
import mod.gottsch.forge.treasure2.core.config.Config;
import mod.gottsch.forge.treasure2.core.enums.Rarity;
import mod.gottsch.forge.treasure2.core.item.effects.IKeyEffects;
import mod.gottsch.forge.treasure2.core.lock.LockState;
import mod.gottsch.forge.treasure2.core.registry.KeyLockRegistry;
import mod.gottsch.forge.treasure2.core.util.LangUtil;
import mod.gottsch.forge.treasure2.core.util.ModUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

import static mod.gottsch.forge.treasure2.core.capability.TreasureCapabilities.DURABILITY;

/**
 * 
 * KeyItem with patch to avoid NBT corruption crashes.
 *
 */
public class KeyItem extends Item implements IKeyEffects {
    public static final int DEFAULT_MAX_USES = 25;
    public static final String DURABILITY_TAG = "treasure2:durability";

    private KeyLockCategory category;
    private boolean craftable;
    private boolean breakable;
    private double successProbability;

    private List<Predicate<LockItem>> fitsLock;
    private List<Predicate<LockItem>> breaksLock;

    private int durability = Integer.MIN_VALUE;

    public KeyItem(Item.Properties properties) {
        this(properties, DEFAULT_MAX_USES);
    }

    public KeyItem(Item.Properties properties, int durability) {
        super(properties.defaultDurability(durability));
        setCategory(KeyLockCategory.ELEMENTAL);
        setBreakable(true);
        setCraftable(false);
        setSuccessProbability(90D);
        setDurability(durability);
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt) {
        DurabilityCapability provider = new DurabilityCapability();
        LazyOptional<IDurabilityHandler> cap = provider.getCapability(TreasureCapabilities.DURABILITY, null);
        cap.ifPresent(c -> {
            c.setDefaultDurability(this.getDurability());
        });
        return provider;
    }

    /**
     * PATCH: Envuelve en try/catch para evitar ArrayIndexOutOfBounds
     * si el CompoundTag interno está corrupto.
     */
    @Override
    public CompoundTag getShareTag(ItemStack stack) {
        try {
            super.getShareTag(stack);

            CompoundTag capabilityTag = null;
            IDurabilityHandler handler = stack.getCapability(DURABILITY).map(h -> h).orElse(null);
            if (handler != null) {
                capabilityTag = handler.save();
            }
            CompoundTag stackTag = stack.getOrCreateTag();
            if (capabilityTag != null) {
                stackTag.put(DURABILITY_TAG, capabilityTag);
            }
            return stackTag;
        } catch (Exception e) {
            Treasure.LOGGER.warn("KeyItem NBT corrupto — eliminando datos inválidos", e);
            return new CompoundTag(); // devuelvo un tag vacío para evitar crashear
        }
    }

    /**
     * PATCH: Comprueba si 'tag' es null antes de llamar 'tag.contains(...)'.
     */
    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundTag tag) {
        super.readShareTag(stack, tag);
        if (tag == null) {
            return; // evita NullPointerException
        }

        if (tag.contains(DURABILITY_TAG)) {
            IDurabilityHandler handler = stack.getCapability(DURABILITY).map(h -> h).orElse(null);
            if (handler != null) {
                handler.load(tag.get(DURABILITY_TAG));
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flag) {
        if (stack.getCapability(DURABILITY).isPresent()) {
            stack.getCapability(DURABILITY).ifPresent(cap -> {
                if (cap.isInfinite()) {
                    tooltip.add(Component.translatable(LangUtil.tooltip("cap.durability.amount.infinite")));
                } else {
                    tooltip.add(Component.translatable(
                        LangUtil.tooltip("cap.durability.amount"),
                        cap.durability(stack.getItem()) - stack.getDamageValue(),
                        cap.durability(stack.getItem())
                    ));
                }
            });
        } else {
            tooltip.add(Component.translatable(
                LangUtil.tooltip("cap.durability.amount"),
                "whaat",
                getDurability()
            ));
        }

        tooltip.add(Component.translatable(LangUtil.tooltip("key_lock.rarity"),
                ChatFormatting.BLUE + Component.translatable(getRarity().getValue().toLowerCase()).getString().toUpperCase()));
        tooltip.add(Component.translatable(LangUtil.tooltip("key_lock.category"),
                ChatFormatting.GOLD + Component.translatable(getCategory().toString().toLowerCase()).getString().toUpperCase()));

        LangUtil.appendAdvancedHoverText(tooltip, tt -> {
            MutableComponent breakableComp;
            if (isBreakable()) {
                breakableComp = Component.translatable(LangUtil.tooltip("boolean.yes")).withStyle(ChatFormatting.DARK_RED);
            } else {
                breakableComp = Component.translatable(LangUtil.tooltip("boolean.no")).withStyle(ChatFormatting.GREEN);
            }
            tooltip.add(Component.translatable(LangUtil.tooltip("key_lock.breakable"), breakableComp));

            MutableComponent craftableComp;
            if (isCraftable()) {
                craftableComp = Component.translatable(LangUtil.tooltip("boolean.yes")).withStyle(ChatFormatting.GREEN);
            } else {
                craftableComp = Component.translatable(LangUtil.tooltip("boolean.no")).withStyle(ChatFormatting.DARK_RED);
            }
            tooltip.add(Component.translatable(LangUtil.tooltip("key_lock.craftable"), craftableComp));

            appendHoverSpecials(stack, worldIn, tooltip, flag);
            appendHoverExtras(stack, worldIn, tooltip, flag);
        });
        appendCurse(stack, tooltip);
    }

    public void appendCurse(ItemStack stack, List<Component> tooltip) {}

    public void appendHoverSpecials(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flag) {}
    public void appendHoverExtras(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flag) {}

    @Override
    public int getBarWidth(ItemStack stack) {
        return stack.getCapability(DURABILITY).map(handler -> {
            float ratio = 13.0F - (float) stack.getDamageValue() * 13.0F / (float) handler.durability(stack.getItem());
            return Math.round(ratio);
        }).orElse(Math.round(13.0F - (float) stack.getDamageValue() * 13.0F / (float) this.getDurability()));
    }

    @Override
    public int getBarColor(ItemStack stack) {
        float stackMaxDamage = stack.getCapability(DURABILITY)
                                    .map(handler -> (float) handler.durability(stack.getItem()))
                                    .orElse((float) this.getDurability());
        float f = Math.max(0.0F, (stackMaxDamage - stack.getDamageValue()) / stackMaxDamage);
        return Mth.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
    }

    @Override
    public boolean isValidRepairItem(ItemStack itemToRepair, ItemStack resourceItem) {
        return resourceItem.getItem() == this || super.isValidRepairItem(itemToRepair, resourceItem);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (WorldInfo.isClientSide(context.getLevel())) {
            return InteractionResult.FAIL;
        }

        BlockPos chestPos = context.getClickedPos();
        BlockState state = context.getLevel().getBlockState(chestPos);
        Block block = state.getBlock();

        if (block instanceof ITreasureChestBlockProxy) {
            chestPos = ((ITreasureChestBlockProxy) block).getChestPos(chestPos);
            state = context.getLevel().getBlockState(chestPos);
            block = state.getBlock();
        }

        if (block instanceof AbstractTreasureChestBlock) {
            BlockEntity blockEntity = context.getLevel().getBlockEntity(chestPos);
            if (blockEntity == null || !(blockEntity instanceof ITreasureChestBlockEntity)) {
                Treasure.LOGGER.warn("null or incorrect blockEntity");
                return InteractionResult.FAIL;
            }
            ITreasureChestBlockEntity chestBlockEntity = (ITreasureChestBlockEntity) blockEntity;

            if (!chestBlockEntity.hasLocks()) {
                return InteractionResult.SUCCESS;
            }

            try {
                ItemStack heldItemStack = context.getPlayer().getItemInHand(context.getHand());
                boolean breakKey = true;
                boolean fitsLock = false;
                LockState lockState = null;
                boolean isKeyBroken = false;

                lockState = fitsFirstLock(chestBlockEntity.getLockStates());
                if (lockState != null) {
                    fitsLock = true;
                }

                if (fitsLock && unlock(lockState.getLock())) {
                    doUnlock(context, chestBlockEntity, lockState);
                    if (!state.getValue(AbstractTreasureChestBlock.DISCOVERED)) {
                        chestBlockEntity = ((AbstractTreasureChestBlock) block).discovered(
                            (AbstractTreasureChestBlockEntity) chestBlockEntity,
                            state,
                            context.getLevel(),
                            chestPos,
                            context.getPlayer()
                        );
                    }
                    chestBlockEntity.sendUpdates();
                    breakKey = false;
                }

                IDurabilityHandler cap = heldItemStack.getCapability(DURABILITY).orElseThrow(IllegalStateException::new);
                if (breakKey) {
                    Treasure.LOGGER.debug("breakKey -> {}", breakKey);
                    if (!context.getPlayer().isCreative() &&
                        (isBreakable() || anyLockBreaksKey(chestBlockEntity.getLockStates(), this)) &&
                        Config.SERVER.keysAndLocks.enableKeyBreaks.get()) {

                        Treasure.LOGGER.debug("is breakable -> {}", isBreakable());
                        int durability = getDurability();
                        int damage = heldItemStack.getDamageValue() + (durability - (heldItemStack.getDamageValue() % durability));
                        heldItemStack.setDamageValue(damage);
                        Treasure.LOGGER.debug("damaging key -> {}", heldItemStack.getDamageValue());
                        if (heldItemStack.getDamageValue() >= cap.durability(heldItemStack.getItem())) {
                            heldItemStack.shrink(1);
                        }
                        doKeyBreakEffects(context.getLevel(), context.getPlayer(), chestPos);
                        isKeyBroken = true;
                    } else if (!fitsLock) {
                        doKeyNotFitEffects(context.getLevel(), context.getPlayer(), chestPos);
                    } else {
                        doKeyUnableToUnlockEffects(context.getLevel(), context.getPlayer(), chestPos);
                    }
                }

                if (!context.getPlayer().isCreative() && isDamageable(heldItemStack) && !isKeyBroken) {
                    heldItemStack.setDamageValue(heldItemStack.getDamageValue() + 1);
                    Treasure.LOGGER.debug("damaging key -> {}", heldItemStack.getDamageValue());
                    if (heldItemStack.getDamageValue() >= cap.durability(heldItemStack.getItem())) {
                        heldItemStack.shrink(1);
                    }
                }
            } catch (Exception e) {
                Treasure.LOGGER.error("error: ", e);
            }
        }
        return super.useOn(context);
    }

    public void doUnlock(UseOnContext context, ITreasureChestBlockEntity chestTileEntity, LockState lockState) {
        LockItem lock = lockState.getLock();
        lock.doUnlock(context.getLevel(), context.getPlayer(), context.getClickedPos(), lockState);

        if (!breaksLock(lock)) {
            lock.dropLock(context.getLevel(), context.getClickedPos());
        }
    }

    public boolean fitsLock(LockItem lockItem) {
        if (getFitsLock() == null || getFitsLock().isEmpty()) {
            return false;
        }
        for (Predicate<LockItem> p : this.getFitsLock()) {
            boolean result = p.test(lockItem);
            if (!result) {
                return false;
            }
        }
        return true;
    }

    public LockState fitsFirstLock(List<LockState> lockStates) {
        LockState lockState = null;
        for (LockState ls : lockStates) {
            if (ls.getLock() != null) {
                lockState = ls;
                if (lockState.getLock().acceptsKey(this) || fitsLock(lockState.getLock())) {
                    return ls;
                }
            }
        }
        return null;
    }

    public boolean unlock(LockItem lockItem) {
        if (lockItem.acceptsKey(this) || fitsLock(lockItem)) {
            Treasure.LOGGER.debug("lock -> {} accepts key -> {}", ModUtil.getName(lockItem), ModUtil.getName(this));
            if (RandomHelper.checkProbability(new Random(), this.getSuccessProbability())) {
                Treasure.LOGGER.debug("unlock attempt met probability");
                return true;
            }
        }
        return false;
    }

    public boolean breaksLock(LockItem lockItem) {
        if (getBreaksLock() == null || getBreaksLock().isEmpty()) {
            return false;
        }
        for (Predicate<LockItem> p : this.getBreaksLock()) {
            boolean result = p.test(lockItem);
            if (!result) {
                return false;
            }
        }
        return true;
    }

    public boolean anyLockBreaksKey(List<LockState> lockStates, KeyItem key) {
        for (LockState ls : lockStates) {
            if (ls.getLock() != null && ls.getLock().breaksKey(key)) {
                return true;
            }
        }
        return false;
    }

    public IRarity getRarity() {
        IRarity rarity = KeyLockRegistry.getRarityByKey(this);
        if (rarity == null) {
            return Rarity.NONE;
        }
        return rarity;
    }

    public boolean isCraftable() {
        return craftable;
    }

    public KeyItem setCraftable(boolean craftable) {
        this.craftable = craftable;
        return this;
    }

    @Override
    public String toString() {
        return "KeyItem [rarity=" + getRarity() + ", craftable=" + craftable + "]";
    }

    public KeyLockCategory getCategory() {
        return category;
    }

    public KeyItem setCategory(KeyLockCategory category) {
        this.category = category;
        return this;
    }

    public boolean isBreakable() {
        return breakable;
    }

    public KeyItem setBreakable(boolean breakable) {
        this.breakable = breakable;
        return this;
    }

    public KeyItem addFitsLock(Predicate<LockItem> p) {
        if (fitsLock == null) {
            fitsLock = new ArrayList<>();
        }
        fitsLock.add(p);
        return this;
    }

    public List<Predicate<LockItem>> getFitsLock() {
        return this.fitsLock;
    }

    public KeyItem addBreaksLock(Predicate<LockItem> p) {
        if (breaksLock == null) {
            breaksLock = new ArrayList<>();
        }
        breaksLock.add(p);
        return this;
    }

    public List<Predicate<LockItem>> getBreaksLock() {
        return breaksLock;
    }

    public double getSuccessProbability() {
        return successProbability;
    }

    public KeyItem setSuccessProbability(double successProbability) {
        this.successProbability = successProbability;
        return this;
    }

    /**
     * Convenience method
     */
    public boolean isDamageable(ItemStack stack) {
        if (stack == null) {
            return super.isDamageable(stack);
        }
        IDurabilityHandler handler = stack.getCapability(TreasureCapabilities.DURABILITY).orElse(null);
        if (handler != null) {
            return !handler.isInfinite();
        }
        return true;
    }

    public int getDurability() {
        return durability;
    }

    public void setDurability(int durability) {
        this.durability = durability;
    }
}

