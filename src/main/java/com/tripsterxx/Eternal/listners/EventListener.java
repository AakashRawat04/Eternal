package com.tripsterxx.Eternal.listners;

import com.tripsterxx.Eternal.EternalBot;
import net.dv8tion.jda.api.entities.Channel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.exceptions.ContextException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class EventListener extends ListenerAdapter {
    public final long bot_logs_channel_id = 998665610995183637L;

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
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        String name = event.getMember().getEffectiveName();

        //useless commands..
        if (message.contains("ping")){
            event.getChannel().sendMessage("pong..").queue();
        }
        if(message.equals("daddy")){
            event.getChannel().sendMessage("aah nooo " + name +".... stop it plssss... aaahhhhh").queue();
        }
        if(message.equals("mommy")){
            event.getChannel().sendMessage("sugar mommy for "+ name +" incoming").queue();
        }
        if(message.equals("ily")){
            if (name.equals("bigfloppa")){
                event.getChannel().sendMessage("mommis most fav boi "+ name).queue();
            }
            event.getChannel().sendMessage("mommy loves "+name).queue();
        }
    }


    //Member join message --> sends message in bot logs channel
    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        TextChannel bot_logs = event.getGuild().getTextChannelsByName("bot-logs",true).get(0);
        TextChannel generalChannel = event.getGuild().getTextChannelsByName("general",true).get(0);
        User user = event.getUser();
        String welcomeMessage = user.getAsTag() + " joined the server!! \nWELCOME and have fun here in " + generalChannel.getAsMention() + " channel. ";
        bot_logs.sendMessage(welcomeMessage).queue();
    }


    //this block of code dose not work for the time being.
    //it throws contextException and somewhere it says it need to be cached too
    public void sendMessage(User user, String content) {
        user.openPrivateChannel()
                .flatMap(channel -> channel.sendMessage(content))
                .queue();
    }
    public String msgContent = "hey seems like you left the server";


    //Member left message --> sends message in bot logs channel
    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {
        TextChannel bot_logs = event.getGuild().getTextChannelById(bot_logs_channel_id);
        User user = event.getUser();
        String memberLeftMessage = user.getName() + " left the server!! \nSee you somewhere...";
        assert bot_logs != null;
        bot_logs.sendMessage(memberLeftMessage).queue();
    }


}