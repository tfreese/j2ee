package de.freese.acc.client;

import de.freese.acc.model.Depot;
import de.freese.acc.model.Security;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.TopicConnection;

/**
 * // TODO Maybe rename to "DepotController"
 * 
 * @author Thomas Freese
 */
public class TradeViewController extends AnchorPane implements Initializable, MessageListener
{
	/**
	 * 
	 */
	@FXML
	private Label balance = null;
	/**
	 * 
	 */
	@FXML
	private Button buy = null;
	/**
	 * 
	 */
	@FXML
	private TableColumn<Security, String> dateColumn = null;
	/**
	 * 
	 */
	private Depot depot = null;
	/**
	 * 
	 */
	@FXML
	private TableColumn<Security, String> nameColumn = null;
	/**
	 * 
	 */
	private ObservableList<Security> observableSecurities = null;
	/**
	 * 
	 */
	@FXML
	private Button quit = null;
	/**
	 * 
	 */
	@FXML
	private TableColumn<Security, String> rateColumn = null;
	/**
	 * 
	 */
	private List<Security> securities = null;
	/**
	 * 
	 */
	@FXML
	private TableView<Security> securityTable = null;
	/**
	 * 
	 */
	@FXML
	private Button sell = null;

	/**
	 * Erstellt ein neues {@link TradeViewController} Object.
	 */
	public TradeViewController()
	{
		super();

		setupJMS();
	}

	/**
	 * @param event {@link ActionEvent}
	 */
	public void buy(final ActionEvent event)
	{
		Security selectedSecurity = this.securityTable.getSelectionModel().getSelectedItem();

		Stage stage = (Stage) this.securityTable.getScene().getWindow();
		SceneLoader sceneLoader = new SceneLoader();
		sceneLoader.load("BuyOrderView.fxml");
		Scene tradeViewScene = sceneLoader.getScene();
		BuyOrderController buyOrderController = sceneLoader.getController();
		buyOrderController.showSecurityToBuy(selectedSecurity);
		buyOrderController.setDepot(this.depot);
		stage.setScene(tradeViewScene);
	}

	/**
	 * @return {@link Depot}
	 */
	public Depot getDepot()
	{
		return this.depot;
	}

	/**
	 * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
	 */
	@Override
	public void initialize(final URL location, final ResourceBundle resources)
	{
		this.securities = Main.securityStorage.getAllSecurities();
		this.observableSecurities = FXCollections.observableList(this.securities);
		updateSecurities();

		this.nameColumn.setCellValueFactory(new PropertyValueFactory<Security, String>("isin"));

		this.dateColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Security, String>, ObservableValue<String>>()
		{
			@Override
			public ObservableValue<String> call(final CellDataFeatures<Security, String> s)
			{
				Date date = s.getValue().getLatestRate().getDate();
				return new ReadOnlyObjectWrapper<String>(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(date));
			}
		});

		this.rateColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Security, String>, ObservableValue<String>>()
		{
			@Override
			public ObservableValue<String> call(final CellDataFeatures<Security, String> s)
			{
				String initialValue = Float.toString(s.getValue().getLatestRate().getRate());
				return new ReadOnlyObjectWrapper<String>(initialValue);
			}
		});

		this.securityTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Security>()
		{
			@Override
			public void changed(final ObservableValue<? extends Security> observableValue, final Security oldSecurity, final Security newSecurity)
			{
				if (newSecurity != null)
				{
					TradeViewController.this.buy.setDisable(false);
				}
			}
		});

		this.securityTable.setItems(this.observableSecurities);

		this.buy.setDisable(true);
	}

	/**
	 * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
	 */
	@Override
	public void onMessage(final Message message)
	{
		System.out.println("TradeViewController.onMessage()");

		try
		{
			if (message instanceof TextMessage)
			{
				String text = ((TextMessage) message).getText();

				if ("Securities updated.".equals(text))
				{
					updateSecurities();
				}
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
	 * @param depot {@link Depot}
	 */
	public void setDepot(final Depot depot)
	{
		this.depot = depot;
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
			MessageConsumer messageConsumer = session.createConsumer(Main.securitiesTopic);
			messageConsumer.setMessageListener(this);
			connection.start();
		}
		catch (JMSException ex)
		{
			System.out.println(ex);
		}
	}

	/**
	 * 
	 */
	public void updateDepot()
	{
		this.balance.setText(this.depot.getBalance().toString() + "€");
	}

	/**
	 * 
	 */
	private void updateSecurities()
	{
		this.observableSecurities.clear();
		this.observableSecurities.addAll(Main.securityStorage.getAllSecurities());
	}
}
