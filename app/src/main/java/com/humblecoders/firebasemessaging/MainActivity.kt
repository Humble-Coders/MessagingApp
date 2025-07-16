package com.humblecoders.firebasemessaging
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {
     val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MessagingApp(db)
        }
    }
}