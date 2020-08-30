/**
 * 
 */
package com.someguyssoftware.treasure2.generator;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.someguyssoftware.gottschcore.spatial.ICoords;
import com.someguyssoftware.gottschcore.world.gen.structure.BlockContext;

import net.minecraft.block.Block;

/**
 * 
 * @author Mark Gottschling on Feb 7, 2020
 *
 */
public class TemplateGeneratorData extends GeneratorData {
	/*
	 * size of the structure represented by ICoords
	 */
	private ICoords size;
	
	/*
	 * map by block. this method assumes that a list of block will be provided to scan for.
	 */
	private Multimap<Block, BlockContext> map = ArrayListMultimap.create();

	public ICoords getSize() {
		return size;
	}

	public void setSize(ICoords size) {
		this.size = size;
	}

	public Multimap<Block, BlockContext> getMap() {
		return map;
	}

	public void setMap(Multimap<Block, BlockContext> map) {
		this.map = map;
	}
}
