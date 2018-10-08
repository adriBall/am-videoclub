package ui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import com.jfoenix.controls.JFXListView;

import dominio.Genero;
import dominio.Pelicula;
import excepciones.ExcepcionNoExistenPeliculas;
import excepciones.ExcepcionNoHayCopiasDisponibles;
import excepciones.ExcepcionPeliculaYaAlquilada;
import excepciones.ExcepcionUsuarioMoroso;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import modelo.IVideoclubSocio;
import ui.custom.LabelPelicula;
import ui.custom.Toast;

public class CatalogoSocioController extends SocioBarController implements Initializable {
	public final static String VIEW = "view/catalogoSocio.fxml";
	private volatile Timer timer;
	private ToggleGroup group;
	@FXML TextField titleField;
	@FXML TextField anyoField;
	@FXML TextField actorField;
	@FXML TextField directorField;
	@FXML VBox boxGeneros;
	@FXML private JFXListView<Label> listaPeliculas;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		new Thread(() -> cargarCatalogo()).start();
		
		Platform.runLater(() -> {
			group = new ToggleGroup();
			for(Genero genero:Genero.values()) {
				RadioButton btn = new RadioButton(genero.toString());
				btn.setToggleGroup(group);
				btn.setStyle("-fx-font-size: 14;-fx-text-fill: white;");
				boxGeneros.getChildren().add(btn);
			}
		});
	}
	
	@FXML public void buttonBuscar(MouseEvent e) {
		
		Alert alert = new Alert(AlertType.INFORMATION, "Función en desarrollo", ButtonType.OK);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(
		    new Image("resources/logo.png"));
		alert.showAndWait();
//		new Thread(() -> cargarBusqueda()).start();
	}
	
	@FXML public void buttonLimpiar(MouseEvent e) {
		Main.navigateTo(VIEW);
	}
	
	public void cargarBusqueda() {
		String titulo = titleField.getText().trim();
		if(titulo.isEmpty())
			titulo = null;
		
		String actor = actorField.getText().trim();
		if(actor.isEmpty())
			actor = null;
		
		String director = directorField.getText().trim();
		if(director.isEmpty())
			director = null;
		
		Integer anyo = null;
		if(!anyoField.getText().trim().isEmpty())
			try {
				anyo = Integer.parseInt(anyoField.getText().trim());
			} catch (NumberFormatException e) {
				anyoField.clear();
			}
		
		Genero genero = null;
		if(group.getSelectedToggle() != null)
			genero = Genero.getGeneroPorNombre(((RadioButton)group.getSelectedToggle()).getText());
		
		List<Pelicula> busqueda = ((IVideoclubSocio)videoclub).buscarPeliculas(titulo, actor, director, genero, anyo);
		Platform.runLater(() -> {
			listaPeliculas.getItems().clear();
			for(Pelicula pelicula:busqueda) {
				LabelPelicula labelPelicula = new LabelPelicula(pelicula);
				setLabelPeliculaHandlers(labelPelicula);
				listaPeliculas.getItems().add(labelPelicula);
			}
		});
	}
	
	private void cargarCatalogo() {
		try {
			List<Pelicula> catalogo = ((IVideoclubSocio) videoclub).listarPeliculas();
			Platform.runLater(() -> {
				for(Pelicula pelicula:catalogo) {
					LabelPelicula labelPelicula = new LabelPelicula(pelicula);
					setLabelPeliculaHandlers(labelPelicula);
					listaPeliculas.getItems().add(labelPelicula);
				}
			});
		} catch (ExcepcionNoExistenPeliculas e) {
			Alert alert = new Alert(AlertType.INFORMATION, "No hay películas", ButtonType.OK);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(
			    new Image("resources/logo.png"));
			alert.showAndWait();
		}
	}
	
	public void peliculaSeleccionadaHandler(MouseEvent event) {
		LabelPelicula source = (LabelPelicula) event.getSource();
		Pelicula peliculaID = new Pelicula();
		peliculaID.setTituloVO(source.getTituloVO());
		peliculaID.setAnyo(source.getAnyo());
		Pelicula pelicula = videoclub.verDatosPelicula(peliculaID);
		
		ConsultarPeliculaSocioController.setPelicula(pelicula);
		Main.navigateTo(ConsultarPeliculaSocioController.VIEW);
		
	}
	
	private void setLabelPeliculaHandlers(LabelPelicula labelPelicula) {
		labelPelicula.addEventHandler(MouseEvent.MOUSE_CLICKED, this::peliculaSeleccionadaHandler);
		labelPelicula.setOnMousePressed((event) -> {
			timer = new Timer(true);
			timer.schedule(new TimerTask() {
				
				@Override
				public void run() {
						Platform.runLater(() -> {
							Alert alert = new Alert(AlertType.CONFIRMATION, "¿Alquilar la película "+labelPelicula.getPelicula().getTituloEsp()+"?", ButtonType.YES, ButtonType.NO);
							Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
							stage.getIcons().add(
							    new Image("resources/logo.png"));
							alert.showAndWait();		
							if (alert.getResult() == ButtonType.YES) {
								try {
									((IVideoclubSocio) videoclub).alquilarPelicula(labelPelicula.getPelicula());
									Toast.makeText(Main.getPrimaryStage(), "Película alquilada", 1500, 500, 500);
								} catch (ExcepcionPeliculaYaAlquilada e) {
									alert = new Alert(AlertType.INFORMATION, "Ya tienes esta película alquilada", ButtonType.OK);
									stage = (Stage) alert.getDialogPane().getScene().getWindow();
									stage.getIcons().add(
									    new Image("resources/logo.png"));
									alert.showAndWait();
								} catch (ExcepcionUsuarioMoroso e) {
									alert = new Alert(AlertType.INFORMATION, "No puedes alquilar películas, eres moroso", ButtonType.OK);
									stage = (Stage) alert.getDialogPane().getScene().getWindow();
									stage.getIcons().add(
									    new Image("resources/logo.png"));
									alert.showAndWait();
								} catch (ExcepcionNoHayCopiasDisponibles e) {
									alert = new Alert(AlertType.INFORMATION, "No quedan copias de esta película", ButtonType.OK);
									stage = (Stage) alert.getDialogPane().getScene().getWindow();
									stage.getIcons().add(
									    new Image("resources/logo.png"));
									alert.showAndWait();
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
