package com.tripsterxx.Eternal.commands;

import com.tripsterxx.Eternal.slashCommandManager.SlashCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class welcome extends SlashCommand {
    public welcome() {
        super("welcome", "simple welcome command");
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        String userTag = event.getUser().getAsTag();
        event.reply("welcome to the server **\"+ userTag+\"**!").queue();
    }
}
