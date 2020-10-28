/*
 * Apache Felix OSGi tutorial.
**/

package tutorial.couplerBase.service;

/**
 * transforms a string
**/
public interface CouplerService
{
    /**
     * Transforms a string
     * @param string the string to be transformed.
     * @return String returns transformed string
    **/
    public String translateWord(String string);
}