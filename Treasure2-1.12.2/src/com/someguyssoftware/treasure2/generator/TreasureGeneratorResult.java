/**
 * 
 */
package com.someguyssoftware.treasure2.generator;

/**
 * @author Mark Gottschling on Aug 15, 2019
 *
 */
public class GenericGeneratorResult<DATA extends INewTreasureGeneratorData> implements INewTreasureGeneratorResult<DATA> {
	private DATA data;
	private boolean success;
	
	public GenericGeneratorResult() {
		
	}
	
	public GenericGeneratorResult(boolean success) {
		setSuccess(success);
	}
	
	public GenericGeneratorResult(boolean success, DATA data) {
		setSuccess(success);
		setData(data);
	}
	
	private void setSuccess(boolean success) {
		this.success = success;
	}
	
	@Override
	public DATA getData() {
		return data;
	}

	@Override
	public void setData(DATA data) {
		this.data = data;
	}

	@Override
	public boolean isSuccess() {
		return success;
	}

	@Override
	public INewTreasureGeneratorResult<DATA> success() {
		setSuccess(true);
		return this;
	}

	@Override
	public INewTreasureGeneratorResult<DATA> fail() {
		setSuccess(false);
		return this;
	}
}
