package ui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import dominio.Genero;
import dominio.Licencia;
import dominio.Pelicula;
import excepciones.ExcepcionCamposNoValidos;
import excepciones.ExcepcionCamposNulos;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import modelo.IVideoclubAdmin;
import ui.custom.RemovableField;

public class EditarPeliculaController extends AdminBarController implements Initializable {
	public final static String VIEW ="view/editarPelicula.fxml";
	private static Pelicula pelicula;
	private ToggleGroup group;
	@FXML TextField titleEspField;
	@FXML TextField tituloVOField;
	@FXML TextField anyoField;
	@FXML TextArea sinopsisArea;
	@FXML HBox actoresBox;
	@FXML HBox directoresBox;
	@FXML HBox generosBox;
	@FXML HBox licenciasBox;
	@FXML Label error;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Platform.runLater(() -> {
			
			titleEspField.setText(pelicula.getTituloEsp());
			tituloVOField.setText(pelicula.getTituloVO());
			anyoField.setText(pelicula.getAnyo().toString());
			sinopsisArea.setText(pelicula.getSinopsis());
			
			JFXButton addActorButton = new JFXButton("+");
			addActorButton.setStyle("-fx-font-size: 14; -fx-text-fill: white;-fx-background-color: #4DB6AC;;-fx-font-weight: bold;");
			addActorButton.setPrefWidth(JFXButton.USE_COMPUTED_SIZE);
			addActorButton.setPrefHeight(JFXButton.USE_COMPUTED_SIZE);
			addActorButton.setOnMouseClicked((e) -> {
				if(actoresBox.getChildren().size() < 8)
					actoresBox.getChildren().add(actoresBox.getChildren().size()-1, new RemovableField(actoresBox));
			});
			actoresBox.getChildren().add(addActorButton);
			
			for(String actor:pelicula.getListaActores())
				actoresBox.getChildren().add(actoresBox.getChildren().size()-1, new RemovableField(actoresBox,actor));
			
			JFXButton addDirectorButton = new JFXButton("+");
			addDirectorButton.setStyle("-fx-font-size: 14; -fx-text-fill: white;-fx-background-color: #4DB6AC;;-fx-font-weight: bold;");
			addDirectorButton.setPrefWidth(JFXButton.USE_COMPUTED_SIZE);
			addDirectorButton.setPrefHeight(JFXButton.USE_COMPUTED_SIZE);
			addDirectorButton.setOnMouseClicked((e) -> {
				if(directoresBox.getChildren().size() < 8)
					directoresBox.getChildren().add(directoresBox.getChildren().size()-1, new RemovableField(directoresBox));
			});
			directoresBox.getChildren().add(addDirectorButton);
			
			for(String director:pelicula.getListaDirectores())
				directoresBox.getChildren().add(directoresBox.getChildren().size()-1, new RemovableField(directoresBox,director));
			
			for(Genero genero:Genero.values()) {
				CheckBox cBox = new CheckBox(genero.toString());
				generosBox.getChildren().add(cBox);
				if(pelicula.getListaGeneros().contains(genero))
					cBox.setSelected(true);
			}
			
			group = new ToggleGroup();
			for(Licencia licencia:Licencia.values()) {
				RadioButton btn = new RadioButton(licencia.toString().toLowerCase());
				btn.setToggleGroup(group);
				licenciasBox.getChildren().add(btn);
				if(pelicula.getNombreLicencia().equals(licencia))
					btn.setSelected(true);
			}

		});
	}
	
	@FXML public void keySubmit(KeyEvent e) throws IOException {
		if(e.getCode().equals(KeyCode.ENTER))
			add();
	}
	
	@FXML public void cancel(MouseEvent event) {
		Main.navigateTo(ConsultarPeliculaAdminController.VIEW);
	}
	
	@FXML public void add(MouseEvent event) {
		add();
	}
	
	@FXML public void goBack(MouseEvent e) {
		goBack();
	}
	
	private void add() {
		Pelicula nueva = new Pelicula();
		nueva.setTituloVO(tituloVOField.getText().trim());
		nueva.setTituloEsp(titleEspField.getText().trim());
		nueva.setSinopsis(sinopsisArea.getText().trim());
		if(nueva.getSinopsis().length() > 300) {
			error.setVisible(true);
			error.setText("La sinopsis no debe sobrepasar los 300 caracteres");
			return;
		}
		try {
			nueva.setAnyo(Integer.parseInt(anyoField.getText().trim()));
		} catch (NumberFormatException e) {
			if(anyoField.getText().trim().isEmpty()) {
				error.setText("Debes introducir todos los campos");
				error.setVisible(true);
			}
			else {
				error.setText("El año debe ser numérico");
				error.setVisible(true);
			}
			return;
		}
		
		for(Node node:actoresBox.getChildren())
			if(node instanceof RemovableField)
				if(!((RemovableField)node).getText().isEmpty())
					nueva.addActor(((RemovableField)node).getText());
		
		for(Node node:directoresBox.getChildren())
			if(node instanceof RemovableField)
				if(!((RemovableField)node).getText().isEmpty())
					nueva.addDirector(((RemovableField)node).getText());
		
		for(Node node:generosBox.getChildren())
			if(node instanceof CheckBox && ((CheckBox)node).isSelected())
				if(!nueva.getListaGeneros().contains(Genero.getGeneroPorNombre(((CheckBox)node).getText())))
				nueva.addGenero(Genero.getGeneroPorNombre(((CheckBox)node).getText()));
		
		if(nueva.getListaActores().isEmpty()) {
			error.setVisible(true);
			error.setText("Se ha de añadir por lo menos 1 actor");
			return;
		}
		
		if(nueva.getListaDirectores().isEmpty()) {
			error.setVisible(true);
			error.setText("Se ha de añadir por lo menos 1 director");
			return;
		}
		
		if(nueva.getListaGeneros().isEmpty()) {
			error.setVisible(true);
			error.setText("Se ha de seleccionar por lo menos 1 género");
			return;
		}
		
		if(nueva.getSinopsis().length() > 300) {
			error.setVisible(true);
			error.setText("La sinopsis no debe sobrepasar los 300 caracteres.");
			return;
		}
		
		if(group.getToggles().isEmpty()) {
			error.setVisible(true);
			error.setText("Debes elegir una licencia");
			return;
		}
		
		nueva.setNombreLicencia(Licencia.getLicenciaPorNombre(((RadioButton)group.getSelectedToggle()).getText()));

		try {
			((IVideoclubAdmin) videoclub).actualizarDatosPelicula(pelicula.getTituloVO(), pelicula.getAnyo(), nueva);
			ConsultarPeliculaAdminController.setPelicula(nueva);
			Main.navigateTo(ConsultarPeliculaAdminController.VIEW);
		} catch (ExcepcionCamposNoValidos e) {
			error.setVisible(true);
			error.setText("Hay campos no válidos");
		} catch (ExcepcionCamposNulos e) {
			error.setVisible(true);
			error.setText("Hay campos nulos");
		} 
		
	}

	public static void setPelicula(Pelicula pelicula) {
		EditarPeliculaController.pelicula = pelicula;
	}
	
	private static void goBack() {
		Main.navigateTo(PeliculasAdminController.VIEW);
	}

}
