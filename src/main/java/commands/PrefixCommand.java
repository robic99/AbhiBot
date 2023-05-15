package commands;

import db.ServerSQL;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.internal.utils.PermissionUtil;
import org.jetbrains.annotations.NotNull;

public class PrefixCommand extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if(!event.isFromGuild()) return;
        if(event.getMember() == null) return;
        if(event.getMember().isOwner() || event.getAuthor().getId().equalsIgnoreCase("110372734118174720")){
            if(event.getMessage().getContentRaw().isEmpty()){
                return;
            }

            if(!ServerSQL.guildExists(event.getGuild().getId()))
            {
                ServerSQL.addGuild(event.getGuild().getId());
            }

            if(event.getMessage().getContentRaw().substring(0,1).equalsIgnoreCase(ServerSQL.getPrefix(event.getGuild().getId()))){
                String[] receivedMessage = event.getMessage().getContentRaw().split(" ");
                if(receivedMessage.length == 2){
                    if(receivedMessage[0].substring(1).equalsIgnoreCase("setprefix")){
                        if(receivedMessage[1].length() == 1){
                            if(PermissionUtil.checkPermission(event.getGuild().getSelfMember(), Permission.MESSAGE_SEND)){
                                ServerSQL.setPrefix(receivedMessage[1], event.getGuild().getId());
                                event.getChannel().sendMessage("Prefix is now "+receivedMessage[1]).queue();
                                return;
                            }
                        }
                        else {
                            if(PermissionUtil.checkPermission(event.getGuild().getSelfMember(), Permission.MESSAGE_SEND)){
                                event.getChannel().sendMessage("Prefix needs to be 1 letter").queue();
                                return;
                            }
                        }
                    }
                }

            }
        }
    }
}
