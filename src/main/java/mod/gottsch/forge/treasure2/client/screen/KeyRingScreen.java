/*
 * This file is part of  Treasure2.
 * Copyright (c) 2021 Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.client.screen;

import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.inventory.KeyRingContainerMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/**
 * 
 * @author Mark Gottschling on 2021
 *
 */
public class KeyRingScreen extends AbstractChestScreen<KeyRingContainerMenu> {
	// This is the resource location for the background image for the GUI
		private static final ResourceLocation TEXTURE = new ResourceLocation(Treasure.MODID, "textures/gui/screen/key_ring2.png");
		
	/**
	 * 
	 * @param menu
	 * @param playerInventory
	 * @param title
	 */
	public KeyRingScreen(KeyRingContainerMenu menu, Inventory playerInventory, Component title) {
		super(menu, playerInventory, title);
		// Set the width and height of the gui.  Should match the size of the texture!
		imageWidth = 176;
		imageHeight = 182;
		this.inventoryLabelY = this.imageHeight - 93;
		setBgTexture(TEXTURE);
	}

    @Override
    protected void renderLabels(GuiGraphics matrixStack, int mouseX, int mouseY) {
    	// force vanilla
		super.renderLabels(matrixStack, mouseX, mouseY);
    } 

}
