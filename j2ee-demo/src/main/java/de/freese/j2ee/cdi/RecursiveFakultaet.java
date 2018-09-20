package de.freese.j2ee.cdi;

/**
 * @author Thomas Freese
 */
@Recursive
public class RecursiveFakultaet implements IFakultaet
{
	/**
	 * Erstellt ein neues {@link RecursiveFakultaet} Object.
	 */
	public RecursiveFakultaet()
	{
		super();
	}

	/**
	 * @see de.freese.j2ee.cdi.IFakultaet#getFakultaet(int)
	 */
	@Override
	public long getFakultaet(final int n)
	{
		if (n > 1)
		{
			return n * getFakultaet(n - 1);
		}
		else
		{
			return 1;
		}
	}
}
