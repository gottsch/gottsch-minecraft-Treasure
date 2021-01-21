package com.someguyssoftware.treasure2.command.argument;

import java.util.Optional;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandSource;

public class DecayArgument implements ArgumentType<IDecayArgument> {

    public static DecayArgument decay() {
		return new DecayArgument();
    }
    
    public static Optional<String> getDecayRuleset(final CommandContext<CommandSource> context, final String name) {
        try {
           return Optional.ofNullable(context.getArgument(name, String.class));
        }
        catch(Exception e) {
            return Optional.empty();
        }
    }
    
    @Override
	public IDecayArgument parse(StringReader reader) throws CommandSyntaxException {
		// TODO Auto-generated method stub
		return null;
	}
}