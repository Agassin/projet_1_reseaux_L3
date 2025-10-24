package reseaux;

import java.util.Random;

/**
 * Implémentation de l'algorithme de chiffrement DES (Data Encryption Standard)
 * et Triple DES.
 *
 * Cette classe permet de chiffrer et déchiffrer des messages textuels en utilisant
 * l'algorithme DES standard (16 rondes) ainsi que sa variante Triple DES pour
 * une sécurité renforcée.
 *
 * @author Votre Nom
 * @version 1.0
 */
public class DES {

    // ============================================================================
    // CONSTANTES - Tables de permutation et paramètres DES
    // ============================================================================

    /** Taille d'un bloc DES en bits (64 bits) */
    private static final int TAILLE_BLOC = 64;

    /** Taille d'un sous-bloc (moitié gauche ou droite) en bits (32 bits) */
    private static final int TAILLE_SOUS_BLOC = 32;

    /** Nombre de rondes de chiffrement DES (16 rondes) */
    private static final int NB_RONDE = 16;

    /**
     * Table de permutation initiale (IP)
     * Réorganise les 64 bits d'entrée selon un schéma prédéfini
     */
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

    /**
     * Table de permutation inverse finale (IP^-1)
     * Inverse la permutation initiale pour reconstituer le bloc final
     */
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

    /**
     * Permutation Choice 1 (PC-1)
     * Sélectionne et permute 56 bits de la clé maître de 64 bits
     */
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

    /**
     * Permutation Choice 2 (PC-2)
     * Sélectionne 48 bits parmi les 56 bits pour générer une sous-clé de ronde
     */
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

    /**
     * Table d'expansion (E)
     * Étend 32 bits à 48 bits en dupliquant certains bits
     */
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

    /**
     * Permutation P
     * Permute les 32 bits de sortie des S-Boxes
     */
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

    /**
     * S-Boxes (Boîtes de substitution)
     * 8 tables de substitution non-linéaires qui transforment 6 bits en 4 bits
     * Élément clé de la sécurité du DES
     */
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

    /**
     * Tableau de décalage pour la génération des sous-clés
     * Indique le nombre de positions de rotation à gauche pour chaque ronde
     */
    private static final int[] TAB_DECALAGE = {1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1};

    // ============================================================================
    // ATTRIBUTS D'INSTANCE
    // ============================================================================

    /** Clé maître de 64 bits utilisée pour la génération des sous-clés */
    private int[] masterKey;

    /** Tableau des 16 sous-clés de 48 bits, une par ronde */
    private int[][] tab_cles;

    // ============================================================================
    // CONSTRUCTEURS
    // ============================================================================

    /**
     * Constructeur par défaut.
     * Génère une clé maître aléatoire et initialise les sous-clés.
     */
    public DES() {
        this.masterKey = genereMasterKey();
        this.tab_cles = new int[NB_RONDE][48];
        genererToutesCles();
    }

    /**
     * Constructeur avec clé spécifique.
     * Utilisé notamment pour le Triple DES avec des clés différentes.
     *
     * @param masterKey Clé maître de 64 bits. Si null ou invalide,
     *                  une clé aléatoire sera générée.
     */
    public DES(int[] masterKey) {
        // Vérification de la validité de la clé fournie
        if (masterKey != null && masterKey.length == 64) {
            this.masterKey = masterKey.clone(); // Clone pour éviter les modifications externes
        } else {
            this.masterKey = genereMasterKey(); // Génération d'une clé aléatoire
        }
        this.tab_cles = new int[NB_RONDE][48];
        genererToutesCles();
    }

    // ============================================================================
    // GÉNÉRATION DES CLÉS
    // ============================================================================

