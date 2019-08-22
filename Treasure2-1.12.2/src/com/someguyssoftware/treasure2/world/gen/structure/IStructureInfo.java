/**
 * 
 */
package com.someguyssoftware.treasure2.world.gen.structure;

import com.google.common.collect.Multimap;
import com.someguyssoftware.gottschcore.positional.ICoords;

import net.minecraft.block.Block;

/**
 * @author Mark Gottschling on Jan 18, 2019
 *
 */
@Deprecated
public interface IStructureInfo {

	ICoords getCoords();
	void setCoords(ICoords coords);
	
	ICoords getSize();

	void setSize(ICoords size);

	Multimap<Block, ICoords> getMap();

	void setMap(Multimap<Block, ICoords> map);

}