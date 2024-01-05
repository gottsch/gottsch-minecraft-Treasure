/**
 * 
 */
package mod.gottsch.forge.treasure2.core.generator.ruins;

import java.util.List;
import java.util.Random;

import com.someguyssoftware.gottschcore.measurement.Quantity;
import com.someguyssoftware.gottschcore.spatial.Coords;
import com.someguyssoftware.gottschcore.spatial.ICoords;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.gottschcore.world.gen.structure.BlockContext;
import com.someguyssoftware.gottschcore.world.gen.structure.GottschTemplate;
import com.someguyssoftware.gottschcore.world.gen.structure.IDecayProcessor;
import com.someguyssoftware.gottschcore.world.gen.structure.IDecayRuleSet;
import com.someguyssoftware.gottschcore.world.gen.structure.PlacementSettings;
import com.someguyssoftware.gottschcore.world.gen.structure.StructureMarkers;

import mod.gottsch.forge.treasure2.core.Treasure;
import mod.gottsch.forge.treasure2.core.generator.ChestGeneratorData;
import mod.gottsch.forge.treasure2.core.generator.GenUtil;
import mod.gottsch.forge.treasure2.core.generator.GeneratorResult;
import mod.gottsch.forge.treasure2.core.generator.TemplateGeneratorData;
import mod.gottsch.forge.treasure2.core.meta.StructureArchetype;
import mod.gottsch.forge.treasure2.core.meta.StructureMeta;
import mod.gottsch.forge.treasure2.core.meta.StructureType;
import mod.gottsch.forge.treasure2.core.registry.TreasureDecayRegistry;
import mod.gottsch.forge.treasure2.core.registry.TreasureMetaRegistry;
import mod.gottsch.forge.treasure2.core.registry.TreasureTemplateRegistry;
import mod.gottsch.forge.treasure2.core.world.gen.structure.TemplateGenerator;
import mod.gottsch.forge.treasure2.core.world.gen.structure.TemplateHolder;
import net.minecraft.block.material.Material;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.ChunkGenerator;

/**
 * 
 * @author Mark Gottschling on Dec 13, 2019
 *
 */
public class SurfaceRuinGenerator implements IRuinGenerator<GeneratorResult<ChestGeneratorData>> {
	
	private static final double REQUIRED_BASE_SIZE = 75;
	private static final double REQUIRED_AIR_SIZE = 50;

	/**
	 * 
	 */
	public SurfaceRuinGenerator() {}
	
	@Override
	public GeneratorResult<ChestGeneratorData> generate(IServerWorld world, ChunkGenerator generator, Random random,
			ICoords originalSpawnCoords) {
		return generate(world, generator, random, originalSpawnCoords, null, null);
	}
	
	@Override
	public GeneratorResult<ChestGeneratorData> generate(IServerWorld world, ChunkGenerator generator, Random random,
			ICoords originalSpawnCoords, IDecayRuleSet decayRuleSet) {
		return generate(world, generator, random, originalSpawnCoords, null, decayRuleSet);
	}
	
	@Override
	public GeneratorResult<ChestGeneratorData> generate(IServerWorld world, ChunkGenerator generator, Random random,
			ICoords originalSpawnCoords, TemplateHolder holder) {
		return generate(world, generator, random, originalSpawnCoords, holder, null);
	}

