package ani.saikou.anilist

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ani.saikou.MainActivity
import ani.saikou.startMainActivity
import java.io.File
import androidx.browser.customtabs.CustomTabsIntent




var anilist : Anilist = Anilist()

class Anilist {
    var token : String? = null

    fun loginIntent(context: Context){
        val clientID = 6818

        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(context, Uri.parse("https://anilist.co/api/v2/oauth/authorize?client_id=$clientID&response_type=code"))
    }

    fun getSavedToken(context: Context):Boolean{
        if ("anilistToken" in context.fileList()){
            token = File(context.filesDir, "anilistToken").readText()
            return true
        }
        return false
    }
}

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val data: Uri? = intent?.data
        anilist.token = Regex("""(?<=access_token=).+(?=&token_type)""").find(data.toString())!!.value
        val filename = "anilistToken"
        this.openFileOutput(filename, Context.MODE_PRIVATE).use {
            it.write(anilist.token!!.toByteArray())
        }
        startMainActivity(this)
    }
}