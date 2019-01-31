/* Created by Kartik Venkataraman, 4 Dec 2018 */
/* Rev 0.2.  Code Cleanup - Kartik Venkataraman 29 Jan 2019 */
/* Rev 0.22  Mapped string literals to STRINGS.XML - Kartik Venkataraman 31 Jan 2019 */
/*           No changes to FireBase, will handle that as a part of the class restructuring */

package com.example.cloo2019

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.google.firebase.database.FirebaseDatabase

class CleanActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clean)

        val toiletID = intent.getStringExtra(getString(R.string.intent_toilet_id))
        val toiletName = intent.getStringExtra(getString(R.string.intent_toilet_name))
        val janitorName = intent.getStringExtra(getString(R.string.intent_janitor))

        val textToiletName = findViewById<TextView>(R.id.textView_toiletName)
        textToiletName.text = toiletName

        val textJanitorName = findViewById<EditText>(R.id.editText_JanitorName)
        textJanitorName.setText(janitorName)

        val buttonCleaned = findViewById<Button>(R.id.button_Cleaned)
        val janitorComments = findViewById<EditText>(R.id.editTextOthers)
        val timeStamp = findViewById<TextView>(R.id.textViewCleanTimeStamp)
        timeStamp!!.text = CurrentTimeStamp().getPresentableTimeString(CurrentTimeStamp().getString())

        buttonCleaned.setOnClickListener {
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

            val fireDBRef = FirebaseDatabase.getInstance().getReference("ToiletClean/$toiletID")
            val cleanedRecordId = fireDBRef.push().key
            if (cleanedRecordId == null) {
                Toast.makeText(
                    this@CleanActivity,
                    getString(R.string.error_msg_clean_record_id_null),   // "Clean Report cannot be captured as push key returned null."
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val cleanedToilet = CleanedRecord(
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
                    janitorName
                )

                fireDBRef.child(cleanedRecordId).setValue(cleanedToilet).addOnCompleteListener {

                    // CleanToilet has been updated.  Now the rest of the records should be updated
                    // ToiletMaster needs to be updated with the timestamp of when it is cleaned and by whom, so that it can be
                    // shown in the landing page
                    // Similarly, ratings is reset to 0, However ratingLifeTime values are left as is.
                    val toiletDBRef = FirebaseDatabase.getInstance().getReference("ToiletMaster/$toiletID")
                    toiletDBRef.child("lastCleanedTimeStamp").setValue(recordTime)
                    toiletDBRef.child("lastCleanedBy").setValue(textJanitorName.text.toString())

                    toiletDBRef.child("ratingSum").setValue(0)       // value is reset to 0
                    toiletDBRef.child("numberOfRatings").setValue(0)    // value is reset to 0


                    Toast.makeText(this@CleanActivity, getString(R.string.status_clean_report_captured), // "Thank you! Clean report captured"
                        Toast.LENGTH_SHORT).show()
                }
            }

            finish()
        }
    }
}
