/**
 * 
 */
package com.someguyssoftware.treasure2.generator;

/**
 * @author Mark Gottschling on Aug 15, 2019
 *
 */
public interface ITreasureGeneratorResult<DATA extends ITreasureGeneratorData> {

	public DATA getData();
	public void setData(DATA data);
	
	public boolean isSuccess();
	public ITreasureGeneratorResult<DATA> success();
	public ITreasureGeneratorResult<DATA> fail();
	

}
