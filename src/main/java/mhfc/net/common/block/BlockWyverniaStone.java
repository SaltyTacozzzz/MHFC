package mhfc.net.common.block;

import java.util.Random;

import mhfc.net.MHFCMain;
import mhfc.net.common.index.ResourceInterface;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockWyverniaStone extends Block {

	public BlockWyverniaStone() {
		super(Material.ROCK);
		setUnlocalizedName(ResourceInterface.block_wyverianstone_name);
		setCreativeTab(MHFCMain.mhfctabs);
	}

	@Override
	public int quantityDropped(Random random) {
		return 1;
	}
}
