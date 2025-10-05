/*
 * This file is part of Treasure2.
 * Copyright (c) 2025 Mark Gottschling (gottsch)
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the Open Software Licence 3.0.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Open Software Licence 3.0 for more details.
 *
 * You should have received a copy of the Open Software Licence
 * along with Treasure2. If not, see <https://www.tldrlegal.com/license/open-software-licence-3-0>.
 */
package mod.gottsch.forge.treasure2.client.model.entity;

import mod.gottsch.forge.treasure2.core.entity.monster.Mimic;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

/**
 *
 * @author Mark Gottschling on Aug 6, 2024
 *
 */
public abstract class MimicModel<T extends Entity> extends EntityModel<T> {
    protected final float MAX_OPEN_RADIANS = -0.3926991F;
    protected final float BODY_RADIANS = 0.2618F;
    protected final float ONE_SECOND_IN_TICKS = 20F;

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        Mimic mimic = (Mimic) entity;

        // reset
        getLid().xRot = 0f;

        if (mimic.isActive()) {
            activeAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        } else {
            float timeSlice = getTimeSlice(ageInTicks);
            if (timeSlice <= 1F) {
                openAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, timeSlice);
            }
            else {
                mimic.setActive(true);
            }
        }
    }

    /**
     *
     * @param entity
     * @param limbSwing
     * @param limbSwingAmount
     * @param ageInTicks
     * @param netHeadYaw
     * @param headPitch
     */
    public void activeAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        Mimic mimic = (Mimic) entity;

        // default body setup
        getBody().xRot = BODY_RADIANS; // 15 degrees

        // default chomp lid
        if (mimic.hasTarget()) {
            bobMouth(getLid(), 22.5f, 22.5f, ageInTicks);
        } else {
            bobMouth(getLid(), 22.5f, 3f, ageInTicks);
        }

        bob(getBody(), getBodyY(), ageInTicks);

        // concrete classes should override to set any additional animations
    }

    /**
     *
     * @param entity
     * @param limbSwing
     * @param limbSwingAmount
     * @param ageInTicks
     * @param netHeadYaw
     * @param headPitch
     * @param timeSlice
     */
    public abstract void openAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float timeSlice);

    public abstract ModelPart getBody();
    public abstract float getBodyY();
    public abstract ModelPart getLid();

    public float getMaxOpenAngle() {
        return MAX_OPEN_RADIANS;
    }

    // returns a normalized age base on max open event time
    public float getTimeSlice(float ageInTicks) {
        return ageInTicks / getMaxOpenEventTime();
    }

    public float getMaxOpenEventTime() {
        return ONE_SECOND_IN_TICKS;
    }

    public void bob(ModelPart part, float originY, float age) {
        part.y = originY + (Mth.cos(age * 0.25F) * 0.5F + 0.05F);
    }

    public void bobMouth(ModelPart mouth, float originRot, float maxRot, float age) {
        mouth.xRot = -(degToRad(originRot + Mth.cos(age * 0.25f) * maxRot));
    }

    protected static float degToRad(float degrees) {
        return degrees * (float)Math.PI / 180 ;
    }

}
