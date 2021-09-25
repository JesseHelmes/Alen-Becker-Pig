package com.example.alenbeckerpig.goal;

import com.google.common.collect.ImmutableList;

import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;

//ticks are not being saved!! so i could do what ever i want! :D
//can i put this class in another class and so use the same variables?..
public class DrinkPotionsGoal<T extends MobEntity> extends Goal {
	// only positive effects!
	// https://minecraft.fandom.com/wiki/Effect
	private final ImmutableList<Effect> drinkablePotionEffects = ImmutableList.of(Effects.SPEED,
			Effects.FIRE_RESISTANCE, Effects.HASTE, Effects.STRENGTH, Effects.INSTANT_HEALTH, Effects.JUMP_BOOST,
			Effects.REGENERATION, Effects.RESISTANCE, Effects.WATER_BREATHING, Effects.NIGHT_VISION,
			Effects.HEALTH_BOOST, Effects.ABSORPTION, Effects.SATURATION, Effects.LUCK, Effects.SLOW_FALLING,
			Effects.CONDUIT_POWER, Effects.DOLPHINS_GRACE, Effects.HERO_OF_THE_VILLAGE);

	private final T user;
	private int potionUseTimer;
	private int potionTicks;

	public DrinkPotionsGoal(T user) {
		this.user = user;
	}

	/**
	 * Returns whether execution should begin. You can also read and cache any state
	 * necessary for execution in this.user method as well.
	 */
	public boolean shouldExecute() {
		if (this.user.world.getDifficulty() == Difficulty.PEACEFUL) {
			return false;
		} else {
			return this.potionUseTimer > 0 || !this.getPotionInventory().isEmpty();
		}
	}

	/**
	 * Keep ticking a continuous task that has already been started
	 */
	public void tick() {
		if (this.potionUseTimer > 0) {
			--this.potionUseTimer;
			return;
		}
		// this.user.getLookVec().
		// this.user.getLookController().
		if (!this.user.world.isRemote && this.user.isAlive() && this.user.isServerWorld()) {
			// this.drinkingMotion();
			this.drinkPotion();
		}
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting() {
		this.potionUseTimer = 0;
		super.startExecuting();
	}

	private boolean isPotion(ItemStack itemStackIn) {
		return itemStackIn.getItem() == Items.POTION;
	}

	public void drinkingMotion() {
		this.user.getLookController().setLookPosition(0, 0, -127);
	}

	// pig heeft geen eten function, kijk online hoe ik er 1 maak wegens drinken
	// fox en panda kunnen eten!
	// geluid en tijd
	// EERST KIJKEN OF DE ITEM ECHT EEN POTION IS, in geval dat een ander mod of
	// iets anders het een carrot gegeven heeft of zo iets
	public void drinkPotion(PigEntity pig) {
		// pig.setHeadRotation(yaw, pitch);
	}

	public void drinkPotion() {
		ItemStack itemStack = this.getPotionInventory();
		if (this.isPotion(itemStack)) {
			++this.potionTicks;
			if (this.potionTicks < 40 && this.potionTicks % 5 == 0) {
				this.user.playSound(itemStack.getDrinkSound(), 1.0F, 1.0F);
				this.user.world.setEntityState(this.user, (byte) 45);

			} else if (this.potionTicks > 40) {
				this.doneDrinkingPotion();
			}
		}
	}

	public void addDrinkablePotionsToEntity() {
		drinkablePotionEffects.forEach(effect -> {
			this.user.addPotionEffect(new EffectInstance(effect, 3600, 2));// 1
		});

		this.potionUseTimer = 3600;
	}

	public void addDrinkablePotionsToEntity(int durationIn) {
		drinkablePotionEffects.forEach(effect -> {
			this.user.addPotionEffect(new EffectInstance(effect, durationIn, 5));
		});
	}

	public void doneDrinkingPotion() {
		if (this.user.world.getDifficulty() == Difficulty.PEACEFUL) {
			return;
		}

		switch (this.user.world.getDifficulty()) {
		case EASY:
			// apply current potion
			ItemStack itemstack = this.user.getItemStackFromSlot(EquipmentSlotType.MAINHAND);
			Potion potion = PotionUtils.getPotionFromItem(itemstack);
			potion.getEffects().forEach(effect -> {
				this.user.addPotionEffect(new EffectInstance(effect.getPotion(), 2400));
			});
			this.user.addPotionEffect(new EffectInstance(Effects.HERO_OF_THE_VILLAGE, 2400));

			break;
		case NORMAL:
			// apply all effects, but invisible
			this.addDrinkablePotionsToEntity();

			break;
		// hard
		default:
			// apply all effects, including INVISIBILITY
			this.addDrinkablePotionsToEntity();
			this.user.addPotionEffect(new EffectInstance(Effects.INVISIBILITY, 3600));

			break;
		}

		this.dropEmptyBottle();
		this.clearPotionInventory();
		this.potionTicks = 0;

	}
	
	//oh.. i cant use a switch?
	/*public Effect convertNegatifToPositiveEffect(Effect effect) {
		if(effect == Effects.WEAKNESS) {
			return Effects.STRENGTH;
		}

		switch(effect) {
		case WEAKNESS:
			return Effects.STRENGTH;
					default:
						return effect;
		}
	}*/

	public void dropEmptyBottle() {
		// isRemote prevents ghost items
		if (!this.user.world.isRemote) {
			if (this.user.world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT)) {
				this.user.entityDropItem(Items.GLASS_BOTTLE);
			}
		}
	}

	// yeahh... we know what happens in here >.>
	public void clearPotionInventory() {
		this.user.setItemStackToSlot(EquipmentSlotType.MAINHAND, ItemStack.EMPTY);
	}

	public ItemStack getPotionInventory() {
		return this.user.getItemStackFromSlot(EquipmentSlotType.MAINHAND);
	}

}
