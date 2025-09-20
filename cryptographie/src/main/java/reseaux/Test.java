package reseaux;

public class Test {
    public static void main(String[] args) {
        DES des = new DES();

        // Test de conversion stringToBits et bitsToString
        String testMessage = "Ta gueule ";
        System.out.println("Message original: " + testMessage);

        int[] bits = des.stringToBits(testMessage);
        System.out.println("Bits: " + arrayToString(bits));

        String convertedBack = des.bitsToString(bits);
        System.out.println("Converti en retour: " + convertedBack);

        // Test de cryptage et décryptage
        System.out.println("\n=== Test Cryptage/Décryptage ===");

        int[] crypted = des.crypte(testMessage);
        System.out.println("Message crypté (bits): " + arrayToString(crypted));

        String decrypted = des.decrypte(crypted);
        System.out.println("Message décrypté: " + decrypted);

        // Vérification
        System.out.println("Test réussi: " + testMessage.equals(decrypted));
    }

    private static String arrayToString(int[] array) {
        if (array == null) return "null";
        if (array.length > 20) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 10; i++) {
                sb.append(array[i]);
            }
            sb.append("...");
            for (int i = array.length - 10; i < array.length; i++) {
                sb.append(array[i]);
            }
            return sb.toString();
        }

        StringBuilder sb = new StringBuilder();
        for (int value : array) {
            sb.append(value);
        }
        return sb.toString();
    }
}