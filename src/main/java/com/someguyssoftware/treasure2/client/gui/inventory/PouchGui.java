/*
 * This file is part of  Treasure2.
 * Copyright (c) 2021, Mark Gottschling (gottsch)
 * 
 * All rights reserved.
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Treasure2.  If not, see <http://www.gnu.org/licenses/lgpl>.
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

	public static final ResourceLocation TEXTURE = new ResourceLocation(Treasure.MODID, "textures/gui/container/pouch.png");
	
	@SuppressWarnings("deprecation")
	public static final String POUCH_LABEL = 	TextFormatting.BLACK + "" + I18n.translateToLocal("item.treasure2:pouch.name");

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
		
		// Bind the image texture of our custom container
		Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);
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
		
		fontRenderer.drawString(POUCH_LABEL, LABEL_XPOS, LABEL_YPOS, Color.darkGray.getRGB());
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
