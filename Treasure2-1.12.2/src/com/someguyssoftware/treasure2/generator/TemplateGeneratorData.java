/**
 * 
 */
package com.someguyssoftware.treasure2.generator;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.someguyssoftware.gottschcore.positional.ICoords;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.block.Block;

/**
 * @author Mark Gottschling on Aug 22, 2019
 *
 */
@Getter @Setter
public class TemplateGeneratorData extends ChestGeneratorData {
	/*
	 * size of the structure represented by ICoords
	 */
	private ICoords size;
	
	/*
	 * map by block. this method assumes that a list of block will be provided to scan for.
	 */
	private Multimap<Block, ICoords> map = ArrayListMultimap.create();
}
