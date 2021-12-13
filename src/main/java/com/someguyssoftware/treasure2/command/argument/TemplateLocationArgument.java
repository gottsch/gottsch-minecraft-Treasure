package com.someguyssoftware.treasure2.command.argument;

import java.util.Collection;
import java.util.Optional;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import net.minecraft.command.CommandSource;
import net.minecraft.network.chat.TranslatableComponent;

public class TemplateLocationArgument implements ArgumentType<TemplateLocation> {
	private static final Collection<String> EXAMPLES = ImmutableList.of("x", "y", "z");
	
	private static final SimpleCommandExceptionType INVALID_LOCATION_EXCEPTION = new SimpleCommandExceptionType(new TranslatableComponent("arguments.template.location.invalid"));
	
	public static TemplateLocationArgument templateLocation() {
		return new TemplateLocationArgument();
	}
	
	public static Optional<TemplateLocation> getTemplateLocation(final CommandContext<CommandSource> context, final String name) {
        try {
           return Optional.ofNullable(context.getArgument(name, TemplateLocation.class));
        }
        catch(Exception e) {
            return Optional.empty();
        }
	}
	
	@Override
	public TemplateLocation parse(StringReader reader) throws CommandSyntaxException {
	      int i = reader.getCursor();
	      String modID = reader.readString();
	      if (reader.canRead() && reader.peek() == ' ') {
	         reader.skip();
		      String archetype = reader.readString();
	         if (reader.canRead() && reader.peek() == ' ') {
	            reader.skip();
			      String name = reader.readString();
	            return new TemplateLocation(modID, archetype, name);
	         } else {
	            reader.setCursor(i);
	            throw INVALID_LOCATION_EXCEPTION.create();
	         }
	      } else {
	         reader.setCursor(i);
	         throw INVALID_LOCATION_EXCEPTION.create();
	      }
	}
	
	@Override
    public Collection<String> getExamples() {
        return EXAMPLES;
     }
}
