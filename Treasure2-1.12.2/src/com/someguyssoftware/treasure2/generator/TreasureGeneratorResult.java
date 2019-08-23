/**
 * 
 */
package com.someguyssoftware.treasure2.generator;

import java.lang.reflect.InvocationTargetException;

import com.someguyssoftware.treasure2.Treasure;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Mark Gottschling on Aug 15, 2019
 *
 */
@NoArgsConstructor 
public class TreasureGeneratorResult<DATA extends ITreasureGeneratorData> implements ITreasureGeneratorResult<DATA> {
	@Setter private DATA data;
	private boolean success;
	
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
	
	/**
	 * 
	 * @param dataClass
	 * @return
	 */
	public DATA createData(Class<DATA> dataClass) {
		try {
			return dataClass.getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			Treasure.logger.warn("Unable to create GeneratorData.");
		}
		return null;
	}
	
	@Override
	public DATA getData() {
		if (data == null) {
			Class<DATA> dataClass = null;
			DATA data = createData(dataClass);
			this.data = data;
		}
		// TODO will have to use reflection to create data if it is null
		return data;
	}
	
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
