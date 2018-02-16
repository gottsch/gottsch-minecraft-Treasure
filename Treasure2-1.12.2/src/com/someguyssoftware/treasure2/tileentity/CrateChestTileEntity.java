package com.someguyssoftware.treasure2.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.translation.I18n;

/**
 * 
 * @author Mark Gottschling on Jan 19, 2018
 *
 */
public class CrateChestTileEntity extends AbstractTreasureChestTileEntity {
    /** The current angle of the latch (between 0 and 1) */
    public float latchAngle;
    /** The angle of the latch last tick */
    public float prevLatchAngle;

	/**
	 * 
	 * @param texture
	 */
	public CrateChestTileEntity() {
		super();
		setCustomName(I18n.translateToLocal("display.crate_chest.name"));
	}
	
	  /**
     * Like the old updateEntity(), except more generic.
     */
	@Override
	public void update() {
		int i = this.pos.getX();
		int j = this.pos.getY();
		int k = this.pos.getZ();
		++this.ticksSinceSync;

		if (!this.world.isRemote && this.numPlayersUsing != 0 && (this.ticksSinceSync + i + j + k) % 200 == 0) {
			this.numPlayersUsing = 0;

			for (EntityPlayer entityplayer : this.world.getEntitiesWithinAABB(EntityPlayer.class,
					new AxisAlignedBB((double) ((float) i - 5.0F), (double) ((float) j - 5.0F),
							(double) ((float) k - 5.0F), (double) ((float) (i + 1) + 5.0F),
							(double) ((float) (j + 1) + 5.0F), (double) ((float) (k + 1) + 5.0F)))) {
				if (entityplayer.openContainer instanceof ContainerChest) {
					IInventory iinventory = ((ContainerChest) entityplayer.openContainer).getLowerChestInventory();

					// TODO proxy goes here:  if (iinventory == this.getProxy() 
                    if (iinventory == this) {
                        ++this.numPlayersUsing;
                    }
				}
			}
		}

		this.prevLidAngle = this.lidAngle;

		if (this.numPlayersUsing > 0 && this.lidAngle == 0.0F) {
			double d1 = (double) i + 0.5D;
			double d2 = (double) k + 0.5D;

			this.world.playSound((EntityPlayer) null, d1, (double) j + 0.5D, d2, SoundEvents.BLOCK_CHEST_OPEN,
					SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
		}

		if (this.numPlayersUsing == 0 && this.lidAngle > 0.0F || this.numPlayersUsing > 0 && this.lidAngle < .125F) {
			float f2 = this.lidAngle;

			if (this.numPlayersUsing > 0) {
				this.lidAngle += 0.0125F;
			} else {
				this.lidAngle -= 0.0125F;
			}

			if (this.lidAngle > 0.125F) {
				this.lidAngle = 0.125F;
			}

			if (this.lidAngle < 0.06F && f2 >= 0.06F) {
				double d3 = (double) i + 0.5D;
				double d0 = (double) k + 0.5D;

				this.world.playSound((EntityPlayer) null, d3, (double) j + 0.5D, d0, SoundEvents.BLOCK_CHEST_CLOSE,
						SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
			}

			if (this.lidAngle < 0.0F) {
				this.lidAngle = 0.0F;
			}
		}
	}
}
