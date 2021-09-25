package com.example.alenbeckerpig.core.init;

import com.example.alenbeckerpig.AlenBeckerPig;
import com.example.alenbeckerpig.common.item.NetherWartWand;
import com.example.alenbeckerpig.core.Foods;

import net.minecraft.block.Blocks;
import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemTier;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemInit {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,
			AlenBeckerPig.MOD_ID);

	public static final RegistryObject<NetherWartWand> NETHER_WART_WAND = ITEMS.register("nether_wart_wand",
			() -> new NetherWartWand(ItemTier.NETHERITE, 1.5F, -3.0F,
					(new Item.Properties()).group(ItemGroup.TOOLS).isImmuneToFire()));

	// this is more of a easter egg for creative mode users
	public static final RegistryObject<NetherWartWand> NETHER_WART_WAND_UGLY = ITEMS.register("nether_wart_wand_ugly",
			() -> new NetherWartWand(ItemTier.NETHERITE, 1.5F, -3.0F,
					(new Item.Properties()).group(ItemGroup.TOOLS).isImmuneToFire()));

	public static final RegistryObject<Item> EDIBLE_NETHER_WART = ITEMS.register("edible_nether_wart",
			() -> new BlockNamedItem(Blocks.NETHER_WART,
					(new Item.Properties()).group(ItemGroup.FOOD).food(Foods.NETHER_WART).isImmuneToFire()));

	// this.. is just random, easter egg maybe?.. who knows? >.>
	// butter ingot = 3 Dandelions on a row
	public static final RegistryObject<Item> BUTTER = ITEMS.register("butter",
			() -> new Item((new Item.Properties()).group(ItemGroup.FOOD).food(Foods.BUTTER)));
}
