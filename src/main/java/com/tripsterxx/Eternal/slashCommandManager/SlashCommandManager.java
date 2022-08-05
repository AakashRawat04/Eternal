package com.tripsterxx.Eternal.slashCommandManager;

import lombok.NonNull;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SlashCommandManager {
    private final JDA jda;
    private final SlashEventHandler eventHandler;
    private final Map<String, SlashCommand> slashCommandCollection = new HashMap<>();

    public SlashCommandManager(JDA jda){
        this.jda = jda;
        eventHandler = new SlashEventHandler(this);
    }

    public void addCommand(@NonNull SlashCommand command) {
        // updating for guild commands.
        Guild guild = jda.getGuildById(908203872454062111L);
        assert guild != null;
        guild.updateCommands().addCommands(command.getInfo()).queue();
        slashCommandCollection.put(command.getName(), command);
    }

    public void addCommands(@NonNull SlashCommand ...commands){
        Arrays.stream(commands).forEach(this::addCommand);
    }

    public synchronized void listen(){
        jda.addEventListener(this.eventHandler);
    }

    public Map<String, SlashCommand> getSlashCommandCollection(){
        return this.slashCommandCollection;
    }
}
