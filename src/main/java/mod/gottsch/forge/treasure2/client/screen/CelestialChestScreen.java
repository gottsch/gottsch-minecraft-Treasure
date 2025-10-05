/*
 * This file is part of Treasure2.
 * Copyright (c) 2025 Mark Gottschling (gottsch)
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the Open Software Licence 3.0.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Open Software Licence 3.0 for more details.
 *
 * You should have received a copy of the Open Software Licence
 * along with Treasure2. If not, see <https://www.tldrlegal.com/license/open-software-licence-3-0>.
 */
package mod.gottsch.forge.treasure2.client.screen;

import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.config.Config;
import mod.gottsch.forge.treasure2.core.inventory.CelestialChestContainerMenu;
import mod.gottsch.forge.treasure2.core.inventory.StandardChestContainerMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.awt.*;

/**
 *
 * @author Mark Gottschling on 9/27/2025
 *
 */
public class CelestialChestScreen extends AbstractChestScreen<CelestialChestContainerMenu> {
	/**
	 *
	 * @param containerMenu
	 * @param inventory
	 * @param name
	 */
	public CelestialChestScreen(CelestialChestContainerMenu containerMenu, Inventory inventory, Component name) {
		super(containerMenu, inventory, name);
		
//		if (Config.CLIENT.gui.enableCustomChestInventoryGui.get()) {
//			this.imageWidth = 176;
//			this.imageHeight = 176;
//			this.titleLabelY =+8;
//			this.inventoryLabelY = this.imageHeight - 102;
//
//			// NOTE uses vanilla BG
//		}
	}
	
    @Override
    protected void renderLabels(GuiGraphics matrixStack, int mouseX, int mouseY) {
//    	if (Config.CLIENT.gui.enableCustomChestInventoryGui.get()) {
//    		renderCustomLabels(matrixStack, mouseX, mouseY);
//    	}
//    	else {
//    		// vanilla
//    		super.renderLabels(matrixStack, mouseX, mouseY);
//    	}
		// NOTE uses vanilla
		super.renderLabels(matrixStack, mouseX, mouseY);
    }
}
