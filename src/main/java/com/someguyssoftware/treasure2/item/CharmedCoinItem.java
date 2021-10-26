/**
 * 
 */
package com.someguyssoftware.treasure2.item;

import java.util.List;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.capability.CharmInventoryCapabilityProvider;
import com.someguyssoftware.treasure2.enums.Coins;
import com.someguyssoftware.treasure2.item.charm.ICharmed;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

/**
 * @author Mark Gottschling on Apr 24, 2020
 *
 */
// TODO rename CharmedCoin
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
//		CharmCapabilityProvider provider =  new CharmCapabilityProvider();
		Treasure.logger.debug("{} item initiating caps", stack.getItem().getRegistryName().toString());
		CharmInventoryCapabilityProvider provider =  new CharmInventoryCapabilityProvider();
		return provider;
	}

	/**
	 * 
	 */
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		addCharmedInfo2(stack, worldIn, tooltip, flagIn);
	}

	/**
	 * 
	 */
	@Override
	public boolean hasEffect(ItemStack stack) {
		return true;
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

}
