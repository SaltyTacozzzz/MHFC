package mhfc.net.common.entity.projectile;

import java.util.List;

import mhfc.net.common.entity.monster.EntityDeviljho;
import mhfc.net.common.entity.monster.EntityTigrex;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityProjectileBlock extends EntityThrowable {

	public EntityProjectileBlock(World par) {
		super(par);
		setSize(1.0F, 1.0F);
	}

	public EntityProjectileBlock(World world, EntityLivingBase living) {
		super(world, living);
		this.posY -= living.getEyeHeight();
		Vec3d look = living.getLookVec();
		this.posX += look.xCoord * 2;
		this.posZ += look.zCoord * 2;
		rotationYaw = living.rotationYaw;
	}

	public EntityProjectileBlock(World par, EntityTigrex e) {
		this(par, (EntityLivingBase) e);
		setSize(1.0F, 1.0F);
	}

	public EntityProjectileBlock(World par, EntityDeviljho e) {
		this(par, (EntityLivingBase) e);
		setSize(1.4F, 1.4F);
	}

	@Override
	protected float getGravityVelocity() {
		return 0.03F;
	}

	@Override
	protected void onImpact(RayTraceResult mop) {
		List<Entity> list = this.worldObj
				.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().expand(2.5D, 2.0D, 2.5D));
		list.remove(getThrower());

		for (Entity entity : list) {
			if (getDistanceSqToEntity(entity) > 6.25D) {
				continue;
			}
			entity.attackEntityFrom(DamageSource.causeMobDamage(getThrower()), 100 + this.rand.nextInt(17));
		}
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound tagcompound) {
		super.writeEntityToNBT(tagcompound);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound tagcompound) {
		super.readEntityFromNBT(tagcompound);
	}

}
