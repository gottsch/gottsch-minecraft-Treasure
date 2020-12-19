
/**
 * NOTE: if I go this route, then this means that an Item has the CHARM CAPABILITY but the CharmInstances may be empty.
 * ie. an item has the ABILITY to be charmed but isn't necessarily charmed.
 * All checks against hasCapability() then would also have to check against the capabilities CharmInstances state.
 * ie. !getCapability().getCharmInstances().isEmpty()
 * NOTE: this also throws a wrench into items like Coins, where they are stackable, but if ICharmed, or ICharmable, they can not be stacked.
 * So in this case then you still would need multiple Item classes to represent each type of charm state.
 * Things like Rings, Amulets, Bracelets, Broches and other Adornments makes sense to be non-stackable, and thus would use ICharmable.
 */
public interface ICharmable {

}