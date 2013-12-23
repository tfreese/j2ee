package de.freese.acc.client;

import de.freese.acc.model.Depot;
import de.freese.acc.model.Security;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * @author Thomas Freese
 */
public class BuyOrderController extends AnchorPane implements Initializable
{
	/**
	 * 
	 */
	@FXML
	private Button buy = null;

	/**
	 * 
	 */
	@FXML
	private Button cancel = null;

	/**
	 * 
	 */
	@FXML
	private TextField count = null;

	/**
	 * 
	 */
	private Depot depot = null;

	/**
	 * 
	 */
	@FXML
	private TextField orderValue = null;

	/**
	 * 
	 */
	@FXML
	private TextField rate = null;

	/**
	 * 
	 */
	@FXML
	private TextField security = null;

	/**
	 * 
	 */
	private Security selectedSecurity = null;

	/**
	 * Erstellt ein neues {@link BuyOrderController} Object.
	 */
	public BuyOrderController()
	{
		super();
	}

	/**
	 * @param event {@link ActionEvent}
	 */
	public void buy(final ActionEvent event)
	{
		System.out.println("BuyOrderController.buy()");

		this.depot = Main.trader.buy(this.depot, this.selectedSecurity, Integer.valueOf(this.count.getText()));
		System.out.println(this.depot.getBalance());

		showTradeViewDialog();
	}

	/**
	 * @param event {@link ActionEvent}
	 */
	public void cancel(final ActionEvent event)
	{
		System.out.println("BuyOrderController.cancel()");

		showTradeViewDialog();
	}

	/**
	 * @param currentOrderValue {@link BigDecimal}
	 * @return boolean
	 */
	private boolean ifNotEnoughMoneyAvailable(final BigDecimal currentOrderValue)
	{
		BigDecimal balance = this.depot.getBalance();

		return currentOrderValue.compareTo(balance) > 0;
	}

	/**
	 * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
	 */
	@Override
	public void initialize(final URL arg0, final ResourceBundle arg1)
	{
	}

	/**
	 * @param depot {@link Depot}
	 */
	public void setDepot(final Depot depot)
	{
		this.depot = depot;
	}

	/**
	 * @param selectedSecurity {@link Security}
	 */
	public void showSecurityToBuy(final Security selectedSecurity)
	{
		this.selectedSecurity = selectedSecurity;
		// TODO show date
		this.security.setText(selectedSecurity.getIsin());
		this.rate.setText(Float.toString(selectedSecurity.getLatestRate().getRate()));
	}

	/**
	 * 
	 */
	private void showTradeViewDialog()
	{
		Stage stage = (Stage) this.cancel.getScene().getWindow();
		SceneLoader sceneLoader = new SceneLoader();
		sceneLoader.load("TradeView.fxml");
		Scene tradeViewScene = sceneLoader.getScene();
		TradeViewController tradeViewController = sceneLoader.getController();
		tradeViewController.setDepot(this.depot);
		tradeViewController.updateDepot();
		stage.setScene(tradeViewScene);
	}

	/**
	 * @param currentOrderValue BigDecimal
	 */
	private void updateBuyButton(final BigDecimal currentOrderValue)
	{
		this.buy.setDisable(ifNotEnoughMoneyAvailable(currentOrderValue));
	}

	/**
	 * @param event {@link KeyEvent}
	 */
	public void updateOrderValue(final KeyEvent event)
	{
		BigDecimal currentOrderValue = new BigDecimal(0);

		if (!this.count.getText().isEmpty())
		{
			BigDecimal currentCount = new BigDecimal(this.count.getText());
			BigDecimal currentRate = new BigDecimal(this.rate.getText());
			currentOrderValue = currentCount.multiply(currentRate);
		}

		this.orderValue.setText(currentOrderValue.toPlainString());

		updateBuyButton(currentOrderValue);
	}
}
