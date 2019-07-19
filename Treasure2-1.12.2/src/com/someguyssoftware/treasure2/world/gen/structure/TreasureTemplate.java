package com.someguyssoftware.treasure2.world.gen.structure;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.someguyssoftware.gottschcore.positional.Coords;
import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityStructure;
import net.minecraft.util.Mirror;
import net.minecraft.util.ObjectIntIdentityMap;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.template.BlockRotationProcessor;
import net.minecraft.world.gen.structure.template.ITemplateProcessor;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;

public class TreasureTemplate extends Template {
	/** blocks in the structure */
	private final List<TreasureTemplate.BlockInfo> blocks = Lists.<TreasureTemplate.BlockInfo>newArrayList();
	/** entities in the structure */
	private final List<TreasureTemplate.EntityInfo> entities = Lists.<TreasureTemplate.EntityInfo>newArrayList();
	/** size of the structure */
	private BlockPos size = BlockPos.ORIGIN;

	/*
	 * A map of block undergroundLocations of all the specials within the template.
	 */
	private final Multimap<Block, ICoords> map = ArrayListMultimap.create();
	
	@Override
	public BlockPos getSize() {
		return this.size;
	}

	/**
	 * 
	 */
	public Map<BlockPos, String> getDataBlocks(BlockPos pos, PlacementSettings placementIn) {
		Map<BlockPos, String> map = Maps.<BlockPos, String>newHashMap();
		StructureBoundingBox structureboundingbox = placementIn.getBoundingBox();

		for (TreasureTemplate.BlockInfo template$blockinfo : this.blocks) {
			BlockPos blockpos = transformedBlockPos(placementIn, template$blockinfo.pos).add(pos);

			if (structureboundingbox == null || structureboundingbox.isVecInside(blockpos)) {
				IBlockState iblockstate = template$blockinfo.blockState;

				if (iblockstate.getBlock() == Blocks.STRUCTURE_BLOCK && template$blockinfo.tileentityData != null) {
					TileEntityStructure.Mode tileentitystructure$mode = TileEntityStructure.Mode.valueOf(template$blockinfo.tileentityData.getString("mode"));

					if (tileentitystructure$mode == TileEntityStructure.Mode.DATA) {
						map.put(blockpos, template$blockinfo.tileentityData.getString("metadata"));
					}
				}
			}
		}

		return map;
	}


	/**
	 * This takes the data stored in this instance and puts them into the world.
	 * 
	 * @param worldIn
	 *            The world to use
	 * @param pos
	 *            The origin position for the structure
	 * @param placementIn
	 *            Placement settings to use
	 */
	public void addBlocksToWorld(World worldIn, BlockPos pos, PlacementSettings placementIn) {
		this.addBlocksToWorld(worldIn, pos, new BlockRotationProcessor(pos, placementIn), placementIn, 2);
	}

	/**
	 * Adds blocks and entities from this structure to the given world.
	 * 
	 * @param worldIn
	 *            The world to use
	 * @param pos
	 *            The origin position for the structure
	 * @param placementIn
	 *            Placement settings to use
	 * @param flags
	 *            Flags to pass to
	 *            {@link World#setBlockState(BlockPos, IBlockState, int)}
	 */
	@Override
	public void addBlocksToWorld(World worldIn, BlockPos pos, PlacementSettings placementIn, int flags) {
		this.addBlocksToWorld(worldIn, pos, new BlockRotationProcessor(pos, placementIn), placementIn, Blocks.BEDROCK, flags);
	}
	
	/**
	 * 
	 * @param worldIn
	 * @param pos
	 * @param placementIn
	 * @param NULL_BLOCK
	 * @param flags
	 */
	public void addBlocksToWorld(World worldIn, BlockPos pos, PlacementSettings placementIn, final Block NULL_BLOCK, int flags) {
		this.addBlocksToWorld(worldIn, pos, new BlockRotationProcessor(pos, placementIn), placementIn, NULL_BLOCK, flags);
	}

