package ui;

import java.io.IOException;

import excepciones.ExcepcionAutenticacionIncorrecta;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import modelo.IVideoclubComun;
import modelo.VideoclubAdmin;
import modelo.VideoclubComun;
import modelo.VideoclubSocio;

/*
 * Controlador de la vista loginScreen. Si los credenciales son correctos pasa
 * al área de socio o de admin según corresponda.
 */
public class LoginController {
	public final static String VIEW = "view/loginScreen.fxml";
	private static IVideoclubComun videoclub;
	@FXML private TextField campoNombre;
	@FXML private PasswordField campoContrasenya;
	@FXML private Text error;

	@FXML public void buttonSubmit(MouseEvent e) throws IOException {
		submit(e);
	}
	
	@FXML public void keySubmit(KeyEvent e) throws IOException {
		if(e.getCode().equals(KeyCode.ENTER))
			submit(e);
	}
	
	private void submit(Event e) {
		try {
			videoclub.iniciarSesion(campoNombre.getText(), campoContrasenya.getText());
			if(videoclub.esSocio()) {
				BarController.setVideoclub(new VideoclubSocio((VideoclubComun)videoclub));
				Main.navigateTo(AreaSocioController.VIEW);
			}
			else {
				BarController.setVideoclub(new VideoclubAdmin((VideoclubComun)videoclub));
				Main.navigateTo(AreaAdminController.VIEW);
			}
		} catch (ExcepcionAutenticacionIncorrecta e1) {
			if(campoNombre.getText().equals("")) {
				error.setText("Debes introducir un nombre de usuario");
				error.setVisible(true);
			}
			else if(campoContrasenya.getText().equals("")) {
				error.setText("Debes introducir una contraseña");
				error.setVisible(true);
			} else if(!campoNombre.getText().equals("") && !campoContrasenya.getText().equals("")) {
				error.setText("Autenticación incorrecta");
				error.setVisible(true);
			}
			else
				error.setVisible(false);
		}
	}
	
	public static void setVideoclub(IVideoclubComun videoclub) {
		LoginController.videoclub = videoclub;
	}

}
