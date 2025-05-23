import android.net.Uri
import android.widget.MediaController
import android.widget.VideoView
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun VideoIntroScreen() {
    val context = LocalContext.current

    AndroidView(
        factory = {
            VideoView(it).apply {
                setVideoURI(
                    Uri.parse("android.resource://${context.packageName}/raw/video_ayuda")
                )
                setMediaController(MediaController(it).apply {
                    setAnchorView(this@apply)
                })
                setOnPreparedListener {
                    it.start()
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
    )
}