    /**
     * Génère les 16 sous-clés de 48 bits à partir de la clé maître.
     *
     * Algorithme :
     * 1. Application de PC-1 pour obtenir 56 bits
     * 2. Découpage en deux moitiés C et D de 28 bits chacune
     * 3. Pour chaque ronde :
     *    - Rotation à gauche de C et D selon TAB_DECALAGE
     *    - Concaténation de C et D
     *    - Application de PC-2 pour obtenir la sous-clé de 48 bits
     */
    private void genererToutesCles() {
        // Étape 1 : Application de la permutation PC-1 (64 bits -> 56 bits)
        int[] pc1Key = permutation(PC1, masterKey);

        // Étape 2 : Découpage en deux moitiés de 28 bits (C et D)
        int[][] cd = decoupage(pc1Key, 28);
        int[] C = cd[0]; // Moitié gauche
        int[] D = cd[1]; // Moitié droite

        // Étape 3 : Génération des 16 sous-clés
        for (int i = 0; i < NB_RONDE; i++) {
            // Rotation à gauche de C et D (1 ou 2 positions selon la ronde)
            C = decalle_gauche(C, TAB_DECALAGE[i]);
            D = decalle_gauche(D, TAB_DECALAGE[i]);

            // Concaténation de C et D (56 bits)
            int[] CD = recollage_bloc(new int[][]{C, D});

            // Application de PC-2 pour obtenir 48 bits
            tab_cles[i] = permutation(PC2, CD);
        }
    }

    /**
     * Génère une clé maître aléatoire de 64 bits.
     *
     * @return Tableau de 64 entiers (0 ou 1) représentant la clé
     */
    public int[] genereMasterKey() {
        int[] cle = new int[64];
        Random random = new Random();
        // Génération aléatoire de chaque bit
        for (int i = 0; i < 64; i++) {
            cle[i] = random.nextInt(2); // 0 ou 1
        }
        return cle;
    }

    // ============================================================================
    // CONVERSIONS CHAÎNE <-> BITS
    // ============================================================================

    /**
     * Convertit une chaîne de caractères en tableau de bits.
     *
     * Chaque caractère est converti en son code ASCII sur 8 bits.
     * Les bits sont ordonnés du plus significatif au moins significatif.
     *
     * @param message Chaîne à convertir
     * @return Tableau de bits (0 et 1), ou tableau vide si message null
     */
    public int[] stringToBits(String message) {
        if (message == null) {
            return new int[0];
        }

        // Conversion de la chaîne en tableau d'octets
        byte[] bytes = message.getBytes();
        int[] bits = new int[bytes.length * 8];
        int index = 0;

        // Pour chaque octet
        for (int i = 0; i < bytes.length; i++) {
            byte b = bytes[i];
            // Extraction des 8 bits de l'octet (du MSB au LSB)
            for (int bitPosition = 7; bitPosition >= 0; bitPosition--) {
                int temp = b >> bitPosition; // Décalage du bit à la position 0
                int bitVal = temp & 1;        // Masque pour extraire le bit
                bits[index] = bitVal;
                index++;
            }
        }
        return bits;
    }

    /**
     * Convertit un tableau de bits en chaîne de caractères.
     *
     * Les bits sont regroupés par 8 pour former des octets,
     * puis convertis en caractères ASCII.
     *
     * @param blocs Tableau de bits (doit être un multiple de 8)
     * @return Chaîne de caractères correspondante
     * @throws IllegalArgumentException Si la longueur n'est pas un multiple de 8
     */
    public String bitsToString(int[] blocs) {
        if (blocs == null || blocs.length == 0) {
            return "";
        }
        if (blocs.length % 8 != 0) {
            throw new IllegalArgumentException("La longueur du tableau doit être un multiple de 8");
        }

        int nCara = blocs.length / 8; // Nombre de caractères
        byte[] bytes = new byte[nCara];

        // Pour chaque caractère (8 bits)
        for (int i = 0; i < nCara; i++) {
            int byteVal = 0;
            // Reconstruction de l'octet bit par bit
            for (int j = 0; j < 8; j++) {
                int indice = i * 8 + j;
                int bit = blocs[indice];
                byteVal = byteVal * 2; // Décalage à gauche
                if (bit == 1) {
                    byteVal = byteVal + 1; // Ajout du bit
                }
            }
            bytes[i] = (byte) byteVal;
        }
        return new String(bytes);
    }

    // ============================================================================
    // OPÉRATIONS DE PERMUTATION ET MANIPULATION DE BITS
    // ============================================================================

