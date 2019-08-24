/**
 * 
 */
package com.someguyssoftware.treasure2.generator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.someguyssoftware.treasure2.Treasure;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Mark Gottschling on Aug 15, 2019
 *
 */
public class GeneratorResult<DATA extends IGeneratorData> implements IGeneratorResult<DATA> {
	private boolean success;
	@Setter
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
				Treasure.logger.error(e);
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
	public String toString() {
		return "GeneratorResult [success=" + success + ", data=" + data + ", dataClass=" + dataClass + "]";
	}
}
