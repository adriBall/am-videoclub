package ui;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import dominio.Genero;
import dominio.Pelicula;
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
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import modelo.IVideoclubAdmin;

public class ConsultarPeliculaAdminController extends AdminBarController implements Initializable {
	public final static String VIEW = "view/consultarPeliculaAdmin.fxml";
	private static Pelicula pelicula;
	@FXML private JFXButton backButton;
	@FXML private Label titulo;
	@FXML private Label VOANYO;
	@FXML private Label descriptionArea;
	@FXML private HBox actoresBox;
	@FXML private HBox directoresBox;
	@FXML private HBox generosBox;
	@FXML private Label disponibles;
	@FXML private JFXButton editButton;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Platform.runLater( () -> {
			titulo.setText(pelicula.getTituloEsp());
			VOANYO.setText("VO: "+pelicula.getTituloVO()+" - "+pelicula.getAnyo());
			descriptionArea.setText(pelicula.getSinopsis());
			disponibles.setText(pelicula.getCopiasDisponibles()+" de "
			+pelicula.getNombreLicencia().getNumeroPrestamos()+" copias disponibles (Licencia "
					+pelicula.getNombreLicencia().toString()+")");
			
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
	
	@FXML public void editarDatos(MouseEvent e) {
		EditarPeliculaController.setPelicula(pelicula);
		Main.navigateTo(EditarPeliculaController.VIEW);
	}
	
	@FXML public void eliminarPeli(MouseEvent e) {
		Alert alert = new Alert(AlertType.CONFIRMATION, "¿Eliminar la película "+pelicula.getTituloEsp()+"?", ButtonType.YES, ButtonType.NO);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(
		    new Image("resources/logo.png"));
		alert.showAndWait();		
		if (alert.getResult() == ButtonType.YES) {
			try {
				((IVideoclubAdmin) videoclub).darBajaPelicula(pelicula);
				goBack();
			} catch (ExcepcionPeliculaNoExiste e1) {
				e1.printStackTrace();
			}
		}
		
	}
	
	@FXML public void goBack(MouseEvent e) {
		goBack();
	}
	
	private static void goBack() {
		Main.navigateTo(PeliculasAdminController.VIEW);
	}
	
	public static void setPelicula(Pelicula pelicula) {
		ConsultarPeliculaAdminController.pelicula = pelicula;
	}
	
}
