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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import modelo.IVideoclubSocio;
import ui.custom.Toast;;

public class CambiarContrasenyaController extends SocioBarController implements Initializable {
	public final static String VIEW = "view/cambiarContrasenyaSocio.fxml";
	private static Socio socio;
	@FXML PasswordField campoActual;
	@FXML PasswordField campoNueva;
	@FXML PasswordField campoNuevaRepetida;
	@FXML Text error;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Platform.runLater(() -> campoActual.requestFocus());
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
		nuevos.setUsuario(socio.getCredenciales().getUsuario());
		nuevos.setAdmin(false);
		Socio socioNuevo = new Socio();
		socioNuevo.setIdSocio(socio.getIdSocio());
		socioNuevo.setNombreSocio(socio.getNombreSocio());
		socioNuevo.setDireccion(socio.getDireccion());
		socioNuevo.setTelefono(socio.getTelefono());
		socioNuevo.setCorreo(socio.getCorreo());
		socioNuevo.setUltimoPago(socio.getUltimoPago());
		if(campoActual.getText().trim().isEmpty()) {
			error.setText("Debes introducir tu contraseña");
			error.setVisible(true);
		}	
		else if(!campoNueva.getText().trim().equals(campoNuevaRepetida.getText())) {
			error.setText("Las contraseñas no coinciden");
			error.setVisible(true);
		}
		else if(!socio.getCredenciales().getContrasenya().equals(campoActual.getText().trim())) {
			error.setText("La contraseña no es correcta");
			error.setVisible(true);
		}
		else {
			nuevos.setContrasenya(campoNueva.getText().trim());
			socioNuevo.setCredenciales(nuevos);
			try {
				((IVideoclubSocio) videoclub).actualizarMisDatos(socioNuevo);
				videoclub.iniciarSesion(nuevos.getUsuario(), nuevos.getContrasenya());
				Toast.makeText(Main.getPrimaryStage(), "Contraseña cambiada", 1500, 500, 500);
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
			} catch(ExcepcionSocioYaExistente e1) {}
		}
	}
	
	public static void setSocio(Socio socio) {
		CambiarContrasenyaController.socio = socio;
	}
	
}
