package com.someguyssoftware.treasure2.command.argument;

import java.util.Optional;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandSource;

public class TemplateLocationArgument implements ArgumentType<ITemplateLocationArgument> {

	
	public static TemplateLocationArgument templateLocation() {
		return new TemplateLocationArgument();
	}
	
	public static Optional<TemplateLocation> getTemplateLocation(final CommandContext<CommandSource> context, final String name) {
        try {
           return Optional.ofNullable(getArgument(name, TemplateLocation.class));
        }
        catch(Exception e) {
            return Optional.empty();
        }
	}
	
	@Override
	public ITemplateLocationArgument parse(StringReader reader) throws CommandSyntaxException {
		// TODO Auto-generated method stub
		return null;
	}

	public static class TemplateLocation {
		private String modID;
		public String getModID() {
			return modID;
		}

		public String getArcheType() {
			return archeType;
		}

		public String getName() {
			return name;
		}

		private String archeType;
		private String name;
		
		public TemplateLocation(String modID, String archeType, String name) {
			this.modID = modID;
			this.archeType = archeType;
			this.name = name;
		}
	}
}
