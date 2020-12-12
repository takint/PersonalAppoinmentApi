package specialtopic.ngoctinle.personalappointment

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.io.BufferedReader

class NetworkHelper {
    companion object {
        const val APP_ID = "appID"

        // Loopback ip address (127.0.0.0) will cause cannot connect issue
        // Considering the phone has its own loopback ip address
        var API_ENDPOINT_URL = "http://10.0.2.2:5000"
        private var config_file = "config.txt"

        @RequiresApi(Build.VERSION_CODES.M)
        fun isNetworkConnected(app: Application): Boolean {
            //1
            val connectivityManager =
                app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            //2
            val activeNetwork = connectivityManager.activeNetwork
            //3
            val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
            //4
            return networkCapabilities != null &&
                    networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        }

        fun getConfiguration(context: Context) {
            try {
                val configText = getConfigFromAssets(context, config_file)
                if (configText.isNotBlank()) {
                    API_ENDPOINT_URL = configText.trim()
                }
            } catch (e: Exception) {
                Log.d("error", e.message.toString())
            }
        }

        private fun getConfigFromAssets(context: Context, fileName: String): String {
            return context.assets.open(fileName).use {
                it.bufferedReader().use { bf: BufferedReader ->
                    bf.readText()
                }
            }
        }
    }
}