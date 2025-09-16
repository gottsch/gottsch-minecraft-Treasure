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

import mod.gottsch.forge.treasure2.core.structure.templatesystem.data.ChestSubprocessorData;
import mod.gottsch.forge.treasure2.core.world.feature.IFeatureType;

/**
 * @author by Mark Gottschling on 9/3/2025
 */
public abstract class ChestSubprocessor implements IChestSubprocessor {
    private IFeatureType featureType;
    private ChestSubprocessorData data;

    public ChestSubprocessor() {
    }

    public ChestSubprocessor(ChestSubprocessorData data) {
        this.data = data;
    }

    @Override
    public IFeatureType getFeatureType() {
        return featureType;
    }

    @Override
    public void setFeatureType(IFeatureType featureType) {
        this.featureType = featureType;
    }

    @Override
    public ChestSubprocessorData getData() {
        return data;
    }

    @Override
    public void setData(ChestSubprocessorData data) {
        this.data = data;
    }


}
