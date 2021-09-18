package com.example.alenbeckerpig.core.init;

import com.example.alenbeckerpig.AlenBeckerPig;
import com.example.alenbeckerpig.common.item.NetherWartWand;

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
			() -> new NetherWartWand(ItemTier.NETHERITE, 1.5F, -3.0F, (new Item.Properties()).group(ItemGroup.TOOLS).isImmuneToFire()));
	
}
