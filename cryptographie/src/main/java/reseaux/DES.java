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
        byte[] bytes = message.getBytes();
        int[] bits = new int[bytes.length * 8];
        int index = 0;
        for (byte b : bytes) {
            for (int i = 7; i >= 0; i--) {
                bits[index++] = (b >> i) & 1;
            }
        }
        return bits;
    }


    public String decrypte(int[] messageCode) {
        return bitsToString(messageCode);
    }

    public String bitsToString(int[] blocs) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < blocs.length; i += 8) {
            int value = 0;
            for (int j = 0; j < 8; j++) {
                value = (value << 1) + blocs[i + j];
            }
            sb.append((char) value);
        }
        return sb.toString();
    }

    public int[] genereCle(int n) {
        int[] dummy = new int[48];
        for (int i = 0; i < 48; i++) dummy[i] = 0;
        tab_cles[n] = dummy;
        return dummy;
    }

    public int[] permutation(int[] tab_permutation, int[] bloc) {
        int[] output = new int[tab_permutation.length];
        for (int i = 0; i < tab_permutation.length; i++) {
            output[i] = bloc[tab_permutation[i] - 1];
        }
        return output;
    }

    public int[] invPermutation(int[] tab_permutation, int[] bloc) {
        int[] output = new int[tab_permutation.length];
        for (int i = 0; i < tab_permutation.length; i++) {
            output[tab_permutation[i] - 1] = bloc[i];
        }
        return output;
    }

    public int[][] decoupage(int[] bloc, int tailleBlocs) {
        int nbBlocs = bloc.length / tailleBlocs;
        int[][] blocs = new int[nbBlocs][tailleBlocs];
        for (int i = 0; i < nbBlocs; i++) {
            System.arraycopy(bloc, i * tailleBlocs, blocs[i], 0, tailleBlocs);
        }
        return blocs;
    }

    public int[] recollage_bloc(int[][] blocs) {
        int taille = blocs.length * blocs[0].length;
        int[] resultat = new int[taille];
        int index = 0;
        for (int[] bloc : blocs) {
            for (int bit : bloc) {
                resultat[index++] = bit;
            }
        }
        return resultat;
    }

    public int[] decalle_gauche(int[] bloc, int nbCran) {
        int[] output = new int[bloc.length];
        for (int i = 0; i < bloc.length; i++) {
            output[i] = bloc[(i + nbCran) % bloc.length];
        }
        return output;
    }

    public int[] xor(int[] tab1, int[] tab2) {
        int[] resultat = new int[tab1.length];
        for (int i = 0; i < tab1.length; i++) {
            resultat[i] = tab1[i] ^ tab2[i];
        }
        return resultat;
    }

    public int[] fonction_S(int[] tab) {
        int[] sortie = new int[32];
        int indexSortie = 0;
        for (int i = 0; i < 8; i++) {
            int[] bloc6 = new int[6];
            System.arraycopy(tab, i * 6, bloc6, 0, 6);
            int ligne = (bloc6[0] << 1) | bloc6[5];
            int colonne = (bloc6[1] << 3) | (bloc6[2] << 2) | (bloc6[3] << 1) | bloc6[4];
            int val = Constantes.S_BOXES[i][ligne][colonne]; // 0..15
            for (int j = 3; j >= 0; j--) {
                sortie[indexSortie + j] = val & 1;
                val >>= 1;
            }
            indexSortie += 4;
        }
        return sortie;
    }

    public int[] fonction_F(int[] uneCle, int[] unD) {
        int[] Dexp = permutation(Constantes.E, unD);
        int[] xored = xor(Dexp, uneCle);
        int[] Sres = fonction_S(xored);
        return Sres;
    }


}