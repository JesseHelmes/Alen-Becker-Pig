package com.example.alenbeckerpig.common.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.NetherWartBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class PlaceableNetherWartBlock extends NetherWartBlock {

	public PlaceableNetherWartBlock(AbstractBlock.Properties builder) {
		super(builder);
	}

	@Override
	protected boolean isValidGround(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return state.isSolid();
	}

}
