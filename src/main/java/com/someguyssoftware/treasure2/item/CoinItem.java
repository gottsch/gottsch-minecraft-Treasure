/**
 * 
 */
package com.someguyssoftware.treasure2.item;

import static com.someguyssoftware.treasure2.Treasure.LOGGER;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import com.someguyssoftware.gottschcore.block.BlockContext;
import com.someguyssoftware.gottschcore.item.ModItem;
import com.someguyssoftware.gottschcore.loot.LootPoolShell;
import com.someguyssoftware.gottschcore.loot.LootTableShell;
import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.gottschcore.spatial.Coords;
import com.someguyssoftware.gottschcore.spatial.ICoords;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.IWishingWellBlock;
import com.someguyssoftware.treasure2.capability.CharmableCapabilityProvider;
import com.someguyssoftware.treasure2.capability.ICharmableCapability;
import com.someguyssoftware.treasure2.capability.TreasureCapabilities;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.enums.Coins;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.item.TreasureItems;
import com.someguyssoftware.treasure2.loot.TreasureLootTableRegistry;

import net.minecraft.block.Blocks;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

/**
 * 
 * @author Mark Gottschling on Sep 13, 2014
 *
 */
@Deprecated
public class CoinItem extends ModItem implements IWishable {
	private static final int MAX_CUSTOM_STACK_SIZE = 64;
	public static final int MAX_STACK_SIZE = 8;
	
	private Coins coin;
	
