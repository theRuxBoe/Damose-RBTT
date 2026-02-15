package backend.user;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;

class UserDBTest {
    private UserDB userDB;

    @BeforeEach
    void setUp() throws IOException {
        userDB = new UserDB();
    }

    @Test
    void testCreateAndLoginSuccess() throws Exception {
        userDB.createNewAccount("Marco", "pass123456");
        
        assertTrue(userDB.logIn("Marco", "pass123456"), "Il login dovrebbe riuscire");
    }

    @Test
    void testDuplicateAccountThrowsException() throws Exception {
        userDB.createNewAccount("Giovanni", "ambarabaciccicocco");
        
        assertThrows(AccountAlreadyExistsException.class, () -> {
            userDB.createNewAccount("Giovanni", "altraPass");
        }, "Dovrebbe impedire la creazione di un duplicato");
    }

    @Test
    void testLoginFailure() throws Exception {
        userDB.createNewAccount("Luca", "brotatochip");
        
        assertFalse(userDB.logIn("Luca", "sbagliata"), "Password errata dovrebbe fallire");
        assertFalse(userDB.logIn("Inesistente", "brotatochip"), "Utente inesistente dovrebbe fallire");
    }
    
    @Test
    void testFindUserByName() throws IllegalArgumentException, AccountAlreadyExistsException, IOException {
        userDB.createNewAccount("Matteo", "vangelosecondomatteo");
        
        assertTrue(userDB.findUserByName("Matteo").isPresent(), "Dovrebbe trovare l'utente Matteo");
    }
}
