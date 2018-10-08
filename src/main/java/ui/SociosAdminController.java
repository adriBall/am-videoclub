package ui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import com.jfoenix.controls.JFXListView;

import dominio.Socio;
import excepciones.ExcepcionNoExistenSocios;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import modelo.IVideoclubAdmin;
import ui.custom.LabelSocio;

/*
 * Controlador de la vista sociosAdmin. Extiende a AdminBarController y además inicializa
 * la lista de socios obteniendolos del videoclub.
 */
public class SociosAdminController extends AdminBarController implements Initializable {
	public final static String VIEW = "view/sociosAdmin.fxml";
	private volatile Timer timer;
	@FXML private JFXListView<Label> listaSocios;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		new Thread(() -> cargarSocios()).start();
	}
	
	public void buttonAltaSocio(MouseEvent e) {
		AnyadirSocioController.setBackView(VIEW);
		Main.navigateTo(AnyadirSocioController.VIEW);
	}
	
	private void cargarSocios() {
		try {
			List<Socio> socios = ((IVideoclubAdmin) videoclub).listarSocios();
			Platform.runLater(() -> {
				for(Socio socio:socios) {
					LabelSocio labelSocio = new LabelSocio(socio);
					setLabelSocioHandlers(labelSocio);
					listaSocios.getItems().add(labelSocio);
				}
			});
		} catch (ExcepcionNoExistenSocios e) {}
	}
	
	public void socioSeleccionadoHandler(MouseEvent event) {
		LabelSocio source = (LabelSocio) event.getSource();
		Socio socio = ((IVideoclubAdmin) videoclub).verDatosSocio(source.getSocio().getIdSocio());
		ConsultarSocioAdminController.setSocio(socio);
		ConsultarSocioAdminController.setBackView(VIEW);
		Main.navigateTo(ConsultarSocioAdminController.VIEW);
	}
	
	private void setLabelSocioHandlers(LabelSocio labelSocio) {
		labelSocio.addEventHandler(MouseEvent.MOUSE_CLICKED, this::socioSeleccionadoHandler);
		labelSocio.setOnMousePressed((event) -> {
			timer = new Timer(true);
			timer.schedule(new TimerTask() {
				
				@Override
				public void run() {
						Platform.runLater(() -> {
							Alert alert = new Alert(AlertType.CONFIRMATION, "¿Dar de baja a "+labelSocio.getUsuario()+"?", ButtonType.YES, ButtonType.NO);
							Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
							stage.getIcons().add(
							    new Image("resources/logo.png"));
							alert.showAndWait();		
							if (alert.getResult() == ButtonType.YES) {
								try {
									((IVideoclubAdmin) videoclub).darBajaSocio(labelSocio.getSocio());
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							Main.navigateTo(VIEW);
						});
				}
			}, 1000L);
		});
		labelSocio.setOnMouseReleased((event) -> timer.cancel());
	}
	
}
