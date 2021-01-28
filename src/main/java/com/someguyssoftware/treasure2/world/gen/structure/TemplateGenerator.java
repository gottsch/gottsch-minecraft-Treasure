/**
 * 
 */
package com.someguyssoftware.treasure2.world.gen.structure;

import java.util.Map.Entry;
import java.util.Random;

import com.someguyssoftware.gottschcore.spatial.Coords;
import com.someguyssoftware.gottschcore.spatial.ICoords;
import com.someguyssoftware.gottschcore.world.gen.structure.BlockContext;
import com.someguyssoftware.gottschcore.world.gen.structure.GottschTemplate;
import com.someguyssoftware.gottschcore.world.gen.structure.IDecayProcessor;
import com.someguyssoftware.gottschcore.world.gen.structure.PlacementSettings;
import com.someguyssoftware.gottschcore.world.gen.structure.StructureMarkers;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.TreasureBlocks;
import com.someguyssoftware.treasure2.generator.GeneratorResult;
import com.someguyssoftware.treasure2.generator.TemplateGeneratorData;
import com.someguyssoftware.treasure2.meta.StructureMeta;
import com.someguyssoftware.treasure2.registry.TreasureMetaRegistry;
import com.someguyssoftware.treasure2.registry.TreasureTemplateRegistry;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraftforge.registries.ForgeRegistries;

// TODO getMarkerBlock should be in TemplateGenerator as well (passed in)
// TODO move TemplateGenerator to world.gen.structure (in gottschcore)
// TODO structure gen should probably pass in the replacement map

/**
 * 
 * @author Mark Gottschling on Jan 24, 2019
 *
 */
public class TemplateGenerator implements ITemplateGenerator<GeneratorResult<TemplateGeneratorData>> {
	// facing property of a vanilla chest
	private static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
	private static final EnumProperty<Direction> CHEST_FACING = DirectionProperty.create("facing", Direction.class);

	private Block nullBlock;

	// TODO constructor should probably take in null block or list of null blocks,
	// markersblocks, and replacement blocks
	public TemplateGenerator() {
		// use the default null block
		setNullBlock(TreasureTemplateRegistry.getMarkerBlock(StructureMarkers.NULL));
	}

	@Override
	public GeneratorResult<TemplateGeneratorData> generate(IWorld world, Random random, TemplateHolder templateHolder,
			PlacementSettings placement, ICoords coords) {
		return generate(world, random, null, templateHolder, placement, coords);
	}

	/**
	 * 
	 */
	@Override
	public GeneratorResult<TemplateGeneratorData> generate(IWorld world, Random random, IDecayProcessor decayProcessor,
			TemplateHolder templateHolder, PlacementSettings placement, ICoords coords) {

		GeneratorResult<TemplateGeneratorData> result = new GeneratorResult<>(TemplateGeneratorData.class);

		GottschTemplate template = (GottschTemplate) templateHolder.getTemplate();
		Treasure.LOGGER.debug("template size -> {}", template.getSize());
		// get the meta
		StructureMeta meta = (StructureMeta) TreasureMetaRegistry.get(templateHolder.getMetaLocation().toString());
		if (meta == null) {
			Treasure.LOGGER.debug("Unable to locate meta data for template -> {}", templateHolder.getLocation());
			return result.fail();
		}
		Treasure.LOGGER.debug("meta -> {}", meta);

		// if the meta provides a null block, use it
		if (meta.getNullBlockName() != null && !meta.getNullBlockName().equals("")) {
			Treasure.LOGGER.debug("setting the null block to -> {}", meta.getNullBlockName());
//			setNullBlock(Block.getBlockFromName(meta.getNullBlockName()));
			setNullBlock(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(meta.getNullBlockName())));
		}
		Treasure.LOGGER.debug("Using null block -> {}", getNullBlock().getRegistryName());
		
		// find the offset block
		int offset = 0;
		ICoords offsetCoords = null;
		if (meta.getOffset() != null) {
			// NOTE going to need to negate meta offset since a negative value will be
			// provided for downward movement, whereas
			// an offset derived from a template will always be positive and thus is negated
			// later to correct the positioning.
			offsetCoords = new Coords(0, -meta.getOffset().getY(), 0);
			Treasure.LOGGER.debug("Using meta offset coords -> {}", offsetCoords);
		} else {
			offsetCoords = template.findCoords(random, TreasureTemplateRegistry.getMarkerBlock(StructureMarkers.OFFSET));
		}

