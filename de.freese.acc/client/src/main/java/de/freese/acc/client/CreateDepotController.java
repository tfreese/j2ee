package de.freese.acc.client;

import de.freese.acc.model.Depot;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TopicConnection;

/**
 * @author Thomas Freese
 */
public class CreateDepotController extends AnchorPane implements Initializable, MessageListener
{
	/**
	 * 
	 */
	@FXML
	private Button create = null;
	/**
	 * 
	 */
	@FXML
	private Label label = null;
	/**
	 * 
	 */
	@FXML
	private TextField mailAddress = null;
	/**
	 * 
	 */
	@FXML
	private PasswordField password = null;

	/**
	 * Erstellt ein neues {@link CreateDepotController} Object.
	 */
	public CreateDepotController()
	{
		super();

		setupJMS();
	}

	/**
	 * @param event {@link KeyEvent}
	 */
	public void checkEMailAddress(final KeyEvent event)
	{
		if (Main.depotStorage.mailAddressUsed(this.mailAddress.getText()))
		{
			this.label.setText("Die E-Mail Adresse ist schon registriert.");
			this.create.setDisable(true);
		}
		else
		{
			this.label.setText("");
			this.create.setDisable(false);
		}
	}

	/**
	 * @param event {@link ActionEvent}
	 */
	public void createDepot(final ActionEvent event)
	{
		Depot depot = Main.depotStorage.register(this.mailAddress.getText(), this.password.getText());

		if (depot != null)
		{
			Stage stage = (Stage) this.label.getScene().getWindow();
			SceneLoader sceneLoader = new SceneLoader();
			sceneLoader.load("TradeView.fxml");
			Scene tradeViewScene = sceneLoader.getScene();
			TradeViewController tradeViewController = sceneLoader.getController();
			tradeViewController.setDepot(depot);
			tradeViewController.updateDepot();
			stage.setScene(tradeViewScene);
		}
		else
		{
			this.label.setText("Es ist ein Fehler aufgetreten!");
		}
	}

	/**
	 * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
	 */
	@Override
	public void initialize(final URL arg0, final ResourceBundle arg1)
	{
	}

	/**
	 * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
	 */
	@Override
	public void onMessage(final Message message)
	{
		System.out.println("CreateDepotController.onMessage()");
		System.out.println(message);

		try
		{
			System.out.println("Destination: " + message.getJMSDestination());

			if (message instanceof ObjectMessage)
			{
				ObjectMessage objectMessage = (ObjectMessage) message;
				Depot depot = (Depot) objectMessage.getObject();
				updateLabel(depot);
			}
		}
		catch (JMSException ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 * @param event {@link ActionEvent}
	 */
	public void quit(final ActionEvent event)
	{
		Platform.exit();
	}

	/**
	 * 
	 */
	private void setupJMS()
	{
		try
		{
			TopicConnection connection = Main.connectionFactory.createTopicConnection();
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			MessageConsumer messageConsumer = session.createConsumer(Main.topic);
			messageConsumer.setMessageListener(this);
			connection.start();
		}
		catch (JMSException ex)
		{
			System.out.println(ex);
		}
	}

	/**
	 * @param depot {@link Depot}
	 */
	private void updateLabel(final Depot depot)
	{
		Platform.runLater(new Runnable()
		{
			/**
			 * @see java.lang.Runnable#run()
			 */
			@Override
			public void run()
			{
				CreateDepotController.this.label.setText(CreateDepotController.this.label.getText() + " " + "Depot erhalten: " + depot.getMailAddress());
			}
		});
	}
}
