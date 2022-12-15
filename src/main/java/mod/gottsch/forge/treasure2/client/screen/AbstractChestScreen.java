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

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.config.Config;
import mod.gottsch.forge.treasure2.core.inventory.AbstractTreasureContainerMenu;
import mod.gottsch.forge.treasure2.core.inventory.StandardChestContainerMenu;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/**
 * TODO look at AbstractContainerMenu for how to do custom props with only using 1 set of variables
 * ie it is moving the Config check out to the concrete classes
 * @author Mark Gottschling on Nov 20, 2022
 *
 */
public abstract class AbstractChestScreen<T extends AbstractTreasureContainerMenu> extends AbstractContainerScreen<T> {
	private static final ResourceLocation VANILLA_BG_TEXTURE = new ResourceLocation(Treasure.MODID, "textures/gui/screen/treasure_chest.png");
	
	private ResourceLocation bgTexture;
	private Inventory inventory;
	
	/**
	 * 
	 * @param containerMenu
	 * @param inventory
	 * @param name
	 */
	public AbstractChestScreen(T containerMenu, Inventory inventory, Component name) {
		super(containerMenu, inventory, name);
		this.inventory = inventory;
		this.bgTexture = VANILLA_BG_TEXTURE;

		// default vanilla chest size
		imageWidth = 176;
		imageHeight = 167;
		
		// TODO research - this might be custom label position because of the shadow
		this.inventoryLabelY +=1;
	}
	
    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        	RenderSystem.setShaderTexture(0, getBgTexture());
            int relX = (this.width - this.imageWidth) / 2;
            int relY = (this.height - this.imageHeight) / 2;
            this.blit(matrixStack, relX, relY, 0, 0, this.imageWidth, this.imageHeight);
    }
    
    /**
     * 
     * @param matrixStack
     * @param mouseX
     * @param mouseY
     */
    protected void renderCustomLabels(PoseStack matrixStack, int mouseX, int mouseY) {
		drawShadowLabel(matrixStack, title, this.titleLabelX, this.titleLabelY, getCustomColor(), getCustomShadowColor());
		drawShadowLabel(matrixStack, getInventory().getDisplayName(), this.inventoryLabelX, this.inventoryLabelY, getCustomColor(), getCustomShadowColor());
    }
    
    protected void drawShadowLabel(PoseStack matrixStack, Component title, int xpos, int ypos, int color, int shadow) {
    	this.font.draw(matrixStack, title, xpos+1, ypos+1, shadow);
    	this.font.draw(matrixStack, title, xpos, ypos, color);
    }
    
	public Inventory getInventory() {
		return inventory;
	}
	
    /**
     * 
     * @return
     */
	public ResourceLocation getBgTexture() {
		return bgTexture;
	}
	
	public void setBgTexture(ResourceLocation bgTexture) {
		this.bgTexture = bgTexture;
	}
	
	/*
	 * NOTE getCustomXXX methods are only called if Config.CLIENT.gui.enableCustomChestInventoryGui is enabled.
	 */
	
	public int getCustomColor() {
		return 0;
	}

	public int getCustomShadowColor() {
		return 0;
	}
	
}
