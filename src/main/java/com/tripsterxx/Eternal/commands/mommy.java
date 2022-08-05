package com.tripsterxx.Eternal.commands;

import com.tripsterxx.Eternal.slashCommandManager.SlashCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class mommy extends SlashCommand {

    public mommy() {
        super("mommy", "Your sugar mommy commadn");
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.getChannel().sendMessage("dont disturb me! I have work to do so that i can feed YOU").queue();
    }
}
