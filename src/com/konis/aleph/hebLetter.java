package com.konis.aleph;

public class hebLetter {
    public boolean isDagesh() {
        return dagesh;
    }

    private char heb_letter;
    public Vowel vowel;
    boolean dagesh=false;
    public boolean isVoul;
    public String letter;
    public int level;
    public hebLetter(char c, Vowel v){
        setHeb_letter(c);
        letter = getLetterFromHeb(c);
        if (dagesh)
            System.out.println("O O");

        level = getLevelFromHeb(c);
        vowel = v;
        isVoul=false;
    }
    public Vowel getVowel() {
        return vowel;
    }

    public void setVowel(Vowel v) {
        this.vowel = v;
        this.isVoul=false; // FIXME: 11/14/18 why false? 
    }

    public char getHeb_letter() {
        return heb_letter;
    }
    public void setHeb_letter(char c) {
        this.heb_letter = c;
    }
    public String toString(){
        return Character.toString(heb_letter);
    }

    public enum Vowel {
        PATAH, TZERE, HIRIK, HOLAM, KUBUTZ, SHVA
    }

    public static String getLetterFromHeb(char c){

        switch (c) {
            case '0':
                return "vowel";
            case 'א':
                return "alef";
            case 'ב':
                return "bet";
            case 'ג':
                return "gimel";
            case 'ד':
                return "dalet";
            case 'ה':
                return "he";
            case 'ו':
                return "vav";
            case 'ז':
                return "zain";
            case 'ח':
                return "het";
            case 'ט':
                return "tet";
            case 'י':
                return "yod";
            case 'כ':
                return "kaf";
            case 'ל':
                return "lamed";
            case 'מ':
                return "mem";
            case 'נ':
                return "nun";
            case 'ס':
                return "sameh";
            case 'ע':
                return "ain";
            case 'פ':
                return "pe";
            case 'צ':
                return "tzadi";
            case 'ק':
                return "kof";
            case 'ר':
                return "resh";
            case 'ש':
                return "shin";
            case 'ת':
                return "tav";
            case 'ף':
                return "peE";
            case 'ך':
                return "kafE";
            case 'ם':
                return "memE";
            case 'ן':
                return "nunE";
            case 'ץ':
                return "tzadiE";
            default:
                break;
        }
        return "";

    }

    public static int getLevelFromHeb(char c){

        switch (c) {
            case '0':
                return -1;
            case 'א':
                return 0;
            case 'ב':
                return 0;
            case 'ג':
                return 0;
            case 'ד':
                return 0;
            case 'ה':
                return 1;
            case 'ו':
                return 2;
            case 'ז':
                return 1;
            case 'ח':
                return 2;
            case 'ט':
                return 2;
            case 'י':
                return 2;
            case 'כ':
                return 0;
            case 'ל':
                return 0;
            case 'מ':
                return 0;
            case 'נ':
                return 0;
            case 'ס':
                return 0;
            case 'ע':
                return 2;
            case 'פ':
                return 1;
            case 'צ':
                return 2;
            case 'ק':
                return 1;
            case 'ר':
                return 0;
            case 'ש':
                return 2;
            case 'ת':
                return 1;
            case 'ף':
                return 6;
            case 'ך':
                return 6;
            case 'ם':
                return 3;
            case 'ן':
                return 3;
            case 'ץ':
                return 6;
            default:
                break;
        }
        return -1;

    }
    public static Vowel getVowelfromIndex(int i) throws Exception
    {
        Vowel v = null;
        switch (i){
            case 0:
                v=Vowel.PATAH;
                break;
            case 1:
                v=Vowel.TZERE;
                break;
            case 2:
                v=Vowel.HIRIK;
                break;
            case 3:
                v=Vowel.HOLAM;
                break;
            case 4:
                v=Vowel.KUBUTZ;
                break;
            default:
                throw new Exception("Something strange happend with vowel indexing");
        }
        return v;
    }

    public void setDagesh() {
        // TODO Auto-generated method stub
        dagesh=true;
    }
    public static void main(String[] args) {
        Vowel v = Vowel.TZERE;
        char c = 'ש';
        hebLetter hl = new hebLetter(c,v);
        System.out.println("heb letter is: " + hl.toString());

    }
}
