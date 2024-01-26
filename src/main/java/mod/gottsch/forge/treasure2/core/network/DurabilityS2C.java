/*
 * This file is part of  Treasure2.
 * Copyright (c) 2024 Mark Gottschling (gottsch)
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

import mod.gottsch.forge.treasure2.Treasure;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

/**
 * 
 * @author Mark Gottschling on Jan 25, 2024
 *
 */
public class DurabilityS2C {
	private final UUID playerUuid;
	private final int slot;
	private final int damage;

	public DurabilityS2C(UUID playerUuid, int slot, int damage) {
		this.playerUuid = playerUuid;
		this.slot = slot;
		this.damage = damage;
	}
	
	public static void encode(DurabilityS2C msg, FriendlyByteBuf buf) {
		buf.writeUUID(msg.playerUuid);
		buf.writeInt(msg.slot);
		buf.writeFloat(msg.damage);
	}
	
	public static DurabilityS2C decode(FriendlyByteBuf buf) {
		UUID uuid = buf.readUUID();
		int slot = buf.readInt();
		int damage = buf.readInt();
	    return new DurabilityS2C(uuid, slot, damage);
	}
	
	public static void handle(DurabilityS2C msg, Supplier<NetworkEvent.Context> context) {
		NetworkEvent.Context ctx = context.get();
		LogicalSide sideReceived = ctx.getDirection().getReceptionSide();

		if (sideReceived != LogicalSide.CLIENT) {
			Treasure.LOGGER.warn("MimicSpawnS2C received on wrong side -> {}", ctx.getDirection().getReceptionSide());
			return;
		}

		context.get().enqueueWork(() -> {
			ClientLevel world = Minecraft.getInstance().level;
			if (world != null) {
//				Player player = world.getPlayerByUUID(msg.playerUuid);
//				Entity entity = world.getEntity(msg.entityId);
//				entity.setYBodyRot(msg.bodyYRot);
			}
		});
		context.get().setPacketHandled(true);
	}


	@Override
	public String toString() {
		return "DurabilityS2C{" +
				"playerUuid=" + playerUuid +
				", slot=" + slot +
				", damage=" + damage +
				'}';
	}
}
