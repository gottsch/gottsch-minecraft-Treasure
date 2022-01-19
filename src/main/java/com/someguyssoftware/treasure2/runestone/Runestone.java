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
package com.someguyssoftware.treasure2.runestone;

import java.util.Optional;
import java.util.function.Consumer;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.charm.ICharm;
import com.someguyssoftware.treasure2.charm.TreasureCharmRegistry;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.util.ResourceLocationUtil;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

/**
 * As of today, Jan 14, this class doesn't really require a builder. However the details
 * aren't really fleshed out and this way makes it more expandable in the future.
 * @author Mark Gottschling on Jan 14, 2022
 *
 */
public abstract class Runestone implements IRunestone {
	private ResourceLocation name;
	private String type;
	private String lore;
	// the rarity of the charm
	private Rarity rarity;
	
	protected Runestone(Builder builder) {
		this.name = builder.name;
		this.type = builder.type;
		this.lore = builder.lore;
		this.rarity = builder.rarity;
	}
	
	// TODO necessary? - only if saving state values - like for undo()
	@Override
	public IRunestoneEntity createEntity() {
		IRunestoneEntity entity = new RunestoneEntity();
		entity.setRunestone(this);
		
		return entity;
	}
	
	/**
	 * Determines whether this Runestone is valid for the given ItemStack
	 */
	@Override
	abstract public boolean isValid(ItemStack itemStack);
	
	/**
	 * Applies the Runestone's ability/modification to the ItemStack
	 * @param itemStack
	 */
	@Override
	abstract public void apply(ItemStack itemStack);
	
	/**
	 * Undoes the Runestone's ability/modification from the ItemStack 
	 * @param itemStack
	 */
	abstract public void undo(ItemStack itemStack);
	
	public static Optional<IRunestone> load(NBTTagCompound tag) {
		Optional<IRunestone> runestone = Optional.empty();
		// read the name of the runestone and fetch from the registry
		try {
			String name = tag.getString("name");			
			ResourceLocation resource = ResourceLocationUtil.create(name);
			runestone = TreasureRunestones.get(resource);
			if (!runestone.isPresent()) {
				throw new Exception(String.format("Unable to locate charm %s in registry.", resource.toString()));
			}
		}
		catch(Exception e) {
			Treasure.logger.error("Unable to read state to NBT:", e);
		}	

		return runestone;
	}
	
	/**
	 * 
	 * @param tag
	 * @return
	 */
	@Override
	public NBTTagCompound save(NBTTagCompound nbt) {
		try {
			nbt.setString("name", this.name.toString());
		}
		catch(Exception e) {
			Treasure.logger.error("Unable to write state to NBT:", e);
		}
		return nbt;
	}
	
	/*
	 * 
	 */
	abstract public static class Builder {
		private final ResourceLocation name;
		private String type;
		public String lore;
		public  Rarity rarity;
		
		public Builder(ResourceLocation name) {
			this.name = name;
		}
		
		/**
		 * 
		 * @param builder
		 * @return
		 */
		public Builder with(Consumer<Builder> builder)  {
			builder.accept(this);
			return this;
		}
		
		abstract public IRunestone build();	
	}

	@Override
	public ResourceLocation getName() {
		return name;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public Rarity getRarity() {
		return rarity;
	}

	@Override
	public String getLore() {
		return lore;
	}

	@Override
	public void setLore(String lore) {
		this.lore = lore;
	}

	@Override
	public String toString() {
		return "Runestone [name=" + name + ", type=" + type + ", lore=" + lore + ", rarity=" + rarity + "]";
	}
}
