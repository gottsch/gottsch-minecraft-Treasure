public class ExampleItemCapabilityProvider implements ICapabilityProvider, ICapabilitySerializable<NBTTagCompound> {

    // capabilities for item
    private final EffectiveMaxDamageCapability effectiveMaxDamageCapability = new EffectiveMaxDamageCapability();

    @Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == TreasureCapabilities.EFFECTIVE_MAX_DAMAGE_CAPABILITY) {
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == TreasureCapabilities.EFFECTIVE_MAX_DAMAGE_CAPABILITY) {
			return EFFECTIVE_MAX_DAMAGE_CAPABILITY.cast(this.instance);
		}
		return null;
	}
}