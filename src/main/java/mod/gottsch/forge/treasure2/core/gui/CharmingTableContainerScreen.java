/*
 * This file is part of  Treasure2.
 * Copyright (c) 2022 Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.core.gui;

import java.awt.Color;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import mod.gottsch.forge.treasure2.core.Treasure;
import mod.gottsch.forge.treasure2.core.inventory.CharmingTableContainer;
import mod.gottsch.forge.treasure2.core.inventory.PouchContainer;
import mod.gottsch.forge.treasure2.core.util.ModUtils;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * 
 * @author Mark Gottschling on Feb 10, 2022
 *
 */
public class CharmingTableContainerScreen extends ContainerScreen<CharmingTableContainer> {
	public static final ResourceLocation TEXTURE = ModUtils.asLocation("textures/gui/container/charming_table.png");

	public static final ITextComponent LABEL = new TranslationTextComponent("display.charming_table.name").withStyle(TextFormatting.BLACK);
	public static final ITextComponent CHARMS_LABEL = new TranslationTextComponent("display.charming_table.charms").withStyle(TextFormatting.BLACK);
	public static final ITextComponent RUNES_LABEL = new TranslationTextComponent("display.charming_table.runes").withStyle(TextFormatting.BLACK);

	private final CharmingTableContainer bench;
	private final PlayerInventory playerInventory;

	/**
	 * 
	 * @param container
	 * @param playerInventory
	 * @param title
	 */
	public CharmingTableContainerScreen(CharmingTableContainer container, PlayerInventory inventory,
			ITextComponent title) {
		super(container, inventory, title);
		this.playerInventory = inventory;
		this.bench = (CharmingTableContainer) this.menu;

		// Set the width and height of the gui.  Should match the size of the TEXTURE!
		imageWidth = 177;
		imageHeight = 256;
	}

	@Override
	public void render(MatrixStack matrix, int mouseX, int mouseY, float p_230430_4_) {
		this.renderBackground(matrix);
		super.render(matrix, mouseX, mouseY, p_230430_4_);
		this.renderTooltip(matrix, mouseX, mouseY);
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of the items)
	 * Taken directly from ChestScreen
	 */
	@Override
	public void renderLabels(MatrixStack matrixStack, int mouseX, int mouseY ) {
		final int LABEL_XPOS = 5;
		final int LABEL_YPOS = 5;

		font.draw(matrixStack, LABEL, LABEL_XPOS, LABEL_YPOS, Color.GRAY.getRGB());
		font.draw(matrixStack, CHARMS_LABEL, LABEL_XPOS, 38, Color.GRAY.getRGB());
		font.draw(matrixStack, RUNES_LABEL, LABEL_XPOS, 129, Color.GRAY.getRGB());

		// add cost line
		if (this.bench.maximumCost > 0 && (this.bench.getSlot(2).hasItem())
				|| this.bench.getSlot(11).hasItem()
				|| this.bench.getSlot(12).hasItem()
				|| this.bench.getSlot(13).hasItem()
				|| this.bench.getSlot(14).hasItem()
				|| this.bench.getSlot(17).hasItem()) {
			ITextComponent s = new TranslationTextComponent("display.charming_table.cost", this.bench.maximumCost);
			int k = this.width - 8 - font.width(s);
			font.draw(matrixStack, s, k+1, 162, Color.GRAY.getRGB());
			font.draw(matrixStack, s, k, 161, Color.GREEN.getRGB());
		}
	}

	@Override
	protected void renderBg(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bind(TEXTURE);

		// width and height are the size provided to the window when initialised after creation.
		// xSize, ySize are the expected size of the TEXTURE-? usually seems to be left as a default.
		// The code below is typical for vanilla containers, so I've just copied that- it appears to centre the TEXTURE within
		//  the available window
		int edgeSpacingX = (this.width - this.imageWidth) / 2;
		int edgeSpacingY = (this.height - this.imageHeight) / 2;
		this.blit(matrixStack, edgeSpacingX, edgeSpacingY, 0, 0, this.imageWidth, this.imageHeight);
	}
}
