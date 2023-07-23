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
import mod.gottsch.forge.treasure2.core.chest.SkullChestType;
import mod.gottsch.forge.treasure2.core.config.Config;
import mod.gottsch.forge.treasure2.core.inventory.SkullChestContainerMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class SkullChestScreen extends AbstractChestScreen<SkullChestContainerMenu> {

	// this is the resource location for the background image for the GUI
	private static final ResourceLocation VANILLA_BG_TEXTURE = new ResourceLocation(Treasure.MODID, "textures/gui/screen/vanilla_skull_chest.png");
	private static final ResourceLocation BG_TEXTURE = new ResourceLocation(Treasure.MODID, "textures/gui/screen/skull_chest.png");
	private static final ResourceLocation GOLD_BG_TEXTURE = new ResourceLocation(Treasure.MODID, "textures/gui/screen/gold_skull_chest.png");
	private static final ResourceLocation CRYSTAL_BG_TEXTURE = new ResourceLocation(Treasure.MODID, "textures/gui/screen/crystal_skull_chest.png");

	private static final Color LIGHT_BONE = new Color(224, 209, 194);
	private static final Color DARK_BONE = new Color(87, 79, 66);
	
	private static final Color LIGHT_GOLD_BONE = new Color(242, 240, 214);
	private static final Color DARK_GOLD_BONE = new Color(99, 64, 0);
	
	private static final Color LIGHT_CRYSTAL_BONE = new Color(230, 246, 243);
	private static final Color DARK_CRYSTAL_BONE = new Color(77, 114, 108);
	
	/**
	 * 
	 * @param menu
	 * @param playerInventory
	 * @param title
	 */
	public SkullChestScreen(SkullChestContainerMenu menu, Inventory playerInventory, Component title) {
		super(menu, playerInventory, title);
		
		// Set the width and height of the gui.  Should match the size of the texture!
		imageWidth = 176;
		imageHeight = 167;
		if (Config.CLIENT.gui.enableCustomChestInventoryGui.get()) {
			if (menu.getSkullType() == SkullChestType.GOLD_SKULL) {
				setBgTexture(GOLD_BG_TEXTURE);
			}
			else if (menu.getSkullType() == SkullChestType.CRYSTAL_SKULL) {
				setBgTexture(CRYSTAL_BG_TEXTURE);
			}
			else {
				setBgTexture(BG_TEXTURE);
			}
		}
		else {
			setBgTexture(VANILLA_BG_TEXTURE);
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
		if (this.menu.getSkullType() == SkullChestType.GOLD_SKULL) {
			return LIGHT_GOLD_BONE.getRGB();
		} else if (this.menu.getSkullType() == SkullChestType.CRYSTAL_SKULL) {
			return LIGHT_CRYSTAL_BONE.getRGB();
		} else {
			return LIGHT_BONE.getRGB();
		}
	}
	
	@Override
	public int getCustomShadowColor() {
		if (this.menu.getSkullType() == SkullChestType.GOLD_SKULL) {
			return DARK_GOLD_BONE.getRGB();
		} else if (this.menu.getSkullType() == SkullChestType.CRYSTAL_SKULL) {
			return DARK_CRYSTAL_BONE.getRGB();
		} else {
			return DARK_BONE.getRGB();
		}
	}
}
