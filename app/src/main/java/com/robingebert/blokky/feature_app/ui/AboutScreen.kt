package com.robingebert.blokky.feature_app.ui

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Balance
import androidx.compose.material.icons.rounded.BugReport
import androidx.compose.material.icons.rounded.QrCode2
import androidx.compose.material.icons.rounded.SettingsEthernet
import androidx.compose.material.icons.rounded.Web
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.graphics.drawable.toBitmap
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity

import com.robingebert.blokky.R
import qrcode.QRCode
import java.io.ByteArrayOutputStream

@Composable
fun AboutScreen() {

    val context = LocalContext.current

    var showQrCodeDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    painterResource(R.drawable.ic_policy),
                    contentDescription = "App Logo",
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(verticalArrangement = Arrangement.Center, modifier = Modifier.weight(1f)) {
                    Text(text = "Blokky", style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = "Version: " + getVersionName(context),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                IconButton(
                    onClick = {
                        showQrCodeDialog = true
                    }
                ) {
                    Icon(
                        Icons.Rounded.QrCode2,
                        contentDescription = "Share App",
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(15.dp)),
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            HeroCard(
                icon = Icons.Rounded.BugReport,
                title = "Report Bug"
            ) {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:")
                    putExtra(Intent.EXTRA_EMAIL, arrayOf("blokky@robingebert.com"))
                    putExtra(Intent.EXTRA_SUBJECT, "Blokky Bug Report")
                }
                context.startActivity(intent)
            }

            HeroCard(
                icon = Icons.Rounded.Balance,
                title = "Licenses"
            ) {
                context.startActivity(Intent(context, OssLicensesMenuActivity::class.java))
            }

            HeroCard(
                icon = Icons.Rounded.Web,
                title = "Website"
            ) {
                val browserIntent =
                    Intent(Intent.ACTION_VIEW, Uri.parse("https://blokky.robingebert.com"))
                context.startActivity(browserIntent)
            }
            HeroCard(
                icon = Icons.Rounded.SettingsEthernet,
                title = "Code"
            ) {
                val browserIntent =
                    Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/ronjar/blokky"))
                context.startActivity(browserIntent)
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            )
        ) {
            Text(
                text = "This app was developed by me, Robin 😊.\nI have ADHD myself and I wanted to create an app that helps me stay focused (actually a friend asked me whether this is possible and as a good ADHD person, i was up for the challenge). \nI hope you enjoy it and have a productive time! 😉",
                modifier = Modifier.padding(12.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            )
        ) {
            Column (modifier = Modifier.padding(12.dp)){
                Text(
                    text = "Regarding the Accessibility Warnings: This App uses the Accessibility Service only to block reels. It doesn't even have the Internet Permission, which would be required to share your data. If you have any questions regarding Data Safety, feel free to contact me or look through the source code.",
                )
                Spacer(modifier = Modifier.height(4.dp))
                TextButton(
                    onClick = {context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://blokky.robingebert.com/sites/AboutAccessibilityServices")))}
                ) {
                    Text(text = "Learn More")
                }
            }
        }
    }

    if (showQrCodeDialog) {
        QrCodeDialog {
            showQrCodeDialog = false
        }
    }
}

fun getVersionName(context: Context): String {
    val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
    return pInfo?.versionName ?: "Unknown"
}

@Composable
fun HeroCard(icon: ImageVector, title: String, onClick: () -> Unit = {}) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        modifier = Modifier
            .padding(4.dp)
            .clip(RoundedCornerShape(15.dp))
            .clickable(
                onClick = onClick
            ),
        shape = RoundedCornerShape(15.dp),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(12.dp)
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(32.dp))
            Text(text = title, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun QrCodeDialog(
    onDismissRequest: () -> Unit
) {
    val context = LocalContext.current
    val logoBitmap = context.resources.getDrawable(R.drawable.ic_policy, context.theme).toBitmap()
    val stream = ByteArrayOutputStream()
    logoBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
    val logoQRCode = QRCode.ofRoundedSquares()
        .withBackgroundColor(android.graphics.Color.TRANSPARENT)
        .withColor(if (isSystemInDarkTheme()) android.graphics.Color.WHITE else android.graphics.Color.BLACK)
        .build("https://play.google.com/store/apps/details?id=com.robingebert.blokky")
    val logoQRCodePngData = logoQRCode.renderToBytes()

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Share Blokky (PlayStore):", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Image(
                    bitmap = BitmapFactory.decodeByteArray(
                        logoQRCodePngData,
                        0,
                        logoQRCodePngData.size
                    ).asImageBitmap(),
                    contentDescription = "Play Store Link",
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Thanks 😊", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Preview
@Composable
fun AboutScreenPreview() {
    AboutScreen()
}