package cloudsession;

/**
 * @author Thomas Freese
 */
public class SessionServiceFactory
{
	/**
	 * 
	 */
	private static ICloudSession session = null;

	/**
	 * 
	 */
	private static int defaultSessionLivetimeInSecs = 15;

	/**
	 * @return {@link ICloudSession}
	 */
	public static ICloudSession getService()
	{
		return getService(defaultSessionLivetimeInSecs);
	}

	/**
	 * @param defaultSessionLivetimeInSecs int
	 * @return {@link ICloudSession}
	 */
	public static ICloudSession getService(final int defaultSessionLivetimeInSecs)
	{
		if (session == null)
		{
			ICloudSession cs = new DummySessionService();
			// CloudSession cs = new AmazonSessionService();

			session = new CloudSessionCache(cs, defaultSessionLivetimeInSecs);
		}

		return session;
	}
}
