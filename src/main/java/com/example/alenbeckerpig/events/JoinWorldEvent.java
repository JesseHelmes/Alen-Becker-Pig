package com.example.alenbeckerpig.events;

import com.example.alenbeckerpig.goal.DrinkPotionsGoal;
import com.example.alenbeckerpig.goal.PickupItemGoal;
import com.example.alenbeckerpig.goal.PotionPanicGoal;
import com.example.alenbeckerpig.goal.RevengePotionGoal;

import net.minecraft.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.PanicGoal;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class JoinWorldEvent {
	@SubscribeEvent
	public void joinWorld(EntityJoinWorldEvent event) {
		if (!(event.getEntity() instanceof PigEntity)) {
			return;
		}

		PigEntity pig = (PigEntity) event.getEntity();
		this.addPigGoals(pig);
		// pig.clearActivePotions();// faster testing, for now
	}

	private void addPigGoals(PigEntity pig) {
		// removes default pig goals
		pig.goalSelector.getRunningGoals().forEach((goal) -> {
			if (goal.getGoal() instanceof PanicGoal) {
				pig.goalSelector.removeGoal(goal.getGoal());
			}
		});

		pig.targetSelector.addGoal(1, new PotionPanicGoal(pig, 1.25D));

		/// items that the pig wants
		pig.goalSelector.addGoal(6, new TemptGoal(pig, 1.2D, false, Ingredient.fromItems(Items.NETHER_WART)));

		// change priority of the goals?
		pig.goalSelector.addGoal(5, new PickupItemGoal<PigEntity>(pig));// 5
		pig.goalSelector.addGoal(5, new DrinkPotionsGoal<PigEntity>(pig));// 5
		pig.targetSelector.addGoal(4, new RevengePotionGoal<PlayerEntity>(pig, PlayerEntity.class, true, false));// 0

		pig.goalSelector.addGoal(4, new LeapAtTargetGoal(pig, 0.4F));// 4
		pig.goalSelector.addGoal(5, new MeleeAttackGoal(pig, 1.0D, true));// 5
	}
}
