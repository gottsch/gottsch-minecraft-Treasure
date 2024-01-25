package mod.gottsch.forge.treasure2.core.item;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import com.google.common.collect.Maps;

import mod.gottsch.forge.gottschcore.enums.IRarity;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.block.TreasureBlocks;
import mod.gottsch.forge.treasure2.core.entity.TreasureEntities;
import mod.gottsch.forge.treasure2.core.enums.Rarity;
import mod.gottsch.forge.treasure2.core.item.weapon.Axe;
import mod.gottsch.forge.treasure2.core.item.weapon.Sword;
import mod.gottsch.forge.treasure2.core.item.weapon.TreasureWeapons;
import mod.gottsch.forge.treasure2.core.material.TreasureArmorMaterial;
import mod.gottsch.forge.treasure2.core.setup.Registration;
import mod.gottsch.forge.treasure2.core.util.LangUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeableArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

/**
 * 
 * @author Mark Gottschling on Nov 9, 2022
 *
 */
@Mod.EventBusSubscriber(modid = Treasure.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TreasureItems {
	// tab items
	public static final RegistryObject<Item> LOGO = Registration.ITEMS.register("treasure_tab", () -> new Item(new Item.Properties()));

	// item properties
	public static final Item.Properties TREASURE_ITEM_PROPERTIES = new Item.Properties();
	public static final Supplier<Item.Properties> TREASURE_PROPS_SUPPLIER = () -> new Item.Properties();

	// 
	public static final Map<RegistryObject<Block>, RegistryObject<Item>> BLOCK_ITEM_MAP = Maps.newHashMap();
	
	// treasure tool
	public static RegistryObject<Item> TREASURE_TOOL = Registration.ITEMS.register("treasure_tool", () -> new TreasureToolItem(TREASURE_PROPS_SUPPLIER.get()));

	// keys
	public static RegistryObject<KeyItem> WOOD_KEY = Registration.ITEMS.register("wood_key", () -> new KeyItem(new Item.Properties()
//			.durability(Config.SERVER.keysAndLocks.woodKeyMaxUses.get())
			)
			.setCategory(KeyLockCategory.ELEMENTAL)
			.setCraftable(false));

	public static RegistryObject<KeyItem> STONE_KEY = Registration.ITEMS.register("stone_key", () -> new KeyItem(new Item.Properties()
//			.durability(Config.SERVER.keysAndLocks.stoneKeyMaxUses.get())
			)
			.setCategory(KeyLockCategory.ELEMENTAL)
			.setCraftable(false));

	public static RegistryObject<KeyItem> LEAF_KEY = Registration.ITEMS.register("leaf_key", () -> new KeyItem(new Item.Properties()
//			.durability(Config.SERVER.keysAndLocks.leafKeyMaxUses.get())
			)
			.setCategory(KeyLockCategory.ELEMENTAL)
			.setCraftable(false));

	public static RegistryObject<KeyItem> EMBER_KEY = Registration.ITEMS.register("ember_key", () -> new KeyItem(new Item.Properties())
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
			new Item.Properties())
			.setCategory(KeyLockCategory.ELEMENTAL)
			.setBreakable(false)
			.setCraftable(false));

	public static RegistryObject<KeyItem> IRON_KEY = Registration.ITEMS.register("iron_key", () -> new KeyItem(new Item.Properties())
			.setCategory(KeyLockCategory.METALS)
			.setCraftable(false));

	public static RegistryObject<KeyItem> GOLD_KEY = Registration.ITEMS.register("gold_key", () -> new KeyItem(new Item.Properties())
			.setCategory(KeyLockCategory.METALS)
			.setCraftable(false));

	public static RegistryObject<KeyItem> METALLURGISTS_KEY = Registration.ITEMS.register("metallurgists_key", () -> new MetallurgistsKey(
			new Item.Properties())
			.setCategory(KeyLockCategory.METALS)
			.setBreakable(false)
			.setCraftable(false));
	
	public static RegistryObject<KeyItem> DIAMOND_KEY = Registration.ITEMS.register("diamond_key", () -> new KeyItem(new Item.Properties())
			.setCategory(KeyLockCategory.GEMS)
			.setBreakable(false)
			.setCraftable(false));

	public static RegistryObject<KeyItem> EMERALD_KEY = Registration.ITEMS.register("emerald_key", () -> new KeyItem(new Item.Properties())
			.setCategory(KeyLockCategory.GEMS)
			.setBreakable(false)
			.setCraftable(false));

	public static RegistryObject<KeyItem> TOPAZ_KEY = Registration.ITEMS.register("topaz_key", () -> new KeyItem(new Item.Properties())
			.setCategory(KeyLockCategory.GEMS)
			.setBreakable(false)
			.setCraftable(true));
	
	public static RegistryObject<KeyItem> ONYX_KEY = Registration.ITEMS.register("onyx_key", () -> new KeyItem(new Item.Properties())
			.setCategory(KeyLockCategory.GEMS)
			.setBreakable(false)
			.setCraftable(true));
	
	public static RegistryObject<KeyItem> RUBY_KEY = Registration.ITEMS.register("ruby_key", () -> new KeyItem(new Item.Properties())
			.setCategory(KeyLockCategory.GEMS)
			.setBreakable(false)
			.setCraftable(true));

	public static RegistryObject<KeyItem> SAPPHIRE_KEY = Registration.ITEMS.register("sapphire_key", () -> new KeyItem(new Item.Properties())
			.setCategory(KeyLockCategory.GEMS)
			.setBreakable(false)
			.setCraftable(true));

	public static RegistryObject<KeyItem> JEWELLED_KEY = Registration.ITEMS.register("jewelled_key", 
			() -> new JewelledKey(new Item.Properties())
			.setCategory(KeyLockCategory.GEMS)
			.setBreakable(false)
			.setCraftable(false));


	public static RegistryObject<KeyItem> SPIDER_KEY = Registration.ITEMS.register("spider_key", () -> new KeyItem(new Item.Properties())
			.setCategory(KeyLockCategory.MOB)
			.setBreakable(true)
			.setCraftable(true));

	public static RegistryObject<KeyItem> WITHER_KEY = Registration.ITEMS.register("wither_key", () -> new KeyItem(new Item.Properties())
			.setCategory(KeyLockCategory.WITHER)
			.setBreakable(false)
			.setCraftable(true));


	public static RegistryObject<KeyItem> SKELETON_KEY = Registration.ITEMS.register("skeleton_key", 
			() -> new SkeletonKey(new Item.Properties())
			.setCategory(KeyLockCategory.ELEMENTAL)
			.setBreakable(false)
			.setCraftable(false));

	public static RegistryObject<KeyItem> PILFERERS_LOCK_PICK = Registration.ITEMS.register("pilferers_lock_pick", 
			() -> new PilferersLockPick(new Item.Properties())
			.setCategory(KeyLockCategory.ELEMENTAL)
			.setBreakable(true)
			.setCraftable(true)
			.setSuccessProbability(32)
			);

	public static RegistryObject<KeyItem> THIEFS_LOCK_PICK = Registration.ITEMS.register("thiefs_lock_pick", 
			() -> new ThiefsLockPick(new Item.Properties())
			.setCategory(KeyLockCategory.ELEMENTAL)
			.setBreakable(true)
			.setCraftable(true)
			.setSuccessProbability(48)
			);

	// TODO update
	// opens all locks and is infinite users
	public static RegistryObject<KeyItem> ONE_KEY = Registration.ITEMS.register("one_key",
			() -> new KeyItem(new Item.Properties().durability(1000), true) {

		@Override
		public boolean isFoil(ItemStack stack) {
			return true;
		}

		@Override
		public Component getName(ItemStack stack) {
			return ((MutableComponent)super.getName(stack)).withStyle(ChatFormatting.YELLOW);
		}

		@Override
		public  void appendHoverSpecials(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
			tooltip.add(
					Component.translatable(LangUtil.tooltip("key_lock.specials"), 
							ChatFormatting.GOLD + Component.translatable(LangUtil.tooltip("key_lock.one_key.specials")).getString())
					);
		}
		@Override
		public  void appendHoverExtras(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
			tooltip.add(Component.literal(LangUtil.NEWLINE));
			tooltip.add(Component.literal(LangUtil.INDENT4)
					.append(Component.translatable(LangUtil.tooltip("key_lock.one_key.lore"))
							.append(Component.literal(LangUtil.INDENT4)).withStyle(ChatFormatting.LIGHT_PURPLE).withStyle(ChatFormatting.ITALIC)));
			tooltip.add(Component.literal(LangUtil.NEWLINE));
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
	// opens any lock
	.addFitsLock(lock -> {
		return true;
	}));

	// FUTURE
	// opens all epic/rare/scarce/uncommon/common but not infinite use
	public static RegistryObject<KeyItem> DRAGON_KEY;
	// opens all locks but is one time use only
	public static RegistryObject<KeyItem> MASTER_KEY;	

	// locks
	public static final Supplier<Item.Properties> LOCK_ITEM_PROPERTIES = () -> new Item.Properties();

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
	
	public static RegistryObject<LockItem> TOPAZ_LOCK = Registration.ITEMS.register("topaz_lock", () -> new LockItem(LOCK_ITEM_PROPERTIES.get(), new KeyItem[] {TOPAZ_KEY.get(), JEWELLED_KEY.get()})
			.setCategory(KeyLockCategory.GEMS));
	public static RegistryObject<LockItem> ONYX_LOCK = Registration.ITEMS.register("onyx_lock", () -> new LockItem(LOCK_ITEM_PROPERTIES.get(), new KeyItem[] {ONYX_KEY.get(), JEWELLED_KEY.get()})
			.setCategory(KeyLockCategory.GEMS));	
	
	public static RegistryObject<LockItem> RUBY_LOCK = Registration.ITEMS.register("ruby_lock", () -> new LockItem(LOCK_ITEM_PROPERTIES.get(), new KeyItem[] {RUBY_KEY.get(), JEWELLED_KEY.get()})
			.setCategory(KeyLockCategory.GEMS));
	public static RegistryObject<LockItem> SAPPHIRE_LOCK = Registration.ITEMS.register("sapphire_lock", () -> new LockItem(LOCK_ITEM_PROPERTIES.get(), new KeyItem[] {SAPPHIRE_KEY.get(), JEWELLED_KEY.get()})
			.setCategory(KeyLockCategory.GEMS));	

	public static RegistryObject<LockItem> SPIDER_LOCK = Registration.ITEMS.register("spider_lock", () -> new LockItem(LOCK_ITEM_PROPERTIES.get(), new KeyItem[] {SPIDER_KEY.get()})
			.setCategory(KeyLockCategory.MOB));

	public static RegistryObject<LockItem> WITHER_LOCK = Registration.ITEMS.register("wither_lock", () -> new LockItem(LOCK_ITEM_PROPERTIES.get(), new KeyItem[] {WITHER_KEY.get()}) {
		@Override
		public IRarity getRarity() {
			return Rarity.SCARCE;
		};
	}.setCategory(KeyLockCategory.WITHER));

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

	// block items
	public static RegistryObject<WitherStickItem> WITHER_STICK_ITEM = Registration.ITEMS.register("wither_stick_item", () -> new WitherStickItem(TreasureBlocks.WITHER_BRANCH.get(), TREASURE_PROPS_SUPPLIER.get()));
	public static RegistryObject<WitherRootItem> WITHER_ROOT_ITEM = Registration.ITEMS.register("wither_root_item", () -> new WitherRootItem(TreasureBlocks.WITHER_ROOT.get(), TREASURE_PROPS_SUPPLIER.get()));
	public static RegistryObject<Item> WITHER_LOG_ITEM = fromBlock(TreasureBlocks.WITHER_LOG, TREASURE_PROPS_SUPPLIER);
	public static RegistryObject<Item> WITHER_BROKEN_LOG_ITEM = fromBlock(TreasureBlocks.WITHER_BROKEN_LOG, TREASURE_PROPS_SUPPLIER);
	public static RegistryObject<Item> WITHER_SOUL_LOG_ITEM = fromBlock(TreasureBlocks.WITHER_SOUL_LOG, TREASURE_PROPS_SUPPLIER);
	public static RegistryObject<Item> WISHING_PLANKS_ITEM = fromBlock(TreasureBlocks.WITHER_PLANKS, TREASURE_PROPS_SUPPLIER);
	
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

	public static final RegistryObject<Item> SKELETON_ITEM = Registration.ITEMS.register("skeleton", () -> new SkeletonItem(TreasureBlocks.SKELETON.get(), TREASURE_PROPS_SUPPLIER.get()));

	// vanity items
	public static final RegistryObject<Item> EYE_PATCH = Registration.ITEMS.register("eye_patch", 
			() ->  new DyeableArmorItem(TreasureArmorMaterial.PATCH, ArmorItem.Type.HELMET, TREASURE_PROPS_SUPPLIER.get()));

	// eggs
	public static final RegistryObject<Item> BOUND_SOUL_EGG = Registration.ITEMS.register("bound_soul_egg", () -> new ForgeSpawnEggItem(TreasureEntities.BOUND_SOUL_ENTITY_TYPE, 0x000000, 0x2b2b2b, TREASURE_PROPS_SUPPLIER.get()));
	public static final RegistryObject<Item> WOOD_CHEST_MIMIC_EGG = Registration.ITEMS.register("wood_chest_mimic_egg", () -> new ForgeSpawnEggItem(TreasureEntities.WOOD_CHEST_MIMIC_ENTITY_TYPE, 0x9f844d, 0x54442c, TREASURE_PROPS_SUPPLIER.get()));
	public static final RegistryObject<Item> PIRATE_CHEST_MIMIC_EGG = Registration.ITEMS.register("pirate_chest_mimic_egg", () -> new ForgeSpawnEggItem(TreasureEntities.PIRATE_CHEST_MIMIC_ENTITY_TYPE, 0x010101, 0x3b3b3b, TREASURE_PROPS_SUPPLIER.get()));
	public static final RegistryObject<Item> VIKING_CHEST_MIMIC_EGG = Registration.ITEMS.register("viking_chest_mimic_egg", () -> new ForgeSpawnEggItem(TreasureEntities.VIKING_CHEST_MIMIC_ENTITY_TYPE, 0x642e1e, 0x753c27, TREASURE_PROPS_SUPPLIER.get()));
	public static final RegistryObject<Item> CAULDRON_CHEST_MIMIC_EGG = Registration.ITEMS.register("cauldron_chest_mimic_egg", () -> new ForgeSpawnEggItem(TreasureEntities.CAULDRON_CHEST_MIMIC_ENTITY_TYPE, 0x6e5c30, 0x4a4a4a, TREASURE_PROPS_SUPPLIER.get()));
	public static final RegistryObject<Item> CRATE_CHEST_MIMIC_EGG = Registration.ITEMS.register("crate_chest_mimic_egg", () -> new ForgeSpawnEggItem(TreasureEntities.CRATE_CHEST_MIMIC_ENTITY_TYPE, 0x6e5c60, 0x434343, TREASURE_PROPS_SUPPLIER.get()));
	public static final RegistryObject<Item> MOLDY_CRATE_CHEST_MIMIC_EGG = Registration.ITEMS.register("moldy_crate_chest_mimic_egg", () -> new ForgeSpawnEggItem(TreasureEntities.MOLDY_CRATE_CHEST_MIMIC_ENTITY_TYPE, 0x635360, 0x464646, TREASURE_PROPS_SUPPLIER.get()));
	public static final RegistryObject<Item> CARDBOARD_BOX_MIMIC_EGG = Registration.ITEMS.register("cardboard_box_mimic_egg", () -> new ForgeSpawnEggItem(TreasureEntities.CARDBOARD_BOX_MIMIC_ENTITY_TYPE, 0x6f5e60, 0x404040, TREASURE_PROPS_SUPPLIER.get()));

	// tiers
	public static final ForgeTier COPPER = new ForgeTier(1, 200, 5.0F, 1.0F, 10, BlockTags.NEEDS_STONE_TOOL, () -> Ingredient.of(Items.COPPER_INGOT));
	public static final ForgeTier STEEL = new ForgeTier(2, 600, 6.5F, 2.5F, 15, BlockTags.NEEDS_IRON_TOOL, () -> Ingredient.of(Items.IRON_INGOT));
	public static final ForgeTier BONE = new ForgeTier(2, 200, 6.25F, 2F, 16, BlockTags.NEEDS_IRON_TOOL, () -> Ingredient.of(Items.BONE));
	public static final ForgeTier SHADOW = new ForgeTier(3, 1600, 9.0F, 4.0F, 15, BlockTags.NEEDS_DIAMOND_TOOL, () -> Ingredient.of(Items.NETHERITE_INGOT));

	// special tiers
	public static final ForgeTier SKULL = new ForgeTier(3, 1800, 9.0F, 4.0F, 15, BlockTags.NEEDS_IRON_TOOL, () -> Ingredient.of(Items.SKELETON_SKULL));
	public static final ForgeTier RARE = new ForgeTier(4, 1700, 9.5F, 3.0F, 18, BlockTags.NEEDS_IRON_TOOL, () -> Ingredient.of(Items.DIAMOND));
	public static final ForgeTier EPIC = new ForgeTier(4, 1800, 9.5F, 4.5F, 18, BlockTags.NEEDS_IRON_TOOL, () -> Ingredient.of(Items.DIAMOND));
	public static final ForgeTier LEGENDARY = new ForgeTier(5, 2200, 10.0F, 5.0F, 20, BlockTags.NEEDS_IRON_TOOL, () -> Ingredient.of(Items.DIAMOND));
	public static final ForgeTier MYTHICAL = new ForgeTier(6, 2400, 11.0F, 6.0F, 22, BlockTags.NEEDS_IRON_TOOL, () -> Ingredient.of(Items.NETHERITE_INGOT));

	// TEMP References
	//	   WOOD(0, 59, 2.0F, 0.0F, 15, 
	//	   STONE(1, 131, 4.0F, 1.0F, 5, 
	//	   IRON(2, 250, 6.0F, 2.0F, 14, 
	//	   DIAMOND(3, 1561, 8.0F, 3.0F, 10, 
	//	   GOLD(0, 32, 12.0F, 0.0F, 22, 
	//	   NETHERITE(4, 2031, 9.0F, 4.0F, 15, 

	//	STONE_SWORD = new SwordItem(Tiers.STONE, 3, -2.4F, (new Item.Properties()).tab(CreativeModeTab.TAB_COMBAT)));
	//	IRON_SWORD = new SwordItem(Tiers.IRON, 3, -2.4F, (new Item.Properties()).tab(CreativeModeTab.TAB_COMBAT)));
	//	DIAMOND = SwordItem(Tiers.DIAMOND, 3, -2.4F



	// HAMMER/MACE = new Sword(Tier, 8.0F, -4.0 // most damage, slowest, + 2x durability damage to target
	
	/*
	 *  swords
	 */
	// short swords (copper, iron, steel)
	public static final RegistryObject<Item> COPPER_SHORT_SWORD = Registration.ITEMS.register("copper_short_sword", 
			() -> new Sword(COPPER, 2.5f, -2.0F, TREASURE_ITEM_PROPERTIES));

	public static final RegistryObject<Item> CHIPPED_COPPER_SHORT_SWORD = Registration.ITEMS.register("chipped_copper_short_sword", 
			() -> new Sword(COPPER, 2.4f, -2.0F, TREASURE_ITEM_PROPERTIES));

	public static final RegistryObject<Item> IRON_SHORT_SWORD = Registration.ITEMS.register("iron_short_sword", 
			() -> new Sword(Tiers.IRON, 2.5f, -2.0F, TREASURE_ITEM_PROPERTIES));

	public static final RegistryObject<Item> CHIPPED_IRON_SHORT_SWORD = Registration.ITEMS.register("chipped_iron_short_sword", 
			() -> new Sword(Tiers.IRON, 2.4f, -2.0F, TREASURE_ITEM_PROPERTIES));

	public static final RegistryObject<Item> STEEL_SHORT_SWORD = Registration.ITEMS.register("steel_short_sword", 
			() -> new Sword(STEEL, 2.5f, -2.0F, TREASURE_ITEM_PROPERTIES));

	public static final RegistryObject<Item> CHIPPED_STEEL_SHORT_SWORD = Registration.ITEMS.register("chipped_steel_short_sword", 
			() -> new Sword(STEEL, 2.4f, -2.0F, TREASURE_ITEM_PROPERTIES));

	// rapier
	public static final RegistryObject<Item> COPPER_RAPIER = Registration.ITEMS.register("copper_rapier", 
			() -> new Sword(COPPER, 2.6f, -2.0F, TREASURE_PROPS_SUPPLIER.get()));
	
	// longswords (steel, skull, shadow, +)
	public static final RegistryObject<Item> STEEL_SWORD = Registration.ITEMS.register("steel_sword", 
			() -> new SwordItem(STEEL, 3, -2.4F, TREASURE_ITEM_PROPERTIES));

	public static final RegistryObject<Item> SKULL_SWORD = Registration.ITEMS.register("skull_sword", 
			() -> new SwordItem(SKULL, 3, -2.4F, TREASURE_ITEM_PROPERTIES));

	// large sword
	public static final RegistryObject<Item> SWORD_POWER = Registration.ITEMS.register("sword_of_power", 
			() -> new Sword(MYTHICAL, 3, -2.4F, TREASURE_ITEM_PROPERTIES) {
				@Override
				public  void appendHoverExtras(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
					tooltip.add(Component.literal(LangUtil.NEWLINE));
					tooltip.add(Component.literal(LangUtil.INDENT4)
							.append(Component.translatable(LangUtil.tooltip("weapons.sword_of_power.lore"))
									.append(Component.literal(LangUtil.INDENT4)).withStyle(ChatFormatting.LIGHT_PURPLE).withStyle(ChatFormatting.ITALIC)));
					tooltip.add(Component.literal(LangUtil.NEWLINE));
				}
				@Override
				public boolean isUnique() {
					return true;
				}
				@Override
				public boolean isValidRepairItem(ItemStack itemStack, ItemStack repairStack) {
					return false;
				}
			});
	
	public static final RegistryObject<Item> BLACK_SWORD = Registration.ITEMS.register("black_sword", 
			() -> new Sword(MYTHICAL, 3, -2.4F, TREASURE_ITEM_PROPERTIES) {
				@Override
				public  void appendHoverExtras(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
					tooltip.add(Component.literal(LangUtil.NEWLINE));
					tooltip.add(Component.literal(LangUtil.INDENT4)
							.append(Component.translatable(LangUtil.tooltip("weapons.black_sword.lore"))
									.append(Component.literal(LangUtil.INDENT4)).withStyle(ChatFormatting.LIGHT_PURPLE).withStyle(ChatFormatting.ITALIC)));
					tooltip.add(Component.literal(LangUtil.NEWLINE));
				}
				@Override
				public boolean isUnique() {
					return true;
				}
				@Override
				public boolean isValidRepairItem(ItemStack itemStack, ItemStack repairStack) {
					return false;
				}
			});

	public static final RegistryObject<Item> OATHBRINGER = Registration.ITEMS.register("oathbringer", 
			() -> new Sword(MYTHICAL, 3, -2.0F, 50F, 7F, TREASURE_ITEM_PROPERTIES) {
				@Override
				public  void appendHoverExtras(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
					tooltip.add(Component.literal(LangUtil.NEWLINE));
					// lore may be multiple lines, so separate on ~ and add to tooltip
					Component lore = Component.translatable(LangUtil.tooltip("weapons.oathbringer.lore"));
					for (String s : lore.getString().split("~")) {	
						tooltip.add(Component.literal(LangUtil.INDENT4)
								.append(Component.translatable(s)).withStyle(ChatFormatting.LIGHT_PURPLE).withStyle(ChatFormatting.ITALIC));
					}
					tooltip.add(Component.literal(LangUtil.NEWLINE));
				}
				@Override
				public boolean isUnique() {
					return true;
				}
				@Override
				public boolean isValidRepairItem(ItemStack itemStack, ItemStack repairStack) {
					return false;
				}
			});

	public static final RegistryObject<Item> SWORD_OMENS = Registration.ITEMS.register("sword_of_omens", 
			() -> new Sword(EPIC, 3, -2.4F, 35F, 5F, TREASURE_ITEM_PROPERTIES) {
				@Override
				public  void appendHoverExtras(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
					tooltip.add(Component.literal(LangUtil.NEWLINE));
					tooltip.add(Component.literal(LangUtil.INDENT4)
							.append(Component.translatable(LangUtil.tooltip("weapons.sword_of_omens.lore"))
									.append(Component.literal(LangUtil.INDENT4)).withStyle(ChatFormatting.LIGHT_PURPLE).withStyle(ChatFormatting.ITALIC)));
					tooltip.add(Component.literal(LangUtil.NEWLINE));
				}
				@Override
				public boolean isUnique() {
					return true;
				}
				@Override
				public boolean isValidRepairItem(ItemStack itemStack, ItemStack repairStack) {
					return false;
				}
			});

	public static final RegistryObject<Item> CALLANDOR = Registration.ITEMS.register("callandor", 
			() -> new Sword(MYTHICAL, 3, -2.0F, 75F, 9F, TREASURE_ITEM_PROPERTIES) {
				@Override
				public  void appendHoverExtras(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
					tooltip.add(Component.literal(LangUtil.NEWLINE));
					// lore may be multiple lines, so separate on ~ and add to tooltip
					Component lore = Component.translatable(LangUtil.tooltip("weapons.callandor.lore"));
					for (String s : lore.getString().split("~")) {	
						tooltip.add(Component.literal(LangUtil.INDENT4)
								.append(Component.translatable(s)).withStyle(ChatFormatting.LIGHT_PURPLE).withStyle(ChatFormatting.ITALIC));
					}
					tooltip.add(Component.literal(LangUtil.NEWLINE));
				}
				@Override
				public boolean isUnique() {
					return true;
				}
				@Override
				public boolean isValidRepairItem(ItemStack itemStack, ItemStack repairStack) {
					return false;
				}
			});
	
	// broad/bastard swords (steel, black)
	public static final RegistryObject<Item> IRON_BROADSWORD = Registration.ITEMS.register("iron_broadsword", 
			() -> new Sword(Tiers.IRON, 3.5f, -2.8F, TREASURE_ITEM_PROPERTIES));
	
	public static final RegistryObject<Item> STEEL_BROADSWORD = Registration.ITEMS.register("steel_broadsword", 
			() -> new Sword(STEEL, 3.5f, -2.8F, TREASURE_ITEM_PROPERTIES));
	
	// scythes
	public static final RegistryObject<Item> ORCUS = Registration.ITEMS.register("orcus", 
			() -> new Sword(LEGENDARY, 3, -2.4F, 40F, 5F, TREASURE_ITEM_PROPERTIES) {
				@Override
				public  void appendHoverExtras(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
					tooltip.add(Component.literal(LangUtil.NEWLINE));
					// lore may be multiple lines, so separate on ~ and add to tooltip
					Component lore = Component.translatable(LangUtil.tooltip("weapons.orcus.lore"));
					for (String s : lore.getString().split("~")) {	
						tooltip.add(Component.literal(LangUtil.INDENT4)
								.append(Component.translatable(s)).withStyle(ChatFormatting.LIGHT_PURPLE).withStyle(ChatFormatting.ITALIC));
					}
					tooltip.add(Component.literal(LangUtil.NEWLINE));
				}
				@Override
				public boolean isUnique() {
					return true;
				}
				@Override
				public boolean isValidRepairItem(ItemStack itemStack, ItemStack repairStack) {
					return false;
				}
			});

	// katanas (steel, shadow + )
	public static final RegistryObject<Item> SNAKE_EYES_KATANA = Registration.ITEMS.register("snake_eyes_katana", 
			() -> new Sword(RARE, 3, -1.5F, 25f, 5f, TREASURE_ITEM_PROPERTIES) {
				@Override
				public  void appendHoverExtras(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
					tooltip.add(Component.literal(LangUtil.NEWLINE));
					// lore may be multiple lines, so separate on ~ and add to tooltip
					Component lore = Component.translatable(LangUtil.tooltip("weapons.snake_eyes_katana.lore"));
					for (String s : lore.getString().split("~")) {	
						tooltip.add(Component.literal(LangUtil.INDENT4)
								.append(Component.translatable(s)).withStyle(ChatFormatting.LIGHT_PURPLE).withStyle(ChatFormatting.ITALIC));
					}
					tooltip.add(Component.literal(LangUtil.NEWLINE));
				}
				@Override
				public boolean isUnique() {
					return true;
				}
				@Override
				public boolean isValidRepairItem(ItemStack itemStack, ItemStack repairStack) {
					return false;
				}
			});

	public static final RegistryObject<Item> STORM_SHADOWS_KATANA = Registration.ITEMS.register("storm_shadows_katana", 
			() -> new Sword(RARE, 3, -1.5f, 25f, 5f, TREASURE_ITEM_PROPERTIES) {
				@Override
				public  void appendHoverExtras(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
					tooltip.add(Component.literal(LangUtil.NEWLINE));
					// lore may be multiple lines, so separate on ~ and add to tooltip
					Component lore = Component.translatable(LangUtil.tooltip("weapons.storm_shadows_katana.lore"));
					for (String s : lore.getString().split("~")) {	
						tooltip.add(Component.literal(LangUtil.INDENT4)
								.append(Component.translatable(s)).withStyle(ChatFormatting.LIGHT_PURPLE).withStyle(ChatFormatting.ITALIC));
					}
					tooltip.add(Component.literal(LangUtil.NEWLINE));
				}
				@Override
				public boolean isUnique() {
					return true;
				}
				@Override
				public boolean isValidRepairItem(ItemStack itemStack, ItemStack repairStack) {
					return false;
				}
			});

	// machetes (steel, shadow)
	public static final RegistryObject<Item> STEEL_MACHETE = Registration.ITEMS.register("steel_machete", 
			() -> new Sword(STEEL, 2.7F, -2.6F, TREASURE_ITEM_PROPERTIES));
	public static final RegistryObject<Item> SHADOW_MACHETE = Registration.ITEMS.register("shadow_machete", 
			() -> new Sword(SHADOW, 2.7F, -2.6F, TREASURE_ITEM_PROPERTIES));

	// flachions (steel, shadow)
	public static final RegistryObject<Item> IRON_FALCHION = Registration.ITEMS.register("iron_falchion", 
			() -> new Sword(Tiers.IRON, 2.8F, -2.4F, TREASURE_ITEM_PROPERTIES));
	public static final RegistryObject<Item> STEEL_FALCHION = Registration.ITEMS.register("steel_falchion", 
			() -> new Sword(STEEL, 2.8F, -2.4F, TREASURE_ITEM_PROPERTIES));
	public static final RegistryObject<Item> SHADOW_FALCHION = Registration.ITEMS.register("shadow_falchion", 
			() -> new Sword(SHADOW, 2.8F, -2.4F, TREASURE_ITEM_PROPERTIES));
	
	// hammers / maces / mauls
	public static final RegistryObject<Item> IRON_MACE = Registration.ITEMS.register("iron_mace", 
			() -> new Sword(Tiers.IRON, TreasureWeapons.HAMMER_BASE_DAMAGE, TreasureWeapons.HAMMER_BASE_SPEED, TREASURE_ITEM_PROPERTIES));
	public static final RegistryObject<Item> STEEL_MACE = Registration.ITEMS.register("steel_mace", 
			() -> new Sword(STEEL, TreasureWeapons.HAMMER_BASE_DAMAGE, TreasureWeapons.HAMMER_BASE_SPEED, TREASURE_ITEM_PROPERTIES));
	
	public static final RegistryObject<Item> MJOLNIR = Registration.ITEMS.register("mjolnir", 
			() -> new Sword(MYTHICAL, 
					TreasureWeapons.HAMMER_BASE_DAMAGE, TreasureWeapons.HAMMER_BASE_SPEED + 0.7f, 
					75F, 9F, TREASURE_ITEM_PROPERTIES) {
				@Override
				public  void appendHoverExtras(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
					tooltip.add(Component.literal(LangUtil.NEWLINE));
					// lore may be multiple lines, so separate on ~ and add to tooltip
					Component lore = Component.translatable(LangUtil.tooltip("weapons.mjolnir.lore"));
					for (String s : lore.getString().split("~")) {	
						tooltip.add(Component.literal(LangUtil.INDENT4)
								.append(Component.translatable(s)).withStyle(ChatFormatting.LIGHT_PURPLE).withStyle(ChatFormatting.ITALIC));
					}
					tooltip.add(Component.literal(LangUtil.NEWLINE));
				}
				@Override
				public boolean isUnique() {
					return true;
				}
				@Override
				public boolean isValidRepairItem(ItemStack itemStack, ItemStack repairStack) {
					return false;
				}
			});

	// axes
	//	WOODEN_AXE = new AxeItem(Tiers.WOOD, 6.0F, -3.2F
	//	STONE_AXE = new AxeItem(Tiers.STONE, 7.0F, -3.2F
	//	IRON_AXE = new AxeItem(Tiers.IRON, 6.0F, -3.1F  // USE AS DEFAULT STATS FOR AXES
	//	DIAMOND_AXE = new AxeItem(Tiers.DIAMOND, 5.0F, -3.0F
	//	NETHERITE_AXE = new AxeItem(Tiers.NETHERITE, 5.0F, -3.0F
	public static final RegistryObject<Item> COPPER_BROAD_AXE = Registration.ITEMS.register("copper_broad_axe", 
			() -> new Axe(COPPER, TreasureWeapons.AXE_BASE_DAMAGE - 1.0F, TreasureWeapons.AXE_BASE_SPEED + 0.2f, TREASURE_ITEM_PROPERTIES));
	public static final RegistryObject<Item> IRON_BROAD_AXE = Registration.ITEMS.register("iron_broad_axe", 
			() -> new Axe(Tiers.IRON, TreasureWeapons.AXE_BASE_DAMAGE - 1.0F, TreasureWeapons.AXE_BASE_SPEED + 0.2f, TREASURE_ITEM_PROPERTIES));
	public static final RegistryObject<Item> STEEL_BROAD_AXE = Registration.ITEMS.register("steel_broad_axe", 
			() -> new Axe(STEEL, TreasureWeapons.AXE_BASE_DAMAGE - 1.0F, TreasureWeapons.AXE_BASE_SPEED + 0.2f, TREASURE_ITEM_PROPERTIES));
	
	public static final RegistryObject<Item> IRON_DWARVEN_AXE = Registration.ITEMS.register("iron_dwarven_axe", 
			() -> new Axe(Tiers.IRON, TreasureWeapons.AXE_BASE_DAMAGE + 0.5F, TreasureWeapons.AXE_BASE_SPEED + 0.1f, TREASURE_ITEM_PROPERTIES));
	
	
	public static final RegistryObject<Item> AXE_DURIN = Registration.ITEMS.register("axe_of_durin", 
			() -> new Axe(LEGENDARY, TreasureWeapons.AXE_BASE_DAMAGE, TreasureWeapons.AXE_BASE_SPEED + 0.5f, 
					65F, 7F, TREASURE_ITEM_PROPERTIES) {
				@Override
				public  void appendHoverExtras(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
					tooltip.add(Component.literal(LangUtil.NEWLINE));
					// lore may be multiple lines, so separate on ~ and add to tooltip
					Component lore = Component.translatable(LangUtil.tooltip("weapons.axe_of_durin.lore"));
					for (String s : lore.getString().split("~")) {	
						tooltip.add(Component.literal(LangUtil.INDENT4)
								.append(Component.translatable(s)).withStyle(ChatFormatting.LIGHT_PURPLE).withStyle(ChatFormatting.ITALIC));
					}
					tooltip.add(Component.literal(LangUtil.NEWLINE));
				}
				@Override
				public boolean isUnique() {
					return true;
				}
				@Override
				public boolean isValidRepairItem(ItemStack itemStack, ItemStack repairStack) {
					return false;
				}
			});
	
	public static final RegistryObject<Item> HEADSMANS_AXE = Registration.ITEMS.register("headsmans_axe", 
			() -> new Axe(EPIC, TreasureWeapons.AXE_BASE_DAMAGE, TreasureWeapons.AXE_BASE_SPEED + 0.3f, 
					55F, 6F, TREASURE_ITEM_PROPERTIES) {
				@Override
				public  void appendHoverExtras(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
					tooltip.add(Component.literal(LangUtil.NEWLINE));
					// lore may be multiple lines, so separate on ~ and add to tooltip
					Component lore = Component.translatable(LangUtil.tooltip("weapons.headsmans_axe.lore"));
					for (String s : lore.getString().split("~")) {	
						tooltip.add(Component.literal(LangUtil.INDENT4)
								.append(Component.translatable(s)).withStyle(ChatFormatting.LIGHT_PURPLE).withStyle(ChatFormatting.ITALIC));
					}
					tooltip.add(Component.literal(LangUtil.NEWLINE));
				}
				@Override
				public boolean isUnique() {
					return true;
				}
				@Override
				public boolean isValidRepairItem(ItemStack itemStack, ItemStack repairStack) {
					return false;
				}
			});

	static {
		// register all the chests
		TreasureBlocks.CHESTS.forEach(g -> {
			RegistryObject<Item> item = fromChestBlock(g, TREASURE_PROPS_SUPPLIER);
			BLOCK_ITEM_MAP.put(g, item);
		});

		// register all the gravestone items w/o keeping a static reference
		TreasureBlocks.GRAVESTONES.forEach(g -> {
			RegistryObject<Item> item = fromBlock(g, TREASURE_PROPS_SUPPLIER);
			BLOCK_ITEM_MAP.put(g, item);
		});

		// register all the gravestone spawner items w/o keeping a static reference
		TreasureBlocks.GRAVESTONE_SPAWNERS.forEach(g -> {
			RegistryObject<Item> item = fromBlock(g, TREASURE_PROPS_SUPPLIER);
			BLOCK_ITEM_MAP.put(g, item);
		});
	}

	/**
	 * 
	 */
	public static void register(IEventBus bus) {
		// cycle through all block and create items
		Registration.registerItems(bus);
	}

	// convenience method: take a RegistryObject<Block> and make a corresponding RegistryObject<Item> from it
	public static <B extends Block> RegistryObject<Item> fromBlock(RegistryObject<B> block, Supplier<Item.Properties> itemProperties) {
		return Registration.ITEMS.register(block.getId().getPath(), () -> new BlockItem(block.get(), itemProperties.get()));
	}

	public static <B extends Block> RegistryObject<Item> fromChestBlock(RegistryObject<B> block, Supplier<Item.Properties> itemProperties) {
		return Registration.ITEMS.register(block.getId().getPath(), () -> new TreasureChestBlockItem(block.get(), itemProperties.get()));
	}
}
