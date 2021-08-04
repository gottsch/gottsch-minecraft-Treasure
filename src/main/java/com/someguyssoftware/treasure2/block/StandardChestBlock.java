/*
 * This file is part of  Treasure2.
 * Copyright (c) 2021, Mark Gottschling (gottsch)
 * 
 * All rights reserved.
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
package com.someguyssoftware.treasure2.block;

import com.someguyssoftware.treasure2.chest.TreasureChestType;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

/**
 * @author Mark Gottschling on Jan 9, 2018
 *
 */
public class StandardChestBlock extends AbstractChestBlock {

	/**
	 * 
	 * @param modID
	 * @param name
	 * @param tileEntity
	 * @param type
	 */
	public StandardChestBlock(String modID, String name, Class<? extends AbstractTreasureChestTileEntity> tileEntity,
			TreasureChestType type, Rarity rarity) {
		this(modID, name, tileEntity, type, rarity, Block.Properties.of(Material.WOOD));
	}

	/**
	 * 
	 * @param modID
	 * @param name
	 * @param material
	 * @param tileEntity
	 * @param type
	 */
	public StandardChestBlock(String modID, String name, Class<? extends AbstractTreasureChestTileEntity> tileEntity,
			TreasureChestType type, Rarity rarity, Block.Properties properties) {
		super(modID, name, tileEntity, type, rarity, properties);
	}

	/**
	 * 
	 * @param tagCompound
	 */
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
