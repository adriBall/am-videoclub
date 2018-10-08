package ui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import dominio.Credenciales;
import dominio.Socio;
import excepciones.ExcepcionCamposNoValidos;
import excepciones.ExcepcionCamposNulos;
import excepciones.ExcepcionSocioYaExistente;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import modelo.IVideoclubAdmin;
import ui.custom.Toast;;

public class AnyadirSocioController extends AdminBarController implements Initializable {
	public final static String VIEW ="view/anyadirSocio.fxml";
	private static String backView;
	@FXML TextField dniField;
	@FXML TextField nameField;
	@FXML TextField dirField;
	@FXML TextField tlfField;
	@FXML TextField emailField;
	@FXML TextField usernameField;
	@FXML TextField pwdField;
	@FXML Text error;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Platform.runLater(() -> dniField.requestFocus());
	}
	
	@FXML public void keySubmit(KeyEvent e) throws IOException {
		if(e.getCode().equals(KeyCode.ENTER))
			add();
	}
	
	public void add(MouseEvent event) {
		add();
	}
	
	@FXML public void goBack(MouseEvent e) {
		goBack();
	}
	
	private void add() {
		Socio nuevo = new Socio();
		nuevo.setIdSocio(dniField.getText().trim());
		nuevo.setNombreSocio(nameField.getText().trim());
		nuevo.setCorreo(emailField.getText().trim());
		try {
			nuevo.setTelefono(Integer.parseInt(tlfField.getText().trim()));
		} catch (NumberFormatException e) {
			if(tlfField.getText().trim().isEmpty()) {
				error.setText("Debes introducir todos los campos");
				error.setVisible(true);
			}
			else {
				error.setText("El teléfono debe ser numérico");
				error.setVisible(true);
			}
			return;
		}
		nuevo.setDireccion(dirField.getText().trim());
		Credenciales nuevos = new Credenciales();
		nuevos.setAdmin(false);
		nuevos.setUsuario(usernameField.getText().trim());
		nuevos.setContrasenya(pwdField.getText().trim());
		nuevo.setCredenciales(nuevos);
		try {
			((IVideoclubAdmin) videoclub).darAltaSocio(nuevo);
			goBack();
			Toast.makeText(Main.getPrimaryStage(), "Socio añadido", 1500, 500, 500);
		} catch (ExcepcionSocioYaExistente e) {
			error.setVisible(true);
			error.setText("El socio ya está dado de alta");
		} catch (ExcepcionCamposNoValidos e) {
			error.setVisible(true);
			error.setText("Hay campos no válidos");
		} catch (ExcepcionCamposNulos e) {
			error.setVisible(true);
			error.setText("Hay campos nulos");
		} 
	}
	
	private static void goBack() {
		Main.navigateTo(backView);
	}
	
	public static void setBackView(String backView) {
		AnyadirSocioController.backView = backView;
	}

}
