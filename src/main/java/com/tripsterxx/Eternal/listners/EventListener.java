package com.tripsterxx.Eternal.listners;

// Imports
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateOnlineStatusEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import com.tripsterxx.Eternal.EternalBot;

import java.util.List;
import java.util.Objects;

public class EventListener extends ListenerAdapter {

    // specifying channel ids
    public final long bot_logs_channel_id = 998665610995183637L;

    // gets activated when someone in the server changes their online status or comes online or goes offline.
    // also shares total online members in the server.
    @Override
    public void onUserUpdateOnlineStatus(@NotNull UserUpdateOnlineStatusEvent event) {
        List<Member> membersList = event.getGuild().getMembers();

        int online_members = 0;
        for(Member member:membersList){
            if (member.getOnlineStatus() == OnlineStatus.ONLINE){
                online_members++;
            }
            else if (member.getOnlineStatus() == OnlineStatus.IDLE){
                online_members++;
            }
            else if (member.getOnlineStatus() == OnlineStatus.DO_NOT_DISTURB){
                online_members++;
            }
        }

        User user = event.getUser();
        String message = "**"+ user.getAsTag()+"**"+ " updated their online status to: " + event.getNewOnlineStatus().name() + ".\nThere are now "+online_members + " online";
        TextChannel bot_logs = event.getGuild().getTextChannelsByName("bot-logs",true).get(0);

        bot_logs.sendMessage(message).queue();
    }

    // Activates when someone reacts to a message.
    // sends a message and jump url in the bot logs channel.
    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        //user reacted to a message with "thumbs up" in #channel_name
        User user = event.getUser();
        String emoji = event.getReaction().getEmoji().getAsReactionCode();
        String channelMention = event.getChannel().getAsMention();
        String jumpUrl = event.getJumpUrl();

        assert user != null;
        String message = user.getAsTag() + " reacted to a message with " + emoji + " in the " + channelMention + " channel. jump url: "+ jumpUrl;

//        if the message is to be sent into the default first text channel of the server, usually the welcome channel, then use code provided below
//        event.getGuild().getDefaultChannel().asStandardGuildMessageChannel().sendMessage(message).queue();

        TextChannel bot_logs = event.getGuild().getTextChannelsByName("bot-logs",true).get(0);
        bot_logs.sendMessage(message).queue();

        // self role updation.
        String selfRoleMessage = event.getMessageId();
        if (selfRoleMessage.equals("998976223076175882")){
            bot_logs.sendMessage("reaction role added to "+user).queue();
        }
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        // Getting some important information about the user and the messages sent.
        String message = event.getMessage().getContentRaw();
        String name = Objects.requireNonNull(event.getMember()).getEffectiveName();

        // Basic ping command with no ping in ms reply.
        if (message.equals("ping")){
            event.getChannel().sendMessage("hey! Sup " + name).queue();
        }

        // Reacts to when someone pings the bot. could be use in the future to make new users comfortable by sending a message to use a slash command for help

        if (message.equals("<@998293876752253038>")){
            event.getChannel().sendMessage("Hey "+ name + " nice to meet you\n"+"I'm currently watching: " + EternalBot.watching_status).queue();
        }
        // message.getContentRaw().contains("@here")
    }


    //Member join message --> sends message in bot logs channel
    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        TextChannel bot_logs = event.getGuild().getTextChannelsByName("bot-logs",true).get(0);
        TextChannel generalChannel = event.getGuild().getTextChannelsByName("general",true).get(0);
        User user = event.getUser();
        String welcomeMessage = user.getAsMention() + " joined the server!! \nWELCOME and have fun here in " + generalChannel.getAsMention() + " channel. ";
        bot_logs.sendMessage(welcomeMessage).queue();
    }


    //this block of code does not work for the time being.
    //it throws contextException, and somewhere it says it needs to be cached too.
    public void sendMessage(User user, String content) {
        user.openPrivateChannel()
                .flatMap(channel -> channel.sendMessage(content))
                .queue();
    }
    public String msgContent = "hey seems like you left the server";


    //Member left message --> sends message in bot logs channel
    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {

        //Here the bot logs channel could have been found by the getChannelByName but this is to show how to do the same with different approach.
        TextChannel bot_logs = event.getGuild().getTextChannelById(bot_logs_channel_id);
        User user = event.getUser();
        String memberLeftMessage = user.getName() + " left the server!! \nSee you somewhere...";
        assert bot_logs != null;
        bot_logs.sendMessage(memberLeftMessage).queue();
    }

}