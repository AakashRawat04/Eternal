package com.tripsterxx.Eternal;

import com.tripsterxx.Eternal.Music.MusicPlayer;
import com.tripsterxx.Eternal.commands.CommandManager;
import com.tripsterxx.Eternal.listners.EventListener;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;

public class EternalBot {

    //Bot version 1.0.0

    //Defining Final Keywords.
    private final Dotenv config;
    private final ShardManager shardManager;
    public static final String watching_status = "Breaking Bad";


    //EternalBot class constructor (setting up the bot --> status and activity are also defined here.)
    public EternalBot() throws LoginException {
        //Loading up token from dotenv file.
        config = Dotenv.configure().load();
        String TOKEN = config.get("TOKEN");

        //Sharding
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(TOKEN);
        builder.enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_PRESENCES);
        builder.setStatus(OnlineStatus.ONLINE);
//        builder.setActivity(Activity.watching("Breaking Bad"));
        builder.setActivity(Activity.watching(watching_status));
        builder.setMemberCachePolicy(MemberCachePolicy.ALL); // it is going to cache all the members using lazy loading which means the data is being loaded slowly.(we are caching the members)
        builder.setChunkingFilter(ChunkingFilter.ALL); // it forces your bot to cache all the users. (we are storing them all at the startup)
        builder.enableCache(CacheFlag.ONLINE_STATUS); //(making sure we store their online status)
        builder.enableCache(CacheFlag.VOICE_STATE); // (making sure we are caching the voice state)
        shardManager = builder.build();

        //Register listeners
        shardManager.addEventListener(
                new EventListener(),
                new CommandManager()
        );
    }

    //some getters.
    public Dotenv getConfig(){
        return config;
    }
    public ShardManager getShardManager(){
        return shardManager;
    }

    // Initializing the bot--> makes the bot online.
    public static void main(String[] args) {
        try{
            EternalBot bot = new EternalBot();
        }catch (LoginException e){
            System.out.println("ERROR: Provided Bot Token is Invalid!");
            System.out.println(e.getMessage());
        }
    }
}