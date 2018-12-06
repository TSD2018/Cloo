/* Created by Kartik Venkataraman, 4 Dec 2018 */

package com.example.cloo2019

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase

class CleanActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clean)

        var toiletID: String = intent.getStringExtra("TOILET_ID")
//        toiletID = intent.getStringExtra("TOILET_ID")

        val buttonCleaned = findViewById<Button>(R.id.button_Cleaned)

        val janitorComments: EditText = findViewById(R.id.editTextOthers)
        val janitorID: String = "Test Name, should come from Janitor login"
        val recordTime: String = "Test, should include the system timestamp"

/*        janitorComments = findViewById(R.id.editTextOthers)
        janitorID = "Test Name, should come from Janitor login"
        recordTime = "Test, should include the system timestamp"
*/
        buttonCleaned.setOnClickListener(){

            val waterAvailable: CheckBox = findViewById(R.id.checkBoxWater)
            val lightWorking: CheckBox = findViewById(R.id.checkBoxElectricity)
            val stinks: CheckBox = findViewById(R.id.checkBoxStinks)
            val plumbingOk: CheckBox = findViewById(R.id.checkBoxPlumbing)
            val mirror: CheckBox = findViewById(R.id.checkBoxMirror)
            val sink: CheckBox = findViewById(R.id.checkBoxSink)
            val toilets: CheckBox = findViewById(R.id.checkBoxToilets)
            val floor: CheckBox = findViewById(R.id.checkBoxFloor)
            val wallTiles: CheckBox = findViewById(R.id.checkBoxWall)
            val soap: CheckBox = findViewById(R.id.checkBoxSoap)
            val tissue: CheckBox = findViewById(R.id.checkBoxTissue)

/*            waterAvailable = findViewById(R.id.checkBoxWater)
            lightWorking = findViewById(R.id.checkBoxElectricity)
            stinks = findViewById(R.id.checkBoxStinks)
            plumbingOk = findViewById(R.id.checkBoxPlumbing)
            mirror = findViewById(R.id.checkBoxMirror)
            sink = findViewById(R.id.checkBoxSink)
            toilets = findViewById(R.id.checkBoxToilets)
            floor = findViewById(R.id.checkBoxFloor)
            wallTiles = findViewById(R.id.checkBoxWall)
            soap = findViewById(R.id.checkBoxSoap)
            tissue = findViewById(R.id.checkBoxTissue)
*/


            var cleanedToilet = CleanedRecord()

/*            var cleanedToiletChecklist: List<CleanedRecordChecklist>

            cleanedToiletChecklist = mutableListOf(
                CleanedRecordChecklist("waterAvailable",waterAvailable.isChecked),
                CleanedRecordChecklist("lightsWorking",lightWorking.isChecked),
                CleanedRecordChecklist("stinks",stinks.isChecked),
                CleanedRecordChecklist("plumbingOk",plumbingOk.isChecked),
                CleanedRecordChecklist("mirror",mirror.isChecked),
                CleanedRecordChecklist("sink",sink.isChecked),
                CleanedRecordChecklist("toilets",toilets.isChecked),
                CleanedRecordChecklist("floor",floor.isChecked),
                CleanedRecordChecklist("wallTiles",wallTiles.isChecked),
                CleanedRecordChecklist("soap",soap.isChecked),
                CleanedRecordChecklist("tissue",tissue.isChecked))
*/
            cleanedToilet.toiletId = toiletID
//            cleanedToilet.checkList = cleanedToiletChecklist
            cleanedToilet.comments = janitorComments.text.toString()
            cleanedToilet.janitorID = janitorID
            cleanedToilet.timestamp = recordTime

            cleanedToilet.waterAvailable = waterAvailable.isChecked
            cleanedToilet.lightsWorking = lightWorking.isChecked
            cleanedToilet.stinks = stinks.isChecked
            cleanedToilet.plumbingOk = plumbingOk.isChecked
            cleanedToilet.mirror = mirror.isChecked
            cleanedToilet.sink = sink.isChecked
            cleanedToilet.toilets = toilets.isChecked
            cleanedToilet.floor = floor.isChecked
            cleanedToilet.wallTiles = wallTiles.isChecked
            cleanedToilet.soap = soap.isChecked
            cleanedToilet.tissue = tissue.isChecked

            val FireDBRef = FirebaseDatabase.getInstance().getReference("toilet_clean")
            val cleanedRecordId = FireDBRef.push().key
            if(cleanedRecordId == null) {
                Toast.makeText(this@CleanActivity, "push key returned null", Toast.LENGTH_SHORT).show()
//                return
            }
            else {
                FireDBRef.child(cleanedRecordId).setValue(cleanedToilet).addOnCompleteListener {
                    Toast.makeText(this@CleanActivity, "Thank you! Cleaned Record Submitted", Toast.LENGTH_SHORT).show()
                }
            }
            Toast.makeText(this@CleanActivity,"KV_DEBUG:: End of Cleaned in CleanActivity!", Toast.LENGTH_SHORT).show()


        }

    }
}
