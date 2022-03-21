import CommandList.*;
import Utility.Option;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Commands implements EventListener {

    public static ArrayList<BlankCommand> list = new ArrayList<BlankCommand>();

    public static boolean doResetCommands = false;

    public Commands() {
        //list.add(new Help());
        list.add(new Sus());

        if (!doResetCommands)
            return;
        System.out.println("Commands Added to list. Now adding to servers...");
        try {
            SusBot.jda.awaitReady();
            SusBot.jda.updateCommands().queue();
            CommandListUpdateAction Updater = SusBot.jda.updateCommands();
            for (BlankCommand cmd : list) {
                System.out.printf("Adding %s cmd.\n", cmd.getName());

                CommandData c = new CommandData(cmd.getName(), cmd.getDescription());
                for (Option o : cmd.getParameters()) {
                    c.addOption(o.getType(), o.getName(), o.getDescription(), o.getRequired());
                }
                Updater = Updater.addCommands(c);
            }
            Updater.queue();
        } catch (Exception e) {
            System.out.println("Something horrible happened");

            e.printStackTrace();
        }
    }

    @Override
    public void onEvent(@NotNull GenericEvent event) {
        if (event instanceof SlashCommandEvent) {
            SlashCommandEvent CmdEvent = (SlashCommandEvent) event;
            String command = CmdEvent.getName();
            MessageChannel channel = CmdEvent.getChannel();
            System.out.println(String.format("[%s] executed [%s] in [%s]",CmdEvent.getMember().getNickname(),command, channel.getName()));

            for (BlankCommand cmd : list) {
                if (command.toLowerCase().equals(cmd.getName())) {
                    cmd.execute(CmdEvent);
                }
            }
        }
    }



}
