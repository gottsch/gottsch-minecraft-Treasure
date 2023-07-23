/*
 * This file is part of  Treasure2.
 * Copyright (c) 2023 Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.client.screen;

import java.awt.Color;

import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.config.Config;
import mod.gottsch.forge.treasure2.core.inventory.VikingChestContainerMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/**
 * 
 * @author Mark Gottschling May 25, 2023
 *
 */
public class VikingChestScreen extends AbstractChestScreen<VikingChestContainerMenu> {

	// this is the resource location for the background image for the GUI
	private static final ResourceLocation BG_TEXTURE = new ResourceLocation(Treasure.MODID, "textures/gui/screen/viking_chest.png");
	private static final Color CHEST_LIGHT_BROWN = new Color(188, 168, 142);//161, 142, 116);
	private static final Color CHEST_DARK_BROWN = new Color(38, 30, 14);
	
	/**
	 * 
	 * @param screenContainer
	 * @param playerInventory
	 * @param title
	 */
	public VikingChestScreen(VikingChestContainerMenu screenContainer, Inventory playerInventory, Component title) {
		super(screenContainer, playerInventory, title);
		if (Config.CLIENT.gui.enableCustomChestInventoryGui.get()) {			
			this.imageWidth = 176;
			this.imageHeight = 176;
			this.titleLabelY =+8;
			this.inventoryLabelY = this.imageHeight - 102;
			// TODO lookup a registry based on name to get the BG
			setBgTexture(BG_TEXTURE);
		}
	}
	
    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
    	if (Config.CLIENT.gui.enableCustomChestInventoryGui.get()) {
    		renderCustomLabels(guiGraphics, mouseX, mouseY);
    	}
    	else {
    		// vanilla
    		super.renderLabels(guiGraphics, mouseX, mouseY);
    	}
    }
    
	@Override
	public int getCustomColor() {
		return CHEST_LIGHT_BROWN.getRGB();
	}
	
	@Override
	public int getCustomShadowColor() {
		return CHEST_DARK_BROWN.getRGB();
	}
}
