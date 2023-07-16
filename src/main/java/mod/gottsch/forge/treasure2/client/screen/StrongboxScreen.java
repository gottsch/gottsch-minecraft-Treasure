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

import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.inventory.StrongboxContainerMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class StrongboxScreen extends AbstractChestScreen<StrongboxContainerMenu> {

	// this is the resource location for the background image for the GUI
	private static final ResourceLocation BG_TEXTURE = new ResourceLocation(Treasure.MODID, "textures/gui/screen/strongbox.png");

	/**
	 * 
	 * @param screenContainer
	 * @param playerInventory
	 * @param title
	 */
	public StrongboxScreen(StrongboxContainerMenu screenContainer, Inventory playerInventory, Component title) {
		super(screenContainer, playerInventory, title);
		// Set the width and height of the gui.  Should match the size of the texture!
		imageWidth = 176;
		imageHeight = 167;
		setBgTexture(BG_TEXTURE);
	}


}
