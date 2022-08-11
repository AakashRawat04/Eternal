package com.tripsterxx.Eternal.commands;

import com.tripsterxx.Eternal.slashCommandManager.SlashCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.List;

public class get_roles extends SlashCommand {
    public get_roles() {
        super("get-roles", "shows all the roles in the server!");
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        // run the slash roles command.(deferred replies)
        List<Role> roleList;

        // checking if the user has the permission to manage roles.
        if (event.getMember().hasPermission(Permission.MANAGE_ROLES)){
            roleList = event.getGuild().getRoles();
            event.deferReply().queue();
            String response = "";
            for (Role role: roleList){
                response += role.getAsMention() + "\n";
            }
            event.getHook().sendMessage(response).queue();
        }
        else {
            event.getChannel().sendMessage("you don't have permission to use this command!").queue();
        }
    }
}
