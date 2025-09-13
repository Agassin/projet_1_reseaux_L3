package reseaux;

public class Test {
    public static void main(String[] args) {
        DES DES = new DES();

        String message = "ABCDEFGH";
        int[] messageCrypte = DES.crypte(message);

        System.out.println("Message clair : " + message);

        System.out.print("Message crypté (bits) : ");
        for (int bit : messageCrypte) System.out.print(bit);
        System.out.println();

        String messageDecrypte = DES.decrypte(messageCrypte);
        System.out.println("Message décrypté : " + messageDecrypte);

        // Test de la fonction F avec la clé de ronde 1
        int[] bloc32 = new int[32];
        for (int i = 0; i < 32; i++) bloc32[i] = i % 2; // petit exemple

        int[] F = DES.fonction_F(DES.tab_cles[0], bloc32);
        System.out.print("Résultat fonction F (32 bits) : ");
        for (int b : F) System.out.print(b);
        System.out.println();
    }
}
