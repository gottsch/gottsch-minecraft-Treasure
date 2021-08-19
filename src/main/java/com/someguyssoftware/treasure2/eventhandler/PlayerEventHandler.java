package com.someguyssoftware.treasure2.eventhandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.someguyssoftware.gottschcore.spatial.Coords;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.capability.CharmableCapability.InventoryType;
import com.someguyssoftware.treasure2.capability.CharmableCapabilityProvider;
import com.someguyssoftware.treasure2.capability.ICharmableCapability;
import com.someguyssoftware.treasure2.capability.TreasureCapabilities;
import com.someguyssoftware.treasure2.charm.CharmEntity;
import com.someguyssoftware.treasure2.charm.ICharm;
import com.someguyssoftware.treasure2.charm.ICharmEntity;
import com.someguyssoftware.treasure2.charm.TreasureCharms;
import com.someguyssoftware.treasure2.item.CoinItem;
import com.someguyssoftware.treasure2.item.IWishable;
import com.someguyssoftware.treasure2.item.PearlItem;
import com.someguyssoftware.treasure2.network.CharmMessageToClient;
import com.someguyssoftware.treasure2.network.TreasureNetworking;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.network.PacketDistributor;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypePreset;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

/**
 * @author Mark Gottschling on Apr 26, 2018
 *
 */
@Mod.EventBusSubscriber(modid = Treasure.MODID, bus = EventBusSubscriber.Bus.FORGE)
public class PlayerEventHandler {
	private static final String CURIOS_ID = "curios";
	private static final List<String> curiosSlots = Arrays.asList("necklace", "bracelet", "ring", "charm");

	/**
	 * changes to current (1.12.2) methodology
	 * [x] 1. Register Charm types with Events so only charms related to the event will be processed and can eliminate a instanceof check for every charm.
	 * [x] 2. Gather all charms to be processed first, filter if appropriate, then sort by priority, then execute them.
	 */

	/*
	 * Subscribing to multiple types of Living events for Charm Interactions so that instanceof doesn't have to be called everytime.
	 */

	/**
	 * 
	 * @param event
	 */
	@SubscribeEvent
	public static void checkCharmsInteraction(LivingUpdateEvent event) {
		if (WorldInfo.isClientSide(event.getEntity().level)) {
			return;
		}

		// do something to player every update tick:
		if (event.getEntity() instanceof PlayerEntity) {

			// get the player
			ServerPlayerEntity player = (ServerPlayerEntity) event.getEntity();
			processCharms(event, player);
		}
	}

	/**
	 * 
	 * @param event
	 * @param player
	 */
	private static void processCharms(Event event, ServerPlayerEntity player) {
		/*
		 * a list of charm contexts to execute
		 */
		List<CharmContext> charmsToExecute;

		Treasure.LOGGER.debug("processing charms...");
		// gather all charms
		charmsToExecute = gatherCharms(event, player);
		Treasure.LOGGER.debug("size of charms to execute -> {}", charmsToExecute.size());
		// TODO filter charms ??

		// sort charms
		Collections.sort(charmsToExecute, CharmContext.priorityComparator);

		// execute charms
		executeCharms(event, player, charmsToExecute);
	}

