package com.someguyssoftware.treasure2.block;

import com.someguyssoftware.gottschcore.block.ModLogBlock;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LogBlock;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;

/**
 * 
 * @author Mark Gottschling on Mar 20, 2018
 *
 */
//@Deprecated
public class WitherLogBlock extends ModLogBlock implements ITreasureBlock {
//	public static final EnumProperty<Bark> BARK = EnumProperty.<Bark>create("bark", Bark.class);
//	public static final EnumProperty<SkeletonBlock.EnumPartType> PART = EnumProperty.<SkeletonBlock.EnumPartType>create("part", SkeletonBlock.EnumPartType.class);
	
	/**
	 * 
	 * @param modID
	 * @param name
	 * @param verticalColorIn
	 * @param properties
	 */
	public WitherLogBlock(String modID, String name,Properties properties) {
		super(modID, name, MaterialColor.OBSIDIAN, properties);
		this.setDefaultState(this.stateContainer.getBaseState().with(AXIS, Direction.Axis.Y));//.with(BARK, Bark.NORMAL));
	}

	/**
	 * 
	 */
//	@Override
//	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
////		builder.add(PART, AXIS);
//	}

//	/**
//	 * 
//	 * @author Mark Gottschling on Mar 20, 2018
//	 *
//	 */
//	public static enum Bark implements IStringSerializable {
//		NORMAL("normal", 0),
//		FULL("full", 1),
//		STRIPPED("stripped", 2);
//
//	    private final String name;
//	    private final int value;
//	    
//	    private Bark(String name, int value) {
//	        this.name = name;
//	        this.value = value;
//	    }
//
//		/**
//		 * @return the name
//		 */
//		public String getName() {
//			return name;
//		}
//
//		/**
//		 * @return the value
//		 */
//		public int getValue() {
//			return value;
//		}
//	}
}