	/**
	 * Adds blocks and entities from this structure to the given world.
	 * 
	 * @param worldIn
	 *            The world to use
	 * @param pos
	 *            The origin position for the structure
	 * @param templateProcessor
	 *            The template processor to use
	 * @param placementIn
	 *            Placement settings to use
	 * @param flags
	 *            Flags to pass to
	 *            {@link World#setBlockState(BlockPos, IBlockState, int)}
	 */
	public void addBlocksToWorld(World worldIn, BlockPos pos, @Nullable ITemplateProcessor templateProcessor, PlacementSettings placementIn, final Block NULL_BLOCK, int flags) {
		if ((!this.blocks.isEmpty() || !placementIn.getIgnoreEntities() && !this.entities.isEmpty()) && this.size.getX() >= 1 && this.size.getY() >= 1 && this.size.getZ() >= 1) {
			Block replacedBlock = placementIn.getReplacedBlock();
			StructureBoundingBox structureboundingbox = placementIn.getBoundingBox();

			for (TreasureTemplate.BlockInfo blockInfo : this.blocks) {
				BlockPos blockpos = transformedBlockPos(placementIn, blockInfo.pos).add(pos);
				// Forge: skip processing blocks outside BB to prevent cascading worldgen issues
				if (structureboundingbox != null && !structureboundingbox.isVecInside(blockpos))
					continue;
				TreasureTemplate.BlockInfo processedBlockInfo = templateProcessor != null ? templateProcessor.processBlock(worldIn, blockpos, blockInfo) : blockInfo;

				if (processedBlockInfo != null) {
					Block processedBlock = processedBlockInfo.blockState.getBlock();

					if ((replacedBlock == null || replacedBlock != processedBlock) && (!placementIn.getIgnoreStructureBlock() || processedBlock != Blocks.STRUCTURE_BLOCK)
							&& (structureboundingbox == null || structureboundingbox.isVecInside(blockpos))
							&& processedBlock != NULL_BLOCK
							) {
						IBlockState iblockstate = processedBlockInfo.blockState.withMirror(placementIn.getMirror());
						IBlockState iblockstate1 = iblockstate.withRotation(placementIn.getRotation());

						if (processedBlockInfo.tileentityData != null) {
							TileEntity tileentity = worldIn.getTileEntity(blockpos);

							if (tileentity != null) {
								if (tileentity instanceof IInventory) {
									((IInventory) tileentity).clear();
								}

								worldIn.setBlockState(blockpos, Blocks.BARRIER.getDefaultState(), 4);
							}
						}

						if (worldIn.setBlockState(blockpos, iblockstate1, flags) && processedBlockInfo.tileentityData != null) {
							TileEntity tileentity2 = worldIn.getTileEntity(blockpos);

							if (tileentity2 != null) {
								processedBlockInfo.tileentityData.setInteger("x", blockpos.getX());
								processedBlockInfo.tileentityData.setInteger("y", blockpos.getY());
								processedBlockInfo.tileentityData.setInteger("z", blockpos.getZ());
								tileentity2.readFromNBT(processedBlockInfo.tileentityData);
								tileentity2.mirror(placementIn.getMirror());
								tileentity2.rotate(placementIn.getRotation());
							}
						}
					}
				}
			}

			for (TreasureTemplate.BlockInfo template$blockinfo2 : this.blocks) {
				if (replacedBlock == null || replacedBlock != template$blockinfo2.blockState.getBlock()) {
					BlockPos blockpos1 = transformedBlockPos(placementIn, template$blockinfo2.pos).add(pos);

					if (structureboundingbox == null || structureboundingbox.isVecInside(blockpos1)) {
						worldIn.notifyNeighborsRespectDebug(blockpos1, template$blockinfo2.blockState.getBlock(), false);

						if (template$blockinfo2.tileentityData != null) {
							TileEntity tileentity1 = worldIn.getTileEntity(blockpos1);

							if (tileentity1 != null) {
								tileentity1.markDirty();
							}
						}
					}
				}
			}

			if (!placementIn.getIgnoreEntities()) {
				this.addEntitiesToWorld(worldIn, pos, placementIn.getMirror(), placementIn.getRotation(), structureboundingbox);
			}
		}
	}

