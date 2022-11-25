/**
 * 
 */
package mod.gottsch.forge.treasure2.core.item;

import java.util.EnumSet;
import java.util.List;

import java.util.stream.Collectors;


/**
 * @author Mark Gottschling on Jan 15, 2018
 *
 */
public enum KeyLockCategory implements IKeyLockCategory {
	NONE,
	ELEMENTAL,
	METALS,
	GEMS,
	MAGIC,
	MOB,
	CRYPTID,
	WITHER,
	UNIQUE;

	@Override
	public String toString() {
		return this.name();
	}
	
	/**
	* 
	* @return
	*/
	public static List<String> getNames() {
		List<String> names = EnumSet.allOf(KeyLockCategory.class).stream().map(x -> x.name()).collect(Collectors.toList());
		return names;
	}
}