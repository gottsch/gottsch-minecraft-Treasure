package com.someguyssoftware.treasure2.tileentity;

import com.someguyssoftware.treasure2.inventory.StandardChestContainer;

import net.minecraft.world.entity.player.Player;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.SoundEvents;
import net.minecraft.network.chat.TranslatableComponent;

/**
 * 
 * @author Mark Gottschling on Nov 29, 2020
 *
 */
public class CardboardBoxTileEntity extends AbstractTreasureChestTileEntity {
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
		super(TreasureTileEntities.CARDBOARD_BOX_TILE_ENTITY_TYPE);
		setCustomName(new TranslatableComponent("display.cardboard_box.name"));
	}


	/**
	 * 
	 * @param windowID
	 * @param inventory
	 * @param player
	 * @return
	 */
	public Container createServerContainer(int windowID, PlayerInventory inventory, Player player) {
		return new StandardChestContainer(windowID, inventory, this);
	}

	@Override
	public void updateEntityState() {

		// save the previous positions and angles of box components
		this.prevLidAngle = this.lidAngle;
		this.prevInnerLidAngle = this.innerLidAngle;

		// opening ie. players
		if (this.openCount > 0) {
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
					this.playSound(SoundEvents.CHEST_OPEN);
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
					this.playSound(SoundEvents.CHEST_CLOSE);
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
