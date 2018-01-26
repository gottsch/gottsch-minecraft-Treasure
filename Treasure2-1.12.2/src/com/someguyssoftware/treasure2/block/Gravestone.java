/**
 * 
 */
package com.someguyssoftware.treasure2.block;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.someguyssoftware.gottschcore.block.ModBlock;
import com.someguyssoftware.treasure2.Treasure;


/**
 * 
 * @author Mark Gottschling on Jan 18, 2018
 *
 */
public class Gravestone extends ModBlock {

	// meta/state properties
    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public static final PropertyEnum TEXTURE = PropertyEnum.create("texture", EnumTexture.class);
	public static final PropertyEnum ENGRAVING = PropertyEnum.create("engraving", EnumEngraving.class);
	
	// block class level properties	
	public String texture1;
	public String texture2;
	public String engraving1;
	public String engraving2;
	
	/**
	 * 
	 * @param modID
	 * @param name
	 */
	public Gravestone(String modID, String name) {
		this(modID, name, Material.ROCK);
	}
	
	/**
	 * 
	 * @param modID
	 * @param name
	 * @param material
	 */
	public Gravestone(String modID, String name, Material material) {
		super(modID, name, Material.ROCK);
		setHardness(5.0F);
		setHarvestLevel("pickaxe", 1);
//		this.setDefaultState(this.blockState.getBaseState()
//				.withProperty(FACING, EnumFacing.NORTH)
//				.withProperty(TEXTURE, EnumTexture.TEXTURE1)
//				.withProperty(ENGRAVING, EnumEngraving.ENGRAVING1));
		
		setCreativeTab(Treasure.TREASURE_TAB);
	}
	

	/**
	 * 
	 */
	@Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {FACING, TEXTURE, ENGRAVING});
    }
	
	// used by the renderer to control lighting and visibility of other blocks.
	// set to false because this block doesn't fill the entire 1x1x1 space
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	// used by the renderer to control lighting and visibility of other blocks, also by
	// (eg) wall or fence to control whether the fence joins itself to this block
	// set to false because this block doesn't fill the entire 1x1x1 space
	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	/**
	 * This is required to process alpha channels in block textures
	 */
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        
    	if (Minecraft.isFancyGraphicsEnabled()) {
    		return BlockRenderLayer.TRANSLUCENT;
    	}
    	else {
    		return BlockRenderLayer.CUTOUT_MIPPED;
    	}
    }
    
	/**
	 * Render using a TESR.
	 */
	@Override
	public EnumBlockRenderType getRenderType(IBlockState iBlockState) {
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		// face the block  towards the palyer (there isn't really a front)
		worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 3);
	}
	
    /**
     * Convert the given metadata into a BlockState for this Block
     */
	@Override
    public IBlockState getStateFromMeta(int meta) {
		IBlockState blockState = null;

		// 0-3 = t1/e1
		// 4-7 = t2/e1
		// 8-11 = t1/e2
		// 12-15 = t2/e2
		
        if ((meta & 4) > 0 && (meta & 8) < 1) {
        	blockState = this.getDefaultState()
        			.withProperty(TEXTURE, EnumTexture.TEXTURE2)
        			.withProperty(ENGRAVING, EnumEngraving.ENGRAVING1)
        			.withProperty(FACING, EnumFacing.getHorizontal(meta % 4));
        }        
        else if ((meta & 8) == 8 && (meta & 12) == 8) {
        	blockState = this.getDefaultState()
        			.withProperty(TEXTURE, EnumTexture.TEXTURE1)
        			.withProperty(ENGRAVING, EnumEngraving.ENGRAVING2)
        			.withProperty(FACING, EnumFacing.getHorizontal(meta % 4));
        }
        else if ((meta & 12) > 8) {
        	blockState = this.getDefaultState()
        			.withProperty(TEXTURE, EnumTexture.TEXTURE2)
        			.withProperty(ENGRAVING, EnumEngraving.ENGRAVING2)
        			.withProperty(FACING, EnumFacing.getHorizontal(meta % 4));        	
        }
        else {
        	blockState = this.getDefaultState()
        			.withProperty(TEXTURE, EnumTexture.TEXTURE1)
        			.withProperty(ENGRAVING, EnumEngraving.ENGRAVING1)
        			.withProperty(FACING, EnumFacing.getHorizontal(meta % 4));
        }

		return  blockState;
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state) {
    	int facingIndex = ((EnumFacing)state.getValue(FACING)).getHorizontalIndex(); // 0-3
    	int textureIndex = ((EnumTexture)state.getValue(TEXTURE)).getIndex();
    	int engravingIndex = ((EnumEngraving)state.getValue(ENGRAVING)).getIndex();
    	
    	/*
    	 * will return a value 0 -15:
    	 * (1->4 * 1->2 * 1->2 ) - 1
    	 */
    	// TODO this is wrong
    	//return ((facingIndex + 1) * (textureIndex + 1) * (engravingIndex + 1)) -1;
    	return facingIndex + (textureIndex * 4) + (engravingIndex * 8);
    }
    
    /**
     * Drop the item with the current state;
     */
    @Override
    public int damageDropped(IBlockState state) {
    	return getMetaFromState(state);
    }

    /**
     * 
     */
    @Override
    public void getSubBlocks(CreativeTabs tabs, NonNullList<ItemStack> items) {
    	items.add(new ItemStack(this, 1, 0)); // meta 0 = texture1, engraving1
    	items.add(new ItemStack(this, 1, 4)); // meta 4 = texture2. engraving1
    	items.add(new ItemStack(this, 1, 8)); // meta 8 = texture1, engraving2
    	items.add(new ItemStack(this, 1, 12)); // meta 12 = texture2, engraving2
    }

    /**
     * 
     */
    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
    	return new ItemStack(Item.getItemFromBlock(this), 1, this.getMetaFromState(world.getBlockState(pos)));
    }
    