    /**
     * Applique une permutation sur un bloc de bits.
     *
     * @param tab_permutation Table définissant la permutation (positions 1-indexed)
     * @param bloc Bloc de bits à permuter
     * @return Bloc permuté de même taille que tab_permutation
     */
    public int[] permutation(int[] tab_permutation, int[] bloc) {
        int[] resultat = new int[tab_permutation.length];
        // Pour chaque position du résultat, on prend le bit indiqué par la table
        for (int i = 0; i < tab_permutation.length; i++) {
            resultat[i] = bloc[tab_permutation[i] - 1]; // -1 car indexation à partir de 1
        }
        return resultat;
    }

    /**
     * Applique la permutation inverse (pour décoder).
     *
     * @param tab_permutation Table de permutation originale
     * @param bloc Bloc à permuter inversement
     * @return Bloc avec permutation inversée
     */
    public int[] invPermutation(int[] tab_permutation, int[] bloc) {
        int[] resultat = new int[bloc.length];
        // Inverse : le bit i du bloc va à la position indiquée par tab_permutation[i]
        for (int i = 0; i < tab_permutation.length; i++) {
            resultat[tab_permutation[i] - 1] = bloc[i];
        }
        return resultat;
    }

    /**
     * Découpe un tableau de bits en plusieurs blocs de taille fixe.
     *
     * @param bloc Tableau de bits à découper
     * @param tailleBlocs Taille de chaque bloc (doit diviser exactement la longueur)
     * @return Tableau 2D contenant les différents blocs
     */
    public int[][] decoupage(int[] bloc, int tailleBlocs) {
        int nbBlocs = bloc.length / tailleBlocs;
        int[][] blocs = new int[nbBlocs][tailleBlocs];
        // Copie de chaque segment dans un bloc
        for (int i = 0; i < nbBlocs; i++) {
            System.arraycopy(bloc, i * tailleBlocs, blocs[i], 0, tailleBlocs);
        }
        return blocs;
    }

    /**
     * Recolle plusieurs blocs de bits en un seul tableau continu.
     *
     * @param blocs Tableau 2D de blocs à recoller
     * @return Tableau 1D contenant tous les bits concaténés
     */
    public int[] recollage_bloc(int[][] blocs) {
        // Calcul de la longueur totale
        int totalLength = 0;
        for (int[] bloc : blocs) {
            totalLength += bloc.length;
        }

        // Copie de tous les blocs dans un seul tableau
        int[] result = new int[totalLength];
        int index = 0;
        for (int[] bloc : blocs) {
            System.arraycopy(bloc, 0, result, index, bloc.length);
            index += bloc.length;
        }
        return result;
    }

    /**
     * Effectue une rotation (décalage circulaire) à gauche sur un bloc de bits.
     *
     * @param bloc Bloc à décaler
     * @param nbCran Nombre de positions de décalage
     * @return Nouveau bloc décalé
     */
    public int[] decalle_gauche(int[] bloc, int nbCran) {
        int[] res = new int[bloc.length];
        // Les bits de position nbCran à fin vont au début
        System.arraycopy(bloc, nbCran, res, 0, bloc.length - nbCran);
        // Les nbCran premiers bits vont à la fin
        System.arraycopy(bloc, 0, res, bloc.length - nbCran, nbCran);
        return res;
    }

    /**
     * Effectue un XOR bit à bit entre deux tableaux de même taille.
     *
     * @param tab1 Premier tableau
     * @param tab2 Second tableau
     * @return Résultat du XOR (0 si bits identiques, 1 sinon)
     * @throws IllegalArgumentException Si les tableaux n'ont pas la même longueur
     */
    public int[] xor(int[] tab1, int[] tab2) {
        if (tab1.length != tab2.length) {
            throw new IllegalArgumentException("Les tableaux doivent avoir la même longueur");
        }
        int[] res = new int[tab1.length];
        // XOR : 0 si identiques, 1 si différents
        for (int i = 0; i < tab1.length; i++) {
            res[i] = (tab1[i] == tab2[i]) ? 0 : 1;
        }
        return res;
    }

    // ============================================================================
    // FONCTION F ET S-BOXES
    // ============================================================================

