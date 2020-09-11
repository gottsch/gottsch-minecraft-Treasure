/**
 * 
 */
package com.someguyssoftware.treasure2.item;

import java.util.List;
import java.util.Random;

import static com.someguyssoftware.treasure2.Treasure.logger;
import com.someguyssoftware.gottschcore.item.ModItem;
import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.AbstractChestBlock;
import com.someguyssoftware.treasure2.block.ITreasureChestProxy;
import com.someguyssoftware.treasure2.capability.EffectiveMaxDamageCapability;
import com.someguyssoftware.treasure2.capability.EffectiveMaxDamageCapabilityProvider;
import com.someguyssoftware.treasure2.capability.IEffectiveMaxDamageCapability;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.enums.Category;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.lock.LockState;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

/**
 * 
 * @author Mark Gottschling on Jan 11, 2018
 *
 */
public class KeyItem extends ModItem {
	public static final int DEFAULT_MAX_USES = 25;
	
	/*
	 * The category that the key belongs to
	 */
	private Category category;
	
	/*
	 * The rarity of the key
	 */
	private Rarity rarity;	

	/*
	 * Is the key craftable
	 */
	private boolean craftable;
	
	/*
	 * Can the key break attempting to unlock a lock
	 */
	private boolean breakable;

	/*
	 * Can the key take damage and lose durability
	 */
	private boolean damageable;
	
	/*
	 * The probability of a successful unlocking
	 */
	private double successProbability;
	
	/**
	 * 
	 * @param modID
	 * @param name
	 */
	public KeyItem(String modID, String name) {
		setItemName(modID, name);
		setCreativeTab(Treasure.TREASURE_TAB);
		setCategory(Category.BASIC);
		setRarity(Rarity.COMMON);
		setBreakable(true);
		setDamageable(true);
		setCraftable(false);
		setMaxDamage(DEFAULT_MAX_USES);
		setSuccessProbability(95D);	
		setMaxStackSize(1); // 12/3/2018: set to max 1 because keys are damaged and don't stack well.
	}

