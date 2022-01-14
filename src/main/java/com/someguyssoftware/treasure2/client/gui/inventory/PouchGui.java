/**
 * 
 */
package com.someguyssoftware.treasure2.client.gui.inventory;

import java.awt.Color;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.inventory.KeyRingContainer;
import com.someguyssoftware.treasure2.inventory.PouchContainer;
import com.someguyssoftware.treasure2.inventory.StrongboxChestContainer;
import com.someguyssoftware.treasure2.item.PouchItem;
import com.someguyssoftware.treasure2.item.TreasureItems;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;

/**
 * 
 * @author Mark Gottschling on May 14, 2020
 *
 */
public class PouchGui extends GuiContainer {

	// This is the resource location for the background images for the GUI

	public static final ResourceLocation[] TEXTURES = {
			new ResourceLocation(Treasure.MODID, "textures/gui/container/pouch.png"),
			new ResourceLocation(Treasure.MODID, "textures/gui/container/lucky_pouch.png"),
			new ResourceLocation(Treasure.MODID, "textures/gui/container/apprentices_pouch.png"),
			new ResourceLocation(Treasure.MODID, "textures/gui/container/masters_pouch.png")
	};
	@SuppressWarnings("deprecation")
	public static final String[] POUCH_LABELS = {
			TextFormatting.BLACK + "" + I18n.translateToLocal("item.treasure2:pouch.name"),
			TextFormatting.BLACK + "" + I18n.translateToLocal("item.treasure2:lucky_pouch.name"),
			TextFormatting.BLACK + "" + I18n.translateToLocal("item.treasure2:apprentices_pouch.name"),
			TextFormatting.BLACK + "" + I18n.translateToLocal("item.treasure2:masters_pouch.name")
	};

	private PouchItem pouchItem;
	
	/**
	 * 
	 * @param invPlayer
	 * @param inventory
	 */
	public PouchGui(InventoryPlayer invPlayer, IInventory inventory, ItemStack stack) {
		super(new PouchContainer(invPlayer, inventory, stack));
		pouchItem = (PouchItem)stack.getItem();
		
		// Set the width and height of the gui.  Should match the size of the texture!
		xSize = 177;
		ySize = 200;
	}	
	
	/* (non-Javadoc)
	 * @see net.minecraft.client.gui.inventory.GuiContainer#drawGuiContainerBackgroundLayer(float, int, int)
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		ResourceLocation texture = null;
		if (pouchItem == TreasureItems.POUCH) {
			texture = TEXTURES[0];
		}
//		else if (pouchItem == TreasureItems.LUCKY_POUCH) {
//			texture = TEXTURES[1];
//		}
//		else if (pouchItem == TreasureItems.APPRENTICES_POUCH) {
//			texture = TEXTURES[2];
//		}
//		else if (pouchItem == TreasureItems.MASTERS_POUCH) {
//			texture = TEXTURES[3];
//		}
		
		// Bind the image texture of our custom container
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		// Draw the image
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}

	// draw the foreground for the GUI - rendered after the slots, but before the dragged items and tooltips
	// renders relative to the top left corner of the background
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		final int LABEL_XPOS = 5;
		final int LABEL_YPOS = 5;
		
		String label = "";
		if (pouchItem == TreasureItems.POUCH) {
			label = POUCH_LABELS[0];
		}
//		else if (pouchItem == TreasureItems.LUCKY_POUCH) {
//			label = POUCH_LABELS[1];
//		}
//		else if (pouchItem == TreasureItems.APPRENTICES_POUCH) {
//			label = POUCH_LABELS[2];
//		}
//		else if (pouchItem == TreasureItems.MASTERS_POUCH) {
//			label = POUCH_LABELS[3];
//		}
		
		fontRenderer.drawString(label, LABEL_XPOS, LABEL_YPOS, Color.darkGray.getRGB());
	}
	
	/**
	 * 
	 */
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
	}
}