		if (offsetCoords != null) {
			offset = -offsetCoords.getY();
		}

		// update the spawn coords with the offset
		ICoords spawnCoords = coords.add(0, offset, 0);
//		Treasure.LOGGER.debug("spawn coords with offset -> {}", spawnCoords);
		
		// generate the structure
		if (decayProcessor == null) {
			Treasure.LOGGER.debug("no decay processor found.");
			template.addBlocksToWorld(world, spawnCoords.toPos(), placement, 
					getNullBlock(), TreasureTemplateRegistry.getTemplateManager().getReplacementMap(), 3);
		} else {
			decayProcessor.setDecayStartY(Math.abs(offset));
			template.addBlocksToWorld(world, spawnCoords.toPos(), placement, 
					decayProcessor, getNullBlock(), TreasureTemplateRegistry.getTemplateManager().getReplacementMap(), 3);
		}

		// process all strcture markers, positioning absolutely
		for (Entry<Block, BlockContext> entry : template.getTagBlockMap().entries()) {
			BlockContext context = getAbsoluteTransformedContext(entry.getValue(), spawnCoords, placement);
			result.getData().getMap().put(entry.getKey(), context);
			Treasure.LOGGER.debug("new: adding to structure info absoluted transformed coords -> {} : {}",
					entry.getKey().getRegistryName(), context.getCoords().toShortString());
		}

		// get the transformed size
		BlockPos transformedSize = template.transformedSize(placement.getRotation());
//		Treasure.LOGGER.debug("transformed size -> {}", transformedSize.toString());

		// calculate the new spawn coords - that includes the rotation, and negates the
		// Y offset
		spawnCoords = getTransformedSpawnCoords(spawnCoords, new Coords(transformedSize), placement).add(0, -offset, 0);

//		Treasure.LOGGER.debug("spawn coords after rotation -> " + spawnCoords);
		// update result data
		result.getData().setSpawnCoords(spawnCoords);
		result.getData().setSize(new Coords(transformedSize));

		return result.success();
	}

	/**
	 * 
	 * @param contextIn
	 * @param spawnCoords
	 * @param placement
	 * @return
	 */
	private BlockContext getAbsoluteTransformedContext(BlockContext contextIn, ICoords spawnCoords,
			PlacementSettings placement) {
		BlockContext context = new BlockContext();

		// get the absolute coords of chest
		ICoords coords = new Coords(GottschTemplate.transformedCoords(placement, contextIn.getCoords()));
		coords = spawnCoords.add(coords);
		context.setCoords(coords);

		// get the block state of the chest
		BlockState chestState = contextIn.getState();
		chestState = chestState.mirror(placement.getMirror());
		chestState = chestState.rotate(placement.getRotation());
		if (chestState.getProperties().contains(FACING)) {
			BlockState modState = TreasureBlocks.WOOD_CHEST.getDefaultState().with(CHEST_FACING,
					(Direction) chestState.get(FACING));
			context.setState(modState);
		} else {
			context.setState(contextIn.getState());
		}
		return context;
	}

	/**
	 * 
	 * @param coords
	 * @param size
	 * @param placement
	 * @return
	 */
	public ICoords getTransformedSpawnCoords(final ICoords coords, final ICoords size,
			final PlacementSettings placement) {

		ICoords spawnCoords = null;
		int x = 0;
		int z = 0;
		switch (placement.getRotation()) {
		case NONE:
			x = coords.getX();
			z = coords.getZ();
			break;
		case CLOCKWISE_90:
			x = coords.getX() - (size.getZ() - 1);
			z = coords.getZ();
			break;
		case CLOCKWISE_180:
			x = coords.getX() - (size.getX() - 1);
			z = coords.getZ() - (size.getZ() - 1);
			break;
		case COUNTERCLOCKWISE_90:
			x = coords.getX();
			z = coords.getZ() - (size.getX() - 1);
			break;
		default:
			break;
		}
		spawnCoords = new Coords(x, coords.getY(), z);
		return spawnCoords;
	}

	@Override
	public Block getNullBlock() {
		if (nullBlock == null) {
			nullBlock = TreasureTemplateRegistry.getMarkerBlock(StructureMarkers.NULL);
		}
		return nullBlock;
	}

	@Override
	public void setNullBlock(Block nullBlock) {
		this.nullBlock = nullBlock;
	}

}