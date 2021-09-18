package com.example.alenbeckerpig.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;

import com.example.alenbeckerpig.AlenBeckerPig;

import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.BushBlock;
import net.minecraft.block.NetherWartBlock;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

//als dit werkt kan ik ook dit gebruiken voor.. pig model.. toch?
@Mixin(NetherWartBlock.class)
public class NetherWartBlockMixin extends NetherWartBlock {
	public static final BooleanProperty PLACED_BY_WAND = BooleanProperty.create("placed_by_wand");

	protected NetherWartBlockMixin(Properties properties) {
		super(properties);
		AlenBeckerPig.LOGGER.info("con");
	}

	//@Inject(method = "isValidGround", at = @At("RETURN"))
	
	@Override
	protected boolean isValidGround(BlockState state, IBlockReader worldIn, BlockPos pos) {
		AlenBeckerPig.LOGGER.info("ieet");
		return state.isIn(Blocks.SOUL_SAND) || state.get(PLACED_BY_WAND);
	}

	@Override
	//@Inject(method = "fillStateContainer", at = @At("HEAD"))
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		AlenBeckerPig.LOGGER.info("add!");
		builder.add(PLACED_BY_WAND);
		builder.add(AGE);
	}
}
