package com.example.app_iliyan.view

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_iliyan.model.GroupChat
import com.example.app_iliyan.model.User
import com.example.app_iliyan.view.components.GroupChatListView
import com.example.app_iliyan.view.components.SnackbarManager.ScaffoldSnackbar

class HomeActivity : ComponentActivity() {

  @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val groupchatList: List<GroupChat> =
        listOf(
            GroupChat(
                "Group 1",
                listOf(
                    User("Username1", "email", "password"), User("User 2", "email", "password"))),
            GroupChat("Group 2", listOf()),
            GroupChat("Group 3", listOf()),
            GroupChat("Group 4", listOf()),
            GroupChat("Group 5", listOf()),
            GroupChat("Group 6", listOf()))

    setContent {
      ScaffoldSnackbar {
        val context = LocalContext.current
        HomeLayout(items = groupchatList)
      }
    }
  }
}

@SuppressLint("CoroutineCreationDuringComposition", "UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeLayout(items: List<GroupChat>) {
  var searchText by remember { mutableStateOf("") }
  var selectedTab by remember { mutableStateOf("Chat") }

  Scaffold(
      bottomBar = {
        NavigationBar {
          NavigationBarItem(
              icon = { Icon(Icons.Filled.CheckCircle, contentDescription = null) },
              label = { Text("Chat") },
              selected = selectedTab == "Chat",
              onClick = { selectedTab = "Chat" })
          NavigationBarItem(
              icon = { Icon(Icons.Filled.AccountBox, contentDescription = null) },
              label = { Text("Contacts") },
              selected = selectedTab == "Contacts",
              onClick = { selectedTab = "Contacts" })
          NavigationBarItem(
              icon = { Icon(Icons.Filled.Settings, contentDescription = null) },
              label = { Text("Settings") },
              selected = selectedTab == "Settings",
              onClick = { selectedTab = "Settings" })
        }
      },
      floatingActionButton = {
        FloatingActionButton(onClick = { /*TODO*/}, modifier = Modifier.padding(0.dp)) {
          Icon(Icons.Filled.Add, contentDescription = "Add")
        }
      }) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
          Text(
              text = selectedTab,
              color = Color.Black,
              style =
                  TextStyle(
                      fontSize = 25.sp,
                      fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold))
          Column(modifier = Modifier.fillMaxWidth().padding(25.dp)) {
            TextField(
                value = searchText,
                onValueChange = { newText -> searchText = newText },
                modifier = Modifier.fillMaxWidth().padding(16.dp).fillMaxWidth(0.8f),
                shape = RoundedCornerShape(22.dp),
                leadingIcon = {
                  Icon(imageVector = Icons.Default.Search, contentDescription = "Search here")
                },
                label = { Text("Search something...") },
                colors =
                    TextFieldDefaults.textFieldColors(
                        containerColor = Color(red = 234, green = 221, blue = 255),
                        cursorColor = Color.Black,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent),
                singleLine = true)
            Spacer(modifier = Modifier.height(16.dp))

            GroupChatListView(items)
          }
        }
      }
}

@Preview(showBackground = true)
@Composable
fun HomeLayoutPreview() {
  val groupchatList: List<GroupChat> =
      listOf(
          GroupChat("Group 1", listOf()),
          GroupChat("Group 2", listOf()),
          GroupChat("Group 3", listOf()),
          GroupChat("Group 4", listOf()),
          GroupChat("Group 5", listOf()),
          GroupChat("Group 6", listOf()))

  HomeLayout(items = groupchatList)
}
