package com.someguyssoftware.treasure2.tileentity;

import com.someguyssoftware.treasure2.inventory.StandardChestContainer;

import net.minecraft.world.entity.player.Player;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SoundEvents;
import net.minecraft.network.chat.TranslatableComponent;

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
		super(TreasureTileEntities.CRATE_CHEST_TILE_ENTITY_TYPE);
		setCustomName(new TranslatableComponent("display.crate_chest.name"));
	}

	public CrateChestTileEntity(TileEntityType<? extends CrateChestTileEntity> tileEntityType) {
		super(tileEntityType);
		setCustomName(new TranslatableComponent("display.crate_chest.name"));
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
		this.prevLidAngle = this.lidAngle;

		if (this.openCount > 0 && this.lidAngle == 0.0F) {
			this.playSound(SoundEvents.CHEST_OPEN);
		}

		if (this.openCount == 0 && this.lidAngle > 0.0F || this.openCount > 0 && this.lidAngle < .125F) {
			float f2 = this.lidAngle;

			if (this.openCount > 0) {
				this.lidAngle += 0.0125F;
			} else {
				this.lidAngle -= 0.0125F;
			}

			if (this.lidAngle > 0.125F) {
				this.lidAngle = 0.125F;
			}

			if (this.lidAngle < 0.06F && f2 >= 0.06F) {
				this.playSound(SoundEvents.CHEST_CLOSE);
			}

			if (this.lidAngle < 0.0F) {
				this.lidAngle = 0.0F;
			}
		}
	}
}
