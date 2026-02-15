package test.java.frontend;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.lang.reflect.Field;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import main.java.frontend.MainFrame;
import main.java.frontend.RightPanelToMainFrameController;
import main.java.frontend.rightpanel.FocusPanel;
import main.java.frontend.rightpanel.RightPanel;
import main.java.frontend.rightpanel.SearchPanel;
import main.java.frontend.user.LoginToMainFrame;

/**
 * (GENERATA DA AI)
 */
class RightPanelToMainFrameControllerTest {

    @Mock private MainFrame mainFrameMock;
    @Mock private JFrame jFrameMock;
    @Mock private JPanel mainPanelMock;
    @Mock private Container contentPaneMock;
    @Mock private CardLayout cardLayoutMock;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);

        // Setup della catena di chiamate per il MainFrame
        when(mainFrameMock.getFrame()).thenReturn(jFrameMock);
        when(mainFrameMock.getMainPanel()).thenReturn(mainPanelMock);
        when(mainFrameMock.getCardLayout()).thenReturn(cardLayoutMock);
        when(jFrameMock.getContentPane()).thenReturn(contentPaneMock);

        // Impostiamo il frame nel controller prima di ogni test
        RightPanelToMainFrameController.setFrame(mainFrameMock);
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
        // PULIZIA DELLO STATO STATICO:
        // Poiché la classe usa campi statici, dobbiamo resettarli a null
        // altrimenti i test si influenzano a vicenda.
        resetStaticField("rp");
        resetStaticField("mframe");
    }

    // --- TEST CREATE RIGHT PANEL ---

    @Test
    @DisplayName("CreateRightPanel: Utente Loggato -> Apre FavouritePanel")
    void testCreateRightPanel_Logged() {
        // 1. Mockiamo LoginToMainFrame statico
        try (MockedStatic<LoginToMainFrame> loginMock = mockStatic(LoginToMainFrame.class);
             // 2. Intercettiamo la new RightPanel()
             MockedConstruction<RightPanel> rpConstructor = mockConstruction(RightPanel.class)) {

            // Configurazione: Utente loggato
            loginMock.when(LoginToMainFrame::isLogged).thenReturn(true);

            // Act
            RightPanelToMainFrameController.createRightPanel();

            // Recuperiamo l'istanza di RightPanel creata dentro il metodo
            RightPanel createdRp = rpConstructor.constructed().get(0);

            // Assert
            // Verifica che il pannello sia stato aggiunto al mainPanel
            verify(mainPanelMock).add(createdRp, BorderLayout.EAST);
            // Verifica switch CardLayout
            verify(cardLayoutMock).show(contentPaneMock, "Main Panel");
            // Verifica logica di business: deve aprire i preferiti
            verify(createdRp).openFavouritePanel();
            verify(createdRp, never()).openSearchPanel();
            // Verifica refresh grafico
            verify(jFrameMock).repaint();
            verify(jFrameMock).revalidate();
        }
    }

    @Test
    @DisplayName("CreateRightPanel: Utente Non Loggato -> Apre SearchPanel")
    void testCreateRightPanel_NotLogged() {
        try (MockedStatic<LoginToMainFrame> loginMock = mockStatic(LoginToMainFrame.class);
             MockedConstruction<RightPanel> rpConstructor = mockConstruction(RightPanel.class)) {

            // Configurazione: Utente NON loggato
            loginMock.when(LoginToMainFrame::isLogged).thenReturn(false);

            // Act
            RightPanelToMainFrameController.createRightPanel();

            RightPanel createdRp = rpConstructor.constructed().get(0);

            // Assert
            verify(createdRp).openSearchPanel();
            verify(createdRp, never()).openFavouritePanel();
        }
    }

    @Test
    @DisplayName("Edge Case: Sostituzione pannello esistente (Rimuove il vecchio)")
    void testCreateRightPanel_RemovesOldPanel() throws Exception {
        // Setup: Iniettiamo un vecchio pannello mockato nel campo statico 'rp'
        RightPanel oldRp = mock(RightPanel.class);
        setStaticField("rp", oldRp);

        try (MockedStatic<LoginToMainFrame> loginMock = mockStatic(LoginToMainFrame.class);
             MockedConstruction<RightPanel> rpConstructor = mockConstruction(RightPanel.class)) {
            
            loginMock.when(LoginToMainFrame::isLogged).thenReturn(false);

            // Act
            RightPanelToMainFrameController.createRightPanel();

            // Assert
            // Deve aver rimosso il vecchio pannello PRIMA di aggiungere il nuovo
            verify(mainPanelMock).remove(oldRp);
            verify(mainPanelMock).add(any(RightPanel.class), eq(BorderLayout.EAST));
        }
    }

    // --- TEST SWITCH PANEL ---

    @Test
    @DisplayName("Switch: Da SearchPanel -> A FavouritePanel")
    void testSwitch_FromSearch_ToFavourite() throws Exception {
        // Arrange
        RightPanel mockRp = mock(RightPanel.class);
        // Simuliamo che il pannello corrente sia SearchPanel
        when(mockRp.getCurrent()).thenReturn(new SearchPanel()); 
        
        // Iniettiamo il mock nel controller
        setStaticField("rp", mockRp);

        // Act
        RightPanelToMainFrameController.switchCurrentRightPanel();

        // Assert
        verify(mockRp).openFavouritePanel();
        verify(jFrameMock).repaint(); // Verifica refresh
    }

    @Test
    @DisplayName("Switch: Da FocusPanel -> A FavouritePanel")
    void testSwitch_FromFocus_ToFavourite() throws Exception {
        // Arrange
        RightPanel mockRp = mock(RightPanel.class);
        // FocusPanel è trattato come SearchPanel nella logica if
        when(mockRp.getCurrent()).thenReturn(new FocusPanel()); 
        
        setStaticField("rp", mockRp);

        // Act
        RightPanelToMainFrameController.switchCurrentRightPanel();

        // Assert
        verify(mockRp).openFavouritePanel();
    }

    @Test
    @DisplayName("Switch: Da Altro (es. Favourite) -> A SearchPanel")
    void testSwitch_FromFavourite_ToSearch() throws Exception {
        // Arrange
        RightPanel mockRp = mock(RightPanel.class);
        // Simuliamo un pannello generico (o FavouritePanel se esistesse come classe distinta)
        when(mockRp.getCurrent()).thenReturn(new JPanel()); 
        
        setStaticField("rp", mockRp);

        // Act
        RightPanelToMainFrameController.switchCurrentRightPanel();

        // Assert
        verify(mockRp).openSearchPanel();
    }
    
    @Test
    @DisplayName("Edge Case: MainFrame non settato (NullPointerException)")
    void testMissingFrame() {
        // Resettiamo il frame a null per simulare l'errore
        RightPanelToMainFrameController.setFrame(null);
        
        // Se il frame non è settato, ci aspettiamo che il codice lanci NPE
        assertThrows(NullPointerException.class, () -> {
            RightPanelToMainFrameController.createRightPanel();
        });
    }

    // --- HELPER METODI PER REFLECTION ---
    
    // Serve per modificare i campi "private static" che non hanno setter
    private void setStaticField(String fieldName, Object value) throws Exception {
        Field field = RightPanelToMainFrameController.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(null, value);
    }
    
    // Serve per resettare i campi a null nel tearDown
    private void resetStaticField(String fieldName) throws Exception {
        setStaticField(fieldName, null);
    }
}
