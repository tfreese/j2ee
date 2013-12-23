package de.freese.acc.client;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * @author Thomas Freese
 */
public class JavaFxMain extends Application
{
	/**
	 * Erstellt ein neues {@link JavaFxMain} Object.
	 */
	public JavaFxMain()
	{
		super();
	}

	/**
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(final Stage primaryStage)
	{
		SceneLoader sceneLoader = new SceneLoader();
		sceneLoader.load("CreateDepot.fxml");
		Scene scene = sceneLoader.getScene();
		primaryStage.setScene(scene);
		primaryStage.sizeToScene();
		primaryStage.setResizable(false);
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.show();
	}
}
