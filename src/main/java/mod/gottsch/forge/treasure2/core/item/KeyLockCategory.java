/**
 * 
 */
package mod.gottsch.forge.treasure2.core.item;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import mod.gottsch.forge.gottschcore.enums.IEnum;


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
	public String getName() {
		return name();
	}
	
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

	@Override
	public Integer getCode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Integer, IEnum> getCodes() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, IEnum> getValues() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCode(Integer arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValue(String arg0) {
		// TODO Auto-generated method stub
		
	}
}