    /**
     * Applique les S-Boxes (boîtes de substitution) sur 48 bits.
     *
     * Les 48 bits d'entrée sont divisés en 8 groupes de 6 bits.
     * Chaque groupe passe par une S-Box qui le transforme en 4 bits.
     * Le résultat final est de 32 bits.
     *
     * @param tab Tableau de 48 bits
     * @return Tableau de 32 bits après substitution
     * @throws IllegalArgumentException Si l'entrée ne fait pas 48 bits
     */
    public int[] fonction_S(int[] tab) {
        if (tab.length != 48) {
            throw new IllegalArgumentException("Il faut 48 bits en entrée");
        }

        int[] res = new int[32];
        int indexRes = 0;

        // Traitement de chaque groupe de 6 bits (8 S-Boxes)
        for (int i = 0; i < 8; i++) {
            int debut = i * 6;

            // Les bits 1 et 6 déterminent la ligne (0-3)
            String bitsL = "" + tab[debut] + tab[debut + 5];
            int l = Integer.parseInt(bitsL, 2);

            // Les bits 2,3,4,5 déterminent la colonne (0-15)
            String bitsCol = "" + tab[debut + 1] + tab[debut + 2] +
                    tab[debut + 3] + tab[debut + 4];
            int col = Integer.parseInt(bitsCol, 2);

            // Consultation de la S-Box correspondante
            int val = S[i][l][col];

            // Conversion de la valeur (0-15) en 4 bits
            String binaire = String.format("%4s", Integer.toBinaryString(val)).replace(' ', '0');
            for (int j = 0; j < 4; j++) {
                res[indexRes] = Character.getNumericValue(binaire.charAt(j));
                indexRes++;
            }
        }
        return res;
    }

    /**
     * Fonction F de Feistel : cœur de l'algorithme DES.
     *
     * Étapes :
     * 1. Expansion de 32 bits à 48 bits (table E)
     * 2. XOR avec la sous-clé de la ronde
     * 3. Passage par les S-Boxes (48 bits -> 32 bits)
     * 4. Permutation P finale
     *
     * @param unD Moitié droite du bloc (32 bits)
     * @param ronde Numéro de la ronde (0-15) pour sélectionner la sous-clé
     * @return Résultat de la fonction F (32 bits)
     */
    public int[] fonction_F(int[] unD, int ronde) {
        // Étape 1 : Expansion 32 -> 48 bits
        int[] perm = permutation(E, unD);

        // Étape 2 : XOR avec la sous-clé de ronde
        int[] xorRes = xor(perm, tab_cles[ronde]);

        // Étape 3 : S-Boxes 48 -> 32 bits
        int[] sRes = fonction_S(xorRes);

        // Étape 4 : Permutation P
        return permutation(P, sRes);
    }

    // ============================================================================
    // CHIFFREMENT ET DÉCHIFFREMENT DES
    // ============================================================================

