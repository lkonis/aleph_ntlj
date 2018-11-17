package com.konis.aleph;
import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

// To play sound using Clip, the process need to be alive.
// Hence, we use a Swing application.
public class SoundClip  {
    static Clip clip;
    // Constructor
    @SuppressWarnings("unused")
    public SoundClip(String clipWavFile) {

        try {
            // Open an audio input stream.
            URL url = this.getClass().getClassLoader().getResource(clipWavFile);
            url  = new URL(clipWavFile);
            if (url == null)
            {
                System.err.println("Couldn't find file: " + clipWavFile);
                //             return null;
            }
            else
            {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
                // Get a sound clip resource.
                clip = AudioSystem.getClip();
                // Open audio clip and load samples from the audio input stream.
                clip.open(audioIn);
                clip.start();
            }
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }

    }


    public static void main(String[] args) {
        String pathto = "/home/konisas/IdeaProjects/al/src/data/wav_files/";
        //pathto ="/home/ubtosh/Downloads/boo.wav";
        new SoundClip(pathto);//"applause.wav");
    }
}
