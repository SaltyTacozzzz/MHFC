package mhfc.net.common.item.armor.donators;

import java.util.List;

import mhfc.net.common.index.ResourceInterface;
import mhfc.net.common.index.armor.Material;
import mhfc.net.common.index.armor.Model;
import mhfc.net.common.item.ItemRarity;
import mhfc.net.common.item.armor.ArmorExclusive;
import mhfc.net.common.system.ColorSystem;
import mhfc.net.common.system.DonatorSystem;
import mhfc.net.common.util.Assert;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class DragoonArmor extends ArmorExclusive {
	private static final String[] names = { ResourceInterface.armor_dragoon_helm_name,
			ResourceInterface.armor_dragoon_chest_name, ResourceInterface.armor_dragoon_legs_name,
			ResourceInterface.armor_dragoon_boots_name };

	public DragoonArmor(EntityEquipmentSlot type) {
		super(DonatorSystem.dragoon, Material.dragoon, ItemRarity.R04, type);
		setUnlocalizedName(names[3 - type.getIndex()]);
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected ModelBiped getBipedModel(EntityEquipmentSlot armorSlot) {
		switch (armorSlot) {
		case HEAD:
			return Model.dragoon;
		case LEGS:
			return null;
		case FEET:
			return Model.dragoon;
		case CHEST:
			return Model.dragoon;
		case MAINHAND:
		case OFFHAND:
		default:
			Assert.logUnreachable("Armor can only be equiped on armor slots, got {}", armorSlot);
		}

		return null;
	}

	@Override
	public void addInformation(
			ItemStack par1ItemStack,
			EntityPlayer par2EntityPlayer,
			List<String> par3List,
			boolean par4) {
		par3List.add(ColorSystem.ENUMAQUA + "[ Donators Exclusive ");
		par3List.add("Health Boost" + ColorSystem.ENUMGOLD + "(S)");
	}

	@Override
	protected void applySetBonus(World world, EntityPlayer player) {
		float h = player.getHealth();
		player.addPotionEffect(new PotionEffect(MobEffects.HEALTH_BOOST, 200, 1, true, true));
		player.setHealth(h);
	}
}
