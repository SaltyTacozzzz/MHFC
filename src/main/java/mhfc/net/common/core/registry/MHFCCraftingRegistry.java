package mhfc.net.common.core.registry;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class MHFCCraftingRegistry {

	public static void init() {
		GameRegistry.addRecipe(
				new ItemStack(MHFCBlockRegistry.getRegistry().mhfcblockhunterbench, 1),
				new Object[] { "XAX", "SXS", "ASA", Character.valueOf('X'), Blocks.LOG, Character.valueOf('A'),
						Blocks.IRON_BLOCK, Character.valueOf('S'), Items.BRICK });
		GameRegistry.addRecipe(
				new ItemStack(MHFCItemRegistry.getRegistry().bombMaterial, 2),
				new Object[] { "XXX", "XAX", "XXX", Character.valueOf('X'), Items.LEATHER, Character.valueOf('A'),
						Items.GUNPOWDER });
		GameRegistry.addRecipe(
				new ItemStack(MHFCItemRegistry.getRegistry().lumberbar, 10),
				new Object[] { "XXX", "AAA", "   ", Character.valueOf('X'), Items.LEATHER, Character.valueOf('A'),
						Blocks.PLANKS });
		GameRegistry.addRecipe(
				new ItemStack(MHFCItemRegistry.getRegistry().moldedIron, 15),
				new Object[] { "XXX", "XAX", "   ", Character.valueOf('X'), Items.IRON_INGOT, Character.valueOf('A'),
						Items.GUNPOWDER });
		GameRegistry.addRecipe(
				new ItemStack(MHFCItemRegistry.getRegistry().steelbar, 5),
				new Object[] { "AXA", "XXX", "   ", Character.valueOf('X'), MHFCItemRegistry.getRegistry().lumberbar,
						Character.valueOf('A'),
						MHFCItemRegistry.getRegistry().moldedIron });
	}
}
