package ui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import com.jfoenix.controls.JFXListView;

import dominio.Pelicula;
import excepciones.ExcepcionNoExistenPeliculas;
import excepciones.ExcepcionPeliculaNoExiste;
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
import ui.custom.LabelPelicula;;

/*
 * Controlador de la vista catalogoAdmin. Extiende a AdminBarController y además inicializa
 * la lista de películas obteniendolas del videoclub.
 */
public class PeliculasAdminController extends AdminBarController implements Initializable {
	public final static String VIEW = "view/peliculasAdmin.fxml";
	private volatile Timer timer;
	@FXML private JFXListView<Label> listaPeliculas;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		new Thread(() -> cargarCatalogo()).start();
	}
	
	@FXML public void altaPeli(MouseEvent e) {
		AnyadirPeliculaController.setBackView(VIEW);
		Main.navigateTo(AnyadirPeliculaController.VIEW);
	}
	
	private void cargarCatalogo() {
		try {
			List<Pelicula> catalogo = ((IVideoclubAdmin) videoclub).listarPeliculas();
			Platform.runLater(() -> {
				for(Pelicula pelicula:catalogo) {
					LabelPelicula labelPelicula = new LabelPelicula(pelicula);
					setLabelPeliculaHandlers(labelPelicula);
					listaPeliculas.getItems().add(labelPelicula);
				}
			});
		} catch (ExcepcionNoExistenPeliculas e) {}
	}
	
	public void peliculaSeleccionadaHandler(MouseEvent event) {
		LabelPelicula source = (LabelPelicula) event.getSource();
		Pelicula peliculaID = new Pelicula();
		peliculaID.setTituloVO(source.getTituloVO());
		peliculaID.setAnyo(source.getAnyo());
		Pelicula pelicula = videoclub.verDatosPelicula(peliculaID);
		
		ConsultarPeliculaAdminController.setPelicula(pelicula);
		Main.navigateTo(ConsultarPeliculaAdminController.VIEW);
		
	}
	
	private void setLabelPeliculaHandlers(LabelPelicula labelPelicula) {
		labelPelicula.addEventHandler(MouseEvent.MOUSE_CLICKED, this::peliculaSeleccionadaHandler);
		labelPelicula.setOnMousePressed((event) -> {
			timer = new Timer(true);
			timer.schedule(new TimerTask() {
				
				@Override
				public void run() {
						Platform.runLater(() -> {
							Alert alert = new Alert(AlertType.CONFIRMATION, "¿Eliminar la película "+labelPelicula.getPelicula().getTituloEsp()+"?", ButtonType.YES, ButtonType.NO);
							Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
							stage.getIcons().add(
							    new Image("resources/logo.png"));
							alert.showAndWait();		
							if (alert.getResult() == ButtonType.YES) {
								try {
									((IVideoclubAdmin) videoclub).darBajaPelicula(labelPelicula.getPelicula());
								} catch (ExcepcionPeliculaNoExiste e) {
									e.printStackTrace();
								}
							}
							Main.navigateTo(VIEW);
						});
				}
			}, 1000L);
		});
		labelPelicula.setOnMouseReleased((event) -> timer.cancel());
	}
	
}
