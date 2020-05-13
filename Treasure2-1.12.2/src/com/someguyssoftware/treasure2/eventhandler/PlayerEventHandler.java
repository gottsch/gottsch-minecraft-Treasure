/**
 * 
 */
package com.someguyssoftware.treasure2.eventhandler;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import com.someguyssoftware.gottschcore.mod.IMod;
import com.someguyssoftware.gottschcore.positional.Coords;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.FogBlock;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.enums.FogType;
import com.someguyssoftware.treasure2.item.TreasureItems;
import com.someguyssoftware.treasure2.item.charm.CharmCapabilityProvider;
import com.someguyssoftware.treasure2.item.charm.CharmType;
import com.someguyssoftware.treasure2.item.charm.ICharm;
import com.someguyssoftware.treasure2.item.charm.ICharmCapability;
import com.someguyssoftware.treasure2.item.charm.ICharmState;
import com.someguyssoftware.treasure2.item.charm.ICharmed;
import com.someguyssoftware.treasure2.network.CharmMessageToClient;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

/**
 * @author Mark Gottschling on Apr 26, 2018
 *
 */
public class PlayerEventHandler {
	private static final String FIRST_JOIN_NBT_KEY = "treasure2.firstjoin";
	private static final String PATCHOULI_MODID = "patchouli";
	private static final String PATCHOULI_GUIDE_BOOK_ID = "patchouli:guide_book";
	private static final String PATCHOULI_GUIDE_TAG_ID = "patchouli:book";
	private static final String TREASURE2_GUIDE_TAG_VALUE = "treasure2:guide";

	// reference to the mod.
	private IMod mod;

	/**
	 * 
	 */
	public PlayerEventHandler(IMod mod) {
		setMod(mod);
	}

//	TEMP remove until patchouli book is complete.
//    @SubscribeEvent
	public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
		// check if config is enabled
		if (!TreasureConfig.MOD.enableStartingBook) {
			return;
		}

		if (event.player.isCreative()) {
			return;
		}

		NBTTagCompound data = event.player.getEntityData();
		NBTTagCompound persistent;
		if (!data.hasKey(EntityPlayer.PERSISTED_NBT_TAG)) {
			data.setTag(EntityPlayer.PERSISTED_NBT_TAG, (persistent = new NBTTagCompound()));
		} else {
			persistent = data.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
		}

