/**
 * 
 */
package com.someguyssoftware.treasure2.item;

import java.util.List;

import com.someguyssoftware.gottschcore.item.ModItem;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.capability.PouchCapabilityProvider;
import com.someguyssoftware.treasure2.client.gui.GuiHandler;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

/**
 * @author Mark Gottschling on May 13, 2020
 *
 */
public class PouchItem extends ModItem implements IPouch {
	public static int GUIID = 50;
	private PouchType pouchType;
	
	/**
	 * 
	 * @param modID
	 * @param name
	 */
	public PouchItem(String modID, String name) {
		setItemName(modID, name);
		setCreativeTab(Treasure.TREASURE_TAB);
		setMaxStackSize(1);
		setPouchType(PouchType.STANDARD);
	}
	
	/**
	 * 
	 * @param modID
	 * @param name
	 * @param type
	 */
	public PouchItem(String modID, String name, PouchType type) {
		this(modID, name);
		setPouchType(type);
	}
	
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
		PouchCapabilityProvider provider = new PouchCapabilityProvider();
		return provider;
	}
	
	/**
	 * 
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);

		tooltip.add(I18n.translateToLocalFormatted("tooltip.label.coin_pouch", TextFormatting.GOLD));
	}
	
	/**
	 * 
	 */
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		// exit if on the client
		if (WorldInfo.isClientSide(worldIn)) {			
//			return new ActionResult<ItemStack>(EnumActionResult.FAIL, playerIn.getHeldItem(handIn));
			return new ActionResult<ItemStack>(EnumActionResult.PASS, playerIn.getHeldItem(handIn)); // 9/2/20
		}

		BlockPos pos = playerIn.getPosition();
		playerIn.openGui(Treasure.instance, GUIID, worldIn, pos.getX(), pos.getY(), pos.getZ());
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}

	@Override
	public PouchType getPouchType() {
		return pouchType;
	}

	public PouchItem setPouchType(PouchType pouchType) {
		this.pouchType = pouchType;
		return this;
	}
}
