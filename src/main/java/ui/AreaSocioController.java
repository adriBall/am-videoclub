package ui;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import com.jfoenix.controls.JFXListView;

import dominio.PeliculaAlquilada;
import dominio.Socio;
import excepciones.ExcepcionNoHayPeliculasPendientes;
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
import modelo.IVideoclubSocio;
import ui.custom.LabelPeliculaAlquilada;;

public class AreaSocioController extends SocioBarController implements Initializable {
	public final static String VIEW = "view/areaDeSocio.fxml";
	private static Socio socio;
	private volatile Timer timer;
	@FXML private JFXListView<Label> listaPeliculasPendientes;
	@FXML private Label alquiladasComentario;
	@FXML private Label welcomeMessage;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		new Thread(() -> cargarPendientesYSocio()).start();
	}
	
	@FXML public void buttonMisDatos(MouseEvent e) {
		ConsultarPersonalesSocioController.setSocio(socio);
		Main.navigateTo(ConsultarPersonalesSocioController.VIEW);	
	}
	
	@FXML public void buttonCambiarContrasenya(MouseEvent e) {
		CambiarContrasenyaController.setSocio(socio);
		Main.navigateTo(CambiarContrasenyaController.VIEW);
	}

	@FXML public void buttonCambiarUsername(MouseEvent e) {
		CambiarUsernameController.setSocio(socio);
		Main.navigateTo(CambiarUsernameController.VIEW);
	}
	
	private void cargarPendientesYSocio() {
		socio = ((IVideoclubSocio) videoclub).verDatosPersonales();
		Platform.runLater(()-> {
			welcomeMessage.setText("Bienvenid@ "+socio.getCredenciales().getUsuario());
		});
		
		try {
			List<PeliculaAlquilada> alquiladas = ((IVideoclubSocio) videoclub).misPeliculasAlquiladas();
			Platform.runLater(() -> {
				for(PeliculaAlquilada alquilada:alquiladas) {
					LabelPeliculaAlquilada labelAlquilada = new LabelPeliculaAlquilada(alquilada);
					setLabelAlquiladaHandlers(labelAlquilada);
					listaPeliculasPendientes.getItems().add(labelAlquilada);
				}
			});
		} catch (ExcepcionNoHayPeliculasPendientes e) {
			Platform.runLater(() -> alquiladasComentario.setText("No tienes películas alquiladas"));
		}
	}
	
	public void alquiladaSeleccionadaHandler(MouseEvent event) {
		LabelPeliculaAlquilada source = (LabelPeliculaAlquilada) event.getSource();
		
		if(Desktop.isDesktopSupported()) {
			try {
				Desktop.getDesktop().browse(new URI(source.getEnlace()));
			} catch (IOException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
		else {
			Alert alert = new Alert(AlertType.INFORMATION, "No se ha podido abrir el navegador, "
					+ "aquí tienes el enlace a la película:\n\n"+source.getEnlace(), ButtonType.OK);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(
			    new Image("resources/logo.png"));
			alert.showAndWait();
		}
	}
	
	private void setLabelAlquiladaHandlers(LabelPeliculaAlquilada labelAlquilada) {
		labelAlquilada.addEventHandler(MouseEvent.MOUSE_CLICKED, this::alquiladaSeleccionadaHandler);
		labelAlquilada.setOnMousePressed((event) -> {
			timer = new Timer(true);
			timer.schedule(new TimerTask() {
				
				@Override
				public void run() {
						Platform.runLater(() -> {
							Alert alert = new Alert(AlertType.CONFIRMATION, "¿Devolver la película "+labelAlquilada.getAlquilada().getTituloEsp()+"?", ButtonType.YES, ButtonType.NO);
							Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
							stage.getIcons().add(
							    new Image("resources/logo.png"));
							alert.showAndWait();		
							if (alert.getResult() == ButtonType.YES)
								((IVideoclubSocio) videoclub).devolverPelicula(labelAlquilada.getAlquilada());
							Main.navigateTo(VIEW);
						});
				}
			}, 1000L);
		});
		labelAlquilada.setOnMouseReleased((event) -> timer.cancel());
	}
	
}
