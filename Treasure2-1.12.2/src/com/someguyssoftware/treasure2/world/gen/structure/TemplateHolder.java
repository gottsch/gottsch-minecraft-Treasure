/**
 * 
 */
package com.someguyssoftware.treasure2.world.gen.structure;

import net.minecraft.world.gen.structure.template.Template;

/**
 * @author Mark Gottschling on Aug 1, 2019
 *
 */
public class TemplateHolder {
	private Template template;
	private String metaKey;
	
	public TemplateHolder() {}

	public TemplateHolder(Template template, String metaKey) {
		setTemplate(template);
		setMetaKey(metaKey);
	}
	
	public Template getTemplate() {
		return template;
	}

	public void setTemplate(Template template) {
		this.template = template;
	}

	public String getMetaKey() {
		return metaKey;
	}

	public void setMetaKey(String metaKey) {
		this.metaKey = metaKey;
	}
}
