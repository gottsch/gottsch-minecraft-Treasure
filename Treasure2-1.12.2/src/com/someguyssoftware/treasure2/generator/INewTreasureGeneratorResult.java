/**
 * 
 */
package com.someguyssoftware.treasure2.generator;

/**
 * @author Mark Gottschling on Aug 15, 2019
 *
 */
public interface INewTreasureGeneratorResult<DATA extends INewTreasureGeneratorData> {

	public DATA getData();
	public void setData(DATA data);
	
	public boolean isSuccess();
	public INewTreasureGeneratorResult<DATA> success();
	public INewTreasureGeneratorResult<DATA> fail();
	

}