    /**
     * Chiffre un message texte avec l'algorithme DES.
     *
     * Algorithme :
     * 1. Conversion du texte en bits
     * 2. Ajout de padding si nécessaire (complément à 64 bits)
     * 3. Stockage de la longueur originale (32 bits)
     * 4. Pour chaque bloc de 64 bits :
     *    - Permutation initiale (IP)
     *    - 16 rondes de Feistel : G(i+1) = D(i), D(i+1) = G(i) XOR F(D(i), K(i))
     *    - Permutation inverse (IP^-1)
     * 5. Ajout de la longueur en en-tête du message chiffré
     *
     * @param message_clair Texte en clair à chiffrer
     * @return Tableau de bits chiffré avec longueur en en-tête
     */
    public int[] crypte(String message_clair) {
        // Étape 1 : Conversion en bits
        int[] bits = stringToBits(message_clair);
        int longueurOriginale = bits.length;

        // Étape 2 : Padding pour compléter au multiple de 64 bits
        int reste = bits.length % TAILLE_BLOC;
        if (reste != 0) {
            // Ajout de bits à 0 pour compléter le dernier bloc
            int nouveauLength = bits.length + (TAILLE_BLOC - reste);
            int[] bitsAvecPadding = new int[nouveauLength];
            System.arraycopy(bits, 0, bitsAvecPadding, 0, bits.length);
            // Les bits ajoutés sont automatiquement à 0 (valeur par défaut en Java)
            bits = bitsAvecPadding;
        }

        // Étape 3 : Encodage de la longueur originale sur 32 bits
        // Ceci permet de retirer le padding lors du déchiffrement
        int[] longueurBits = new int[32];
        for (int i = 31; i >= 0; i--) {
            longueurBits[31 - i] = (longueurOriginale >> i) & 1;
        }

        // Étape 4 : Découpage en blocs de 64 bits
        int[][] blocs64 = decoupage(bits, TAILLE_BLOC);
        int[][] blocsCryptes = new int[blocs64.length][TAILLE_BLOC];

        // Chiffrement de chaque bloc
        for (int i = 0; i < blocs64.length; i++) {
            // Permutation initiale (IP)
            int[] blocPermute = permutation(PERM_INITIALE, blocs64[i]);

            // Découpage en moitié gauche (G) et droite (D)
            int[][] gd = decoupage(blocPermute, TAILLE_SOUS_BLOC);
            int[] G = gd[0];
            int[] D = gd[1];

            // 16 rondes de Feistel
            for (int ronde = 0; ronde < NB_RONDE; ronde++) {
                int[] G_prev = G; // Sauvegarde de G

                // Calcul de F(D, K_ronde)
                int[] f_result = fonction_F(D, ronde);

                // Échange : G devient l'ancien D
                G = D;

                // D devient G_prev XOR F(D, K)
                D = xor(G_prev, f_result);
            }

            // Recollement : attention, on inverse D et G (swap final)
            int[] blocRecolle = recollage_bloc(new int[][]{D, G});

            // Permutation inverse finale (IP^-1)
            blocsCryptes[i] = permutation(PERM_INVERSE, blocRecolle);
        }

        // Étape 5 : Recollement de tous les blocs chiffrés
        int[] resultat = recollage_bloc(blocsCryptes);

        // Ajout de la longueur en en-tête (32 bits + message chiffré)
        int[] resultatAvecLongueur = new int[32 + resultat.length];
        System.arraycopy(longueurBits, 0, resultatAvecLongueur, 0, 32);
        System.arraycopy(resultat, 0, resultatAvecLongueur, 32, resultat.length);

        return resultatAvecLongueur;
    }

    /**
     * Déchiffre un message chiffré avec l'algorithme DES.
     *
     * Le processus est identique au chiffrement, mais les sous-clés sont
     * utilisées dans l'ordre inverse (de K15 à K0).
     *
     * Algorithme :
     * 1. Extraction de la longueur originale (32 premiers bits)
     * 2. Pour chaque bloc de 64 bits :
     *    - Permutation initiale (IP)
     *    - 16 rondes de Feistel avec clés inversées
     *    - Permutation inverse (IP^-1)
     * 3. Suppression du padding selon la longueur originale
     * 4. Conversion des bits en texte
     *
     * @param messageCode Message chiffré (avec longueur en en-tête)
     * @return Texte déchiffré
     */
    public String decrypte(int[] messageCode) {
        // Étape 1 : Extraction de la longueur originale (32 premiers bits)
        int longueurOriginale = 0;
        for (int i = 0; i < 32; i++) {
            longueurOriginale = (longueurOriginale << 1) | messageCode[i];
        }

        // Extraction du message chiffré (sans les 32 bits de longueur)
        int[] messageCrypteSansLongueur = new int[messageCode.length - 32];
        System.arraycopy(messageCode, 32, messageCrypteSansLongueur, 0, messageCrypteSansLongueur.length);

        // Étape 2 : Découpage en blocs de 64 bits
        int[][] blocs64 = decoupage(messageCrypteSansLongueur, TAILLE_BLOC);
        int[][] blocsDecryptes = new int[blocs64.length][TAILLE_BLOC];

        // Déchiffrement de chaque bloc
        for (int i = 0; i < blocs64.length; i++) {
            // Permutation initiale (IP)
            int[] blocPermute = permutation(PERM_INITIALE, blocs64[i]);

            // Découpage en moitié gauche (G) et droite (D)
            int[][] gd = decoupage(blocPermute, TAILLE_SOUS_BLOC);
            int[] G = gd[0];
            int[] D = gd[1];

            // 16 rondes de Feistel avec clés dans l'ordre INVERSE
            for (int ronde = 0; ronde < NB_RONDE; ronde++) {
                int[] G_prev = G; // Sauvegarde de G

                // Utilisation des clés en ordre inverse : K15, K14, ..., K0
                int[] f_result = fonction_F(D, NB_RONDE - 1 - ronde);

                // Échange : G devient l'ancien D
                G = D;

                // D devient G_prev XOR F(D, K)
                D = xor(G_prev, f_result);
            }

            // Recollement : swap final de D et G
            int[] blocRecolle = recollage_bloc(new int[][]{D, G});

            // Permutation inverse finale (IP^-1)
            blocsDecryptes[i] = permutation(PERM_INVERSE, blocRecolle);
        }

        // Étape 3 : Recollement de tous les blocs
        int[] tousLesBits = recollage_bloc(blocsDecryptes);

        // Étape 4 : Suppression du padding - on ne garde que la longueur originale
        int[] bitsOriginaux = new int[longueurOriginale];
        System.arraycopy(tousLesBits, 0, bitsOriginaux, 0, longueurOriginale);

        // Étape 5 : Conversion des bits en texte
        return bitsToString(bitsOriginaux);
    }

