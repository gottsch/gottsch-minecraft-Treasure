/**
 * 
 */
package com.someguyssoftware.treasure2.eventhandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import com.someguyssoftware.gottschcore.mod.IMod;
import com.someguyssoftware.gottschcore.positional.Coords;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.FogBlock;
import com.someguyssoftware.treasure2.capability.CharmableCapabilityProvider;
import com.someguyssoftware.treasure2.capability.CharmCapabilityProvider;
import com.someguyssoftware.treasure2.capability.ICharmCapability;
import com.someguyssoftware.treasure2.capability.PouchCapabilityProvider;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.enums.FogType;
import com.someguyssoftware.treasure2.item.IPouch;
import com.someguyssoftware.treasure2.item.PouchType;
import com.someguyssoftware.treasure2.item.TreasureItems;
import com.someguyssoftware.treasure2.item.charm.ICharm;
import com.someguyssoftware.treasure2.item.charm.ICharmInstance;
import com.someguyssoftware.treasure2.item.charm.ICharmable;
import com.someguyssoftware.treasure2.item.charm.ICharmed;
import com.someguyssoftware.treasure2.item.wish.IWishable;
import com.someguyssoftware.treasure2.network.CharmMessageToClient;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
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
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.items.IItemHandler;

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
			processCharms(event, player);
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
			processCharms(event, player);
		}		
	}
	
	/**
	 * 
	 * @param event
	 */
	@SubscribeEvent
	public void checkCharmsInteractionWithAttack(LivingHurtEvent event) {
		if (WorldInfo.isClientSide(event.getEntity().getEntityWorld())) {
			return;
		}
		
		if (event.getSource().getTrueSource() instanceof EntityPlayer) {
			// get the player
			EntityPlayerMP player = (EntityPlayerMP) event.getSource().getTrueSource();
			processCharms(event, player);
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

		// if the harvested blcok has a tile entity then don't process
		// NOTE this may exclude non-inventory blocks
		IBlockState harvestedState = event.getState();
		Block harvestedBlock = harvestedState.getBlock();
		if (harvestedBlock.hasTileEntity(harvestedState)) {
			return;
		}

		// get the player
		EntityPlayerMP player = (EntityPlayerMP) event.getHarvester();
		processCharms(event, player);
	}

	/**
	 * 
	 * @param event
	 * @param player
	 */
	private void processCharms(Event event, EntityPlayerMP player) {
		final List<String> nonMultipleUpdateCharms = new ArrayList<>(5);
		
		// check each hand
		Optional<CharmContext> context = null;
		for (EnumHand hand : EnumHand.values()) {
			context = getCharmContext(player, hand);
			if (context.isPresent()) {
				if (context.get().type == CharmedType.CHARM || context.get().type == CharmedType.ADORNMENT) {
					doCharms(context.get(), player, event, nonMultipleUpdateCharms);
				}
				else {
					doPouch(context.get(), player, event, nonMultipleUpdateCharms);
				}
			}
		}

		// check hotbar - get the context at each slot
		for (int hotbarSlot = 0; hotbarSlot < 9; hotbarSlot++) {
			if (player.inventory.getStackInSlot(hotbarSlot) != player.getHeldItemMainhand()) {
				context = getCharmContext(player, hotbarSlot);
				if (context.isPresent()) {
					if (context.get().type == CharmedType.ADORNMENT) {
						// Treasure.logger.debug("is a hotbar adornment -> {} @ slot -> {}", context.get().itemStack.getItem().getRegistryName(), context.get().slot);
						// at this point, we know the item in slot x has charm capabilities
						doCharms(context.get(), player, event, nonMultipleUpdateCharms);
					}
				}
			}
		}

		// TODO future integration check Baubles equiped

	}

	/**
	 * 
	 * @param context
	 * @param player
	 * @param event
	 */
	public void doPouch(CharmContext context, EntityPlayerMP player, Event event, final List<String> nonMultipleUpdateCharms) {
		// get the capability of the pouch
		IItemHandler cap = context.itemStack.getCapability(PouchCapabilityProvider.INVENTORY_CAPABILITY, null);
		// TODO this slots bit could be better. Maybe pouch item should have number of slots property
		// scan the first 3 slots of pouch (this only works for pouches... what if in future there are other focuses ?
		int slots = context.itemStack.getItem() == TreasureItems.LUCKY_POUCH ? 1 : 
			context.itemStack.getItem() == TreasureItems.APPRENTICES_POUCH ? 2 : 3;
		for (int focusIndex = 0; focusIndex < slots; focusIndex++) {
			ItemStack itemStack = cap.getStackInSlot(focusIndex);
			// update the context to the specific charm
			context.itemStack = itemStack;
			context.slot = focusIndex;
			// TODO a way around instanceof check if to add a property(s) to the charmCapability - boolean charmed and/or boolean charmable.
			//			if (itemStack.hasCapability(CharmCapabilityProvider.CHARM_CAPABILITY, null)) {
			if (itemStack.getItem() instanceof ICharmed) {
				context.capability = itemStack.getCapability(CharmCapabilityProvider.CHARM_CAPABILITY, null);
				doCharms(context, player, event, nonMultipleUpdateCharms);
			}
			else if (itemStack.getItem() instanceof ICharmable) {
				// TODO need to check the cap because it might not be charmed
				//			else if (itemStack.hasCapability(CharmableCapabilityProvider.CHARM_CAPABILITY, null)) {
				context.capability = itemStack.getCapability(CharmableCapabilityProvider.CHARM_CAPABILITY, null);
				doCharms(context, player, event, nonMultipleUpdateCharms);
			}
		}
	}

	/**
	 * 
	 * @param context
	 * @param player
	 * @param event
	 */
	private void doCharms(CharmContext context, EntityPlayerMP player, Event event, final List<String> nonMultipleUpdateCharms) {
        List<ICharmInstance> removeInstances = new ArrayList<>(3);
		ICharmCapability capability = context.capability;
		List<ICharmInstance> charmInstances = capability.getCharmInstances();
		for (ICharmInstance charmInstance : charmInstances) {
			boolean isCharmUpdatable = true;
            ICharm charm = (ICharm)charmInstance.getCharm();
            // Treasure.logger.debug("{} charm allows multiple updates -> {}", charm.getName(), charm.isAllowMultipleUpdates());
			if (!charm.isAllowMultipleUpdates()) {
                // Treasure.logger.debug("{} charm denies multiple updates", charm.getName());
				// check if in list
				if (nonMultipleUpdateCharms.contains(charm.getType())) {
                    // Treasure.logger.debug("blacklist contains charm type -> {}", charm.getType());
					isCharmUpdatable = false;
				}
			}
			else {
                // Treasure.logger.debug("blacklist doesn't contain charm type -> {}", charm.getType());
				nonMultipleUpdateCharms.add(charm.getType());
			}
            
            // Treasure.logger.debug("is charm {} updatable -> {}", charm.getName(), isCharmUpdatable);
			if (isCharmUpdatable && 
					charmInstance.getCharm().update(player.world, new Random(), new Coords((int)player.posX, (int)player.posY, (int)player.posZ), player, event, charmInstance.getData())) {
				// send state message to client
				CharmMessageToClient message = new CharmMessageToClient(player.getName(), charmInstance, context.hand, context.slot);
//				Treasure.logger.debug("Message to client -> {}", message);
				Treasure.simpleNetworkWrapper.sendTo(message, player);
            }

            // mark Charm if instanceof ICharmable and no uses remain
            if (charmInstance.getData().getValue() <= 0.0 && charmInstance.getCharm() instanceof ICharmable) {
            	Treasure.logger.debug("charm is empty, add to remove list");
                removeInstances.add(charmInstance);
            }
        }
        
        // remove any charms that have no uses remaining
        if (!removeInstances.isEmpty()) {
            removeInstances.forEach(instance -> {
                charmInstances.remove(instance);
                // TODO send message to client to remove charm";
            });
        }
	}

	/**
	 * 
	 * @param player
	 * @param hand
	 * @return
	 */
	private Optional<CharmContext> getCharmContext(EntityPlayerMP player, EnumHand hand) {
		CharmContext context = new CharmContext();

		ItemStack heldStack = player.getHeldItem(hand);		
		context.hand = hand;
		context.itemStack = heldStack;

		if (heldStack.isEmpty()) {
			return Optional.empty();
		}

		Optional<CharmedType> type = getType(heldStack);
		if (!type.isPresent()) {
			return Optional.empty();
		}
		context.type = type.get();
		switch(context.type) {
		case ADORNMENT:
			context.capability = heldStack.getCapability(CharmableCapabilityProvider.CHARM_CAPABILITY, null);
			break;
		case CHARM:
			context.capability = heldStack.getCapability(CharmCapabilityProvider.CHARM_CAPABILITY, null);
			break;		
		}	
		return Optional.of(context);
	}

	@SuppressWarnings("incomplete-switch")
	private Optional<CharmContext> getCharmContext(EntityPlayerMP player, int hotbarSlot) {
		CharmContext context = new CharmContext();

		ItemStack stack = player.inventory.getStackInSlot(hotbarSlot);
		if (stack.isEmpty()) {
			return Optional.empty();
		}
		Optional<CharmedType> type = getType(stack);
		if (!type.isPresent()) {
			return Optional.empty();
		}

		context.hotbarSlot = hotbarSlot;
		context.slot = hotbarSlot;
		context.itemStack = stack;
		context.type = type.get();
		switch(context.type) {
		case ADORNMENT:
			context.capability = stack.getCapability(CharmableCapabilityProvider.CHARM_CAPABILITY, null);
			break;
		case CHARM:
			context.capability = stack.getCapability(CharmCapabilityProvider.CHARM_CAPABILITY, null);
			break;		
		}		
		return Optional.of(context);
	}

	/**
	 * 
	 * @param stack
	 * @return
	 */
	private Optional<CharmedType> getType(ItemStack stack) {
		CharmedType type = null;
		//		if(stack.hasCapability(CharmCapabilityProvider.CHARM_CAPABILITY, null)) {
		if (stack.getItem() instanceof ICharmed) {
			//			Treasure.logger.debug("has charm.charm capability");
			type = CharmedType.CHARM;
		}
		//		else if (stack.hasCapability(CharmableCapabilityProvider.CHARM_CAPABILITY, null)) {
		else if (stack.getItem() instanceof ICharmable) {
			//			Treasure.logger.debug("has adornment.charm capability");
			type = CharmedType.ADORNMENT;
		}
		else if(stack.getItem() instanceof IPouch && ((IPouch)stack.getItem()).getPouchType() == PouchType.ARCANE) {
			type = CharmedType.FOCUS;
		}

		return Optional.ofNullable(type);
	}

	/**
	 * 
	 * @param event
	 */
	@SubscribeEvent
	public void onTossCoinEvent(ItemTossEvent event) {
		if (WorldInfo.isClientSide(event.getPlayer().world)) {
			return;
		}

		Item item = event.getEntityItem().getItem().getItem();
		if (item instanceof IWishable) {
			ItemStack stack = event.getEntityItem().getItem();
			NBTTagCompound nbt = new NBTTagCompound();
			//			nbt.setString(IWishable.DROPPED_BY_KEY, event.getPlayer().getName());
			Treasure.logger.debug("player {}'s uuid -> {}", event.getPlayer().getName(), EntityPlayer.getUUID(event.getPlayer().getGameProfile()).toString());
			nbt.setString(IWishable.DROPPED_BY_KEY, EntityPlayer.getUUID(event.getPlayer().getGameProfile()).toString());
			stack.setTagCompound(nbt);			
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

	/**
	 * 
	 * @author Mark Gottschling on Apr 30, 2020
	 *
	 */
	private class CharmContext {
		EnumHand hand;
		Integer slot;
		Integer hotbarSlot;
		ItemStack itemStack;
		CharmedType type;
		ICharmCapability capability;

		CharmContext() {}
		//		CharmContext(ItemStack stack, EnumHand hand, Integer slot, CharmedType type) {
		//			this.itemStack = stack;
		//			this.hand = hand;
		//			this.slot = slot;
		//			this.type = type;
		//		}
		//		// TODO hotbarSlot should probably be an array/list
		//		CharmContext(ItemStack stack, EnumHand hand, Integer slot, Integer hotbarSlot, CharmedType type) {
		//			this(stack, hand, slot, type);
		//			this.hotbarSlot = hotbarSlot;
		//		}
	}

	private enum CharmedType {
		CHARM,
		FOCUS,
		ADORNMENT
	}
}
