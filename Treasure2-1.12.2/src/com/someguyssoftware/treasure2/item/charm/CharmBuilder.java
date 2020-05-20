/**
 * 
 */
package com.someguyssoftware.treasure2.item.charm;

/**
 * @author Mark Gottschling on Apr 26, 2020
 *
 */
public class CharmBuilder implements ICharmBuilder {
		private final String name;
		private final CharmType type;
		private final CharmLevel level;
		private double charmValueModifier = 1.0;
		private double charmPercentModifier = 1.0;
		private double charmDurationModifier = 1.0;
		
		public CharmBuilder(String name, CharmType type, CharmLevel level) {
			this.name = name;
			this.type = type;
			this.level = level;
		}
		
		@Override
		public ICharm build() {
			ICharm charm = null;
			switch(this.type) {
			case HEALING:
				charm = new HealingCharm(this);
				break;
			case SHIELDING:
				charm = new ShieldingCharm(this);
				break;
			case FULLNESS:
				charm = new FullnessCharm(this);
				break;
			case HARVESTING:
				charm = new HarvestingCharm(this);
				break;
			case DECAY:
				charm = new DecayCharm(this);
			case ILLUMINATION:
				charm = new IlluminationCharm(this);
			}

//			Treasure.logger.debug("building charm from -> {} to -> {}", this.toString(), charm.toString());
			return charm;
		}
		
		public CharmBuilder valueModifier(double value) {
			this.charmValueModifier = value;
			return this;
		}
		
		public CharmBuilder percentModifier(double percent) {
			this.charmPercentModifier = percent;
			return this;
		}
		
		public CharmBuilder durationModifier(double duration) {
			this.charmDurationModifier = duration;
			return this;
		}

		public String getName() {
			return name;
		}

		public CharmType getType() {
			return type;
		}

		public CharmLevel getLevel() {
			return level;
		}

		public double getCharmValueModifier() {
			return charmValueModifier;
		}

		public double getCharmPercentModifier() {
			return charmPercentModifier;
		}

		@Override
		public String toString() {
			return "CharmBuilder [name=" + name + ", type=" + type + ", level=" + level + ", charmValueModifier="
					+ charmValueModifier + ", charmPercentModifier=" + charmPercentModifier + ", charmDurationModifier="
					+ charmDurationModifier + "]";
		}

		public double getCharmDurationModifier() {
			return charmDurationModifier;
		}
}
