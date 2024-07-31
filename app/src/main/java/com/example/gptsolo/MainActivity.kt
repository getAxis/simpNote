package com.example.gptsolo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import com.example.gptsolo.ui.theme.GptsoloTheme
import com.example.gptsolo.ui.theme.Peach20
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                DailyPlannerApp()
            }
        }
    }
}

@Composable
fun DailyPlannerApp() {
    var inputText by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf(listOf<Note>()) }

    Scaffold(
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            NotesList(notes)
            Spacer(modifier = Modifier.weight(1f)) // Spacer для размещения полей ввода и кнопки внизу
            BottomInput(inputText, onInputChange = { newText -> inputText = newText }) {
                val currentDateTime =
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
                val newNote = Note(
                    text = inputText,
                    dateTime = currentDateTime
                )
                notes = notes + newNote
                inputText = ""
            }
        }
    }
}
//2
@Composable
fun BottomInput(value: String, onInputChange: (String) -> Unit, onPinClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onInputChange,
            modifier = Modifier
                .weight(1f)
                .padding(16.dp),
            singleLine = true,
            label = { Text("Print some text") }
        )
        Spacer(modifier = Modifier.width(1.dp))
        Button(
            onClick = onPinClick,
            modifier = Modifier
                .padding(3.dp)
        ) {
            Text("Pin")
        }
    }
}
//change

@Composable
fun NotesList(notes: List<Note>) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        notes.forEach { note ->
            NoteCard(note)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun NoteCard(note: Note) {
    Row(modifier = Modifier.padding(all = 8.dp)) {
        Spacer(modifier = Modifier.width(8.dp))
        Surface(
            shape = MaterialTheme.shapes.medium,
            shadowElevation = 3.dp, color = Peach20,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp)
        ) {
            Column {
                Text(
                    text = note.dateTime,
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(vertical = 10.dp)
                )

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = note.text,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

        }

    }
}

data class Note(
    val text: String,
    val dateTime: String
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    GptsoloTheme(
        content = content
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewDailyPlannerApp() {
    GptsoloTheme(){

        NoteCard(Note(dateTime = "231312",text = "dasdad"))
    }
}
