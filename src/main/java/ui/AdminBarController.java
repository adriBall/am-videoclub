package ui;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

/*
 * Controlador que extiende a BarController para añadir los botones de listar socios y películas.
 */
public class AdminBarController extends BarController {
	
	@FXML public void buttonSocios(MouseEvent e) throws IOException {
		Main.navigateTo(SociosAdminController.VIEW);
	}
	
	@FXML public void buttonPeliculas(MouseEvent e) throws IOException {
		Main.navigateTo(PeliculasAdminController.VIEW);
	}

}
