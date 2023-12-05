package com.example.app_iliyan.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_iliyan.R
import com.example.app_iliyan.model.LocalData

@Composable
fun SettingsOptionListView() {
  val item = LocalData.getAuthenticatedUser()

  Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
    Card(
        modifier =
            Modifier.padding(0.dp)
                // .width(250.dp)
                // .clickable {  }
                .fillMaxSize()
                .padding(8.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)) {
          Column {
            Image(
                painter = painterResource(id = R.drawable.user),
                contentDescription = item?.username,
                alignment = Alignment.CenterStart,
                modifier = Modifier.size(70.dp, 70.dp))

            Spacer(modifier = Modifier.width(16.dp))
            Column {
              Row(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = "Username: " + item?.username,
                    style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold))

                Text(text = "Edit", style = TextStyle(fontSize = 18.sp, color = Color.Blue))
              }
              Spacer(modifier = Modifier.height(8.dp))

              Row(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = "Email: " + item?.email,
                    style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold))

                Text(text = "Edit", style = TextStyle(fontSize = 18.sp, color = Color.Blue))
              }
              Spacer(modifier = Modifier.height(80.dp))

              Button(onClick = { /*TODO*/}, modifier = Modifier.fillMaxWidth()) { Text("Log out") }

              Button(
                  onClick = { /*TODO*/},
                  modifier = Modifier.fillMaxWidth(),
                  colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) {
                    Text("Delete account")
                  }
            }
          }
        }
  }
}
