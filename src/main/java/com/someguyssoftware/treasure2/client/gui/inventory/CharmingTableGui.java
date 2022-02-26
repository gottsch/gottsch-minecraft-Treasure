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
import com.someguyssoftware.treasure2.inventory.CharmingTableContainer;
import com.someguyssoftware.treasure2.inventory.JewelerBenchContainer;
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
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerRepair;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

/**
 * 
 * @author Mark Gottschling on Feb 10, 2022
 *
 */
public class CharmingTableGui extends GuiContainer implements IContainerListener {

	// This is the resource location for the background images for the GUI

	public static final ResourceLocation TEXTURE = new ResourceLocation(Treasure.MODID,
			"textures/gui/container/charming_table.png");

	@SuppressWarnings("deprecation")
	public static final String LABEL = TextFormatting.BLACK + "" + I18n.translateToLocal("display.charming_table.name");
	public static final String CHARMS_LABEL = TextFormatting.BLACK + "" + I18n.translateToLocal("display.charming_table.charms");
	public static final String RUNES_LABEL = TextFormatting.BLACK + "" + I18n.translateToLocal("display.charming_table.runes");

	private final CharmingTableContainer bench;
	private final InventoryPlayer playerInventory;

	/**
	 * 
	 * @param inventory
	 * @param inventory
	 */
	public CharmingTableGui(InventoryPlayer inventory, World world) {
		super(new CharmingTableContainer(inventory, world, Minecraft.getMinecraft().player));
		this.playerInventory = inventory;
		this.bench = (CharmingTableContainer) this.inventorySlots;

		// Set the width and height of the gui. Should match the size of the texture!
		xSize = 177;
		ySize = 256;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.minecraft.client.gui.inventory.GuiContainer#
	 * drawGuiContainerBackgroundLayer(float, int, int)
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

		// Bind the image texture of our custom container
		Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);
		// Draw the image
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}

	// draw the foreground for the GUI - rendered after the slots, but before the
	// dragged items and tooltips
	// renders relative to the top left corner of the background
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		final int LABEL_XPOS = 5;
		final int LABEL_YPOS = 5;

		fontRenderer.drawString(LABEL, LABEL_XPOS, LABEL_YPOS, Color.GRAY.getRGB());
		fontRenderer.drawString(CHARMS_LABEL, LABEL_XPOS, 38, Color.GRAY.getRGB());
		fontRenderer.drawString(RUNES_LABEL, LABEL_XPOS, 129, Color.GRAY.getRGB());
		
		// add cost line
		
//		try {
//			if (this.bench.maximumCost > 0 && (this.bench.getSlot(4).getHasStack() || this.bench.getSlot(5).getHasStack()
//					|| this.bench.getSlot(6).getHasStack())) {
//				String s = I18n.translateToLocalFormatted("display.charming_bench.cost", this.bench.maximumCost);
//				int k = this.xSize - 8 - this.fontRenderer.getStringWidth(s);
//				fontRenderer.drawString(s, k+1, 75, Color.GRAY.getRGB());
//				fontRenderer.drawString(s, k, 74, TextFormatting.GOLD.getColorIndex());
//			}
//		}
//		catch(Exception e) {
//			Treasure.logger.error("error in gui:", e);
//		}
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

	@Override
	public void sendAllContents(Container containerToSend, NonNullList<ItemStack> itemsList) {
		// send the input slot ???
		this.sendSlotContents(containerToSend, 0, containerToSend.getSlot(0).getStack());
	}

	@Override
	public void sendSlotContents(Container containerToSend, int slotIndex, ItemStack stack) {
	}

	@Override
	public void sendWindowProperty(Container containerIn, int varToUpdate, int newValue) {
	}

	@Override
	public void sendAllWindowProperties(Container containerIn, IInventory inventory) {
	}
}
