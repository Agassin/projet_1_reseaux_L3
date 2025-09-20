package reseaux;

import java.util.Random;

public class DES {
    private static final int TAILLE_BLOC = 64;
    private static final int TAILLE_SOUS_BLOC = 32;
    private static final int NB_RONDE = 1;

    // Tables de permutation (valeurs d'indice commençant à 1)
    private static final int[] PERM_INITIALE = {
            58, 50, 42, 34, 26, 18, 10, 2,
            60, 52, 44, 36, 28, 20, 12, 4,
            62, 54, 46, 38, 30, 22, 14, 6,
            64, 56, 48, 40, 32, 24, 16, 8,
            57, 49, 41, 33, 25, 17, 9, 1,
            59, 51, 43, 35, 27, 19, 11, 3,
            61, 53, 45, 37, 29, 21, 13, 5,
            63, 55, 47, 39, 31, 23, 15, 7
    };

    private static final int[] PERM_INVERSE = {
            40, 8, 48, 16, 56, 24, 64, 32,
            39, 7, 47, 15, 55, 23, 63, 31,
            38, 6, 46, 14, 54, 22, 62, 30,
            37, 5, 45, 13, 53, 21, 61, 29,
            36, 4, 44, 12, 52, 20, 60, 28,
            35, 3, 43, 11, 51, 19, 59, 27,
            34, 2, 42, 10, 50, 18, 58, 26,
            33, 1, 41, 9, 49, 17, 57, 25
    };

    private static final int[] PC1 = {
            57, 49, 41, 33, 25, 17, 9,
            1, 58, 50, 42, 34, 26, 18,
            10, 2, 59, 51, 43, 35, 27,
            19, 11, 3, 60, 52, 44, 36,
            63, 55, 47, 39, 31, 23, 15,
            7, 62, 54, 46, 38, 30, 22,
            14, 6, 61, 53, 45, 37, 29,
            21, 13, 5, 28, 20, 12, 4
    };

    private static final int[] PC2 = {
            14, 17, 11, 24, 1, 5,
            3, 28, 15, 6, 21, 10,
            23, 19, 12, 4, 26, 8,
            16, 7, 27, 20, 13, 2,
            41, 52, 31, 37, 47, 55,
            30, 40, 51, 45, 33, 48,
            44, 49, 39, 56, 34, 53,
            46, 42, 50, 36, 29, 32
    };

    private static final int[] E = {
            32, 1, 2, 3, 4, 5,
            4, 5, 6, 7, 8, 9,
            8, 9, 10, 11, 12, 13,
            12, 13, 14, 15, 16, 17,
            16, 17, 18, 19, 20, 21,
            20, 21, 22, 23, 24, 25,
            24, 25, 26, 27, 28, 29,
            28, 29, 30, 31, 32, 1
    };

    private static final int[] P = {
            16, 7, 20, 21,
            29, 12, 28, 17,
            1, 15, 23, 26,
            5, 18, 31, 10,
            2, 8, 24, 14,
            32, 3, 27, 9,
            19, 13, 30, 6,
            22, 11, 4, 25
    };

    // Table S simplifiée (utilisée pour toutes les boîtes S)
    private static final int[][] S = {
            {14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7},
            {0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8},
            {4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0},
            {15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13}
    };

    private static final int[] TAB_DECALAGE = {1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1};



    private int[] masterKey;
    int[][] tab_cles;

    public DES() {
        this.masterKey = genereMasterKey();
        this.tab_cles = new int[NB_RONDE][48];
        for (int i = 0; i < NB_RONDE; i++) {
            genereCle(i);
        }
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
        if (message == null) return new int[0];
        int[] bits = new int[message.length() * 16];
        int index = 0;
        for (char ch : message.toCharArray()) {
            int charValue = (int) ch;
            for (int i = 15; i >= 0; i--) {
                bits[index++] = (charValue >>> i) & 1;
            }
        }
        return bits;
    }


    public String decrypte(int[] messageCode) {

        return bitsToString(messageCode);
    }

