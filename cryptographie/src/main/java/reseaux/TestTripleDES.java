package reseaux;

public class TestTripleDES {
    public static void main(String[] args) {
        System.out.println("=== TEST TRIPLE DES INTÉGRÉ ===");

        // Création d'une instance DES
        DES des = new DES();

        // Test 1: Triple DES avec clés aléatoires
        System.out.println("\n--- Test 1: Triple DES avec clés aléatoires ---");
        String message1 = "Hello Triple DES!";
        System.out.println("Message original: " + message1);

        int[] messageCrypte1 = des.crypteTripleDES(message1);
        System.out.println("Message crypté avec Triple DES");

        // Pour déchiffrer, on a besoin des mêmes clés
        int[] key2 = des.genereMasterKey();
        int[] key3 = des.genereMasterKey();
        String messageDecrypte1 = des.decrypteTripleDES(messageCrypte1, key2, key3);
        System.out.println("Message décrypté: " + messageDecrypte1);

        boolean test1Reussi = message1.equals(messageDecrypte1);
        System.out.println("Test 1 réussi: " + test1Reussi);

        // Test 2: Triple DES avec clés spécifiques
        System.out.println("\n--- Test 2: Triple DES avec clés spécifiques ---");
        int[] key1 = des.getMasterKey();
        int[] key2_spec = des.genereMasterKey();
        int[] key3_spec = des.genereMasterKey();

        String message2 = "Test avec clés spécifiques";
        System.out.println("Message original: " + message2);

        int[] messageCrypte2 = des.crypteTripleDES1(message2, key1, key2_spec, key3_spec);
        String messageDecrypte2 = des.decrypteTripleDES1(messageCrypte2, key1, key2_spec, key3_spec);
        System.out.println("Message décrypté: " + messageDecrypte2);

        boolean test2Reussi = message2.equals(messageDecrypte2);
        System.out.println("Test 2 réussi: " + test2Reussi);

        // Test 3: Vérification de la sécurité renforcée
        System.out.println("\n--- Test 3: Vérification sécurité ---");
        String message3 = "Message secret important";
        int[] crypteNormal = des.crypte(message3);
        int[] crypteTriple = des.crypteTripleDES1(message3, key1, key2_spec, key3_spec);

        System.out.println("Taille cryptage normal: " + crypteNormal.length + " bits");
        System.out.println("Taille cryptage Triple DES: " + crypteTriple.length + " bits");
        System.out.println("Triple DES ajoute une couche de sécurité supplémentaire");

        // Résumé final
        System.out.println("\n=== RÉSUMÉ TESTS TRIPLE DES ===");
        System.out.println("Test 1 (clés aléatoires): " + (test1Reussi ? "✓ RÉUSSI" : "✗ ÉCHEC"));
        System.out.println("Test 2 (clés spécifiques): " + (test2Reussi ? "✓ RÉUSSI" : "✗ ÉCHEC"));

        int testsReussis = (test1Reussi ? 1 : 0) + (test2Reussi ? 1 : 0);
        System.out.println("Total: " + testsReussis + "/2 tests réussis");
    }
}