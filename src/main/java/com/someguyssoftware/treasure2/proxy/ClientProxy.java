/**
 * 
 */
package com.someguyssoftware.treasure2.proxy;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.client.model.BandedChestModel;
import com.someguyssoftware.treasure2.client.model.CardboardBoxModel;
import com.someguyssoftware.treasure2.client.model.CauldronChestModel;
import com.someguyssoftware.treasure2.client.model.ClamChestModel;
import com.someguyssoftware.treasure2.client.model.CompressorChestModel;
import com.someguyssoftware.treasure2.client.model.CrateChestModel;
import com.someguyssoftware.treasure2.client.model.DreadPirateChestModel;
import com.someguyssoftware.treasure2.client.model.MilkCrateModel;
import com.someguyssoftware.treasure2.client.model.MimicModel;
import com.someguyssoftware.treasure2.client.model.OysterChestModel;
import com.someguyssoftware.treasure2.client.model.SafeModel;
import com.someguyssoftware.treasure2.client.model.SkullChestModel;
import com.someguyssoftware.treasure2.client.model.SpiderChestModel;
import com.someguyssoftware.treasure2.client.model.StandardChestModel;
import com.someguyssoftware.treasure2.client.model.StrongboxModel;
import com.someguyssoftware.treasure2.client.model.VikingChestModel;
import com.someguyssoftware.treasure2.client.model.WitherChestModel;
import com.someguyssoftware.treasure2.client.render.entity.BoundSoulRenderer;
import com.someguyssoftware.treasure2.client.render.entity.MimicEntityRenderer;
import com.someguyssoftware.treasure2.client.render.tileentity.CardboardBoxTileEntityRenderer;
import com.someguyssoftware.treasure2.client.render.tileentity.CauldronChestTileEntityRenderer;
import com.someguyssoftware.treasure2.client.render.tileentity.CompressorChestTileEntityRenderer;
import com.someguyssoftware.treasure2.client.render.tileentity.CrateChestTileEntityRenderer;
import com.someguyssoftware.treasure2.client.render.tileentity.MilkCrateTileEntityRenderer;
import com.someguyssoftware.treasure2.client.render.tileentity.MolluscChestTileEntityRenderer;
import com.someguyssoftware.treasure2.client.render.tileentity.SafeTileEntityRenderer;
import com.someguyssoftware.treasure2.client.render.tileentity.SkullChestTileEntityRenderer;
import com.someguyssoftware.treasure2.client.render.tileentity.StrongboxTileEntityRenderer;
import com.someguyssoftware.treasure2.client.render.tileentity.TreasureChestTileEntityRenderer;
import com.someguyssoftware.treasure2.client.render.tileentity.WitherChestTileEntityRenderer;
import com.someguyssoftware.treasure2.entity.monster.BoundSoulEntity;
import com.someguyssoftware.treasure2.entity.monster.PirateMimicEntity;
import com.someguyssoftware.treasure2.entity.monster.WoodMimicEntity;
import com.someguyssoftware.treasure2.particle.MistTextureStitcher;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;
import com.someguyssoftware.treasure2.tileentity.CardboardBoxTileEntity;
import com.someguyssoftware.treasure2.tileentity.CauldronChestTileEntity;
import com.someguyssoftware.treasure2.tileentity.ClamChestTileEntity;
import com.someguyssoftware.treasure2.tileentity.CompressorChestTileEntity;
import com.someguyssoftware.treasure2.tileentity.CrateChestTileEntity;
import com.someguyssoftware.treasure2.tileentity.DreadPirateChestTileEntity;
import com.someguyssoftware.treasure2.tileentity.GoldSkullChestTileEntity;
import com.someguyssoftware.treasure2.tileentity.GoldStrongboxTileEntity;
import com.someguyssoftware.treasure2.tileentity.IronStrongboxTileEntity;
import com.someguyssoftware.treasure2.tileentity.IronboundChestTileEntity;
import com.someguyssoftware.treasure2.tileentity.MilkCrateTileEntity;
import com.someguyssoftware.treasure2.tileentity.MoldyCrateChestTileEntity;
import com.someguyssoftware.treasure2.tileentity.OysterChestTileEntity;
import com.someguyssoftware.treasure2.tileentity.PirateChestTileEntity;
import com.someguyssoftware.treasure2.tileentity.SafeTileEntity;
import com.someguyssoftware.treasure2.tileentity.SkullChestTileEntity;
import com.someguyssoftware.treasure2.tileentity.SpiderChestTileEntity;
import com.someguyssoftware.treasure2.tileentity.VikingChestTileEntity;
import com.someguyssoftware.treasure2.tileentity.WhaleBonePirateChestTileEntity;
import com.someguyssoftware.treasure2.tileentity.WitherChestTileEntity;
import com.someguyssoftware.treasure2.tileentity.WoodChestTileEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

/**
 * @author Mark Gottschling on Mar 10, 2018
 *
 */
@Mod.EventBusSubscriber(modid=Treasure.MODID, value = Side.CLIENT)
public class ClientProxy {
	
