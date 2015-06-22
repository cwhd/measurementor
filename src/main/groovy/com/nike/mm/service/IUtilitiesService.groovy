package com.nike.mm.service

/**
 * general utilities as helper methods
 */
interface IUtilitiesService {

    /**
     * removes the base of the URL and returns the path
     * @param url
     * @return
     */
    String getPathFromUrl(url)

    /**
     * Cleans the name of the build.
     * @param fullBuildName
     * @return
     */
    String cleanFullBuildName(fullBuildName)

    /**
     * take the @ out of emails so elasticsearch doesn't index everyone's email domain
     * @param author string representing the original email
     * @return string with the clean, EC index friendly email
     */
    String cleanEmail(author)

    /**
     * User names from JIRA have spaces, dashes, and other strange things that will mess up the EC index.
     * This method cleans all that junk off
     * @param userName
     * @return
     */
    String makeNonTokenFriendly(userName)

    /**
     * get a data from a JIRA REST response and turn it into a real date
     * @param date string that represents a date from a JIRA response
     * @return a java date
     */
    Date cleanJiraDate(date)

    /**
     * get a data from a Github REST response and turn it into a real date
     * @param date string that represents a date from a Github response
     * @return a java date
     */
    Date cleanGithubDate(date)

    /**
     * this method will tell you if your estimates are high or low.
     * get the exact time ratio for the upper & lower bounds
     * find the difference between the 2
     * divide that by 2
     * @param estimate the original estimate on the card
     * @param actualTime how long (in days) the task took to complete
     * @param maxEstimate the highest possible estimate for your team
     * @param maxTime the most time your team can spend on a task.  This should be the length of your dev cycle in days
     * @param estimationValues the possible values that your team can estimate.  This is typcially the beginning of the fibonacci series.
     * @return a value of 0 indicates that you're good.  Greater than 0 means underestimating, less than 0 indicated overestimating.
     */
    def estimateHealth(estimate, actualTime, maxEstimate, maxTime, estimationValues)

    /**
     * this will return the different between 2 dates in unix timestamp format in hours
     * @param firstDate whichever of the 2 dates happened first
     * @param secondDate  whichever of the 2 dates happend last
     * @return a double representing the difference in hours.
     * For example If the difference is 55 minutes return would be 0.9166666667
     * Check out the unit tests to see some test data
     */
    double getDifferenceBetweenDatesInHours(firstDate, secondDate)
}