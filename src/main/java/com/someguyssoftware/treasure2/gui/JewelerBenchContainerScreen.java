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
package com.someguyssoftware.treasure2.gui;

import java.awt.Color;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.someguyssoftware.treasure2.inventory.JewelerBenchContainer;
import com.someguyssoftware.treasure2.util.ModUtils;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * 
 * @author Mark Gottschling on Feb 4, 2022
 *
 */
public class JewelerBenchContainerScreen extends ContainerScreen<JewelerBenchContainer> {

	// This is the resource location for the background images for the GUI

	public static final ResourceLocation TEXTURE = ModUtils.asLocation("textures/gui/container/jeweler_bench.png");

	@SuppressWarnings("deprecation")
	public static final ITextComponent LABEL =  new TranslationTextComponent("display.jeweler_bench.name").withStyle(TextFormatting.BLACK);

	private final JewelerBenchContainer bench;
	//    private GuiTextField nameField;
	private final PlayerInventory playerInventory;

	/**
	 * 
	 * @param inventory
	 * @param inventory
	 */
	public JewelerBenchContainerScreen(JewelerBenchContainer container, PlayerInventory inventory, ITextComponent title) {
		super(container, inventory, title);
		this.playerInventory = inventory;
		this.bench = (JewelerBenchContainer) this.menu;

		// Set the width and height of the gui. Should match the size of the texture!
		imageWidth = 177;
		imageHeight = 200;
	}

	@Override
	public void render(MatrixStack matrix, int mouseX, int mouseY, float p_230430_4_) {
		this.renderBackground(matrix);
		super.render(matrix, mouseX, mouseY, p_230430_4_);
		this.renderTooltip(matrix, mouseX, mouseY);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.minecraft.client.gui.inventory.GuiContainer#
	 * drawGuiContainerBackgroundLayer(float, int, int)
	 */
	@Override
	protected void renderBg(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bind(TEXTURE);
		// draw the image
		int edgeSpacingX = (this.width - this.imageWidth) / 2;
		int edgeSpacingY = (this.height - this.imageHeight) / 2;
		this.blit(matrixStack, edgeSpacingX, edgeSpacingY, 0, 0, this.imageWidth, this.imageHeight);
	}

	// draw the foreground for the GUI - rendered after the slots, but before the
	// dragged items and tooltips
	// renders relative to the top left corner of the background
	@Override
	public void renderLabels(MatrixStack matrixStack, int mouseX, int mouseY ) {
		final int LABEL_XPOS = 5;
		final int LABEL_YPOS = 5;

		font.draw(matrixStack, LABEL, LABEL_XPOS, LABEL_YPOS, Color.GRAY.getRGB());

		// add cost line
		if (this.bench.maximumCost > 0 && (this.bench.getSlot(4).hasItem() || this.bench.getSlot(5).hasItem()
				|| this.bench.getSlot(6).hasItem())) {
			ITextComponent s = new TranslationTextComponent("display.jeweler_bench.cost", this.bench.maximumCost);
			int k = this.width - 8 - font.width(s);
			font.draw(matrixStack, s, k+1, 75, Color.GRAY.getRGB());
			font.draw(matrixStack, s, k, 74, Color.GREEN.getRGB());
		}
	}

	public void refreshContainer(Container p_71110_1_, NonNullList<ItemStack> p_71110_2_) {
		this.slotChanged(p_71110_1_, 0, p_71110_1_.getSlot(0).getItem());
	}

	public void setContainerData(Container p_71112_1_, int p_71112_2_, int p_71112_3_) {
	}

	public void slotChanged(Container p_71111_1_, int p_71111_2_, ItemStack p_71111_3_) {
	}
}
