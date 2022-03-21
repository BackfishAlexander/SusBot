import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import java.io.File;
import java.util.Scanner;

public class SusBot {
    public static JDA jda;
    public static String prefix = "/";

    public static void main(String[] args) throws Exception {
        File tokenFile = new File("token.txt");
        Scanner tokenReader = new Scanner(tokenFile);
        String token = tokenReader.nextLine();
        tokenReader.close();
        jda = JDABuilder.createDefault(token).build();
        jda.addEventListener(new Commands());
    }
}
