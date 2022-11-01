package baguchan.soulecho.message;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameType;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class CustomRespawnMessage {
	private int entityId;
	public CustomRespawnMessage(int entityId) {
		this.entityId = entityId;
	}

	public void serialize(FriendlyByteBuf buffer) {
		buffer.writeInt(this.entityId);
	}

	public static CustomRespawnMessage deserialize(FriendlyByteBuf buffer) {
		return new CustomRespawnMessage(buffer.readInt());
	}

	public static boolean handle(CustomRespawnMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
		NetworkEvent.Context context = contextSupplier.get();

		if (context.getDirection().getReceptionSide() == LogicalSide.SERVER) {
			context.enqueueWork(() -> {
				ServerPlayer entity = contextSupplier.get().getSender();

				if (entity.getHealth() > 0.0F) {
					return;
				}

					contextSupplier.get().getSender().connection.player = contextSupplier.get().getSender().level.getServer().getPlayerList().respawn(entity, false);

			});
		}

		return true;
	}
}