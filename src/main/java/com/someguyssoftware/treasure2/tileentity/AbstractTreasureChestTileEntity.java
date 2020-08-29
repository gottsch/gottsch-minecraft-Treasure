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
import com.someguyssoftware.treasure2.block.AbstractChestBlock;
import com.someguyssoftware.treasure2.chest.ChestSlotCount;
import com.someguyssoftware.treasure2.inventory.ITreasureContainer;
import com.someguyssoftware.treasure2.lock.LockState;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.state.properties.ChestType;
import net.minecraft.tileentity.IChestLid;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.INameable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.Constants;
/**
 * 
 * @author Mark Gottschling onDec 22, 2017
 *
 */
public abstract class AbstractTreasureChestTileEntity extends AbstractModTileEntity implements IChestLid, ITickableTileEntity, IInventory, INamedContainerProvider, INameable {
	/*
	 * A list of lockStates the chest has. The list should be the size of the max
	 * allowed for the chestType.
	 */
	private List<LockState> lockStates;

	/*
	 * The FACING index value of the TreasureChestBlock
	 */
	private Direction facing;

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
//	private int numberOfSlots = 27; // default size
	private NonNullList<ItemStack> items = NonNullList.<ItemStack>withSize(getNumberOfSlots(), ItemStack.EMPTY);
	private ITextComponent customName;

	/**
	 * 
	 */
	public AbstractTreasureChestTileEntity(TileEntityType<?> type) {
		super(type);
		setFacing(Direction.NORTH.getIndex());
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

		// NOTE in 1.15.2 this block is replaced by calculatePlayersUsingSync()
		if (WorldInfo.isServerSide(getWorld()) && this.numPlayersUsing != 0
				&& (this.ticksSinceSync + i + j + k) % 200 == 0) {
			this.numPlayersUsing = 0;
			float f = 5.0F;

			for (PlayerEntity player : getWorld().getEntitiesWithinAABB(PlayerEntity.class,
					new AxisAlignedBB((double) ((float) i - 5.0F), (double) ((float) j - 5.0F),
							(double) ((float) k - 5.0F), (double) ((float) (i + 1) + 5.0F),
							(double) ((float) (j + 1) + 5.0F), (double) ((float) (k + 1) + 5.0F)))) {

				if (player.openContainer instanceof ITreasureContainer) {
					IInventory inventory = ((ITreasureContainer) player.openContainer).getContents();
					if (inventory == this) {
						++this.numPlayersUsing;
					}
				}
			}
		}

		// TODO checkout the vanilla on angle update -- somehow the renderThread isn't getting updates to the TE.
		this.prevLidAngle = this.lidAngle;
		if (this.numPlayersUsing > 0 && this.lidAngle == 0.0F) {
			this.playSound(SoundEvents.BLOCK_CHEST_OPEN);
		}

//				Treasure.LOGGER.info("test: numPlayers -> {}, previous angle -> {}, new angle -> {}", this.numPlayersUsing, this.prevLidAngle, this.lidAngle);
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
				this.playSound(SoundEvents.BLOCK_CHEST_CLOSE);
			}

