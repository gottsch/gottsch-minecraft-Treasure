/**
 * 
 */
package com.someguyssoftware.treasure2.generator.ruins;

import java.util.List;
import java.util.Random;

import com.someguyssoftware.gottschcore.measurement.Quantity;
import com.someguyssoftware.gottschcore.positional.ICoords;
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
import com.someguyssoftware.treasure2.tileentity.ProximitySpawnerTileEntity;
import com.someguyssoftware.treasure2.world.gen.structure.TemplateHolder;

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

	GeneratorResult<ChestGeneratorData> generate(World world, Random random, ICoords spawnCoords);	
	GeneratorResult<ChestGeneratorData> generate(World world, Random random, ICoords originalSpawnCoords,
			IDecayRuleSet decayRuleSet);
	GeneratorResult<ChestGeneratorData> generate(World world, Random random, ICoords originalSpawnCoords,
			TemplateHolder holder);
	GeneratorResult<ChestGeneratorData> generate(World world, Random random, ICoords originalSpawnCoords,
			TemplateHolder holder, IDecayRuleSet decayRuleSet);

	default public TemplateHolder selectTemplate(World world, Random random, ICoords coords, StructureArchetype archetype, StructureType type) {
		// get the biome ID
		Biome biome = world.getBiome(coords.toPos());
		TemplateHolder holder = TreasureTemplateRegistry.getManager().getTemplate(world, random, archetype, type, biome);
		return holder;
	}

	default public void buildOneTimeSpawners(World world, Random random, List<BlockContext> proximityContexts, Quantity quantity, double d) {
		for (BlockContext c : proximityContexts) {
			Treasure.logger.debug("placing proximity spawner at -> {}", c.getCoords().toShortString());
	    	world.setBlockState(c.getCoords().toPos(), TreasureBlocks.PROXIMITY_SPAWNER.getDefaultState());
	    	ProximitySpawnerTileEntity te = (ProximitySpawnerTileEntity) world.getTileEntity(c.getCoords().toPos());
	    	ResourceLocation r = DungeonHooks.getRandomDungeonMob(random);
	    	Treasure.logger.debug("using mob -> {} for poximity spawner.", r.toString());
	    	te.setMobName(r);
	    	te.setMobNum(new Quantity(1, 2));
	    	te.setProximity(5D);
		}
	}

	default public void buildVanillaSpawners(World world, Random random, List<BlockContext> spawnerContexts) {
		for (BlockContext c : spawnerContexts) {
			world.setBlockState(c.getCoords().toPos(), Blocks.MOB_SPAWNER.getDefaultState());
			TileEntityMobSpawner te = (TileEntityMobSpawner) world.getTileEntity(c.getCoords().toPos());
			ResourceLocation r = DungeonHooks.getRandomDungeonMob(random);
			te.getSpawnerBaseLogic().setEntityId(r);
		}
	}
}
