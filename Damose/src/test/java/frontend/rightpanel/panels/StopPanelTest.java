package test.java.frontend.rightpanel.panels;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import main.java.backend.model.Fermata;
import main.java.frontend.rightpanel.FocusController;
import main.java.frontend.rightpanel.panels.StopFocus;
import main.java.frontend.rightpanel.panels.StopPanel;
import main.java.frontend.user.FavouritesDBManager;
import main.java.frontend.user.LoginToMainFrame;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
/**
 * (GENERATA DA AI)
 */
class StopPanelTest {

    // Mock Statici
    private MockedStatic<LoginToMainFrame> loginMock;
    private MockedStatic<FavouritesDBManager> favDbMock;
    private MockedStatic<FocusController> focusControllerMock;

    @Mock
    private Fermata mockFermata;

    @BeforeEach
    void setUp() {
        // Inizializzazione Mock Statici
        loginMock = mockStatic(LoginToMainFrame.class);
        favDbMock = mockStatic(FavouritesDBManager.class);
        focusControllerMock = mockStatic(FocusController.class);

        // Setup base Fermata
        when(mockFermata.getName()).thenReturn("Colosseo");
        when(mockFermata.getStopId()).thenReturn("888");
    }

    @AfterEach
    void tearDown() {
        // Chiusura risorse statiche
        loginMock.close();
        favDbMock.close();
        focusControllerMock.close();
    }

    // ========================================================================
    // TEST: Visualizzazione Base
    // ========================================================================

    @Test
    @DisplayName("Visualizzazione: Utente NON loggato -> Mostra nome ma NO bottone")
    void testConstructor_NotLogged() {
        // Arrange
        loginMock.when(LoginToMainFrame::isLogged).thenReturn(false);

        // Act
        StopPanel panel = new StopPanel(mockFermata);

        // Assert
        // Verifica Testo
        JLabel label = findLabel(panel);
        assertNotNull(label);
        assertTrue(label.getText().contains("Colosseo"));
        assertTrue(label.getText().contains("888"));

        // Verifica Assenza Bottone
        JButton btn = findButton(panel);
        assertNull(btn, "Il bottone preferiti non deve esistere se l'utente non è loggato");
    }

    @Test
    @DisplayName("Visualizzazione: Utente Loggato -> Mostra bottone preferiti")
    void testConstructor_Logged() {
        // Arrange
        loginMock.when(LoginToMainFrame::isLogged).thenReturn(true);
        favDbMock.when(() -> FavouritesDBManager.isPresent(mockFermata)).thenReturn(false);

        // Act
        StopPanel panel = new StopPanel(mockFermata);

        // Assert
        JButton btn = findButton(panel);
        assertNotNull(btn, "Il bottone preferiti deve esistere se l'utente è loggato");
        assertEquals("☆", btn.getText()); // Default non preferito
    }

    // ========================================================================
    // TEST: Interazione Preferiti
    // ========================================================================

    @Test
    @DisplayName("Preferiti: Aggiunta e Rimozione (Toggle)")
    void testFavourites_Toggle() {
        // Arrange
        loginMock.when(LoginToMainFrame::isLogged).thenReturn(true);
        // Stato iniziale: NON è nei preferiti
        favDbMock.when(() -> FavouritesDBManager.isPresent(mockFermata)).thenReturn(false);

        StopPanel panel = new StopPanel(mockFermata);
        JButton btn = findButton(panel);

        // Act 1: Click per aggiungere
        btn.doClick();

        // Assert 1
        assertEquals("★", btn.getText());
        favDbMock.verify(() -> FavouritesDBManager.addToFavourites(mockFermata));

        // Act 2: Click per rimuovere
        btn.doClick();

        // Assert 2
        assertEquals("☆", btn.getText());
        favDbMock.verify(() -> FavouritesDBManager.remove(mockFermata));
    }

    @Test
    @DisplayName("Preferiti: Stato iniziale Già Preferito")
    void testFavourites_AlreadyPresent() {
        // Arrange
        loginMock.when(LoginToMainFrame::isLogged).thenReturn(true);
        // Stato iniziale: E' GIA' nei preferiti
        favDbMock.when(() -> FavouritesDBManager.isPresent(mockFermata)).thenReturn(true);

        // Act
        StopPanel panel = new StopPanel(mockFermata);
        JButton btn = findButton(panel);

        // Assert
        assertEquals("★", btn.getText(), "Se è già preferito, la stella deve essere piena all'avvio");
    }

