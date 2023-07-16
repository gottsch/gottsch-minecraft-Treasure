/*
 * This file is part of  Dungeon Denizens.
 * Copyright (c) 2021, Mark Gottschling (gottsch)
 * 
 * All rights reserved.
 *
 * Dungeon Denizens is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Dungeon Denizens is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Dungeon Denizens.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */
package mod.gottsch.forge.treasure2.client.model.entity;

import net.minecraft.client.model.geom.ModelPart;

/**
 * 
 * @author Mark Gottschling on Apr 30, 2022
 *
 */
public interface IHumanlikeModel {
	public ModelPart getHead();
	public ModelPart getBody();
	public ModelPart getRightArm();
	public ModelPart getLeftArm();
}
