/**
 * 
 */
package com.someguyssoftware.treasure2.generator.submerged;

import java.util.List;
import java.util.Random;

import com.someguyssoftware.gottschcore.measurement.Quantity;
import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.gottschcore.world.gen.structure.GottschTemplate;
import com.someguyssoftware.gottschcore.world.gen.structure.StructureMarkers;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.TreasureBlocks;
import com.someguyssoftware.treasure2.generator.GenUtil;
import com.someguyssoftware.treasure2.generator.marker.GravestoneMarkerGenerator;
import com.someguyssoftware.treasure2.generator.structure.IStructureGenerator;
import com.someguyssoftware.treasure2.generator.structure.StructureGenerator;
import com.someguyssoftware.treasure2.meta.StructureArchetype;
import com.someguyssoftware.treasure2.meta.StructureType;
import com.someguyssoftware.treasure2.tileentity.ProximitySpawnerTileEntity;
import com.someguyssoftware.treasure2.world.gen.structure.IStructureInfo;
import com.someguyssoftware.treasure2.world.gen.structure.IStructureInfoProvider;
import com.someguyssoftware.treasure2.world.gen.structure.StructureInfo;
import com.someguyssoftware.treasure2.world.gen.structure.TemplateHolder;

import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraftforge.common.DungeonHooks;

/**
 * @author Mark Gottschling on Aug 13, 2019
 *
 */
public class SubmergedStructureGenerator implements IStructureInfoProvider {
	IStructureInfo info;
	
	/**
	 * 
	 */
	public SubmergedStructureGenerator() {}
	
	/**
	 * 
	 * @param world
	 * @param random
	 * @param coords
	 * @return
	 */
	public boolean generate(World world, Random random, ICoords spawnCoords) {
		// get the biome ID
		Biome biome = world.getBiome(spawnCoords.toPos());
		
		// get the template from the given archetype, type and biome
		GottschTemplate template = getTemplate(world, random, StructureArchetype.SUBMERGED, StructureType.RUIN, biome);
		
		if (template == null) {
			Treasure.logger.debug("could not find random template");
			return false;
		}
		
		// TODO offset should be moved into StructureBuilder
		// find the offset block
		int offset = 0;
		ICoords offsetCoords = template.findCoords(random, GenUtil.getMarkerBlock(StructureMarkers.OFFSET));
		if (offsetCoords != null) {
			offset = -offsetCoords.getY();
		}
		
		// update the spawn coords with the offset
		spawnCoords = spawnCoords.add(0, offset, 0);
		
		// select a random rotation
		Rotation rotation = Rotation.values()[random.nextInt(Rotation.values().length)];
		Treasure.logger.debug("rotation used -> {}", rotation);
		
		// setup placement
		PlacementSettings placement = new PlacementSettings();
		placement.setRotation(rotation).setRandom(random);
		
		BlockPos transformedSize = template.transformedSize(rotation);
		
		for (int i = 0; i < 3; i++) {
			if (!WorldInfo.isSolidBase(world, spawnCoords, transformedSize.getX(), transformedSize.getZ(), 50)) {
				if (i == 3) {
					Treasure.logger.debug("Coords -> [{}] does not meet {}% solid base requirements for size -> {} x {}", 50, spawnCoords.toShortString(), transformedSize.getX(), transformedSize.getY());
					return false;
				}
				else {
					spawnCoords = spawnCoords.add(0, -1, 0);
				}
			}
			else {
				continue;
			}
		}
		
		// generate the structure
		IStructureGenerator generator = new StructureGenerator();
		generator.setNullBlock(Blocks.AIR);
		
		 IStructureInfo info = generator.generate(world, random, template, placement, spawnCoords);
		if (info == null) return false;
		
		Treasure.logger.debug("returned info -> {}", info);
		setInfo(info);
		
		// interrogate info for spawners and any other special block processing (except chests that are handler by caller
		List<ICoords> spawnerCoords = (List<ICoords>) info.getMap().get(GenUtil.getMarkerBlock(StructureMarkers.SPAWNER));
		List<ICoords> proximityCoords = (List<ICoords>) info.getMap().get(GenUtil.getMarkerBlock(StructureMarkers.PROXIMITY_SPAWNER));
		
		// TODO 1.6.0: move to own method
		// populate vanilla spawners
		for (ICoords c : spawnerCoords) {
			ICoords c2 = spawnCoords.add(c);
			world.setBlockState(c2.toPos(), Blocks.MOB_SPAWNER.getDefaultState());
			TileEntityMobSpawner te = (TileEntityMobSpawner) world.getTileEntity(c2.toPos());
			ResourceLocation r = DungeonHooks.getRandomDungeonMob(random);
			te.getSpawnerBaseLogic().setEntityId(r);
		}
		
		// TODO 1.6.0: move to own method
		// populate proximity spawners
		for (ICoords c : proximityCoords) {
			ICoords c2 = spawnCoords.add(c);
	    	world.setBlockState(c2.toPos(), TreasureBlocks.PROXIMITY_SPAWNER.getDefaultState());
	    	ProximitySpawnerTileEntity te = (ProximitySpawnerTileEntity) world.getTileEntity(c2.toPos());
	    	ResourceLocation r = DungeonHooks.getRandomDungeonMob(random);
	    	te.setMobName(r);
	    	te.setMobNum(new Quantity(1, 2));
	    	te.setProximity(5D);
		}
		
		return true;
	}

	/**
	 * 
	 * @param world
	 * @param random
	 * @param key
	 * @param biome
	 * @return
	 */
	public GottschTemplate getTemplate(World world, Random random, StructureArchetype archetype, StructureType type, Biome biome) {
		Template template = null;
		// get structure by archetype (subterranean) and type (room)
		String key =archetype.getName()	+ ":" + type.getName();
		
		Integer biomeID = Biome.getIdForBiome(biome);
		
		List<TemplateHolder> templateHolders = Treasure.TEMPLATE_MANAGER.getTemplatesByArchetypeTypeBiomeTable().get(key, biomeID);
		if (templateHolders == null || templateHolders.isEmpty()) {
			Treasure.logger.debug("could not find template holders for archetype:type, biome -> {} {}", key, biome.getBiomeName());
		}
		
		TemplateHolder holder = templateHolders.get(random.nextInt(templateHolders.size()));
		if (holder == null) {
			Treasure.logger.debug("could not find random template holder.");
		}
		
		template = holder.getTemplate();
		Treasure.logger.debug("selected template holder -> {} : {}", holder.getLocation(), holder.getMetaLocation());
		
		return (GottschTemplate) template;
	}
	
	/**
	 * @return the info
	 */
	@Override
	public IStructureInfo getInfo() {
		return info;
	}

	/**
	 * @param info2 the info to set
	 */
	protected void setInfo(IStructureInfo info) {
		this.info = info;
	}
}
