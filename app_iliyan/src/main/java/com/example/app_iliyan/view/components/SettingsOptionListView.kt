package com.example.app_iliyan.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
        modifier = Modifier.padding(0.dp).fillMaxSize().padding(8.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        colors =
            CardDefaults.cardColors(containerColor = Color.White, contentColor = Color.LightGray),
        shape = RoundedCornerShape(8.dp)) {
          Column(
              modifier = Modifier.fillMaxSize(),
              horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(100.dp))

                Image(
                    painter = painterResource(id = R.drawable.user),
                    contentDescription = item?.username,
                    modifier = Modifier.size(100.dp, 100.dp))

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Username: " + item?.username,
                    style =
                        TextStyle(
                            fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black),
                    modifier = Modifier.align(Alignment.CenterHorizontally))

                Text(
                    text = "Edit",
                    style = TextStyle(fontSize = 18.sp, color = Color.Blue),
                    modifier = Modifier.align(Alignment.CenterHorizontally))

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Email: " + item?.email,
                    style =
                        TextStyle(
                            fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black),
                    modifier = Modifier.align(Alignment.CenterHorizontally))

                Text(
                    text = "Edit",
                    style = TextStyle(fontSize = 18.sp, color = Color.Blue),
                    modifier = Modifier.align(Alignment.CenterHorizontally))

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Edit password",
                    style = TextStyle(fontSize = 18.sp, color = Color.Blue),
                    modifier = Modifier.align(Alignment.CenterHorizontally))

                Spacer(modifier = Modifier.height(80.dp))

                Button(
                    onClick = { /*TODO*/},
                    modifier = Modifier.fillMaxWidth(0.5f).align(Alignment.CenterHorizontally)) {
                      Text("Log out")
                    }

                Button(
                    onClick = { /*TODO*/},
                    modifier = Modifier.fillMaxWidth(0.5f).align(Alignment.CenterHorizontally),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) {
                      Text("Delete account")
                    }
              }
        }
  }
}
