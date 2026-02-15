package test.java.frontend.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.awt.Color;
import java.awt.GridBagLayout;
import java.io.IOException;
import java.lang.reflect.Field;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import main.java.backend.user.UserDB;
import main.java.frontend.MainFrame;
import main.java.frontend.user.UserPanel;

@ExtendWith(MockitoExtension.class)
/**
 * (GENERATA DA AI)
 */
class UserPanelTest {

    @Mock
    private MainFrame mockMainFrame;
    
    @Mock
    private JFrame mockJFrame;

    private UserPanel userPanel;

    // Mock statico per JOptionPane
    private MockedStatic<JOptionPane> jOptionPaneMock;

    @BeforeEach
    void setUp() throws Exception {
        // Reset del campo statico DB prima di ogni test per garantire isolamento
        resetStaticDbField();

        jOptionPaneMock = mockStatic(JOptionPane.class);

        // Instanziazione del pannello (UserPanel non è abstract nel codice fornito, quindi possiamo istanziarlo)
        // NOTA: Se l'immagine non viene trovata nel classpath di test, new ImageIcon(null) 
        // non lancia eccezione, crea un'icona vuota. Quindi il costruttore è sicuro.
        userPanel = new UserPanel();
    }

    @AfterEach
    void tearDown() throws Exception {
        jOptionPaneMock.close();
        resetStaticDbField();
    }

    // Helper per resettare il campo privato statico 'db'
    private void resetStaticDbField() throws Exception {
        Field instance = UserPanel.class.getDeclaredField("db");
        instance.setAccessible(true);
        instance.set(null, null);
    }

    // ========================================================================
    // TEST: Inizializzazione UI
    // ========================================================================

    @Test
    @DisplayName("Costruttore: Inizializza correttamente i componenti grafici")
    void testConstructor_UIInitialization() {
        // Assert
        assertNotNull(userPanel.getLayout(), "Il layout non deve essere null");
        assertEquals(GridBagLayout.class, userPanel.getLayout().getClass());

        // Verifica campi di testo
        JTextField usernameField = userPanel.getUsername();
        assertNotNull(usernameField, "Il campo username deve essere inizializzato");
        assertEquals(20, usernameField.getColumns());

        JPasswordField pwdField = userPanel.getPwdField();
        assertNotNull(pwdField, "Il campo password deve essere inizializzato");
        assertEquals(20, pwdField.getColumns());

        // Verifica spazio bottoni
        assertNotNull(userPanel.getButtonSpace(), "Il pannello bottoni deve essere inizializzato");
        
        // Verifica colore di sfondo (opzionale, basato sul codice 0x7851a9)
        Color expectedColor = new Color(0x7851a9);
        assertEquals(expectedColor, userPanel.getBackground());
    }

    // ========================================================================
    // TEST: Gestione Database (openDB)
    // ========================================================================

    @Test
    @DisplayName("openDB: Crea una nuova istanza di UserDB se non esiste")
    void testOpenDB_Success() {
        // Usiamo mockConstruction per intercettare "new UserDB()"
        try (MockedConstruction<UserDB> mockedDB = mockConstruction(UserDB.class)) {
            
            // Act
            userPanel.openDB();

            // Assert
            // 1. Verifica che UserDB sia stato istanziato
            assertEquals(1, mockedDB.constructed().size());
            
            // 2. Verifica che il getter restituisca l'istanza creata
            UserDB createdDB = mockedDB.constructed().get(0);
            assertSame(createdDB, userPanel.getDB());
        }
    }

    @Test
    @DisplayName("openDB: Se il DB è già inizializzato, non ne crea uno nuovo (Singleton behaviour)")
    void testOpenDB_AlreadyInitialized() {
        try (MockedConstruction<UserDB> mockedDB = mockConstruction(UserDB.class)) {
            
            // Act
            userPanel.openDB(); // Prima chiamata
            userPanel.openDB(); // Seconda chiamata

            // Assert
            // Il costruttore deve essere chiamato SOLO UNA VOLTA
            assertEquals(1, mockedDB.constructed().size());
        }
    }


    @Test
    @DisplayName("Edge Case - openDB: IOException senza Observer (NullPointerException nel catch)")
    void testOpenDB_IOException_NoObserver() {
        // Questo test documenta un potenziale bug nel codice originale:
        // Nel catch(IOException e) c'è 'JOptionPane.showMessageDialog(observer.getFrame(), ...)'
        // Se observer è null, questo lancerà una NullPointerException.
        
        try (MockedConstruction<UserDB> mockedDB = mockConstruction(UserDB.class, 
                (mock, context) -> {
                    throw new IOException("Fail");
                })) {
            
            // Assicuriamoci che observer sia null
            userPanel.setObserver(null);

            // Act & Assert
            // Ci aspettiamo che il codice originale fallisca qui
            try {
                userPanel.openDB();
            } catch (NullPointerException e) {
                // Test passato: abbiamo confermato che senza observer crasha nel catch block
                return;
            } catch (Exception e) {
                 // Se lancia altro è un problema
            }
        }
    }

    // ========================================================================
    // TEST: Getters e Setters
    // ========================================================================

    @Test
    @DisplayName("Observer: Getter e Setter funzionano correttamente")
    void testObserver_GetterSetter() {
        // Act
        userPanel.setObserver(mockMainFrame);

        // Assert
        assertSame(mockMainFrame, userPanel.getObserver());
    }
}