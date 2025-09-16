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
package mod.gottsch.forge.treasure2.core.registry;

import com.google.common.collect.Maps;
import mod.gottsch.forge.gottschcore.enums.IRarity;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.structure.templatesystem.chest.IChestSubprocessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Mark Gottschling on 8/22/2025
 */
@Deprecated
public class ChestSubprocessorRegistry {
	private static final Map<IRarity, IChestSubprocessor> MAP = Maps.newHashMap();

	private ChestSubprocessorRegistry() {}

	
	public static void register(IRarity rarity, IChestSubprocessor subprocessor) {
			IChestSubprocessor originalGenerator = MAP.put(rarity, subprocessor);
			if (originalGenerator != null) {
				Treasure.LOGGER.debug("replaced generator -> {} with -> {} for rarity -> {}", originalGenerator.getClass().getSimpleName(), subprocessor.getClass().getSimpleName(), rarity);
			}
	}

	public static Optional<IChestSubprocessor> get(IRarity rarity) {
		return Optional.ofNullable(MAP.get(rarity));
	}

	public static List<IChestSubprocessor> getValues() {
		return new ArrayList<>(MAP.values());
	}
}
