package ui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import dominio.Genero;
import dominio.Pelicula;
import excepciones.ExcepcionNoHayCopiasDisponibles;
import excepciones.ExcepcionPeliculaYaAlquilada;
import excepciones.ExcepcionUsuarioMoroso;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import modelo.IVideoclubSocio;
import ui.custom.Toast;

public class ConsultarPeliculaSocioController extends SocioBarController implements Initializable {
	public final static String VIEW = "view/consultarPeliculaSocio.fxml";
	private static Pelicula pelicula;
	@FXML private JFXButton backButton;
	@FXML private Label titulo;
	@FXML private Label VOANYO;
	@FXML private Label descriptionArea;
	@FXML private HBox actoresBox;
	@FXML private HBox directoresBox;
	@FXML private HBox generosBox;
	@FXML private Label disponibles;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Platform.runLater( () -> {
			titulo.setText(pelicula.getTituloEsp());
			VOANYO.setText("VO: "+pelicula.getTituloVO()+" - "+pelicula.getAnyo());
			descriptionArea.setText(pelicula.getSinopsis());
			disponibles.setText(pelicula.getCopiasDisponibles()+" copias disponibles");
			
			for(String actor:pelicula.getListaActores()) {
				Label label = new Label(actor);
				label.setStyle("-fx-font-size: 14;");
				actoresBox.getChildren().add(label);
			}
			
			for(String director:pelicula.getListaDirectores()) {
				Label label = new Label(director);
				label.setStyle("-fx-font-size: 14;");
				directoresBox.getChildren().add(label);
			}
			
			for(Genero genero:pelicula.getListaGeneros()) {
				Label label = new Label(genero.toString());
				label.setStyle("-fx-font-size: 14;");
				generosBox.getChildren().add(label);
			}
			
		});
	
	}
	
	@FXML public void goBack(MouseEvent e) {
		goBack();
	}
	
	private static void goBack() {
		Main.navigateTo(CatalogoSocioController.VIEW);
	}
	
	@FXML public void alquilarPeli(MouseEvent e) throws IOException {
		Alert alert;
		Stage stage;
		
		try {
			((IVideoclubSocio) videoclub).alquilarPelicula(pelicula);
			Toast.makeText(Main.getPrimaryStage(), "Película alquilada", 1500, 500, 500);
			goBack(e);
		} catch (ExcepcionPeliculaYaAlquilada e1) {
			alert = new Alert(AlertType.INFORMATION, "Ya tienes esta película alquilada", ButtonType.OK);
			stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(
			    new Image("resources/logo.png"));
			alert.showAndWait();
		} catch (ExcepcionUsuarioMoroso e1) {
			alert = new Alert(AlertType.INFORMATION, "No puedes alquilar películas, eres moroso", ButtonType.OK);
			stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(
			    new Image("resources/logo.png"));
			alert.showAndWait();
		} catch (ExcepcionNoHayCopiasDisponibles e1) {
			alert = new Alert(AlertType.INFORMATION, "No quedan copias de esta película", ButtonType.OK);
			stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(
			    new Image("resources/logo.png"));
			alert.showAndWait();
		}
	}
	
	public static void setPelicula(Pelicula pelicula) {
		ConsultarPeliculaSocioController.pelicula = pelicula;
	}
	
}
