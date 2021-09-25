package com.example.alenbeckerpig.common.item;

import com.example.alenbeckerpig.core.init.BlockInit;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.Multimap;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.GrassPathBlock;
import net.minecraft.block.NetherWartBlock;
import net.minecraft.block.SoulSandBlock;
import net.minecraft.enchantment.IVanishable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.TieredItem;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

// extends TieredItem implements IVanishable
//toch niet toolItem? het is geen tool.. maar wel een tier
public class NetherWartWand extends TieredItem implements IVanishable {
	// this is a compensation, as i am not able to place a nether warts next to
	// another that is not on soul sand.
	// or i have to extend nether wart block.. and use that, but spawn nether wart
	// items only
	private final float attackDamage;
	/** Modifiers applied when the item is in the mainhand of a user. */
	private final Multimap<Attribute, AttributeModifier> attributeModifiers;
	private final int maxAge = 3;

	public NetherWartWand(IItemTier tier, float attackDamageIn, float attackSpeedIn, Item.Properties builderIn) {
		super(tier, builderIn);
		this.attackDamage = attackDamageIn + tier.getAttackDamage();
		Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Nether wart wand modifier",
				(double) this.attackDamage, AttributeModifier.Operation.ADDITION));
		builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(ATTACK_SPEED_MODIFIER, "Nether wart wand modifier",
				(double) attackSpeedIn, AttributeModifier.Operation.ADDITION));
		this.attributeModifiers = builder.build();
	}

	public float getAttackDamage() {
		return this.attackDamage;
	}

	/**
	 * Current implementations of this method in child classes do not use the entry
	 * argument beside ev. They just raise the damage on the stack.
	 */
	@Override
	public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		stack.damageItem(this.reseiveDamage(attacker), attacker, (entity) -> {// 2
			entity.sendBreakAnimation(EquipmentSlotType.MAINHAND);
		});
		return true;
	}

	/**
	 * Called when a Block is destroyed using this Item. Return true to trigger the
	 * "Use Item" statistic.
	 */
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, BlockState state, BlockPos pos,
			LivingEntity entityLiving) {
		if (!worldIn.isRemote && state.getBlockHardness(worldIn, pos) != 0.0F) {
			stack.damageItem(this.reseiveDamage(entityLiving), entityLiving, (entity) -> {// 1
				entity.sendBreakAnimation(EquipmentSlotType.MAINHAND);
			});
		}
		return true;
	}

	/**
	 * Gets a map of item attribute modifiers, used by ItemSword to increase hit
	 * damage.
	 */
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType equipmentSlot) {
		return equipmentSlot == EquipmentSlotType.MAINHAND ? this.attributeModifiers
				: super.getAttributeModifiers(equipmentSlot);
	}

	/**
	 * Called when this item is used when targetting a Block
	 */
	public ActionResultType onItemUse(ItemUseContext context) {
		PlayerEntity playerentity = context.getPlayer();
		World world = context.getWorld();
		BlockPos blockpos = context.getPos();
		BlockState blockstate = world.getBlockState(blockpos);
		Block block = blockstate.getBlock();

		if (context.getFace() == Direction.DOWN) {
			return ActionResultType.PASS;
		} else {
			BlockState blockstate1 = null;
			if (world.isAirBlock(blockpos.up()) && blockstate.isSolid()) {
				world.playSound(playerentity, blockpos, SoundEvents.ITEM_NETHER_WART_PLANT, SoundCategory.BLOCKS, 1.0F,
						1.0F);

				// looks first if the underlaying block is a Soul Block, if it is Soul Block
				// then it will place the default Nether Wart block.
				// this is only for when our mod gets removed and in that case all Nether Warts
				// placed on Soul Sands will stay
				// unlike our custom placeable clone of the Nether Wart, this one will disappear
				// if you removed the mod.
				if ((blockstate.getBlock() instanceof SoulSandBlock)) {
					blockstate1 = Blocks.NETHER_WART.getDefaultState();
				} else {
					blockstate1 = BlockInit.PLACEABLE_NETHER_WART.get().getDefaultState();
				}
			}

			if (blockstate1 != null) {
				if (!world.isRemote) {
					world.setBlockState(blockpos.up(), blockstate1.with(NetherWartBlock.AGE, this.getMaxAge()), 11);
					if (blockstate.getBlock() instanceof GrassPathBlock) {
						world.setBlockState(blockpos, Blocks.DIRT.getDefaultState(), 11);
					}
					if (playerentity != null) {
						context.getItem().damageItem(this.reseiveDamage(playerentity), playerentity, (player) -> {
							player.sendBreakAnimation(context.getHand());
						});
					}
				}

				return ActionResultType.func_233537_a_(world.isRemote);
			} else {
				return ActionResultType.PASS;
			}
		}

	}

	public int getMaxAge() {
		return this.maxAge;
	}

	private int reseiveDamage(LivingEntity livingEntity) {
		switch (livingEntity.world.getDifficulty()) {
		case PEACEFUL:
			return 0;
		case EASY:
			return 1;
		case NORMAL:
			return 2;
		case HARD:
			return 3;
		default:
			return 2;
		}
	}

	private int doDamage(LivingEntity livingEntity) {
		switch (livingEntity.world.getDifficulty()) {
		case PEACEFUL:
			return 10;
		case EASY:
			return 8;
		case NORMAL:
			return 6;
		case HARD:
			return 4;
		default:
			return 6;
		}
	}

}
