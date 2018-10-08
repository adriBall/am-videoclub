package ui;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import dominio.Credenciales;
import dominio.Socio;
import excepciones.ExcepcionCamposNoValidos;
import excepciones.ExcepcionCamposNulos;
import excepciones.ExcepcionSocioNoExiste;
import excepciones.ExcepcionSocioYaExistente;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import modelo.IVideoclubAdmin;

public class ConsultarSocioAdminController extends AdminBarController implements Initializable {
	public final static String VIEW = "view/consultarYEditarSocioAdmin.fxml";
	private static String backView;
	private static Socio socio;
	@FXML Label titleField;
	@FXML TextField dniField;
	@FXML TextField nameField;
	@FXML TextField dirField;
	@FXML TextField tlfField;
	@FXML TextField emailField;
	@FXML TextField usernameField;
	@FXML TextField pwdField;
	@FXML Label payField;
	@FXML HBox buttonsPanel;
	@FXML JFXButton editButton;
	@FXML Text error;
	@FXML BorderPane pane;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Platform.runLater(() -> {
			titleField.setText("Datos del socio "+socio.getCredenciales().getUsuario());
			dniField.setText(socio.getIdSocio());
			nameField.setText(socio.getNombreSocio());
			dirField.setText(socio.getDireccion());
			tlfField.setText(""+socio.getTelefono());
			emailField.setText(socio.getCorreo());
			usernameField.setText(socio.getCredenciales().getUsuario());
			pwdField.setText(socio.getCredenciales().getContrasenya());
			String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(socio.getUltimoPago());
			String pago = "Último pago "+formattedDate;
			if(socio.esMoroso())
				pago+=" Debe "+socio.mensualidadesQueDebe()+" mensualidades";
			payField.setText(pago);
		});
	}
	
	@FXML public void keySubmit(KeyEvent e) {
		if(e.getCode().equals(KeyCode.ENTER))
			save();
	}
	
	@FXML public void pagarSocio(MouseEvent e) {
		try {
			((IVideoclubAdmin) videoclub).pagarMensualidadSocio(socio);
			socio = ((IVideoclubAdmin) videoclub).verDatosSocio(socio.getIdSocio());
			recargarVista();
		} catch (ExcepcionSocioNoExiste e1) {
			e1.printStackTrace();
		}
	}
	
	@FXML public void editarDatos(MouseEvent e) {
		Platform.runLater(() -> {
			TextField defaultField = new TextField();
			dniField.setEditable(true);
			dniField.setStyle(defaultField.getStyle());
			nameField.setEditable(true);
			nameField.setStyle(defaultField.getStyle());
			dirField.setEditable(true);
			dirField.setStyle(defaultField.getStyle());
			tlfField.setEditable(true);
			tlfField.setStyle(defaultField.getStyle());
			emailField.setEditable(true);
			emailField.setStyle(defaultField.getStyle());
			usernameField.setEditable(true);
			usernameField.setStyle(defaultField.getStyle());
			pwdField.setEditable(true);
			pwdField.setStyle(defaultField.getStyle());
			buttonsPanel.getChildren().clear();
			
			JFXButton save = new JFXButton("Guardar cambios");
			save.addEventHandler(MouseEvent.MOUSE_CLICKED, this::saveHandler);
			save.setPrefHeight(editButton.getPrefHeight());
			save.setStyle(editButton.getStyle()+";-fx-font-size: 14");
			JFXButton cancel = new JFXButton("Cancelar");
			cancel.addEventHandler(MouseEvent.MOUSE_CLICKED, this::recargarVista);
			cancel.setPrefHeight(editButton.getPrefHeight());
			cancel.setStyle(editButton.getStyle()+";-fx-font-size: 14");
			buttonsPanel.getChildren().add(save);
			buttonsPanel.getChildren().add(cancel);
			
			dniField.requestFocus();
			pane.setOnKeyPressed(this::keySubmit);
		});
	}
	
	@FXML public void eliminarSocio(MouseEvent e) {
		Alert alert = new Alert(AlertType.CONFIRMATION, "¿Dar de baja a "+socio.getCredenciales().getUsuario()+"?", ButtonType.YES, ButtonType.NO);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(
		    new Image("resources/logo.png"));
		alert.showAndWait();		
		if (alert.getResult() == ButtonType.YES) {
			try {
				((IVideoclubAdmin) videoclub).darBajaSocio(socio);
				goBack();
			} catch (ExcepcionSocioNoExiste e1) {
				e1.printStackTrace();
			}
		}
		
	}
	
	public void saveHandler(MouseEvent event) {
		save();
	}
	
	private void save() {
		Socio nuevosDatos = new Socio();
		nuevosDatos.setIdSocio(dniField.getText().trim());
		nuevosDatos.setNombreSocio(nameField.getText().trim());
		nuevosDatos.setCorreo(emailField.getText().trim());
		try {
			nuevosDatos.setTelefono(Integer.parseInt(tlfField.getText().trim()));
		} catch (NumberFormatException e) {
			error.setText("El teléfono debe ser numérico");
			error.setVisible(true);
			return;
		}
		Credenciales nuevosCredenciales =new Credenciales();
		nuevosCredenciales.setUsuario(usernameField.getText().trim());
		nuevosCredenciales.setContrasenya(pwdField.getText().trim());
		nuevosCredenciales.setAdmin(false);
		nuevosDatos.setCredenciales(nuevosCredenciales);
		nuevosDatos.setDireccion(dirField.getText().trim());
		nuevosDatos.setUltimoPago(socio.getUltimoPago());
		try {
			((IVideoclubAdmin) videoclub).actualizarDatosSocio(socio.getCredenciales(), nuevosDatos);
			socio = nuevosDatos;
			recargarVista();
		} catch (ExcepcionCamposNoValidos e) {
			error.setText("Hay campos no válidos");
			error.setVisible(true);
		} catch (ExcepcionCamposNulos e) {
			error.setText("Hay campos nulos");
			error.setVisible(true);
		} catch (ExcepcionSocioYaExistente e) {
			error.setText("El username no está disponible");
			error.setVisible(true);
		}
	}
	
	public void recargarVista(MouseEvent e) {
		recargarVista();
	}
	
	public void recargarVista() {
		Main.navigateTo(VIEW);
	}
	
	@FXML public void goBack(MouseEvent e) throws IOException {
		goBack();
	}
	
	private static void goBack() {
		Main.navigateTo(backView);
	}
	
	public static void setSocio(Socio socio) {
		ConsultarSocioAdminController.socio = socio;
	}
	
	public static void setBackView(String backView) {
		ConsultarSocioAdminController.backView = backView;
	}

}
