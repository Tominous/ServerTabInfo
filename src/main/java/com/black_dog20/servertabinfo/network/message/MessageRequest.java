package com.black_dog20.servertabinfo.network.message;


import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import com.black_dog20.servertabinfo.network.PacketHandler;
import com.black_dog20.servertabinfo.reference.Constants;
import com.black_dog20.servertabinfo.utility.Helper;
import com.black_dog20.servertabinfo.utility.TpsDimension;

import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.registry.IRegistry;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;


public class MessageRequest {

	@SuppressWarnings("deprecation")
	public static void onMessage(MessageRequest message, Supplier<NetworkEvent.Context> context) {

		List<TpsDimension> dims = new ArrayList<TpsDimension>();
		MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
		int ping = context.get().getSender().ping;
	
		dims.add(new TpsDimension("gui.servertabinfo.overall" , Helper.mean(server.tickTimeArray),Constants.VERSION));
		
		for(WorldServer world : server.forgeGetWorldMap().values()) {
			String name = IRegistry.field_212622_k.getKey(world.dimension.getType()).toString();
			if(name.equals(null)) {
				name = "";
			}
			dims.add(new TpsDimension(name, Helper.mean(server.getTickTime(world.dimension.getType())), world.dimension.getType().getId()));
		
			PacketHandler.network.reply(new MessageResponseServerInfo(Constants.VERSION, dims, ping), context.get());
		}
	}

	public MessageRequest() {}

	public void toBytes(PacketBuffer buf) {
	}

	public static MessageRequest fromBytes(PacketBuffer buf) {
		return new MessageRequest();
	}
}
