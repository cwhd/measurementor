package org.cwhd.measure

class History {
    static mapWith = "mongo"
    static searchable = true

    Date timestamp
    String changedBy
    String changeField
    String newValue
    String key
    String sourceId
    String dataType

    static constraints = {
        timestamp nullable: true
        changedBy nullable: true
        changeField nullable: true
        newValue nullable: true
        key nullable: true
        sourceId nullable: true
        dataType nullable: true
     }
}
