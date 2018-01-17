/**
 * MIT License
 *
 * Copyright (c) 2017 Mark Gottschling (gottsch)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * Contributors:
 * <p>
 * gottsch - initial API and implementation
 */
package com.someguyssoftware.treasure2.block;

import com.someguyssoftware.treasure2.tileentity.TreasureChestTileEntity;
import com.someguyssoftware.treasure2.tileentity.TestChestTileEntity;
import com.someguyssoftware.treasure2.tileentity.WoodenChestTileEntity;

/**
 * TODO change class name and figure out way to get the name into the TreasureChestItem
 * @author Mark Gottschling onDec 22, 2017
 * 
 * Used cpw's idea for containing chest type info in an enum.
 *
 */
@Deprecated
public enum OldTreasureChestType {
//	TEST(TestChestTileEntity.class, 0, 0),
	WOODEN(WoodenChestTileEntity.class, 0, 0);
	
	public static final OldTreasureChestType VALUES[] = values();
	
	public final String name;
	private Class<? extends TreasureChestTileEntity> clazz;
	private int minLocks;
	private int maxLocks;
	
	/**
	 * 
	 * @param clazz
	 * @param minLocks
	 * @param maxLocks
	 */
	OldTreasureChestType(Class<? extends TreasureChestTileEntity> clazz, int minLocks, int maxLocks) {
		this.name = this.name().toLowerCase();
		this.clazz = clazz;
		this.minLocks = minLocks;
		this.maxLocks = maxLocks;
	}
	
	
}
