package de.freese.cdi.weld;

import de.freese.cdi.weld.tellermachine.AutomatedTellerMachineImpl;
import de.freese.cdi.weld.tellermachine.IAutomatedTellerMachine;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

/**
 * CDI Demo mit WELD.
 *
 * @author Thomas Freese
 */
public class Main
{
    /**
     *
     */
    // private static BeanContainer beanContainer = BeanContainerManager.getInstance();
    /**
     * @param args String[]
     */
    public static void main(final String[] args)
    {
        final Weld weld = new Weld();
        WeldContainer container = weld.initialize();
//		Runtime.getRuntime().addShutdownHook(new Thread()
//		{
//			/**
//			 * @see java.lang.Thread#run()
//			 */
//			@Override
//			public void run()
//			{
//				weld.shutdown();
//			}
//		});

        IAutomatedTellerMachine atm
                = container.instance().select(AutomatedTellerMachineImpl.class).get();

        atm.deposit(1.00F);
    }
}
