/**
 * 
 */
package com.someguyssoftware.treasure2.world.gen.structure;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.structure.template.Template;

/**
 * @author Mark Gottschling on Aug 1, 2019
 *
 */
public class TemplateHolder {
	private Template template;
	private ResourceLocation location;
	private ResourceLocation metaLocation;
	private ResourceLocation decayRuleSetLocation;
	
	public TemplateHolder() {}

	public TemplateHolder(Template template, ResourceLocation location, ResourceLocation metaLocation, ResourceLocation ruleSetLocation) {
		setTemplate(template);
		setLocation(metaLocation);
		setMetaLocation(metaLocation);
		setDecayRuleSetLocation(ruleSetLocation);
	}
	
	public TemplateHolder(Template template, ResourceLocation location, ResourceLocation metaLocation) {
		setTemplate(template);
		setLocation(metaLocation);
		setMetaLocation(metaLocation);
	}
	
	public Template getTemplate() {
		return template;
	}

	// TODO placeholder method. In future will check if there are child templates associated.
	public boolean isComplex() {
		return false;
	}
	
	public TemplateHolder setTemplate(Template template) {
		this.template = template;
		return this;
	}

	public ResourceLocation getMetaLocation() {
		return metaLocation;
	}

	public ResourceLocation getLocation() {
		return location;
	}

	public TemplateHolder setLocation(ResourceLocation location) {
		this.location = location;
		return this;
	}

	public TemplateHolder setMetaLocation(ResourceLocation metaLocation) {
		this.metaLocation = metaLocation;
		return this;
	}

	public ResourceLocation getDecayRuleSetLocation() {
		return decayRuleSetLocation;
	}

	public TemplateHolder setDecayRuleSetLocation(ResourceLocation decayRuleSetLocation) {
		this.decayRuleSetLocation = decayRuleSetLocation;
		return this;
	}

	@Override
	public String toString() {
		return "TemplateHolder [location=" + location.toString() + ", metaLocation=" + metaLocation.toString()
				+ ", decayRuleSetLocation=" + decayRuleSetLocation.toString() + "]";
	}
}
