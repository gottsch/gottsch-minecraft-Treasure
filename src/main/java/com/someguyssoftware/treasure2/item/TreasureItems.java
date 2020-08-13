/**
 * 
 */
package com.someguyssoftware.treasure2.item;

import java.util.function.Supplier;

import com.someguyssoftware.gottschcore.item.ModItem;
import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

/**
 * @author Mark Gottschling on Aug 12, 2020
 * This class has the register event handler for all custom items.
 * This class uses @Mod.EventBusSubscriber so the event handler has to be static
 * This class uses @ObjectHolder to get a reference to the items
 *
 */
@Mod.EventBusSubscriber(modid = Treasure.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(Treasure.MODID)
public class TreasureItems {
	
    public static final Item LOGO = new ModItem(Treasure.MODID, "treasure_tab", new Item.Properties());
    public static final Item TREASURE_TOOL = new TreasureToolItem(Treasure.MODID, "treasure_tool", new Item.Properties());

    /**
     * The actual event handler that registers the custom items.
     *
     * @param event The event this event handler handles
     */
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        // in here you pass in all item instances you want to register.
        // make sure you always set the registry name.
        event.getRegistry().registerAll(
            LOGO,
            TREASURE_TOOL
        );
    }

    /**
     * 
     * @author Mark Gottschling on Aug 12, 2020
     *
     */
    public static class ModItemGroup extends ItemGroup {
    	private final Supplier<ItemStack> iconSupplier;

    	public ModItemGroup(final String name, final Supplier<ItemStack> iconSupplier) {
    		super(name);
    		this.iconSupplier = iconSupplier;
    	}

    	@Override
    	public ItemStack createIcon() {
    		return iconSupplier.get();
    	}
    }
}
