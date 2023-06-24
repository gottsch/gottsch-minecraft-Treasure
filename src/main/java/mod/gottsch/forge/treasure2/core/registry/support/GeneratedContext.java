/*
 * This file is part of  Treasure2.
 * Copyright (c) 2022 Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.core.registry.support;

import java.util.Optional;

import mod.gottsch.forge.gottschcore.enums.IRarity;
import mod.gottsch.forge.gottschcore.spatial.Coords;
import mod.gottsch.forge.gottschcore.spatial.ICoords;
import mod.gottsch.forge.treasure2.api.TreasureApi;
import mod.gottsch.forge.treasure2.core.enums.Rarity;
import net.minecraft.nbt.CompoundTag;

/*
 * 
 */
public class GeneratedContext {
	private ICoords coords;
	private IRarity rarity;
	
	public GeneratedContext() {}
	public GeneratedContext(IRarity rarity, ICoords coords) {
		this.rarity = rarity;
		this.coords = coords;
	}
	
	public CompoundTag save() {
		CompoundTag tag = new CompoundTag();
		if (coords != null && coords != Coords.EMPTY) {
			tag.put("coords", coords.save(new CompoundTag()));
		}
		if (rarity != null) {
			tag.putString("rarity", rarity.getValue());
		}
		return tag;
	}
	
	public void load(CompoundTag tag) {
		if (tag.contains("coords")) {
			CompoundTag coordsTag = tag.getCompound("coords");
			this.coords = Coords.EMPTY.load(coordsTag);
		}
		if (tag.contains("rarity")) {
			Optional<IRarity> rarity = TreasureApi.getRarity(tag.getString("rarity"));
			if (rarity.isPresent()) {
				this.rarity = rarity.get();
			}
			else {
				this.rarity = Rarity.NONE;
			}
		}
	}
	
	public ICoords getCoords() {
		return coords;
	}
	public void setCoords(ICoords coords) {
		this.coords = coords;
	}
	public IRarity getRarity() {
		return rarity;
	}
	public void setRarity(IRarity rarity) {
		this.rarity = rarity;
	}
	
	@Override
	public String toString() {
		return "GeneratedContext [coords=" + coords + ", rarity=" + rarity + "]";
	}
}
