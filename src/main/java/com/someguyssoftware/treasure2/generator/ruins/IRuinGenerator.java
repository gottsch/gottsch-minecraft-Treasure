/**
 * 
 */
package com.someguyssoftware.treasure2.generator.ruins;

import java.util.List;
import java.util.Random;

import com.someguyssoftware.gottschcore.measurement.Quantity;
import com.someguyssoftware.gottschcore.spatial.ICoords;
import com.someguyssoftware.gottschcore.tileentity.ProximitySpawnerTileEntity;
import com.someguyssoftware.gottschcore.world.gen.structure.BlockContext;
import com.someguyssoftware.gottschcore.world.gen.structure.IDecayRuleSet;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.TreasureBlocks;
import com.someguyssoftware.treasure2.generator.ChestGeneratorData;
import com.someguyssoftware.treasure2.generator.GeneratorResult;
import com.someguyssoftware.treasure2.generator.IGeneratorResult;
import com.someguyssoftware.treasure2.meta.StructureArchetype;
import com.someguyssoftware.treasure2.meta.StructureType;
import com.someguyssoftware.treasure2.registry.TreasureTemplateRegistry;
import com.someguyssoftware.treasure2.world.gen.structure.TemplateHolder;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.DungeonHooks;

/**
 * @author Mark Gottschling on Aug 23, 2019
 *
 */
public interface IRuinGenerator<RESULT extends IGeneratorResult<?>> {

	GeneratorResult<ChestGeneratorData> generate(IServerWorld world, Random random, ICoords spawnCoords);	
	GeneratorResult<ChestGeneratorData> generate(IServerWorld world, Random random, ICoords originalSpawnCoords,
			IDecayRuleSet decayRuleSet);
	GeneratorResult<ChestGeneratorData> generate(IServerWorld world, Random random, ICoords originalSpawnCoords,
			TemplateHolder holder);
	GeneratorResult<ChestGeneratorData> generate(IServerWorld world, Random random, ICoords originalSpawnCoords,
			TemplateHolder holder, IDecayRuleSet decayRuleSet);

	default public TemplateHolder selectTemplate(IServerWorld world, Random random, ICoords coords, StructureArchetype archetype, StructureType type) {
		// get the biome ID
		Biome biome = world.getBiome(coords.toPos());
		TemplateHolder holder = TreasureTemplateRegistry.getTemplateManager().getTemplate(random, archetype, type, biome);
		return holder;
	}

	default public void buildOneTimeSpawners(IServerWorld world, Random random, List<BlockContext> proximityContexts, Quantity quantity, double d) {
		for (BlockContext c : proximityContexts) {
			Treasure.LOGGER.debug("placing proximity spawner at -> {}", c.getCoords().toShortString());
	    	world.setBlock(c.getCoords().toPos(), TreasureBlocks.PROXIMITY_SPAWNER.defaultBlockState(), 3);
	    	ProximitySpawnerTileEntity te = (ProximitySpawnerTileEntity) world.getBlockEntity(c.getCoords().toPos());
	    	EntityType<?> r = DungeonHooks.getRandomDungeonMob(random);
	    	Treasure.LOGGER.debug("using mob -> {} for poximity spawner.", r.toString());
	    	te.setMobName(r.getRegistryName());
	    	te.setMobNum(new Quantity(1, 2));
	    	te.setProximity(5D);
		}
	}

	default public void buildVanillaSpawners(IServerWorld world, Random random, List<BlockContext> spawnerContexts) {
		for (BlockContext c : spawnerContexts) {
			world.setBlock(c.getCoords().toPos(), Blocks.SPAWNER.defaultBlockState(), 3);
			MobSpawnerTileEntity te = (MobSpawnerTileEntity) world.getBlockEntity(c.getCoords().toPos());
			EntityType<?> r = DungeonHooks.getRandomDungeonMob(random);
			te.getSpawner().setEntityId(r);
		}
	}
}