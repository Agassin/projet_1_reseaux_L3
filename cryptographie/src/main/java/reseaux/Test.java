package reseaux;

public class Test {
    public static void main(String[] args) {
        DES des = new DES();

        // Test 1: Message simple
        System.out.println("=== TEST 1 - Message simple ===");
        String message1 = "Hello";
        System.out.println("Message original: " + message1);

        int[] messageCrypte1 = des.crypte(message1);
        System.out.println("Message crypté: " + messageCrypte1.length + " bits");

        String messageDecrypte1 = des.decrypte(messageCrypte1);
        System.out.println("Message décrypté: " + messageDecrypte1);

        boolean test1Reussi = message1.equals(messageDecrypte1);
        System.out.println("Test 1 réussi: " + test1Reussi);
        System.out.println();

        // Test 2: Message exactement 64 bits (8 caractères)
        System.out.println("=== TEST 2 - Message 64 bits ===");
        String message2 = "12345678";
        System.out.println("Message original: " + message2);

        int[] messageCrypte2 = des.crypte(message2);
        System.out.println("Message crypté: " + messageCrypte2.length + " bits");

        String messageDecrypte2 = des.decrypte(messageCrypte2);
        System.out.println("Message décrypté: " + messageDecrypte2);

        boolean test2Reussi = message2.equals(messageDecrypte2);
        System.out.println("Test 2 réussi: " + test2Reussi);
        System.out.println();

        // Test 3: Message avec caractères spéciaux
        System.out.println("=== TEST 3 - Caractères spéciaux ===");
        String message3 = "Café@123";
        System.out.println("Message original: " + message3);

        int[] messageCrypte3 = des.crypte(message3);
        System.out.println("Message crypté: " + messageCrypte3.length + " bits");

        String messageDecrypte3 = des.decrypte(messageCrypte3);
        System.out.println("Message décrypté: " + messageDecrypte3);

        boolean test3Reussi = message3.equals(messageDecrypte3);
        System.out.println("Test 3 réussi: " + test3Reussi);
        System.out.println();

        // Test 4: Message long
        System.out.println("=== TEST 4 - Message long ===");
        String message4 = "Ceci est un message plus long";
        System.out.println("Message original: " + message4);

        int[] messageCrypte4 = des.crypte(message4);
        System.out.println("Message crypté: " + messageCrypte4.length + " bits");

        String messageDecrypte4 = des.decrypte(messageCrypte4);
        System.out.println("Message décrypté: " + messageDecrypte4);

        boolean test4Reussi = message4.equals(messageDecrypte4);
        System.out.println("Test 4 réussi: " + test4Reussi);
        System.out.println();

        // Test 5: Vérification de la cohérence
        System.out.println("=== TEST 5 - Double chiffrement/déchiffrement ===");
        String message5 = "Test123";
        System.out.println("Message original: " + message5);

        // Premier chiffrement/déchiffrement
        int[] crypte1 = des.crypte(message5);
        String decrypte1 = des.decrypte(crypte1);

        // Deuxième chiffrement/déchiffrement
        int[] crypte2 = des.crypte(decrypte1);
        String decrypte2 = des.decrypte(crypte2);

        System.out.println("Après double cycle: " + decrypte2);
        boolean test5Reussi = message5.equals(decrypte2);
        System.out.println("Test 5 réussi: " + test5Reussi);
        System.out.println();

        // Résumé des tests
        System.out.println("=== RÉSUMÉ DES TESTS ===");
        System.out.println("Test 1 (simple): " + (test1Reussi ? "✓ RÉUSSI" : "✗ ÉCHEC"));
        System.out.println("Test 2 (64 bits): " + (test2Reussi ? "✓ RÉUSSI" : "✗ ÉCHEC"));
        System.out.println("Test 3 (spéciaux): " + (test3Reussi ? "✓ RÉUSSI" : "✗ ÉCHEC"));
        System.out.println("Test 4 (long): " + (test4Reussi ? "✓ RÉUSSI" : "✗ ÉCHEC"));
        System.out.println("Test 5 (cohérence): " + (test5Reussi ? "✓ RÉUSSI" : "✗ ÉCHEC"));

        int testsReussis = (test1Reussi ? 1 : 0) + (test2Reussi ? 1 : 0) +
                (test3Reussi ? 1 : 0) + (test4Reussi ? 1 : 0) + (test5Reussi ? 1 : 0);
        System.out.println("\nTotal: " + testsReussis + "/5 tests réussis");
    }
}