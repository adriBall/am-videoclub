package ui;

import javafx.fxml.FXML;

import javafx.scene.input.MouseEvent;
import modelo.IVideoclubComun;

/*
 * Controlador de los elementos comunes para todos los usuarios de la barra superior.
 * Estos son los botones de cerrar sesión, ver el catálogo e ir a mi área.
 */
public class BarController {
	protected static IVideoclubComun videoclub;
	
	@FXML public void logOut(MouseEvent e) {
		videoclub.cerrarSesion();
		Main.navigateTo(LoginController.VIEW);
	}
	
	@FXML public void buttonMiArea(MouseEvent e) {
		if(videoclub.esSocio())
			Main.navigateTo(AreaSocioController.VIEW);
		else
			Main.navigateTo(AreaAdminController.VIEW);
	}
	
	public synchronized static void setVideoclub(IVideoclubComun videoclub) {
		BarController.videoclub = videoclub;
	}

}
