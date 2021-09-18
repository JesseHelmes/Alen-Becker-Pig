package com.example.alenbeckerpig.goal;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.goal.PanicGoal;
import net.minecraft.potion.Effects;

public class PotionPanicGoal extends PanicGoal {

	public PotionPanicGoal(CreatureEntity creature, double speedIn) {
		super(creature, speedIn);
	}

	   /**
	    * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
	    * method as well.
	    */
	   public boolean shouldExecute() {
	      return this.creature.getActivePotionEffect(Effects.HERO_OF_THE_VILLAGE) == null && super.shouldExecute();
	   }

}