	/**
	 * Examine and collect  all Charms (not CharmEntity nor CharmItems) that the player has in valid slots.
	 * @param event
	 * @param player
	 * @return
	 */
	private static List<CharmContext> gatherCharms(Event event, ServerPlayerEntity player) {
		final List<CharmContext> contexts = new ArrayList<>(5);

		// get the slot provider - curios (general slots) or minecraft (hotbar)
		String slotProviderId  = ModList.get().isLoaded(CURIOS_ID) ? CURIOS_ID : "minecaft";

		// check each hand
		for (Hand hand : Hand.values()) {
			Treasure.LOGGER.debug("checking hand -> {}", hand.name());
			ItemStack heldStack = player.getItemInHand(hand);
			heldStack.getCapability(TreasureCapabilities.CHARMABLE_CAPABILITY).ifPresent(cap -> {
				Treasure.LOGGER.debug("hand charm has cap!");
				for (InventoryType type : InventoryType.values()) {
					Treasure.LOGGER.debug("hand charm checking inventory type -> {}", type);
					AtomicInteger index = new AtomicInteger();
					// requires indexed for-loop
					for (int i = 0; i < cap.getCharmEntities()[type.getValue()].size(); i++) {
						ICharmEntity entity =  cap.getCharmEntities()[type.getValue()].get(i);
						Treasure.LOGGER.debug("hand cap got charm entity -> {}", entity.getCharm().getName().toString());
						//					cap.getCharmEntities()[type.getValue()].forEach(entity -> {
						if (!TreasureCharms.isCharmEventRegistered(event.getClass(), entity.getCharm().getType())) {
							Treasure.LOGGER.debug("charm type -> {} is not register for this event -> {}", entity.getCharm().getType(), event.getClass().getSimpleName());
							continue;
						}
						index.set(i);
						CharmContext context  = new CharmContext.Builder().with($ -> {
							$.hand = hand;
							$.itemStack = heldStack;
							$.capability = cap;
							$.type = type;
							$.index = index.get();
							$.entity = entity;
						}).build();
						Treasure.LOGGER.debug("hand charm adding to context");
						contexts.add(context);
						//					});
					}
				}
			});
		}

		// check slots
		if (slotProviderId.equals(CURIOS_ID)) {
			// check curio slots
			LazyOptional<ICuriosItemHandler> handler = CuriosApi.getCuriosHelper().getCuriosHandler(player);
			handler.ifPresent(itemHandler -> {
				// curios type names -> head, necklace, back, bracelet, hands, ring, belt, charm, feet
				curiosSlots.forEach(slot -> {
					Optional<ICurioStacksHandler> stacksOptional = itemHandler.getStacksHandler(slot);
					stacksOptional.ifPresent(stacksHandler -> {
						ItemStack curiosStack = stacksHandler.getStacks().getStackInSlot(0);
						Treasure.LOGGER.debug("curios.charm slot stack -> {}", curiosStack.getDisplayName().getString());
						curiosStack.getCapability(TreasureCapabilities.CHARMABLE_CAPABILITY).ifPresent(cap -> {
							for (InventoryType type : InventoryType.values()) {
								AtomicInteger index = new AtomicInteger();
								// requires indexed for-loop
								for (int i = 0; i < cap.getCharmEntities()[type.getValue()].size(); i++) {
									ICharmEntity entity =  cap.getCharmEntities()[type.getValue()].get(i);
									Treasure.LOGGER.debug("curios cap got charm entity -> {}", entity.getCharm().getName().toString());

									//								cap.getCharmEntities()[type.getValue()].forEach(entity -> {
									if (!TreasureCharms.isCharmEventRegistered(event.getClass(), entity.getCharm().getType())) {
										Treasure.LOGGER.debug("charm type -> {} is not register for this event -> {}", entity.getCharm().getType(), event.getClass().getSimpleName());
										continue;
									}
									index.set(i);
									CharmContext curiosContext = new CharmContext.Builder().with($ -> {
										$.slotProviderId = slotProviderId;
										$.slot = slot;
										$.itemStack = curiosStack;
										$.capability = cap;
										$.type = type;
										$.index = index.get();
										$.entity = entity;
									}).build();
									Treasure.LOGGER.debug("curios charm adding to context");
									contexts.add(curiosContext);
								}
								//								});
							}
						});
					});
				});
			});
		}
		else {
			// TODO check hotbar slots
			// TODO only allow IAdornments from hotbar
		}
		return contexts;
	}

	/**
	 * 
	 * @param event
	 * @param player
	 * @param contexts
	 */
	private static void executeCharms(Event event, ServerPlayerEntity player, List<CharmContext> contexts) {
		Treasure.LOGGER.debug("executing charms...");
		/*
		 * a list of charm types that are non-stackable that should not be executed more than once.
		 */
		final List<String> executeOnceCharmTypes = new ArrayList<>(5);

		contexts.forEach(context -> {
			Treasure.LOGGER.debug("....charm -> {}", context.getEntity().getCharm().getName().toString());
			ICharm charm = (ICharm)context.getEntity().getCharm();
			if (!charm.isEffectStackable()) {
				// check if this charm type is already in the monitored list
				if (executeOnceCharmTypes.contains(charm.getType())) {
					return;
				}
				else {
					Treasure.LOGGER.debug("adding charm to execute once list");
					// add the charm type to the monitored list
					executeOnceCharmTypes.add(charm.getType());
				}
			}

			// if charm is executable and executes successfully
			if (context.getEntity().getCharm().update(player.level, new Random(), new Coords(player.position()), player, event, context.getEntity())) {
				// send state message to client
				//				CharmMessageToClient message = new CharmMessageToClient(player.getStringUUID(), entity, context.hand, context.slot, context.slotProviderId);
				//				Treasure.logger.debug("Message to client -> {}", message);
				//				TreasureNetworking.simpleChannel.send(PacketDistributor.PLAYER.with(() -> player), message);
			}

			// remove if uses are empty and the capability is bindable ie. charm, not adornment
			// NOTE this leaves empty charms on non-bindables for future recharge
			if (context.getEntity().getValue() <= 0.0 && context.capability.isBindable()) {
				Treasure.LOGGER.debug("charm is empty -> remove");
				// locate the charm from context and remove
				context.capability.getCharmEntities()[context.type.getValue()].remove(context.getIndex());
			}
		});
	}

