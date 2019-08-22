/**
 * 
 */
package com.someguyssoftware.treasure2.generator;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Mark Gottschling on Aug 15, 2019
 *
 */
@NoArgsConstructor 
public class TreasureGeneratorResult<DATA extends ITreasureGeneratorData> implements ITreasureGeneratorResult<DATA> {
	@Getter @Setter private /*TreasureGeneratorData*/DATA data;
	private boolean success;
	
	/**
	 * 
	 */
//	public TreasureGeneratorResult() {	}
	
	/**
	 * 
	 * @param success
	 */
	public TreasureGeneratorResult(boolean success) {
		setSuccess(success);
	}
	
	/**
	 * 
	 * @param success
	 * @param data
	 */
	public TreasureGeneratorResult(boolean success, DATA data) {
		setSuccess(success);
		setData(data);
	}
	
	private void setSuccess(boolean success) {
		this.success = success;
	}

	@Override
	public boolean isSuccess() {
		return success;
	}

//	@Override
//	public TreasureGeneratorResult success() {
//		setSuccess(true);
//		return this;
//	}

//	@Override
//	public TreasureGeneratorResult fail() {
//		setSuccess(false);
//		return this;
//	}
//
//	@Override
//	public TreasureGeneratorData getData() {
//		if (this.data == null) {
//			data = new TreasureGeneratorData();
//		}
//		return data;
//	}

//	@Override
//	public void setData(TreasureGeneratorData data) {
//		this.data = data;
//	}

//	@Override
//	public void setData(DATA data) {
//		this.data = data;	
//	}
//
//	@Override
//	public DATA getData() {
//		// TODO will have to use reflection to create data if it is null
//		return data;
//	}
	
	@Override
	public TreasureGeneratorResult<DATA> success() {
		setSuccess(true);
		return this;
	}
	
	@Override
	public TreasureGeneratorResult<DATA> fail() {
		setSuccess(false);
		return this;
	}
}
