/**
 * 
 */
package com.someguyssoftware.treasure2.proxy;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.client.model.BandedChestModel;
import com.someguyssoftware.treasure2.client.model.CompressorChestModel;
import com.someguyssoftware.treasure2.client.model.CrateChestModel;
import com.someguyssoftware.treasure2.client.model.DreadPirateChestModel;
import com.someguyssoftware.treasure2.client.model.MimicModel;
import com.someguyssoftware.treasure2.client.model.SafeModel;
import com.someguyssoftware.treasure2.client.model.StandardChestModel;
import com.someguyssoftware.treasure2.client.model.StrongboxModel;
import com.someguyssoftware.treasure2.client.model.WitherChestModel;
import com.someguyssoftware.treasure2.client.render.entity.MimicEntityRenderer;
import com.someguyssoftware.treasure2.client.render.tileentity.CompressorChestTileEntityRenderer;
import com.someguyssoftware.treasure2.client.render.tileentity.CrateChestTileEntityRenderer;
import com.someguyssoftware.treasure2.client.render.tileentity.SafeTileEntityRenderer;
import com.someguyssoftware.treasure2.client.render.tileentity.StrongboxTileEntityRenderer;
import com.someguyssoftware.treasure2.client.render.tileentity.TreasureChestTileEntityRenderer;
import com.someguyssoftware.treasure2.client.render.tileentity.WitherChestTileEntityRenderer;
import com.someguyssoftware.treasure2.entity.monster.WoodMimicEntity;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;
import com.someguyssoftware.treasure2.tileentity.CompressorChestTileEntity;
import com.someguyssoftware.treasure2.tileentity.CrateChestTileEntity;
import com.someguyssoftware.treasure2.tileentity.DreadPirateChestTileEntity;
import com.someguyssoftware.treasure2.tileentity.GoldStrongboxTileEntity;
import com.someguyssoftware.treasure2.tileentity.IronStrongboxTileEntity;
import com.someguyssoftware.treasure2.tileentity.IronboundChestTileEntity;
import com.someguyssoftware.treasure2.tileentity.MoldyCrateChestTileEntity;
import com.someguyssoftware.treasure2.tileentity.PirateChestTileEntity;
import com.someguyssoftware.treasure2.tileentity.SafeTileEntity;
import com.someguyssoftware.treasure2.tileentity.WitherChestTileEntity;
import com.someguyssoftware.treasure2.tileentity.WoodChestTileEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
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
		
		// compressor chest
		ClientRegistry.bindTileEntitySpecialRenderer(
				CompressorChestTileEntity.class,
				new CompressorChestTileEntityRenderer("compressor-chest", new CompressorChestModel()));
		
		// wither chest
		ClientRegistry.bindTileEntitySpecialRenderer(
				WitherChestTileEntity.class,
				new WitherChestTileEntityRenderer("wither-chest", new WitherChestModel()));
		
		/*
		 *  register the entity render handlers
		 */		
		RenderingRegistry.registerEntityRenderingHandler(WoodMimicEntity.class, 	
				new MimicEntityRenderer(
						Minecraft.getMinecraft().getRenderManager(), 
						new MimicModel(),
						new ResourceLocation(Treasure.MODID + ":textures/entity/mob/wood-mimic.png")));
		
	}
}
