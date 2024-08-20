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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.Alignment
import java.text.SimpleDateFormat
import android.content.Context
import java.util.*
import androidx.compose.ui.graphics.Color


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                DailyPlannerApp(applicationContext)
            }
        }
    }
}

@Composable
fun DailyPlannerApp(context: Context) {
    var inputText by remember { mutableStateOf("") }
    var listnotes by remember { mutableStateOf(PreferencesHelper.getNotes(context)) }

    fun removeNote(note: Note) {
        listnotes = listnotes.toMutableList().also { it.remove(note) }
        PreferencesHelper.saveNotes(context,listnotes)
    }
    Scaffold(
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()   // гарантирует, что Box займет всё пространство
        ){
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                NotesList(notes = listnotes, onDelete = {note -> removeNote(note)})
                //Spacer(modifier = Modifier.weight(1f)) // Spacer для размещения полей ввода и кнопки внизу

            }
            Column (modifier =  Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
            ){
                BottomInput(inputText, onInputChange = { newText -> inputText = newText }) {
                    val currentDateTime =
                        SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())
                    if (inputText != "")
                    {
                        val newNote = Note(
                            text = inputText,
                            dateTime = currentDateTime
                        )
                        listnotes = listnotes + newNote
                        inputText = ""
                        PreferencesHelper.saveNotes(context, listnotes)
                    }
                }
            }
        }


    }
}

//2
@OptIn(ExperimentalMaterial3Api::class)
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
            label = { Text("Print some text") },
            colors = OutlinedTextFieldDefaults.colors(
                 focusedBorderColor = Peach20)
        )
        Spacer(modifier = Modifier.width(1.dp))
        Button(
            modifier = Modifier
                .padding(3.dp),
            onClick = onPinClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Peach20)
        ) {
            Text("Pin")
        }
    }
}
//change

@Composable
fun NotesList(notes: List<Note>, onDelete: (Note) -> Unit) {
    var count = 1
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        val leftColumNotes = mutableListOf<Note>()
        val rightColumNotes = mutableListOf<Note>()

        notes.forEachIndexed  { index, note ->
            if(index %2 ==0){
                leftColumNotes.add(note)
                count ++
            }
            else {
                rightColumNotes.add(note)
                count ++
            }

        }
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
            ) {
                leftColumNotes.forEach { note ->
                    NoteCard(note, onDelete = { onDelete(note) })

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
            ) {
                rightColumNotes.forEach { note ->
                    NoteCard(note, onDelete = { onDelete(note) })
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }

}

@Composable
fun NoteCard(note: Note, onDelete:() -> Unit) {
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

            Box() {
                // Вложенные элементы
                Button(
                    onClick = {onDelete()},
                    modifier = Modifier
                        .align(Alignment.TopEnd)  // Размещаем в правом верхнем углу
                        .padding(3.dp),  // Отступы от краев контейнера
                    colors = ButtonDefaults. buttonColors(
                        containerColor = Color.Transparent
                    )
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.trashcon),
                        contentDescription = "Remote note",
                        modifier = Modifier
                            .size(24.dp)

                    )
                }
            }
        }

    }
}

data class Note(val id: Int = 0,val text: String,val dateTime: String)

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

    }
}
