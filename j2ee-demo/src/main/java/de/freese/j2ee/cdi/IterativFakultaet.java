package de.freese.j2ee.cdi;

/**
 * @author Thomas Freese
 */
@Iterative
public class IterativFakultaet implements IFakultaet
{
	/**
	 * Erstellt ein neues {@link IterativFakultaet} Object.
	 */
	public IterativFakultaet()
	{
		super();
	}

	/**
	 * @see de.freese.j2ee.cdi.IFakultaet#getFakultaet(int)
	 */
	@Override
	public long getFakultaet(final int n)
	{
		long res = 1;

		for (int i = 1; i <= n; i++)
		{
			res *= i;
		}

		return res;
	}
}
