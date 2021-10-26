/**
 * 
 */
package com.someguyssoftware.treasure2.item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.someguyssoftware.gottschcore.item.ModItem;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.AbstractChestBlock;
import com.someguyssoftware.treasure2.block.ITreasureChestProxy;
import com.someguyssoftware.treasure2.enums.Category;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.lock.LockState;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

/**
 * @author Mark Gottschling onJan 10, 2018
 *
 */
public class LockItem extends ModItem {

	/*
	 * The category that the lock belongs to
	 */
	private Category category;

	/*
	 * The rarity of the lock
	 */
	private Rarity rarity;

	/*
	 * Flag if the lock is craftable
	 */
	private boolean craftable;

	/*
	 * a list of keys that unlock the lock
	 */
	private List<KeyItem> keys = new ArrayList<>(3);

	/**
	 * 
	 * @param item
	 * @param keys
	 */
	public LockItem(String modID, String name, KeyItem[] keys) {
		this(modID, name);
		getKeys().addAll(Arrays.asList(keys));
	}

	/**
	 * 
	 * @param item
	 */
	public LockItem(String modID, String name) {
		setItemName(modID, name);
		setCategory(Category.ELEMENTAL);
		setRarity(Rarity.COMMON);
		setCraftable(false);
		setCreativeTab(Treasure.TREASURE_TAB);
	}

	/**
	 * Format: Item Name (vanilla minecraft) Rarity: [COMMON | UNCOMMON | SCARCE |
	 * RARE| EPIC] [color = Gold] Category: [...] [color = Gold] Craftable: [Yes |
	 * No] [color = Green | Dark Red] Accepts Keys: [list] [color = Gold]
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);

		tooltip.add(I18n.translateToLocalFormatted("tooltip.label.rarity",
				TextFormatting.DARK_BLUE + getRarity().toString()));
		tooltip.add(I18n.translateToLocalFormatted("tooltip.label.category", getCategory()));

		String craftable = "";
		if (isCraftable()) {
			craftable = TextFormatting.GREEN + I18n.translateToLocal("tooltip.yes");
		} else {
			craftable = TextFormatting.DARK_RED + I18n.translateToLocal("tooltip.no");
		}
		tooltip.add(I18n.translateToLocalFormatted("tooltip.label.craftable", craftable));

//		String keyList = getKeys().stream().map(e -> I18n.translateToLocalFormatted(TextFormatting.BLUE + e.getUnlocalizedName() + ".name"))
//				.collect(Collectors.joining(", "));
//		tooltip.add(I18n.translateToLocalFormatted("tooltip.label.accepts_keys", keyList));
		
		tooltip.add(I18n.translateToLocal("tooltip.label.accepts_keys"));
		getKeys().forEach(key -> {
			String formattedKey = I18n.translateToLocalFormatted(key.getUnlocalizedName() + ".name");
			tooltip.add(I18n.translateToLocalFormatted("- " + TextFormatting.DARK_GREEN + formattedKey));
		});
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
			chestPos = ((ITreasureChestProxy) block).getChestPos(chestPos);
			block = worldIn.getBlockState(chestPos).getBlock();
		}

		if (block instanceof AbstractChestBlock) {
			// get the tile entity
			AbstractTreasureChestTileEntity tileEntity = (AbstractTreasureChestTileEntity) worldIn.getTileEntity(chestPos);

			// exit if on the client
			if (WorldInfo.isClientSide(worldIn)) {
				return EnumActionResult.FAIL;
			}

			try {
				ItemStack heldItem = player.getHeldItem(hand);
				// handle the lock
				// NOTE don't use the return boolean as the locked flag here, as the chest is
				// already locked and if the method was
				// unsuccessful it could state the chest is unlocked.
				handleHeldLock(tileEntity, player, heldItem);
			} catch (Exception e) {
				Treasure.logger.error("error: ", e);
			}
		}
		return super.onItemUse(player, worldIn, chestPos, hand, facing, hitX, hitY, hitZ);
	}

	/**
	 * 
	 * @param te
	 * @param player
	 * @param heldItem
	 * @return flag indicating if a lock was added
	 */
	private boolean handleHeldLock(AbstractTreasureChestTileEntity tileEntity, EntityPlayer player, ItemStack heldItem) {
		boolean lockedAdded = false;
		LockItem lock = (LockItem) heldItem.getItem();
		// add the lock to the first lockstate that has an available slot
		for (LockState lockState : tileEntity.getLockStates()) {
			if (lockState != null && lockState.getLock() == null) {
				lockState.setLock(lock);
				tileEntity.sendUpdates();
				// decrement item in hand
				heldItem.shrink(1);
				lockedAdded = true;
				break;
			}
		}
		return lockedAdded;
	}

	/**
	 * 
	 * @param keyItem
	 * @return
	 */
	public boolean acceptsKey(KeyItem keyItem) {
		for (KeyItem k : getKeys()) {
			if (k == keyItem)
				return true;
		}
		return false;
	}

    public boolean breaksKey(KeyItem keyItem) {
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
	public LockItem setRarity(Rarity rarity) {
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
	public LockItem setCraftable(boolean craftable) {
		this.craftable = craftable;
		return this;
	}

	/**
	 * @return the keys
	 */
	public List<KeyItem> getKeys() {
		return keys;
	}

	/**
	 * @param keys the keys to set
	 */
	public LockItem setKeys(List<KeyItem> keys) {
		this.keys = keys;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "LockItem [name=" + getRegistryName() + ", rarity=" + rarity + ", craftable=" + craftable + ", keys="
				+ keys + "]";
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
	public LockItem setCategory(Category category) {
		this.category = category;
		return this;
	}
}