    // ============================================================================
    // TRIPLE DES (3DES)
    // ============================================================================

    /**
     * Chiffre un message avec l'algorithme Triple DES.
     *
     * Triple DES applique DES trois fois de suite pour renforcer la sécurité :
     * - Chiffrement avec la clé courante
     * - Déchiffrement du résultat (avec la même clé)
     * - Chiffrement du résultat (avec la même clé)
     *
     * Cette implémentation utilise une seule clé (DES-EEE).
     * Pour une sécurité maximale, on pourrait utiliser 3 clés différentes (3TDES).
     *
     * Formule : C = E_K3(D_K2(E_K1(P)))
     * Ici simplifié avec K1=K2=K3 : C = E_K(D_K(E_K(P)))
     *
     * @param message_clair Texte en clair à chiffrer
     * @return Message chiffré avec Triple DES
     */
    public int[] crypte3DES(String message_clair) {
        // Triple DES : Encrypt -> Decrypt -> Encrypt
        // Étape 1 : Premier chiffrement
        int[] premiere_encryption = crypte(message_clair);

        // Étape 2 : Déchiffrement du résultat
        String decrypt_intermediaire = decrypte(premiere_encryption);

        // Étape 3 : Second chiffrement
        return crypte(decrypt_intermediaire);
    }

    /**
     * Déchiffre un message chiffré avec l'algorithme Triple DES.
     *
     * Le déchiffrement Triple DES inverse les opérations du chiffrement :
     * - Déchiffrement du message
     * - Chiffrement du résultat
     * - Déchiffrement du résultat
     *
     * Formule : P = D_K1(E_K2(D_K3(C)))
     * Ici simplifié avec K1=K2=K3 : P = D_K(E_K(D_K(C)))
     *
     * @param messageCode Message chiffré avec Triple DES
     * @return Texte déchiffré
     */
    public String decrypte3DES(int[] messageCode) {
        // Triple DES inverse : Decrypt -> Encrypt -> Decrypt
        // Étape 1 : Premier déchiffrement
        String premier_decrypt = decrypte(messageCode);

        // Étape 2 : Chiffrement du résultat
        int[] encrypt_intermediaire = crypte(premier_decrypt);

        // Étape 3 : Second déchiffrement
        return decrypte(encrypt_intermediaire);
    }

    // ============================================================================
    // MÉTHODES ANNEXES
    // ============================================================================

    /**
     * Retourne la clé maître utilisée par cette instance DES.
     *
     * @return Copie de la clé maître (64 bits)
     */
    public int[] getMasterKey() {
        return masterKey.clone();
    }

    /**
     * Retourne les sous-clés générées pour les 16 rondes.
     *
     * @return Copie du tableau des sous-clés (16 clés de 48 bits)
     */
    public int[][] getTabCles() {
        int[][] copie = new int[tab_cles.length][];
        for (int i = 0; i < tab_cles.length; i++) {
            copie[i] = tab_cles[i].clone();
        }
        return copie;
    }
}