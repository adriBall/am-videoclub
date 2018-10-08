package ui;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import dominio.Socio;
import excepciones.ExcepcionCamposNoValidos;
import excepciones.ExcepcionCamposNulos;
import excepciones.ExcepcionNoPermitido;
import excepciones.ExcepcionSocioYaExistente;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import modelo.IVideoclubSocio;

public class ConsultarPersonalesSocioController extends SocioBarController implements Initializable {
	public final static String VIEW = "view/consultarYEditarPersonalesSocio.fxml";
	private static Socio socio;
	@FXML TextField dniField;
	@FXML TextField nameField;
	@FXML TextField dirField;
	@FXML TextField tlfField;
	@FXML TextField emailField;
	@FXML Label payField;
	@FXML HBox buttonsPanel;
	@FXML JFXButton editButton;
	@FXML Text error;
	@FXML BorderPane pane;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Platform.runLater(() -> {
			dniField.setText(socio.getIdSocio());
			nameField.setText(socio.getNombreSocio());
			dirField.setText(socio.getDireccion());
			tlfField.setText(""+socio.getTelefono());
			emailField.setText(socio.getCorreo());
			String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(socio.getUltimoPago());
			String pago = "Último pago "+formattedDate;
			if(socio.esMoroso())
				pago+=" Debes "+socio.mensualidadesQueDebe()+" mensualidades";
			payField.setText(pago);
		});
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
			buttonsPanel.getChildren().clear();
			
			JFXButton save = new JFXButton("Guardar cambios");
			save.addEventHandler(MouseEvent.MOUSE_CLICKED, this::saveHandler);
			save.setPrefHeight(editButton.getPrefHeight());
			save.setStyle(editButton.getStyle()+";-fx-font-size: 14");
			JFXButton cancel = new JFXButton("Cancelar");
			cancel.addEventHandler(MouseEvent.MOUSE_CLICKED, (event) -> recargarVista());
			cancel.setPrefHeight(editButton.getPrefHeight());
			cancel.setStyle(editButton.getStyle()+";-fx-font-size: 14");
			buttonsPanel.getChildren().add(save);
			buttonsPanel.getChildren().add(cancel);
			
			dniField.requestFocus();
			pane.setOnKeyPressed(this::keySubmit);
		});
	}
	
	@FXML public void keySubmit(KeyEvent e) {
		if(e.getCode().equals(KeyCode.ENTER))
			guardar();
	}
	
	public void saveHandler(MouseEvent event) {
		guardar();
	}
	
	private void recargarVista() {
		Main.navigateTo(VIEW);
	}
	
	@FXML public void goBack(MouseEvent e) {
		Main.navigateTo(AreaSocioController.VIEW);
	}
	
	private void guardar() {
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
		nuevosDatos.setCredenciales(socio.getCredenciales());
		nuevosDatos.setDireccion(dirField.getText().trim());
		nuevosDatos.setUltimoPago(socio.getUltimoPago());
		try {
			((IVideoclubSocio) videoclub).actualizarMisDatos(nuevosDatos);
			socio = nuevosDatos;
			recargarVista();
		} catch (ExcepcionCamposNoValidos e) {
			error.setText("Hay campos no válidos");
			error.setVisible(true);
		} catch (ExcepcionCamposNulos e) {
			error.setText("Hay campos nulos");
			error.setVisible(true);
		} catch (ExcepcionNoPermitido e) {
			error.setText("No se permite modificar la fecha de último pago");
			error.setVisible(true);
		} catch (ExcepcionSocioYaExistente e) {
			error.setVisible(true);
			error.setText("El username no está disponible");
		}
	}
	
	public static void setSocio(Socio socio) {
		ConsultarPersonalesSocioController.socio = socio;
	}

}
