/**
 * 
 */
package com.someguyssoftware.treasure2.tileentity;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nullable;

import com.someguyssoftware.gottschcore.tileentity.AbstractModTileEntity;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.TreasureChestBlock;
import com.someguyssoftware.treasure2.lock.LockState;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

/**
 * 
 * @author Mark Gottschling onDec 22, 2017
 *
 */
public abstract class AbstractTreasureChestTileEntity extends AbstractModTileEntity implements IInventory, ITickable {
	/*
	 * A list of lockStates the chest has. The list should be the size of the max
	 * allowed for the chestType.
	 */
	private List<LockState> lockStates;

	/*
	 * The FACING meta value of the TreasureChestBlock
	 */
	private int facing;

	/*
	 * Vanilla properties for controlling the lid
	 */
	/** The current angle of the lid (between 0 and 1) */
	public float lidAngle;
	/** The angle of the lid last tick */
	public float prevLidAngle;
	/** The number of players currently using this chest */
	public int numPlayersUsing;
	/** Server sync counter (once per 20 ticks) */
	public int ticksSinceSync;

	/** IInventory properties */
	private int numberOfSlots = 27; // default size
	private NonNullList<ItemStack> items = NonNullList.<ItemStack>withSize(getNumberOfSlots(), ItemStack.EMPTY);
	private String customName;

	/**
	 * 
	 */
	public AbstractTreasureChestTileEntity() {
		setFacing(EnumFacing.NORTH.getIndex());
	}

