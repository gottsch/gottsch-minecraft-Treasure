package mod.gottsch.forge.treasure2.core.item;

import java.util.function.Supplier;

import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.block.TreasureBlocks;
import mod.gottsch.forge.treasure2.core.config.Config;
import mod.gottsch.forge.treasure2.core.setup.Registration;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
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

	// FUTURE
	// opens all epic/rare/scarce/uncommon/common
	public static RegistryObject<KeyItem> DRAGON_KEY;
	public static RegistryObject<KeyItem> MASTER_KEY;
	// opens all locks and is infinite users
	public static RegistryObject<KeyItem> ONE_KEY;
	
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

	
	// block items
	public static final RegistryObject<Item> WOOD_CHEST_ITEM = fromChestBlock(TreasureBlocks.WOOD_CHEST, TREASURE_PROPS_SUPPLIER);


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
