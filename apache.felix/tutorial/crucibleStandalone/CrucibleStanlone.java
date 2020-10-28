package crucibleStandalone;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import host.service.command.Command;
import org.apache.felix.framework.Felix;
import org.apache.felix.framework.util.FelixConstants;
import org.apache.felix.framework.util.StringMap;
import org.apache.felix.framework.cache.BundleCache;
import org.osgi.util.tracker.ServiceTracker;

public class CrucibleStandalone
{
    private HostActivator m_activator = null;
    private Felix m_felix = null;
    private ServiceTracker m_tracker = null;

    public CrucibleStandalone()
    {
        // Create a case-insensitive configuration property map.
        Map configMap = new StringMap(false);
        // Configure the Felix instance to be embedded.
        configMap.put(FelixConstants.EMBEDDED_EXECUTION_PROP, "true");
        // Add the bundle provided service interface package and the core OSGi
        // packages to be exported from the class path via the system bundle.
        configMap.put(Constants.FRAMEWORK_SYSTEMPACKAGES,
                "org.osgi.framework; version=1.3.0," +
                        "org.osgi.service.packageadmin; version=1.2.0," +
                        "org.osgi.service.startlevel; version=1.0.0," +
                        "org.osgi.service.url; version=1.0.0," +
                        "host.service.command; version=1.0.0");
        // Explicitly specify the directory to use for caching bundles.
        configMap.put(BundleCache.CACHE_PROFILE_DIR_PROP, "cache");

        try
        {
            // Create host activator;
            m_activator = new HostActivator(m_lookupMap);
            List list = new ArrayList();
            list.add(m_activator);

            // Now create an instance of the framework with
            // our configuration properties and activator.
            m_felix = new Felix(configMap, list);

            // Now start Felix instance.
            m_felix.start();
        }
        catch (Exception ex)
        {
            System.err.println("Could not create framework: " + ex);
            ex.printStackTrace();
        }

        m_tracker = new ServiceTracker(
                m_activator.getContext(), Command.class.getName(), null);
        m_tracker.open();
    }

    public boolean execute(String name, String commandline)
    {
        // See if any of the currently tracked command services
        // match the specified command name, if so then execute it.
        Object[] services = m_tracker.getServices();
        for (int i = 0; (services != null) && (i < services.length); i++)
        {
            try
            {
                if (((Command) services[i]).getName().equals(name))
                {
                    return ((Command) services[i]).execute(commandline);
                }
            }
            catch (Exception ex)
            {
                // Since the services returned by the tracker could become
                // invalid at any moment, we will catch all exceptions, log
                // a message, and then ignore faulty services.
                System.err.println(ex);
            }
        }
        return false;
    }

    public void shutdownApplication()
    {
        // Shut down the felix framework when stopping the
        // host application.
        m_felix.shutdown();
    }
}