/**
 * 
 */
package com.someguyssoftware.treasure2.eventhandler;

import com.someguyssoftware.gottschcore.mod.IMod;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.FogBlock;
import com.someguyssoftware.treasure2.block.WitherFogBlock;
import com.someguyssoftware.treasure2.client.gui.inventory.KeyRingGui;
import com.someguyssoftware.treasure2.enums.FogType;
import com.someguyssoftware.treasure2.inventory.KeyRingContainer;
import com.someguyssoftware.treasure2.inventory.KeyRingInventory;
import com.someguyssoftware.treasure2.item.KeyRingItem;
import com.someguyssoftware.treasure2.item.TreasureItems;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author Mark Gottschling on Apr 26, 2018
 *
 */
public class PlayerEventHandler {
	// reference to the mod.
	private IMod mod;
	
	/**
	 * 
	 */
	public PlayerEventHandler(IMod mod) {
		setMod(mod);
	}
	
	/**
	 * 
	 * @param event
	 */
	@SubscribeEvent
	public void checkFogInteraction(LivingUpdateEvent event) {
        if (event.getEntity().getEntityWorld() .isRemote) {
        	return;
        }
        
		// do something to player every update tick:
		if (event.getEntity() instanceof EntityPlayer) {
			// get the player
			EntityPlayer player = (EntityPlayer) event.getEntity();
			// determine the pos the player is at
			BlockPos pos = player.getPosition();
			// get the block at pos
			Block block = event.getEntity().getEntityWorld().getBlockState(pos).getBlock();
			if (block instanceof FogBlock) {
				// check the fog type
				if (((FogBlock)block).getFogType() == FogType.WITHER	) {
		        	PotionEffect potionEffect = ((EntityLivingBase)event.getEntity()).getActivePotionEffect(MobEffects.WITHER);
		        	// if player does not have wither effect, add it
		        	if (potionEffect == null) {
		        		((EntityLivingBase)event.getEntity()).addPotionEffect(new PotionEffect(MobEffects.WITHER, 300, 0));
//		        		Treasure.logger.debug("wither potion effect is null, should be adding....");
		        	}					
				}
				else if (((FogBlock)block).getFogType() == FogType.POISON) {
		        	PotionEffect potionEffect = ((EntityLivingBase)event.getEntity()).getActivePotionEffect(MobEffects.POISON);
		        	// if player does not have wither effect, add it
		        	if (potionEffect == null) {
		        		((EntityLivingBase)event.getEntity()).addPotionEffect(new PotionEffect(MobEffects.POISON, 300, 0));
//		        		Treasure.logger.debug("poison potion effect is null, should be adding....");
		        	}				
				}
			}
      	
		}
	}
	
	/**
	 * @return the mod
	 */
	public IMod getMod() {
		return mod;
	}

	/**
	 * @param mod the mod to set
	 */
	public void setMod(IMod mod) {
		this.mod = mod;
	}

}
