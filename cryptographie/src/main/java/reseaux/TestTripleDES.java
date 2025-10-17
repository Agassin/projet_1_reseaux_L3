package reseaux;

public class TestTripleDES {

    public static void main(String[] args) {
        System.out.println("=== TEST TRIPLE DES ===\n");

        // Créer une instance de DES
        DES des = new DES();

        // Test 1: Message court
        System.out.println("--- Test 1: Message court ---");
        testTripleDES(des, "Bonjour!");

        // Test 2: Message moyen
        System.out.println("\n--- Test 2: Message moyen ---");
        testTripleDES(des, "Ceci est un test de Triple DES");

        // Test 3: Message long
        System.out.println("\n--- Test 3: Message long ---");
        testTripleDES(des, "Le Triple DES est un algorithme de chiffrement qui applique DES trois fois avec trois clés différentes pour augmenter la sécurité.");

        // Test 4: Message avec caractères spéciaux
        System.out.println("\n--- Test 4: Caractères spéciaux ---");
        testTripleDES(des, "Héllo Wørld! @#$%^&*()");

        // Test 5: Message vide
        System.out.println("\n--- Test 5: Message vide ---");
        testTripleDES(des, "");

        // Test 6: Un seul caractère
        System.out.println("\n--- Test 6: Un seul caractère ---");
        testTripleDES(des, "A");

        // Test 7: Comparaison DES vs Triple DES
        System.out.println("\n--- Test 7: Comparaison DES vs Triple DES ---");
        comparerDESvsTripleDES(des, "Message de comparaison");

        System.out.println("\n=== FIN DES TESTS ===");
    }

    /**
     * Teste le chiffrement et déchiffrement Triple DES
     */
    private static void testTripleDES(DES des, String message) {
        System.out.println("Message original : \"" + message + "\"");
        System.out.println("Longueur : " + message.length() + " caractères");

        try {
            // Chiffrement
            long startCrypt = System.currentTimeMillis();
            int[] messageCrypte = des.crypte3DES(message);
            long endCrypt = System.currentTimeMillis();

            System.out.println("Message chiffré : " + messageCrypte.length + " bits");
            System.out.println("Temps de chiffrement : " + (endCrypt - startCrypt) + " ms");

            // Afficher les premiers bits du message chiffré
            System.out.print("Premiers bits chiffrés : ");
            for (int i = 192; i < Math.min(192 + 32, messageCrypte.length); i++) {
                System.out.print(messageCrypte[i]);
            }
            System.out.println("...");

            // Déchiffrement
            long startDecrypt = System.currentTimeMillis();
            String messageDecrypte = des.decrypte3DES(messageCrypte);
            long endDecrypt = System.currentTimeMillis();

            System.out.println("Message déchiffré : \"" + messageDecrypte + "\"");
            System.out.println("Temps de déchiffrement : " + (endDecrypt - startDecrypt) + " ms");

            // Vérification
            if (message.equals(messageDecrypte)) {
                System.out.println("✓ TEST RÉUSSI : Le message déchiffré correspond à l'original");
            } else {
                System.out.println("✗ TEST ÉCHOUÉ : Le message déchiffré ne correspond pas");
                System.out.println("  Attendu : \"" + message + "\"");
                System.out.println("  Obtenu  : \"" + messageDecrypte + "\"");
            }

        } catch (Exception e) {
            System.out.println("✗ ERREUR : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Compare les résultats de DES simple et Triple DES
     */
    private static void comparerDESvsTripleDES(DES des, String message) {
        System.out.println("Message : \"" + message + "\"");

        try {
            // DES simple
            int[] crypteDES = des.crypte(message);
            String decrypteDES = des.decrypte(crypteDES);

            // Triple DES
            int[] crypte3DES = des.crypte3DES(message);
            String decrypte3DES = des.decrypte3DES(crypte3DES);

            System.out.println("\nDES simple :");
            System.out.println("  - Taille chiffrée : " + crypteDES.length + " bits");
            System.out.println("  - Déchiffrement réussi : " + message.equals(decrypteDES));

            System.out.println("\nTriple DES :");
            System.out.println("  - Taille chiffrée : " + crypte3DES.length + " bits");
            System.out.println("  - Overhead (3 clés) : 192 bits");
            System.out.println("  - Déchiffrement réussi : " + message.equals(decrypte3DES));

            // Vérifier que les chiffrements sont différents
            boolean identique = true;
            if (crypteDES.length == crypte3DES.length - 192) {
                for (int i = 32; i < crypteDES.length && identique; i++) {
                    if (crypteDES[i] != crypte3DES[i + 192 - 32]) {
                        identique = false;
                    }
                }
            } else {
                identique = false;
            }

            System.out.println("\nLes chiffrements sont différents : " + !identique);

        } catch (Exception e) {
            System.out.println("✗ ERREUR lors de la comparaison : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Affiche un tableau de bits de manière lisible
     */
    private static void afficherBits(int[] bits, int max) {
        for (int i = 0; i < Math.min(bits.length, max); i++) {
            System.out.print(bits[i]);
            if ((i + 1) % 8 == 0) System.out.print(" ");
            if ((i + 1) % 64 == 0) System.out.println();
        }
        if (bits.length > max) {
            System.out.print("... (+" + (bits.length - max) + " bits)");
        }
        System.out.println();
    }
}