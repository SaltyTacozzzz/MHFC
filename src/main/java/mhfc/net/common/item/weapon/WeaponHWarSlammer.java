package mhfc.net.common.item.weapon;

import java.util.List;
import java.util.Random;

import mhfc.net.common.entity.mob.EntityKirin;
import mhfc.net.common.entity.mob.EntityTigrex;
import mhfc.net.common.item.weapon.type.SiegeClass;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;

public class WeaponHWarSlammer extends SiegeClass {

	// private Random rand;
	private float weaponDamage;

	public WeaponHWarSlammer(ToolMaterial getType) {
		super(getType);
		setUnlocalizedName("hammer_5");
		setFull3D();
		rand = new Random();
		weaponDamage = getType.getDamageVsEntity() - 4;
	}
	@Override
	@SuppressWarnings("unchecked")
	public void addInformation(ItemStack par1ItemStack,
			EntityPlayer par2EntityPlayer,
			@SuppressWarnings("rawtypes") List par3List, boolean par4) {
		par3List.add("Hammer Class");
		par3List.add("\u00a79No-Element");
		par3List.add("\u00a72Siege Damage");
	}

	@Override
	public boolean isFull3D() {
		return true;
	}

	@Override
	public void registerIcons(IIconRegister par1IconRegister) {
		itemIcon = par1IconRegister.registerIcon("mhfc:hammer");
	}

	public float getDamageVsEntity(Entity entity) {

		return weaponDamage;
	}

	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase ent,
			EntityLivingBase player) {
		player.addPotionEffect(new PotionEffect(Potion.digSlowdown.id, 80, 1));
		float damage = 0.0f;
		if (ent instanceof EntityKirin) {
			damage = 41;
		}
		if (ent instanceof EntityTigrex) {
			damage = 36f;
			ent.motionX = 0.3D;
		}

		DamageSource dmgSource = DamageSource
				.causePlayerDamage((EntityPlayer) player);
		ent.attackEntityFrom(dmgSource, damage);

		return true;
	}

}
