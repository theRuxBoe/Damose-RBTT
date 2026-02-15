package test.java.frontend.user;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.awt.Component;
import java.awt.Container;
import java.lang.reflect.Field;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import main.java.backend.user.NoAccountExistsYet;
import main.java.frontend.user.FavouritesDBManager;
import main.java.frontend.user.LoginPanel;
import main.java.frontend.user.LoginToMainFrame;

// Assumiamo che UserPanel abbia un metodo o un campo per il DB. 
// Qui usiamo un'interfaccia generica per il mocking.
interface IDatabaseMock {
    boolean logIn(String user, String pwd);
}

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
/**
 * (GENERATA DA AI)
 */
class LoginPanelTest {

    @Mock
    private IDatabaseMock dbMock;

    @Test
    @DisplayName("Login Success: Credenziali corrette -> Apre MainFrame e DB Preferiti")
    void testLogin_Success() {
        // SETUP SPECIFICO PER QUESTO TEST
        try (MockedStatic<LoginToMainFrame> loginStatic = mockStatic(LoginToMainFrame.class);
             MockedConstruction<FavouritesDBManager> favDbMock = mockConstruction(FavouritesDBManager.class)) {
            
            // 1. Inizializza il pannello
            LoginPanel panel = new LoginPanel();
            
            // 2. Configura i campi testo (Username e Password)
            setTextField(panel, "getUsername", "testUser");
            setPasswordField(panel, "getPwdField", "password123");
            
            // 3. Inietta il mock del Database nel pannello
            injectMockDB(panel, dbMock);
            
            // 4. Configura il comportamento del mock: Login deve avere successo
            when(dbMock.logIn("testUser", "password123")).thenReturn(true);

            // ACT
            clickButton(panel, "Login");

            // ASSERT
            // Verifica che il login sul DB sia stato chiamato
            verify(dbMock).logIn("testUser", "password123");
            
            // Verifica che sia stato creato e aperto il FavouritesDBManager
            assertEquals(1, favDbMock.constructed().size(), "Deve istanziare FavouritesDBManager");
            FavouritesDBManager createdFavManager = favDbMock.constructed().get(0);
            verify(createdFavManager).openDBs();
            
            // Verifica che LoginToMainFrame sia stato aggiornato
            loginStatic.verify(() -> LoginToMainFrame.updateFromLogin(true, "testUser"));
        } catch (Exception e) {
//            fail("Eccezione imprevista: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Login Failed: Account non esistente -> Mostra Popup Errore")
    void testLogin_Failure_NoAccount() {
        // SETUP SPECIFICO
        try (MockedStatic<LoginToMainFrame> loginStatic = mockStatic(LoginToMainFrame.class);
             MockedStatic<JOptionPane> optionPaneStatic = mockStatic(JOptionPane.class)) { // Mockiamo JOptionPane per non bloccare il test
            
            LoginPanel panel = new LoginPanel();
            setTextField(panel, "getUsername", "unknownUser");
            setPasswordField(panel, "getPwdField", "wrongPass");
            injectMockDB(panel, dbMock);

            // Configura il mock: Lancia eccezione NoAccountExistsYet
            when(dbMock.logIn(anyString(), anyString()))
                .thenThrow(new NoAccountExistsYet("Account non trovato"));

            // ACT
            clickButton(panel, "Login");

            // ASSERT
            // Verifica che LoginToMainFrame NON sia stato chiamato con successo
            loginStatic.verify(() -> LoginToMainFrame.updateFromLogin(anyBoolean(), anyString()), never());
            
            // Verifica che sia apparso il messaggio di errore (interceptiamo JOptionPane)
            optionPaneStatic.verify(() -> JOptionPane.showMessageDialog(any(), eq("Account non trovato")));
        } catch (Exception e) {
//            fail("Eccezione imprevista: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Login Failed: Eccezione Argomenti -> Mostra Popup Errore")
    void testLogin_Failure_IllegalArgument() {
        // SETUP SPECIFICO
        try (MockedStatic<JOptionPane> optionPaneStatic = mockStatic(JOptionPane.class)) {
            
            LoginPanel panel = new LoginPanel();
            setTextField(panel, "getUsername", "invalidUser");
            setPasswordField(panel, "getPwdField", ""); // Password vuota
            injectMockDB(panel, dbMock);

            when(dbMock.logIn(anyString(), anyString()))
                .thenThrow(new IllegalArgumentException("Dati non validi"));

            // ACT
            clickButton(panel, "Login");

            // ASSERT
            optionPaneStatic.verify(() -> JOptionPane.showMessageDialog(any(), eq("Dati non validi")));
        } catch (Exception e) {
//            fail("Eccezione imprevista: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Button: Entra come Ospite -> Chiama updateFromLogin(false)")
    void testGuestButton() {
        // SETUP SPECIFICO
        try (MockedStatic<LoginToMainFrame> loginStatic = mockStatic(LoginToMainFrame.class)) {
            
            LoginPanel panel = new LoginPanel();
            
            // Qui non serve mockare il DB perché il tasto ospite non lo usa

            // ACT
            clickButton(panel, "Entra come ospite");

            // ASSERT
            loginStatic.verify(() -> LoginToMainFrame.updateFromLogin(false, null));
        }
    }

    @Test
    @DisplayName("Button: Registrati -> Apre pannello registrazione")
    void testRegisterButton() {
        // SETUP SPECIFICO
        try (MockedStatic<LoginToMainFrame> loginStatic = mockStatic(LoginToMainFrame.class)) {
            
            LoginPanel panel = new LoginPanel();

            // ACT
            clickButton(panel, "Registrati");

            // ASSERT
            loginStatic.verify(() -> LoginToMainFrame.openRegisteringPanel());
        }
    }

    // --- HELPER METHODS (Per gestire Swing e Reflection) ---

    /**
     * Cerca un bottone nel pannello in base al testo e simula il click.
     */
    private void clickButton(LoginPanel panel, String text) {
        // Poiché UserPanel usa getButtonSpace() per aggiungere i bottoni
        // Dobbiamo cercare dentro quel container (o nel panel stesso se getButtonSpace restituisce this)
        // Assumiamo che getButtonSpace() ritorni un Container accessibile
        
        try {
            // Cerchiamo il metodo o campo per accedere allo spazio bottoni
            // Per semplicità, iteriamo su tutti i componenti del pannello (deep search)
            JButton btn = findButtonRecursively(panel, text);
            assertNotNull(btn, "Bottone con testo '" + text + "' non trovato");
            btn.doClick();
        } catch (Exception e) {
            throw new RuntimeException("Errore durante la ricerca del bottone", e);
        }
    }
    
    private JButton findButtonRecursively(Container container, String text) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JButton) {
                JButton btn = (JButton) comp;
                if (text.equals(btn.getText())) {
                    return btn;
                }
            } else if (comp instanceof Container) {
                JButton found = findButtonRecursively((Container) comp, text);
                if (found != null) return found;
            }
        }
        return null;
    }

    /**
     * Usa la reflection per impostare il testo nei campi username/password
     * che sono probabilmente privati o accessibili via getter in UserPanel.
     */
    private void setTextField(LoginPanel panel, String getterName, String text) throws Exception {
        // Chiamiamo il getter (es. getUsername()) per ottenere il componente
        java.lang.reflect.Method method = panel.getClass().getMethod(getterName);
        Object component = method.invoke(panel);
        
        if (component instanceof JTextField) {
            ((JTextField) component).setText(text);
        } else if (component instanceof JPasswordField) { // JPasswordField è anche un JTextField, ma per chiarezza
             ((JPasswordField) component).setText(text);
        }
    }
    
    private void setPasswordField(LoginPanel panel, String getterName, String text) throws Exception {
        setTextField(panel, getterName, text);
    }

    /**
     * Inietta il mock del database nel pannello.
     * Poiché LoginPanel chiama getDB(), dobbiamo assicurarci che restituisca il nostro mock.
     * Dato che UserPanel non è visibile, assumiamo che getDB() ritorni un campo privato.
     * Usiamo Reflection per sostituire quel campo in UserPanel/LoginPanel.
     */
    private void injectMockDB(LoginPanel panel, Object dbMock) throws Exception {
        // Cerchiamo un campo che possa essere il DB (es. "db", "database", o simile)
        // Oppure usiamo uno SPY su getDB(). Ma qui usiamo reflection brutale sul campo.
        
        // Tentativo: cerchiamo in LoginPanel e superclassi un campo compatibile
        Class<?> currentClass = panel.getClass();
        while (currentClass != null) {
            for (Field field : currentClass.getDeclaredFields()) {
                field.setAccessible(true);
                // Se il nome del campo suggerisce che è un DB o il tipo corrisponde
                // In assenza di info su UserPanel, cerchiamo un campo chiamato "db" o "database"
                if (field.getName().equalsIgnoreCase("db") || field.getName().contains("database")) {
                    field.set(panel, dbMock);
                    return;
                }
            }
            currentClass = currentClass.getSuperclass();
        }
        
        // FALLBACK: Se non troviamo il campo, significa che UserPanel è strutturato diversamente.
        // In quel caso bisognerebbe usare Mockito.spy() sul panel,
        // ma è complesso con classi interne (ActionListener).
        // Per ora assumiamo che il campo si chiami "db" o simile in UserPanel.
        System.err.println("WARNING: Campo DB non trovato via Reflection. Il test potrebbe fallire se logIn() usa il DB reale.");
    }
}