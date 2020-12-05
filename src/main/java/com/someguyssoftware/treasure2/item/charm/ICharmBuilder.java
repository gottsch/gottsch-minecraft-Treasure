/**
 * 
 */
package com.someguyssoftware.treasure2.item.charm;

/**
 * @author Mark Gottschling on Apr 26, 2020
 *
 */
public interface ICharmBuilder {
	public ICharm build();
	
	public CharmBuilder valueModifier(double value);
	
	public CharmBuilder percentModifier(double percent);

	public String getName();

	public CharmType getType();

	public CharmLevel getLevel();

	public double getCharmValueModifier();

	public double getCharmPercentModifier();
	
	public double getCharmDurationModifier();
}
