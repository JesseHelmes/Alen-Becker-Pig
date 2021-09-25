package com.example.alenbeckerpig.core.init;

import com.example.alenbeckerpig.AlenBeckerPig;
import com.example.alenbeckerpig.common.block.PlaceableNetherWartBlock;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockInit {
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,
			AlenBeckerPig.MOD_ID);

	public static final RegistryObject<PlaceableNetherWartBlock> PLACEABLE_NETHER_WART = BLOCKS.register(
			"placeable_nether_wart",
			() -> new PlaceableNetherWartBlock(AbstractBlock.Properties.create(Material.PLANTS, MaterialColor.RED)
					.doesNotBlockMovement().tickRandomly().sound(SoundType.NETHER_WART)));
}