    @Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
        EffectiveMaxDamageCapabilityProvider provider = new EffectiveMaxDamageCapabilityProvider();
        IEffectiveMaxDamageCapability cap = provider.getCapability(EffectiveMaxDamageCapabilityProvider.EFFECTIVE_MAX_DAMAGE_CAPABILITY, null);
		cap.setEffectiveMaxDamage(getMaxDamage());
		return provider;
    }
    
	/**
	 * Format:
	 * 		Item Name (vanilla minecraft)
	 * 		Rarity: [COMMON | UNCOMMON | SCARCE | RARE| EPIC]  [color = Gold] 
	 * 		Category:  [...] [color = Gold]
	 * 		Max Uses: [n] [color = Gold]
	 * 		Breakable: [Yes | No] [color = Dark Red | Green]
	 * 		Craftable: [Yes | No] [color = Green | Dark Red]
	 * 	 	Damageable: [Yes | No] [color = Dark Red | Green]
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		
		tooltip.add(I18n.translateToLocalFormatted("tooltip.label.rarity", TextFormatting.DARK_BLUE + getRarity().toString()));
        tooltip.add(I18n.translateToLocalFormatted("tooltip.label.category", getCategory()));
		
        if (stack.hasCapability(EffectiveMaxDamageCapabilityProvider.EFFECTIVE_MAX_DAMAGE_CAPABILITY, null)) {
            EffectiveMaxDamageCapability cap = (EffectiveMaxDamageCapability) stack.getCapability(EffectiveMaxDamageCapabilityProvider.EFFECTIVE_MAX_DAMAGE_CAPABILITY, null);
            tooltip.add(I18n.translateToLocalFormatted("tooltip.label.uses", cap.getEffectiveMaxDamage() - stack.getItemDamage(), cap.getEffectiveMaxDamage()));
//            tooltip.add(I18n.translateToLocalFormatted("tooltip.label.effective_max_uses", cap.getEffectiveMaxDamage()));
//    		tooltip.add(I18n.translateToLocalFormatted("tooltip.label.remaining_uses", cap.getEffectiveMaxDamage() - stack.getItemDamage()));
        }
       	tooltip.add(I18n.translateToLocalFormatted("tooltip.label.max_uses", getMaxDamage()));

        
		// is breakable tooltip
		String breakable = "";
		if (isBreakable()) {
			breakable = TextFormatting.DARK_RED + I18n.translateToLocal("tooltip.yes");
		}
		else {
			breakable =TextFormatting.GREEN + I18n.translateToLocal("tooltip.no");
		}
		tooltip.add(
				I18n.translateToLocalFormatted("tooltip.label.breakable", breakable));
		
		String craftable = "";
		if (isCraftable()) {
			craftable = TextFormatting.GREEN + I18n.translateToLocal("tooltip.yes");
		}
		else {
			craftable =TextFormatting.DARK_RED + I18n.translateToLocal("tooltip.no");
		}
		tooltip.add(	I18n.translateToLocalFormatted("tooltip.label.craftable", craftable));
		
		String damageable = "";
		if (isDamageable()) {
			damageable = TextFormatting.DARK_RED + I18n.translateToLocal("tooltip.yes");
		}
		else {
			damageable =TextFormatting.GREEN + I18n.translateToLocal("tooltip.no");
		}
		tooltip.add(
				I18n.translateToLocalFormatted("tooltip.label.damageable", damageable));
	}
	
    /**
     * Queries the percentage of the 'Durability' bar that should be drawn.
     *
     * @param stack The current ItemStack
     * @return 0.0 for 100% (no damage / full bar), 1.0 for 0% (fully damaged / empty bar)
     */
	@Override
    public double getDurabilityForDisplay(ItemStack stack) {
        if (stack.hasCapability(EffectiveMaxDamageCapabilityProvider.EFFECTIVE_MAX_DAMAGE_CAPABILITY, null)) {
            EffectiveMaxDamageCapability cap = (EffectiveMaxDamageCapability) stack.getCapability(EffectiveMaxDamageCapabilityProvider.EFFECTIVE_MAX_DAMAGE_CAPABILITY, null);
            return (double)stack.getItemDamage() / (double) cap.getEffectiveMaxDamage();
        }
        else {
        	return (double)stack.getItemDamage() / (double)stack.getMaxDamage();
        }
    }
    
    /**
     * Return whether this item is repairable in an anvil.
     */
	@Override
	public boolean getIsRepairable(ItemStack itemToRepair, ItemStack resourceItem) {
		if (itemToRepair.isItemDamaged() && itemToRepair.getItem() == this && resourceItem.getItem() == this) {
			return true;
		}
		return false;
    }
	
	/**
	 * 
	 */
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		
		BlockPos chestPos = pos;
		// determine if block at pos is a treasure chest
		Block block = worldIn.getBlockState(chestPos).getBlock();
		if (block instanceof ITreasureChestProxy) {
			chestPos = ((ITreasureChestProxy)block).getChestPos(chestPos);
			block = worldIn.getBlockState(chestPos).getBlock();
		}
		
		if (block instanceof AbstractChestBlock) {
			// get the tile entity
			TileEntity tileEntity = worldIn.getTileEntity(chestPos);
			if (tileEntity == null || !(tileEntity instanceof AbstractTreasureChestTileEntity)) {
				logger.warn("Null or incorrect TileEntity");
				return EnumActionResult.FAIL;
			}
			AbstractTreasureChestTileEntity chestTileEntity = (AbstractTreasureChestTileEntity)tileEntity;
						
			// exit if on the client
			if (WorldInfo.isClientSide(worldIn)) {			
				return EnumActionResult.FAIL;
			}

			// determine if chest is locked
			if (!chestTileEntity.hasLocks()) {
				return EnumActionResult.SUCCESS;
			}
			
			try {
				ItemStack heldItemStack = player.getHeldItem(hand);	
				boolean breakKey = true;
				boolean fitsLock = false;
				LockState lockState = null;
				boolean isKeyBroken = false;
				// check if this key is one that opens a lock (only first lock that key fits is unlocked).
				lockState = fitsFirstLock(chestTileEntity.getLockStates());
				if (lockState != null) {
					fitsLock = true;
				}
				
				if (fitsLock) {
					if (unlock(lockState.getLock())) {
						LockItem lock = lockState.getLock();
						// remove the lock
						lockState.setLock(null);
						// play noise
						worldIn.playSound(player, chestPos, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.3F, 0.6F);
						// update the client
                        chestTileEntity.sendUpdates();
                        if(!breaksLock(lock)) {
                            // spawn the lock
                            if (TreasureConfig.KEYS_LOCKS.enableLockDrops) {
                                InventoryHelper.spawnItemStack(worldIn, (double)chestPos.getX(), (double)chestPos.getY(), (double)chestPos.getZ(), new ItemStack(lock));
                            }
                        }
                        // don't break the key
                        breakKey = false;
					}
				}
                
                // get capability
                IEffectiveMaxDamageCapability cap = heldItemStack.getCapability(EffectiveMaxDamageCapabilityProvider.EFFECTIVE_MAX_DAMAGE_CAPABILITY, null);

				// check key's breakability
				if (breakKey) {                    
					if ((isBreakable() || anyLockBreaksKey(chestTileEntity.getLockStates(), this)) && TreasureConfig.KEYS_LOCKS.enableKeyBreaks) {
						int damage = heldItemStack.getItemDamage() + (getMaxDamage() - (heldItemStack.getItemDamage() % getMaxDamage()));
                        heldItemStack.setItemDamage(damage);
                        if (heldItemStack.getItemDamage() >= cap.getEffectiveMaxDamage()) {
                            // break key;
                            heldItemStack.shrink(1);
                        }
                        player.sendMessage(new TextComponentString("Key broke."));
                        worldIn.playSound(player, chestPos, SoundEvents.BLOCK_METAL_BREAK, SoundCategory.BLOCKS, 0.3F, 0.6F);
                        // flag the key as broken
                        isKeyBroken = true;
					}
					else {
						player.sendMessage(new TextComponentString("Failed to unlock."));
					}						
				}
				
				// user attempted to use key - increment the damage
				if (isDamageable() && !isKeyBroken) {
//                     heldItemStack.damageItem(1, player);
					logger.debug("before damage -> {}", heldItemStack.getItemDamage());
                    heldItemStack.setItemDamage(heldItemStack.getItemDamage() + 1);
                    logger.debug("after damage -> {}", heldItemStack.getItemDamage());
                    if (heldItemStack.getItemDamage() >= cap.getEffectiveMaxDamage()) {
                        heldItemStack.shrink(1);
                    }
					// if (heldItem.getItemDamage() == heldItem.getMaxDamage()) {
					// 	heldItem.shrink(1);
				}
			}
			catch (Exception e) {
				logger.error("error: ", e);
			}			
		}		
		
		return super.onItemUse(player, worldIn, chestPos, hand, facing, hitX, hitY, hitZ);
	}

	/**
	 * This method is a secondary check against a lock item.
	 * Override this method to overrule LockItem.acceptsKey() if this is a key with special abilities.
	 * @param lockItem
	 * @return
	 */
	public boolean fitsLock(LockItem lockItem) {
		return false;
	}
	
	/**
	 * 
	 * @param lockStates
	 * @return
	 */
	public LockState fitsFirstLock(List<LockState> lockStates) {
		LockState lockState = null;
		// check if this key is one that opens a lock (only first lock that key fits is unlocked).
		for (LockState ls : lockStates) {
			if (ls.getLock() != null) {
				lockState = ls;
				if (lockState.getLock().acceptsKey(this) || fitsLock(lockState.getLock())) {
					return ls;
				}
			}
		}
		return null; // <-- TODO should return EMPTY_LOCKSTATE
	}
	
	/**
	 * 
	 * @param lockStates
	 * @param key
	 * @return
	 */
	private boolean anyLockBreaksKey(List<LockState> lockStates, KeyItem key) {
		for (LockState ls : lockStates) {
			if (ls.getLock() != null) {
				if (ls.getLock().breaksKey(key)) {
					return true;
				}				
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param lockItem
	 * @return
	 */
	public boolean unlock(LockItem lockItem) {	
		if (lockItem.acceptsKey(this) || fitsLock(lockItem)) {
			logger.debug("Lock -> {} accepts key -> {}", lockItem.getRegistryName(), this.getRegistryName());
			if (RandomHelper.checkProbability(new Random(), this.getSuccessProbability())) {
				logger.debug("Unlock attempt met probability");
				return true;
			}
		}
		return false;
    }
    
    public boolean breaksLock(LockItem lockItem) {
        return false;
    }
    
	/**
	 * @return the rarity
	 */
	public Rarity getRarity() {
		return rarity;
	}

	/**
	 * @param rarity the rarity to set
	 */
	public KeyItem setRarity(Rarity rarity) {
		this.rarity = rarity;
		return this;
	}

	/**
	 * @return the craftable
	 */
	public boolean isCraftable() {
		return craftable;
	}

	/**
	 * @param craftable the craftable to set
	 */
	public KeyItem setCraftable(boolean craftable) {
		this.craftable = craftable;
		return this;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "KeyItem [rarity=" + rarity + ", craftable=" + craftable + "]";
	}

	/**
	 * @return the category
	 */
	public Category getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public KeyItem setCategory(Category category) {
		this.category = category;
		return this;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isBreakable() {
		return breakable;
	}
	
	/**
	 * 
	 * @param breakable
	 */
	public KeyItem setBreakable(boolean breakable) {
		this.breakable = breakable;
		return this;
	}

	/**
	 * @param damage the uses to set
	 */
	public KeyItem setMaxDamage(int damage) {
		super.setMaxDamage(damage);
		return this;
	}

	/**
	 * @return the successProbability
	 */
	public double getSuccessProbability() {
		return successProbability;
	}

	/**
	 * @param successProbability the successProbability to set
	 */
	public KeyItem setSuccessProbability(double successProbability) {
		this.successProbability = successProbability;
		return this;
	}

	/**
	 * @return the damageable
	 */
	public boolean isDamageable() {
		return damageable;
	}

	/**
	 * @param damageable the damageable to set
	 */
	public void setDamageable(boolean damageable) {
		this.damageable = damageable;
	}
}
