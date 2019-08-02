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
	
	public TemplateHolder() {}

	public TemplateHolder(Template template, ResourceLocation location, ResourceLocation metaLocation) {
		setTemplate(template);
		setLocation(metaLocation);
		setMetaKey(metaLocation);
	}
	
	public Template getTemplate() {
		return template;
	}

	// TODO placeholder method. In future will check if there are child templates associated.
	public boolean isComplex() {
		return false;
	}
	
	public void setTemplate(Template template) {
		this.template = template;
	}

	public ResourceLocation getMetaLocation() {
		return metaLocation;
	}

	public void setMetaKey(ResourceLocation metaKey) {
		this.metaLocation = metaKey;
	}

	public ResourceLocation getLocation() {
		return location;
	}

	public void setLocation(ResourceLocation location) {
		this.location = location;
	}

	public void setMetaLocation(ResourceLocation metaLocation) {
		this.metaLocation = metaLocation;
	}
}
