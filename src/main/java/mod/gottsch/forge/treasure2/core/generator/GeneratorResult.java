/*
 * This file is part of  Treasure2.
 * Copyright (c) 2019 Mark Gottschling (gottsch)
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Treasure2.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */
package mod.gottsch.forge.treasure2.core.generator;

import mod.gottsch.forge.treasure2.Treasure;

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
				// TODO use new way for reflection
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