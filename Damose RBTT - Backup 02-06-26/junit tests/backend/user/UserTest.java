package backend.user;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserTest {
	
	private User user;

    @BeforeEach
    void setUp() {
        user = User.fromPlainPassword("1", "Daniele", "SuperPassword123");
    }

    @Test
    void correctPasswordReturnsTrue() {
        assertTrue(user.checkPassword("SuperPassword123"));
    }

    @Test
    void wrongPasswordReturnsFalse() {
        assertFalse(user.checkPassword("WrongPassword"));
    }

    @Test
    void nullPasswordThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> user.checkPassword(null));
    }

}
