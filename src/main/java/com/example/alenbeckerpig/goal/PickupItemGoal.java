package com.example.alenbeckerpig.goal;

import java.util.List;
import java.util.function.Predicate;

import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Effects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;

public class PickupItemGoal<T extends MobEntity> extends Goal {
	private final T user;

	private static final Predicate<ItemEntity> WANTED_ITEMS = (itemEntity) -> {
		Item item = itemEntity.getItem().getItem();//
		Potion potion = PotionUtils.getPotionFromItem(itemEntity.getItem());
		return (item == Items.POTION && potion != Potions.WATER && potion != Potions.EMPTY) && itemEntity.isAlive()
				&& !itemEntity.cannotPickup();
	};

	public PickupItemGoal(T user) {
		this.user = user;
	}

	/**
	 * Returns whether execution should begin. You can also read and cache any state
	 * necessary for execution in this method as well.
	 */
	public boolean shouldExecute() {
		// i check on HERO_OF_THE_VILLAGE effect because it is the only effect that you
		// can't get from a potion or somekind
		// and this way i don't need to store a NBT variable. I am lazy okay!

		if (this.user.world.getDifficulty() == Difficulty.PEACEFUL
				|| !this.user.world.getGameRules().getBoolean(GameRules.MOB_GRIEFING) || this.user.isChild()
				|| this.user.getActivePotionEffect(Effects.HERO_OF_THE_VILLAGE) != null
				|| !this.getPotionInventory().isEmpty()) {
			return false;
		} else {

			List<ItemEntity> list = this.user.world.getEntitiesWithinAABB(ItemEntity.class, this.user.getBoundingBox(),
					PickupItemGoal.WANTED_ITEMS);
			return !list.isEmpty() && this.getPotionInventory().isEmpty();
		}

	}

	/**
	 * Keep ticking a continuous task that has already been started
	 */
	public void tick() {
		this.lookAndPickupItem();
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting() {

	}

	// looks for item that it can pick up and picks it up
	public void lookAndPickupItem() {
		if (!this.getPotionInventory().isEmpty()
				|| !this.user.world.getGameRules().getBoolean(GameRules.MOB_GRIEFING)) {
			return;
		}

		List<ItemEntity> list = this.user.world.getEntitiesWithinAABB(ItemEntity.class, this.user.getBoundingBox(),
				PickupItemGoal.WANTED_ITEMS);

		if (!list.isEmpty() && this.getPotionInventory().isEmpty()) {
			ItemEntity item = list.get(0);
			this.pickupItem(item);
		}
	}
	
	public void pickupItem() {
		// if item is type potion && !not negative effect (if possible) and ! empty or
		// some other.. things that is.. yeahh... not useful?
		// https://www.tabnine.com/code/java/classes/net.minecraft.potion.Potion

		/*
		 * if(Potions.EMPTY || Potions.) {
		 * 
		 * }
		 */
	}

	// dit pakt een item op!
	protected void pickupItem(ItemEntity itemEntity) {
		if (net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.user.world, this.user)) {
			if (this.getPotionInventory().isEmpty()) {
				this.user.triggerItemPickupTrigger(itemEntity);
				ItemStack itemstack = itemEntity.getItem();
				this.user.setItemStackToSlot(EquipmentSlotType.MAINHAND, itemstack);
				this.user.onItemPickup(itemEntity, itemstack.getCount());
				itemEntity.remove();
			}
		}
	}

	public ItemStack getPotionInventory() {
		return this.user.getItemStackFromSlot(EquipmentSlotType.MAINHAND);
	}

}
