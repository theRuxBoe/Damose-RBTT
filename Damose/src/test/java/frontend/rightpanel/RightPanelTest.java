package test.java.frontend.rightpanel;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import main.java.frontend.rightpanel.FocusController;
import main.java.frontend.rightpanel.RightPanel;
import main.java.frontend.user.LoginToMainFrame;

/**
 * Classe di test unitari per RightPanel.
 * Verifica la logica di inizializzazione del pulsante principale (in base allo stato di login)
 * e la gestione del cambio pannello centrale.
 * * (GENERATA DA AI)
 */
class RightPanelTest {

    /**
     * TEST 1: Verifica inizializzazione quando l'utente NON è loggato.
     * Il bottone deve mostrare il testo "Login".
     */
    @Test
    void testInitialization_NotLogged() {
        // Mock dei metodi statici chiamati nel costruttore
        try (MockedStatic<LoginToMainFrame> loginMock = mockStatic(LoginToMainFrame.class);
             MockedStatic<FocusController> focusMock = mockStatic(FocusController.class)) {

            // Configurazione: Utente non loggato
            loginMock.when(LoginToMainFrame::isLogged).thenReturn(false);

            // Istanziazione
            RightPanel panel = new RightPanel();

            // Verifica che FocusController sia stato configurato
            focusMock.verify(() -> FocusController.setRightPanel(panel), times(1));

            // Ricerca del bottone nel pannello
            JButton foundButton = null;
            for (Component c : panel.getComponents()) {
                if (c instanceof JButton) {
                    foundButton = (JButton) c;
                    break;
                }
            }

            assertNotNull(foundButton, "Il pannello deve contenere un JButton.");
            assertEquals("Login", foundButton.getText(), "Se l'utente non è loggato, il bottone deve dire 'Login'.");
        }
    }

    /**
     * TEST 2: Verifica inizializzazione quando l'utente È loggato.
     * Il bottone deve mostrare il testo "Ricerca".
     */
    @Test
    void testInitialization_Logged() {
        try (MockedStatic<LoginToMainFrame> loginMock = mockStatic(LoginToMainFrame.class);
             MockedStatic<FocusController> focusMock = mockStatic(FocusController.class)) {

            // Configurazione: Utente loggato
            loginMock.when(LoginToMainFrame::isLogged).thenReturn(true);

            RightPanel panel = new RightPanel();

            JButton foundButton = null;
            for (Component c : panel.getComponents()) {
                if (c instanceof JButton) {
                    foundButton = (JButton) c;
                    break;
                }
            }

            assertNotNull(foundButton);
            assertEquals("Ricerca", foundButton.getText(), "Se l'utente è loggato, il bottone deve dire 'Ricerca'.");
        }
    }

    /**
     * TEST 3: Verifica della funzione setAndShowCurrent.
     * Controlla che il pannello centrale venga sostituito correttamente e che
     * la variabile 'current' venga aggiornata.
     */
    @Test
    void testSetAndShowCurrent() {
        // Setup minimo per istanziare la classe senza errori
        try (MockedStatic<LoginToMainFrame> loginMock = mockStatic(LoginToMainFrame.class);
             MockedStatic<FocusController> focusMock = mockStatic(FocusController.class)) {

            loginMock.when(LoginToMainFrame::isLogged).thenReturn(false);
            RightPanel panel = new RightPanel();

            // Creazione di pannelli dummy per il test
            JPanel panel1 = new JPanel();
            JPanel panel2 = new JPanel();

            // Aggiunta del primo pannello
            panel.setAndShowCurrent(panel1);
            assertEquals(panel1, panel.getCurrent(), "Il pannello corrente dovrebbe essere panel1");

            // Sostituzione con il secondo pannello
            panel.setAndShowCurrent(panel2);
            assertEquals(panel2, panel.getCurrent(), "Il pannello corrente dovrebbe essere aggiornato a panel2");
            
            // Verifica opzionale: il panel1 non deve più essere figlio del componente grafico
            boolean containsPanel1 = false;
            for(Component c : panel.getComponents()) {
                if(c == panel1) containsPanel1 = true;
            }
            assertFalse(containsPanel1, "Il vecchio pannello dovrebbe essere rimosso dalla UI");
        }
    }
}