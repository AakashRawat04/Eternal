package com.tripsterxx.Eternal.commands;

import com.tripsterxx.Eternal.slashCommandManager.SlashCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class test_ping extends SlashCommand {
    public test_ping() {
        super("test_ping","test ping command with command manager!");
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
//        event.deferReply().queue();
        event.getJDA().getRestPing().queue( (time) ->
                event.getChannel().sendMessageFormat("Ping: %d ms", time).queue()
        );
    }
}
