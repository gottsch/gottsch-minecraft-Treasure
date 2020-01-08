/**
 * 
 */
package com.someguyssoftware.treasure2.generator;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.someguyssoftware.gottschcore.positional.ICoords;

import net.minecraft.block.Block;

/**
 * @author Mark Gottschling on Aug 22, 2019
 *
 */
public class TemplateGeneratorData extends ChestGeneratorData {
	/*
	 * size of the structure represented by ICoords
	 */
	private ICoords size;
	
	/*
	 * map by block. this method assumes that a list of block will be provided to scan for.
	 */
	private Multimap<Block, ICoords> map = ArrayListMultimap.create();

	public ICoords getSize() {
		return size;
	}

	public void setSize(ICoords size) {
		this.size = size;
	}

	public Multimap<Block, ICoords> getMap() {
		return map;
	}

	public void setMap(Multimap<Block, ICoords> map) {
		this.map = map;
	}
}
