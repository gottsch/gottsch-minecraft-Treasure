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
package com.someguyssoftware.treasure2.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.annotation.Nullable;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;

import com.someguyssoftware.gottschcore.enums.Direction;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.capability.ICharmableCapability;
import com.someguyssoftware.treasure2.capability.InventoryType;
import com.someguyssoftware.treasure2.capability.TreasureCapabilities;
import com.someguyssoftware.treasure2.charm.Charm;
import com.someguyssoftware.treasure2.charm.ICharm;
import com.someguyssoftware.treasure2.charm.ICharmEntity;
import com.someguyssoftware.treasure2.charm.TreasureCharmRegistry;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.item.CharmItem;
import com.someguyssoftware.treasure2.item.TreasureItems;
import com.someguyssoftware.treasure2.util.ResourceLocationUtil;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * 
 * @author Mark Gottschling on Aug 20, 2021
 *
 */
public class SpawnCharmCommand extends CommandBase {
	private static final String LEVEL_ARG = "level";
	private static final String RARITY_ARG = "rarity";
	private static final String BOOK_ARG = "book";
	
	@Override
	public String getName() {
		return "t2-charm";
	}

	@Override
	public String getUsage(ICommandSender var1) {
		return "/t2-charm <charm> [-level <level> -rarity <rarity>]: spawns a Treasure! charm.";
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender commandSender, String[] args) {
		Treasure.LOGGER.debug("Starting to build Treasure! charm ...");		
		World world = commandSender.getEntityWorld();
		Random random = new Random();
		
		try {
			String[] parserArgs = (String[]) Arrays.copyOfRange(args, 1, args.length);
	
			// create the parser
			CommandLineParser parser = new DefaultParser();
			
			// create Options object
			Options options = new Options();
			options.addOption(LEVEL_ARG, true, "");
			options.addOption(RARITY_ARG, true, "");
			options.addOption(BOOK_ARG, false, "");
			// parse the command line arguments
			CommandLine line = parser.parse(options, parserArgs);
			
			String charmTypeName = args[0];
			
			int level = 1;
			if (line.hasOption(LEVEL_ARG)) {
				level = Integer.parseInt(line.getOptionValue(LEVEL_ARG));
			}

			// TODO get rarity arg
			
			// TODO get random charm level by rarity
			
			Optional<ItemStack> charmStack = TreasureItems.getCharm(charmTypeName, level, line.hasOption(BOOK_ARG) ? 1 : 0);
			if (!charmStack.isPresent()) {
				return;
			}
			
			// spawn in world
			InventoryHelper.spawnItemStack(
					world, 
					(double)commandSender.getPosition().getX() + 0.5D, 
					(double)commandSender.getPosition().getY() + 0.5D, 
					(double)commandSender.getPosition().getZ() + 0.5D,
					charmStack.get());
		}
		catch(Exception e) {
			Treasure.LOGGER.error("Error generating Treasure! well:", e);
		}
	}
	
	/**
	 * Get a list of options for when the user presses the TAB key
	 */
	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
			@Nullable BlockPos targetPos) {
		if (args.length == 1) {
			// TODO make some sort of registry list of all charm types
			List<String> charms = Arrays.asList("aegis", "decay", "decrepit", "dirt_fill", "dirt_walk", "drain", 
					"fire_immunity", "fire_resistence", "greater_healing", "healing", "illumination", "life_strike", "reflection",
					"ruin", "satiety", "shielding");
			return getListOfStringsMatchingLastWord(args, charms);
		}
		else { //if (args.length > 1) {
			if (args[args.length - 2].equals("-" + RARITY_ARG)) {
				return getListOfStringsMatchingLastWord(args, Rarity.getNames());
			}
            else if (args[args.length - 2].equals("-" + LEVEL_ARG)) {
            	List<String> levels = new ArrayList<>();
            	for (int i = 1; i <= 9; i++) {
            		levels.add(String.valueOf(i));
            	}
				return getListOfStringsMatchingLastWord(args, levels);
            }
		}
		return Collections.emptyList();
	}
}