package com.tripsterxx.Eternal.commands;

import com.tripsterxx.Eternal.Music.MusicPlayer;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.AutoCompleteQuery;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

import javax.swing.tree.ExpandVetoException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
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
 *
 * @author <b><a href="https://github.com/tripsterxx">Ash</a></b>
 */


public class CommandManager extends ListenerAdapter {

    public static boolean isURL(String url) {
        try {
            new URI(url);
            return true;
        } catch (URISyntaxException e) {
            return false;
        }
    }
    /**
     * <h2>Slash Commands Integration</h2>
     * <p>
     *     The <b>onSlashCommandInteraction</b> is an event which triggers when someone types ` / ` at the start of their message,
     *     and the discord from this point does the work of showing all the slash commands of all the bots available
     *     on the server.
     *     <br/>
     * </p>
     * <p>All the commands in the if statements below are being checked and the one which matches further gets executed.</p>
     *
     * <p>
     *      Slash commands are a new way of interacting with the bot which is actually a better way because now the user
     *      knows how to interact with the bot which makes it simple for the user to understand how to use the command
     *      correctly and also how to
     * </p>
     *
     * <br>
     * <b>Slash commands version: v1.0.0</b>
     *
     * @author <b><a href="https://github.com/tripsterxx">Ash</a></b>
     */

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
            event.reply("Hey **"+ userTag+"**!\nThis is a global command for this bot!\n\nYou are currently in "+
                    event.getGuild().getName() + " server.").queue();
        }

        // use of deferred reply. ( getting all the roles from a server might take some time!! )
        else if (command.equals("get-roles")) {
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

        else if (command.equals("emotion")) {
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

        // command for admin users ( for giving roles to a member --> requires administrator permission)
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

        //music commands v1.0.0
        else if (command.equals("play")) {
            if (!event.getMember().getVoiceState().inAudioChannel()){
                event.getChannel().sendMessage("You are not in a voice channel! \n" +
                        "please join <#1001838124424495114> vc for listening music!").queue();
            }

            OptionMapping link = event.getOption("song_name");
            String songLink = link.toString();
            if(!isURL(songLink)){
                songLink = "ytsearch:" + link + " audio";
            }

            final AudioManager audioManager = event.getGuild().getAudioManager();
            final VoiceChannel eternalVoiceChannel = event.getGuild().getVoiceChannelById("1001838124424495114");
            audioManager.openAudioConnection(eternalVoiceChannel);

            MusicPlayer.getINSTANCE().loadAndPlay((TextChannel) event.getChannel(), songLink);
        }

        else if (command.equals("skip")) {
            if (!event.getMember().getVoiceState().inAudioChannel()){
                event.getChannel().sendMessage("You are not in a voice channel! \n" +
                        "please join <#1001838124424495114> vc for listening music!").queue();
            }
            TextChannel textChannel = event.getChannel().asTextChannel();
            event.getChannel().sendMessage("skipped song").queue();
            MusicPlayer.skipTrack(textChannel);
        }
    }


    /**
     * <b>Updating for guild commands</b>
     * <br>
     * <br>
     * The <b>onGuildReady</b> is an event which triggers when the bot comes online in a server.
     * <br>
     * <p>
     *     This event is currently used for registering slash commands on the server.
     *     An empty <i>list</i> of commands have been made of type <b>CommandData</b>
     *     at the top for storing all the commands and then after creating and adding all the commands and options for the command,
     *     we just add the List of commands at the end to ``addCommand()``function.
     * </p>
     * <p>This is how slash commands are being created and registered in discord by the bot for the time being. </p>
     * @author <b><a href="https://github.com/tripsterxx">Ash</a></b>
     */
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


        //Command: /give-role <userGettingRole> <roleToGive>
        OptionData userGettingRole = new OptionData(OptionType.USER, "user_getting_role", "The user to give the role to.", true);
        OptionData roleToGive = new OptionData(OptionType.ROLE, "role_to_give", "The role to be given", true);

        commandData.add(Commands.slash("give-role","give a user a role")
                .addOptions(userGettingRole,roleToGive));


        //Command: /remove-role <userToRemoveRole> <roleToRemove>
        OptionData userToRemoveRole = new OptionData(OptionType.USER, "user_to_remove_role", "The user to remove role from.", true);
        OptionData roleToRemove = new OptionData(OptionType.ROLE, "role_to_remove", "The role to be removed", true);

        commandData.add(Commands.slash("remove-role", "removes role from the user tagged")
                .addOptions(userToRemoveRole, roleToRemove));


        //Command: /play <song_name>
        OptionData song_name = new OptionData(OptionType.STRING, "song_name", "The name of the song to be played", true);

        commandData.add(Commands.slash("play", "play and listen songs in the vc")
                .addOptions(song_name));


        //Command: /skip simply skips the current song.
        commandData.add(Commands.slash("skip","skips the current track"));

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
