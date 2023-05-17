# AbhiBot
A simple reminder bot

# PREREQUISITES 
Maven needs to be installed to build the jar file.
https://maven.apache.org/download.cgi

https://maven.apache.org/install.html

Java 18 or higher needs to be installed to run the jar file.

# How to get it up and running in the same directory or another directory
The bot does not have to be run in the same directory as the git repo. As long as the token.txt and guildid.txt files are in the same folder as .jar file it will work.

Make a file called "token.txt" with your discord bot token.

Make a file called "guildid.txt" with the server id of your server.

Build a jar file running "mvn install"

Copy "AbhiBot-1.0-SNAPSHOT-jar-with-dependencies.jar" to the directory and rename it to "AbhiBot.jar"

!!! Make sure the "token.txt" and "guildid.txt" files are in the same directory as AbhiBot.jar.

Run the jar file with "java -jar AbhiBot.jar"

# COMMANDS
!setprefix <single character> example: "!setprefix ;"
!toggletr on/off
!togglew on/off
!reminder 1d1h1m example: "!reminder 1d", "!reminder 1d2h3m". You don't have to use all of the time units, you can pick and choose.
