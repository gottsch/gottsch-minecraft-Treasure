/*
 * This file is part of  Treasure2.
 * Copyright (c) 2021 Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import mod.gottsch.forge.gottschcore.spatial.Coords;
import mod.gottsch.forge.gottschcore.spatial.ICoords;
import mod.gottsch.forge.gottschcore.world.WorldInfo;
import mod.gottsch.forge.treasure2.Treasure;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * @author Mark Gottschling on Jul 22, 2021
 *
 */
public class ModUtil {
	/*
	MC 1.18.2: net/minecraft/server/MinecraftServer.storageSource
	Name: l => f_129744_ => storageSource
	Side: BOTH
	AT: public net.minecraft.server.MinecraftServer f_129744_ # storageSource
	Type: net/minecraft/world/level/storage/LevelStorageSource$LevelStorageAccess
	 */
	private static final String SAVE_FORMAT_LEVEL_SAVE_SRG_NAME = "f_129744_";
	
	/*
	MC 1.18.2: net/minecraft/world/item/Item.maxStackSize
	Name: d => f_41370_ => maxStackSize
	Side: BOTH
	AT: public net.minecraft.world.item.Item f_41370_ # maxStackSize
	Type: int
	*/
	public static final String MAX_STACK_SIZSE_SRG_NAME = "f_41370_";
	
	/*
	 MC 1.19.2: net/minecraft/world/item/Item.maxDamage
	Name: e => f_41371_ => maxDamage
	Side: BOTH
	AT: public net.minecraft.world.item.Item f_41371_ # maxDamage
	Type: int
	 */
	public static final String MAX_DAMAGE_SRG_NAME = "f_41371_";
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public static ResourceLocation asLocation(String name) {
		return hasDomain(name) ? new ResourceLocation(name) : new ResourceLocation(Treasure.MODID, name);
	}
	
	public static ResourceLocation getName(Block block) {
		// don't bother checking optional - if it is empty, then the block isn't registered and this shouldn't run anyway.
		ResourceLocation name = ForgeRegistries.BLOCKS.getResourceKey(block).get().location();
		return name;
	}

	public static ResourceLocation getName(Item item) {
		// don't bother checking optional - if it is empty, then the block isn't registered and this shouldn't run anyway.
		ResourceLocation name = ForgeRegistries.ITEMS.getResourceKey(item).get().location();
		return name;
	}
	

	public static ResourceLocation getName(Holder<Biome> biome) {
		return biome.unwrapKey().get().location();	
	}
	
	public static boolean hasDomain(String name) {
		return name.indexOf(":") >= 0;
	}

	public static void setItemMaxStackSize(Item item, int size) {
		ObfuscationReflectionHelper.setPrivateValue(Item.class, item, size, MAX_STACK_SIZSE_SRG_NAME);
	}
	
	public static void setItemDurability(Item item, int durability) {
		try {
			ObfuscationReflectionHelper.setPrivateValue(Item.class, item, durability, MAX_DAMAGE_SRG_NAME);
			setItemMaxStackSize(item, 1);
		} catch(Exception e) {
			Treasure.LOGGER.error("error setting item durability ->", e);
		}
	}
	
	/**
	 * 
	 * @param level
	 * @return
	 */
	public static Optional<Path> getWorldSaveFolder(ServerLevel level) {
		Object save = ObfuscationReflectionHelper.getPrivateValue(MinecraftServer.class, level.getServer(), SAVE_FORMAT_LEVEL_SAVE_SRG_NAME);
		if (save instanceof LevelStorageSource.LevelStorageAccess) {
			Path path = ((LevelStorageSource.LevelStorageAccess) save)
					.getWorldDir().resolve(((LevelStorageSource.LevelStorageAccess) save).getLevelId())
					.resolve("datapacks");
			return Optional.of(path);
		}
		return Optional.empty();
	}
	
	/**
	 *  Get all paths from a folder that inside the JAR file
	 * @param jarPath
	 * @param folder
	 * @return
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public static  List<Path> getPathsFromResourceJAR(Path jarPath, String folder)
			throws URISyntaxException, IOException {

		List<Path> result;

		/*
		 * This block of code would be used if the jar file was unknown and it had to be discovered.
		 * 
        // get path of the current running JAR
        String jarPathOriginal = Treasure.class.getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .toURI()
                .getPath();
        Treasure.LOGGER.debug("JAR Path Original -> {}", jarPathOriginal);

        // file walks JAR
        URI uri = URI.create("jar:file:" + jarPath);
		 */

		try (FileSystem fs = FileSystems.newFileSystem(jarPath, Collections.emptyMap())) {
			result = Files.walk(fs.getPath(folder))
					.filter(Files::isRegularFile)
					.collect(Collectors.toList());
		}
		return result;
	}

	/**
	 * 
	 * @param fileName
	 * @return
	 */
	public static InputStream getFileFromResourceAsStream(String fileName) {

		// The class loader that loaded the class
		ClassLoader classLoader = Treasure.class.getClassLoader();
		InputStream inputStream = classLoader.getResourceAsStream(fileName);

		// the stream holding the file content
		if (inputStream == null) {
			throw new IllegalArgumentException("file not found! " + fileName);
		} else {
			return inputStream;
		}
	}

	/**
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static List<Path> getPathsFromFlatDatapacks(Path path) throws IOException {
		List<Path> result;
		try (Stream<Path> walk = Files.walk(path)) {
			result = walk.filter(Files::isRegularFile)
					.collect(Collectors.toList());
		}
		return result;
	}

	/**
	 * 
	 * @author Mark Gottschling on Jul 25, 2021
	 *
	 */
	public static class SpawnEntityHelper {

		/**
		 * 
		 * @param level
		 * @param random
		 * @param entityType
		 * @param mob
		 * @param coords
		 * @return
		 */
		public static Entity spawn(ServerLevel level, RandomSource random, EntityType<?> entityType, Entity mob, ICoords coords) {

			for (int i = 0; i < 20; i++) { // 20 tries
				int spawnX = coords.getX() + Mth.nextInt(random, 1, 2) * Mth.nextInt(random, -1, 1);
				int spawnY = coords.getY() + Mth.nextInt(random, 1, 2) * Mth.nextInt(random, -1, 1);
				int spawnZ = coords.getZ() + Mth.nextInt(random, 1, 2) * Mth.nextInt(random, -1, 1);
				ICoords spawnCoords = new Coords(spawnX, spawnY, spawnZ);

				boolean isSpawned = false;
				if (!WorldInfo.isClientSide(level)) {
					SpawnPlacements.Type placement = SpawnPlacements.getPlacementType(entityType);
					if (NaturalSpawner.isSpawnPositionOk(placement, level, spawnCoords.toPos(), entityType)) {
						//						mob = entityType.create(level);
						mob.setPos((double)spawnX, (double)spawnY, (double)spawnZ);
						level.addFreshEntityWithPassengers(mob);
						isSpawned = true;
					}
					if (isSpawned) {
						break;
					}
				}
			}
			return mob;
		}
	}
}
