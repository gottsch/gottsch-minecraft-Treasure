/**
 * 
 */
package com.someguyssoftware.treasure2.item.charm;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.someguyssoftware.gottschcore.positional.ICoords;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * @author Mark Gottschling on Apr 25, 2020
 *
 */
public interface ICharm {
	public String getName();
	public String getType();
	public int getLevel();
	public double getMaxValue();
	public int getMaxDuration();
	public double getMaxPercent();
    
    public ICharmInstance createInstance();
    public boolean update(World world, Random random, ICoords coords, EntityPlayer player, Event event, final ICharmData data);

    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag, ICharmData data);
	public NBTTagCompound writeToNBT(NBTTagCompound nbt);

	// TODO remove thses for a generic one that will check the instance
	public boolean doCharm(World world, Random random, ICoords coords, EntityPlayer player, LivingUpdateEvent event, final ICharmData data);
	public boolean doCharm(World world, Random random, ICoords coords, EntityPlayer player, LivingDamageEvent event, final ICharmData data);
	public boolean doCharm(World world, Random random, ICoords coords, EntityPlayer player, BlockEvent.HarvestDropsEvent event, final ICharmData data);
}
