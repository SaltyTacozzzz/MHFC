package mhfc.net.common.weapon.range.bowgun.light;

import java.util.Random;

import mhfc.net.common.entity.projectile.EntityBullet;
import mhfc.net.common.helper.MHFCWeaponClassingHelper;
import mhfc.net.common.util.Cooldown;
import mhfc.net.common.util.lib.MHFCReference;
import mhfc.net.common.weapon.range.bowgun.BowgunClass;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LightBowgun extends BowgunClass {
	protected Random rand = new Random();

	public LightBowgun() {
		super();
		getWeaponDescription(MHFCWeaponClassingHelper.lbowgunname);
		getcooldown = 8;
		setTextureName(MHFCReference.weapon_bgl_barrel_icon);
		enableCooldownDisplay = true;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack iStack, World world, EntityPlayer player) {
		if (Cooldown.canUse(iStack, getcooldown)) {
			if (player.capabilities.isCreativeMode) {
				if (!world.isRemote) {
					world.playSoundAtEntity(player, "mhfc:bowgun-shot", 1.0F, 1.0F);
				}
				EntityThrowable entity = new EntityBullet(world, player, damage);
				entity.posX += (this.rand.nextDouble() - this.rand.nextDouble()) / 2;
				entity.posY += (this.rand.nextDouble() - this.rand.nextDouble()) / 2;
				entity.posZ += (this.rand.nextDouble() - this.rand.nextDouble()) / 2;
				if (!world.isRemote) {
					world.spawnEntityInWorld(entity);
				}
			}
		}
		return iStack;
	}

}
