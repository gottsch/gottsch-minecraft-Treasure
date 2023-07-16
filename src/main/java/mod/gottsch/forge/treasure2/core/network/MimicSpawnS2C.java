/*
 * This file is part of  Treasure2.
 * Copyright (c) 2023 Mark Gottschling (gottsch)
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Treasure2.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */
package mod.gottsch.forge.treasure2.core.network;

import java.util.function.Supplier;

import mod.gottsch.forge.treasure2.Treasure;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

/**
 * 
 * @author Mark Gottschling on Jun 8, 2023
 *
 */
public class MimicSpawnS2C {
	private final int entityId;
	private final float bodyYRot;
	
	public MimicSpawnS2C(int entityId, float bodyYRot) {
		this.entityId = entityId;
		this.bodyYRot = bodyYRot;
	}
	
	public static void encode(MimicSpawnS2C msg, FriendlyByteBuf buf) {
		buf.writeInt(msg.entityId);
		buf.writeFloat(msg.bodyYRot);
	}
	
	public static MimicSpawnS2C decode(FriendlyByteBuf buf) {
		int entityId = buf.readInt();
		float bodyYRot = buf.readFloat();
	    return new MimicSpawnS2C(entityId, bodyYRot);
	}
	
	public static void handle(MimicSpawnS2C msg, Supplier<NetworkEvent.Context> context) {
		NetworkEvent.Context ctx = context.get();
		LogicalSide sideReceived = ctx.getDirection().getReceptionSide();

		if (sideReceived != LogicalSide.CLIENT) {
			Treasure.LOGGER.warn("MimicSpawnS2C received on wrong side -> {}", ctx.getDirection().getReceptionSide());
			return;
		}
		
		context.get().enqueueWork(() -> {
			ClientLevel world = Minecraft.getInstance().level;
			if (world != null) {
				Entity entity = world.getEntity(msg.entityId);
				entity.setYBodyRot(msg.bodyYRot);
			}
		});
		context.get().setPacketHandled(true);
	}

	@Override
	public String toString() {
		return "MimicSpawnS2C [entityId=" + entityId + ", bodyYRot=" + bodyYRot + "]";
	}
	
	
}
