/*
 * This file is part of  Treasure2.
 * Copyright (c) 2019 Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.core.structure;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

/**
 * @author Mark Gottschling on Aug 1, 2019
 *
 */
public class TemplateHolder {
	private StructureTemplate template;
	private ResourceLocation location;
	@Deprecated
	private List<ResourceLocation> decayRuleSetLocation;
	
	private List<String> tags;
	
	public TemplateHolder() {}

	public TemplateHolder(StructureTemplate template, ResourceLocation location, ResourceLocation metaLocation, List<ResourceLocation> ruleSetLocation) {
		setTemplate(template);
		setLocation(metaLocation);
		setDecayRuleSetLocation(ruleSetLocation);
	}
	
	public TemplateHolder(StructureTemplate template, ResourceLocation location, ResourceLocation metaLocation) {
		setTemplate(template);
		setLocation(metaLocation);
	}
	
	public StructureTemplate getTemplate() {
		return template;
	}

	/*
	 * placeholder method. In future will check if there are child templates associated.
	 */
	public boolean isComplex() {
		return false;
	}
	
	public TemplateHolder setTemplate(StructureTemplate template) {
		this.template = template;
		return this;
	}

	public ResourceLocation getLocation() {
		return location;
	}

	public TemplateHolder setLocation(ResourceLocation location) {
		this.location = location;
		return this;
	}

	public List<ResourceLocation> getDecayRuleSetLocation() {
		if (decayRuleSetLocation == null) {
			decayRuleSetLocation = new ArrayList<>();
		}
		return decayRuleSetLocation;
	}

	public TemplateHolder setDecayRuleSetLocation(List<ResourceLocation> decayRuleSetLocation) {
		this.decayRuleSetLocation = decayRuleSetLocation;
		return this;
	}

	@Override
	public String toString() {
		return "TemplateHolder [location=" + location.toString() + "]";
	}

	public List<String> getTags() {
		if (tags == null) {
			tags = new ArrayList<>();
		}
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}
}