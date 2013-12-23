package de.freese.acc.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

/**
 * @author Thomas Freese
 */
public class SceneLoader
{
	/**
	 * 
	 */
	private final FXMLLoader loader;

	/**
	 * 
	 */
	private Scene scene = null;

	/**
	 * Erstellt ein neues {@link SceneLoader} Object.
	 */
	public SceneLoader()
	{
		super();

		this.loader = new FXMLLoader();
	}

	/**
	 * @return Object
	 */
	public <T> T getController()
	{
		return this.loader.getController();
	}

	/**
	 * @return {@link Scene}
	 */
	public Scene getScene()
	{
		return this.scene;
	}

	/**
	 * @param fxml String
	 */
	public void load(final String fxml)
	{
		URL url = ClassLoader.getSystemResource("fxml/" + fxml);

		// InputStream inputStream = JavaFxMain.class.getResourceAsStream(fxml);
		this.loader.setBuilderFactory(new JavaFXBuilderFactory());
		// this.loader.setLocation(JavaFxMain.class.getResource(fxml));
		this.loader.setLocation(url);
		AnchorPane page = null;

		try (InputStream inputStream = url.openStream())
		{
			page = (AnchorPane) this.loader.load(inputStream);
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}

		this.scene = new Scene(page);
	}
}
