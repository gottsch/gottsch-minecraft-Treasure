/**
 * 
 */
package com.someguyssoftware.treasure2.generator;

import com.someguyssoftware.treasure2.Treasure;

/**
 * @author Mark Gottschling on Aug 15, 2019
 *
 */
public class GeneratorResult<DATA extends IGeneratorData> implements IGeneratorResult<DATA> {
	private boolean success;
	private DATA data;
	private Class<DATA> dataClass;
	
	public GeneratorResult(Class<DATA> dataClass) {
		this.dataClass = dataClass;
	}
	
	private void setSuccess(boolean success) {
		this.success = success;
	}

	@Override
	public boolean isSuccess() {
		return success;
	}
	
	@Override
	public DATA getData() {
		if (this.data == null) {
			DATA data;
			try {
				data = dataClass.newInstance();
				this.data = data;
			} catch (InstantiationException | IllegalAccessException e) {
				Treasure.LOGGER.error(e);
			}
		}
		return data;
	}
	
	@Override
	public GeneratorResult<DATA> success() {
		setSuccess(true);
		return this;
	}
	
	@Override
	public GeneratorResult<DATA> fail() {
		setSuccess(false);
		return this;
	}

	@Override
	public void setData(DATA data) {
		this.data = data;
	}
	
	@Override
	public String toString() {
		return "GeneratorResult [success=" + success + ", data=" + data + ", dataClass=" + dataClass + "]";
	}
}