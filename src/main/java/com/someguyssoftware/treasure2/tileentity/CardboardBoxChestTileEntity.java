package com.someguyssoftware.treasure2.tileentity;

import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.inventory.ITreasureContainer;
import com.someguyssoftware.treasure2.inventory.StandardChestContainer;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * 
 * @author Mark Gottschling on Nov 29, 2020
 *
 */
public class CardboardBoxChestTileEntity extends AbstractTreasureChestTileEntity {
	/** The angle of the latch last tick */
	public float prevInnerLidPos;

	public float innerLidAngle;
	public float prevInnerLidAngle;

	public boolean isLidOpen = false;
	public boolean isLidClosed = true;
	public boolean isInnerLidOpen = false;
	public boolean isInnerLidClosed = true;

	/**
	 * 
	 * @param texture
	 */
	public CardboardBoxTileEntity() {
		super();
		setCustomName(I18n.translateToLocal("display.cardboard_box.name"));
	}

	/**
	 * 
	 * @param windowID
	 * @param inventory
	 * @param player
	 * @return
	 */
	public Container createServerContainer(int windowID, PlayerInventory inventory, PlayerEntity player) {
		return new StandardChestContainer(windowID, inventory, this);
	}
	
	/**
	 * Like the old updateEntity(), except more generic.
	 */
	@Override
	public void tick() {
		int i = this.pos.getX();
		int j = this.pos.getY();
		int k = this.pos.getZ();
		++this.ticksSinceSync;

		if (WorldInfo.isServerSide(getWorld()) && this.numPlayersUsing != 0
				&& (this.ticksSinceSync + i + j + k) % 200 == 0) {
			this.numPlayersUsing = 0;

			for (PlayerEntity player : this.world.getEntitiesWithinAABB(PlayerEntity.class,
					new AxisAlignedBB((double) ((float) i - 5.0F), (double) ((float) j - 5.0F),
							(double) ((float) k - 5.0F), (double) ((float) (i + 1) + 5.0F),
							(double) ((float) (j + 1) + 5.0F), (double) ((float) (k + 1) + 5.0F)))) {
				if (player.openContainer instanceof ITreasureContainer) {
					IInventory iinventory = ((ITreasureContainer) player.openContainer).getContents();

					if (iinventory == this) {
						++this.numPlayersUsing;
					}
				}
			}
		}

// save the previous positions and angles of box components
		this.prevLidAngle = this.lidAngle;
		this.prevInnerLidAngle = this.innerLidAngle;

		// opening ie. players
		if (this.numPlayersUsing > 0) {
			// test the lid
			if (this.lidAngle < 1.0F) {
				isLidOpen = false;
				this.lidAngle += 0.1F;
				isLidClosed = false;
				if (this.lidAngle >= 1.0F) {
					this.lidAngle = 1.0F;
					isLidOpen = true;
				}
			} else {
				isLidOpen = true;
			}

			if (isLidOpen) {
				// play the opening chest sound the at the beginning of opening
				if (isInnerLidClosed) {
					double d1 = (double) x + 0.5D;
					double d2 = (double) z + 0.5D;
					this.world.playSound((EntityPlayer) null, d1, (double) y + 0.5D, d2, SoundEvents.BLOCK_CHEST_OPEN,
							SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
				}

				// test the inner lid
				if (this.innerLidAngle < 1.0F) {
					isInnerLidOpen = false;
					this.innerLidAngle += 0.1F;
					isInnerLidClosed = false;
					if (this.innerLidAngle >= 1.0F) {
						this.innerLidAngle = 1.0F;
						isInnerLidOpen = true;
					}
				} else {
					isInnerLidOpen = true;
				}
			}
		}

		// closing
		else {
			// test the inner lid
			if (this.innerLidAngle > 0.0F) {
				isInnerLidClosed = false;
				this.innerLidAngle -= 0.1F;
				isInnerLidOpen = false;
				if (this.innerLidAngle <= 0.0F) {
					this.innerLidAngle = 0.0F;
					isInnerLidClosed = true;
				}
			} else {
				isInnerLidClosed = true;
			}

			// play the closing soud
			if (isInnerLidClosed ) {
				if (isLidOpen) {
					double d3 = (double) x + 0.5D;
					double d0 = (double) z + 0.5D;
					this.world.playSound((EntityPlayer) null, d3, (double) y + 0.5D, d0, SoundEvents.BLOCK_CHEST_CLOSE,
							SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
				}
				// test the outer lid
				if (this.lidAngle > 0.0F) {
					isLidClosed = false;
					this.lidAngle -= 0.1F;
					isLidOpen = false;
					if (this.lidAngle <= 0.0F) {
						this.lidAngle = 0.0F;
						isLidClosed = true;
					}
				} else {
					isLidClosed = true;
				}
			}	
		}		
	}

	public float getPrevInnerLidPos() {
		return prevInnerLidPos;
	}

	public void setPrevInnerLidPos(float innerLidPos) {
		this.prevInnerLidPos = innerLidPos;
	}

	public float getInnerLidAngle() {
		return innerLidAngle;
	}

	public void setInnerLidAngle(float innerLidAngle) {
		this.innerLidAngle = innerLidAngle;
	}

	public float getPrevInnerLidAngle() {
		return prevInnerLidAngle;
	}

	public void setPrevInnerLidAngle(float innerLidAngle) {
		this.prevInnerLidAngle = innerLidAngle;
	}

}
