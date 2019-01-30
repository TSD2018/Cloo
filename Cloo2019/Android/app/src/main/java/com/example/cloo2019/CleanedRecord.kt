/* Created by Kartik Venkataraman, 4 Dec 2018 */
/* Rev 0.2.  Code Cleanup - Kartik Venkataraman 29 Jan 2019 */

package com.example.cloo2019

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
