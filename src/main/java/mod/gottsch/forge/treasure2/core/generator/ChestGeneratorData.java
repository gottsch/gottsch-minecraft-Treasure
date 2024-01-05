/**
 * 
 */
package mod.gottsch.forge.treasure2.core.generator;


import com.someguyssoftware.gottschcore.spatial.ICoords;
import com.someguyssoftware.gottschcore.world.gen.structure.BlockContext;

import mod.gottsch.forge.treasure2.core.chest.ChestEnvironment;
import mod.gottsch.forge.treasure2.core.enums.Rarity;
import net.minecraft.block.BlockState;
import net.minecraft.util.ResourceLocation;


/**
 * 
 * @author Mark Gottschling on Feb 8, 2020
 *
 */
public class ChestGeneratorData extends GeneratorData {
	private BlockContext chestContext;
	private boolean markers;
	private boolean structure;
	private boolean pit;
	private ChestEnvironment environment;
	private Rarity rarity;
	private ResourceLocation registryName;
	
	/**
	 * 
	 */
	public ChestGeneratorData() {
		this.chestContext = new BlockContext();
	}
	
	/**
	 * 
	 * @param chestCoords
	 * @param chestState
	 */
	public ChestGeneratorData(ICoords chestCoords, BlockState chestState) {
		super();
		this.chestContext = new BlockContext(chestCoords, chestState);
	}

	public BlockContext getChestContext() {
		if (chestContext == null) {
			chestContext = new BlockContext();
		}
		return chestContext;
	}

	public void setChestContext(BlockContext chestContext) {
		this.chestContext = chestContext;
	}

	public Rarity getRarity() {
		return rarity;
	}

	public void setRarity(Rarity rarity) {
		this.rarity = rarity;
	}

	public ChestEnvironment getEnvironment() {
		return environment;
	}

	public void setEnvironment(ChestEnvironment environment) {
		this.environment = environment;
	}

	public boolean isMarkers() {
		return markers;
	}

	public void setMarkers(boolean markers) {
		this.markers = markers;
	}

	public boolean isStructure() {
		return structure;
	}

	public void setStructure(boolean structure) {
		this.structure = structure;
	}

	public boolean isPit() {
		return pit;
	}

	public void setPit(boolean pit) {
		this.pit = pit;
	}

	@Override
	public String toString() {
		return "ChestGeneratorData [chestContext=" + chestContext + ", markers=" + markers + ", structure=" + structure
				+ ", pit=" + pit + ", environment=" + environment + ", rarity=" + rarity + "]";
	}

	public ResourceLocation getRegistryName() {
		return registryName;
	}

	public void setRegistryName(ResourceLocation registryName) {
		this.registryName = registryName;
	}
}
