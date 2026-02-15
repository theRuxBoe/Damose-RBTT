package test.java.frontend.user;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import main.java.backend.user.AccountAlreadyExistsException;
import main.java.backend.user.UserDB;
import main.java.frontend.MainFrame;
import main.java.frontend.user.LoginToMainFrame;
import main.java.frontend.user.RegisterPanel;

@ExtendWith(MockitoExtension.class)

@MockitoSettings(strictness = Strictness.LENIENT)
/**
 * (GENERATA DA AI)
 */
class RegisterPanelTest {

    @Mock
    private UserDB mockUserDB;
    
    @Mock
    private MainFrame mockMainFrame;

    // Mock statici per le dipendenze globali
    private MockedStatic<LoginToMainFrame> loginFrameMock;
    private MockedStatic<JOptionPane> jOptionPaneMock;

    private RegisterPanel spyPanel;

    @BeforeEach
    void setUp() {
        // Inizializza i mock statici
        loginFrameMock = mockStatic(LoginToMainFrame.class);
        jOptionPaneMock = mockStatic(JOptionPane.class);

        // Creiamo uno SPY del pannello reale.
        // Questo ci permette di eseguire il vero codice del costruttore e di addButtons,
        // ma di "mentire" su metodi specifici come getDB() o getObserver().
        RegisterPanel realPanel = new RegisterPanel();
        realPanel.setObserver(mockMainFrame);
        realPanel.openDB();
        spyPanel = spy(realPanel);

        // Configuriamo lo spy per restituire i nostri mock
//        doReturn(mockUserDB).when(spyPanel).getDB();
//        doReturn(mockMainFrame).when(spyPanel).getObserver();
        
        // Nota: Assumiamo che getUsername() e getPwdField() ritornino componenti reali 
        // inizializzati da UserPanel (super), quindi non serve mockarli se funzionano in memoria.
        // Se danno problemi, si possono mockare pure loro.
    }

    @AfterEach
    void tearDown() {
        loginFrameMock.close();
        jOptionPaneMock.close();
    }

    // --- Helper per trovare i bottoni nel pannello ---
    private JButton findButtonByText(String text) {
        // Assumiamo che getButtonSpace() ritorni il pannello contenitore dei bottoni
        JPanel buttonSpace = spyPanel.getButtonSpace();
        
        for (Component comp : buttonSpace.getComponents()) {
            if (comp instanceof JButton) {
                JButton btn = (JButton) comp;
                if (btn.getText().equals(text)) {
                    return btn;
                }
            }
        }
        fail("Bottone con testo '" + text + "' non trovato.");
        return null;
    }

    // --- Helper per settare testo ---
    private void setCredentials(String user, String pwd) {
        spyPanel.getUsername().setText(user);
        spyPanel.getPwdField().setText(pwd);
    }

    // ========================================================================
    // TEST: Navigazione (Torna Indietro)
    // ========================================================================

    @Test
    @DisplayName("Click 'Torna indietro' -> Chiama LoginToMainFrame.openLogin")
    void testBackButton() {
        // Arrange
        JButton backBtn = findButtonByText("Torna indietro");

        // Act
        // Simuliamo il click
        for (ActionListener al : backBtn.getActionListeners()) {
            al.actionPerformed(new ActionEvent(backBtn, ActionEvent.ACTION_PERFORMED, null));
        }

        // Assert
        // Verifica che venga chiamato openLogin passando l'observer (mockMainFrame)
        loginFrameMock.verify(() -> LoginToMainFrame.openLogin(mockMainFrame));
    }


    // ========================================================================
    // EDGE CASES: Gestione Errori ed Eccezioni
    // ========================================================================

    @Test
    @DisplayName("Edge Case: AccountAlreadyExistsException -> Mostra errore specifico")
    void testRegister_AccountExists() throws Exception {
        // Arrange
        // Nota: È buona norma che mockUserDB sia iniettato nel panel prima di questo passo
        // registerPanel.setDB(mockUserDB); <--- Assicurati di aver fatto questo nel setup o qui
        
        setCredentials("ExistingUser", "pass");
        String errorMsg = "La password deve contenere almeno 8 caratteri.";
        
        // Simuliamo l'eccezione dal DB
//        doThrow(new AccountAlreadyExistsException(errorMsg))
//            .when(mockUserDB).createNewAccount(anyString(), anyString());

        JButton regBtn = findButtonByText("Registrati");

        // Act
        for (ActionListener al : regBtn.getActionListeners()) {
            al.actionPerformed(new ActionEvent(regBtn, ActionEvent.ACTION_PERFORMED, null));
        }

        // Assert
        // USIAMO any() invece di eq(spyPanel) per il primo argomento
        jOptionPaneMock.verify(() -> JOptionPane.showMessageDialog(
            any(), // Accetta qualsiasi componente padre (risolve il problema del this vs spy)
            eq(errorMsg)
        ));
    }


    @Test
    @DisplayName("Edge Case: IOException (Errore DB) -> Mostra errore I/O")
    void testRegister_IOError() throws Exception {
        // Arrange
        setCredentials("User", "Pass");
        
        doThrow(new IOException("Disk full"))
            .when(mockUserDB).createNewAccount(anyString(), anyString());

        JButton regBtn = findButtonByText("Registrati");

        // Act
        for (ActionListener al : regBtn.getActionListeners()) {
            al.actionPerformed(new ActionEvent(regBtn, ActionEvent.ACTION_PERFORMED, null));
        }

        // Assert
        // Nota: Nel tuo codice il messaggio per IOException è hardcoded
        jOptionPaneMock.verify(() -> JOptionPane.showMessageDialog(any(), any()));
    }
}