package org.cwhd.calculate

import grails.transaction.Transactional
import com.gmongo.GMongo

@Transactional
class MapReducerService {
    def mongo = new GMongo()
    def db = mongo.getDB("test")
    //TODO figure out what cross-collection data needs to be map-reduced

    //TODO get the fields we need to map reduce for the collection
    //TODO run the map-reduce jobs to get the aggregated/sorted metric building blocks we need
    //TODO run a finalize to combine data to make complex metrics
    def mapReduceJiraData() {
        //reduce by project by day
        def result = db.jiraData.mapReduce(
                """
                function map() {
                    var key = this.doneDate;
                    var value = this.storyPoints;
                    emit(key, value);
                };
                """,
                """
                function reduce(key, values) {
                    return Array.sum(values)
                }
                """,
                "mapReduceSandbox",
                [:] // No Query
        )
        fillInTheBlanks("Story Points Over Time", "mapReduceSandbox")
    }

    def mapReduceStashData() {
        //reduce by repo by day

    }

    def mapReduceJenkinsData() {
        //reduce by build by day


    }

    /**
     * this method will fill in blank dates from the map-reduce operation
     * @param seriesName
     * @param sandboxName
     */
    private void fillInTheBlanks(seriesName, sandboxName) {
        def mapReduceSandboxCollection = []
        for(def p : db.mapReduceSandbox.find()) {
            if(p._id) {
                mapReduceSandboxCollection.add(p)
            }
        }
        def to = Calendar.instance
        to.setTime(Date.parse("yyyy-MM-dd", mapReduceSandboxCollection[mapReduceSandboxCollection.size-1]._id))
        def from = Calendar.instance
        from.setTime(Date.parse("yyyy-MM-dd", mapReduceSandboxCollection[0]._id))
        int sandboxHolder = 0
        from.upto(to) {
            def dateAsString = it.format("yyyy-MM-dd")
            println "DATE AS STRING; " + dateAsString
            def found = it.find{ d -> mapReduceSandboxCollection[sandboxHolder]._id.equals(dateAsString) }

            if(found) { //if we have data for this day...
//                def merp = new TimeDataSeriesPoint(date: Date.parse("yyyy-MM-dd", mapReduceSandboxCollection[sandboxHolder]._id), value: mapReduceSandboxCollection[sandboxHolder].value).save(failOnError: true)
//                tds.series.add(merp)
                sandboxHolder++
            } else { //if we don't have the date, add a 0
//                def merp = new TimeDataSeriesPoint(date: Date.parse("yyyy-MM-dd", dateAsString), value: 0).save(failOnError: true)
//                tds.series.add(merp)
            }
        }
//        tds.save()
//        def widget = DashboardWidget.get(47)
//        widget.timeDataSeries.add(tds)
//        widget.save()
        println "EVERYTHING HAS BEEN SAVED!"
    }


    //TODO these next steps should probably be in another service...
    //TODO for each formula run the formula against the metric building blocks we just map-reduced
    //TODO save them in a new collection; aggregatedMetrics { metricName:'whatever', metricValue:'55', date:'2015-02-28' }



    def serviceMethod() {
        //gmongo docs:
        //https://github.com/poiati/gmongo
    }
}
