package main.java.backend.user;

// TODO: Auto-generated Javadoc
/**
 * The Class UserDBManualTest.
 */
public class UserDBManualTest {

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {

        try {
            System.out.println("=== CREAZIONE UserDB ===");
            UserDB userDB = new UserDB();

            System.out.println("\n=== TEST 1: Registrazione valida ===");
            try {
                userDB.createNewAccount("alice", "password123");
                System.out.println("✔ Registrazione riuscita");
            } catch (Exception e) {
                System.out.println("✘ Registrazione fallita: " + e.getMessage());
            }

            System.out.println("\n=== TEST 2: Registrazione con username duplicato ===");
            try {
                userDB.createNewAccount("alice", "altraPassword");
                System.out.println("✘ ERRORE: registrazione duplicata permessa");
            } catch (Exception e) {
                System.out.println("✔ Correttamente bloccata: " + e.getMessage());
            }

            System.out.println("\n=== TEST 3: Registrazione con password vuota ===");
            try {
                userDB.createNewAccount("bob", "");
                System.out.println("✘ ERRORE: password vuota accettata");
            } catch (Exception e) {
                System.out.println("✔ Password vuota bloccata: " + e.getMessage());
            }

            System.out.println("\n=== TEST 4: Registrazione con username vuoto ===");
            try {
                userDB.createNewAccount("   ", "password123");
                System.out.println("✘ ERRORE: username vuoto accettato");
            } catch (Exception e) {
                System.out.println("✔ Username vuoto bloccato: " + e.getMessage());
            }

            System.out.println("\n=== TEST 5: Login corretto ===");
            boolean loginOk = userDB.logIn("alice", "password123");
            System.out.println(loginOk
                    ? "✔ Login riuscito"
                    : "✘ Login fallito");

            System.out.println("\n=== TEST 6: Login con password errata ===");
            boolean loginWrongPwd = userDB.logIn("alice", "passwordSbagliata");
            System.out.println(!loginWrongPwd
                    ? "✔ Login rifiutato correttamente"
                    : "✘ ERRORE: login con password errata");

            System.out.println("\n=== TEST 7: Login con password vuota ===");
            boolean loginEmptyPwd = userDB.logIn("alice", "");
            System.out.println(!loginEmptyPwd
                    ? "✔ Login rifiutato correttamente"
                    : "✘ ERRORE: login con password vuota");

            System.out.println("\n=== TEST 8: Login utente inesistente ===");
            boolean loginNoUser = userDB.logIn("utenteFantasma", "password123");
            System.out.println(!loginNoUser
                    ? "✔ Login rifiutato correttamente"
                    : "✘ ERRORE: login con utente inesistente");

            System.out.println("\n=== TEST 9: findUserByName (NON è login!) ===");
            userDB.findUserByName("alice").ifPresentOrElse(
                    u -> System.out.println("✔ Utente trovato: " + u.getUserName()),
                    () -> System.out.println("✘ Utente non trovato")
            );

            System.out.println("\n=== TEST COMPLETATI ===");

        } catch (Exception e) {
            System.err.println("Errore critico durante i test:");
            e.printStackTrace();
        }
    }
}
