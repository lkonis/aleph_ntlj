package com.konis.aleph;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.swing.JButton;
import javax.swing.JFrame;


public class Wav_say {

    private File soundFile;
    private AudioInputStream audioStream;
    private AudioFormat audioFormat;
    private  SourceDataLine sourceLine;

    /**
     *
     * @param filename the name of the file that is going to be played
     *
     */
    public Wav_say(String filename){

        String strFilename = filename;

        try {
            soundFile = new File(strFilename);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        try {
            audioStream = AudioSystem.getAudioInputStream(soundFile);
        } catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }

        audioFormat = audioStream.getFormat();
//		System.out.println(audioFormat);

        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        try {
            sourceLine = (SourceDataLine) AudioSystem.getLine(info);
            sourceLine.open(audioFormat);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        sourceLine.start();

        Thread playThread =
                new Thread(new PlayThread());
        playThread.start();
    }

    /**
     *
     */
    class PlayThread extends Thread{

        //	class PlaySoundThread extends Thread{
        private final int BUFFER_SIZE = 16384;
        int nBytesRead = 0;
        byte abData[] = new byte[BUFFER_SIZE];
        public void run(){
            //		byte tempBuffer[] = new byte[10000];
            while (nBytesRead != -1) {
                try {
                    nBytesRead = audioStream.read(abData, 0, abData.length);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (nBytesRead >= 0) {
                    @SuppressWarnings("unused")
                    int nBytesWritten = sourceLine.write(abData, 0, nBytesRead);
                }
            }

            sourceLine.drain();
            sourceLine.close();
        }
    }

    public static void main(String[] arg){
        JFrame frame = new JFrame();
        frame.setSize(300, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JButton button = new JButton("Play");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String pathto ="/home/konisas/IdeaProjects/al/src/data/sounds/letters/kaf.wav";
                new Wav_say(pathto);
            }
        });
        frame.add(button);
        frame.setVisible(true);
    }
}