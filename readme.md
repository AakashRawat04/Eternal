# Eternal - Discord Bot

Eternal is a Discord Bot written in JAVA using JDA which is a java wrapper for Discord API.

#### Table of content's:
- [Features](#features)

- [Tech Stack](#tech-stack)

- [Set-up (intllij idea)](#setting-up-the-bot)
  - [Step -- 1](#step-1)
  - [Step -- 2](#step-2)
  - [Step -- 3](#step-3)

- [Optimizations](#optimizations)

- [Related](#related)

- [Slash Command Manager](#slash-command-manager)
  - [A working example](#a-working-example)

- [Authors](#authors)
### Features

- User Commands.
- Moderation Commands For Moderators.
- Advanced Commands For Admin Users.
- Music Commands.
- Every Type Of Commands for every type of users are Available.

## Tech Stack

**Language Used :** Java.

**API wrapper used :** JDA(java discord api) (v5.0.0-alpha.16).

**DataBase :** MongoDb to be used in future.


*****
## Setting up the bot!

We highly recommend you to use intellij idea for at least the development of this bot as we are going to share the set-up process of only intellij idea. Using any other IDE is also encouraged, there is no big difference in setting up the IDE. You can just take hint from the steps given below.

#### Step 1. 
Open intellij idea and make a new project from the start.

- Put the name of the Bot according to you.
- Put the location where you want the project to be located.
- Select the build system to Maven.
- Select the java jdk version (version 8 or higher than 8 is recommended).
- In advanced setting do as per described in the image.
- Create the project.

<img src="https://cdn.discordapp.com/attachments/999332296396124251/999658678837850132/step1.png" width="700" height= "600" alt="step 1">

#### Step 2.
It is highly recommended to change/refactor the Main.java file.
- -> right click Main.java --> Refactor -->Rename
- Provide the name of your bot.
- Good to go!!

<img src="https://cdn.discordapp.com/attachments/999332296396124251/999658731044339863/step2.png" width="610" height="390" alt="step2-1">
<br />
<br />
<img src="https://cdn.discordapp.com/attachments/999332296396124251/999658766058389526/step3.png" width="450" alt="step2-2">


#### Step 3.
You can replace (copy -> paste) the pom.xml file content's yourself or you can just get it yourself while pulling this remote repository onto your local computer. 

>**Do not forget to click on reload maven changes option when you do any changes in you pom.xml file.**

<br />
Things ahead of this process can be just done by pulling this repository.
<br /> 
<br /> 
<img src="https://cdn.discordapp.com/attachments/999332296396124251/999658834652057700/step4.png" width="450" height = "250" alt="step2-2">

*if you find any difficulty in doing anything you can just go to our server for help. the link to the server is given somewhere below.
*****
## Optimizations

A lot of features are being pushed continously. If you want to contribute to the community you can open pull requests.
Anyone offering good features to the bot is welcome.

## Related

We have created this bot using JDA(v5.0.0-alpha.16) (java discord api), which is a java wrapper for the discord api and it is widely used in the java community for making Discord Bots.
  
The documentation of JDA is available in the link below, and if you want to contribute to this project, then understanding JDA is an essential part.
  
  You can read the documentation and the Java docs for the wrapper from links available below.
  
  There is also an official discrod server of JDA, you can join that server if you need and help regarding JDA.

[JDA Documentation](https://github.com/matiassingers/awesome-readme)
  
[JDA Java Docs](https://ci.dv8tion.net/job/JDA5/javadoc/)

[JDA Discord Server](https://discord.gg/jda)

*****
## Slash Command Manager
Slash commands is the new way to interact with the bots, which is actually a better way. It desent require the user to remember the commands completely. There are other good facts about slash commands, but this is not the correct place to talk about it.

Slash commands can be added to the bot just by simple <u>*if else statement's*</u> too but when we talk about scalability it is never recommended to implement slash commads using if-else conditional statement's. 

It is because, when there are lots of command implementation's in just one file using *if-else statements*, then the code becomes messy. It becomes difficult to find errors as all the commands are implemented in the same file (*which might eventually become a file containing thousands of lines of code which is definately not a good sign of a good code base*).

Due to this problem we use <u>**command managers**</u>, so that the code remains more organized and readable.

#### A Working Example

> *A working example of slash commadn manager is demonstrated here in this repository. You can find the working example [here](https://github.com/tripsterxx/Eternal/tree/slashcommandmanager).*

For demonstration purpose a [test_ping](https://github.com/tripsterxx/Eternal/blob/slashcommandmanager/src/main/java/com/tripsterxx/Eternal/commands/test_ping.java) command has been made which returns the actual ping of the bot.

First of all( we need to make a file named same as the command name), we need to extend [SlashCommad](https://github.com/tripsterxx/Eternal/blob/36bb753c35b1be0dcdb6831a33d2a9aa56d0900d/src/main/java/com/tripsterxx/Eternal/slashCommandManager/SlashCommand.java) class, which is an abstract class having an abstract method [execute](https://github.com/tripsterxx/Eternal/blob/36bb753c35b1be0dcdb6831a33d2a9aa56d0900d/src/main/java/com/tripsterxx/Eternal/slashCommandManager/SlashCommand.java#L19), which means we need to implement it while creating a new command. <u>**This execute method is the mothod where all the core logic of the command goes in.**</u>

>*It is usually recommended to make a package named commands for organizing all the commands. This is what I have done at least in my case.*

- #### Here in our case, [SlashCommadManager](https://github.com/tripsterxx/Eternal/blob/36bb753c35b1be0dcdb6831a33d2a9aa56d0900d/src/main/java/com/tripsterxx/Eternal/slashCommandManager/SlashCommandManager.java) is the **Main Class**.

We make the object of it in the main [Eternal bot class](https://github.com/tripsterxx/Eternal/blob/slashcommandmanager/src/main/java/com/tripsterxx/Eternal/EternalBot.java#L63).
The constructor of the SlashCommadManager class required an jda object as an argument.

After initiating the [SlashCommadManager class object](https://github.com/tripsterxx/Eternal/blob/36bb753c35b1be0dcdb6831a33d2a9aa56d0900d/src/main/java/com/tripsterxx/Eternal/EternalBot.java#L63) we can then start making commands and eventually call [addCommands](https://github.com/tripsterxx/Eternal/blob/36bb753c35b1be0dcdb6831a33d2a9aa56d0900d/src/main/java/com/tripsterxx/Eternal/EternalBot.java#L64) method to add those commands. (Multiple commands can be added with the help of commas). We need to call the [listen method](https://github.com/tripsterxx/Eternal/blob/36bb753c35b1be0dcdb6831a33d2a9aa56d0900d/src/main/java/com/tripsterxx/Eternal/slashCommandManager/SlashCommandManager.java#L29) in the end and before adding all the commands, which [simply adds an event listner](https://github.com/tripsterxx/Eternal/blob/36bb753c35b1be0dcdb6831a33d2a9aa56d0900d/src/main/java/com/tripsterxx/Eternal/slashCommandManager/SlashCommandManager.java#L29), because of which we get access of all the method of JDA.

[SlashEventHandler](https://github.com/tripsterxx/Eternal/blob/dev/src/main/java/com/tripsterxx/Eternal/slashCommandManager/SlashEventHandler.java) is the class which extends ListnerAdapter class from JDA. The [Constructor of slash event handler](https://github.com/tripsterxx/Eternal/blob/36bb753c35b1be0dcdb6831a33d2a9aa56d0900d/src/main/java/com/tripsterxx/Eternal/slashCommandManager/SlashEventHandler.java#L11) acts as a setter which sets the final variable slash command manager to given Slash Command Manager in the parameters.

This SlashEventHandler class listens for all the [onSlashCommandInteraction](https://github.com/tripsterxx/Eternal/blob/36bb753c35b1be0dcdb6831a33d2a9aa56d0900d/src/main/java/com/tripsterxx/Eternal/slashCommandManager/SlashEventHandler.java#L16) events and checks if the command is available in the bot's command list or not. If the command provided gets matched then it stops searching for the command and runs the [execute method](https://github.com/tripsterxx/Eternal/blob/36bb753c35b1be0dcdb6831a33d2a9aa56d0900d/src/main/java/com/tripsterxx/Eternal/commands/test_ping.java#L12) ( *In our case it is the ping command* ).

### Contribution in adding new commands in promoted !! feel free to contribute.

## Authors

- [@tripsterxx](https://github.com/tripsterxx)

>##### Other important link's. -> [Discord server for help](https://discord.gg/mAdWzmBkGF)
