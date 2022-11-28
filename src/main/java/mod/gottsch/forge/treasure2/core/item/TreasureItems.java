package mod.gottsch.forge.treasure2.core.item;

import java.util.List;
import java.util.function.Supplier;

import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.block.TreasureBlocks;
import mod.gottsch.forge.treasure2.core.capability.DurabilityCapability;
import mod.gottsch.forge.treasure2.core.capability.DurabilityHandler;
import mod.gottsch.forge.treasure2.core.capability.IDurabilityHandler;
import mod.gottsch.forge.treasure2.core.config.Config;
import mod.gottsch.forge.treasure2.core.setup.Registration;
import mod.gottsch.forge.treasure2.core.util.LangUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

/**
 * 
 * @author Mark Gottschling on Nov 9, 2022
 *
 */
@Mod.EventBusSubscriber(modid = Treasure.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TreasureItems {

	// item groups/tabs
	public static final CreativeModeTab TREASURE_ITEM_GROUP = new CreativeModeTab(Treasure.MODID) {
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(TreasureItems.TREASURE_TAB.get());
		}
	};

	public static final CreativeModeTab ADORNMENTS_ITEM_GROUP = new CreativeModeTab(Treasure.MODID + ".adornments_tab") {
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(TreasureItems.ADORNMENTS_TAB.get());
		}
	};	

	// item properties
	public static final Item.Properties TREASURE_ITEM_PROPERTIES = new Item.Properties().tab(TREASURE_ITEM_GROUP);
	public static final Supplier<Item.Properties> TREASURE_PROPS_SUPPLIER = () -> new Item.Properties().tab(TREASURE_ITEM_GROUP);

	// tab items
	public static final RegistryObject<Item> TREASURE_TAB = Registration.ITEMS.register("treasure_tab", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> ADORNMENTS_TAB = Registration.ITEMS.register("adornments_tab", () -> new Item(new Item.Properties()));

	// keys
	public static RegistryObject<KeyItem> WOOD_KEY = Registration.ITEMS.register("wood_key", () -> new KeyItem(new Item.Properties()
			.durability(Config.SERVER.keysAndLocks.woodKeyMaxUses.get())
			.tab(TREASURE_ITEM_GROUP))
			.setCategory(KeyLockCategory.ELEMENTAL)
			.setCraftable(false));

	public static RegistryObject<KeyItem> STONE_KEY = Registration.ITEMS.register("stone_key", () -> new KeyItem(new Item.Properties()
			.durability(Config.SERVER.keysAndLocks.stoneKeyMaxUses.get())
			.tab(TREASURE_ITEM_GROUP))
			.setCategory(KeyLockCategory.ELEMENTAL)
			.setCraftable(false));

	public static RegistryObject<KeyItem> LEAF_KEY = Registration.ITEMS.register("leaf_key", () -> new KeyItem(new Item.Properties()
			.durability(Config.SERVER.keysAndLocks.leafKeyMaxUses.get())
			.tab(TREASURE_ITEM_GROUP))
			.setCategory(KeyLockCategory.ELEMENTAL)
			.setCraftable(false));

	public static RegistryObject<KeyItem> EMBER_KEY = Registration.ITEMS.register("ember_key", () -> new KeyItem(new Item.Properties()
			.durability(Config.SERVER.keysAndLocks.emberKeyMaxUses.get())
			.tab(TREASURE_ITEM_GROUP))
			.setCategory(KeyLockCategory.ELEMENTAL)
			.setCraftable(false)
			.addFitsLock(lock -> {
				return 
						(lock == TreasureItems.EMBER_LOCK.get() ||
						lock == TreasureItems.WOOD_LOCK.get() ||
						lock == TreasureItems.LEAF_LOCK.get());
			})
			.addBreaksLock(lock -> {
				return (lock == TreasureItems.WOOD_LOCK.get() ||
						lock == TreasureItems.LEAF_LOCK.get());
			}));

	public static RegistryObject<KeyItem> LIGHTNING_KEY = Registration.ITEMS.register("lightning_key", () -> new LightningKey(
			new Item.Properties()
			.durability(Config.SERVER.keysAndLocks.lightningKeyMaxUses.get())
			.tab(TREASURE_ITEM_GROUP))
			.setCategory(KeyLockCategory.ELEMENTAL)
			.setBreakable(false)
			.setCraftable(false));

	public static RegistryObject<KeyItem> IRON_KEY = Registration.ITEMS.register("iron_key", () -> new KeyItem(new Item.Properties()
			.durability(Config.SERVER.keysAndLocks.ironKeyMaxUses.get())
			.tab(TREASURE_ITEM_GROUP))
			.setCategory(KeyLockCategory.METALS)
			.setCraftable(false));

	public static RegistryObject<KeyItem> GOLD_KEY = Registration.ITEMS.register("gold_key", () -> new KeyItem(new Item.Properties()
			.durability(Config.SERVER.keysAndLocks.goldKeyMaxUses.get())
			.tab(TREASURE_ITEM_GROUP))
			.setCategory(KeyLockCategory.METALS)
			.setCraftable(false));

	public static RegistryObject<KeyItem> METALLURGISTS_KEY = Registration.ITEMS.register("metallurgists_key", () -> new MetallurgistsKey(
			new Item.Properties()
			.durability(Config.SERVER.keysAndLocks.metallurgistsKeyMaxUses.get())
			.tab(TREASURE_ITEM_GROUP))
			.setCategory(KeyLockCategory.METALS)
			.setBreakable(false)
			.setCraftable(false));

	public static RegistryObject<KeyItem> DIAMOND_KEY = Registration.ITEMS.register("diamond_key", () -> new KeyItem(new Item.Properties()
			.durability(Config.SERVER.keysAndLocks.diamondKeyMaxUses.get())
			.tab(TREASURE_ITEM_GROUP))
			.setCategory(KeyLockCategory.GEMS)
			.setBreakable(false)
			.setCraftable(false));

	public static RegistryObject<KeyItem> EMERALD_KEY = Registration.ITEMS.register("emerald_key", () -> new KeyItem(new Item.Properties()
			.durability(Config.SERVER.keysAndLocks.emeraldKeyMaxUses.get())
			.tab(TREASURE_ITEM_GROUP))
			.setCategory(KeyLockCategory.GEMS)
			.setBreakable(false)
			.setCraftable(false));

	public static RegistryObject<KeyItem> RUBY_KEY = Registration.ITEMS.register("ruby_key", () -> new KeyItem(new Item.Properties()
			.durability(Config.SERVER.keysAndLocks.rubyKeyMaxUses.get())
			.tab(TREASURE_ITEM_GROUP))
			.setCategory(KeyLockCategory.GEMS)
			.setBreakable(false)
			.setCraftable(true));

	public static RegistryObject<KeyItem> SAPPHIRE_KEY = Registration.ITEMS.register("sapphire_key", () -> new KeyItem(new Item.Properties()
			.durability(Config.SERVER.keysAndLocks.sapphireKeyMaxUses.get())
			.tab(TREASURE_ITEM_GROUP))
			.setCategory(KeyLockCategory.GEMS)
			.setBreakable(false)
			.setCraftable(true));

	public static RegistryObject<KeyItem> JEWELLED_KEY = Registration.ITEMS.register("jewelled_key", 
			() -> new JewelledKey(new Item.Properties().durability(Config.SERVER.keysAndLocks.jewelledKeyMaxUses.get())
					.tab(TREASURE_ITEM_GROUP))
			.setCategory(KeyLockCategory.GEMS)
			.setBreakable(false)
			.setCraftable(false));


	public static RegistryObject<KeyItem> SPIDER_KEY = Registration.ITEMS.register("spider_key", () -> new KeyItem(new Item.Properties()
			.durability(Config.SERVER.keysAndLocks.spiderKeyMaxUses.get())
			.tab(TREASURE_ITEM_GROUP))
			.setCategory(KeyLockCategory.MOB)
			.setBreakable(true)
			.setCraftable(true));

	public static RegistryObject<KeyItem> WITHER_KEY = Registration.ITEMS.register("wither_key", () -> new KeyItem(new Item.Properties()
			.durability(Config.SERVER.keysAndLocks.witherKeyMaxUses.get())
			.tab(TREASURE_ITEM_GROUP))
			.setCategory(KeyLockCategory.WITHER)
			.setBreakable(false)
			.setCraftable(true));


	public static RegistryObject<KeyItem> SKELETON_KEY = Registration.ITEMS.register("skeleton_key", 
			() -> new SkeletonKey(new Item.Properties().durability(Config.SERVER.keysAndLocks.skeletonKeyMaxUses.get())
					.tab(TREASURE_ITEM_GROUP))
			.setCategory(KeyLockCategory.ELEMENTAL)
			.setBreakable(false)
			.setCraftable(false));

	public static RegistryObject<KeyItem> PILFERERS_LOCK_PICK = Registration.ITEMS.register("pilferers_lock_pick", 
			() -> new PilferersLockPick(new Item.Properties().durability(Config.SERVER.keysAndLocks.pilferersLockPickMaxUses.get())
					.tab(TREASURE_ITEM_GROUP))
			.setCategory(KeyLockCategory.ELEMENTAL)
			.setBreakable(true)
			.setCraftable(true)
			.setSuccessProbability(32)
			);

	public static RegistryObject<KeyItem> THIEFS_LOCK_PICK = Registration.ITEMS.register("thiefs_lock_pick", 
			() -> new ThiefsLockPick(new Item.Properties().durability(Config.SERVER.keysAndLocks.thiefsLockPickMaxUses.get())
					.tab(TREASURE_ITEM_GROUP))
			.setCategory(KeyLockCategory.ELEMENTAL)
			.setBreakable(true)
			.setCraftable(true)
			.setSuccessProbability(48)
			);

	// opens all locks and is infinite users
	public static RegistryObject<KeyItem> ONE_KEY = Registration.ITEMS.register("one_key", () -> new KeyItem(new Item.Properties()
			.durability(1000)
			.tab(TREASURE_ITEM_GROUP)
			) {
				@Override
				public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt) {
					IDurabilityHandler handler = new DurabilityHandler();
					handler.setInfinite(true);
					return new DurabilityCapability(handler);
				};
				
				@Override
				public boolean isFoil(ItemStack stack) {
					return true;
				}
		
				@Override
				public Component getName(ItemStack stack) {
					return ((TranslatableComponent)super.getName(stack)).withStyle(ChatFormatting.YELLOW);
				}
				
				@Override
				public  void appendHoverSpecials(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
					new TranslatableComponent(LangUtil.tooltip("key_lock.specials"), 
							ChatFormatting.GOLD + new TranslatableComponent(LangUtil.tooltip("key_lock.one_key.specials")).getString());
				}
				@Override
				public  void appendHoverExtras(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
					tooltip.add(new TextComponent(LangUtil.NEWLINE));
					tooltip.add(new TextComponent(LangUtil.INDENT4)
							.append(new TranslatableComponent(LangUtil.tooltip("key_lock.one_key.lore"))
							.append(new TextComponent(LangUtil.INDENT4)).withStyle(ChatFormatting.LIGHT_PURPLE).withStyle(ChatFormatting.ITALIC)));
					tooltip.add(new TextComponent(LangUtil.NEWLINE));
						}
				
				@Override
				public void appendCurse(ItemStack stack, List<Component> tooltip) {
					if (!EnchantmentHelper.hasVanishingCurse(stack)) {
						stack.enchant(Enchantments.VANISHING_CURSE, 1);
					}
				}		
			}
			.setCategory(KeyLockCategory.MAGIC)
			.setBreakable(false)
			.setCraftable(false)
			.addFitsLock(lock -> {
				return true;
			}));

	// FUTURE
	// opens all epic/rare/scarce/uncommon/common
	public static RegistryObject<KeyItem> DRAGON_KEY;
	public static RegistryObject<KeyItem> MASTER_KEY;	

	// locks
	public static final Supplier<Item.Properties> LOCK_ITEM_PROPERTIES = () -> new Item.Properties().tab(TREASURE_ITEM_GROUP);

	public static RegistryObject<LockItem> WOOD_LOCK = Registration.ITEMS.register("wood_lock", () -> new LockItem(LOCK_ITEM_PROPERTIES.get(), new KeyItem[] {WOOD_KEY.get(), LIGHTNING_KEY.get()})
			.setCategory(KeyLockCategory.ELEMENTAL));

	public static RegistryObject<LockItem> STONE_LOCK = Registration.ITEMS.register("stone_lock", () -> new LockItem(LOCK_ITEM_PROPERTIES.get(), new KeyItem[] {STONE_KEY.get(), LIGHTNING_KEY.get()})
			.setCategory(KeyLockCategory.ELEMENTAL));

	public static RegistryObject<LockItem> EMBER_LOCK = Registration.ITEMS.register("ember_lock", () -> new LockItem(LOCK_ITEM_PROPERTIES.get(), new KeyItem[] {EMBER_KEY.get(), LIGHTNING_KEY.get()})
			.setCategory(KeyLockCategory.ELEMENTAL)
			.addBreaksKey(key -> {
				return (key != TreasureItems.EMBER_KEY.get() &&
						key != TreasureItems.LIGHTNING_KEY.get());
			}));

	public static RegistryObject<LockItem> LEAF_LOCK = Registration.ITEMS.register("leaf_lock", () -> new LockItem(LOCK_ITEM_PROPERTIES.get(), new KeyItem[] {LEAF_KEY.get(), LIGHTNING_KEY.get()})
			.setCategory(KeyLockCategory.ELEMENTAL));

	public static RegistryObject<LockItem> IRON_LOCK = Registration.ITEMS.register("iron_lock", () -> new LockItem(LOCK_ITEM_PROPERTIES.get(), new KeyItem[] {IRON_KEY.get(), METALLURGISTS_KEY.get()})
			.setCategory(KeyLockCategory.METALS));
	public static RegistryObject<LockItem> GOLD_LOCK = Registration.ITEMS.register("gold_lock", () -> new LockItem(LOCK_ITEM_PROPERTIES.get(), new KeyItem[] {GOLD_KEY.get(), METALLURGISTS_KEY.get()})
			.setCategory(KeyLockCategory.METALS));

	public static RegistryObject<LockItem> DIAMOND_LOCK = Registration.ITEMS.register("diamond_lock", () -> new LockItem(LOCK_ITEM_PROPERTIES.get(), new KeyItem[] {DIAMOND_KEY.get(), JEWELLED_KEY.get()})
			.setCategory(KeyLockCategory.GEMS));
	public static RegistryObject<LockItem> EMERALD_LOCK = Registration.ITEMS.register("emerald_lock", () -> new LockItem(LOCK_ITEM_PROPERTIES.get(), new KeyItem[] {EMERALD_KEY.get(), JEWELLED_KEY.get()})
			.setCategory(KeyLockCategory.GEMS));
	public static RegistryObject<LockItem> RUBY_LOCK = Registration.ITEMS.register("ruby_lock", () -> new LockItem(LOCK_ITEM_PROPERTIES.get(), new KeyItem[] {RUBY_KEY.get(), JEWELLED_KEY.get()})
			.setCategory(KeyLockCategory.GEMS));
	public static RegistryObject<LockItem> SAPPHIRE_LOCK = Registration.ITEMS.register("sapphire_lock", () -> new LockItem(LOCK_ITEM_PROPERTIES.get(), new KeyItem[] {SAPPHIRE_KEY.get(), JEWELLED_KEY.get()})
			.setCategory(KeyLockCategory.GEMS));	

	public static RegistryObject<LockItem> SPIDER_LOCK = Registration.ITEMS.register("spider_lock", () -> new LockItem(LOCK_ITEM_PROPERTIES.get(), new KeyItem[] {SPIDER_KEY.get()})
			.setCategory(KeyLockCategory.MOB));

	public static RegistryObject<LockItem> WITHER_LOCK = Registration.ITEMS.register("wither_lock", () -> new LockItem(LOCK_ITEM_PROPERTIES.get(), new KeyItem[] {WITHER_KEY.get()})
			.setCategory(KeyLockCategory.WITHER));

	// keyring
	public static RegistryObject<KeyRingItem> KEY_RING = Registration.ITEMS.register("key_ring", () -> new KeyRingItem(TREASURE_PROPS_SUPPLIER.get()));

	// wealth items
	public static RegistryObject<Item> COPPER_COIN = Registration.ITEMS.register("copper_coin", () -> new WealthItem(TREASURE_PROPS_SUPPLIER.get()));
	public static RegistryObject<Item> SILVER_COIN = Registration.ITEMS.register("silver_coin", () -> new WealthItem(TREASURE_PROPS_SUPPLIER.get()));
	public static RegistryObject<Item> GOLD_COIN = Registration.ITEMS.register("gold_coin", () -> new WealthItem(TREASURE_PROPS_SUPPLIER.get()));
	public static RegistryObject<Item> TOPAZ = Registration.ITEMS.register("topaz", () -> new WealthItem(TREASURE_PROPS_SUPPLIER.get()));
	public static RegistryObject<Item> ONYX = Registration.ITEMS.register("onyx", () -> new WealthItem(TREASURE_PROPS_SUPPLIER.get()));
	public static RegistryObject<Item> RUBY = Registration.ITEMS.register("ruby", () -> new WealthItem(TREASURE_PROPS_SUPPLIER.get()));
	public static RegistryObject<Item> SAPPHIRE = Registration.ITEMS.register("sapphire", () -> new WealthItem(TREASURE_PROPS_SUPPLIER.get()));
	public static RegistryObject<Item> WHITE_PEARL = Registration.ITEMS.register("white_pearl", () -> new WealthItem(TREASURE_PROPS_SUPPLIER.get()));
	public static RegistryObject<Item> BLACK_PEARL = Registration.ITEMS.register("black_pearl", () -> new WealthItem(TREASURE_PROPS_SUPPLIER.get()));
	
	// pouch
	public static RegistryObject<PouchItem> POUCH = Registration.ITEMS.register("pouch", () -> new PouchItem(TREASURE_PROPS_SUPPLIER.get()));
		
	// other
	public static RegistryObject<Item> TREASURE_TOOL = Registration.ITEMS.register("treasure_tool", () -> new Item(TREASURE_PROPS_SUPPLIER.get()));

	// block items
	public static final RegistryObject<Item> WOOD_CHEST_ITEM = fromChestBlock(TreasureBlocks.WOOD_CHEST, TREASURE_PROPS_SUPPLIER);
	public static final RegistryObject<Item> SPANISH_MOSS_ITEM = fromBlock(TreasureBlocks.SPANISH_MOSS, TREASURE_PROPS_SUPPLIER);
	
	public static final RegistryObject<Item> TOPAZ_ORE_ITEM = fromBlock(TreasureBlocks.TOPAZ_ORE, TREASURE_PROPS_SUPPLIER);
	public static final RegistryObject<Item> DEEPSLATE_TOPAZ_ORE_ITEM = fromBlock(TreasureBlocks.DEEPSLATE_TOPAZ_ORE, TREASURE_PROPS_SUPPLIER);
	
	public static final RegistryObject<Item> ONYX_ORE_ITEM = fromBlock(TreasureBlocks.ONYX_ORE, TREASURE_PROPS_SUPPLIER);
	public static final RegistryObject<Item> DEEPSLATE_ONYX_ORE_ITEM = fromBlock(TreasureBlocks.DEEPSLATE_ONYX_ORE, TREASURE_PROPS_SUPPLIER);

	public static final RegistryObject<Item> RUBY_ORE_ITEM = fromBlock(TreasureBlocks.RUBY_ORE, TREASURE_PROPS_SUPPLIER);
	public static final RegistryObject<Item> DEEPSLATE_RUBY_ORE_ITEM = fromBlock(TreasureBlocks.DEEPSLATE_RUBY_ORE, TREASURE_PROPS_SUPPLIER);

	public static final RegistryObject<Item> SAPPHIRE_ORE_ITEM = fromBlock(TreasureBlocks.SAPPHIRE_ORE, TREASURE_PROPS_SUPPLIER);
	public static final RegistryObject<Item> DEEPSLATE_SAPPHIRE_ORE_ITEM = fromBlock(TreasureBlocks.DEEPSLATE_SAPPHIRE_ORE, TREASURE_PROPS_SUPPLIER);

	public static final RegistryObject<Item> WISHING_WELL_ITEM = fromBlock(TreasureBlocks.WISHING_WELL, TREASURE_PROPS_SUPPLIER);
	public static final RegistryObject<Item> DESERT_WISHING_WELL_ITEM = fromBlock(TreasureBlocks.DESERT_WISHING_WELL, TREASURE_PROPS_SUPPLIER);
	
	/**
	 * 
	 */
	public static void register() {
		// cycle through all block and create items
		Registration.registerItems();
	}

	// conveniance method: take a RegistryObject<Block> and make a corresponding RegistryObject<Item> from it
	public static <B extends Block> RegistryObject<Item> fromBlock(RegistryObject<B> block, Supplier<Item.Properties> itemProperties) {
		return Registration.ITEMS.register(block.getId().getPath(), () -> new BlockItem(block.get(), itemProperties.get()));
	}

	public static <B extends Block> RegistryObject<Item> fromChestBlock(RegistryObject<B> block, Supplier<Item.Properties> itemProperties) {
		return Registration.ITEMS.register(block.getId().getPath(), () -> new TreasureChestBlockItem(block.get(), itemProperties.get()));
	}
}
