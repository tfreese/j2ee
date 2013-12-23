package de.freese.acc.model;

import java.math.BigDecimal;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Thomas Freese
 */
public class CalculateTest
{
	/**
	 * Erstellt ein neues {@link CalculateTest} Object.
	 */
	public CalculateTest()
	{
		super();
	}

	/**
	 * 
	 */
	@Test
	public void testBigDecimalCalculation()
	{
		// setup
		float rate = 103.57f;
		int count = 10;
		BigDecimal balance = new BigDecimal(25000);

		// calculate
		String string = Float.toString(rate);
		BigDecimal rateBigDecimal = new BigDecimal(string);
		BigDecimal orderValueBigDecimal = rateBigDecimal.multiply(new BigDecimal(count));

		balance = balance.subtract(orderValueBigDecimal);

		// verify
		Assert.assertEquals("23964.30", balance.toString());
	}
}
