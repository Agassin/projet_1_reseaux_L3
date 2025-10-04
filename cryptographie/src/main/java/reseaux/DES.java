package reseaux;

import java.util.Random;

public class DES {
    private static final int TAILLE_BLOC = 64;
    private static final int TAILLE_SOUS_BLOC = 32;
    private static final int NB_RONDE = 16;

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

    public int[] stringToBits(String message) {
        if (message == null) {
            return new int[0];
        }
        byte[] bytes = message.getBytes();
        int[] bits = new int[bytes.length * 8];
        int index = 0;
        for (int i = 0; i < bytes.length; i++) {
            byte b = bytes[i];
            for (int bitPosition = 7; bitPosition >= 0; bitPosition--) {
                int bitVal;
                int temp = b >> bitPosition;
                bitVal = temp & 1;
                bits[index] = bitVal;
                index++;
            }
        }

        return bits;
    }

    public String bitsToString(int[] blocs) {
        if (blocs == null || blocs.length == 0) {
            return "";
        }
        if (blocs.length % 8 != 0) {
            throw new IllegalArgumentException("La longueur du tableau doit être un multiple de 8");
        }
        int nCara = blocs.length / 8;
        byte[] bytes = new byte[nCara];
        for (int i = 0; i < nCara; i++) {
            int byteVal = 0;
            for (int j = 0; j < 8; j++) {
                int indice = i * 8 + j;
                int bit = blocs[indice];
                byteVal = byteVal * 2;
                if (bit == 1) {
                    byteVal = byteVal + 1;
                }
            }
            bytes[i] = (byte) byteVal;
        }
        return new String(bytes);
    }

    public int[] genereMasterKey() {
        int[] cle = new int[64];
        Random random = new Random();
        for (int i = 0; i < 64; i++) {
            cle[i] = random.nextInt(2);
        }
        return cle;
    }

    public int[] permutation(int[] tab_permutation, int[] bloc) {
        int[] resultat = new int[tab_permutation.length];
        for (int i = 0; i < tab_permutation.length; i++) {
            resultat[i] = bloc[tab_permutation[i] - 1];
        }
        return resultat;
    }

    public int[] invPermutation(int[] tab_permutation, int[] bloc) {
        int[] resultat = new int[tab_permutation.length];
        for (int i = 0; i < tab_permutation.length; i++) {
            resultat[tab_permutation[i] - 1] = bloc[i];
        }
        return resultat;
    }

    public int[][] decoupage(int[] bloc, int tailleBlocs) {
        /*if (bloc.length % tailleBlocs != 0) {
            throw new IllegalArgumentException("La taille du bloc doit être un multiple de " + tailleBlocs);
        }*/
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
        int[] res = new int[bloc.length];
        System.arraycopy(bloc, nbCran, res, 0, bloc.length - nbCran);
        System.arraycopy(bloc, 0, res, bloc.length - nbCran, nbCran);
        return res;
    }

    public int[] xor(int[] tab1, int[] tab2) {
        if (tab1.length != tab2.length) {
            throw new IllegalArgumentException("Les tableaux doivent avoir la même longueur");
        }
        int[] res = new int[tab1.length];
        for (int i = 0; i < tab1.length; i++) {
            int bit1 = tab1[i];
            int bit2 = tab2[i];
            if (bit1 == bit2) {
                res[i] = 0;
            } else {
                res[i] = 1;
            }
        }
        return res;
    }

    public void genereCle(int n) {
        int[] pc1Key = permutation(PC1, masterKey);
        int[][] cd = decoupage(pc1Key, 28);
        int[] C = cd[0];
        int[] D = cd[1];
        for (int i = 0; i <= n; i++) {
            C = decalle_gauche(C, TAB_DECALAGE[i]);
            D = decalle_gauche(D, TAB_DECALAGE[i]);
        }
        int[] CD = recollage_bloc(new int[][]{C, D});
        tab_cles[n] = permutation(PC2, CD);
    }

    public int[] fonction_S(int[] tab) {
        if (tab.length != 48) {
            throw new IllegalArgumentException("Il faut 48 bits en entrée");
        }

        int[] res = new int[32];
        int indexRes = 0;

        for (int i = 0; i < 8; i++) {
            int debut = i * 6;
            String bitsL = "" + tab[debut] + tab[debut + 5];
            int l = Integer.parseInt(bitsL, 2);
            String bitsCol = "" + tab[debut + 1] + tab[debut + 2] +
                    tab[debut + 3] + tab[debut + 4];
            int col = Integer.parseInt(bitsCol, 2);
            int val = S[l][col];
            String binaire = String.format("%4s", Integer.toBinaryString(val)).replace(' ', '0');
            for (int j = 0; j < 4; j++) {
                res[indexRes] = Character.getNumericValue(binaire.charAt(j));
                indexRes++;
            }
        }
        return res;
    }

    public int[] fonction_F(int[] unD) {
        int[] perm = permutation(E, unD);
        int[] xorRes = xor(perm, tab_cles[0]);
        int[] sRes = fonction_S(xorRes);
        return permutation(P, sRes);
    }

    public int[] crypte(String message_clair) {
        int[] bits = stringToBits(message_clair);
        int[][] blocs64 = decoupage(bits, TAILLE_BLOC);
        int[][] blocsCryptes = new int[blocs64.length][TAILLE_BLOC];
        for (int i = 0; i < blocs64.length; i++) {
            int[] blocPermute = permutation(PERM_INITIALE, blocs64[i]);
            int[][] gd = decoupage(blocPermute, TAILLE_SOUS_BLOC);
            int[] G = gd[0];
            int[] D = gd[1];
            for (int ronde = 0; ronde < NB_RONDE; ronde++) {
                if (ronde >= tab_cles.length) {
                    genereCle(ronde);
                }
                int[] G_prev = G.clone();
                int[] D_prev = D.clone();
                int[] f_result = fonction_F(D_prev);
                D = xor(G_prev, f_result);
                G = D_prev;
            }
            int[] blocRecolle = recollage_bloc(new int[][]{G, D});
            blocsCryptes[i] = invPermutation(PERM_INVERSE, blocRecolle);
        }
        return recollage_bloc(blocsCryptes);
    }

    public String decrypte(int[] messageCode) {
        int[][] blocs64 = decoupage(messageCode, TAILLE_BLOC);
        int[][] blocsDecryptes = new int[blocs64.length][TAILLE_BLOC];
        for (int i = 0; i < blocs64.length; i++) {
            int[] blocPermute = permutation(PERM_INITIALE, blocs64[i]);
            int[][] gd = decoupage(blocPermute, TAILLE_SOUS_BLOC);
            int[] G = gd[0];
            int[] D = gd[1];
            for (int ronde = NB_RONDE - 1; ronde >= 0; ronde--) {
                int[] G_prev = G.clone();
                int[] D_prev = D.clone();
                int[] f_result = fonction_F(G_prev);
                G = xor(D_prev, f_result);
                D = G_prev;
            }
            int[] blocRecolle = recollage_bloc(new int[][]{G, D});
            blocsDecryptes[i] = invPermutation(PERM_INVERSE, blocRecolle);
        }

        return bitsToString(recollage_bloc(blocsDecryptes));
    }
}
