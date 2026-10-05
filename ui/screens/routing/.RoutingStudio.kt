package com.nora.tunnel.ui.screens.routing

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RoutingStudio() {
    var mode by remember { mutableStateOf("Rule Based") }
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Routing Studio", style=MaterialTheme.typography.headlineSmall)
        SingleChoiceSegmentedButtonRow(Modifier.padding(vertical=12.dp)) {
            listOf("Global","Direct","Proxy","Block","Rule Based").forEachIndexed { i, label ->
                SegmentedButton(selected = mode==label, onClick={mode=label}, shape=SegmentedButtonDefaults.itemShape(i,5)) { Text(label, maxLines=1) }
            }
        }
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("Domain example.com → Proxy")
                Text("internal.example → Direct")
                Text("GeoIP CN → Direct", style=MaterialTheme.typography.bodySmall)
                Row(Modifier.padding(top=12.dp)) {
                    OutlinedButton(onClick={}){Text("Add rule")}
                    Spacer(Modifier.width(8.dp))
                    Button(onClick={}){Text("Import/export")}
                }
            }
        }
        Text("Application Routing: All / Selected / Excluded - Uses VpnService.addAllowedApplication()", style=MaterialTheme.typography.bodySmall, modifier=Modifier.padding(top=12.dp))
    }
}
