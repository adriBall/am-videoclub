package ui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import dominio.Credenciales;
import dominio.Socio;
import excepciones.ExcepcionAutenticacionIncorrecta;
import excepciones.ExcepcionCamposNoValidos;
import excepciones.ExcepcionCamposNulos;
import excepciones.ExcepcionNoPermitido;
import excepciones.ExcepcionSocioYaExistente;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import modelo.IVideoclubSocio;
import ui.custom.Toast;;

public class CambiarUsernameController extends SocioBarController implements Initializable {
	public final static String VIEW = "view/cambiarUsernameSocio.fxml";
	private static Socio socio;
	@FXML TextField campoUsername;
	@FXML PasswordField campoContrasenya;
	@FXML Text error;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Platform.runLater(() -> campoUsername.requestFocus());
	}
	
	@FXML public void goBack(MouseEvent e) {
		goBack();
	}
	
	@FXML public void keySubmit(KeyEvent e) throws IOException {
		if(e.getCode().equals(KeyCode.ENTER))
			aceptar();
	}
	
	private static void goBack() {
		Main.navigateTo(AreaSocioController.VIEW);
	}
	
	@FXML public void aceptar(MouseEvent e) throws IOException {
		aceptar();
	}
	
	private void aceptar() {
		Credenciales nuevos = new Credenciales();
		nuevos.setUsuario(campoUsername.getText().trim());
		nuevos.setContrasenya(socio.getCredenciales().getContrasenya());
		nuevos.setAdmin(false);
		Socio socioNuevo = new Socio();
		socioNuevo.setIdSocio(socio.getIdSocio());
		socioNuevo.setNombreSocio(socio.getNombreSocio());
		socioNuevo.setDireccion(socio.getDireccion());
		socioNuevo.setTelefono(socio.getTelefono());
		socioNuevo.setCorreo(socio.getCorreo());
		socioNuevo.setUltimoPago(socio.getUltimoPago());
		socioNuevo.setCredenciales(nuevos);
		if(campoContrasenya.getText().trim().isEmpty()) {
			error.setText("Debes introducir tu contraseña");
			error.setVisible(true);
		}	
		else if(!socio.getCredenciales().getContrasenya().equals(campoContrasenya.getText().trim())) {
			error.setText("La contraseña no es correcta");
			error.setVisible(true);
		}
		else {
			try {
				((IVideoclubSocio) videoclub).actualizarMisDatos(socioNuevo);
				videoclub.iniciarSesion(nuevos.getUsuario(), nuevos.getContrasenya());
				Toast.makeText(Main.getPrimaryStage(), "Username cambiado", 1500, 500, 500);
				goBack();
			} catch (ExcepcionCamposNoValidos e1) {
				error.setText("Hay campos no válidos");
				error.setVisible(true);
			} catch (ExcepcionCamposNulos e1) {
				error.setText("Hay nulos");
				error.setVisible(true);
			} catch (ExcepcionNoPermitido e1) {
				error.setText("No se permite modificar la fecha de último pago");
				error.setVisible(true);
			} catch (ExcepcionAutenticacionIncorrecta e1) {
				e1.printStackTrace();
			} catch(ExcepcionSocioYaExistente e1) {
				error.setText("El username no está disponible");
				error.setVisible(true);
			}
		}
	}
	
	public static void setSocio(Socio socio) {
		CambiarUsernameController.socio = socio;
	}
	
}
