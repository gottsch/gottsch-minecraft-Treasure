package com.someguyssoftware.treasure2.command.argument;

public class DecayArgument implements ArgumentType<IDecayArgument> {

    public static DecayArgument decay() {
		return new DecayArgument();
    }
    
    public static Optional<String> getDecayRuleset(final CommandContext<CommandSource> context, final String name) {
        try {
           return Optional.ofNullable(getArgument(name, String.class));
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