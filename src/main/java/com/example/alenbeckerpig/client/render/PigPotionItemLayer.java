package com.example.alenbeckerpig.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.PigModel;
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

		// renders item as a lightbulb dwb
		/*
		 * matrixStackIn.push(); ItemStack itemstack =
		 * entitylivingbaseIn.getItemStackFromSlot(EquipmentSlotType.MAINHAND);
		 * Minecraft.getInstance().getFirstPersonRenderer().renderItemSide(
		 * entitylivingbaseIn, itemstack, ItemCameraTransforms.TransformType.GROUND,
		 * false, matrixStackIn, bufferIn, packedLightIn); matrixStackIn.pop();
		 */

		// super(6, scale, false, 4.0F, 4.0F, 2.0F, 2.0F, 24);
		// this.headModel.setTextureOffset(16, 16).addBox(-2.0F, 0.0F, -9.0F, 4.0F,
		// 3.0F, 1.0F, scale);

		// new Vector3d(0.0D, (double)(0.6F * this.getEyeHeight()),
		// (double)(this.getWidth() * 0.4F))

		// this.getEntityModel()
		// matrixStackIn.translate((double)((this.getEntityModel()).headModel.rotationPointX
		// / 16.0F), (double)((this.getEntityModel()).headModel.rotationPointY / 16.0F),
		// (double)((this.getEntityModel()).headModel.rotationPointZ / 16.0F));
		// getSwimAnimation
		// getYaw
		// getPitch
		// float f1 = entitylivingbaseIn.func_213475_v(partialTicks);

		matrixStackIn.rotate(Vector3f.YP.rotationDegrees(netHeadYaw));
		matrixStackIn.rotate(Vector3f.XP.rotationDegrees(headPitch));

		// x = left
		// y = up 0.90
		// z = forward
		// this is what i did!
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
