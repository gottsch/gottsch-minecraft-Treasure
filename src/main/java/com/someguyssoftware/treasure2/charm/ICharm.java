/**
 * 
 */
package com.someguyssoftware.treasure2.charm;

import java.util.List;
import java.util.Random;

import com.someguyssoftware.gottschcore.positional.ICoords;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * @author Mark Gottschling on Apr 25, 2020
 *
 */
public interface ICharm {
	public ResourceLocation getName();
	public String getType();
	public int getLevel();
	public double getMaxValue();
	public int getMaxDuration();
	public double getMaxPercent();
	boolean isAllowMultipleUpdates();
    
    public boolean update(World world, Random random, ICoords coords, EntityPlayer player, Event event, final ICharmEntity entity);

    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn, ICharmEntity entity);
    
	public NBTTagCompound save(NBTTagCompound nbt);
	
	/**
	 * 
	 */
	ICharmEntity createEntity();
	boolean isCurse();
	public Class<?> getRegisteredEvent();
}