	/**
	 * Like the old updateEntity(), except more generic.
	 */
	public void update() {
		int i = this.pos.getX();
		int j = this.pos.getY();
		int k = this.pos.getZ();
		++this.ticksSinceSync;

		if (WorldInfo.isServerSide(getWorld()) && this.numPlayersUsing != 0
				&& (this.ticksSinceSync + i + j + k) % 200 == 0) {
			this.numPlayersUsing = 0;
			float f = 5.0F;

			for (EntityPlayer entityplayer : this.world.getEntitiesWithinAABB(EntityPlayer.class,
					new AxisAlignedBB((double) ((float) i - 5.0F), (double) ((float) j - 5.0F),
							(double) ((float) k - 5.0F), (double) ((float) (i + 1) + 5.0F),
							(double) ((float) (j + 1) + 5.0F), (double) ((float) (k + 1) + 5.0F)))) {
				if (entityplayer.openContainer instanceof ContainerChest) {
					IInventory iinventory = ((ContainerChest) entityplayer.openContainer).getLowerChestInventory();

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

		if (this.numPlayersUsing == 0 && this.lidAngle > 0.0F || this.numPlayersUsing > 0 && this.lidAngle < 1.0F) {
			float f2 = this.lidAngle;

			if (this.numPlayersUsing > 0) {
				this.lidAngle += 0.1F;
			} else {
				this.lidAngle -= 0.1F;
			}

			if (this.lidAngle > 1.0F) {
				this.lidAngle = 1.0F;
			}

			float f3 = 0.5F;

			if (this.lidAngle < 0.5F && f2 >= 0.5F) {
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

	/**
	 * 
	 */
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound parentNBT) {
		try {
			parentNBT = super.writeToNBT(parentNBT);

			// write lock states
			writeLockStatesToNBT(parentNBT);

			// write inventory
			writeInventoryToNBT(parentNBT);

			// write custom name
			writePropertiesToNBT(parentNBT);
		} catch (Exception e) {
			Treasure.logger.error("Error writing to NBT:", e);
		}
		return parentNBT;
	}

	/**
	 * 
	 * @param parentNBT
	 * @return
	 */
	public NBTTagCompound writeLockStatesToNBT(NBTTagCompound parentNBT) {
		try {
			// write lock states
			if (getLockStates() != null && !getLockStates().isEmpty()) {
				NBTTagList list = new NBTTagList();
				// write custom tile entity properties
				for (LockState state : getLockStates()) {
//					Treasure.logger.debug("Writing lock state:" + state);
					NBTTagCompound stateNBT = new NBTTagCompound();
					state.writeToNBT(stateNBT);
					list.appendTag(stateNBT);
				}
				parentNBT.setTag("lockStates", list);
			}
		} catch (Exception e) {
			Treasure.logger.error("Error writing LockStates to NBT:", e);
		}
		return parentNBT;
	}

	/**
	 * Write custom properties to NBT
	 * 
	 * @param parentNBT
	 * @return
	 */
	public NBTTagCompound writePropertiesToNBT(NBTTagCompound parentNBT) {
		try {
			// write custom name
			if (this.hasCustomName()) {
				parentNBT.setString("CustomName", this.customName);
			}
			// write facing
//			Treasure.logger.debug("Writing FACING to NBT ->{}", getFacing());
			parentNBT.setInteger("facing", getFacing());
		} catch (Exception e) {
			Treasure.logger.error("Error writing Properties to NBT:", e);
		}
		return parentNBT;
	}

	/**
	 * Writes the inventory to NBT
	 * 
	 * @param parentNBT
	 * @return
	 */
	public NBTTagCompound writeInventoryToNBT(NBTTagCompound parentNBT) {
		try {
			// write inventory
			ItemStackHelper.saveAllItems(parentNBT, this.getItems());
		} catch (Exception e) {
			Treasure.logger.error("Error writing Inventory to NBT:", e);
		}
		return parentNBT;
	}

	/**
	 * 
	 * @param parentNBT
	 */
	public void readInventoryFromNBT(NBTTagCompound parentNBT) {
		try {
			// read the inventory
			ItemStackHelper.loadAllItems(parentNBT, this.getItems());
		} catch (Exception e) {
			Treasure.logger.error("Error reading Properties from NBT:", e);
		}
	}

	/**
	 * 
	 * @param parentNBT
	 */
	public void readLockStatesFromNBT(NBTTagCompound parentNBT) {
		try {
			// read the lockstates
			if (parentNBT.hasKey("lockStates")) {
//				Treasure.logger.debug("Has lockStates");
				if (this.getLockStates() != null) {
//					Treasure.logger.debug("size of internal lockstates:" + this.getLockStates().size());
				} else {
					this.setLockStates(new LinkedList<LockState>());
//					Treasure.logger.debug("created lockstates:" + this.getLockStates().size());
				}

				List<LockState> states = new LinkedList<LockState>();
				NBTTagList list = parentNBT.getTagList("lockStates", Constants.NBT.TAG_COMPOUND);
				for (int i = 0; i < list.tagCount(); i++) {
					NBTTagCompound c = list.getCompoundTagAt(i);
					LockState lockState = LockState.readFromNBT(c);
					states.add(lockState.getSlot().getIndex(), lockState);
//					Treasure.logger.debug("Read NBT lockstate:" + lockState);
				}
				// update the tile entity
				setLockStates(states);
			}
		} catch (Exception e) {
			Treasure.logger.error("Error reading Lock States from NBT:", e);
		}
	}

	/**
	 * 
	 * @param parentNBT
	 */
	public void readPropertiesFromNBT(NBTTagCompound parentNBT) {
		try {
			// read the custom name
			if (parentNBT.hasKey("CustomName", 8)) {
				this.customName = parentNBT.getString("CustomName");
			}
			// read the facing
			if (parentNBT.hasKey("facing")) {
//				Treasure.logger.debug("Has 'facing' key -> {}", parentNBT.getInteger("facing"));
				this.setFacing(parentNBT.getInteger("facing"));
			}
		} catch (Exception e) {
			Treasure.logger.error("Error reading Properties from NBT:", e);
		}
	}

	/**
	 * 
	 */
	@Override
	public void readFromNBT(NBTTagCompound parentNBT) {
		super.readFromNBT(parentNBT);

		try {
			readLockStatesFromNBT(parentNBT);
			readInventoryFromNBT(parentNBT);
			readPropertiesFromNBT(parentNBT);
		} catch (Exception e) {
			Treasure.logger.error("Error reading to NBT:", e);
		}
	}

	/*
	 * This method does NOT call the super's readFromNBT()
	 */
	public void readFromItemStackNBT(NBTTagCompound nbt) {
		try {
			readLockStatesFromNBT(nbt);
			readInventoryFromNBT(nbt);
			readPropertiesFromNBT(nbt);
		} catch (Exception e) {
			Treasure.logger.error("Error reading to NBT:", e);
		}
	}

	@Override
	@Nullable
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(this.pos, 3, this.getUpdateTag());
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return this.writeToNBT(new NBTTagCompound());
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		super.onDataPacket(net, pkt);
		handleUpdateTag(pkt.getNbtCompound());
	}

	/**
	 * This controls whether the tile entity gets replaced whenever the block state
	 * is changed. Normally only want this when block actually is replaced. NOTE
	 * this method is very important!
	 */
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
//		Treasure.logger.debug("ShouldRefresh:" + (oldState.getBlock() != newState.getBlock()));
		return oldState.getBlock() != newState.getBlock();

	}

	/**
	 * Sync client and server states
	 */
	public void sendUpdates() {
		world.markBlockRangeForRenderUpdate(pos, pos);
		world.notifyBlockUpdate(pos, getState(), getState(), 3);
		world.scheduleBlockUpdate(pos, this.getBlockType(), 0, 0);
		markDirty();
	}

	/*
	 * Get the corresponding block state
	 */
	private IBlockState getState() {
		return world.getBlockState(pos);
	}

	////////// Custom property modifiers //////////////////////
	/**
	 * @return the lockStates
	 */
	public List<LockState> getLockStates() {
		return lockStates;
	}

	/**
	 * @param lockStates the lockStates to set
	 */
	public void setLockStates(List<LockState> lockStates) {
		this.lockStates = lockStates;
	}

	/**
	 * 
	 * @return
	 */
	public boolean hasLocks() {
		// TODO TEMP do this for now. should have another property numActiveLocks so
		// that the renderer doesn't keep calling this
		if (getLockStates() == null || getLockStates().isEmpty())
			return false;
		for (LockState state : getLockStates()) {
			if (state.getLock() != null)
				return true;
		}
		return false;
	}

	/**
	 * Get the formatted ChatComponent that will be used for the sender's username
	 * in chat
	 */
	@Override
	public ITextComponent getDisplayName() {
		return (ITextComponent) (this.hasCustomName() ? new TextComponentString(this.getName())
				: new TextComponentTranslation(this.getName(), new Object[0]));
	}

	///////////// IInventory Methods ///////////////////////

	@Override
	public String getName() {
		return this.hasCustomName() ? this.getCustomName() : "container.chest";
	}

	/**
	 * 
	 */
	@Override
	public boolean hasCustomName() {
		return getCustomName() != null && getCustomName().length() > 0;
	}

	/**
	 * 
	 */
	@Override
	public int getSizeInventory() {
		if (!hasLocks()) {
			return getNumberOfSlots();
		}
		return 0;
	}

	/**
	 * 
	 */
	@Override
	public boolean isEmpty() {
		for (ItemStack itemstack : this.items) {
			if (!itemstack.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 */
	@Override
	public ItemStack getStackInSlot(int index) {
		if (!hasLocks()) {
			return getItems().get(index);
		}
		return ItemStack.EMPTY;
	}

	/**
	 * 
	 */
	@Override
	public ItemStack decrStackSize(int index, int count) {
		ItemStack itemStack = ItemStack.EMPTY;
		if (!hasLocks()) {
			itemStack = ItemStackHelper.getAndSplit(this.getItems(), index, count);
			if (!itemStack.isEmpty()) {
				this.markDirty();
			}
		}
		return itemStack;
	}

	/**
	 * 
	 */
	@Override
	public ItemStack removeStackFromSlot(int index) {
		if (hasLocks()) {
			return ItemStack.EMPTY;
		}
		return ItemStackHelper.getAndRemove(this.getItems(), index);
	}

	/**
	 * 
	 */
	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		if (!hasLocks()) {
			this.getItems().set(index, stack);
			if (stack.getCount() > this.getInventoryStackLimit()) {
				stack.setCount(this.getInventoryStackLimit());
			}
			this.markDirty();
		}
	}

	/**
	 * Returns the maximum stack size for a inventory slot. Seems to always be 64,
	 * possibly will be extended.
	 */
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	/**
	 * 
	 */
	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		if (this.world.getTileEntity(this.pos) != this) {
			return false;
		} else {
			boolean isUsable = player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D,
					(double) this.pos.getZ() + 0.5D) <= 64.0D;
			return isUsable;
		}
	}

	/**
	 * 
	 */
	@Override
	public void openInventory(EntityPlayer player) {
		if (hasLocks()) {
			return;
		}
		if (!player.isSpectator()) {
			if (this.numPlayersUsing < 0) {
				this.numPlayersUsing = 0;
			}
			++this.numPlayersUsing;
			Treasure.logger.debug("Incremented numPlayersUsing to:" + numPlayersUsing);
			this.world.addBlockEvent(this.pos, this.getBlockType(), 1, this.numPlayersUsing);
			this.world.notifyNeighborsOfStateChange(this.pos, this.getBlockType(), false);
		}
	}

	/**
	 * 
	 */
	@Override
	public void closeInventory(EntityPlayer player) {
		if (!player.isSpectator() && this.getBlockType() instanceof TreasureChestBlock) {
			--this.numPlayersUsing;
			this.world.addBlockEvent(this.pos, this.getBlockType(), 1, this.numPlayersUsing);
			this.world.notifyNeighborsOfStateChange(this.pos, this.getBlockType(), false);
		}
	}

	/**
	 * Returns true if automation is allowed to insert the given stack (ignoring
	 * stack size) into the given slot. For guis use Slot.isItemValid
	 */
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return true;
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {
	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
		this.getItems().clear();
	}

	//////////// End of IInventory Methods ///////////////

	/**
	 * @return the customName
	 */
	public String getCustomName() {
		return customName;
	}

	/**
	 * @param customName the customName to set
	 */
	public void setCustomName(String customName) {
		this.customName = customName;
	}

	/**
	 * @return the numberOfSlots
	 */
	public int getNumberOfSlots() {
		return numberOfSlots;
	}

	/**
	 * @param numberOfSlots the numberOfSlots to set
	 */
	public void setNumberOfSlots(int numberOfSlots) {
		this.numberOfSlots = numberOfSlots;
	}

	/**
	 * @return the items
	 */
	public NonNullList<ItemStack> getItems() {
		return items;
	}

	/**
	 * @param items the items to set
	 */
	public void setItems(NonNullList<ItemStack> chestContents) {
		this.items = chestContents;
	}

	public int getFacing() {
		return facing;
	}

	public void setFacing(int facing) {
		this.facing = facing;
	}

}
