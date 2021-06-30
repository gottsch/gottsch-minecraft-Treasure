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
package com.someguyssoftware.treasure2.world.gen.structure;

import java.util.Random;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.tileentity.ITreasureChestTileEntity;

import net.minecraft.block.Blocks;
import net.minecraft.loot.LootTables;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.IglooPieces;
import net.minecraft.world.gen.feature.structure.TemplateStructurePiece;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;

/**
 * @author Mark Gottschling on Jun 20, 2021
 *
 */
public class TreasureStructurePiece extends TemplateStructurePiece {
	private static final String CHEST_MARKER_NAME = "chest";
	private static final String BOSS_CHEST_MARKER_NAME = "boss_chest";
	private static final String ENTRANCE_MARKER_NAME = "entrance";
	private static final String SPAWNER_MARKER_NAME = "spawner";
	private static final String ONE_TIME_SPAWNER_MARKER_NAME = "one_time_spawner";
	
    private final ResourceLocation templateLocation;
    private final Rotation rotation;
 
    public TreasureStructurePiece(TemplateManager templateManager, ResourceLocation location, BlockPos pos, Rotation rotation, int yOffset) {
        super(IStructurePieceType.IGLOO, 0);
        this.templateLocation = location;
        BlockPos blockpos = pos; //IglooPieces.OFFSETS.get(p_i49313_2_);
        this.templatePosition = pos.offset(blockpos.getX(), blockpos.getY() - yOffset, blockpos.getZ());
        this.rotation = rotation;
        this.loadTemplate(templateManager);
     }
    
    /**
     * 
     * @param templateManager
     */
    private void loadTemplate(TemplateManager templateManager) {
        Template template = templateManager.getOrCreate(this.templateLocation);
        // TODO set the pivot point to mid-piece or on connector position or chest position (so no offset calculations are necessary) 
        PlacementSettings placementsettings = new PlacementSettings().setRotation(this.rotation).setMirror(Mirror.NONE);
        this.setup(template, this.templatePosition, placementsettings);
     }
    
	@Override
	protected void handleDataMarker(String markerName, BlockPos pos, IServerWorld world, Random random, MutableBoundingBox boundingBox) {
		Treasure.LOGGER.debug("handling data marker -> {}", markerName);
        if (CHEST_MARKER_NAME.equals(markerName)) {
        	// remove data marker
            world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            // get the tile entity below the marker
            TileEntity tileEntity = world.getBlockEntity(pos.below());
            if (tileEntity instanceof ChestTileEntity) {
            	Treasure.LOGGER.debug("replacing chest tile entity");
            	// TODO replace current chest with Treasure Chest - rarity and everything should be set in the piece so it is accessible
               //( (ChestTileEntity)tileEntity).setLootTable(LootTables.IGLOO_CHEST, random.nextLong());
            }

         }

	}

}
