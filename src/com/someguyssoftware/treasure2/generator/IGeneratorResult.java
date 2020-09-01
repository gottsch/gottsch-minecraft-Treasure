/**
 * 
 */
package com.someguyssoftware.treasure2.generator;

/**
 * @author Mark Gottschling on Aug 15, 2019
 *
 */
public interface IGeneratorResult<DATA extends IGeneratorData> {

	public DATA getData();
	public void setData(DATA data);
	
	public boolean isSuccess();
	public IGeneratorResult<DATA> success();
	public IGeneratorResult<DATA> fail();
	

}
