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
package mod.gottsch.forge.treasure2.core.structure.templatesystem.chest;

import mod.gottsch.forge.treasure2.core.block.AbstractTreasureChestBlock;
import mod.gottsch.forge.treasure2.core.generator.chest.ChestGenerationHelper;
import mod.gottsch.forge.treasure2.core.rarity.IRarity;
import mod.gottsch.forge.treasure2.core.structure.templatesystem.data.ChestSubprocessorData;

import java.util.List;

/**
 * this class MAY be temporary as its intent to create a bigger pool of chests
 * to select from since there currently is only two Mythical chest models
 * (and zero Legendary).
 *
 * @author by Mark Gottschling on 9/30/2025
 */
public class MythicalChestSubprocessor extends HighTierRarityChestSubprocessor {

    public MythicalChestSubprocessor() {
    }

    public MythicalChestSubprocessor(ChestSubprocessorData data) {
        super(data);
    }

    @Override
    public List<AbstractTreasureChestBlock> getValidChests(final IRarity rarity) {

        List<AbstractTreasureChestBlock> validChests = super.getValidChests(rarity);


        // add all using a lesser rarity (legendary)
        IRarity rarityMinusOne = ChestGenerationHelper.getBoostedRarity(rarity, -1).orElse(rarity);
        validChests.addAll(super.getValidChests(rarityMinusOne));


        // add all using a even lesser rarity (epic)
        IRarity rarityMinusTwo = ChestGenerationHelper.getBoostedRarity(rarity, -2).orElse(rarity);
        validChests.addAll(super.getValidChests(rarityMinusTwo));

        return validChests;
    }
}
