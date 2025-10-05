/*
 * This file is part of Treasure2.
 * Copyright (c) 2025 Mark Gottschling (gottsch)
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the Open Software Licence 3.0.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Open Software Licence 3.0 for more details.
 *
 * You should have received a copy of the Open Software Licence
 * along with Treasure2. If not, see <https://www.tldrlegal.com/license/open-software-licence-3-0>.
 */
package mod.gottsch.forge.treasure2.core.block;

import mod.gottsch.forge.treasure2.core.block.entity.AbstractTreasureChestBlockEntity;
import mod.gottsch.forge.treasure2.core.chest.ChestInventorySize;
import mod.gottsch.forge.treasure2.core.lock.LockLayout;
import net.minecraft.world.level.material.MapColor;

/**
 * @author Mark Gottschling on Jan 9, 2018
 *
 */
public class StandardChestBlock extends AbstractTreasureChestBlock {

//	public StandardChestBlock(Class<? extends AbstractTreasureChestBlockEntity> blockEntityClass, LockLayout type) {
//		this(blockEntityClass, type, Properties.of().mapColor(MapColor.WOOD));
//	}

	public StandardChestBlock(Class<? extends AbstractTreasureChestBlockEntity> blockEntityClass, LockLayout type, Properties properties) {
		super(blockEntityClass, ChestInventorySize.STANDARD.getSize(), type, properties);
	}

	public StandardChestBlock(Class<? extends AbstractTreasureChestBlockEntity> blockEntityClass, int inventorySize, LockLayout type, Properties properties) {
		super(blockEntityClass, inventorySize, type, properties);
	}

	//	private void dump(CompoundNBT tag, ICoords coords, String title) {
		//		ChestNBTPrettyPrinter printer = new ChestNBTPrettyPrinter();
		//		SimpleDateFormat formatter = new SimpleDateFormat("yyyymmdd");
		//
		//		String filename = String.format("chest-nbt-%s-%s.txt", formatter.format(new Date()),
		//				coords.toShortString().replaceAll(" ", "-"));
		//
		//		Path path = Paths.get(TreasureConfig.LOGGING.folder, "dumps").toAbsolutePath();
		//		try {
		//			Files.createDirectories(path);
		//		} catch (IOException e) {
		//			Treasure.LOGGER.error("Couldn't create directories for dump files:", e);
		//			return;
		//		}
		//		String s = printer.print(tag, Paths.get(path.toString(), filename), title);
		//		Treasure.LOGGER.debug(s);
//	}


}