		// check if the patchouli mod is installed
		if (Loader.isModLoaded(PATCHOULI_MODID)) {
			if (!persistent.hasKey(FIRST_JOIN_NBT_KEY)) {
				persistent.setBoolean(FIRST_JOIN_NBT_KEY, true);
				// create the book
				Item guideBook = Item.REGISTRY.getObject(new ResourceLocation(PATCHOULI_GUIDE_BOOK_ID));
				ItemStack stack = new ItemStack(guideBook);
				if (!stack.hasTagCompound()) {
					NBTTagCompound tag = new NBTTagCompound();
					tag.setString(PATCHOULI_GUIDE_TAG_ID, TREASURE2_GUIDE_TAG_VALUE);
					stack.setTagCompound(tag);
				}
				event.player.inventory.addItemStackToInventory(stack);
			}
		}
	}

	/**
	 * 
	 * @param event
	 */
	@SubscribeEvent
	public void checkFogInteraction(LivingUpdateEvent event) {
		if (WorldInfo.isClientSide(event.getEntity().getEntityWorld())) {
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
				if (((FogBlock) block).getFogType() == FogType.WITHER) {
					PotionEffect potionEffect = ((EntityLivingBase) event.getEntity())
							.getActivePotionEffect(MobEffects.WITHER);
					// if player does not have wither effect, add it
					if (potionEffect == null) {
						((EntityLivingBase) event.getEntity())
								.addPotionEffect(new PotionEffect(MobEffects.WITHER, 300, 0));
					}
				} else if (((FogBlock) block).getFogType() == FogType.POISON) {
					PotionEffect potionEffect = ((EntityLivingBase) event.getEntity())
							.getActivePotionEffect(MobEffects.POISON);
					// if player does not have wither effect, add it
					if (potionEffect == null) {
						((EntityLivingBase) event.getEntity())
								.addPotionEffect(new PotionEffect(MobEffects.POISON, 300, 0));
					}
				}
			}

		}
	}

	// TODO refactor these charm methods. they all do the same thing except call a different charm event
	/**
	 * 
	 * @param event
	 */
	@SubscribeEvent
	public void checkCharmsInteraction(LivingUpdateEvent event) {
		if (WorldInfo.isClientSide(event.getEntity().getEntityWorld())) {
			return;
		}

		// do something to player every update tick:
		if (event.getEntity() instanceof EntityPlayer) {
			
			// get the player
			EntityPlayerMP player = (EntityPlayerMP) event.getEntity();
			Optional<CharmContext> context = getCharmContext(player);
			if (!context.isPresent()) {
				return;
			}
			
			if (context.get().itemStack.hasCapability(CharmCapabilityProvider.CHARM_CAPABILITY, null)) {
				ICharmCapability provider = context.get().itemStack.getCapability(CharmCapabilityProvider.CHARM_CAPABILITY, null);
				List<ICharmState> charmStates = provider.getCharmStates();
				Treasure.logger.debug("has capability");
				for (ICharmState charmState : charmStates) {
					Treasure.logger.debug("charm state -> {}: {}", charmState.getCharm().getName(), charmState.toString());
					if (charmState.doCharm(player.world, new Random(), new Coords((int)player.posX, (int)player.posY, (int)player.posZ), player, event)) {
						// send state message to client
						CharmMessageToClient message = new CharmMessageToClient(player.getName(), charmState, context.get().hand, null);
						Treasure.logger.debug("sending living charm message to client -> {}", message);
						Treasure.simpleNetworkWrapper.sendTo(message, player);
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * @param event
	 */
	@SubscribeEvent
	public void checkCharmsInteractionWithDamage(LivingDamageEvent event) {
		if (WorldInfo.isClientSide(event.getEntity().getEntityWorld())) {
			return;
		}

		// do something to player every update tick:
		if (event.getEntity() instanceof EntityPlayer) {
			// get the player
			EntityPlayerMP player = (EntityPlayerMP) event.getEntity();
			Optional<CharmContext> context = getCharmContext(player);
			if (!context.isPresent()) {
				Treasure.logger.debug("context not present");
				return;
			}
			
			if (context.get().itemStack.hasCapability(CharmCapabilityProvider.CHARM_CAPABILITY, null)) {
				ICharmCapability provider = context.get().itemStack.getCapability(CharmCapabilityProvider.CHARM_CAPABILITY, null);
				List<ICharmState> charmStates = provider.getCharmStates();
//				Treasure.logger.debug("has capability");
				for (ICharmState charmState : charmStates) {
//					Treasure.logger.debug("charm state -> {}", charmState.getCharm().getName());
					if (charmState.doCharm(player.world, new Random(), new Coords((int)player.posX, (int)player.posY, (int)player.posZ), player, event)) {
						// send state message to client
						CharmMessageToClient message = new CharmMessageToClient(player.getName(), charmState, context.get().hand, null);
//						Treasure.logger.debug("sending damage charm message to client -> {}", message);
						Treasure.simpleNetworkWrapper.sendTo(message, player);
					}
				}
			}
		}		
	}
	
	@SubscribeEvent
	public void checkCharmsInteractionWithBlock(BlockEvent.HarvestDropsEvent event) {
		if (WorldInfo.isClientSide(event.getWorld())) {
			return;
		}
		
		if (event.getHarvester() == null) {
			return;
		}
		
		// get the player
		EntityPlayerMP player = (EntityPlayerMP) event.getHarvester();
		Optional<CharmContext> context = getCharmContext(player);
		if (!context.isPresent()) {
			return;
		}
		if (context.get().itemStack.hasCapability(CharmCapabilityProvider.CHARM_CAPABILITY, null)) {
			ICharmCapability provider = context.get().itemStack.getCapability(CharmCapabilityProvider.CHARM_CAPABILITY, null);
			List<ICharmState> charmStates = provider.getCharmStates();
			for (ICharmState charmState : charmStates) {
				if (charmState.doCharm(player.world, new Random(), new Coords((int)player.posX, (int)player.posY, (int)player.posZ), player, event)) {
					CharmMessageToClient message = new CharmMessageToClient(player.getName(), charmState, context.get().hand, null);
					Treasure.logger.debug("sending harvest charm message to client -> {}", message);
					Treasure.simpleNetworkWrapper.sendTo(message, player);
				}
			}
		}
	}
	
	/**
	 * 
	 * @param player
	 * @return
	 */
	private Optional<CharmContext> getCharmContext(EntityPlayerMP player) {
		ItemStack mainHeldItem = player.getHeldItem(EnumHand.MAIN_HAND);
		ItemStack offHeldItem = player.getHeldItem(EnumHand.OFF_HAND);
		CharmContext context = new CharmContext();
		if (offHeldItem.getItem() instanceof ICharmed ) {
			context.hand = EnumHand.OFF_HAND;
			context.itemStack = offHeldItem;
		}
		else if (mainHeldItem.getItem() instanceof ICharmed) {
			context.hand = EnumHand.MAIN_HAND;
			context.itemStack = mainHeldItem;
		}
		else {
			return Optional.empty();
		}
		return Optional.of(context);
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

	/**
	 * 
	 * @author Mark Gottschling on Apr 30, 2020
	 *
	 */
	private class CharmContext {
		EnumHand hand;
		int slot;
		ItemStack itemStack;
		
		CharmContext() {}
		CharmContext(ItemStack stack, EnumHand hand, int slot) {
			this.itemStack = stack;
			this.hand = hand;
			this.slot = slot;
		}
	}
}
