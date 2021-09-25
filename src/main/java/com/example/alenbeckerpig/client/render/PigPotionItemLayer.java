package com.example.alenbeckerpig.client.render;

import com.example.alenbeckerpig.AlenBeckerPig;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.PigModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PigPotionItemLayer extends LayerRenderer<PigEntity, PigModel<PigEntity>> {

	public PigPotionItemLayer(IEntityRenderer<PigEntity, PigModel<PigEntity>> renderer) {
		super(renderer);
	}

	public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn,
			PigEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks,
			float netHeadYaw, float headPitch) {

		matrixStackIn.push();

		// renders item as a lightbulb dwb
		/*
		 * matrixStackIn.push(); ItemStack itemstack =
		 * entitylivingbaseIn.getItemStackFromSlot(EquipmentSlotType.MAINHAND);
		 * Minecraft.getInstance().getFirstPersonRenderer().renderItemSide(
		 * entitylivingbaseIn, itemstack, ItemCameraTransforms.TransformType.GROUND,
		 * false, matrixStackIn, bufferIn, packedLightIn); matrixStackIn.pop();
		 */

		// this comes out of pig model
		// super(6, scale, false, 4.0F, 4.0F, 2.0F, 2.0F, 24);
		// this.headModel.setTextureOffset(16, 16).addBox(-2.0F, 0.0F, -9.0F, 4.0F,
		// 3.0F, 1.0F, scale);

		// new Vector3d(0.0D, (double)(0.6F * this.getEyeHeight()),
		// (double)(this.getWidth() * 0.4F))

		// this.getEntityModel()
		/*
		 * matrixStackIn.translate((double)((this.getEntityModel()).headModel.
		 * rotationPointX / 16.0F),
		 * (double)(this.getEntityModel()).headModel.rotationPointY / 16.0F),
		 * (double)((this.getEntityModel()).headModel.rotationPointZ / 16.0F));
		 */
		// getSwimAnimation
		// getYaw
		// getPitch
		// float f1 = entitylivingbaseIn.func_213475_v(partialTicks);

		// entitylivingbaseIn.rotationPitch
		// AlenBeckerPig.LOGGER.info(entitylivingbaseIn.getMotion());

		// goes up in minus when looking up and down to + when it looks down
		// AlenBeckerPig.LOGGER.info(entitylivingbaseIn.rotationPitch);

		// goes more up in minus when looking up and down to + when it looks down then
		// the one above
		// AlenBeckerPig.LOGGER.info(entitylivingbaseIn.rotationYaw);

		// goes 360 left and right
		// AlenBeckerPig.LOGGER.info(entitylivingbaseIn.rotationYawHead);

		// AlenBeckerPig.LOGGER.info(netHeadYaw);

		// AlenBeckerPig.LOGGER.info(headPitch);

		// rotates up and right
		matrixStackIn.rotate(Vector3f.YP.rotationDegrees(netHeadYaw));
		matrixStackIn.rotate(Vector3f.XP.rotationDegrees(headPitch));

		// x = left
		// y = up 0.90
		// z = forward
		// this is what i did!, static forward
		matrixStackIn.translate((double) 0.00F, (double) 0.975F, -1.155D);
		matrixStackIn.rotate(Vector3f.XP.rotationDegrees(90.0F));

		ItemStack itemstack = entitylivingbaseIn.getItemStackFromSlot(EquipmentSlotType.MAINHAND);
		// Minecraft.getInstance().getFirstPersonRenderer().renderItemSide(entitylivingbaseIn,
		// itemstack, ItemCameraTransforms.TransformType.GROUND, false, matrixStackIn,
		// bufferIn, packedLightIn);
		matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90.0F));
		Minecraft.getInstance().getFirstPersonRenderer().renderItemSide(entitylivingbaseIn, itemstack,
				ItemCameraTransforms.TransformType.GROUND, false, matrixStackIn, bufferIn, packedLightIn);
		matrixStackIn.pop();

	}

}
