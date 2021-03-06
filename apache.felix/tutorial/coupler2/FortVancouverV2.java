/*
 * Apache Felix OSGi tutorial.
**/

package tutorial.coupler2;

import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import tutorial.couplerBase.service.CouplerService;

/**
 * This class implements a simple bundle that uses the bundle
 * context to register an English language dictionary service
 * with the OSGi framework. The dictionary service interface is
 * defined in a separate class file and is implemented by an
 * inner class.
**/
public class FortVancouverV2 implements BundleActivator
{
    /**
     * Implements BundleActivator.start(). Registers an
     * instance of a dictionary service using the bundle context;
     * attaches properties to the service that can be queried
     * when performing a service look-up.
     * @param context the framework context for the bundle.
    **/
    public void start(BundleContext context)
    {
        Hashtable<String, String> props = new Hashtable<String, String>();
        props.put("Language", "English");
        context.registerService(
            CouplerService.class.getName(), new FortVancouverImpl(), props);
    }

    /**
     * Implements BundleActivator.stop(). Does nothing since
     * the framework will automatically unregister any registered services.
     * @param context the framework context for the bundle.
    **/
    public void stop(BundleContext context)
    {
        // NOTE: The service is automatically unregistered.
    }

    /**
     * A private inner class that implements a dictionary service;
     * see CouplerService for details of the service.
    **/
    private static class FortVancouverImpl implements CouplerService
    {
        /**
         * Implements CouplerService.checkWord(). Determines
         * if the passed in word is contained in the dictionary.
         * @param word the word to be checked.
         * @return true if the word is in the dictionary,
         *         false otherwise.
        **/
        public String translateWord(String word)
        {
            return "translatedWord2:"+word.toLowerCase();
        }
    }
}