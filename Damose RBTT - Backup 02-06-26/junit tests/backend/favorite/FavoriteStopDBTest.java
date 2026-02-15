package backend.favorite;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import backend.model.Fermata;
import backend.user.User;

class FavoriteStopDBTest {
    
    private FavoriteStopDB db;
    
    // Useremo fermate diverse per test diversi
    private Fermata fermata1;
    private Fermata fermata2;
    private Fermata fermata3;

    @BeforeEach
    void setUp() throws IOException {
        // Inizializza il DB
        db = new FavoriteStopDB();
        
        // Inizializza oggetti Fermata puliti
        // (stopId, name, lat, lon)
        fermata1 = new Fermata("80001", "PIAZZA VENEZIA", 41.895, 12.482);
        fermata2 = new Fermata("80002", "COLOSSEO", 41.890, 12.492);
        fermata3 = new Fermata("80003", "TERMINI", 41.901, 12.501);
    }

    @Test
    void testAddFavoriteStop() throws IOException {
        String userId = "User_AddTest";
        
        // Act
        db.addFavoriteStop(userId, fermata1, "Fermata centro");

        // Assert
        assertTrue(db.isFavoriteStopPresent(userId, fermata1.getStopId()), 
            "La fermata dovrebbe risultare tra i preferiti dopo l'aggiunta");
    }

    @Test
    void testIsFavoriteStopPresent() throws IOException {
        String userId = "User_PresentTest";
        
        // Verifica iniziale: non deve esserci
        assertFalse(db.isFavoriteStopPresent(userId, fermata2.getStopId()));

        // Aggiunta
        db.addFavoriteStop(userId, fermata2, "Controllo presenza");

        // Verifica finale
        assertTrue(db.isFavoriteStopPresent(userId, fermata2.getStopId()));
    }

    @Test
    void testFindFavoriteStopsByUserId() throws IOException {
        String userId = "User_FindListTest_" + System.currentTimeMillis(); 
        
        // Arrange
        db.addFavoriteStop(userId, fermata1, "Prima");
        db.addFavoriteStop(userId, fermata2, "Seconda");
        db.addFavoriteStop(userId, fermata3, "Terza");

        // Act
        Optional<List<FavoriteStop>> lista = db.findFavoriteStopsByUserId(userId);

        // Assert
        assertTrue(lista.isPresent(), "L'Optional dovrebbe contenere una lista");
        assertEquals(3, lista.get().size(), "La lista DEVE contenere esattamente 3 elementi");
        
        // Verifica contenuto (controlliamo gli ID)
        boolean contieneFermata1 = lista.get().stream().anyMatch(f -> f.getFermataSalvata().getStopId().equals("80001"));
        boolean contieneFermata3 = lista.get().stream().anyMatch(f -> f.getFermataSalvata().getStopId().equals("80003"));
        
        assertTrue(contieneFermata1, "La lista deve contenere la fermata 80001");
        assertTrue(contieneFermata3, "La lista deve contenere la fermata 80003");
    }

    @Test
    void testFindFavoriteStopByUserIdAndStopId() throws IOException {
        String userId = "User_FindSingleTest";
        
        // Arrange
        db.addFavoriteStop(userId, fermata1, "Commento specifico");

        // Act
        Optional<FavoriteStop> fav = db.findFavoriteStopByUserIdAndStopId(userId, fermata1.getStopId());

        // Assert
        assertTrue(fav.isPresent());
        assertEquals("Commento specifico", fav.get().getCommento());
        assertEquals("PIAZZA VENEZIA", fav.get().getFermataSalvata().getName());
    }

    @Test
    void testDeleteFavoriteStop() throws IOException {
        String userId = "User_DeleteTest";
        
        // Arrange
        db.addFavoriteStop(userId, fermata1, "Da cancellare");
        assertTrue(db.isFavoriteStopPresent(userId, fermata1.getStopId()));
        
        // Act
        db.deleteFavoriteStop(userId, fermata1.getStopId());

        // Assert
        assertFalse(db.isFavoriteStopPresent(userId, fermata1.getStopId()), 
            "Dopo l'eliminazione la fermata non deve più essere presente");
    }
    
    @Test
    void testDuplicateException() throws IOException {
        String userId = "User_DuplicateTest";
        db.addFavoriteStop(userId, fermata1, "Primo inserimento");
        
        assertThrows(FavoriteAlreadyExistingException.class, () -> {
            db.addFavoriteStop(userId, fermata1, "Secondo inserimento");
        }, "Dovrebbe lanciare eccezione se inserisco due volte la stessa fermata");
    }
}
