package com.konis.aleph;

import java.awt.Color;
import java.util.Random;

import javax.swing.JLabel;
import javax.swing.border.LineBorder;

import com.konis.aleph.hebLetter.Vowel;

@SuppressWarnings("serial")
public class LetterBox extends JLabel {
    public boolean isOccupied = false;
    public boolean isVowelOccupied = false;
    public boolean isOccupiedCorrectly = false;
    public String whoOccupiesMe = "";
    public String letter;
    public Vowel vowel;
    Color[] colors = { Color.magenta, Color.cyan, Color.yellow, Color.orange,
            Color.DARK_GRAY, Color.pink };

    public LetterBox(String text, int x, int y, int boxSize, String inLetter,
                     Vowel inVowel) {
        this(text, x, y, boxSize, inLetter);
        vowel = inVowel;
    }

    public LetterBox(String text, int x, int y, int boxSize, String inLetter) {
        super(text);
        setBounds(x, y, boxSize, boxSize);
        Random rnd = new Random();
        int rndColor = rnd.nextInt(colors.length);
        setBackground(colors[rndColor]);
        setBorder(new LineBorder(Color.red));
        setName(text);
        letter = inLetter;
    }

}
