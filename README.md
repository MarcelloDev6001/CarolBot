<h1 align="center">Carol: a simple and funny Discord Bot</h1>

<p align="center">
  <img src="https://github.com/MarcelloDev6001/CarolBot/blob/main/avatar.png" 
       alt="logo"
       style="border-radius: 15px; border: 3px solid #ffffffff;"
       width="350" />
</p>

<h3 align="center">Carol is a simple discord bot with multi purpose (like administration, diversion, utility, etc...)</h3>

<p align="center">AAIO (Almost All In One)!</p>

<p align="center">Carol is a lot inspired on <a href="https://github.com/LorittaBot/Loritta" target="_blank">Loritta</a> and Carol extremely █████ she!</p>

<p align="center">Carol is currently in beta and it's subject to have a lot of changes on the future.</p>

<h1 align="center">Future of Carol</h1>

### Personnel plans for Carol:

- Create a Discord server. (just like "Apartamento da Loritta", the Discord server of Loritta)

- Create something special of Carol. (like a Minecraft server or a Youtube channel for it and etc...)

- Create a Website to entirely configure Carol for a specific discord server. (configure commands, roles, channels, etc...)

<h1 align="center">How to self-host Carol</h1>

### REQUIREMENTS:
- Java 24 or higher. (I honestly don't know if this bot works with older versions of Java)
- IntelliJ Idea. (You can use both Community version or Ultimate version)
- Pacience and Determination.

### NOTES:
- *`CarolSettings.java` (where is a lot of important configurations in Carol) is located at `src/main/java/com/marc/discordbot/carol/CarolSettings.java`*

### HOSTING:
- Firstly:
create an environment variable called `CAROL_DISCORD_TOKEN` and pass the value for this variable as your Discord Bot Token.

- Secondly:
open `carol-discord-bot/src/main/resources` and insert you Firebase key json file inside this folder with the name `firebase-key.json`. (you can download it quickly searching any tutorial on internet.)
  > **ALTERNATIVE:** Put you `firebase-key.json` in any folder and just change the `CarolSettings.FIREBASE_KEY_PATH` to the correct folder of the Firebase Key Json File.

  > **ALTERNATIVE2:** Leave `CarolSettings.FIREBASE_KEY_PATH` empty and then create an enviroment variable called `FIREBASE_KEY` and pass the value of this variable aas the content of your `firebase-key.json` file.
- Thirdly:
Configure essencial stuffs of the Discord bot on `CarolSettings.java`, like:

| VARIABLE                                         | What the variable do                                                                                       | Type       |
|--------------------------------------------------|-------------------------------------------------------------------------------------------------------------|------------------|
| **JDA_BUILDER_TYPE**                             | how the JDA will construct your bot`                                                                      | `byte (0, 1 or 2)`   |
| **MAX_CACHE_TIME_FOR_MESSAGE_BUTTONS**           | Change the max time a button will be clickable until appears the `MESSAGE_ON_BUTTON_AFTER_UNCACHE` message (in Seconds)  | `long`          |
| **DEFAULT_MESSAGE_ON_ADDED_TO_A_NEW_GUILD**      | What your bot will say when your bot is added on a new guild                                                 | `String`          |
| **DEFAULT_MESSAGE_ON_ADDED_TO_A_NEW_GUILD WITHOUT_OWNER_MENTION** | Same as above, but without owner mention                                                    | `String`          |
| **FIREBASE_KEY_PATH**                            | Where your `FirebaseKey.json` is located. (Leave it null if your content json is at an enviroment variable called FIREBASE_KEY) | `String` |
| **ONLINE_STATUS**                                | Status of your bot (Online, Do Not Disturb, Idle or Offline)                                                 | `net.dv8tion.jda.api.OnlineStatus`          |
| **ACTIVITY_TYPE**                                | Activity of your bot (Listening, Playing, Watching, etc)                                                 | `net.dv8tion.jda.api.entities.Activity.ActivityType` |
| **ACTIVITY_INFO**                                | What message will be displayed on your bot Activity                                                          | `String`          |
| **CHANGE_ACTIVITY_EVERY_DAY**                                | If your bot will change the activity every 12 am                                                 | `boolean`          |
| **MESSAGE_ON_BUTTON_AFTER_UNCACHE**              | What message will be displayed when a user click on a button that was uncached                               | `String`          |

- Fourth:
Open `carol-discord-bot` folder as a Project in IntelliJ, run `CarolLauncher.java` (specifically the function `main()`) and now have fun with your bot :D

<h1 align="center">Invite</h1>

Invite Carol to your discord server by clicking [Here](https://discord.com/oauth2/authorize?client_id=1214985204985241600&permissions=8&integration_type=0&scope=bot).
> Currently, Carol isn't a public bot because it's still in development.
> Hope you guys have pacience, because Carol probally will take a lot of time to be fully developed!
