package com.someguyssoftware.treasure2.item.charm;

import java.util.Random;

import com.someguyssoftware.gottschcore.positional.ICoords;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.world.BlockEvent;

public interface ICharmState {

	ICharm getCharm();

	void setCharm(ICharm charm);

	ICharmVitals getVitals();

	void setVitals(ICharmVitals vitals);

	boolean doCharm(World world, Random random, ICoords coords, EntityPlayer player, LivingUpdateEvent event);

	boolean doCharm(World world, Random random, ICoords coords, EntityPlayer player, LivingDamageEvent event);
	
	boolean doCharm(World world, Random random, ICoords coords, EntityPlayer player, BlockEvent.HarvestDropsEvent event);
}