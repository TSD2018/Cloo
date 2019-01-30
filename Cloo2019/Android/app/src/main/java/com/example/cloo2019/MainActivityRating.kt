/* Created by Kartik Venkataraman, 14 Nov 2018 */
/* Rev 0.2.  Code Cleanup - Kartik Venkataraman 29 Jan 2019 */

package com.example.cloo2019

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.google.firebase.database.FirebaseDatabase

class MainActivityRating : AppCompatActivity() {

    private lateinit var ratingBar: RatingBar
    private lateinit var editComments: EditText

    lateinit var toiletId: String
    var masterRating: Double = 0.0
    private var ratingSum: Int = 0
    private var numberOfRatings: Int = 0
    private var ratingSumLifeTime: Int = 0
    private var numberOfRatingsLifeTime: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main__rating)

        val LocationAddress = findViewById<TextView>(R.id.textView_toiletName)
        ratingBar = findViewById(R.id.ratingBar2)
        editComments = findViewById(R.id.editText_comment)
        LocationAddress.text = intent.getStringExtra("TOILET_ADDRESS")
        toiletId = intent.getStringExtra("TOILET_ID")
        masterRating = intent.getDoubleExtra("RATING", 0.0)
        ratingSum = intent.getIntExtra("RATING_SUM", 0)
        numberOfRatings = intent.getIntExtra("NUMBER_OF_RATINGS", 0)
        ratingSumLifeTime = intent.getIntExtra("RATING_SUM_LIFETIME", 0)
        numberOfRatingsLifeTime = intent.getIntExtra("NUMBER_OF_RATINGS_LIFETIME", 0)

//        if(ratingBar!=null){
            val buttonSubmit=findViewById<Button>(R.id.button_submit_close)
            buttonSubmit?.setOnClickListener {
                val msg = ratingBar.rating.toString()
                Toast.makeText(this@MainActivityRating, msg, Toast.LENGTH_SHORT).show()
                saveUserRatings()
            }
//        }
//        else
//            Toast.makeText(this@MainActivityRating, "ratingBar == null", Toast.LENGTH_SHORT).show()
    }

    private fun saveUserRatings(){

        val userComments: String
        val ChosenRating: Int
        val current = CurrentTimeStamp().getString()
        val userID  = ""

        userComments = editComments.text.toString()
        ChosenRating = ratingBar.rating.toInt()

        val FireDBRef = FirebaseDatabase.getInstance().getReference("ToiletRating/$toiletId")
        val userRatingId = FireDBRef.push().key

        if(userRatingId == null) {
            Toast.makeText(this@MainActivityRating, "Toilet Rating push key returned null", Toast.LENGTH_SHORT).show()
            return
        }

        val userToiletRating = UserRating(userRatingId, toiletId, userID, ChosenRating, userComments,current)

        FireDBRef.child(userRatingId).setValue(userToiletRating).addOnCompleteListener {

            // Also update ToiletMaster with Ratings

            val toiletDBRef = FirebaseDatabase.getInstance().getReference("ToiletMaster/$toiletId")

            ratingSum += ChosenRating
            numberOfRatings += 1

            toiletDBRef.child("ratingSum").setValue(ratingSum)
            toiletDBRef.child("numberOfRatings").setValue(numberOfRatings)

            ratingSumLifeTime += ChosenRating
            numberOfRatingsLifeTime += 1

            toiletDBRef.child("ratingSumLifeTime").setValue(ratingSumLifeTime)
            toiletDBRef.child("numberOfRatingsLifeTime").setValue(numberOfRatingsLifeTime)
            Toast.makeText(this@MainActivityRating,"Thank you! Appreciate your Rating.", Toast.LENGTH_SHORT).show()
        }
//        Toast.makeText(this@MainActivityRating,"Done!", Toast.LENGTH_SHORT).show()

        finish()
    }
}
