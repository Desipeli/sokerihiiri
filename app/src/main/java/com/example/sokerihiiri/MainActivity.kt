package com.example.sokerihiiri

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.sokerihiiri.ui.AppContent
import com.example.sokerihiiri.ui.theme.SokerihiiriTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SokerihiiriTheme {
//                val postNotificationPermission=
//                    rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
//                val sokerihiiriNotificationService= SokerihiiriNotificationService(this)
//                LaunchedEffect(key1 = true){
//                    if(!postNotificationPermission.status.isGranted) {
//                        postNotificationPermission.launchPermissionRequest()
//                    }
//                }
//                Button(onClick = {
//                    sokerihiiriNotificationService.showBasicNotification()
//                }) {
//
//                }
                AppContent()
            }
        }
    }
}