package de.freese.acc.client;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

/**
 * @author Thomas Freese
 */
public class NumericTextField extends TextField
{
	/**
	 * Erstellt ein neues {@link NumericTextField} Object.
	 */
	public NumericTextField()
	{
		textProperty().addListener(new ChangeListener<String>()
		{
			/**
			 * @param observableValue ObservableValue
			 * @param oldValue String
			 * @param newValue String
			 */
			@Override
			public void changed(final ObservableValue<? extends String> observableValue, final String oldValue, final String newValue)
			{
				if (isEmpty(newValue) || isInteger(newValue))
				{
					setText(newValue);
				}
				else
				{
					setText(oldValue);
				}
			}

			/**
			 * @param newValue String
			 * @return boolean
			 */
			private boolean isEmpty(final String newValue)
			{
				return (newValue == null) || newValue.isEmpty();
			}

			/**
			 * @param newValue String
			 * @return boolean
			 */
			private boolean isInteger(final String newValue)
			{
				try
				{
					Integer.valueOf(newValue);
					return true;
				}
				catch (NumberFormatException ex)
				{
					System.out.println("Ignoring invalid value...");
					return false;
				}
			}
		});
	}
}
