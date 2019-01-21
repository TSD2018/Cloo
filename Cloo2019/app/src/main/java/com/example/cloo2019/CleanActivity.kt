/* Created by Kartik Venkataraman, 4 Dec 2018 */

package com.example.cloo2019

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CleanActivity : AppCompatActivity() {

    val TAG = "CleanActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clean)

        val toiletID: String = intent.getStringExtra("TOILET_ID")
        val toiletName: String = intent.getStringExtra("TOILET_NAME")
        val janitorName: String = intent.getStringExtra("TOILET_JANITOR")


        val textToiletName = findViewById<TextView >(R.id.textView_toiletName)
        textToiletName.text = toiletName

        val textJanitorName = findViewById<EditText>(R.id.editText_JanitorName)
        textJanitorName.setText(janitorName)

        val buttonCleaned = findViewById<Button>(R.id.button_Cleaned)
        val janitorComments: EditText = findViewById(R.id.editTextOthers)
        var timeStamp = findViewById<TextView>(R.id.textViewCleanTimeStamp)
        timeStamp!!.text = CurrentTimeStamp().getPresentableTimeString(CurrentTimeStamp().getString())

//        val janitorID: String = "Test Name, should come from Janitor login"
//        var recordTime: String = "Test, should include the system timestamp"

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
            val recordTime = CurrentTimeStamp().getString()

/*            var cleanedToilet = CleanedRecord()
            cleanedToilet.toiletId = toiletID
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
*/
            val FireDBRef = FirebaseDatabase.getInstance().getReference("ToiletClean/$toiletID")
            val cleanedRecordId = FireDBRef.push().key
            if(cleanedRecordId == null) {
                Toast.makeText(this@CleanActivity, "push key returned null", Toast.LENGTH_SHORT).show()
            }
            else {
                var cleanedToilet = CleanedRecord(
                    cleanedRecordId,
                    toiletID,
                    waterAvailable.isChecked,
                    lightWorking.isChecked,
                    stinks.isChecked,
                    plumbingOk.isChecked,
                    mirror.isChecked,
                    sink.isChecked,
                    toilets.isChecked,
                    floor.isChecked,
                    wallTiles.isChecked,
                    soap.isChecked,
                    tissue.isChecked,
                    janitorComments.text.toString(),
                    CurrentTimeStamp().getString(),
                    janitorName)

               FireDBRef.child(cleanedRecordId).setValue(cleanedToilet).addOnCompleteListener {
                    Toast.makeText(this@CleanActivity, "Thank you! Cleaned Record Submitted", Toast.LENGTH_SHORT).show()
                }
            }
            Toast.makeText(this@CleanActivity,"KV_DEBUG:: End of Cleaned in CleanActivity!", Toast.LENGTH_SHORT).show()

/****** TODO: 10-Jan-2019: Kartik TOP *****/
            val toiletDBRef = FirebaseDatabase.getInstance().getReference("ToiletMaster/$toiletID")
            toiletDBRef.child("lastCleanedTimeStamp").setValue(recordTime)
            toiletDBRef.child("lastCleanedBy").setValue(textJanitorName.text.toString())
/****** TODO: 10-Jan-2019: Kartik END *****/
            finish()
        }
    }
}
