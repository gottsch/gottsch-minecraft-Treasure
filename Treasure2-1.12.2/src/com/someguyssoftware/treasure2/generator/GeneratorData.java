/**
 * 
 */
package com.someguyssoftware.treasure2.generator;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.someguyssoftware.gottschcore.positional.ICoords;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Mark Gottschling on Aug 15, 2019
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter 
public class GeneratorData implements IGeneratorData {
	private ICoords spawnCoords;

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
		
}
