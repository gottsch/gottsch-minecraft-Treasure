package com.someguyssoftware.treasure2.client.model.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.someguyssoftware.treasure2.entity.monster.MimicEntity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

/**
 * Base model for the mimic entities, ported from the 1.20.1 MimicModel. A non-active mimic sits closed and flat like
 * a chest. When it wakes it plays its open animation over one second, then leans forward, bobs and chomps.
 *
 * @author Mark Gottschling on 10/16/2026
 */
public abstract class MimicModel extends ModelBase {
	protected static final float MAX_OPEN_RADIANS = -0.3926991F;
	protected static final float BODY_RADIANS = 0.2618F;
	protected static final float ONE_SECOND_IN_TICKS = 20F;
	/** The rotation of the diamond-shaped teeth. */
	protected static final float TOOTH_RADIANS = 0.7854F;

	/** Parts that would give the disguise away (teeth, flesh), so they are hidden until the mimic wakes. */
	private final List<ModelRenderer> hiddenWhileDormant = new ArrayList<>();

	@Override
	public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
		renderModel(scale);
	}

	/**
	 * Renders the model's root parts. Override when there is more than one root.
	 * @param scale
	 */
	protected void renderModel(float scale) {
		getBody().render(scale);
	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entity) {
		MimicEntity mimic = (MimicEntity) entity;

		// reset
		getBody().rotateAngleX = 0F;
		getBody().rotationPointY = getBodyY();
		getLid().rotateAngleX = 0F;
		for (ModelRenderer part : hiddenWhileDormant) {
			part.showModel = mimic.isActive();
		}

		if (!mimic.isActive()) {
			// sit closed, like a chest
			openAnim(mimic, limbSwing, limbSwingAmount, ageInTicks, 0F);
			return;
		}

		// time since the mimic woke, plus the partial tick
		float openAge = mimic.getActiveTicks() + (ageInTicks - mimic.ticksExisted);
		float timeSlice = openAge / getMaxOpenEventTime();
		if (timeSlice <= 1F) {
			openAnim(mimic, limbSwing, limbSwingAmount, ageInTicks, timeSlice);
		}
		else {
			activeAnim(mimic, limbSwing, limbSwingAmount, ageInTicks);
		}
	}

	/**
	 * Concrete classes should override to set any additional animations.
	 * @param mimic
	 * @param limbSwing
	 * @param limbSwingAmount
	 * @param ageInTicks
	 */
	public void activeAnim(MimicEntity mimic, float limbSwing, float limbSwingAmount, float ageInTicks) {
		getBody().rotateAngleX = BODY_RADIANS; // 15 degrees

		// chomp
		if (mimic.hasTarget()) {
			bobMouth(getLid(), 22.5F, 22.5F, ageInTicks);
		}
		else {
			bobMouth(getLid(), 22.5F, 3F, ageInTicks);
		}

		bob(getBody(), getBodyY(), ageInTicks);
	}

	/**
	 * Poses the model part way open. A time slice of 0 is the closed chest, 1 is fully open. The model is shared by
	 * every mimic of its kind, so this must set every part that activeAnim() moves.
	 * @param mimic
	 * @param limbSwing
	 * @param limbSwingAmount
	 * @param ageInTicks
	 * @param timeSlice
	 */
	public abstract void openAnim(MimicEntity mimic, float limbSwing, float limbSwingAmount, float ageInTicks, float timeSlice);

	public abstract ModelRenderer getBody();
	public abstract float getBodyY();
	public abstract ModelRenderer getLid();

	public float getMaxOpenAngle() {
		return MAX_OPEN_RADIANS;
	}

	public float getMaxOpenEventTime() {
		return ONE_SECOND_IN_TICKS;
	}

	public void bob(ModelRenderer part, float originY, float age) {
		part.rotationPointY = originY + (MathHelper.cos(age * 0.25F) * 0.5F + 0.05F);
	}

	public void bobMouth(ModelRenderer mouth, float originRot, float maxRot, float age) {
		mouth.rotateAngleX = -(degToRad(originRot + MathHelper.cos(age * 0.25F) * maxRot));
	}

	/**
	 * Hides these parts (and their children) while the mimic is non-active.
	 * @param parts
	 */
	protected void hideWhileDormant(ModelRenderer... parts) {
		hiddenWhileDormant.addAll(Arrays.asList(parts));
	}

	/**
	 * Creates a part (no boxes yet) and adds it to its parent. The 1.12 equivalent of addOrReplaceChild() with
	 * PartPose.offsetAndRotation().
	 */
	protected ModelRenderer part(ModelRenderer parent, int texU, int texV, float x, float y, float z, float rotX, float rotY, float rotZ) {
		ModelRenderer part = new ModelRenderer(this, texU, texV);
		part.setRotationPoint(x, y, z);
		part.rotateAngleX = rotX;
		part.rotateAngleY = rotY;
		part.rotateAngleZ = rotZ;
		if (parent != null) {
			parent.addChild(part);
		}
		return part;
	}

	protected ModelRenderer part(ModelRenderer parent, int texU, int texV, float x, float y, float z) {
		return part(parent, texU, texV, x, y, z, 0F, 0F, 0F);
	}

	/**
	 * Adds a flat, diamond-shaped tooth: an empty pivot part holding a square, zero-depth box turned 45 degrees.
	 */
	protected ModelRenderer tooth(ModelRenderer parent, float x, float y, float z, float rotX, float rotY,
			float pivotX, float pivotY, float pivotZ, int texU, int texV, float boxX, float boxY, float boxZ, int size) {
		ModelRenderer tooth = part(parent, 0, 0, x, y, z, rotX, rotY, 0F);
		part(tooth, texU, texV, pivotX, pivotY, pivotZ, 0F, 0F, TOOTH_RADIANS).addBox(boxX, boxY, boxZ, size, size, 0);
		return tooth;
	}

	protected static float degToRad(float degrees) {
		return degrees * (float)Math.PI / 180;
	}
}
