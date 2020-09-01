/**
 * 
 */
package com.someguyssoftware.treasure2.enums;

import java.util.Map;

/**
 * @author Mark Gottschling on Aug 31, 2014
 *
 */
public interface IEnum {

	public Integer getCode();
	public void setCode(Integer code);
	public String getValue();
	public void setValue(String value);
	
	public Map<Integer, IEnum> getCodes();
	public Map<String, IEnum> getValues();
	
	public String getName();
}
