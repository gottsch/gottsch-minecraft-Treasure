/**
 * 
 */
package com.someguyssoftware.treasure2.block;

import java.util.Random;

import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.chest.TreasureChestType;
import com.someguyssoftware.treasure2.entity.monster.MimicEntity;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

/**
 * 
 * @author Mark Gottschling on Aug 20, 2018
 *
 */

public class MimicChestBlock extends AbstractChestBlock {
	/*
	 * the class of the mimic this BlockChest should spawn.
	 */
	private Class<? extends MimicEntity> mimicClass;

	/**
	 * 
	 * @param modID
	 * @param name
	 * @param te
	 * @param mimic
	 * @param type
	 * @param rarity
	 */
	public MimicChestBlock(String modID, String name, Class<? extends AbstractTreasureChestTileEntity> te,
			Class<? extends MimicEntity> mimic, TreasureChestType type, Rarity rarity) {
		this(modID, name, Material.WOOD, te, type, rarity);
		setMimicClass(mimic);
	}

	/**
	 * 
	 * @param modID
	 * @param name
	 * @param material
	 * @param te
	 * @param type
	 */
	public MimicChestBlock(String modID, String name, Material material,
			Class<? extends AbstractTreasureChestTileEntity> te, TreasureChestType type, Rarity rarity) {
		super(modID, name, material, te, type, rarity);
	}

	/**
	 * Called just after the player places a block.
	 */
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		Treasure.LOGGER.debug("Placing mimic from item");

		boolean shouldUpdate = false;
		boolean forceUpdate = false;
		AbstractTreasureChestTileEntity tcte = null;

		// face the block towards the player (there isn't really a front)
		worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 3);
		TileEntity te = worldIn.getTileEntity(pos);
		if (te != null && te instanceof AbstractTreasureChestTileEntity) {
			// get the backing tile entity
			tcte = (AbstractTreasureChestTileEntity) te;

			// set the name of the chest
			if (stack.hasDisplayName()) {
				tcte.setCustomName(stack.getDisplayName());
			}

			// update the TCTE facing
			tcte.setFacing(placer.getHorizontalFacing().getOpposite().getIndex());
		}
		if ((forceUpdate || shouldUpdate) && tcte != null) {
			// update the client
			tcte.sendUpdates();
		}
	}

	/**
	 * 
	 */
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

		AbstractTreasureChestTileEntity te = (AbstractTreasureChestTileEntity) worldIn.getTileEntity(pos);

		// exit if on the client
		if (WorldInfo.isClientSide(worldIn)) {
			return true;
		}

		// legacy mimic block (pre 2.4.0) - always the mimic registered for this block
		if (te != null) {
			spawnMimic(worldIn, pos, state, te, playerIn, true);
		}
		return true;
	}

	@Override
	public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {
		super.onBlockClicked(worldIn, pos, playerIn);
	}

	@Override
	public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn) {
		super.onBlockDestroyedByExplosion(worldIn, pos, explosionIn);
	}

	/**
	 * 
	 */
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		// breaking or blowing up a mimic turns it into the mimic
		TileEntity te = worldIn.getTileEntity(pos);
		if (WorldInfo.isServerSide(worldIn) && te instanceof AbstractTreasureChestTileEntity) {
			spawnMimic(worldIn, pos, state, (AbstractTreasureChestTileEntity) te, null, false);
		}
	}

	/**
	 * Handled by breakBlock()
	 */
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return null;
	}

	/**
	 * @return the mimicClass
	 */
	public Class<? extends MimicEntity> getMimicClass() {
		return mimicClass;
	}

	/**
	 * @param mimicClass the mimicClass to set
	 */
	public MimicChestBlock setMimicClass(Class<? extends MimicEntity> mimicClass) {
		this.mimicClass = mimicClass;
		return this;
	}

}
