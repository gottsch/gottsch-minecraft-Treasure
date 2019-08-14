/**
 * 
 */
package com.someguyssoftware.treasure2.generator.submerged;

import java.util.List;
import java.util.Random;

import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.gottschcore.world.gen.structure.GottschTemplate;
import com.someguyssoftware.gottschcore.world.gen.structure.IStructureInfo;
import com.someguyssoftware.gottschcore.world.gen.structure.IStructureInfoProvider;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.world.gen.structure.TreasureTemplateManager.StructureType;

import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.Template;

/**
 * @author Mark Gottschling on Aug 13, 2019
 *
 */
public class SubmergedStructureGenerator implements IStructureInfoProvider {

	@Override
	public IStructureInfo getInfo() {
		// TODO Auto-generated method stub
		return null;
	}

//	@Override
	public boolean generate(World world, Random random, ICoords coords) {

		// TODO change to templatesbyarchetype:type biome or just archetype:type
		List<Template> templates = Treasure.TEMPLATE_MANAGER.getTemplatesByType(StructureType.ABOVEGROUND);
		GottschTemplate template = (GottschTemplate) templates.get(random.nextInt(templates.size()));
		if (template == null) {
			Treasure.logger.debug("could not find random template");
			return false;
		}
		
		return true;
	}
}