	/**
	 * 
	 * @param event
	 */
	@SubscribeEvent
	public void onTossCoinEvent(ItemTossEvent event) {
		if (WorldInfo.isClientSide(event.getPlayer().level)) {
			return;
		}
		Treasure.LOGGER.debug("is remote? -> {}", !event.getPlayer().level.isClientSide);
		Treasure.LOGGER.debug("{} tossing item -> {}", event.getPlayer().getName().getString(), event.getEntityItem().getItem().getDisplayName().getString());
		Item item = event.getEntityItem().getItem().getItem();
		if (item instanceof IWishable) {
			ItemStack stack = event.getEntityItem().getItem();
			CompoundNBT nbt = new CompoundNBT();
			nbt.putString(IWishable.DROPPED_BY_KEY, event.getPlayer().getName().getString());
			Treasure.LOGGER.debug("adding tag to wishable stack...");
			stack.setTag(nbt);			
		}		
	}

	// TODO test - remove
	//	@SubscribeEvent
	//	public void onItemInfo(ItemTooltipEvent event) {
	//		if (event.getItemStack().getItem() == Items.EMERALD) {
	//			event.getToolTip().add(new TranslationTextComponent("tooltip.label.coin").withStyle(TextFormatting.GOLD, TextFormatting.ITALIC));
	//		}
	//	}

	private static class CharmContext {
		private Hand hand;
		private String slotProviderId;
		private String slot;
		private ItemStack itemStack;
		private ICharmableCapability capability;
		private InventoryType type;
		private int index;
		private ICharmEntity entity;

		/**
		 * 
		 */
		CharmContext() {}

		CharmContext(Builder builder) {
			this.hand = builder.hand;
			this.slotProviderId = builder.slotProviderId;
			this.slot = builder.slot;
			this.itemStack = builder.itemStack;
			this.capability = builder.capability;
			this.type = builder.type;
			this.index = builder.index;
			this.entity = builder.entity;
		}

		public static Comparator<CharmContext> priorityComparator = new Comparator<CharmContext>() {
			@Override
			public int compare(CharmContext p1, CharmContext p2) {
				// use p1 < p2 because the sort should be ascending
				if (p1.getEntity().getCharm().getPriority() < p2.getEntity().getCharm().getPriority()) {
					// greater than
					return 1;
				}
				else {
					// less than
					return -1;
				}
			}
		};

		public Hand getHand() {
			return hand;
		}

		public String getSlotProviderId() {
			return slotProviderId;
		}

		public String getSlot() {
			return slot;
		}

		public ItemStack getItemStack() {
			return itemStack;
		}

		public InventoryType getType() {
			return type;
		}

		public int getIndex() {
			return index;
		}

		public ICharmEntity getEntity() {
			return entity;
		}

		public static class Builder {
			public Hand hand;
			public String slotProviderId;
			public String slot;
			public ItemStack itemStack;
			public ICharmableCapability capability;
			public InventoryType type;
			public int index;
			public ICharmEntity entity;

			public Builder with(Consumer<Builder> builder)  {
				builder.accept(this);
				return this;
			}

			public CharmContext build() {
				return new CharmContext(this);
			}
		}

		@Override
		public String toString() {
			return "CharmContext [hand=" + hand + ", slotProviderId=" + slotProviderId + ", slot=" + slot
					+ ", itemStack=" + itemStack == null ? "N/A" : itemStack.getDisplayName().getString()
							+ "type=" + type + "]";
		}
	}
}
