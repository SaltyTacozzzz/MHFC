package mhfc.net.common.core.registry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import org.apache.commons.lang3.tuple.Pair;

import mhfc.net.MHFCMain;
import mhfc.net.common.block.BlockDiscstone;
import mhfc.net.common.block.BlockIceCrystal;
import mhfc.net.common.block.BlockWyverniaClay;
import mhfc.net.common.block.BlockWyverniaDirt;
import mhfc.net.common.block.BlockWyverniaFlower;
import mhfc.net.common.block.BlockWyverniaGrass;
import mhfc.net.common.block.BlockWyverniaOreBlock;
import mhfc.net.common.block.BlockWyverniaOres;
import mhfc.net.common.block.BlockWyverniaPlank;
import mhfc.net.common.block.BlockWyverniaPlant;
import mhfc.net.common.block.BlockWyverniaQuickSand;
import mhfc.net.common.block.BlockWyverniaRock;
import mhfc.net.common.block.BlockWyverniaSand;
import mhfc.net.common.block.BlockWyverniaStone;
import mhfc.net.common.block.BlockWyverniaWood;
import mhfc.net.common.block.container.BlockBBQSpit;
import mhfc.net.common.block.container.BlockHunterBench;
import mhfc.net.common.block.container.BlockStunTrap;
import mhfc.net.common.block.util.BlockExploreArea;
import mhfc.net.common.block.util.BlockQuestBoard;
import mhfc.net.common.block.util.BlockRespawn;
import mhfc.net.common.item.block.ItemBlockBBQSpit;
import mhfc.net.common.item.block.ItemBlockBenchHunter;
import mhfc.net.common.item.block.ItemBlockIceCrystal;
import mhfc.net.common.item.block.ItemBlockQuestBoard;
import mhfc.net.common.item.block.ItemBlockStunTrap;
import mhfc.net.common.item.block.ItemSubtypedBlock;
import mhfc.net.common.util.services.IServiceKey;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class MHFCBlockRegistry {

	/**
	 * 
	 * WorldEdit important commands.
	 * 
	 * /br sphere < block id > < radius /br smooth
	 * 
	 * /butcher
	 * 
	 * /repl <block id>
	 * 
	 * /gmask 0 ( prevents overlapping blocks and only overlaps air blocks)
	 * 
	 * //pos1 ( sets position 1 ) can affect air //pos2 ( sets position 2 ) can affect air
	 * 
	 * //rotate 90 ( rotate copy block by 90 degrees)
	 * 
	 * ~Heltrato
	 * 
	 * 
	 */
	public static void staticInit() {}

	private static final IServiceKey<MHFCBlockRegistry> serviceAccess = RegistryWrapper
			.registerService("block registry", MHFCBlockRegistry::new, MHFCMain.preInitPhase);

	// private static Item item;
	// private static ItemBlockIngot ingot;
	public final Block mhfcblockoreblocks;
	public final Block mhfcblockore;

	public final Block mhfcblockhunterbench;
	public final Block mhfcblockstuntrap;
	public final Block mhfcblockdirt;
	public final Block mhfcblockgrass;
	public final Block mhfcblockstone;
	public final Block mhfcblockdiskstone;
	public final Block mhfcblockicecrystal;
	public final Block mhfcblockclay;
	public final Block mhfcblocksand;
	public final Block mhfcblockflowers;
	public final Block mhfcblockplant;
	public final Block mhfcblockbbqspit;
	public final Block mhfcblockplanks;
	public final Block mhfcblockrocks;
	public final Block mhfcblockquicksand;
	public final Block mhfcblockwood;
	public final Block mhfcblockquestboard;
	public final Block mhfcblockrespawn;
	public final Block mhfcblockexplorearea;

	private final List<Pair<Block, Item>> everyBlockWithItem = new ArrayList<>();
	private final List<Block> everyBlock = new ArrayList<>();
	private final List<Item> everyItem = new ArrayList<>();

	public final Collection<Pair<Block, Item>> allBlocksWithItem = Collections.unmodifiableList(everyBlockWithItem);
	public final Collection<Block> allBlocks = Collections.unmodifiableList(everyBlock);
	public final Collection<Item> allItems = Collections.unmodifiableList(everyItem);

	private MHFCBlockRegistry() {
		mhfcblockdirt = registerBlock("dirt", new BlockWyverniaDirt());//235
		mhfcblockgrass = registerBlock("grass", new BlockWyverniaGrass()); //236
		mhfcblockstone = registerBlock("stone", new BlockWyverniaStone());//237
		mhfcblocksand = registerBlock("sand", new BlockWyverniaSand()); //set 238
		mhfcblockclay = registerBlock("clay_block", new BlockWyverniaClay()); //239

		// Itemblocks. eg Plank woods this are register wit registerBlockWithItem
		mhfcblockwood = registerBlockWithItem("log", new BlockWyverniaWood(), b -> ItemSubtypedBlock.createFor(b)); //240
		mhfcblockplanks = registerBlockWithItem("plank", new BlockWyverniaPlank(), b -> ItemSubtypedBlock.createFor(b));

		mhfcblockrocks = registerBlockWithItem(
				"rock",
				new BlockWyverniaRock(),
				b -> ItemSubtypedBlock.createFor(b).setMaxStackSize(20));
		mhfcblockore = registerBlockWithItem(
				"ore",
				new BlockWyverniaOres(),
				b -> ItemSubtypedBlock.createFor(b).setMaxStackSize(12));
		mhfcblockoreblocks = registerBlockWithItem(
				"block",
				new BlockWyverniaOreBlock(),
				b -> ItemSubtypedBlock.createFor(b).setMaxStackSize(16));
		mhfcblockquicksand = registerBlock("quicksand", new BlockWyverniaQuickSand());
		mhfcblockdiskstone = registerBlock("diskstone", new BlockDiscstone());
		mhfcblockflowers = registerBlockWithItem(
				"flower",
				new BlockWyverniaFlower(),
				b -> ItemSubtypedBlock.createFor(b).setFull3D().setMaxStackSize(4));
		mhfcblockplant = registerBlockWithItem(
				"plant",
				new BlockWyverniaPlant(),
				b -> ItemSubtypedBlock.createFor(b).setFull3D().setMaxStackSize(4));
		// Util
		mhfcblockicecrystal = registerBlockWithItem("icecrystal", new BlockIceCrystal(), ItemBlockIceCrystal::new);
		mhfcblockhunterbench = registerBlockWithItem("hunterbench", new BlockHunterBench(), ItemBlockBenchHunter::new);
		mhfcblockstuntrap = registerBlockWithItem("trap_stun", new BlockStunTrap(), ItemBlockStunTrap::new);
		mhfcblockbbqspit = registerBlockWithItem("bbq_spit", new BlockBBQSpit(), ItemBlockBBQSpit::new);
		mhfcblockquestboard = registerBlockWithItem("questboard", new BlockQuestBoard(), ItemBlockQuestBoard::new);
		mhfcblockrespawn = registerBlock("respawn_marker", new BlockRespawn());
		mhfcblockexplorearea = registerBlock("exploration_teleporter", new BlockExploreArea());

		MHFCMain.logger().info("Blocks registered");
	}

	private <T extends Block> T registerBlock(String registryName, T block) {
		return registerBlockWithItem(registryName, block, new ItemBlock(block));
	}

	private <T extends Block> T registerBlockWithItem(
			String blockRegistry,
			T block,
			Function<? super T, ? extends Item> item) {
		return registerBlockWithItem(blockRegistry, block, item.apply(block));
	}

	private <T extends Block> T registerBlockWithItem(String blockRegistry, T block, Item item) {
		block.setRegistryName(blockRegistry);
		item.setRegistryName(blockRegistry);
		MHFCMain.logger().debug(
				"Registered " + block + " with id " + block.getRegistryName() + " and item " + item.getRegistryName());
		everyBlock.add(block);
		everyItem.add(item);
		everyBlockWithItem.add(Pair.of(block, item));
		GameRegistry.register(item);
		return GameRegistry.register(block);
	}

	public static MHFCBlockRegistry getRegistry() {
		return serviceAccess.getService();
	}
}
