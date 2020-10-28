/*
 * Apache Felix OSGi tutorial.
**/

package tutorial.crucibleLite;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import org.osgi.framework.*;

import tutorial.couplerBase.service.CouplerService;


public class CrucibleLite implements BundleActivator
{
    final String couplersLocation = "file:/d:/Documents/tutorials/apache.felix/tutorial/couplers";

    public void start(BundleContext context) throws Exception
    {
        try {
            System.out.println("Enter a blank line to exit.");
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            String command = "";

            String[] couplers = new String[]{"coupler1.jar", "coupler2.jar"};
            int interator = 0;
            int counter = 0;

            while (true) {
                // Ask the user to enter a word.
                System.out.print("Load 1000 times, y/n?");
                command = in.readLine();
                if (command.toLowerCase().equals("n")) {
                    System.out.println("exiting");
                    break;
                }
                else if (command.toLowerCase().equals("y")){
                    for (int i = 0; i < 1000; i++) {
                        System.out.println("Beginning Loop");
                        installAndStartBundle(couplers[interator]);

                        ServiceReference[] refs = context.getServiceReferences(CouplerService.class.getName(), "(Language=*)");

                        if (refs != null) {

                            CouplerService coupler =
                                    (CouplerService) context.getService(refs[0]);

                            System.out.println(coupler.translateWord("TEST"));
                            context.ungetService(refs[0]);
                            //Thread.sleep(100);
                            uninstallBundle(couplers[interator]);
                        }
                        interator = (interator + 1) % 2;
                        counter++;
                        System.out.println("Loop number:" + counter);
                    }
                }
                else
                    System.out.print("Unknown command");
            }
        } finally {
            //Want it to run forever to test garbage collection
        }

    }

    public void stop(BundleContext context)
    {
        // NOTE: The service is automatically released.
    }

    void installAndStartBundle(String bundleName) throws BundleException {
        BundleContext bundlecontext = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
        Bundle b = bundlecontext.installBundle( couplersLocation+ File.separator + bundleName);
        try {
            b.start();
        } catch (BundleException e) {
            System.out.println("Missing bundle:" + bundleName);
        }

    }

    void uninstallBundle(String bundleName) throws BundleException {
        BundleContext bundlecontext = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
        Bundle b = bundlecontext.installBundle(couplersLocation + File.separator + bundleName);

        try {
            b.uninstall();
        } catch (BundleException e) {
            System.out.println("Missing bundle:" + bundleName);
        }
    }
}