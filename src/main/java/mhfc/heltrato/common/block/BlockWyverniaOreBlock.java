package mhfc.heltrato.common.block;

import java.util.List;

import mhfc.heltrato.MHFCMain;
import mhfc.heltrato.common.util.lib.MHFCReference;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockWyverniaOreBlock extends Block {

	
	@SideOnly(Side.CLIENT)
	private IIcon[] texture;
	
	
	public BlockWyverniaOreBlock() {
		super(Material.rock);
		setBlockName("wyverniaoreblocks");
		setCreativeTab(MHFCMain.mhfctabs);
	}
	
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister regIcon){
		texture = new IIcon[MHFCReference.oreblockSubBlocks.length];
		
		for(int i = 0; i < MHFCReference.oreblockSubBlocks.length; i++){
			texture[i] = regIcon.registerIcon("mhfc:" + MHFCReference.oreblockSubBlocks[i]);
		}
	}
	
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item block, CreativeTabs creativetabs, List list){
		for(int i = 0; i < MHFCReference.oreblockSubBlocks.length; i++){
			list.add(new ItemStack(block, 1, i));
		}
	}
	
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta){
		return texture[meta];
	}
	
	public int damageDropped(int meta){
		return meta;
	}
	
	

}
