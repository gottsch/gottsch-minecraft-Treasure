/**
 * 
 */
package com.someguyssoftware.treasure2.item;

import java.util.List;

import javax.annotation.Nullable;

import com.someguyssoftware.gottschcore.item.ModItem;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.capability.CharmInventoryCapabilityProvider;
import com.someguyssoftware.treasure2.capability.CharmInventoryCapabilityStorage;
import com.someguyssoftware.treasure2.capability.ICharmInventoryCapability;
import com.someguyssoftware.treasure2.capability.TreasureCapabilities;
import com.someguyssoftware.treasure2.enums.AdornmentType;
import com.someguyssoftware.treasure2.integration.baubles.BaublesIntegration;
import com.someguyssoftware.treasure2.item.charm.ICharmable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

/**
 * @author Mark Gottschling on Dec 20, 2020
 *
 */
public class Adornment extends ModItem implements IAdornment, ICharmable, IPouchable {
	private AdornmentType type;

	/*
	 * default max of slot.
	 * maxSlots are the max number of charms an Adornment can hold.
	 * // capability slots below
	 * once a charm is added, a slot is filled and the count is decremented.
	 * when a charm runs out of uses, it is removed from teh Adornment, but the maxSlots are not incremented.
	 * once all maxSlots are used, the Adornment can not add any new Charms
	 */
	private int maxSlots = 2;

    private int level = 1;

    private static final CharmInventoryCapabilityStorage CAPABILITY_STORAGE = new CharmInventoryCapabilityStorage();
    
	/**
	 * 
	 * @param modID
	 * @param name
	 * @param type
	 */
	public Adornment(String modID, String name, AdornmentType type) {
		super();
		setItemName(modID, name);
		setMaxStackSize(1);
		setCreativeTab(Treasure.TREASURE_TAB);
		setType(type);
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
		// TODO will have to create a new CapabilityProvider that includes both CharmInventory and TreasureBaubleProvider
//		CharmInventoryCapabilityProvider provider =  new CharmInventoryCapabilityProvider();
		return BaublesIntegration.isEnabled() ? new BaublesIntegration.AdornmentProvider(type) : new CharmInventoryCapabilityProvider();
//		return provider;
	}

	/**
	 * 
	 */
	@Override
    public String getItemStackDisplayName(ItemStack stack) {
    	String name = super.getItemStackDisplayName(stack);
		return name;
    }
    
	/**
	 * 
	 */
	@Override
	public boolean hasEffect(ItemStack stack) {
		boolean charmed =  false;
		if (stack.hasCapability(TreasureCapabilities.CHARM_INVENTORY, null)) {
			ICharmInventoryCapability cap = stack.getCapability(TreasureCapabilities.CHARM_INVENTORY, null);
			if (cap.getCharmEntities() != null && cap.getCharmEntities().size() > 0) {
				charmed = true;
			}
		}
		return charmed;
	}

	/**
	 * 
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
		super.addInformation(stack, world, tooltip, flag);
		if (isCharmed(stack)) {
			addCharmedInfo(stack, world, tooltip, flag);
		}
		else {
			tooltip.add(TextFormatting.GOLD.toString() + "" + TextFormatting.ITALIC.toString() + I18n.translateToLocal("tooltip.label.charmable"));
		}
		addSlotsInfo(stack, world, tooltip, flag);
	}

	/**
	 * NOTE getNBTShareTag() and readNBTShareTag() are required to sync item capabilities server -> client. I needed this when holding charms in hands and then swapping hands
	 * or having the client update when the Anvil GUI is open.
	 */
	@Override
    public NBTTagCompound getNBTShareTag(ItemStack stack) {
		NBTTagCompound nbt = null;
		// read cap -> write nbt
		nbt = (NBTTagCompound) CAPABILITY_STORAGE.writeNBT(
				TreasureCapabilities.CHARM_INVENTORY,
				stack.getCapability(TreasureCapabilities.CHARM_INVENTORY, null), null);
		return nbt;
	}
	
    @Override
    public void readNBTShareTag(ItemStack stack, @Nullable NBTTagCompound nbt) {
        super.readNBTShareTag(stack, nbt);
        // read nbt -> write key item
       CAPABILITY_STORAGE.readNBT(
    		   TreasureCapabilities.CHARM_INVENTORY, 
				stack.getCapability(TreasureCapabilities.CHARM_INVENTORY, null), 
				null,
				nbt);
    }
    
	public AdornmentType getType() {
		return type;
	}

	public void setType(AdornmentType type) {
		this.type = type;
	}

	@Override
	public int getMaxSlots() {
		return maxSlots;
	}

	public IAdornment setMaxSlots(int maxSlots) {
		this.maxSlots = maxSlots;
		return this;
    }
    
    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public IAdornment setLevel(int level) {
        this.level = level;
        return this;
    }
}
