/**
 * 
 */
package com.someguyssoftware.treasure2.generator.structure;

import java.util.Map.Entry;
import java.util.Random;

import com.someguyssoftware.gottschcore.positional.Coords;
import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.treasure2.enums.StructureMarkers;
import com.someguyssoftware.treasure2.generator.GenUtil;
import com.someguyssoftware.treasure2.world.gen.structure.IStructureInfo;
import com.someguyssoftware.treasure2.world.gen.structure.StructureInfo;
import com.someguyssoftware.treasure2.world.gen.structure.TreasureTemplate;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;

/**
 * @author Mark Gottschling on Jan 24, 2019
 *
 */
public class StructureGenerator implements IStructureGenerator {

	@Override
	public IStructureInfo generate(World world, Random random, TreasureTemplate template, PlacementSettings placement,
			ICoords spawnCoords) {
		
		// generate the structure
		template.addBlocksToWorld(world, spawnCoords.toPos(), placement, GenUtil.getMarkerBlock(StructureMarkers.NULL), 3);
		
		// remove any extra special blocks
		for (ICoords coords : template.getMapCoords()) {
			// TODO skip Offset blocks. (like a null block)
			ICoords c = TreasureTemplate.transformedCoords(placement, coords);
			world.setBlockToAir(spawnCoords.toPos().add(c.toPos()));
//			Treasure.logger.debug("removing mapped block -> {} : {}", c, spawnCoords.toPos().add(c.toPos()));
		}
		
		// get the transformed size
		BlockPos transformedSize = template.transformedSize(placement.getRotation());
		
		// TODO remove
		// get the transformed entrance
		ICoords entranceCoords = template.findCoords(random, GenUtil.getMarkerBlock(StructureMarkers.ENTRANCE));
		if (entranceCoords != null) {
			entranceCoords = new Coords(TreasureTemplate.transformedCoords(placement, entranceCoords));
		}
		// TODO remove
		// get the transformed chest
		ICoords chestCoords = template.findCoords(random, GenUtil.getMarkerBlock(StructureMarkers.CHEST));
		if (chestCoords != null) {
			chestCoords = new Coords(TreasureTemplate.transformedCoords(placement, chestCoords));
		}

		// TODO need to capture the facing or meta of the chest, perform the rotation on the facing  and save it in the Map with the pos... need a new object to hold more data
				
		// update StrucutreInfo
		IStructureInfo info = new StructureInfo();
		info.setCoords(spawnCoords);
		info.setSize(new Coords(transformedSize));
		// process all specials and adding them to the StructureInfo
		// TODO change to stream
		for (Entry<Block, ICoords> entry : template.getMap().entries()) {
			ICoords c = new Coords(TreasureTemplate.transformedCoords(placement, entry.getValue()));
			info.getMap().put(entry.getKey(), c);
		}
		
		return info;
	}

}