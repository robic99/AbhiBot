import classes.Token;
import commands.PrefixCommand;
import commands.ReminderCommand;
import db.ServerSQL;
import db.UserSQL;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;

public class AbhiBot {
    private Token token;
    private ServerSQL serverSQL;
    private UserSQL userSQL;


    public AbhiBot() throws LoginException {
        setupBot();
    }

    public static void main(String[] args) throws LoginException {
        AbhiBot bot = new AbhiBot();
    }

    public void setupBot() throws LoginException {
        serverSQL = new ServerSQL();
        userSQL = new UserSQL();
        token = new Token();
        JDA jda = JDABuilder.createDefault(token.getToken()).build();
        jda.addEventListener(new ReminderCommand());
        jda.addEventListener(new PrefixCommand());
    }
}
