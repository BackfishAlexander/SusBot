package CommandList;

import Utility.AudioPlayerSendHandler;
import Utility.Option;
import Utility.TrackScheduler;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import javafx.scene.media.Track;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;


import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Sus implements BlankCommand {

    String name = "sus";
    String description = "For when imposter acting sus";
    ArrayList<Option> parameters = new ArrayList<Option>();
    SlashCommandEvent e;
    AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
    static String[] sounds;


    public Sus() {
        File list = new File("Sounds");
        sounds = list.list();
    }

    public Sus(SlashCommandEvent e) {
        this.e = e;
        new Thread(this).start();
    }

    @Override
    public void execute(SlashCommandEvent e) {
        new Sus(e);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public ArrayList<Option> getParameters() {
        return this.parameters;
    }

    @Override
    public void run() {
        try {
            VoiceChannel vc = e.getMember().getVoiceState().getChannel();

            if (!Objects.nonNull(vc)) {
                e.reply("The imposter must have already vented..").queue();
                e.getHook().deleteOriginal().queueAfter(5, TimeUnit.SECONDS);
                return;
            }

            AudioManager audio = e.getGuild().getAudioManager();
            audio.openAudioConnection(vc);

            if (sounds.length == 0) {
                e.reply("Alex, your library fucked up").queue();
                e.getHook().deleteOriginal().queueAfter(5, TimeUnit.SECONDS);
                return;
            }

            int randomNum = (int) (Math.random() * sounds.length);

            AudioPlayer player = playerManager.createPlayer();
            AudioSourceManagers.registerLocalSource(playerManager);
            TrackScheduler ts = new TrackScheduler();
            ts.audio = audio;
            ts.e = e;
            player.addListener(new TrackScheduler());

            //AudioPlayerSendHandler handler = new AudioPlayerSendHandler(player);


            Path currentRelativePath = Paths.get("");
            String s = currentRelativePath.toAbsolutePath().toString();
            s = s + "/Sounds/";
            s = s + sounds[randomNum];

            playerManager.loadItem(s, new AudioLoadResultHandler() {
                @Override
                public void trackLoaded(AudioTrack track) {
                    audio.setSendingHandler(new AudioPlayerSendHandler(player));
                    audio.openAudioConnection(vc);

                    player.playTrack(track);
                }

                @Override
                public void playlistLoaded(AudioPlaylist playlist) {

                }

                @Override
                public void noMatches() {

                }

                @Override
                public void loadFailed(FriendlyException exception) {

                }
            });

            //audio.setSendingHandler(handler);

        }
        catch (Exception ex) {
            e.reply("The imposter must have already vented...");
            e.getHook().deleteOriginal().queueAfter(5, TimeUnit.SECONDS);
            return;
        }
        e.reply("There is a sussy imposter AMONG US").queue();



    }
}
