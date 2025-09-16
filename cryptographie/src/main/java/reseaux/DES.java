package reseaux;

import java.util.Random;

public class DES {
    private static final int TAILLE_BLOC = 64;
    private static final int TAILLE_SOUS_BLOC = 32;
    private static final int NB_RONDE = 1;



    private int[] masterKey;
    int[][] tab_cles;

    public DES() {
        this.masterKey = genereMasterKey();
        this.tab_cles = new int[NB_RONDE][48];
    }

    public int[] genereMasterKey() {
        int[] key = new int[64];
        Random random = new Random();
        for (int i = 0; i < 64; i++) {
            key[i] = random.nextInt(2);
        }
        return key;
    }


    public int[] crypte(String message_clair) {
        int[] bits = stringToBits(message_clair);
        return bits;
    }

    public int[] stringToBits(String message) {
        int[] bits = new int[message.length() * 16];
        int index = 0;

        for (int c = 0; c < message.length(); c++) {
            char ch = message.charAt(c);
            for (int i = 15; i >= 0; i--) {
                bits[index++] = (ch >> i) & 1;
            }
        }
        return bits;
    }


    public String decrypte(int[] messageCode) {

        return bitsToString(messageCode);
    }

    public String bitsToString(int[] blocs) {
        String result = "";
        for (int i = 0; i + 7 < blocs.length; i += 8) {
            int value = 0;
            for (int j = 0; j < 8; j++) {
                int bit = blocs[i + j];
                value = value * 2 + bit;
            }
            result += (char) value;
        }

        return result;
    }

    public int[] genereCle(int n) {
        int[] c = new int[48];
        for (int i = 0; i < 48; i++) c[i] = 0;
        tab_cles[n] = c;
        return c;
    }

    public int[] permutation(int[] tab_permutation, int[] bloc) {
        int[] output = new int[tab_permutation.length];
        for (int i = 0; i < tab_permutation.length; i++) {
            output[i] = bloc[tab_permutation[i] - 1];
        }
        return output;
    }


}