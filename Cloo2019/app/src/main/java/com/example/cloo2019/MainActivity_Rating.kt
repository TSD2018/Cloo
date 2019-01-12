/* Created by Kartik Venkataraman, 14 Nov 2018 */

package com.example.cloo2019

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.time.LocalDateTime

class MainActivity_Rating : AppCompatActivity() {

    lateinit var ratingBar: RatingBar
    private lateinit var editComments: EditText

    lateinit var toilet_id: String
    var masterRating: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main__rating)

        val LocationAddress = findViewById<TextView>(R.id.textView_location)
        ratingBar = findViewById(R.id.ratingBar2)
        editComments = findViewById(R.id.editText_comment)

        LocationAddress.text = intent.getStringExtra("TOILET_ADDRESS")
        toilet_id = intent.getStringExtra("TOILET_ID")
        masterRating = intent.getDoubleExtra("RATING", 0.0)

        if(ratingBar!=null){
            val buttonSubmit=findViewById<Button>(R.id.button_submit_close)
            buttonSubmit?.setOnClickListener {
                val msg = ratingBar.rating.toString()
                Toast.makeText(this@MainActivity_Rating, msg, Toast.LENGTH_SHORT).show()

                val msg_Comments = editComments.text


                saveUserRatings()
            }
        }
        else
            Toast.makeText(this@MainActivity_Rating, "ratingBar == null", Toast.LENGTH_SHORT).show()
    }

    private fun saveUserRatings(){

        val userComments: String
        val ChosenRating: Int
        val current = CurrentTimeStamp().getString()
        val userID: String = ""

        userComments = editComments.text.toString()
        ChosenRating = ratingBar.rating.toInt()

        val FireDBRef = FirebaseDatabase.getInstance().getReference("ToiletRating/$toilet_id")
//        val FireDBRef = FirebaseDatabase.getInstance().getReference("toilet_ratings")
        val userRatingId = FireDBRef.push().key

        if(userRatingId == null) {
            Toast.makeText(this@MainActivity_Rating, "push key returned null", Toast.LENGTH_SHORT).show()
            return
        }

        val asb = UserRating(userRatingId, toilet_id, userID, ChosenRating, userComments,current)

        FireDBRef.child(userRatingId).setValue(asb).addOnCompleteListener {
            Toast.makeText(this@MainActivity_Rating,"Thank you! Rating Saved", Toast.LENGTH_SHORT).show()
        }
        Toast.makeText(this@MainActivity_Rating,"Done!", Toast.LENGTH_SHORT).show()

        finish()
    }
}
