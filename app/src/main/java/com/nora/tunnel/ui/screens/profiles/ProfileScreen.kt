package com.nora.tunnel.ui.screens.profiles

import androidx.compose.foundation.layout.*
import androidx.compose.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ProfileScreen() {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("My Profiles", style = MaterialTheme.typography.headlineSmall)
        OutlinedTextField(value="", onValueChange={}, label={Text("Search")}, modifier=Modifier.fillMaxWidth().padding(vertical=8.dp))
        LazyColumn {
            items(3) { 
                Card(Modifier.fillMaxWidth().padding(vertical=6.dp)) {
                    ListItem(headlineContent={Text("★ Singapore")}, supportingContent={Text("VLESS • Xray • gRPC")},
                        trailingContent={ Row{ TextButton(onClick={}){Text("TEST")}; Button(onClick={}){Text("Connect")} } })
                }
            }
        }
        Button(onClick={}, modifier=Modifier.fillMaxWidth()){ Text("+ Add Profile") }
        Text("Long press: Edit / Duplicate / Export / Delete", style=MaterialTheme.typography.bodySmall)
    }
}
