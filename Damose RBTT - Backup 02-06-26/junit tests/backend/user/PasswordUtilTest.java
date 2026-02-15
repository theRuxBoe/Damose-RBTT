package backend.user;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PasswordUtilTest {

	@Test
    void samePasswordProducesSameHash() {
        String password = "SuperPassword123";

        String hash1 = PasswordUtil.hash(password);
        String hash2 = PasswordUtil.hash(password);

        assertEquals(hash1, hash2,
                "La stessa password dovrebbe produrre sempre lo stesso hash");
    }

    @Test
    void differentPasswordsProduceDifferentHashes() {
        String password1 = "PasswordOne";
        String password2 = "PasswordTwo";

        String hash1 = PasswordUtil.hash(password1);
        String hash2 = PasswordUtil.hash(password2);

        assertNotEquals(hash1, hash2,
                "Password diverse dovrebbero produrre hash diversi");
    }

    @Test
    void nullInputThrowsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            PasswordUtil.hash(null);
        });

        assertEquals("Password nulla non consentita", exception.getMessage());
    }
    
    @Test
    void hashIs64CharactersLong() {
        String password = "TestPassword";
        String hash = PasswordUtil.hash(password);

        assertEquals(64, hash.length(),
                "Un hash SHA-256 deve essere lungo 64 caratteri esadecimali");
    }
}
