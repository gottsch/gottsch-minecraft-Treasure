package com.someguyssoftware.treasure2.command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.BlockPosArgument;
import net.minecraft.command.arguments.MessageArgument;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class MySayCommand {
   public static void register(CommandDispatcher<CommandSource> dispatcher) {
//      dispatcher.register(Commands.literal("mysay").requires((source) -> {
//         return source.hasPermissionLevel(2);
//      }).then(Commands.argument("message", MessageArgument.message()).executes((source) -> {
//         ITextComponent itextcomponent = MessageArgument.getMessage(source, "message");
//         source.getSource().getServer().getPlayerList().sendMessage(new TranslationTextComponent("chat.type.announcement", source.getSource().getDisplayName(), itextcomponent));
//         return 1;
//      })));
      
		dispatcher.register(Commands.literal("tchest").requires((source) -> {
			return source.hasPermissionLevel(2);
		}).then(Commands.argument("m", MessageArgument.message()).executes((source) -> {
			Treasure.LOGGER.info("Hello!");
			return 1;
		})));
   }
}