	@Override
	public GeneratorResult<ChestGeneratorData> generate(IServerWorld world, ChunkGenerator chunkGenerator, Random random,
			ICoords originalSpawnCoords, TemplateHolder holder, IDecayRuleSet decayRuleSet) {		
		GeneratorResult<ChestGeneratorData> result = new GeneratorResult<>(ChestGeneratorData.class);

		/*
		 * Setup
		 */
		// create the generator
		TemplateGenerator generator = new TemplateGenerator();
		
		// get the template holder from the given archetype, type and biome
		if (holder == null) {
			holder = selectTemplate(world, random, originalSpawnCoords, StructureArchetype.SURFACE, StructureType.RUIN);
		}
		if (holder == null) return result.fail();		

		GottschTemplate template = (GottschTemplate) holder.getTemplate();
		Treasure.LOGGER.debug("selected template holder -> {} : {}", holder.getLocation(), holder.getMetaLocation());
		if (template == null) {
			Treasure.LOGGER.debug("could not find random template");
			return result.fail();
		}
		
		// find the 'entrance' block
		ICoords entranceCoords = TreasureTemplateRegistry.getManager().getOffset(random, holder, StructureMarkers.ENTRANCE);
		if (entranceCoords == null) {
			Treasure.LOGGER.debug("Unable to locate entrance position.");
			return result.fail();
		}
		Treasure.LOGGER.debug("entrance coords -> {}", entranceCoords.toShortString());
		
		// select a random rotation
		Rotation rotation = Rotation.values()[random.nextInt(Rotation.values().length)];
		Treasure.LOGGER.debug("rotation used -> {}", rotation);
		
		// setup placement
		PlacementSettings placement = new PlacementSettings();
		placement.setRotation(rotation).setRandom(random);
	
		// NOTE these values are still relative to origin (spawnCoords);
		ICoords newEntrance = new Coords(GottschTemplate.transformedVec3d(placement, entranceCoords.toVec3d()));
		Treasure.LOGGER.debug("new entrance coords -> {}", newEntrance.toShortString());
	
		// determine the actual spawn coords
		ICoords templateSize = new Coords(holder.getTemplate().getSize(placement.getRotation()));

		Treasure.LOGGER.debug("original coords -> {}",originalSpawnCoords.toShortString());
		
		/*
		 * we want to align the new entrance (rotated structure) to the center of the chunk ie. original spawn
		 */
		BlockPos transformedSize = template.getSize(rotation);
		ICoords alignedSpawnCoords = align(originalSpawnCoords, newEntrance, transformedSize, placement);
		Treasure.LOGGER.debug("aligned spawn coords -> {}", alignedSpawnCoords.toShortString());

		
		// NOTE these checks don't really belong in a generator as their task is to just generate.
		// however, the template is unknown outside this call and thus the rotate, placement, size and actual coords would be unknown.
		/**
		 * Environment Checks
		 */
		if (chunkGenerator == null) {
			alignedSpawnCoords = WorldInfo.getDryLandSurfaceCoords(world, alignedSpawnCoords);
		}
		else {
			alignedSpawnCoords = WorldInfo.getDryLandSurfaceCoords(world, chunkGenerator, alignedSpawnCoords);
		}
		Treasure.LOGGER.debug("surface coords -> {}", alignedSpawnCoords.toShortString());
		if (alignedSpawnCoords == WorldInfo.EMPTY_COORDS) {
			return result.fail();
		}
		
		// check if it has % land base
		for (int i = 0; i < 3; i++) {
			Treasure.LOGGER.debug("finding solid base index -> {} at coords -> {}", i, alignedSpawnCoords.toShortString());
			if (!WorldInfo.isSolidBase(world, alignedSpawnCoords, templateSize.getX(), templateSize.getZ(), REQUIRED_BASE_SIZE)) {
				if (i == 2) {
					Treasure.LOGGER.debug("Coords -> [{}] does not meet {}% solid base requirements for size -> {} x {}", alignedSpawnCoords.toShortString(), REQUIRED_BASE_SIZE, templateSize.getX(), templateSize.getZ());
					return result.fail();
				}
				else {
					alignedSpawnCoords = alignedSpawnCoords.add(0, -1, 0);
					Treasure.LOGGER.debug("move aligned spawn coords down for solid base check -> {}", alignedSpawnCoords.toShortString());
				}
			}
			else {
				break;
			}
		}
		Treasure.LOGGER.debug("using solid base coords -> {}", alignedSpawnCoords.toShortString());
		
		// check if the plane above the actual spawn coords is % air
		Treasure.LOGGER.debug("checking for {} % air at coords -> {} for dimensions -> {} x {}", REQUIRED_AIR_SIZE, alignedSpawnCoords.add(0, 1, 0), templateSize.getX(), templateSize.getZ());
		if (!WorldInfo.isAirBase(world, alignedSpawnCoords.add(0, 1, 0), templateSize.getX(), templateSize.getZ(), REQUIRED_AIR_SIZE)) {
			Treasure.LOGGER.debug("Coords -> [{}] does not meet {} % air base requirements for size -> {} x {}", alignedSpawnCoords.toShortString(), REQUIRED_AIR_SIZE, templateSize.getX(), templateSize.getZ());
			return result.fail();
		}
		
		/**
		 * Build
		 */
		
		// update original spawn coords' y-value to be that of aligned spawn coords.
		// this is the coords that need to be supplied to the template generator to allow
		// the structure to generator in the correct place
		originalSpawnCoords = new Coords(originalSpawnCoords.getX(), alignedSpawnCoords.getY(), originalSpawnCoords.getZ());
		Treasure.LOGGER.debug("using spawn coords to generate -> {}", originalSpawnCoords);
		
		// NOTE don't like this here and then AGAIN in TemplateGenerator
		// get the rule set from the meta which is in the holder
		StructureMeta meta = (StructureMeta) TreasureMetaRegistry.get(holder.getMetaLocation().toString());
		if (meta == null) {
			Treasure.LOGGER.debug("Unable to locate meta data for template -> {}", holder.getLocation());
			return result.fail();
		}
		
		// setup the decay ruleset and processor
		IDecayProcessor decayProcessor = null;
//		Treasure.LOGGER.debug("decay rule set -> {}", decayRuleSet);
//		Treasure.LOGGER.debug("decay rule set location -> {}", holder.getDecayRuleSetLocation());
//		if (decayRuleSet == null && holder.getDecayRuleSetLocation() != null && holder.getDecayRuleSetLocation().size() > 0) {
//			Treasure.LOGGER.debug("TreasureDecayManager.map -> {}", TreasureDecayRegistry.getDecayManager().getRuleSetMap());
//			// create a decay processor
//			// TODO TemplateHolder contains ResourceLocations to the decay rule sets, however the DecayManager is a map keyed by the name only.
//			// maybe add a convenience method in holder to return just the name ie minus the extension.
//			decayRuleSet = TreasureDecayRegistry.getDecayManager().getRuleSetMap().get(holder.getDecayRuleSetLocation().get(random.nextInt(holder.getDecayRuleSetLocation().size())).toString().replace(".json", ""));
//			
////			List<String> keys = TreasureDecayRegistry.getDecayManager().getRuleSetMap().keySet().stream().collect(Collectors.toList());
////			Treasure.LOGGER.debug("ruleset keys -> {}", keys);
////			decayRuleSet = TreasureDecayRegistry.get(keys.get(random.nextInt(keys.size())));
//			if (Treasure.LOGGER.isDebugEnabled() && decayRuleSet != null) {
//				Treasure.LOGGER.debug("randomly selected decayRuleSet -> {}", decayRuleSet.getName());
//			}
//		}
//		if (decayRuleSet != null) {
//			// TEMP 7/18/21 - remove the decay processor until it is working
////			decayProcessor = new DecayProcessor(Treasure.instance.getInstance(), decayRuleSet);
//		}
//		Treasure.LOGGER.debug("using decay rule set -> {}", decayRuleSet);
//		Treasure.LOGGER.debug("decay processor -> {}", decayProcessor);
		
		GeneratorResult<TemplateGeneratorData> genResult = generator.generate(world, random, decayProcessor, holder, placement, originalSpawnCoords);
		 if (!genResult.isSuccess()) return result.fail();

		Treasure.LOGGER.debug("surface gen result -> {}", genResult);

		// interrogate info for spawners and any other special block processing (except chests that are handler by caller
		List<BlockContext> bossChestContexts =
					(List<BlockContext>) genResult.getData().getMap().get(GenUtil.getMarkerBlock(StructureMarkers.BOSS_CHEST));
		List<BlockContext> chestContexts =
				(List<BlockContext>) genResult.getData().getMap().get(GenUtil.getMarkerBlock(StructureMarkers.CHEST));
		List<BlockContext> spawnerContexts =
				(List<BlockContext>) genResult.getData().getMap().get(GenUtil.getMarkerBlock(StructureMarkers.SPAWNER));
		List<BlockContext> proximityContexts =
				(List<BlockContext>) genResult.getData().getMap().get(GenUtil.getMarkerBlock(StructureMarkers.PROXIMITY_SPAWNER));

		/*
		 *  NOTE currently only 1 chest is allowed per structure - the rest are ignored.
		 */
		// check if there is a boss chest(s)
		// TODO turn these checks into methods --> getChestContext();
		BlockContext chestContext = null;
		if (bossChestContexts != null && bossChestContexts.size() > 0) {
			if (bossChestContexts.size() > 1) {
				chestContext = bossChestContexts.get(random.nextInt(bossChestContexts.size()));
			}
			else {
				chestContext = bossChestContexts.get(0);
			}			
		}		

		// if a boss chest wasn't found, search for regular chests
		if (chestContext == null) {
			if (chestContexts != null && chestContexts.size() > 0) {
				if (chestContexts.size() > 1) {
					chestContext = chestContexts.get(random.nextInt(chestContexts.size()));
				}
				else {
					chestContext = chestContexts.get(0);
				}			
			}			
		}

		ICoords chestCoords = null;
		if (chestContext != null) {
			// move the chest coords to the first solid block beneath it.
			chestCoords = getSolidSurfaceCoords(world, chestContext.getCoords());
			if (chestCoords == WorldInfo.EMPTY_COORDS) chestCoords = null;
			chestContext.setCoords(chestCoords);
		}
		if (chestCoords == null) {
			return result.fail();
		}
		
		if (proximityContexts != null)
			Treasure.LOGGER.debug("Proximity spawners size -> {}", proximityContexts.size());
		else
			Treasure.LOGGER.debug("No proximity spawners found.");
		
		// populate vanilla spawners
		buildVanillaSpawners(world, random, spawnerContexts);
		
		// populate proximity spawners
		buildOneTimeSpawners(world, random, proximityContexts, new Quantity(1,2), 5D);
		
		// copy all data from genResult
		result.getData().setSpawnCoords(genResult.getData().getSpawnCoords());
		
		// update with chest context
		result.getData().setChestContext(chestContext);
		
		return result.success();
	}
	
