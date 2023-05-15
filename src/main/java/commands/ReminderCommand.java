package commands;

import classes.Reminder;
import classes.Task;
import db.ServerSQL;
import db.UserSQL;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import net.dv8tion.jda.internal.utils.PermissionUtil;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReminderCommand extends ListenerAdapter {


    private List<String> getAliases() {
        return List.of("remind", "reminder", "tr", "w");
    }

    private List<String> getAliasesView() {
        return List.of("reminders", "rl", "rs");
    }

    private List<String> getAliasesToggle() {
        return List.of("toggletr", "togglew");
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        if (!e.isFromGuild()) return;
        if (!PermissionUtil.checkPermission(e.getGuild().getSelfMember(), Permission.MESSAGE_SEND))
            return;
        if (e.isFromThread()) {
            if (!PermissionUtil.checkPermission(e.getGuild().getSelfMember(), Permission.MESSAGE_SEND_IN_THREADS))
                return;
        }
        if (e.getMessage().getContentRaw().isEmpty()) return;

        String guildPrefix = ServerSQL.getPrefix(e.getGuild().getId());
        if (!e.getMessage().getContentRaw().split(" ")[0].substring(0, 1).equalsIgnoreCase(guildPrefix)) return;
        String[] message = e.getMessage().getContentRaw().split(" ");
        String firstMessage = message[0].substring(1);
        for (int i = 0; i < getAliases().size(); i++) {
            if (firstMessage.equalsIgnoreCase(getAliases().get(i))) {
                executeCommand(e);
                break;
            }
        }
        for (int i = 0; i < getAliasesToggle().size(); i++) {
            if (firstMessage.equalsIgnoreCase(getAliasesToggle().get(i))) {
                executeToggle(e);
                break;
            }
        }

    }

    public void executeCommand(MessageReceivedEvent e) {
        if (!UserSQL.isUserAdded(e.getAuthor().getId())) {
            UserSQL.addUser(e.getAuthor().getId());
        }

        int defaultToggle = UserSQL.getDefaultToggle(e.getAuthor().getId());
        int workToggle = UserSQL.getDefaultWorkToggle(e.getAuthor().getId());

        EmbedBuilder builder = new EmbedBuilder();
        Random obj = new Random();
        int rand = obj.nextInt(0xffffff + 1);
        String code = String.format("#%06x", rand);
        builder.setColor(Color.decode(code));
        String name = e.getAuthor().getName();
        builder.setAuthor("Reminder from " + name, e.getAuthor().getEffectiveAvatarUrl(), e.getAuthor().getEffectiveAvatarUrl());
        StringBuilder sb = new StringBuilder();

        builder.setDescription("Default 10 minute reminder.");

        int days = 0;
        int hours = 0;
        int minutes = 0;
        String[] message = e.getMessage().getContentRaw().split(" ");
        String messageToBeSent = "";
        if (message.length == 1) {
            if(message[0].contains("w")){
                if(workToggle > 0) {
                    hours = 4;
                    messageToBeSent = "Your w is off cooldown.";
                }
                else return;
            }
            else {
                if (defaultToggle > 0) {
                    minutes = defaultToggle;
                    messageToBeSent = "Your tr is off cooldown.";
                } else return;
            }
        } else if (message.length >= 2) {
            String timeString = message[1].toLowerCase();

            if (timeString.contains("d")) {
                Pattern p = Pattern.compile("\\d+(?=d)");
                Matcher m = p.matcher(timeString);
                if (m.find()) {
                    try {
                        days = Integer.parseInt(m.group(0));
                    } catch (NumberFormatException ex) {
                        e.getChannel().sendMessage("Use the correct format eg. 1d2h3m").queue();
                        return;
                    }
                }
            }
            if (timeString.contains("h")) {
                Pattern p = Pattern.compile("\\d+(?=h)");
                Matcher m = p.matcher(timeString);
                if (m.find()) {
                    try {
                        hours = Integer.parseInt(m.group(0));
                    } catch (NumberFormatException ex) {
                        e.getChannel().sendMessage("Use the correct format eg. 1d2h3m").queue();
                        return;
                    }
                }
            }
            if (timeString.contains("m")) {
                Pattern p = Pattern.compile("\\d+(?=m)");
                Matcher m = p.matcher(timeString);
                if (m.find()) {
                    try {
                        minutes = Integer.parseInt(m.group(0));
                    } catch (NumberFormatException ex) {
                        e.getChannel().sendMessage("Use the correct format eg. 1d2h3m").queue();
                        return;
                    }
                }
            }
        }


        if (message.length == 2) {
            messageToBeSent = "You asked to be reminded at this time.";
        } else if (message.length > 2) {
            StringBuilder sbContent = new StringBuilder();
            for (int i = 2; i < message.length; i++) {
                if (sbContent.length() > 1000) break;
                sbContent.append(message[i]).append(" ");
            }
            messageToBeSent = sbContent.toString();
        }
        builder.setDescription(messageToBeSent);
        Reminder r = new Reminder(new Task() {
            @Override
            public void execute() {
                Random r = new Random();
                int x = r.nextInt(10) + 1;
                String silly = "";
                if (x > 9) silly = "the silly goose ";
                e.getChannel().sendMessage("**Reminder** for " + silly + e.getAuthor().getAsMention()).setEmbeds(builder.build()).queue();
            }
        });
        if (days > 80000) days = 80000;
        if (hours > 80000) hours = 80000;
        if (minutes > 80000) minutes = 80000;
        hours += (days * 24);

        r.startWithDelay(hours, minutes);

        StringBuilder sbM = new StringBuilder();

        if (days > 0) {
            sbM.append(" ").append(days);
            if (days == 1) sbM.append(" day");
            else sbM.append(" days");
        }
        if (hours > 0) {
            if (days > 0) sbM.append(" and");
            sbM.append(" ").append(hours);
            if (hours == 1) sbM.append(" hour");
            else sbM.append(" hours");
        }
        if (minutes > 0) {
            if (hours > 0 || days > 0) sbM.append(" and");

            sbM.append(" ").append(minutes);
            if (minutes == 1) sbM.append(" minute");
            else sbM.append(" minutes");
        }
        long currentTime = System.currentTimeMillis();
        long addedTime = ((hours * 60L) + minutes) * 60000;
        int time = (int) ((currentTime + addedTime) / 1000);
        e.getChannel().sendMessage("Set a reminder in" + sbM.toString() + " from now. (<t:" + time + ":f>)").queue();


    }

    public void executeToggle(MessageReceivedEvent e) {

        String[] message = e.getMessage().getContentRaw().split(" ");
        if (message.length < 2)
        {
            e.getChannel().sendMessage(e.getAuthor().getAsMention() + " only on and off options available.").queue();
            return;
        }

        String type = message[0];
        String decision = message[1];
        String typeMessage = "";
        if (decision.equalsIgnoreCase("on")) {
            typeMessage = enableGeneralToggle(e.getAuthor().getId(), type);
            e.getChannel().sendMessage(e.getAuthor().getAsMention() + " " + typeMessage +" command now enabled.").queue();
        } else if (decision.equalsIgnoreCase("off")) {
            typeMessage = disableGeneralToggle(e.getAuthor().getId(), type);
            e.getChannel().sendMessage(e.getAuthor().getAsMention() + " " + typeMessage +" command now disabled.").queue();
        } else {
            e.getChannel().sendMessage(e.getAuthor().getAsMention() + " only on and off options available.").queue();
        }


    }


    public String enableGeneralToggle(String authorId, String type)
    {
        String typeMessage = "";
        if(type.contains("togglew"))
        {
            UserSQL.setDefaultWorkToggle(authorId, 240);
            typeMessage = "w";
        }
        else {
            typeMessage = "tr";
            UserSQL.setDefaultToggle(authorId, 10);
        }
        return typeMessage;
    }

    public String disableGeneralToggle(String authorId, String type)
    {
        String typeMessage = "";
        if(type.contains("togglew"))
        {
            UserSQL.setDefaultWorkToggle(authorId, 0);
            typeMessage = "w";
        }
        else {
            typeMessage = "tr";
            UserSQL.setDefaultToggle(authorId, 0);
        }
        return typeMessage;
    }
}
