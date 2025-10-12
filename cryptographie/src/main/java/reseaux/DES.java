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
            32, 27, 3, 9,
            19, 13, 30, 6,
            22, 11, 4, 25
    };

    // Tables S-Box complètes (8 boîtes de substitution)
    private static final int[][][] S = {
            {
                    {14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7},
                    {0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8},
                    {4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0},
                    {15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13}
            },
            {
                    {15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10},
                    {3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5},
                    {0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15},
                    {13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9}
            },
            {
                    {10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8},
                    {13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1},
                    {13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7},
                    {1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12}
            },
            {
                    {7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15},
                    {13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9},
                    {10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4},
                    {3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14}
            },
            {
                    {2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9},
                    {14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6},
                    {4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14},
                    {11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3}
            },
            {
                    {12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11},
                    {10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8},
                    {9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6},
                    {4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13}
            },
            {
                    {4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1},
                    {13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6},
                    {1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2},
                    {6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12}
            },
            {
                    {13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7},
                    {1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2},
                    {7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8},
                    {2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11}
            }
    };

    private static final int[] TAB_DECALAGE = {1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1};

    private int[] masterKey;
    private int[][] tab_cles;

    public DES() {
        this.masterKey = genereMasterKey();
        this.tab_cles = new int[NB_RONDE][48];
        genererToutesCles();
    }

    private void genererToutesCles() {
        int[] pc1Key = permutation(PC1, masterKey);
        int[][] cd = decoupage(pc1Key, 28);
        int[] C = cd[0];
        int[] D = cd[1];

        for (int i = 0; i < NB_RONDE; i++) {
            // Décaler C et D pour cette ronde
            C = decalle_gauche(C, TAB_DECALAGE[i]);
            D = decalle_gauche(D, TAB_DECALAGE[i]);

            // Générer la clé pour cette ronde
            int[] CD = recollage_bloc(new int[][]{C, D});
            tab_cles[i] = permutation(PC2, CD);
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
        int[] resultat = new int[bloc.length];
        for (int i = 0; i < tab_permutation.length; i++) {
            resultat[tab_permutation[i] - 1] = bloc[i];
        }
        return resultat;
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
            res[i] = (tab1[i] == tab2[i]) ? 0 : 1;
        }
        return res;
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
            int val = S[i][l][col];
            String binaire = String.format("%4s", Integer.toBinaryString(val)).replace(' ', '0');
            for (int j = 0; j < 4; j++) {
                res[indexRes] = Character.getNumericValue(binaire.charAt(j));
                indexRes++;
            }
        }
        return res;
    }

    // CORRECTION: Ajout du paramètre ronde
    public int[] fonction_F(int[] unD, int ronde) {
        int[] perm = permutation(E, unD);
        int[] xorRes = xor(perm, tab_cles[ronde]);
        int[] sRes = fonction_S(xorRes);
        return permutation(P, sRes);
    }

    public int[] crypte(String message_clair) {
        int[] bits = stringToBits(message_clair);
        int longueurOriginale = bits.length;

        // Ajout du padding si nécessaire
        int reste = bits.length % TAILLE_BLOC;
        if (reste != 0) {
            int nouveauLength = bits.length + (TAILLE_BLOC - reste);
            int[] bitsAvecPadding = new int[nouveauLength];
            System.arraycopy(bits, 0, bitsAvecPadding, 0, bits.length);
            bits = bitsAvecPadding;
        }

        // Stocker la longueur originale dans les 32 premiers bits du résultat
        int[] longueurBits = new int[32];
        for (int i = 31; i >= 0; i--) {
            longueurBits[31 - i] = (longueurOriginale >> i) & 1;
        }

        int[][] blocs64 = decoupage(bits, TAILLE_BLOC);
        int[][] blocsCryptes = new int[blocs64.length][TAILLE_BLOC];

        for (int i = 0; i < blocs64.length; i++) {
            int[] blocPermute = permutation(PERM_INITIALE, blocs64[i]);
            int[][] gd = decoupage(blocPermute, TAILLE_SOUS_BLOC);
            int[] G = gd[0];
            int[] D = gd[1];

            for (int ronde = 0; ronde < NB_RONDE; ronde++) {
                int[] G_prev = G;
                int[] f_result = fonction_F(D, ronde);
                G = D;
                D = xor(G_prev, f_result);
            }

            // Après la dernière ronde, on inverse G et D
            int[] blocRecolle = recollage_bloc(new int[][]{D, G});
            blocsCryptes[i] = permutation(PERM_INVERSE, blocRecolle);
        }

        int[] resultat = recollage_bloc(blocsCryptes);

        // Ajouter la longueur au début
        int[] resultatAvecLongueur = new int[32 + resultat.length];
        System.arraycopy(longueurBits, 0, resultatAvecLongueur, 0, 32);
        System.arraycopy(resultat, 0, resultatAvecLongueur, 32, resultat.length);

        return resultatAvecLongueur;
    }

    public String decrypte(int[] messageCode) {
        // Extraire la longueur originale des 32 premiers bits
        int longueurOriginale = 0;
        for (int i = 0; i < 32; i++) {
            longueurOriginale = (longueurOriginale << 1) | messageCode[i];
        }

        // Extraire le message crypté (sans les 32 premiers bits)
        int[] messageCrypteSansLongueur = new int[messageCode.length - 32];
        System.arraycopy(messageCode, 32, messageCrypteSansLongueur, 0, messageCrypteSansLongueur.length);

        int[][] blocs64 = decoupage(messageCrypteSansLongueur, TAILLE_BLOC);
        int[][] blocsDecryptes = new int[blocs64.length][TAILLE_BLOC];

        for (int i = 0; i < blocs64.length; i++) {
            int[] blocPermute = permutation(PERM_INITIALE, blocs64[i]);
            int[][] gd = decoupage(blocPermute, TAILLE_SOUS_BLOC);
            int[] G = gd[0];
            int[] D = gd[1];

            // Pour le déchiffrement : même structure que chiffrement mais clés inversées
            for (int ronde = 0; ronde < NB_RONDE; ronde++) {
                int[] G_prev = G;
                int[] f_result = fonction_F(D, NB_RONDE - 1 - ronde);
                G = D;
                D = xor(G_prev, f_result);
            }

            // Après la dernière ronde, on inverse G et D
            int[] blocRecolle = recollage_bloc(new int[][]{D, G});
            blocsDecryptes[i] = permutation(PERM_INVERSE, blocRecolle);
        }

        int[] tousLesBits = recollage_bloc(blocsDecryptes);

        // Ne garder que les bits correspondant à la longueur originale
        int[] bitsOriginaux = new int[longueurOriginale];
        System.arraycopy(tousLesBits, 0, bitsOriginaux, 0, longueurOriginale);

        return bitsToString(bitsOriginaux);
    }
}
