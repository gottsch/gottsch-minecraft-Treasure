/**
 * 
 */
package com.someguyssoftware.treasure2.item;

import java.util.List;

import com.someguyssoftware.treasure2.enums.Coins;
import com.someguyssoftware.treasure2.item.charm.CharmCapabilityProvider;
import com.someguyssoftware.treasure2.item.charm.CharmType;
import com.someguyssoftware.treasure2.item.charm.ICharm;
import com.someguyssoftware.treasure2.item.charm.ICharmCapability;
import com.someguyssoftware.treasure2.item.charm.ICharmState;
import com.someguyssoftware.treasure2.item.charm.ICharmed;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

/**
 * @author Mark Gottschling on Apr 24, 2020
 *
 */
public class CharmedCoinItem extends CoinItem implements ICharmed {

	/**
	 * 
	 * @param modID
	 * @param name
	 */
	public CharmedCoinItem(String modID, String name) {
		super(modID, name);
		this.setMaxStackSize(1);
		// prevent from showing in any creative tab
		this.setCreativeTab(null);
	}

	/**
	 * 
	 * @param modID
	 * @param name
	 * @param coin
	 */
	public CharmedCoinItem(String modID, String name, Coins coin) {
		super(modID, name, coin);
		this.setMaxStackSize(1);
		this.setCreativeTab(null);
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
		CharmCapabilityProvider provider =  new CharmCapabilityProvider();
		return provider;
	}

	/**
	 * 
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		// TODO addInformation needs to inherit from a parent class
		tooltip.add(TextFormatting.GOLD.toString() + "" + TextFormatting.ITALIC.toString() + I18n.translateToLocal("tooltip.label.charmed"));
		tooltip.add(TextFormatting.YELLOW.toString() + "" + TextFormatting.BOLD+ I18n.translateToLocal("tooltip.label.charms"));
		// get the capabilities
		ICharmCapability cap = stack.getCapability(CharmCapabilityProvider.CHARM_CAPABILITY, null);
		if (cap != null) {
			List<ICharmState> charmStates = cap.getCharmStates();
			for (ICharmState state : charmStates) {
				String msg = "";
				TextFormatting color = TextFormatting.WHITE;
				CharmType type = state.getCharm().getCharmType();
				String extra = "";
				// TODO change to switch
				switch(type) {
				case HEALING:
					color = TextFormatting.RED;
					extra = TextFormatting.GRAY +  "" + TextFormatting.ITALIC + I18n.translateToLocalFormatted("tooltip.charm.healing_rate");
					break;
				case SHIELDING:
					color = TextFormatting.AQUA;
					extra = TextFormatting.GRAY + "" + TextFormatting.ITALIC + I18n.translateToLocalFormatted("tooltip.charm.shield_rate", state.getCharm().getMaxPercent()*100F);
					break;
				case FULLNESS:
					color = TextFormatting.DARK_GREEN;
					extra = TextFormatting.GRAY + "" + TextFormatting.ITALIC + I18n.translateToLocalFormatted("tooltip.charm.fullness_rate");
					break;
				case HARVESTING:
					color = TextFormatting.GREEN;
					extra = TextFormatting.GRAY + "" + TextFormatting.ITALIC + I18n.translateToLocalFormatted("tooltip.charm.harvest_rate", state.getCharm().getMaxPercent());
					break;
				case DECAY:
					color = TextFormatting.DARK_RED;
					extra = TextFormatting.GRAY +  "" + TextFormatting.ITALIC + I18n.translateToLocalFormatted("tooltip.charm.decay_rate");
					break;
				}
				
				tooltip.add("  " + color + I18n.translateToLocalFormatted("tooltip.charm." + state.getCharm().getName(), 
						String.valueOf(Math.toIntExact(Math.round(state.getVitals().getValue()))), 
						String.valueOf(Math.toIntExact(Math.round(state.getCharm().getMaxValue())))));
				if (!"".equals(extra)) {
					tooltip.add(" " + extra);
				}
			}
		}
	}

	/*
	 * Example
	 */
	//	public void doSomething(World world, ICoords coords) {
	//		List<IWishProvider> providers = new ArrayList<>();
	//		Optional<IWishProvider> provider = this.getWishProvider(world, coords, providers, (w, c, p) -> {
	//			return providers.get(0);
	//		});
	//	}

	// TODO can't add charms from TreasureCharms. Has to be unique instances since the charms hold state, just like BlockState and LockState.
	// TODO apply modifiers to charms (replacing if necessary)
	public ICharm addCharm(ICharm charm) {
		return charm;
	}

	//	@Override
	public List<ICharmState> getCharmStates(ItemStack stack) {
		List<ICharmState> charmStates = null;
		if (stack.hasCapability(CharmCapabilityProvider.CHARM_CAPABILITY, null)) {
			ICharmCapability cap = stack.getCapability(CharmCapabilityProvider.CHARM_CAPABILITY, null);
			if (cap != null) {
				charmStates = cap.getCharmStates();
			}
		}
		return charmStates; // TODO should return empty object or Optional
	}

}
