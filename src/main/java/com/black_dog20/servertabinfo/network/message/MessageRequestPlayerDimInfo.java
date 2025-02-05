package com.black_dog20.servertabinfo.network.message;

import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

import com.black_dog20.servertabinfo.network.PacketHandler;
import com.black_dog20.servertabinfo.utility.Helper;
import com.black_dog20.servertabinfo.utility.TpsDimension;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.registry.IRegistry;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class MessageRequestPlayerDimInfo {
	

	@SuppressWarnings("deprecation")
	public static void onMessage(MessageRequestPlayerDimInfo message, Supplier<NetworkEvent.Context> context) {
		MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
		HashMap<String, TpsDimension> playerDims = new HashMap<String, TpsDimension>();

		List<EntityPlayerMP>playerList = server.getPlayerList().getPlayers();
		
		for(EntityPlayerMP player : playerList) {
			String name = IRegistry.field_212622_k.getKey(player.getEntityWorld().dimension.getType()).toString();
			DimensionType type = player.getEntityWorld().dimension.getType();
			Double meanTickTime = Helper.mean(server.getTickTime(type));
			TpsDimension dim = new TpsDimension(name, meanTickTime, type.getId());
			playerDims.put(player.getDisplayName().getFormattedText(), dim);
		}
		
		PacketHandler.network.reply(new MessageResponsePlayerDimInfo(playerDims), context.get());
	}

	public MessageRequestPlayerDimInfo() {}
	


	public void toBytes(PacketBuffer buf) {
	}


	public static MessageRequestPlayerDimInfo fromBytes(PacketBuffer buf) {
		return new MessageRequestPlayerDimInfo();
	}
}