    public String bitsToString(int[] blocs) {
        if (blocs == null || blocs.length == 0) return "";
        if (blocs.length % 16 != 0) {
            throw new IllegalArgumentException("La longueur du tableau doit être un multiple de 16");
        }
        StringBuilder result = new StringBuilder();
        int nbCaracteres = blocs.length / 16;
        for (int i = 0; i < nbCaracteres; i++) {
            int charValue = 0;
            int startIndex = i * 16;
            for (int j = 0; j < 16; j++) {
                charValue = (charValue << 1) | blocs[startIndex + j];
            }
            result.append((char) charValue);
        }
        return result.toString();
    }

    public int[] genereCle(int n) {
        int[] pc1Key = permutation(PC1, masterKey);
        int[][] cd = decoupage(pc1Key, 28);
        int[] C = cd[0];
        int[] D = cd[1];
        for (int i = 0; i <= n; i++) {
            C = decalle_gauche(C, TAB_DECALAGE[i]);
            D = decalle_gauche(D, TAB_DECALAGE[i]);
        }
        int[] CD = recollage_bloc(new int[][]{C, D});
        int[] cle = permutation(PC2, CD);
        tab_cles[n] = cle;
        return cle;
    }

    public int[] permutation(int[] tab_permutation, int[] bloc) {
        int[] tab_rep = new int[tab_permutation.length];
        for (int i = 0; i < tab_permutation.length; i++) {
            tab_rep[i] = bloc[tab_permutation[i] - 1];
        }
        return tab_rep;
    }

    public int[] invPermutation(int[] tab_permutation, int[] bloc) {
        int[] tab_rep = new int[tab_permutation.length];
        for (int i = 0; i < tab_permutation.length; i++) {
            tab_rep[tab_permutation[i] - 1] = bloc[i];
        }
        return tab_rep;
    }

    public int[][] decoupage(int[] bloc, int tailleBlocs) {
        if (bloc.length % tailleBlocs != 0) {
            throw new IllegalArgumentException("La taille du bloc doit être un multiple de " + tailleBlocs);
        }
        int nbBlocs = bloc.length / tailleBlocs;
        int[][] blocs = new int[nbBlocs][tailleBlocs];
        for (int i = 0; i < nbBlocs; i++) {
            System.arraycopy(bloc, i * tailleBlocs, blocs[i], 0, tailleBlocs);
        }
        return blocs;
    }

    public int[] recollage_bloc(int[][] blocs) {
        int totalLength = 0;
        for (int[] bloc : blocs) {
            totalLength += bloc.length;
        }
        int[] result = new int[totalLength];
        int index = 0;
        for (int[] bloc : blocs) {
            System.arraycopy(bloc, 0, result, index, bloc.length);
            index += bloc.length;
        }
        return result;
    }

    public int[] decalle_gauche(int[] bloc, int nbCran) {
        int[] result = new int[bloc.length];
        System.arraycopy(bloc, nbCran, result, 0, bloc.length - nbCran);
        System.arraycopy(bloc, 0, result, bloc.length - nbCran, nbCran);
        return result;
    }

    public int[] xor(int[] tab1, int[] tab2) {
        if (tab1.length != tab2.length) {
            throw new IllegalArgumentException("Les tableaux doivent avoir la même longueur");
        }
        int[] result = new int[tab1.length];
        for (int i = 0; i < tab1.length; i++) {
            result[i] = tab1[i] ^ tab2[i];
        }
        return result;
    }

    public int[] fonction_S(int[] tab) {
        if (tab.length != 48) {
            throw new IllegalArgumentException("L'entrée de la fonction S doit être de 48 bits");
        }
        int[] result = new int[32];
        int resultIndex = 0;
        for (int i = 0; i < 8; i++) {
            int start = i * 6;
            int row = (tab[start] << 1) | tab[start + 5];
            int col = (tab[start + 1] << 3) | (tab[start + 2] << 2) | (tab[start + 3] << 1) | tab[start + 4];
            int sValue = S[row][col];
            for (int j = 3; j >= 0; j--) {
                result[resultIndex + j] = sValue & 1;
                sValue >>= 1;
            }
            resultIndex += 4;
        }
        return result;
    }

    public int[] fonction_F(int[] uneCle, int[] unD) {
        int[] expanded = permutation(E, unD);
        int[] xorResult = xor(expanded, uneCle);
        int[] sResult = fonction_S(xorResult);
        return permutation(P, sResult);
    }
}