	/**
	 * NOTE candidate for GottschCore
	 * @param world
	 * @param coords
	 * @return
	 */
	public static ICoords getSolidSurfaceCoords(final IServerWorld world, final ICoords coords) {
		boolean isSurfaceBlock = false;
		ICoords newCoords = coords;
		
		while (!isSurfaceBlock) {
			// get the cube that is 1 below current position
			com.someguyssoftware.gottschcore.block.BlockContext blockContext = new com.someguyssoftware.gottschcore.block.BlockContext(world, newCoords.down(1));
//			Treasure.LOGGER.debug("below block -> {} @ {}", cube.getState().getBlock().getRegistryName(), cube.getCoords().toShortString());
			// exit if not valid Y coordinate
			if (!WorldInfo.isValidY(blockContext.getCoords())) {
				return WorldInfo.EMPTY_COORDS;
			}	

			if (blockContext.equalsMaterial(Material.AIR) || blockContext.isReplaceable()
					|| blockContext.equalsMaterial(Material.LEAVES) || blockContext.isLiquid()
					|| blockContext.isBurning()) {
//				Treasure.LOGGER.debug("block is air, leaves, replacable, liquid or burning");
				newCoords = newCoords.down(1);
			}
			else {
				isSurfaceBlock = true;
			}		
		}
		return newCoords;
	}
	
	/**
	 * TODO move to abstract or interface
	 * @param spawnCoords
	 * @param newEntrance
	 * @param transformedSize
	 * @param placement
	 * @return
	 */
	private ICoords align(ICoords spawnCoords, ICoords newEntrance, BlockPos transformedSize, PlacementSettings placement) {
		ICoords startCoords = null;
		// NOTE work with rotations only for now
		
		// first offset spawnCoords by newEntrance
		startCoords = spawnCoords.add(-newEntrance.getX(), 0, -newEntrance.getZ());
		
		return startCoords;
	}
}