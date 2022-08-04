package com.tripsterxx.Eternal.slashCommandManager;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.*;


import java.util.ArrayList;
import java.util.List;

public abstract class SlashCommand {
    private final String name;
    private final String description;

    public SlashCommand(String name, String description){
        this.name = name;
        this.description = description;
    }

    public abstract void execute(SlashCommandInteractionEvent slashCommandInteractionEvent);

    public synchronized List<CommandData> getInfo(){
        List<CommandData> commandData = new ArrayList<>();
        commandData.add(Commands.slash(name, description));
        return commandData;
    }

    public String getName(){
        return this.name;
    }

    public String getDescription(){
        return this.description;
    }
}
