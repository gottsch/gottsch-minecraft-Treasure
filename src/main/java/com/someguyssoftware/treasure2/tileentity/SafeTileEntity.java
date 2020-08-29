package com.someguyssoftware.treasure2.tileentity;

import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.inventory.ITreasureContainer;
import com.someguyssoftware.treasure2.inventory.StandardChestContainer;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * 
 * @author Mark Gottschling on Feb 19, 2018
 *
 */
public class SafeTileEntity extends AbstractTreasureChestTileEntity {
	/** The angle of the latch last tick */
	public float prevLatchPos;

	public float handleAngle;
	public float prevHandleAngle;

	public boolean isHandleOpen = false;
	public boolean isHandleClosed = true;
	public boolean isLidOpen = false;
	public boolean isLidClosed = false;

	/**
	 * 
	 * @param texture
	 */
	public SafeTileEntity() {
		super(TreasureTileEntities.safeTileEntityType);
		setCustomName(new TranslationTextComponent("display.safe.name"));
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
		int x = this.pos.getX();
		int y = this.pos.getY();
		int z = this.pos.getZ();
		++this.ticksSinceSync;

		/*
		 * recalculating if the chest is in use by any players
		 */
		if (WorldInfo.isServerSide(getWorld()) && this.numPlayersUsing != 0
				&& (this.ticksSinceSync + x + y + z) % 200 == 0) {
			this.numPlayersUsing = 0;

			for (PlayerEntity entityplayer : this.world.getEntitiesWithinAABB(PlayerEntity.class,
					new AxisAlignedBB((double) ((float) x - 5.0F), (double) ((float) y - 5.0F),
							(double) ((float) z - 5.0F), (double) ((float) (x + 1) + 5.0F),
							(double) ((float) (y + 1) + 5.0F), (double) ((float) (z + 1) + 5.0F)))) {
				if (entityplayer.openContainer instanceof ITreasureContainer) {
					IInventory iinventory = ((ITreasureContainer) entityplayer.openContainer).getContents();

					if (iinventory == this) {
						++this.numPlayersUsing;
					}
				}
			}
		}

		// save the previous positions and angles of safe components
		this.prevLidAngle = this.lidAngle;
		this.prevHandleAngle = this.handleAngle;

		// opening ie. players
		if (this.numPlayersUsing > 0) {
			// test the handle
			if (this.handleAngle > -1.0F) {
				isHandleOpen = false;
				this.handleAngle -= 0.1F;
				isHandleClosed = false;
				if (this.handleAngle <= -1.0F) {
					this.handleAngle = -1.0F;
					isHandleOpen = true;
				}
			} else {
				isHandleOpen = true;
			}

			if (isHandleOpen) {
				// play the opening chest sound the at the beginning of opening
				if (this.lidAngle == 0.0F) {
//					double d1 = (double) x + 0.5D;
//					double d2 = (double) z + 0.5D;
//					this.world.playSound((PlayerEntity) null, d1, (double) y + 0.5D, d2, SoundEvents.BLOCK_CHEST_OPEN,
//							SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
					this.playSound(SoundEvents.BLOCK_CHEST_OPEN);
				}

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
			}
		}

		// closing ie no players
		else {
			float f2 = this.lidAngle;

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

			// play the closing sound
			if (this.lidAngle < 0.06F && f2 >= 0.06F) {
//				double d3 = (double) x + 0.5D;
//				double d0 = (double) z + 0.5D;
//
//				this.world.playSound((PlayerEntity) null, d3, (double) y + 0.5D, d0, SoundEvents.BLOCK_CHEST_CLOSE,
//						SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
				this.playSound(SoundEvents.BLOCK_CHEST_CLOSE);
			}

			if (isLidClosed) {
				if (this.handleAngle < 0.0F) {
					isHandleClosed = false;
					this.handleAngle += 0.1F;
					isHandleOpen = false;
					if (this.handleAngle >= 0.0F) {
						this.handleAngle = 0.0F;
						isHandleClosed = true;
					}
				} else {
					isHandleClosed = true;
				}
			}

		}
	}

	/**
	 * @return the prevLatchPos
	 */
	public float getPrevLatchPos() {
		return prevLatchPos;
	}

	/**
	 * @param prevLatchPos the prevLatchPos to set
	 */
	public void setPrevLatchPos(float prevLatchPos) {
		this.prevLatchPos = prevLatchPos;
	}

	/**
	 * @return the handleAngle
	 */
	public float getHandleAngle() {
		return handleAngle;
	}

	/**
	 * @param handleAngle the handleAngle to set
	 */
	public void setHandleAngle(float handleAngle) {
		this.handleAngle = handleAngle;
	}

	/**
	 * @return the prevHandleAngle
	 */
	public float getPrevHandleAngle() {
		return prevHandleAngle;
	}

	/**
	 * @param prevHandleAngle the prevHandleAngle to set
	 */
	public void setPrevHandleAngle(float prevHandleAngle) {
		this.prevHandleAngle = prevHandleAngle;
	}
}
