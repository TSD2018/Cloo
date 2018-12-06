/* Created by Kartik Venkataraman, 4 Dec 2018 */

package com.example.cloo2019

class CleanedRecordChecklist (val checkListItem: String, val checkListValue: Boolean)
{
}

class CleanedRecord (
    var cleanedRecordId: String, var toiletId: String,
    var waterAvailable: Boolean, var lightsWorking: Boolean, var stinks: Boolean, var plumbingOk: Boolean,
    var mirror: Boolean, var sink: Boolean, var toilets: Boolean, var floor: Boolean, var wallTiles: Boolean,
    var soap: Boolean, var tissue: Boolean, var comments: String, var timestamp: String, var janitorID: String) {

    constructor() : this("", "", false, false, true, false,
        false, false, false, false, false, false, false,"", "",
        "")
    {
    }
}

/*
class CleanedRecord (
    var cleanedRecordId: String, var toiletId: String, var checkList: CleanedRecordChecklist?, var comments: String,
    var timestamp: String, var janitorID: String) {
    constructor() : this("", "", null, "", "", "") {
    }
}
        */
