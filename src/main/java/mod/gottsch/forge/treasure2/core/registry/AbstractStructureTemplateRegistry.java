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
package mod.gottsch.forge.treasure2.core.registry;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.mojang.datafixers.DataFixer;

import mod.gottsch.forge.gottschcore.world.gen.structure.StructureMarkers;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

// TODO move to GottschCore
/*
 *  TODO this won't work as abstract with static methods, as you'll have to
 *  call them like AbstractStructureTemplateRegistry.getMarkerMap().
 *  As well, the reason the Registry and Manager classes were separate was
 *  because the Manager classes were instances and could be abstracted whereas
 *  the Registry classes were Singletons.
 */

/**
 * 
 * @author Mark Gottschling on Nov 30, 2022
 *
 */
public abstract class AbstractStructureTemplateRegistry {
	private static DataFixer fixer;
	
	/*
	 * 
	 */
	private static final Map<StructureMarkers, Block> markerMap;
	
	/*
	 * standard list of marker blocks to scan for 
	 */
	private static final List<Block> markerScanList;
	
	/*
	 * standard list of replacements blocks.
	 * NOTE needs to be <IBlockState, IBlockState> (for v1.12.x anyway)
	 */
	private static final Map<BlockState, BlockState> replacementMap;
	
	static {
        // setup standard list of markers
        markerMap = Maps.newHashMapWithExpectedSize(10);
        markerMap.put(StructureMarkers.CHEST, Blocks.CHEST);
        markerMap.put(StructureMarkers.BOSS_CHEST, Blocks.ENDER_CHEST);
        markerMap.put(StructureMarkers.SPAWNER, Blocks.SPAWNER);
        markerMap.put(StructureMarkers.ENTRANCE, Blocks.GOLD_BLOCK);
        markerMap.put(StructureMarkers.OFFSET, Blocks.REDSTONE_BLOCK);
        markerMap.put(StructureMarkers.PROXIMITY_SPAWNER, Blocks.IRON_BLOCK);
        markerMap.put(StructureMarkers.NULL, Blocks.BEDROCK);
        
        // default marker scan list
        markerScanList = Arrays.asList(new Block[] {
    			markerMap.get(StructureMarkers.CHEST),
    			markerMap.get(StructureMarkers.BOSS_CHEST),
    			markerMap.get(StructureMarkers.SPAWNER),
    			markerMap.get(StructureMarkers.ENTRANCE),
    			markerMap.get(StructureMarkers.OFFSET),
    			markerMap.get(StructureMarkers.PROXIMITY_SPAWNER)
    			});
        
        // TODO rename this to preWriteReplacementMap();
        // default replacement scan map
        replacementMap = Maps.newHashMap();
        replacementMap.put(Blocks.WHITE_WOOL.defaultBlockState(), Blocks.AIR.defaultBlockState());
        replacementMap.put(Blocks.BLACK_STAINED_GLASS.defaultBlockState(), Blocks.SPAWNER.defaultBlockState());    

	}
	
	private AbstractStructureTemplateRegistry() {}
	
	/**
	 * 
	 * @param fixer
	 */
	public void create(DataFixer fixer) {
		AbstractStructureTemplateRegistry.fixer = fixer;		
	}
		
	public Map<StructureMarkers, Block> getMarkerMap() {
		return markerMap;
	}

}
