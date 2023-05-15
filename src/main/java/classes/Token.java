package classes;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class Token {

    private String token;

    public Token() {
        try {
            token = Files.readString(Path.of("token.txt"), StandardCharsets.UTF_8);
        } catch (IOException e){
            e.printStackTrace();
        }

    }

    public String getToken(){
        return token;
    }
}
