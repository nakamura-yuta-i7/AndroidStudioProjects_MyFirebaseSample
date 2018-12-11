package com.nkmr.firebase

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    fun gotoNext(v: View) {
        startActivity(Intent(this, Main2Activity::class.java))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference()
        val nameRef = myRef.child("name")

        Log.d("koko", "test message")
        nameRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = dataSnapshot.getValue(String::class.java)
                Log.d("koko1", "Value is: " + value!!)
                textView2.text = "Realtime Database from value: ${value}"
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("koko2", "Failed to read value.", error.toException())
            }
        })
    }
}
