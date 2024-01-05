package mod.gottsch.forge.treasure2.core.command;

import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import net.minecraft.command.arguments.ItemArgument;

public class CharmItemArgument extends ItemArgument {

	   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> p_listSuggestions_1_, SuggestionsBuilder sb) {
		   return sb.suggest("treasure2:copper_charm").buildFuture();
	   }
}
