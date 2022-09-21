
public interface IChestFeature extends ITreasureFeature {
    
	/**
	 * 
	 * @param world
	 * @param dimension
	 * @param spawnCoords
	 * @return
	 */
	 // TODO need to pass in the key (surface, submerged, wither, etc)
	default public boolean meetsProximityCriteria(ServerWorld world, ResourceLocation dimension, ICoords spawnCoords) {
		if (ITreasureFeature.isRegisteredChestWithinDistance(world, dimension, "surface", spawnCoords, 
				TreasureConfig.CHESTS.surfaceChestGen.minBlockDistance.get())) {
			Treasure.LOGGER.debug("The distance to the nearest treasure chest is less than the minimun required.");
			return false;
		}	
		return true;
	}
}