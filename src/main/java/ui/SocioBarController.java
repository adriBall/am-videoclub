package ui;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

public class SocioBarController extends BarController {
	
	@FXML public void buttonCatalogo(MouseEvent e) throws IOException {
		Main.navigateTo(CatalogoSocioController.VIEW);
	}

}
