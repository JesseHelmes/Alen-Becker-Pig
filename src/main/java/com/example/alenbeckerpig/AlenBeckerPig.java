package com.example.alenbeckerpig;

import com.example.alenbeckerpig.client.render.PigPotionItemLayer;
import com.example.alenbeckerpig.core.init.BlockInit;
import com.example.alenbeckerpig.core.init.ItemInit;
import com.example.alenbeckerpig.goal.DrinkPotionsGoal;
import com.example.alenbeckerpig.goal.PickupItemGoal;
import com.example.alenbeckerpig.goal.PotionPanicGoal;
import com.example.alenbeckerpig.goal.RevengePotionGoal;

import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.PigRenderer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.PanicGoal;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.Effects;
import net.minecraft.world.GameRules;
import net.minecraftforge.common.BasicTrade;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.Message;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/*
 * USEFULL LINKS
 * 
 * https://minecraft.fandom.com/wiki/Loot_table
 * https://minecraft.fandom.com/wiki/Advancement/JSON_format
 * https://minecraft.fandom.com/wiki/Recipe
 */

//choclate? white: more milk, cocoa bones,
//

/*
 * ToDo
 * 
 * //advancement revoke Dev only alenbeckerpig:nether/eat_nether_wart make
 * edible nether warts? eat: not possible, but if it can i will!
 * 
 * 
 * nether wart wand recipt when achievement is maded so you can craft it if you
 * lost it:
 * 
 * think of way to get nether wart wand when you lost it..
 * 
 * don't drop nether wart wand on pig death?
 * 
 * make for cod a separated mod and have it use the classic blue cod with the mod name "classic cod"
 * include the advencement, lang, attack damage and textures
 * 
 * Extra
 * 
 * show potion/item in mouth
 * 
 * turn head up so it will looks like it is drinking
 * 
 * turn head up before it starts drinking? look at pig if this is what happends:
 * onderzoek
 * 
 * 
 * make pig stand still and have belly sounds like in alen becker pig
 * 
 * 
 * CLEANUP
 * 
 * cleanup comments
 * 
 * put class variables in top
 * 
 * remove unused imports!: always busy!
 * 
 * move events to own class
 * 
 * move items (functions) to own class if needed
 */

/*
 * Low graphics is using my version and for the higher ones i combine nether wart and a stick. 
 * Nether wart turn 45 and maybe scale the sides a bit incase you see overlapping glitch.
 */

/*
 * wat als ik er voor kan zorgen dat een pig meerdere potions kan drinken zolang
 * er een potion in de buurt ligt? maar hij moet eerst door de drinken heen gaan
 * en een lege terug gooien voordat die de volgede pakt wanneer er geen potions
 * meer zijn om te drinken krijgt die de status effecten want nu is het zoals
 * die er 1 drinkt dat die ook effects krijgt die die niet gedronken had!
 * 
 * 
 * WAT ALS! een pig in een village leeft en potions heeft gedronken, wanneer een
 * zombie aanvalt dan valt die alle monsters aan! Pig protector!
 */

// The value here should match an entry in the META-INF/mods.toml file
@Mod(AlenBeckerPig.MOD_ID)
public class AlenBeckerPig {
	// Directly reference a log4j logger.
	public static final Logger LOGGER = LogManager.getLogger();
	public static final String MOD_ID = "alenbeckerpig";
//bottly

	// kill @e[type=minecraft:pig]

	public AlenBeckerPig() {
		// Register the setup method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::finishLoading);

		BlockInit.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
		ItemInit.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());

		// 1.17
		// FMLJavaModLoadingContext.get().getModEventBus().addListener(this::registerAttributes);

