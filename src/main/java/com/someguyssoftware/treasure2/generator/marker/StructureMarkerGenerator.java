/**
 * 
 */
package com.someguyssoftware.treasure2.generator.marker;

import java.util.List;
import java.util.Random;

import com.someguyssoftware.gottschcore.measurement.Quantity;
import com.someguyssoftware.gottschcore.spatial.Coords;
import com.someguyssoftware.gottschcore.spatial.ICoords;
import com.someguyssoftware.gottschcore.tileentity.ProximitySpawnerTileEntity;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.gottschcore.world.gen.structure.BlockContext;
import com.someguyssoftware.gottschcore.world.gen.structure.GottschTemplate;
import com.someguyssoftware.gottschcore.world.gen.structure.PlacementSettings;
import com.someguyssoftware.gottschcore.world.gen.structure.StructureMarkers;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.TreasureBlocks;
import com.someguyssoftware.treasure2.generator.GeneratorData;
import com.someguyssoftware.treasure2.generator.GeneratorResult;
import com.someguyssoftware.treasure2.generator.TemplateGeneratorData;
import com.someguyssoftware.treasure2.meta.StructureArchetype;
import com.someguyssoftware.treasure2.meta.StructureType;
import com.someguyssoftware.treasure2.registry.TreasureTemplateRegistry;
import com.someguyssoftware.treasure2.world.gen.structure.ITemplateGenerator;
import com.someguyssoftware.treasure2.world.gen.structure.TemplateGenerator;
import com.someguyssoftware.treasure2.world.gen.structure.TemplateHolder;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.DungeonHooks;

/**
 * @author Mark Gottschling on Jan 28, 2019
 *
 */
public class StructureMarkerGenerator implements IMarkerGenerator<GeneratorResult<GeneratorData>> {

	/**
	 * 
	 */
	public StructureMarkerGenerator() {
	}

	@Override
	public GeneratorResult<GeneratorData> generate(IServerWorld world, Random random, ICoords coords) {
		GeneratorResult<GeneratorData> result = new GeneratorResult<>(GeneratorData.class);
	
		// get the biome ID
		Biome biome = world.getBiome(coords.toPos());
		
		// get the template from the given archetype, type and biome
		TemplateHolder holder = TreasureTemplateRegistry.getTemplateManager().getTemplate(random, StructureArchetype.SURFACE, StructureType.MARKER, biome);
		if (holder == null) {
			return result.fail();
		}

		// TODO could move offset to TemplateGenerator : getOffset() which checks both the offsetblock and the meta
		// get the offset
		int offset = 0;
//		ICoords offsetCoords = ((GottschTemplate2)holder.getTemplate()).findCoords(random, TreasureTemplateRegistry.getMarkerBlock(StructureMarkers.OFFSET));
		ICoords offsetCoords = TreasureTemplateRegistry.getTemplateManager().getOffset(random, holder, StructureMarkers.OFFSET);
		if (offsetCoords != null) {
			offset = -offsetCoords.getY();
		}
		
		// find entrance
//		ICoords entranceCoords =((GottschTemplate2)holder. getTemplate()).findCoords(random, TreasureTemplateRegistry.getMarkerBlock(StructureMarkers.ENTRANCE));
		ICoords entranceCoords =TreasureTemplateRegistry.getTemplateManager().getOffset(random, holder, StructureMarkers.ENTRANCE);
		if (entranceCoords == null) {
			Treasure.LOGGER.debug("Unable to locate entrance position.");
			return result.fail();
		}

		// select a rotation
		Rotation rotation = Rotation.values()[random.nextInt(Rotation.values().length)];
		Treasure.LOGGER.debug("above ground rotation used -> {}", rotation);
				
		// setup placement
		PlacementSettings placement = new PlacementSettings();
		placement.setRotation(rotation).setRandom(random);
		
		// TODO move into TemplateGenerator
		// NOTE these values are still relative to origin (spawnCoords);
		ICoords newEntrance = new Coords(GottschTemplate.transformedVec3d(placement, entranceCoords.toVec3d()));
		
		/*
		 *  adjust spawn coords to line up room entrance with pit
		 */
		BlockPos transformedSize = holder.getTemplate().getSize(rotation);
		ICoords spawnCoords = ITemplateGenerator.alignEntranceToCoords(/*spawnCoords*/coords, newEntrance, transformedSize, placement);
				
		// if offset is 2 or less, then determine if the solid ground percentage is valid
		if (offset >= -2) {
			if (!WorldInfo.isSolidBase(world, spawnCoords, transformedSize.getX(), transformedSize.getZ(), 70)) {
				Treasure.LOGGER.debug("Coords -> [{}] does not meet {}% solid base requirements for size -> {} x {}", 70, spawnCoords.toShortString(), transformedSize.getX(), transformedSize.getY());
				 GeneratorResult<GeneratorData> genResult = new GravestoneMarkerGenerator().generate(world, random, coords);
				 return genResult;
			}
		}

		// generate the structure
		GeneratorResult<TemplateGeneratorData> genResult = new TemplateGenerator().generate(world, random, holder, placement, spawnCoords);
		if (!genResult.isSuccess()) return result.fail();

		// interrogate info for spawners and any other special block processing (except chests that are handler by caller
		List<BlockContext> spawnerContexts =
				(List<BlockContext>) genResult.getData().getMap().get(TreasureTemplateRegistry.getMarkerBlock(StructureMarkers.SPAWNER));
		List<BlockContext> proximityContexts =
				(List<BlockContext>) genResult.getData().getMap().get(TreasureTemplateRegistry.getMarkerBlock(StructureMarkers.PROXIMITY_SPAWNER));
		
		// TODO exact same as SubmergedRuinGenerator... need to put them in an abstract/interface common to all structure generators
		// populate vanilla spawners
		for (BlockContext c : spawnerContexts) {
			ICoords c2 = spawnCoords.add(c.getCoords());
			world.setBlock(c2.toPos(), Blocks.SPAWNER.defaultBlockState(), 3);
			MobSpawnerTileEntity te = (MobSpawnerTileEntity) world.getBlockEntity(c2.toPos());
			EntityType<?> r = DungeonHooks.getRandomDungeonMob(random);
			te.getSpawner().setEntityId(r);
		}
		
		// populate proximity spawners
		for (BlockContext c : proximityContexts) {
			ICoords c2 = spawnCoords.add(c.getCoords());
	    	world.setBlock(c2.toPos(), TreasureBlocks.PROXIMITY_SPAWNER.defaultBlockState(), 3);
	    	ProximitySpawnerTileEntity te = (ProximitySpawnerTileEntity) world.getBlockEntity(c2.toPos());
	    	EntityType<?> r = DungeonHooks.getRandomDungeonMob(random);
	    	te.setMobName(r.getRegistryName());
	    	te.setMobNum(new Quantity(1, 2));
	    	te.setProximity(10D);
//		    Treasure.LOGGER.debug("Creating proximity spawner @ {} -> [mobName={}, spawnRange={}", c.getCoords().toShortString(), r, te.getProximity());
		}		

		result.setData(genResult.getData());
		return result.success();
	}
}