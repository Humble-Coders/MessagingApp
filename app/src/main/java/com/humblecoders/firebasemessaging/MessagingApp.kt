package com.humblecoders.firebasemessaging

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.collections.plus

@Composable
fun MessagingApp(db: FirebaseFirestore) {
    var messageText by remember { mutableStateOf("") }
    var messages by remember { mutableStateOf(mapOf<String, String>()) }

    val users = listOf("Anmol", "Shivinder", "Vaibhavi", "Aadita", "Diya", "Keha", "Manasavi", "Manvi", "Molika", "Rishi", "Suhani")
    val currentUser = "Anmol" // Hardcoded sender name

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Message input
        OutlinedTextField(
            value = messageText,
            onValueChange = { messageText = it },
            label = { Text("Enter message") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                if (messageText.isNotBlank()) {
                    sendMessage(currentUser, messageText,db)
                    messageText = ""
                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Send")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // User cards
        users.forEach { userName ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                onClick = {
                    fetchMessage(userName=userName,db=db, onMessageReceived = { message ->
                        println("MessagingApp Message received for $userName: $message")
                        messages = messages + (userName to message)
                        println("MessagingApp Messages list $messages")
                    }
                    )
                }
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(text = userName)
                    Text(text = messages[userName] ?: "No message",)
                }
            }
        }
    }
}

 fun sendMessage(currentUser: String, message: String,db: FirebaseFirestore) {

    db.collection("messages")
        .document(currentUser)
        .set(mapOf("message" to message))

}

 fun fetchMessage(userName: String,db: FirebaseFirestore, onMessageReceived: (String) -> Unit) {
    db.collection("messages")
        .document(userName).get()

        .addOnSuccessListener { document ->
            val message = document.getString("message") ?: "No message found"
            onMessageReceived(message)
        }
}