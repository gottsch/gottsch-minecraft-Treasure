/**
 * 
 */
package com.someguyssoftware.treasure2.generator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;

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
	
	/**
	 * 
	 * @param dataClass
	 * @return
	 */
	public DATA createData(Class<DATA> dataClass) {
		try {
			Treasure.logger.debug("creating data object");

				 // Get the class name of this instance's type.
		        ParameterizedType pt = (ParameterizedType) getClass().getGenericSuperclass();
				String parameterClassName = pt.getActualTypeArguments()[0].toString().split("\\s")[1];
				Treasure.logger.debug("parameter class name -> {}", parameterClassName);
				// Instantiate the Parameter and initialize it.
		        DATA parameter = null;
				parameter = (DATA) Class.forName(parameterClassName).newInstance();
		        return parameter;


//			Class<DATA> dd = (Class<DATA>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
//			return dd.getDeclaredConstructor().newInstance();			
//		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
//				| NoSuchMethodException | SecurityException e) {
//			Treasure.logger.warn("Unable to create GeneratorData.");
//		}
		} catch(Exception e) {	
			Treasure.logger.error("error ->", e);
			}
		return null;
	}
	
	@Override
	public DATA getData() {
		Treasure.logger.debug("in getData()");
		if (this.data == null) {
			Treasure.logger.debug("data is null");
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
