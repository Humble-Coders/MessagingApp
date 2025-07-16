package com.humblecoders.firebasemessaging
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MessagingApp()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MessagingApp() {
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
                        sendMessage(currentUser, messageText)
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
                    onClick = { fetchMessage(userName) { message ->
                        messages = messages + (userName to message)
                    }

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

    private fun sendMessage(senderName: String, message: String) {
        db.collection("messages").document(senderName)
            .set(mapOf("message" to message))
    }

    private fun fetchMessage(senderName: String, onMessageReceived: (String) -> Unit) {
        db.collection("messages")
            .document(senderName).get()
            .addOnSuccessListener { document ->
                val message = document.getString("message") ?: "No message found"
                onMessageReceived(message)
            }
    }
}