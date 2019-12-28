/**
 * 
 */
package com.someguyssoftware.treasure2.generator.ruins;

import java.util.List;
import java.util.Random;

import com.someguyssoftware.gottschcore.measurement.Quantity;
import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.gottschcore.world.gen.structure.IDecayProcessor;
import com.someguyssoftware.gottschcore.world.gen.structure.IDecayRuleSet;
import com.someguyssoftware.treasure2.generator.IGeneratorResult;
import com.someguyssoftware.treasure2.generator.TemplateGeneratorData;
import com.someguyssoftware.treasure2.meta.StructureArchetype;
import com.someguyssoftware.treasure2.meta.StructureType;
import com.someguyssoftware.treasure2.tileentity.ProximitySpawnerTileEntity;
import com.someguyssoftware.treasure2.world.gen.structure.TemplateHolder;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.TreasureBlocks;
import com.someguyssoftware.treasure2.generator.GeneratorResult;

import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.DungeonHooks;

/**
 * @author Mark Gottschling on Aug 23, 2019
 *
 */
public interface IRuinGenerator<RESULT extends IGeneratorResult<?>> {

	GeneratorResult<TemplateGeneratorData> generate(World world, Random random, ICoords spawnCoords);
	GeneratorResult<TemplateGeneratorData> generate(World world, Random random, ICoords originalSpawnCoords,
			IDecayRuleSet decayRuleSet);
	
//	void buildOneTimeSpawners(World world, Random random, ICoords spawnCoords, List<ICoords> proximityCoords,
//			Quantity quantity, double d);
//
//	void buildVanillaSpawners(World world, Random random, ICoords spawnCoords, List<ICoords> spawnerCoords);

	default public TemplateHolder selectTemplate(World world, Random random, ICoords coords, StructureArchetype archetype, StructureType type) {
		// get the biome ID
		Biome biome = world.getBiome(coords.toPos());
		TemplateHolder holder = Treasure.TEMPLATE_MANAGER.getTemplate(world, random, archetype, type, biome);
		return holder;
	}
	
	default public void buildOneTimeSpawners(World world, Random random, ICoords spawnCoords, List<ICoords> proximityCoords, Quantity quantity, double d) {
		for (ICoords c : proximityCoords) {
			ICoords c2 = spawnCoords.add(c);
	    	world.setBlockState(c2.toPos(), TreasureBlocks.PROXIMITY_SPAWNER.getDefaultState());
	    	ProximitySpawnerTileEntity te = (ProximitySpawnerTileEntity) world.getTileEntity(c2.toPos());
	    	ResourceLocation r = DungeonHooks.getRandomDungeonMob(random);
	    	te.setMobName(r);
	    	te.setMobNum(new Quantity(1, 2));
	    	te.setProximity(5D);
		}
	}

	default public void buildVanillaSpawners(World world, Random random, ICoords spawnCoords, List<ICoords> spawnerCoords) {
		for (ICoords c : spawnerCoords) {
			ICoords c2 = spawnCoords.add(c);
			world.setBlockState(c2.toPos(), Blocks.MOB_SPAWNER.getDefaultState());
			TileEntityMobSpawner te = (TileEntityMobSpawner) world.getTileEntity(c2.toPos());
			ResourceLocation r = DungeonHooks.getRandomDungeonMob(random);
			te.getSpawnerBaseLogic().setEntityId(r);
		}
	}
}
