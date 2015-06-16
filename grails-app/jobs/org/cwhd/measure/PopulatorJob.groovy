package org.cwhd.measure

import org.apache.commons.logging.LogFactory
/**
 * the main job that calls all the services to get data.  note that history
 * is stored in mongo along with everything else
 */
class PopulatorJob {
    private static final logger = LogFactory.getLog(this)
    def updateDataFromSourceService

    static triggers = {
        //wait an hour to start, repeat every 8 hours
        //simple startDelay: 3600000, repeatInterval: 28800000, repeatCount:-1
    }

    def execute() {
        logger.info("----------------------------------")
        logger.info("STARTING TIMED JOB")
        logger.info("----------------------------------")
        //updateDataFromSourceService.getAllData()
    }
}