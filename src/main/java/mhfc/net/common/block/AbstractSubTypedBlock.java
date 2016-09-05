package mhfc.net.common.block;

import java.util.List;

import mhfc.net.common.util.SubTypedItem;
import mhfc.net.common.util.SubTypedItem.SubTypeEnum;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class AbstractSubTypedBlock<T extends Enum<T> & SubTypeEnum<Block>> extends Block {

	protected final SubTypedItem<Block, T> blockTrait;
	protected final PropertyEnum<T> subtypeProperty;

	public AbstractSubTypedBlock(Class<T> subtypeEnumClazz, Material blockMaterial) {
		super(blockMaterial);
		blockTrait = new SubTypedItem<>(subtypeEnumClazz);
		subtypeProperty = PropertyEnum.create("variant", subtypeEnumClazz);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, subtypeProperty);
	}

	@Override
	public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list) {
		blockTrait.getSubItems(item, list);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(subtypeProperty, blockTrait.getSubType(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return blockTrait.getMeta(state.getValue(subtypeProperty));
	}

	@Override
	public int damageDropped(IBlockState state) {
		return getMetaFromState(state);
	}
}