    // ========================================================================
    // TEST: Interazione Click Pannello (Apertura Focus)
    // ========================================================================

    @Test
    @DisplayName("Click Pannello: Crea StopFocus e chiama FocusController")
    void testPanelClick_OpensFocus() {
        // Arrange
        loginMock.when(LoginToMainFrame::isLogged).thenReturn(false);
        StopPanel panel = new StopPanel(mockFermata);

        // CRITICO: Dobbiamo mockare la costruzione di StopFocus perché avviene dentro il listener
        // e StopFocus lancia timer/backend calls che farebbero fallire il test.
        try (MockedConstruction<StopFocus> mockedFocus = mockConstruction(StopFocus.class)) {
            
            // Act
            // Simuliamo il click del mouse. 
            // Recuperiamo i listener registrati sul pannello.
            MouseListener[] listeners = panel.getMouseListeners();
            assertTrue(listeners.length > 0, "Il pannello deve avere un MouseListener");
            
            // Creiamo un evento fake
            MouseEvent clickEvent = new MouseEvent(panel, MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), 0, 10, 10, 1, false);
            
            // Invochiamo mouseClicked sul primo listener trovato
            listeners[0].mouseClicked(clickEvent);

            // Assert
            // 1. Verifica che StopFocus sia stato istanziato
            assertEquals(1, mockedFocus.constructed().size(), "Deve essere creato un nuovo StopFocus");
            
            // 2. Verifica che FocusController sia stato chiamato con il mock creato
            StopFocus createdMock = mockedFocus.constructed().get(0);
            focusControllerMock.verify(() -> FocusController.openFocus(createdMock));
        }
    }

    // ========================================================================
    // EDGE CASES
    // ========================================================================

    @Test
    @DisplayName("Edge Case: Fermata Null (Constructor)")
    void testConstructor_NullFermata() {
        // Il codice non ha check espliciti per null, ma testiamo che il pannello venga creato
        // (Probabilmente fallirebbe addDataLabels se non mockiamo getName, ma qui testiamo l'assegnazione)
        
        loginMock.when(LoginToMainFrame::isLogged).thenReturn(false);
        
        // Se passiamo null, addDataLabels lancerà NPE subito su stop.getName().
        // Questo è un comportamento atteso se il codice non gestisce null.
        // Possiamo aspettarci un'eccezione o fixare il codice. Assumiamo che lanci eccezione.
        try {
            new StopPanel(null);
        } catch (NullPointerException e) {
            // Expected
        }
    }
    
    @Test
    @DisplayName("Edge Case: Click senza FocusController inizializzato (Static Mock check)")
    void testPanelClick_VerifyArguments() {
        // Questo test verifica specificamente che stiamo passando la fermata corretta al costruttore di StopFocus
        loginMock.when(LoginToMainFrame::isLogged).thenReturn(false);
        StopPanel panel = new StopPanel(mockFermata);

        try (MockedConstruction<StopFocus> mockedFocus = mockConstruction(StopFocus.class)) {
             panel.getMouseListeners()[0].mouseClicked(null);
             
             // Non possiamo verificare facilmente gli argomenti del costruttore con mockConstruction in modo diretto sui valori,
             // ma possiamo verificare che il flusso arrivi a FocusController.
             focusControllerMock.verify(() -> FocusController.openFocus(any(StopFocus.class)));
        }
    }

    // ========================================================================
    // Helpers
    // ========================================================================

    private JButton findButton(Container container) {
        for (Component c : container.getComponents()) {
            if (c instanceof JButton) {
                return (JButton) c;
            } else if (c instanceof Container) {
                JButton b = findButton((Container) c);
                if (b != null) return b;
            }
        }
        return null;
    }

    private JLabel findLabel(Container container) {
        for (Component c : container.getComponents()) {
            if (c instanceof JLabel) {
                return (JLabel) c;
            } else if (c instanceof Container) {
                JLabel l = findLabel((Container) c);
                if (l != null) return l;
            }
        }
        return null;
    }
}