	/**
	 * 
	 * @param worldIn
	 * @param pos
	 * @param mirrorIn
	 * @param rotationIn
	 * @param aabb
	 */
	private void addEntitiesToWorld(World worldIn, BlockPos pos, Mirror mirrorIn, Rotation rotationIn, @Nullable StructureBoundingBox aabb) {
		PlacementSettings placement = new PlacementSettings();
		placement.setRotation(rotationIn).setMirror(mirrorIn);
		
		for (TreasureTemplate.EntityInfo template$entityinfo : this.entities) {
			BlockPos blockpos = transformedBlockPos(placement, template$entityinfo.blockPos).add(pos);

			if (aabb == null || aabb.isVecInside(blockpos)) {
				NBTTagCompound nbttagcompound = template$entityinfo.entityData;
				Vec3d vec3d = transformedVec3d(template$entityinfo.pos, mirrorIn, rotationIn);
				Vec3d vec3d1 = vec3d.addVector((double) pos.getX(), (double) pos.getY(), (double) pos.getZ());
				NBTTagList nbttaglist = new NBTTagList();
				nbttaglist.appendTag(new NBTTagDouble(vec3d1.x));
				nbttaglist.appendTag(new NBTTagDouble(vec3d1.y));
				nbttaglist.appendTag(new NBTTagDouble(vec3d1.z));
				nbttagcompound.setTag("Pos", nbttaglist);
				nbttagcompound.setUniqueId("UUID", UUID.randomUUID());
				Entity entity;

				try {
					entity = EntityList.createEntityFromNBT(nbttagcompound, worldIn);
				} catch (Exception var15) {
					entity = null;
				}

				if (entity != null) {
					float f = entity.getMirroredYaw(mirrorIn);
					f = f + (entity.rotationYaw - entity.getRotatedYaw(rotationIn));
					entity.setLocationAndAngles(vec3d1.x, vec3d1.y, vec3d1.z, f, entity.rotationPitch);
					worldIn.spawnEntity(entity);
				}
			}
		}
	}

	/**
	 * 
	 */
	@Override
	public BlockPos transformedSize(Rotation rotationIn) {
		switch (rotationIn) {
		case COUNTERCLOCKWISE_90:
		case CLOCKWISE_90:
			return new BlockPos(this.getSize().getZ(), this.getSize().getY(), this.getSize().getX());
		default:
			return this.getSize();
		}
	}
    
	/**
	 * Wrapper for transformedBlockPos()
	 * @param placementIn
	 * @param coords
	 * @return
	 */
    public static ICoords transformedCoords(PlacementSettings placement, ICoords coords) {
        return new Coords(transformedBlockPos(placement, coords.toPos()));
    }

    /**
     * 
     * @param vec
     * @param mirrorIn
     * @param rotationIn
     * @return
     */
	private static Vec3d transformedVec3d(Vec3d vec, Mirror mirrorIn, Rotation rotationIn) {
		double d0 = vec.x;
		double d1 = vec.y;
		double d2 = vec.z;
		boolean flag = true;

		switch (mirrorIn) {
		case LEFT_RIGHT:
			d2 = 1.0D - d2;
			break;
		case FRONT_BACK:
			d0 = 1.0D - d0;
			break;
		default:
			flag = false;
		}

		switch (rotationIn) {
		case COUNTERCLOCKWISE_90:
			return new Vec3d(d2, d1, 1.0D - d0);
		case CLOCKWISE_90:
			return new Vec3d(1.0D - d2, d1, d0);
		case CLOCKWISE_180:
			return new Vec3d(1.0D - d0, d1, 1.0D - d2);
		default:
			return flag ? new Vec3d(d0, d1, d2) : vec;
		}
	}

	/**
	 * 
	 */
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		TreasureTemplate.BasicPalette template$basicpalette = new TreasureTemplate.BasicPalette();
		NBTTagList nbttaglist = new NBTTagList();

