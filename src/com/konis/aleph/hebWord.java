package com.konis.aleph;

import com.konis.aleph.hebLetter.Vowel;


public class hebWord {
    private java.util.List<hebLetter> word = new java.util.ArrayList<>();
    public int length=0;
    public String iconPath;
    public String sayPath;
    public int level=0;

    public String getSayPath() {
        return sayPath;
    }

    public void setSayPath(String sayPath) {
        this.sayPath = sayPath;
    }
    public int vowelCount=0;

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public hebLetter getChar (int i){
        return word.get(i);
    }
    public void remLetter(int index){
        word.remove(index);
        length--;
    }

    /**
     * constructor that interpret special characters and knows how to set accent and vowels for each letter
     * @param w
     */
    public hebWord(String w){
        int N = w.length();
        for (int i=0; i<N; i++)
        {
            // first - without vowel or accent
            hebLetter l = new hebLetter(w.charAt(i), null);
            {
                word.add(l);
                length++;
            }
        }
    }

    public hebWord(String w, String icon){
        this(w);
        iconPath=icon;
    }

    // getter
    public java.util.List<hebLetter> getWord() {
        return word;
    }
    // setter
    public void setWord(java.util.List<hebLetter> word) {
        this.word = word;
    }
    // display
    public String toString(){
        char[] chars =new char[length];
        for (int i=0; i<length; i++){
            hebLetter c=getChar(i);
            chars[i]= c.getHeb_letter();
        }

        return new String(chars);

    }
    // add vowel to existing word
    public void addVowels(String line, boolean useFalseVowel) {
        // also identify letters to remove
        // these can be any of yod, vav that use as vowels
        int charCnt=0;

        // now add vowels
        hebLetter l = null;
        for (int i=0; i<line.length(); i++)
        {
            char c = line.charAt(i) ; // get special sign
            if (c=='p')
            {
                l.setDagesh();// TODO: fix this
                continue;
            }
            l = this.getChar(charCnt);// hw.gline.charAt(charCnt);	// get hebrew character

            if (c!='x')	// is this a vowel?
            {
                switch (c)
                {
                    case 'a':
                        l.setVowel(Vowel.PATAH);
                        break;
                    case 'e':
                        l.setVowel(Vowel.TZERE);
                        break;
                    case 'i':
                        l.setVowel(Vowel.HIRIK);
                        break;
                    case 'o':
                        l.setVowel(Vowel.HOLAM);
                        break;
                    case 'u':
                        l.setVowel(Vowel.KUBUTZ);
                        break;
                    case 'j':
                        l.setVowel(Vowel.SHVA);
                }
                vowelCount++;
            }
            // remove long vowels, if desired TODO: add enable option
            else if ((l.letter=="vav" || l.letter=="yod" ) && c=='x')
            {
                if (useFalseVowel){
                    this.remLetter(charCnt);
                    continue;
                }
            }
            else if (l.letter=="he" && charCnt==(this.length-1)) // last letter that is he
            {
                if (useFalseVowel){
                    this.remLetter(charCnt);
                    continue;
                }
            }
            else	// hopefuly this is always shva
                if (useFalseVowel)
                    l.setVowel(Vowel.SHVA);

            charCnt++;
        }
    }

    public static void main(String[] args) {
        String in_word="אופנים";
        String in_vowel="oxaaix";
        hebWord hw = new hebWord(in_word);
        hw.addVowels(in_vowel,false);
        System.out.printf(hw.toString());
    }
}