	@SuppressWarnings("deprecation")
	@SubscribeEvent
	public static void registerRenderers(@SuppressWarnings("rawtypes") final RegistryEvent.Register event) {
		// register the texture stitcher, which is used to insert the mist image into the blocks texture sheet
	    MinecraftForge.EVENT_BUS.register(new MistTextureStitcher());
	    
		/*
		 *  register tile entity special renderers
		 */
		// default
		ClientRegistry.bindTileEntitySpecialRenderer(
				AbstractTreasureChestTileEntity.class,
				new TreasureChestTileEntityRenderer("standard-chest", new StandardChestModel()));
		
		// wood chest
		ClientRegistry.bindTileEntitySpecialRenderer(
				WoodChestTileEntity.class,
				new TreasureChestTileEntityRenderer("wood-chest", new StandardChestModel()));
		
		// crate chest
		ClientRegistry.bindTileEntitySpecialRenderer(
				CrateChestTileEntity.class,
				new CrateChestTileEntityRenderer("crate-chest", new CrateChestModel()));

		// moldy crate chest
		ClientRegistry.bindTileEntitySpecialRenderer(
				MoldyCrateChestTileEntity.class,
				new CrateChestTileEntityRenderer("crate-chest-moldy", new CrateChestModel()));
		
		// ironbound chest
		ClientRegistry.bindTileEntitySpecialRenderer(
				IronboundChestTileEntity.class,
				new TreasureChestTileEntityRenderer("ironbound-chest", new BandedChestModel()));		
		
		// pirate chest
		ClientRegistry.bindTileEntitySpecialRenderer(
				PirateChestTileEntity.class,
				new TreasureChestTileEntityRenderer("pirate-chest", new StandardChestModel()));
	
		// iron strongbox
		ClientRegistry.bindTileEntitySpecialRenderer(
				IronStrongboxTileEntity.class,
				new StrongboxTileEntityRenderer("iron-strongbox", new StrongboxModel()));
		
		// gold strongbox
		ClientRegistry.bindTileEntitySpecialRenderer(
				GoldStrongboxTileEntity.class,
				new StrongboxTileEntityRenderer("gold-strongbox", new StrongboxModel()));
		
		// safe
		ClientRegistry.bindTileEntitySpecialRenderer(
				SafeTileEntity.class,
				new SafeTileEntityRenderer("safe", new SafeModel()));
		
		// dread pirate chest
		ClientRegistry.bindTileEntitySpecialRenderer(
				DreadPirateChestTileEntity.class,
				new TreasureChestTileEntityRenderer("dread-pirate-chest", new DreadPirateChestModel()));
		
		// whale bone pirate chest - uses same model as dread pirate
//		ClientRegistry.bindTileEntitySpecialRenderer(
//				WhaleBonePirateChestTileEntity.class,
//				new TreasureChestTileEntityRenderer("whale-bone-pirate-chest", new DreadPirateChestModel()));
		
		// compressor chest
		ClientRegistry.bindTileEntitySpecialRenderer(
				CompressorChestTileEntity.class,
				new CompressorChestTileEntityRenderer("compressor-chest", new CompressorChestModel()));
		
		// wither chest
		ClientRegistry.bindTileEntitySpecialRenderer(
				WitherChestTileEntity.class,
				new WitherChestTileEntityRenderer("wither-chest", new WitherChestModel()));

		// skull chest
		ClientRegistry.bindTileEntitySpecialRenderer(
				SkullChestTileEntity.class,
				new SkullChestTileEntityRenderer("skull-chest", new SkullChestModel()));
		
		// gold skull chest
		ClientRegistry.bindTileEntitySpecialRenderer(
				GoldSkullChestTileEntity.class,
				new SkullChestTileEntityRenderer("gold-skull-chest", new SkullChestModel()));
		
		// cauldron chest
		ClientRegistry.bindTileEntitySpecialRenderer(
				CauldronChestTileEntity.class,
				new CauldronChestTileEntityRenderer("cauldron-chest", new CauldronChestModel()));

		// spider chest
		ClientRegistry.bindTileEntitySpecialRenderer(
				SpiderChestTileEntity.class,
				new TreasureChestTileEntityRenderer("spider-chest", new SpiderChestModel()));
		
		// viking chest
		ClientRegistry.bindTileEntitySpecialRenderer(
				VikingChestTileEntity.class,
				new TreasureChestTileEntityRenderer("viking-chest", new VikingChestModel()));
        
        // cardboard box
		ClientRegistry.bindTileEntitySpecialRenderer(
				CardboardBoxTileEntity.class,
				new CardboardBoxTileEntityRenderer("cardboard-box", new CardboardBoxModel()));        

        // milk crate
		ClientRegistry.bindTileEntitySpecialRenderer(
				MilkCrateTileEntity.class,
                new MilkCrateTileEntityRenderer("milk-crate", new MilkCrateModel()));  

		/*
		 *  register the entity render handlers
		 */		
		RenderingRegistry.registerEntityRenderingHandler(WoodMimicEntity.class, 	
				new MimicEntityRenderer(
						Minecraft.getMinecraft().getRenderManager(), 
						new MimicModel(),
						new ResourceLocation(Treasure.MODID + ":textures/entity/mob/wood-mimic.png")));

		RenderingRegistry.registerEntityRenderingHandler(PirateMimicEntity.class, 	
				new MimicEntityRenderer(
						Minecraft.getMinecraft().getRenderManager(), 
						new MimicModel(),
						new ResourceLocation(Treasure.MODID + ":textures/entity/mob/pirate-mimic.png")));
		
		RenderingRegistry.registerEntityRenderingHandler(BoundSoulEntity.class, 	
				new BoundSoulRenderer(
						Minecraft.getMinecraft().getRenderManager()));
		
//	    RenderingRegistry.registerEntityRenderingHandler(Bound, IRenderFactory<? super T> renderFactory)
//	    {
//	        INSTANCE.entityRenderers.put(entityClass, renderFactory);
//	    }
	}
}
