/**
 * 
 */
package com.someguyssoftware.treasure2.generator.structure;

import java.util.Map.Entry;
import java.util.Random;

import com.someguyssoftware.gottschcore.positional.Coords;
import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.gottschcore.world.gen.structure.GottschTemplate;
import com.someguyssoftware.gottschcore.world.gen.structure.StructureMarkers;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.generator.GenUtil;
import com.someguyssoftware.treasure2.world.gen.structure.IStructureInfo;
import com.someguyssoftware.treasure2.world.gen.structure.StructureInfo;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;

/**
 * TODO should implement IStructureInfoProvider and return IGeneratorResult from generate() method?
 * @author Mark Gottschling on Jan 24, 2019
 *
 */
public class StructureGenerator implements IStructureGenerator {

	private Block nullBlock;
	
	public StructureGenerator() {}
	
	/**
	 * 
	 */
	@Override
	public IStructureInfo generate(World world, Random random, GottschTemplate template, PlacementSettings placement,
			ICoords spawnCoords) {

		// generate the structure
		template.addBlocksToWorld(world, spawnCoords.toPos(), placement, getNullBlock(), 3);
		
		// TODO should replace map blocks prior to addBlocksToWorld(), replacing them with null blocks
		// OR this should be handled within the addBlocksToWorld() call itself, checking if a block is a map block.
		
		// TODO if this is handled on template read, this block can go away
		// remove any extra special blocks
		for (ICoords coords : template.getMapCoords()) {
			ICoords c = GottschTemplate.transformedCoords(placement, coords);
			// TODO shouldn't be setting to air, but to null block
			world.setBlockToAir(spawnCoords.toPos().add(c.toPos()));
//			Treasure.logger.debug("removing mapped block -> {} : {}", c, spawnCoords.toPos().add(c.toPos()));
		}
		
		// get the transformed size
		BlockPos transformedSize = template.transformedSize(placement.getRotation());
				
		// TODO need to capture the facing or meta of the chest, perform the rotation on the facing  and save it in the Map with the pos... need a new object to hold more data
				
		// update StrucutreInfo
		IStructureInfo info = new StructureInfo();
		info.setCoords(spawnCoords);
		info.setSize(new Coords(transformedSize));
		// process all specials and adding them to the StructureInfo
		// TODO change to stream
		for (Entry<Block, ICoords> entry : template.getMap().entries()) {
			ICoords c = new Coords(GottschTemplate.transformedCoords(placement, entry.getValue()));
			info.getMap().put(entry.getKey(), c);
			Treasure.logger.debug("adding to structure info transformed coords -> {} : {}", entry.getKey().getLocalizedName(), c.toShortString());
		}
		
		return info;
	}

	@Override
	public Block getNullBlock() {
		if (nullBlock == null) {
			nullBlock = GenUtil.getMarkerBlock(StructureMarkers.NULL);
		}
		return nullBlock;
	}

	@Override
	public void setNullBlock(Block nullBlock) {
		this.nullBlock = nullBlock;
	}

}