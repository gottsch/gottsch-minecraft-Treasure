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
package mod.gottsch.forge.treasure2.client.screen;

import java.awt.Color;

import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.config.Config;
import mod.gottsch.forge.treasure2.core.inventory.StandardChestContainerMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/**
 *
 * @author Mark Gottschling on Nov 18, 2022
 *
 */
public class StandardChestScreen extends AbstractChestScreen<StandardChestContainerMenu> {
	// the resource locations for the background images of the GUI
	private static final ResourceLocation BG_TEXTURE = new ResourceLocation(Treasure.MODID, "textures/gui/screen/wood_chest.png");
	private static final Color CHEST_LIGHT_BROWN = new Color(188, 168, 142);//161, 142, 116);
	private static final Color CHEST_DARK_BROWN = new Color(38, 30, 14);

	/**
	 * 
	 * @param containerMenu
	 * @param inventory
	 * @param name
	 */
	public StandardChestScreen(StandardChestContainerMenu containerMenu, Inventory inventory, Component name) {
		super(containerMenu, inventory, name);
		
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
    protected void renderLabels(GuiGraphics matrixStack, int mouseX, int mouseY) {
    	if (Config.CLIENT.gui.enableCustomChestInventoryGui.get()) {
    		renderCustomLabels(matrixStack, mouseX, mouseY);
    	}
    	else {
    		// vanilla
    		super.renderLabels(matrixStack, mouseX, mouseY);
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
