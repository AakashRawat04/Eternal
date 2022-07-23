package com.tripsterxx.Eternal;

import com.tripsterxx.Eternal.listners.EventListener;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

import javax.security.auth.login.LoginException;

public class EternalBot {

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
        builder.enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES);
        builder.setStatus(OnlineStatus.ONLINE);
//        builder.setActivity(Activity.watching("Breaking Bad"));
        builder.setActivity(Activity.watching(watching_status));
        shardManager = builder.build();

        //Register listeners
        shardManager.addEventListener(new EventListener());
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