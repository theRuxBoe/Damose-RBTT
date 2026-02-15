package backend.favorite;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import backend.model.Linea;
import backend.model.RisultatoLinea;
import backend.user.User;

class FavoriteRouteDBTest {
	
	private FavoriteRouteDB db;
    private RisultatoLinea lineaTest1;
    private RisultatoLinea lineaTest2;
    private RisultatoLinea lineaTest3;
    private RisultatoLinea lineaTest4;
    private RisultatoLinea lineaTest5;
    private RisultatoLinea lineaTest6;

    @BeforeEach
    void setUp() throws IOException {
        db = new FavoriteRouteDB();
    }

    @Test
    void testAddFavoriteRoute() throws IOException {
    	
    	lineaTest1 = new RisultatoLinea(new Linea("078", "078", "BIS", "TARANTELLI - CADUTI LIBERAZIONE", 3), "Tarantelli");
    	String id1 = "user1_id";
    	
		// Act
        db.addFavoriteRoute(id1, lineaTest1, "Linea che usavo per andare a scuola (quando non esplodeva)");

        // Assert
        assertTrue(db.isFavoriteRoutePresent(id1, lineaTest1.getRouteId(), lineaTest1.getDirectionName()), 
            "La linea dovrebbe risultare tra i preferiti dell'utente");
    }

    @Test
    void testIsFavoriteRoutePresent() throws IOException {
    	
    	lineaTest2 = new RisultatoLinea(new Linea("780", "780", "OP1", "", 3), "Boh");
    	String id2 = "user2_id";
    	
        // Verifica iniziale: non deve esserci
        assertFalse(db.isFavoriteRoutePresent(id2, lineaTest2.getRouteId(), lineaTest2.getDirectionName()));

        // Aggiunta
        db.addFavoriteRoute(id2, lineaTest2, "Test presenza");

        // Verifica finale
        assertTrue(db.isFavoriteRoutePresent(id2, lineaTest2.getRouteId(), lineaTest2.getDirectionName()));
    }

    @Test
    void testFindFavoriteRoutesByUserId() throws IOException {
    	
    	 lineaTest3 = new RisultatoLinea(new Linea("777", "777", "BIS", "AGRICOLTURA - BEATA VERGINE DEL CARMELO", 3), "Yes");
    	String id3 = "user3_id";
    	
        // Arrange
        db.addFavoriteRoute(id3, lineaTest3, "Test del find favorite");

        // Act
        Optional<List<FavoriteRoute>> lista = db.findFavoriteRoutesByUserId(id3);

        // Assert
        assertTrue(lista.isPresent(), "L'Optional dovrebbe contenere una lista");
        assertEquals(1, lista.get().size(), "La lista dovrebbe contenere esattamente 3 elementi");
        assertEquals("777", lista.get().get(0).getLineaSalvata().getRouteId());
    }

    @Test
    void testFindFavoriteRouteByUserIdAndRouteId() throws IOException {
    	
    	lineaTest4 = new RisultatoLinea(new Linea("992", "992", "BIS", "STAZIONE IPOGEO DEGLI OTTAVI - STAZIONE IPOGEO DEGLI OTTAVI", 3), "Wow");
    	String id4 = "user4_id";
    	
        // Arrange
        db.addFavoriteRoute(id4, lineaTest4, "Speciale");

        // Act
        Optional<FavoriteRoute> fav = db.findFavoriteRouteByUserIdRouteIdDirName(id4, lineaTest4.getRouteId(), lineaTest4.getDirectionName());

        // Assert
        assertTrue(fav.isPresent());
        assertEquals("Speciale", fav.get().getCommento());
    }

    @Test
    void testDeleteFavoriteRoute() throws IOException {
    	
    	lineaTest5 = new RisultatoLinea(new Linea("030", "030", "TUS", "STAZIONE LA STORTA - TRAGLIATELLA- CIVICO 225", 3), "yahoo");
    	String id5 = "user5_id";
    	
        // Arrange
        db.addFavoriteRoute(id5, lineaTest5, "Test di eliminazione");
        
        // Act
        db.deleteFavoriteRoute(id5, lineaTest5.getRouteId(), lineaTest5.getDirectionName());

        // Assert
        assertFalse(db.isFavoriteRoutePresent(id5, lineaTest5.getRouteId(), lineaTest5.getDirectionName()), 
            "Dopo l'eliminazione la linea non deve più essere presente");
    }

    @Test
    void testDuplicateException() throws IOException {
    	
    	lineaTest6 = new RisultatoLinea(new Linea("025", "025", "BIS", "FORMICHI - MOMBASIGLIO", 3), "pastalpesto");
    	String id6 = "user6_id";
    	
        // Arrange
        db.addFavoriteRoute(id6, lineaTest6, "Primo inserimento");

        // Assert
        // Verifichiamo che lanci l'eccezione se proviamo a inserire la stessa linea per lo stesso utente
        assertThrows(FavoriteAlreadyExistingException.class, () -> {
            db.addFavoriteRoute(id6, lineaTest6, "Secondo inserimento fallimentare");
        }, "Dovrebbe lanciare FavoriteAlreadyExistingException per duplicati");
    }

}
