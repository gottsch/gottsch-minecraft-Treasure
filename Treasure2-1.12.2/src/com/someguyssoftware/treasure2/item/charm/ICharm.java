/**
 * 
 */
package com.someguyssoftware.treasure2.item.charm;

import java.util.Random;

import com.someguyssoftware.gottschcore.positional.ICoords;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.world.BlockEvent;

/**
 * @author Mark Gottschling on Apr 25, 2020
 *
 */
public interface ICharm {
	public CharmType getCharmType();
	public CharmLevel getCharmLevel();
	public String getName();
	public double getMaxValue();
	public int getMaxDuration();
	public double getMaxPercent();
	
	public NBTTagCompound writeToNBT(NBTTagCompound nbt);

	public ICharmVitals doCharm(World world, Random random, ICoords coords, EntityPlayer player, LivingUpdateEvent event, final ICharmVitals vitals);
	public ICharmVitals doCharm(World world, Random random, ICoords coords, EntityPlayer player, LivingDamageEvent event, final ICharmVitals vitals);
	public ICharmVitals doCharm(World world, Random random, ICoords coords, EntityPlayer player, BlockEvent.HarvestDropsEvent event, final ICharmVitals vitals);
}
