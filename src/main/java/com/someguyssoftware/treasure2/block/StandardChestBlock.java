/**
 * 
 */
package com.someguyssoftware.treasure2.block;

import com.someguyssoftware.gottschcore.spatial.ICoords;
import com.someguyssoftware.treasure2.chest.TreasureChestType;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

/**
 * @author Mark Gottschling on Jan 9, 2018
 *
 */
public class StandardChestBlock extends AbstractChestBlock<AbstractTreasureChestTileEntity> {

	//	private static final VoxelShape AABB = Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 14.0D, 14.0D, 14.0D);

	/**
	 * 
	 * @param modID
	 * @param name
	 * @param tileEntity
	 * @param type
	 */
	public StandardChestBlock(String modID, String name, Class<? extends AbstractTreasureChestTileEntity> tileEntity,
			TreasureChestType type, Rarity rarity) {
		this(modID, name, tileEntity, type, rarity, Block.Properties.create(Material.WOOD));
	}

	/**
	 * 
	 * @param modID
	 * @param name
	 * @param material
	 * @param tileEntity
	 * @param type
	 */
	public StandardChestBlock(String modID, String name, Class<? extends AbstractTreasureChestTileEntity> tileEntity,
			TreasureChestType type, Rarity rarity, Block.Properties properties) {
		super(modID, name, tileEntity, type, rarity, properties);
	}


	/**
	 * 
	 */
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return this.getBounds()[0];
	}

	/**
	 * Handled by breakBlock()
	 */
	//	@Override
	//	public Item getItemDropped(BlockState state, Random rand, int fortune) {
	//		return null;
	//	}

	/**
	 * 
	 * @param tagCompound
	 */
//	private void dump(CompoundNBT tag, ICoords coords, String title) {
		//		ChestNBTPrettyPrinter printer = new ChestNBTPrettyPrinter();
		//		SimpleDateFormat formatter = new SimpleDateFormat("yyyymmdd");
		//
		//		String filename = String.format("chest-nbt-%s-%s.txt", formatter.format(new Date()),
		//				coords.toShortString().replaceAll(" ", "-"));
		//
		//		Path path = Paths.get(TreasureConfig.LOGGING.folder, "dumps").toAbsolutePath();
		//		try {
		//			Files.createDirectories(path);
		//		} catch (IOException e) {
		//			Treasure.LOGGER.error("Couldn't create directories for dump files:", e);
		//			return;
		//		}
		//		String s = printer.print(tag, Paths.get(path.toString(), filename), title);
		//		Treasure.LOGGER.debug(s);
//	}


}