			if (this.lidAngle < 0.0F) {
				this.lidAngle = 0.0F;
			}
		}
	}

	protected void playSound(SoundEvent soundIn) {
		double d0 = (double)this.pos.getX() + 0.5D;
		double d1 = (double)this.pos.getY() + 0.5D;
		double d2 = (double)this.pos.getZ() + 0.5D;
		this.world.playSound((PlayerEntity)null, d0, d1, d2, soundIn, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
	}

	/**
	 * 
	 */
	@Override
	public CompoundNBT write(CompoundNBT parentNBT) {
		try {
			parentNBT = super.write(parentNBT);

			// TODO LockSlots don't have a Heading ???
			// write lock states
			writeLockStatesToNBT(parentNBT);

			// write inventory
			writeInventoryToNBT(parentNBT);

			// write custom name
			writePropertiesToNBT(parentNBT);
		} catch (Exception e) {
			Treasure.LOGGER.error("Error writing to NBT:", e);
		}
		return parentNBT;
	}

	/**
	 * 
	 * @param parentNBT
	 * @return
	 */
	public CompoundNBT writeLockStatesToNBT(CompoundNBT parentNBT) {
		try {
			// write lock states
			if (getLockStates() != null && !getLockStates().isEmpty()) {
				ListNBT list = new ListNBT();
				// write custom tile entity properties
				for (LockState state : getLockStates()) {
//					Treasure.LOGGER.info("Writing lock state:" + state);
					CompoundNBT stateNBT = new CompoundNBT();
					state.writeToNBT(stateNBT);
					list.add(stateNBT);
				}
				parentNBT.put("lockStates", list);
			}
		} catch (Exception e) {
			Treasure.LOGGER.error("Error writing LockStates to NBT:", e);
		}
		return parentNBT;
	}

	/**
	 * Write custom properties to NBT
	 * 
	 * @param parentNBT
	 * @return
	 */
	public CompoundNBT writePropertiesToNBT(CompoundNBT parentNBT) {
		try {
			// write custom name
			if (this.hasCustomName()) {
				parentNBT.putString("CustomName", ITextComponent.Serializer.toJson(this.customName));
			}
			// write facing
			parentNBT.putInt("facing", getFacing().getIndex());

		} catch (Exception e) {
			Treasure.LOGGER.error("Error writing Properties to NBT:", e);
		}
		return parentNBT;
	}

	/**
	 * Writes the inventory to NBT
	 * 
	 * @param parentNBT
	 * @return
	 */
	public CompoundNBT writeInventoryToNBT(CompoundNBT parentNBT) {
		try {
			// write inventory
			ItemStackHelper.saveAllItems(parentNBT, this.getItems());
		} catch (Exception e) {
			Treasure.LOGGER.error("Error writing Inventory to NBT:", e);
		}
		return parentNBT;
	}

	/**
	 * 
	 * @param parentNBT
	 */
	public void readInventoryFromNBT(CompoundNBT parentNBT) {
		try {
			// read the inventory
			ItemStackHelper.loadAllItems(parentNBT, this.getItems());
		} catch (Exception e) {
			Treasure.LOGGER.error("Error reading Properties from NBT:", e);
		}
	}

	/**
	 * 
	 * @param parentNBT
	 */
	public void readLockStatesFromNBT(CompoundNBT parentNBT) {
		try {
			// read the lockstates
			if (parentNBT.contains("lockStates")) {
//				Treasure.LOGGER.info("Has lockStates");
				if (this.getLockStates() != null) {
//					Treasure.LOGGER.info("size of internal lockstates:" + this.getLockStates().size());
				} else {
					this.setLockStates(new LinkedList<LockState>());
//					Treasure.LOGGER.info("created lockstates:" + this.getLockStates().size());
				}

				List<LockState> states = new LinkedList<LockState>();
				ListNBT list = parentNBT.getList("lockStates", Constants.NBT.TAG_COMPOUND);
				for (int i = 0; i < list.size(); i++) {
					CompoundNBT c = list.getCompound(i);
					LockState lockState = LockState.readFromNBT(c);
					states.add(lockState.getSlot().getIndex(), lockState);
//					Treasure.LOGGER.info("Read NBT lockstate:" + lockState);
				}
				// update the tile entity
				setLockStates(states);
			}
		} catch (Exception e) {
			Treasure.LOGGER.error("Error reading Lock States from NBT:", e);
		}
	}

	/**
	 * 
	 * @param nbt
	 */
	public void readPropertiesFromNBT(CompoundNBT nbt) {
		try {
			// read the custom name
			if (nbt.contains("CustomName", 8)) {
				this.customName = ITextComponent.Serializer.fromJson(nbt.getString("CustomName"));
			}
			// read the facing
			if (nbt.contains("facing")) {
				this.setFacing(nbt.getInt("facing"));
			}

		} catch (Exception e) {
			Treasure.LOGGER.error("Error reading Properties from NBT:", e);
		}
	}

	/**
	 * 
	 */
	@Override
	public void read(CompoundNBT parentNBT) {
		super.read(parentNBT);

		try {
			readLockStatesFromNBT(parentNBT);
			readInventoryFromNBT(parentNBT);
			readPropertiesFromNBT(parentNBT);
//			Treasure.LOGGER.info("completed read");
		} catch (Exception e) {
			Treasure.LOGGER.error("Error reading to NBT:", e);
		}
	}

	/*
	 * This method does NOT call the super's readFromNBT()
	 */
	public void readFromItemStackNBT(CompoundNBT nbt) {
		try {
			readLockStatesFromNBT(nbt);
			readInventoryFromNBT(nbt);
			readPropertiesFromNBT(nbt);
		} catch (Exception e) {
			Treasure.LOGGER.error("Error reading to NBT:", e);
		}
	}

	@Override
	@Nullable
	public SUpdateTileEntityPacket getUpdatePacket() {
//		Treasure.LOGGER.info("getUpdatePacket is writing packet");
		return new SUpdateTileEntityPacket(this.pos, 3, this.getUpdateTag());
	}

	@Override
	public CompoundNBT getUpdateTag() {
//		Treasure.LOGGER.info("getUpdateTag is writing data");
		return this.write(new CompoundNBT());
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
//		Treasure.LOGGER.info("onDataPacket is reading data");
		//		super.onDataPacket(net, pkt);
		//		handleUpdateTag(pkt.getNbtCompound());
		read(pkt.getNbtCompound());
	}

	/**
	 * This controls whether the tile entity gets replaced whenever the block state
	 * is changed. Normally only want this when block actually is replaced. NOTE
	 * this method is very important!
	 */
	// NOTE logic moved to Block.onReplaced. see BlockFurnace.onReplaced for an example.
	//	@Override
	//	public boolean shouldRefresh(World world, BlockPos pos, BlockState oldState, BlockState newState) {
	//		//		Treasure.LOGGER.debug("ShouldRefresh:" + (oldState.getBlock() != newState.getBlock()));
	//		return oldState.getBlock() != newState.getBlock();
	//
	//	}

	/**
	 * Sync client and server states
	 */
	public void sendUpdates() {
		BlockState blockState = world.getBlockState(pos);
		world.markBlockRangeForRenderUpdate(pos, blockState, blockState);
		world.notifyBlockUpdate(pos, blockState, blockState, 3);
		//		world.scheduleBlockUpdate(pos, this.getType(), 0, 0); ??? replacement
		markDirty();
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
	 * Creates the server container. Wrapped by vanilla createMenu().
	 * @param windowID
	 * @param inventory
	 * @param player
	 * @return
	 */
	abstract public Container createServerContainer(int windowID, PlayerInventory inventory, PlayerEntity player);
	
	protected ITextComponent getDefaultName() {
		return new TranslationTextComponent("container.chest");
	}

	@Override
	public ITextComponent getName() {
		return this.hasCustomName() ? this.getCustomName() : this.getDefaultName();
	}		

	@Override
	public ITextComponent getDisplayName() {
		return this.getName();
	}

	  /**
	   * The name is misleading; createMenu has nothing to do with creating a Screen, it is used to create the Container on the server only
	   * @param windowID
	   * @param playerInventory
	   * @param playerEntity
	   * @return
	   */
	@Nullable
	@Override
	public Container createMenu(int windowID, PlayerInventory playerInventory, PlayerEntity playerEntity) {
	    return createServerContainer(windowID, playerInventory, playerEntity);
	}
	
	///////////// IInventory Methods ///////////////////////

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
	public boolean isUsableByPlayer(PlayerEntity player) {
		if (this.world.getTileEntity(this.pos) != this) {
			return false;
		} else {
			boolean isUsable = player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D,
					(double) this.pos.getZ() + 0.5D) <= 64.0D;
			return isUsable;
		}
	}

	/**
	 * See {@link Block#eventReceived} for more information. This must return true serverside before it is called
	 * clientside.
	 */
	@Override
	public boolean receiveClientEvent(int id, int type) {
		if (id == 1) {
			this.numPlayersUsing = type;
			return true;
		} else {
			return super.receiveClientEvent(id, type);
		}
	}

	/**
	 * 
	 */
	@Override
	public void openInventory(PlayerEntity player) {
//		Treasure.LOGGER.info("opening inventory -> {}", player.getName());

		if (hasLocks()) {
//			Treasure.LOGGER.info("has locks - don't increment num players");
			return;
		}
		if (!player.isSpectator()) {
			if (this.numPlayersUsing < 0) {
				this.numPlayersUsing = 0;
			}
			++this.numPlayersUsing;
			onOpenOrClose();
		}
	}

	/**
	 * 
	 */
	@Override
	public void closeInventory(PlayerEntity player) {
		if (!player.isSpectator()) {
			--this.numPlayersUsing;
			onOpenOrClose();
		}
	}

	protected void onOpenOrClose() {
		Block block = this.getBlockState().getBlock();
		if (block instanceof AbstractChestBlock) {
			this.world.addBlockEvent(this.pos, block, 1, this.numPlayersUsing);
			this.world.notifyNeighborsOfStateChange(this.pos, block);
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
	public void clear() {
		this.getItems().clear();
	}

	//////////// End of IInventory Methods ///////////////

	/**
	 * @return the customName
	 */
	@Override
	public ITextComponent getCustomName() {
		return customName;
	}

	/**
	 * @param customName the customName to set
	 */
	public void setCustomName(ITextComponent customName) {
		this.customName = customName;
	}

	/**
	 * @return the numberOfSlots
	 */
	public int getNumberOfSlots() {
		return ChestSlotCount.STANDARD.getSize();
	}

	/**
	 * @param numberOfSlots the numberOfSlots to set
	 */
//	public void setNumberOfSlots(int numberOfSlots) {
//		this.numberOfSlots = numberOfSlots;
//	}

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

	public Direction getFacing() {
		//	public int getFacing() {
		return facing;
	}

	public void setFacing(Direction facing) {
		this.facing = facing;
	}

	public void setFacing(int facingIndex) {
		//		this.facing = facing;
		this.facing = Direction.byIndex(facingIndex);
	}

	@Override
	public float getLidAngle(float partialTicks) {
		return this.lidAngle;
	}
}