		for (TreasureTemplate.BlockInfo template$blockinfo : this.blocks) {
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setTag("pos", this.writeInts(template$blockinfo.pos.getX(), template$blockinfo.pos.getY(), template$blockinfo.pos.getZ()));
			nbttagcompound.setInteger("state", template$basicpalette.idFor(template$blockinfo.blockState));

			if (template$blockinfo.tileentityData != null) {
				nbttagcompound.setTag("nbt", template$blockinfo.tileentityData);
			}

			nbttaglist.appendTag(nbttagcompound);
		}

		NBTTagList nbttaglist1 = new NBTTagList();

		for (TreasureTemplate.EntityInfo template$entityinfo : this.entities) {
			NBTTagCompound nbttagcompound1 = new NBTTagCompound();
			nbttagcompound1.setTag("pos", this.writeDoubles(template$entityinfo.pos.x, template$entityinfo.pos.y, template$entityinfo.pos.z));
			nbttagcompound1.setTag("blockPos", this.writeInts(template$entityinfo.blockPos.getX(), template$entityinfo.blockPos.getY(), template$entityinfo.blockPos.getZ()));

			if (template$entityinfo.entityData != null) {
				nbttagcompound1.setTag("nbt", template$entityinfo.entityData);
			}

			nbttaglist1.appendTag(nbttagcompound1);
		}

		NBTTagList nbttaglist2 = new NBTTagList();

		for (IBlockState iblockstate : template$basicpalette) {
			nbttaglist2.appendTag(NBTUtil.writeBlockState(new NBTTagCompound(), iblockstate));
		}

