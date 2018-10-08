package ui;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import dao.IDAOVideoclub;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import modelo.VideoclubComun;
import stub.DAOStub;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;

/*
 * Clase principal. Inicializa el modelo del videoclub e inicia la GUI en la ventana
 * de log in.
 */
public class Main extends Application {
	public static Stage primaryStage;

	@Override
	public void start(Stage primaryStage) throws IOException {
		Main.primaryStage = primaryStage;
		Parent root = FXMLLoader.load(getClass().getResource(LoginController.VIEW));

		// Para utilizar el stub
		IDAOVideoclub dao = new DAOStub();
		Logger.getAnonymousLogger().log(Level.INFO, "Using mock database");

		VideoclubComun videoclub = new VideoclubComun(dao);
		LoginController.setVideoclub(videoclub);

		Scene scene = new Scene(root);
		primaryStage.setTitle("Videoclub");
		primaryStage.setMinHeight(600.0);
		primaryStage.setMinWidth(800.0);
		primaryStage.setMaximized(true);
		primaryStage.getIcons().add(new Image("resources/logo.png"));
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

	public static Stage getPrimaryStage() {
		return primaryStage;
	}

	public static void navigateTo(String fileName) {
		try {
			Parent root = FXMLLoader.load(Main.class.getResource(fileName));
			primaryStage.getScene().setRoot(root);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