		// Register ourselves for server and other game events we are interested in
		MinecraftForge.EVENT_BUS.register(this);
	}

	// prevent nether wart register and register our nether wart in its place
	/*
	 * @SubscribeEvent public static void initBlock(final
	 * RegistryEvent.Register<Item> event) { event.getRegistry().; }
	 */

	private void setup(final FMLCommonSetupEvent event) {
		// https://github.com/TheGreyGhost/MinecraftByExample/blob/master/src/main/java/minecraftbyexample/mbe11_item_variants/StartupClientOnly.java
		/*
		 * we need to attach the fullness PropertyOverride to the Item, but there are
		 * two things to be careful of: 1) We should do this on a client installation
		 * only, not on a DedicatedServer installation. Hence we need to use
		 * FMLClientSetupEvent. 2) FMLClientSetupEvent is multithreaded but
		 * ItemModelsProperties is not multithread-safe. So we need to use the
		 * enqueueWork method, which lets us register a function for synchronous
		 * execution in the main thread after the parallel processing is completed
		 */
		event.enqueueWork(() -> {
			GlobalEntityTypeAttributes.put(EntityType.PIG,
					PigEntity.func_234215_eI_().createMutableAttribute(Attributes.ATTACK_DAMAGE, 1.0D).create());
		});
		RenderTypeLookup.setRenderLayer(BlockInit.PLACEABLE_NETHER_WART.get(), RenderType.getCutout());

	}

	// 1.17 change attributes like attack for entities
	/*
	 * public void registerAttributes(EntityAttributeCreationEvent event) {
	 * event.put(EntityType.PIG,
	 * PigEntity.func_234215_eI_().createMutableAttribute(Attributes.ATTACK_DAMAGE,
	 * 0.5D).create()); }
	 */

	@OnlyIn(Dist.CLIENT)
	private void finishLoading(FMLLoadCompleteEvent event) {
		AlenBeckerPig.addLayers();
	}

	public static void addLayers() {
		EntityRenderer<?> render = Minecraft.getInstance().getRenderManager().renderers.get(EntityType.PIG);
		if (render instanceof PigRenderer) {
			PigRenderer pigRenderer = (PigRenderer) render;
			pigRenderer.addLayer(new PigPotionItemLayer(pigRenderer));
		}
	}

	// just because logger info gives me all those shit parameters every time!
	public static void info(String msg) {
		AlenBeckerPig.LOGGER.info(msg);
	}

	public static void info(Object msg) {
		AlenBeckerPig.LOGGER.info(msg);
	}

	@SubscribeEvent
	public void joinWorld(EntityJoinWorldEvent event) {
		if (!(event.getEntity() instanceof PigEntity)) {
			return;
		}

		PigEntity pig = (PigEntity) event.getEntity();
		this.addPigGoals(pig);
		pig.clearActivePotions();// faster testing, for now
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
		// pig.goalSelector.addGoal(5, new DrinkPotionsGoal<PigEntity>(pig));// 5
		pig.targetSelector.addGoal(4, new RevengePotionGoal<PlayerEntity>(pig, PlayerEntity.class, true, false));// 0

		pig.goalSelector.addGoal(4, new LeapAtTargetGoal(pig, 0.4F));// 4
		pig.goalSelector.addGoal(5, new MeleeAttackGoal(pig, 1.0D, true));// 5
	}

	@SubscribeEvent
	public void cancelNetherWartCampfirePlacement(PlayerInteractEvent.RightClickBlock event) {
		// allows Nether Wart to be placed only on Soul Campfires
		if (event.getItemStack().getItem().equals(Items.NETHER_WART)
				&& event.getWorld().getBlockState(event.getPos()).getBlock().equals(Blocks.CAMPFIRE)) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public void addWanderTraderTrade(WandererTradesEvent event) {
		event.getGenericTrades().add(new BasicTrade(4, new ItemStack(ItemInit.EDIBLE_NETHER_WART.get()), 5, 10));
		event.getGenericTrades().add(new BasicTrade(64, new ItemStack(ItemInit.NETHER_WART_WAND.get()), 1, 640));
	}

	// NIET WAT IK ZOCHT!!!
	// https://github.com/Schmille/Bamboo2/blob/master/src/main/java/schmille/bamboo2/Bamboo2.java
	// https://github.com/ricksouth/serilum-mc-mods/blob/master/sources/Edibles/src/main/java/com/natamus/edibles/events/EdibleEvent.java

	// ctrl + / to comment out the whole block

//	@SubscribeEvent
//	public void cancelNetherWartPlacement(PlayerInteractEvent.RightClickBlock event) {
//		if (!event.getWorld().isRemote) {
//			return;
//		}
//
//		if (event.getItemStack().getItem().equals(ItemInit.EDIBLE_NETHER_WART.get())
//				&& (!event.getWorld().getBlockState(event.getPos()).getBlock().equals(Blocks.SOUL_SAND))) {
//			event.setCanceled(true);
//			//player.getInventory().setChanged();
//			return;
//		}
//	}
//	
//	@SubscribeEvent
//	public void eatNetherWart(PlayerInteractEvent.RightClickItem event) {
//		PlayerEntity player = event.getPlayer();
//		ItemStack itemStack = event.getItemStack();
//		Item item = itemStack.getItem();
//		if (item.equals(Items.NETHER_WART)) {
//			ItemStack itemStackReplacer = new ItemStack(ItemInit.EDIBLE_NETHER_WART.get(), itemStack.getCount());
//			int slot = player.inventory.currentItem;
//			player.inventory.setInventorySlotContents(slot, itemStackReplacer);
//		}
//	}
//
//	@SubscribeEvent
//	public void startEatNetherWart(LivingEntityUseItemEvent.Start event) {
//		if (!(event.getEntity() instanceof PlayerEntity)) {
//			return;
//		}
//
//		if (!event.getEntity().world.isRemote()) {
//			this.LOGGER.info("start");
//		}
//	}
//
//	@SubscribeEvent
//	public void finishEatNetherWart(LivingEntityUseItemEvent.Finish event) {
//		if (!(event.getEntity() instanceof PlayerEntity)) {
//			return;
//		}
//
//		this.LOGGER.info("finish");
//		ItemStack itemStack = event.getItem();
//		if (itemStack.getItem().equals(ItemInit.EDIBLE_NETHER_WART.get())) {
//			PlayerEntity player = (PlayerEntity) event.getEntity();
//			ItemStack itemStackReplacer = new ItemStack(Items.NETHER_WART, itemStack.getCount());
//			int slot = player.inventory.currentItem;
//			player.inventory.setInventorySlotContents(slot, itemStackReplacer);
//		}
//	}
//
//	@SubscribeEvent
//	public void stopEatNetherWart(LivingEntityUseItemEvent.Stop event) {
//		if (!(event.getEntity() instanceof PlayerEntity)) {
//			return;
//		}
//
//		this.LOGGER.info("stop");
//		ItemStack itemStack = event.getItem();
//		if (itemStack.getItem().equals(ItemInit.EDIBLE_NETHER_WART.get())) {
//			PlayerEntity player = (PlayerEntity) event.getEntity();
//			ItemStack itemStackReplacer = new ItemStack(Items.NETHER_WART.getItem(), itemStack.getCount());
//			int slot = player.inventory.currentItem;
//			player.inventory.setInventorySlotContents(slot, itemStackReplacer);
//		}
//	}

	// going to be static because other classes use this too
	public void dropEmptyBottle(PigEntity pig) {
		// isRemote prevents ghost items
		if (!pig.world.isRemote) {
			if (pig.world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT)) {
				pig.entityDropItem(Items.GLASS_BOTTLE);
			}
		}
	}

	// going to be static because other classes use this too
	// yeahh... we know what happens in here >.>
	public void dropPotionInventory(PigEntity pig) {
		if (!pig.world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT)) {
			return;
		}

		// isRemote prevents ghost items
		if (!pig.world.isRemote) {
			ItemStack itemstack = pig.getItemStackFromSlot(EquipmentSlotType.MAINHAND);
			if (!itemstack.isEmpty()) {
				pig.entityDropItem(itemstack);
				this.clearPotionInventory(pig);
			}
		}
	}

	// going to be static because other classes use this too
	// yeahh... we know what happens in here >.>
	public void clearPotionInventory(PigEntity pig) {
		pig.setItemStackToSlot(EquipmentSlotType.MAINHAND, ItemStack.EMPTY);
	}

	@SubscribeEvent
	public void onHit(LivingAttackEvent event) {
		if (!(event.getEntity() instanceof PigEntity)) {
			return;
		}

		PigEntity pig = (PigEntity) event.getEntity();
		if (pig.isAlive()) {
			this.dropPotionInventory(pig);
		}

	}

	@SubscribeEvent
	public void onDeath(LivingDeathEvent event) {
		if (!(event.getEntity() instanceof PigEntity)) {
			return;
		}

		// our poor pig.. ;w;
		PigEntity pig = (PigEntity) event.getEntity();

		// drops potion if our pig has not fully drunken the potion
		this.dropPotionInventory(pig);

		if (pig.getActivePotionEffect(Effects.HERO_OF_THE_VILLAGE) != null) {
			// 1 op de 200 so 0.5%
			if (pig.world.rand.nextInt(199) == 0) {
				// drop nether wart wand
				pig.entityDropItem(ItemInit.NETHER_WART_WAND.get());
			}

			// 1 op de 100 so 1%
			if (pig.world.rand.nextInt(99) == 0) {
				pig.entityDropItem(ItemInit.EDIBLE_NETHER_WART.get());
			}
		}
	}

}
