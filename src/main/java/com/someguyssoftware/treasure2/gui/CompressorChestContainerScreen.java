package com.someguyssoftware.treasure2.gui;

import java.awt.Color;

import com.mojang.blaze3d.systems.RenderSystem;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.inventory.CompressorChestContainer;
import com.someguyssoftware.treasure2.inventory.StrongboxChestContainer;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class CompressorChestContainerScreen extends ContainerScreen<CompressorChestContainer> {

	// this is the resource location for the background image for the GUI
	private static final ResourceLocation texture = new ResourceLocation(Treasure.MODID, "textures/gui/container/compressor_chest.png");

	/**
	 * 
	 * @param screenContainer
	 * @param playerInventory
	 * @param title
	 */
	public CompressorChestContainerScreen(CompressorChestContainer screenContainer, PlayerInventory playerInventory,
			ITextComponent title) {
		super(screenContainer, playerInventory, title);
		// Set the width and height of the gui.  Should match the size of the texture!
		xSize = 247;
		ySize = 184;
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of the items)
	 * Taken directly from ChestScreen
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		final float LABEL_XPOS = 5;
		final float FONT_Y_SPACING = 10;
		final float CHEST_LABEL_YPOS = getContainer().getContainerInventoryYPos() - FONT_Y_SPACING;
		font.drawString(this.title.getFormattedText(), LABEL_XPOS, CHEST_LABEL_YPOS, Color.darkGray.getRGB());
		final float PLAYER_INV_LABEL_YPOS = getContainer().getPlayerInventoryYPos() - FONT_Y_SPACING;
		this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 
				LABEL_XPOS, PLAYER_INV_LABEL_YPOS, Color.darkGray.getRGB());
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bindTexture(texture);

		// width and height are the size provided to the window when initialised after creation.
		// xSize, ySize are the expected size of the texture-? usually seems to be left as a default.
		// The code below is typical for vanilla containers, so I've just copied that- it appears to centre the texture within
		//  the available window
		int edgeSpacingX = (this.width - this.xSize) / 2;
		int edgeSpacingY = (this.height - this.ySize) / 2;
		this.blit(edgeSpacingX, edgeSpacingY, 0, 0, this.xSize, this.ySize);
	}

}
