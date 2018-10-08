package ui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import com.jfoenix.controls.JFXListView;

import dominio.Socio;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import modelo.IVideoclubAdmin;
import ui.custom.LabelSocio;
import ui.custom.Toast;

public class AreaAdminController extends AdminBarController implements Initializable {
	public final static String VIEW = "view/areaDeAdmin.fxml";
	private volatile Timer timer;
	@FXML private JFXListView<Label> listaSociosMorosos;
	@FXML private Label morososComentario;
	@FXML private Label welcomeMessage;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		new Thread(() -> cargarMorosos()).start();
		Platform.runLater(()-> welcomeMessage.setText("Bienvenid@ "+videoclub.getUsuarioSesion()));
	}
	
	@FXML public void buttonCheck(MouseEvent e) {
		new Thread(() -> ((IVideoclubAdmin) videoclub).chequearAntiguedadAlquileres()).start();
		Toast.makeText(Main.getPrimaryStage(), "Alquileres caducados eliminados", 1500, 500, 500);
	}
	
	@FXML public void buttonAltaSocio(MouseEvent e) {
		AnyadirSocioController.setBackView(VIEW);
		Main.navigateTo(AnyadirSocioController.VIEW);
	}

	@FXML public void buttonAltaPeli(MouseEvent e) {
		AnyadirPeliculaController.setBackView(VIEW);
		Main.navigateTo(AnyadirPeliculaController.VIEW);
	}
	
	private void cargarMorosos() {
		List<Socio> listaMorosos = ((IVideoclubAdmin) videoclub).listarUsuariosMorosos();
		if(listaMorosos.isEmpty())
			Platform.runLater(() -> morososComentario.setText("No hay socios morosos"));
		Platform.runLater(() -> {
			for(Socio socio:listaMorosos) {
				LabelSocio labelSocio = new LabelSocio(socio);
				setLabelSocioHandlers(labelSocio);
				listaSociosMorosos.getItems().add(labelSocio);
			}
		});
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
							Alert alert = new Alert(AlertType.CONFIRMATION, "¿Dar de baja a "+labelSocio.getNombreSocio()+"?", ButtonType.YES, ButtonType.NO);
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
