package org.cwhd.measure

/**
 * Created by cdav18 on 1/9/15.
 */
public interface SourceDataFetcher {

    //this method should connect to the source system, get the data you need, parse it into a domain object, and save it
    //TODO i should probably break this up into separate methods for better testability and more obvious extendability
    //TODO and this should probably be an abstract class since there is default behaviour...
    def getData(startAt, maxResults, project, fromDate)

}