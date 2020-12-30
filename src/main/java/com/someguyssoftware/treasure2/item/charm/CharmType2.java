/**
 * 
 */
package com.someguyssoftware.treasure2.item.charm;

import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 
 * @author Mark Gottschling on Dec 29, 2020
 *
 */
public enum CharmType2 {
	CHARM,
	CURSE;
	
	public static List<String> getNames() {
		List<String> names = EnumSet.allOf(CharmType2.class).stream().map(x -> x.name()).collect(Collectors.toList());
		return names;
	}
}
