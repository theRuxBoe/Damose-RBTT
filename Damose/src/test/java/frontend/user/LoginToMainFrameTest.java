package test.java.frontend.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.awt.CardLayout;
import java.awt.Container;
import java.lang.reflect.Field;

import javax.swing.JFrame;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import main.java.frontend.MainFrame;
import main.java.frontend.RightPanelToMainFrameController;
import main.java.frontend.user.LoginPanel;
import main.java.frontend.user.LoginToMainFrame;
import main.java.frontend.user.RegisterPanel;

@ExtendWith(MockitoExtension.class)
/**
 * (GENERATA DA AI)
 */
class LoginToMainFrameTest {

    @Mock
    private MainFrame mockMainFrame;
    @Mock
    private JFrame mockJFrame;
    @Mock
    private Container mockContentPane;
    @Mock
    private CardLayout mockCardLayout;

    // --- SETUP & TEARDOWN ---

    @BeforeEach
    void setUp() {
        // Configurazione base della catena di chiamate Swing:
        // MainFrame -> JFrame -> ContentPane (Container)
        // MainFrame -> CardLayout
        
        // Nota: Configuriamo i mock solo se necessario nel test specifico, 
        // ma qui prepariamo comportamenti comuni per evitare ripetizioni.
        // Usiamo lenient() perché in alcuni test (es. updateFromLogin) non servono.
    }

    @AfterEach
    void tearDown() throws Exception {
        // PULIZIA DELLO STATO STATICO
        // Essendo campi statici privati, dobbiamo resettarli via Reflection
        // altrimenti i test interferiscono tra loro.
        resetStaticField("logpane");
        resetStaticField("regpane");
        resetStaticField("frame");
        resetStaticField("logged");
        resetStaticField("user");
    }

    // Helper per resettare i campi statici privati a null/false
    private void resetStaticField(String fieldName) throws Exception {
        Field field = LoginToMainFrame.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        if (field.getType() == boolean.class) {
            field.setBoolean(null, false);
        } else {
            field.set(null, null);
        }
    }

    // --- TEST METODI UI (openLogin, openRegisteringPanel) ---

    @Test
    @DisplayName("openLogin: Crea il pannello, lo aggiunge al frame e mostra la card")
    void testOpenLogin_FirstTime() {
        // Arrange
        setupSwingMocks();

        // Intercettiamo il 'new LoginPanel()'
        try (MockedConstruction<LoginPanel> mockedPanel = mockConstruction(LoginPanel.class)) {
            
            // Act
            LoginToMainFrame.openLogin(mockMainFrame);

            // Assert
            // 1. Verifica creazione pannello
            assertEquals(1, mockedPanel.constructed().size());
            LoginPanel createdPanel = mockedPanel.constructed().get(0);

            // 2. Verifica aggiunta al content pane
            verify(mockContentPane).add(createdPanel, "Login Panel");

            // 3. Verifica switch del layout
            verify(mockCardLayout).show(mockContentPane, "Login Panel");

            // 4. Verifica repaint/revalidate
            verify(mockJFrame).repaint();
            verify(mockJFrame).revalidate();
        }
    }

    @Test
    @DisplayName("openLogin: Se richiamato, riusa il pannello esistente (Singleton behavior)")
    void testOpenLogin_AlreadyExists() {
        // Arrange
        setupSwingMocks();

        try (MockedConstruction<LoginPanel> mockedPanel = mockConstruction(LoginPanel.class)) {
            
            // Act
            // Prima chiamata
            LoginToMainFrame.openLogin(mockMainFrame);
            // Seconda chiamata
            LoginToMainFrame.openLogin(mockMainFrame);

            // Assert
            // Il costruttore deve essere chiamato UNA SOLA volta
            assertEquals(1, mockedPanel.constructed().size());
            
            // add() deve essere chiamato una sola volta
            verify(mockContentPane, times(1)).add(any(LoginPanel.class), eq("Login Panel"));
            
            // show() deve essere chiamato due volte (perché vogliamo visualizzarlo cmq)
            verify(mockCardLayout, times(2)).show(mockContentPane, "Login Panel");
        }
    }

    @Test
    @DisplayName("openRegisteringPanel: Crea e mostra il pannello di registrazione")
    void testOpenRegisteringPanel_Success() {
        // Arrange
        setupSwingMocks();

        // openRegisteringPanel assume che 'frame' sia già settato (solitamente via openLogin)
        // Quindi chiamiamo prima openLogin per inizializzare il campo statico 'frame'
        try (MockedConstruction<LoginPanel> mockLog = mockConstruction(LoginPanel.class);
             MockedConstruction<RegisterPanel> mockReg = mockConstruction(RegisterPanel.class)) {

            LoginToMainFrame.openLogin(mockMainFrame); // Setta il frame statico

            // Act
            LoginToMainFrame.openRegisteringPanel();

            // Assert
            assertEquals(1, mockReg.constructed().size());
            RegisterPanel createdReg = mockReg.constructed().get(0);

            verify(mockContentPane).add(createdReg, "Register Panel");
            verify(mockCardLayout).show(mockContentPane, "Register Panel");
        }
    }

    @Test
    @DisplayName("Edge Case - openRegisteringPanel: Lancia NPE se openLogin non è mai stato chiamato")
    void testOpenRegisteringPanel_WithoutFrame() {
        // Arrange
        // Non chiamiamo openLogin, quindi il campo statico 'frame' è null.
        
        // Act & Assert
        // Ci aspettiamo NullPointerException perché il codice fa frame.getFrame()...
        assertThrows(NullPointerException.class, () -> {
            LoginToMainFrame.openRegisteringPanel();
        });
    }

    // --- TEST METODI LOGICA (updateFromLogin, Getters) ---

    @Test
    @DisplayName("updateFromLogin: Aggiorna stato e chiama il controller del pannello destro")
    void testUpdateFromLogin_Success() {
        // Arrange
        String username = "TestUser";
        
        // Mock statico per il controller esterno chiamato dentro il metodo
        try (MockedStatic<RightPanelToMainFrameController> ctrlMock = mockStatic(RightPanelToMainFrameController.class)) {
            
            // Act
            LoginToMainFrame.updateFromLogin(true, username);

            // Assert
            assertTrue(LoginToMainFrame.isLogged());
            assertEquals(username, LoginToMainFrame.getCurrentUser());
            
            // Verifica che venga creato il pannello destro
            ctrlMock.verify(RightPanelToMainFrameController::createRightPanel);
        }
    }

    @Test
    @DisplayName("updateFromLogin: Logout o Login fallito resetta i dati")
    void testUpdateFromLogin_Logout() {
        // Arrange
        try (MockedStatic<RightPanelToMainFrameController> ctrlMock = mockStatic(RightPanelToMainFrameController.class)) {
            
            // Act
            LoginToMainFrame.updateFromLogin(false, null);

            // Assert
            assertFalse(LoginToMainFrame.isLogged());
            assertNull(LoginToMainFrame.getCurrentUser());
        }
    }

    // --- Helper Configuration ---
    
    private void setupSwingMocks() {
        // Configura la catena di get per evitare NPE
        when(mockMainFrame.getFrame()).thenReturn(mockJFrame);
        when(mockMainFrame.getCardLayout()).thenReturn(mockCardLayout);
        when(mockJFrame.getContentPane()).thenReturn(mockContentPane);
    }
}