	/**
	 * 
	 */
	public CoinItem (String modID, String name, Item.Properties properties)	 {
        super(modID, name, properties.tab(TreasureItemGroups.MOD_ITEM_GROUP)
        		.stacksTo(Math.min(MAX_CUSTOM_STACK_SIZE, TreasureConfig.COINS.coinMaxStackSize.get())));
		// set the coin to gold by default
		this.coin = Coins.GOLD;
	}
	
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
		CharmableCapabilityProvider provider =  new CharmableCapabilityProvider();
		return provider;
	}
	
	/**
	 * 
	 */
	@Override
	public void appendHoverText(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
		// standard coin info
		tooltip.add(new TranslationTextComponent("tooltip.label.coin").withStyle(TextFormatting.GOLD, TextFormatting.ITALIC));
		// charmable info
		ICharmableCapability cap = getCap(stack);
		if (cap.isCharmed()) {
			cap.appendHoverText(stack, worldIn, tooltip, flagIn);
		}
	}
	
	/**
	 * Convenience method.
	 * @param stack
	 * @return
	 */
	public ICharmableCapability getCap(ItemStack stack) {
		return stack.getCapability(TreasureCapabilities.CHARMABLE, null).orElseThrow(IllegalStateException::new);
	}
	
	/**
	 * 
	 */
	@Override
	public boolean isFoil(ItemStack stack) {
		ICharmableCapability cap = getCap(stack);
		if (cap.isCharmed()) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * 
	 */
	@Override
	public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entityItem) {
		// get the item stack or number of items.
		ItemStack entityItemStack = entityItem.getItem();
		
		World world = entityItem.level;
		if (WorldInfo.isClientSide(world)) {
			return super.onEntityItemUpdate(stack, entityItem);
		}
		
		// get the position
		ICoords coords = new Coords(entityItem.blockPosition());
		BlockContext blockContext = new BlockContext(world, coords);
		int numWishingWellBlocks = 0;
		// check if in water
		if (blockContext.equalsBlock(Blocks.WATER)) {
			// check if the water block is adjacent to 2 wishing well blocks
			ICoords checkCoords = coords.add(-1, 0, -1);
			for (int z = 0; z < 3; z++) {
				for (int x = 0; x < 3; x++) {
					BlockContext checkCube = new BlockContext(world, checkCoords);
					if (checkCube.toBlock() instanceof IWishingWellBlock) {
						numWishingWellBlocks++;
					}					
					if (numWishingWellBlocks >= 2) {
						break;
					}
				}
			}
			
			if (numWishingWellBlocks >=2) {
				Random random = new Random();
				for (int itemIndex = 0; itemIndex < entityItemStack.getCount(); itemIndex++) {
					// generate an item for each item in the stack
					Optional<ItemStack> lootStack = generateLoot(world, random, entityItem.getItem(), coords);
					if (lootStack.isPresent()) {
						// spawn the item 
						InventoryHelper.dropItemStack(world, (double)coords.getX(), (double)coords.getY()+1, (double)coords.getZ(), stack);
					}
				}
				// remove the item entity
				entityItem.remove();
				return true;
			}
		}
		
		return super.onEntityItemUpdate(stack, entityItem);
	}
	
	/**
	 * 
	 * @param world
	 * @param random
	 * @param itemStack
	 * @param coords
	 */
	@Override
	public Optional<ItemStack> generateLoot(World world, Random random, ItemStack itemStack, ICoords coords) {
		List<LootTableShell> lootTables = new ArrayList<>();

		// determine coin type
		if (getCoin() == Coins.COPPER) {
			lootTables.addAll(TreasureLootTableRegistry.getLootTableMaster().getLootTableByRarity(Rarity.COMMON));
		}
		else if (getCoin() == Coins.SILVER) {
			lootTables.addAll(TreasureLootTableRegistry.getLootTableMaster().getLootTableByRarity(Rarity.UNCOMMON));
			lootTables.addAll(TreasureLootTableRegistry.getLootTableMaster().getLootTableByRarity(Rarity.SCARCE));
		}
		else if (getCoin() == Coins.GOLD) {					
			lootTables.addAll(TreasureLootTableRegistry.getLootTableMaster().getLootTableByRarity(Rarity.SCARCE));
			lootTables.addAll(TreasureLootTableRegistry.getLootTableMaster().getLootTableByRarity(Rarity.RARE));
		}
		
		// TODO most of this seems repeated from IChestGenerator.  Make a common class/methods
		
		ItemStack outputStack = null;
		// handle if loot tables is null or size = 0. return an item (apple) to ensure continuing functionality
		if (lootTables == null || lootTables.size() == 0) {
			// TODO change to a randomized treasure key
//			stack = new ItemStack(Items.APPLE);
			List<KeyItem> keys = new ArrayList<>(TreasureItems.keys.get((getCoin() == Coins.SILVER) ? Rarity.UNCOMMON : Rarity.SCARCE));
			outputStack = new ItemStack(keys.get(random.nextInt(keys.size())));
		}
		else {
			// attempt to get the player who dropped the coin
			ItemStack coinItem = itemStack;
			CompoundNBT nbt = coinItem.getTag();
			LOGGER.debug("item as a tag");
			PlayerEntity player = null;
			if (nbt != null && nbt.contains(DROPPED_BY_KEY)) {
				// TODO change to check by UUID
				for (PlayerEntity p : world.players()) {
					if (p.getName().getString().equalsIgnoreCase(nbt.getString(DROPPED_BY_KEY))) {
						player = p;
					}
				}
				if (player != null && LOGGER.isDebugEnabled()) {
					LOGGER.debug("coin dropped by player -> {}", player.getName());
				}
				else {
					LOGGER.debug("can't find player!");
				}
			}
			LOGGER.debug("player -> {}", player.getName().getString());

			// select a table shell
			LootTableShell tableShell = lootTables.get(RandomHelper.randomInt(random, 0, lootTables.size()-1));
			if (tableShell.getResourceLocation() == null) {
				return Optional.empty();
			}
			
			// get the vanilla table from shell
			LootTable table = world.getServer().getLootTables().get(tableShell.getResourceLocation());
			// get a list of loot pools
			List<LootPoolShell> lootPoolShells = tableShell.getPools();
			
			// generate a context
			LootContext lootContext = new LootContext.Builder((ServerWorld) world)
					.withLuck((player != null) ? player.getLuck() : 0)
					.withParameter(LootParameters.THIS_ENTITY, player)
					.withParameter(LootParameters.ORIGIN, coords.toVec3d()).create(LootParameterSets.CHEST);

			List<ItemStack> itemStacks = new ArrayList<>();
			for (LootPoolShell pool : lootPoolShells) {
				LOGGER.debug("coin: processing pool -> {}", pool.getName());
				// go get the vanilla managed pool
				LootPool lootPool = table.getPool(pool.getName());
				
				// geneate loot from pools
				// TODO https://github.com/gottsch/gottsch-minecraft-Treasure/issues/242
				// lootPool is null.
				if (lootPool != null) {
					lootPool.addRandomItems(itemStacks::add, lootContext);
				}
				else {
					Treasure.LOGGER.warn("loot pool -> {} is null", pool.getName());
				}
			}

			// get effective rarity
			Rarity effectiveRarity = TreasureLootTableRegistry.getLootTableMaster().getEffectiveRarity(tableShell, (getCoin() == Coins.SILVER) ? Rarity.UNCOMMON : Rarity.SCARCE);	
			LOGGER.debug("coin: using effective rarity -> {}", effectiveRarity);
			
			// get all injected loot tables
			injectLoot(world, random, itemStacks, tableShell.getCategory(), effectiveRarity, lootContext);
			
			// select one item randomly
			outputStack = itemStacks.get(RandomHelper.randomInt(0, itemStacks.size()-1));
		}				
		return Optional.of(outputStack);
	}
	
	/**
	 * @return the coin
	 */
	public Coins getCoin() {
		return coin;
	}
	/**
	 * @param coin the coin to set
	 */
	public CoinItem setCoin(Coins coin) {
		this.coin = coin;
		return this;
	}
}