//	@Override	
//	public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
//		IBlockState state = (IBlockState) worldIn.getBlockState(pos);
//		setBlockBoundsBasedOnState(state, this.getXb(), this.getYb(), this.getZb());
//	}

	/**
	 * 
	 * @param state
	 * @param xb
	 * @param yb
	 * @param zb
	 */
//	public void setBlockBoundsBasedOnState(IBlockState state, Bounds xb, Bounds yb, Bounds zb) {
//		float widthMax= (float) Math.ceil(xb.getMax());
//		float depthMax = (float) Math.ceil(zb.getMax());		
//		
//        if (state.getValue(FACING) == EnumFacing.NORTH) {
//			this.setBlockBounds(
//					// as is
//					xb.getMin(), 
//					yb.getMin(),
//					zb.getMin(),
//					xb.getMax(),
//					yb.getMax(),
//					zb.getMax()
//				);
//        }
//        else if (state.getValue(FACING) == EnumFacing.SOUTH) {
//			this.setBlockBounds(
//					widthMax- xb.getMax(), 
//					yb.getMin(),
//					depthMax - zb.getMax(),
//					widthMax - xb.getMin(),
//					yb.getMax(),
//					depthMax - zb.getMin()
//				);		
//        }
//        else if (state.getValue(FACING) == EnumFacing.EAST) {
//			this.setBlockBounds(
//					depthMax - zb.getMax(), 
//					yb.getMin(),
//					widthMax - xb.getMax(),
//					depthMax - zb.getMin(),
//					yb.getMax(),
//					widthMax - xb.getMin()
//				);
//        }
//        else if (state.getValue(FACING) == EnumFacing.WEST) {
//			this.setBlockBounds(
//					// transpose only
//					zb.getMin(), 
//					yb.getMin(),
//					xb.getMin(),
//					zb.getMax(),
//					yb.getMax(),
//					xb.getMax()
//				);
//        }
//	}

	/**
	 * 
	 * @author Mark Gottschling on Nov 16, 2015
	 *
	 */
    public static enum EnumEngraving implements IStringSerializable {
        ENGRAVING1(0, "engraving1"),
        ENGRAVING2(1, "engraving2");

        private final int index;
        private final String name;
        
        private EnumEngraving(int index, String name) {
        	this.index = index;
            this.name = name;
        }

        public String toString() {
            return this.name;
        }

        public String getName() {
            return this.name;
        }

		public int getIndex() {
			return index;
		}
    }
    
	/**
	 * 
	 * @author Mark Gottschling on Nov 16, 2015
	 *
	 */
    public static enum EnumTexture implements IStringSerializable {
        TEXTURE1(0, "texture1"),
        TEXTURE2(1, "texture2");

        private final int index;
        private final String name;
        
        private EnumTexture(int index, String name) {
        	this.index = index;
            this.name = name;
        }

        public String toString() {
            return this.name;
        }

        public String getName() {
            return this.name;
        }

		public int getIndex() {
			return index;
		}
    }

    ///////////////////////////////////////////////////////
	public String getTexture1() {
		return texture1;
	}

	public Gravestone setTexture1(String texture1) {
		this.texture1 = texture1;
		return this;
	}

	public String getTexture2() {
		return texture2;
	}

	public Gravestone setTexture2(String texture2) {
		this.texture2 = texture2;
		return this;
	}

	public String getEngraving1() {
		return engraving1;
	}

	public Gravestone setEngraving1(String engraving1) {
		this.engraving1 = engraving1;
		return this;
	}

	public String getEngraving2() {
		return engraving2;
	}

	public Gravestone setEngraving2(String engraving2) {
		this.engraving2 = engraving2;
		return this;
	}	
}
