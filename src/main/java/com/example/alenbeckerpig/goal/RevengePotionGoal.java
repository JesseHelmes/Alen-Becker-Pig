package com.example.alenbeckerpig.goal;

import com.example.alenbeckerpig.AlenBeckerPig;

import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.potion.Effects;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;

public class RevengePotionGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
	
	private static final EntityPredicate suitableTarget = (new EntityPredicate()).setLineOfSiteRequired().setUseInvisibilityCheck();

	//this should be the HurtByTargetGoal?..
	public RevengePotionGoal(MobEntity goalOwnerIn, Class<T> targetClassIn, boolean checkSight, boolean nearbyOnlyIn) {
		super(goalOwnerIn, targetClassIn, checkSight, nearbyOnlyIn);
	}

	/**
	 * Returns whether execution should begin. You can also read and cache any state
	 * necessary for execution in this method as well.
	 */
	public boolean shouldExecute() {
		if (this.goalOwner.world.getDifficulty() == Difficulty.PEACEFUL
				|| this.goalOwner.getActivePotionEffect(Effects.HERO_OF_THE_VILLAGE) == null) {
			return false;
		} else if (this.goalOwner.getRevengeTarget() != null) {
			if (this.goalOwner.getRevengeTarget().getType() == EntityType.PLAYER && this.goalOwner.world.getGameRules().getBoolean(GameRules.UNIVERSAL_ANGER)) {
	            return false;
	         } else {
	            return this.isSuitableTarget(this.goalOwner.getRevengeTarget(), suitableTarget);
	         }
		} else {
			return false;
		}
	}
	
	/*
	public boolean shouldExecute() {
		if (this.goalOwner.world.getDifficulty() == Difficulty.PEACEFUL
				|| this.goalOwner.getActivePotionEffect(Effects.HERO_OF_THE_VILLAGE) == null) {
			return false;
		} else if (this.goalOwner.getRevengeTarget() != null) {
			return this.nearestTarget != null;
		} else {
			return false;
		}
	}
	*/

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting() {
		this.goalOwner.setAttackTarget(this.goalOwner.getRevengeTarget());
		this.target = this.goalOwner.getAttackTarget();
		this.unseenMemoryTicks = 300;
		AlenBeckerPig.LOGGER.info("attack!..");

		super.startExecuting();
	}

}
