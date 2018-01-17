/**
 * 
 */
package com.someguyssoftware.treasure2;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.someguyssoftware.gottschcore.annotation.Credits;
import com.someguyssoftware.gottschcore.command.ShowVersionCommand;
import com.someguyssoftware.gottschcore.config.IConfig;
import com.someguyssoftware.gottschcore.mod.AbstractMod;
import com.someguyssoftware.gottschcore.mod.IMod;
import com.someguyssoftware.gottschcore.version.BuildVersion;
import com.someguyssoftware.treasure2.client.gui.GuiHandler;
import com.someguyssoftware.treasure2.client.model.StandardChestModel;
import com.someguyssoftware.treasure2.client.render.tileentity.TestChestRenderer;
import com.someguyssoftware.treasure2.client.render.tileentity.TreasureChestTileEntityRenderer;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.item.TreasureItems;
import com.someguyssoftware.treasure2.tileentity.TestChestTileEntity;
import com.someguyssoftware.treasure2.tileentity.TreasureChestTileEntity;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Mark Gottschling onDec 22, 2017
 *
 */
@Mod(
		modid=Treasure.MODID,
		name=Treasure.NAME,
		version=Treasure.VERSION,
		dependencies="required-after:gottschcore@[1.3.0,)",
		acceptedMinecraftVersions = "[1.12.2]",
		updateJSON = Treasure.UPDATE_JSON_URL
		)
@Credits(values={"Treasure was first developed by Mark Gottschling on Aug 27, 2014.","Credits to Mason for ideas and debugging."})
public class Treasure extends AbstractMod {

	// constants
	public static final String MODID = "treasure2";
	protected static final String NAME = "Treasure2";
	protected static final String VERSION = "5.0.0";
	public static final String UPDATE_JSON_URL = "https://raw.githubusercontent.com/gottsch/gottsch-minecraft-Treasure/master/Treasure2-1.12.2/update.json";

	private static final String VERSION_URL = "https://www.dropbox.com/s/6j3h91c69nd7g4f/treasure-versions2.json?dl=1";
	private static final BuildVersion MINECRAFT_VERSION = new BuildVersion(1, 12, 2);
	
	private static final String TREASURE_CONFIG_DIR = "treasure2";
	private static TreasureConfig config;
	
	// latest version
	private static BuildVersion latestVersion;
	
	// logger
	public static Logger logger = LogManager.getLogger(Treasure.NAME);
	
	@Instance(value=Treasure.MODID)
	public static Treasure instance;
	
	/*
	 *  Treasure Creative Tab
	 *  Must be instantized <b>before</b> any registry events so that it is available to assign to blocks and items.
	 */
	public static CreativeTabs TREASURE_TAB = new CreativeTabs(CreativeTabs.getNextID(), Treasure.MODID + ":" + TreasureConfig.TREASURE_TAB_ID) {
		@SideOnly(Side.CLIENT)
		public ItemStack getTabIconItem() {
			return new ItemStack(TreasureItems.TREASURE_TAB, 1);
		}
	};
    
	/**
	 * 
	 */
	public Treasure() {}
	
	/**
	 * 
	 * @param event
	 */
	@Override
	@EventHandler
	public void preInt(FMLPreInitializationEvent event) {
		super.preInt(event);
		
		// register additional events
		
		// create and load the config file		
		config = new TreasureConfig(this, event.getModConfigurationDirectory(), TREASURE_CONFIG_DIR, "general.cfg");
		
		// configure logging
		addRollingFileAppenderToLogger(Treasure.NAME, Treasure.NAME + "Appender", config);
		
		// register the GUI handler
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
	}
	
	/**
	 * 
	 * @param event
	 */
	@EventHandler
    public void serverStarted(FMLServerStartingEvent event) {
		if (!getConfig().isModEnabled()) return;
		
    	// add a show version command
    	event.registerServerCommand(new ShowVersionCommand(this));
    	
		// register additional commands
    }
	
	/**
	 * 
	 */
	@Override
	@EventHandler
	public void init(FMLInitializationEvent event) {
		// don't process is mod is disabled
		if (!getConfig().isModEnabled()) return;
		
		super.init(event);
		
		// register tile entity special renderers
		// TODO the renderer should take in the Texture, ItemRenderer
		ClientRegistry.bindTileEntitySpecialRenderer(TestChestTileEntity.class, new TestChestRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(
				TreasureChestTileEntity.class,
				new TreasureChestTileEntityRenderer("standard-chest", new StandardChestModel()));
	}


	
	/**
	 * 
	 */
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		if (!getConfig().isModEnabled()) return;		

		// perform any post init
		super.postInit(event);
	}
	
	/* (non-Javadoc)
	 * @see com.someguyssoftware.gottschcore.mod.IMod#getConfig()
	 */
	@Override
	public IConfig getConfig() {
		// TODO Auto-generated method stub
		return Treasure.config;
	}

	/* (non-Javadoc)
	 * @see com.someguyssoftware.gottschcore.mod.IMod#getMinecraftVersion()
	 */
	@Override
	public BuildVersion getMinecraftVersion() {
		return Treasure.MINECRAFT_VERSION;
	}

	/* (non-Javadoc)
	 * @see com.someguyssoftware.gottschcore.mod.IMod#getVerisionURL()
	 */
	@Override
	public String getVerisionURL() {
		return Treasure.VERSION_URL;
	}

	/* (non-Javadoc)
	 * @see com.someguyssoftware.gottschcore.mod.IMod#getName()
	 */
	@Override
	public String getName() {
		return Treasure.NAME;
	}

	/* (non-Javadoc)
	 * @see com.someguyssoftware.gottschcore.mod.IMod#getId()
	 */
	@Override
	public String getId() {
		return Treasure.MODID;
	}

	/* (non-Javadoc)
	 * @see com.someguyssoftware.gottschcore.mod.AbstractMod#getInstance()
	 */
	@Override
	public IMod getInstance() {
		return Treasure.instance;
	}

	/* (non-Javadoc)
	 * @see com.someguyssoftware.gottschcore.mod.AbstractMod#getVersion()
	 */
	@Override
	public String getVersion() {
		return Treasure.VERSION;
	}

	@Override
	public BuildVersion getModLatestVersion() {
		return latestVersion;
	}

	@Override
	public void setModLatestVersion(BuildVersion version) {
		Treasure.latestVersion = version;		
	}
	
	@Override
	public String getUpdateURL() {
		return Treasure.UPDATE_JSON_URL;
	}

}
