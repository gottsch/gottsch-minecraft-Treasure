package com.someguyssoftware.treasure2.capability;


import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class AdornmentCapabilityProvider implements ICapabilitySerializable<CompoundNBT> {

	private final ICharmableCapability charmableCap;
	private final IDurabilityCapability durabilityCap;
	private final IRunestonesCapability runestonesCap;
	// TODO add IPouchableCapability
	
	public AdornmentCapabilityProvider() {
		this.charmableCap = new CharmableCapability(0, 0, 0);
		this.durabilityCap = new DurabilityCapability(100, 100);
		this.runestonesCap = new RunestonesCapability(0, 0, 0);
	}
	
	@Deprecated
	public AdornmentCapabilityProvider(ICharmableCapability charmableCap) {
		this.charmableCap = charmableCap;
		this.durabilityCap = new DurabilityCapability(100, 100);
		this.runestonesCap = new RunestonesCapability(0, 0, 0);
	}

	public AdornmentCapabilityProvider(ICharmableCapability charmableCap, IDurabilityCapability durabilityCap) {
		this.charmableCap = charmableCap;
		this.durabilityCap = durabilityCap;
		this.runestonesCap = new RunestonesCapability(0, 0, 0);
	}
	
	public AdornmentCapabilityProvider(ICharmableCapability charmableCap, IRunestonesCapability runestonesCap, IDurabilityCapability durabilityCap) {
		this.charmableCap = charmableCap;
		this.runestonesCap = runestonesCap;
		this.durabilityCap = durabilityCap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction facing) {
		if (capability != null) {
			if (capability == TreasureCapabilities.CHARMABLE) {
				return (LazyOptional<T>) LazyOptional.of(() -> this.charmableCap);
			}
			else if (capability == TreasureCapabilities.RUNESTONES) {
				return (LazyOptional<T>) LazyOptional.of(() -> this.runestonesCap);
			}
			else if (capability == TreasureCapabilities.DURABILITY_CAPABILITY) {
				return (LazyOptional<T>) LazyOptional.of(() -> this.durabilityCap);
			}
		}
		return LazyOptional.empty();
	}
	
	@Override
	public CompoundNBT serializeNBT() {
		CompoundNBT charmableTag = (CompoundNBT)TreasureCapabilities.CHARMABLE.getStorage().writeNBT(TreasureCapabilities.CHARMABLE, charmableCap, null);
		CompoundNBT runestonesTag = (CompoundNBT)TreasureCapabilities.RUNESTONES.getStorage().writeNBT(TreasureCapabilities.RUNESTONES, runestonesCap, null);
		CompoundNBT durabilityTag = (CompoundNBT)TreasureCapabilities.DURABILITY_CAPABILITY.getStorage().writeNBT(TreasureCapabilities.DURABILITY_CAPABILITY, durabilityCap, null);
		CompoundNBT tag = new CompoundNBT();
		tag.put("charmable", charmableTag);
		tag.put("runestones", runestonesTag);
		tag.put("durability", durabilityTag);
		return tag;
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		if (nbt.contains("charmable")) {
			CompoundNBT tag = nbt.getCompound("charmable");
			TreasureCapabilities.CHARMABLE.getStorage().readNBT(TreasureCapabilities.CHARMABLE, charmableCap, null, tag);
		}
		if (nbt.contains("runestones")) {
			CompoundNBT tag = nbt.getCompound("runestones");
			TreasureCapabilities.RUNESTONES.getStorage().readNBT(TreasureCapabilities.RUNESTONES, runestonesCap, null, tag);
		}
		if (nbt.contains("durability")) {
			CompoundNBT tag = nbt.getCompound("durability");
			TreasureCapabilities.DURABILITY_CAPABILITY.getStorage().readNBT(TreasureCapabilities.DURABILITY_CAPABILITY, durabilityCap, null, tag);
		}
	}
}