		net.minecraftforge.fml.common.FMLCommonHandler.instance().getDataFixer().writeVersionData(nbt); // Moved up for MC updating reasons.
		nbt.setTag("palette", nbttaglist2);
		nbt.setTag("blocks", nbttaglist);
		nbt.setTag("entities", nbttaglist1);
		nbt.setTag("size", this.writeInts(this.size.getX(), this.size.getY(), this.size.getZ()));
		nbt.setString("author", getAuthor());
		nbt.setInteger("DataVersion", 1343);
		return nbt;
	}

	/**
	 * 
	 */
	/**
	 * 
	 * @param compound
	 * @param scanForBlocks
	 */
	public void read(NBTTagCompound compound, List<Block> scanForBlocks) {
		Treasure.logger.debug("made it to template.read()");
		this.blocks.clear();
		this.entities.clear();
		NBTTagList nbttaglist = compound.getTagList("size", 3);
		this.size = new BlockPos(nbttaglist.getIntAt(0), nbttaglist.getIntAt(1), nbttaglist.getIntAt(2));
		setAuthor(compound.getString("author"));
		TreasureTemplate.BasicPalette template$basicpalette = new TreasureTemplate.BasicPalette();
		NBTTagList nbttaglist1 = compound.getTagList("palette", 10);

		for (int i = 0; i < nbttaglist1.tagCount(); ++i) {
			template$basicpalette.addMapping(NBTUtil.readBlockState(nbttaglist1.getCompoundTagAt(i)), i);
		}

		NBTTagList blocks = compound.getTagList("blocks", 10);

		for (int blockIndex = 0; blockIndex < blocks.tagCount(); ++blockIndex) {
			NBTTagCompound nbttagcompound = blocks.getCompoundTagAt(blockIndex);
			NBTTagList nbttaglist2 = nbttagcompound.getTagList("pos", 3);
			BlockPos blockPos = new BlockPos(nbttaglist2.getIntAt(0), nbttaglist2.getIntAt(1), nbttaglist2.getIntAt(2));
			IBlockState blockState = template$basicpalette.stateFor(nbttagcompound.getInteger("state"));
			NBTTagCompound nbttagcompound1;

			if (nbttagcompound.hasKey("nbt")) {
				nbttagcompound1 = nbttagcompound.getCompoundTag("nbt");
			} else {
				nbttagcompound1 = null;
			}
			this.blocks.add(new TreasureTemplate.BlockInfo(blockPos, blockState, nbttagcompound1));
			
			// check if a scan block
			Block block = blockState.getBlock();
			if (block != Blocks.AIR && scanForBlocks.contains(block)) {
				// add pos to map
				Treasure.logger.debug("template map adding block -> {} with pos -> {}", block.getRegistryName(), blockPos);
				map.put(block, new Coords(blockPos));
			}
		}

		NBTTagList nbttaglist4 = compound.getTagList("entities", 10);

		for (int k = 0; k < nbttaglist4.tagCount(); ++k) {
			NBTTagCompound nbttagcompound3 = nbttaglist4.getCompoundTagAt(k);
			NBTTagList nbttaglist5 = nbttagcompound3.getTagList("pos", 6);
			Vec3d vec3d = new Vec3d(nbttaglist5.getDoubleAt(0), nbttaglist5.getDoubleAt(1), nbttaglist5.getDoubleAt(2));
			NBTTagList nbttaglist6 = nbttagcompound3.getTagList("blockPos", 3);
			BlockPos blockpos1 = new BlockPos(nbttaglist6.getIntAt(0), nbttaglist6.getIntAt(1), nbttaglist6.getIntAt(2));

			if (nbttagcompound3.hasKey("nbt")) {
				NBTTagCompound nbttagcompound2 = nbttagcompound3.getCompoundTag("nbt");
				this.entities.add(new TreasureTemplate.EntityInfo(vec3d, blockpos1, nbttagcompound2));
			}
		}
	}

	private NBTTagList writeInts(int... values) {
		NBTTagList nbttaglist = new NBTTagList();

		for (int i : values) {
			nbttaglist.appendTag(new NBTTagInt(i));
		}

		return nbttaglist;
	}

	private NBTTagList writeDoubles(double... values) {
		NBTTagList nbttaglist = new NBTTagList();

		for (double d0 : values) {
			nbttaglist.appendTag(new NBTTagDouble(d0));
		}

		return nbttaglist;
	}

	static class BasicPalette implements Iterable<IBlockState> {
		public static final IBlockState DEFAULT_BLOCK_STATE = Blocks.AIR.getDefaultState();
		final ObjectIntIdentityMap<IBlockState> ids;
		private int lastId;

		private BasicPalette() {
			this.ids = new ObjectIntIdentityMap<IBlockState>(16);
		}

		public int idFor(IBlockState state) {
			int i = this.ids.get(state);

			if (i == -1) {
				i = this.lastId++;
				this.ids.put(state, i);
			}

			return i;
		}

		@Nullable
		public IBlockState stateFor(int id) {
			IBlockState iblockstate = this.ids.getByValue(id);
			return iblockstate == null ? DEFAULT_BLOCK_STATE : iblockstate;
		}

		public Iterator<IBlockState> iterator() {
			return this.ids.iterator();
		}

		public void addMapping(IBlockState p_189956_1_, int p_189956_2_) {
			this.ids.put(p_189956_1_, p_189956_2_);
		}
	}

	/**
	 * @return the map
	 */
	public Multimap<Block, ICoords> getMap() {
		return map;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(BlockPos size) {
		this.size = size;
	}

	/**
	 * 
	 * @param random
	 * @param findBlock
	 * @return
	 */
	public ICoords findCoords(Random random, Block findBlock) {
		ICoords coords = null; // TODO should this be an empty object
		List<ICoords> list = (List<ICoords>) getMap().get(findBlock);
		if (list.isEmpty()) return new Coords(0, 0, 0);
		if (list.size() == 1) coords = list.get(0);
		else coords = list.get(random.nextInt(list.size()));
		return coords;
	}
	
	/**
	 * 
	 * @param findBlock
	 * @return
	 */
	public List<ICoords> findCoords(Block findBlock) {
		List<ICoords> list = (List<ICoords>) getMap().get(findBlock);
		return list;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<ICoords> getMapCoords() {
		List<ICoords> coords = getMap().values().stream().collect(Collectors.toList());
		return coords;
	}
}