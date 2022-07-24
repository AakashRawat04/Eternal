package com.tripsterxx.Eternal.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

/**
 * Global:
 * These commands are available in every server your bot is in (regardless of sharding!) and direct message (Private Channels).
 * These commands can take up to 1 hour to show up.
 * It is recommended to use guild commands for testing purposes.(unlimited)
 *<br/>
 *<br/>
 * Guild:
 * These commands are only in the specific guild that you created them in and cannot be used in direct messages.
 * These commands show up immediately after creation.(max 100)
 *<br/>
 *<br/>
 * NOTE:
 *<br/>
 * When onSlashCommandInteraction is triggered the bot has 3 seconds to respond to the message.
 *if the command needs more time to reply, then use of deferReply() is recommended which tells the discord to wait a little longer than usual.
 */


public class CommandManager extends ListenerAdapter {
    //slash commands integration v1.0.0
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName(); // /hello -> command = hello

        if (command.equals("welcome")){
            // run the '/welcome' command.
            String userTag = event.getUser().getAsTag();
            event.reply("welcome to the server **"+ userTag+"**!").queue();
        }

        else if (command.equals("welcome-global")){
            // run the '/welcome-global' command.
            String userTag = event.getUser().getAsTag();
            event.reply("Hey **"+ userTag+"**!\nThis is a global command for this bot!\n\nYou are currently in"+
                    event.getGuild().getName() + " server.").queue();
        }

        // use of deferred reply.
        else if (command.equals("get-roles")) {
            // run the slash roles command.(deferred replies
            List<Role> roleList;

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

        else if (command.equals("say")){
            OptionMapping messageOption = event.getOption("message");
            OptionMapping channelOption = event.getOption("channel");

            MessageChannel channel;
            if (channelOption!=null){
                channel = channelOption.getAsChannel().asGuildMessageChannel();
            }
            else {
                channel = event.getChannel();
            }
            String message = "";
            if (messageOption!=null){
                message = messageOption.getAsString();
            }
            channel.sendMessage(message).queue();
            event.reply("your message was sent").setEphemeral(true).queue();
        }

        else if (command.equals("emote")) {
            OptionMapping option = event.getOption("type");
            String type = option.getAsString();
            Member member =  event.getMember();

            String replyMessage = "";
            switch (type.toLowerCase()){
                case "hug"->{
                    replyMessage += "A hug from the Lovely Eternal Bot to Lonely " + member.getAsMention();
                }
                case "laugh"->{
                    replyMessage+="haha you are soo funny sir!";
                }

                case "sad"->{
                    replyMessage+="It is ok to be sad " + member.getAsMention();
                }

                case "cry"->{
                    replyMessage+="Cry hard! " + member.getAsMention() + ". just like you do at 3AM in the night\n EVERYDAY!!!!";
                }
            }

            event.reply(replyMessage).queue();
        }

        else if (command.equals("give-role")) {
            Member member = event.getOption("user").getAsMember();
            Role role = event.getOption("role").getAsRole();

            String commandUser = event.getMember().getAsMention();

            //checking for administrator permission.
            if (member.hasPermission(Permission.ADMINISTRATOR)){
                event.getGuild().addRoleToMember(member,role).queue();
                event.reply(member.getAsMention() + " has been given the " + role.getAsMention() + " role!").queue();
            }
            else {
                event.reply(commandUser + " is trying to give " + role.getName() + " to " + member.getEffectiveName()).queue();
            }

        }

        else if (command.equals("remove-role")) {
            Member member = event.getOption("user").getAsMember();
            Role role = event.getOption("role").getAsRole();

            String commandUser = event.getMember().getAsMention();
            // checking for administrator permission..
            if (member.hasPermission(Permission.ADMINISTRATOR)){
                event.getGuild().removeRoleFromMember(member,role).queue();
                event.reply(role.getAsMention() + " role has been taken from " + member.getAsMention()).queue();
            }
            else {
                event.reply(commandUser + " is trying to remove " + role.getAsMention() + " from " + member.getAsMention()).queue();
            }
        }

    }


    // updating for guild commands.
    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        List<CommandData> commandData = new ArrayList<>();

        commandData.add(Commands.slash("welcome", "Get welcomed by the bot"));
        commandData.add(Commands.slash("get-roles","shows all the roles in the server"));


        // /say command --> /say <message> [channel]
        OptionData option1 = new OptionData(OptionType.STRING, "message","The message you want the bot to say", true);
        OptionData option2 = new OptionData(OptionType.CHANNEL, "channel", "the channel you want to send this message in. current channel is default.", false)
                .setChannelTypes(ChannelType.TEXT, ChannelType.GUILD_PUBLIC_THREAD, ChannelType.NEWS);

        commandData.add(Commands.slash("say","make the bot say a message")
                .addOptions(option1, option2));


        //Command /emote [type]
        OptionData emoteOption = new OptionData(OptionType.STRING, "type", "the type of emotion to express",true)
                .addChoice("Hug","hug")
                .addChoice("laugh", "laugh")
                .addChoice("sad","sad")
                .addChoice("cry", "cry");

        commandData.add(Commands.slash("emote", "express your emotions through text")
                .addOptions(emoteOption));


        //Command: /give-role <user> <role>
        OptionData userGettingRole = new OptionData(OptionType.USER, "user", "The user to give the role to.", true);
        OptionData roleToGive = new OptionData(OptionType.ROLE, "role", "The role to be given", true);

        commandData.add(Commands.slash("give-role","give a user a role")
                .addOptions(userGettingRole,roleToGive));


        //Command: /remove-role <user> <role>
        OptionData userToRemoveRole = new OptionData(OptionType.USER, "user", "The user to remove role from.", true);
        OptionData roleToRemove = new OptionData(OptionType.ROLE, "role", "The role to be removed", true);

        commandData.add(Commands.slash("remove-role", "removes role from the user tagged")
                .addOptions(userToRemoveRole, roleToRemove));

        event.getGuild().updateCommands().addCommands(commandData).queue();
    }



    //updating for global commands.
    @Override
    public void onReady(@NotNull ReadyEvent event) {
        List<CommandData> commandData = new ArrayList<>();
        commandData.add(Commands.slash("welcome-global", "Get welcomed by the bot"));
        event.getJDA().updateCommands().addCommands(commandData).queue();
    }
}
