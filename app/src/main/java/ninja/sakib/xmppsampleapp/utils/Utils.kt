package ninja.sakib.xmppsampleapp.utils

import android.content.Context
import android.os.Build
import io.realm.RealmConfiguration
import org.jivesoftware.smack.ConnectionConfiguration
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration
import org.jivesoftware.smack.util.TLSUtils
import java.io.File
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager

/**
 * := Coded with love by Sakib Sami on 8/8/16.
 * := s4kibs4mi@gmail.com
 * := www.sakib.ninja
 * := Coffee : Dream : Code
 */

class Utils {
    companion object {
        fun getRealmConfiguration(context: Context): RealmConfiguration {
            return RealmConfiguration.Builder(context)
                    .name("config.realm")
                    .deleteRealmIfMigrationNeeded()
                    .schemaVersion(1)
                    .build()
        }

        fun getConnectionConfiguration(username: String, password: String, serverAddress: String, serverPort: Int): XMPPTCPConnectionConfiguration {
            val configuration = XMPPTCPConnectionConfiguration.builder()
            configuration.setHost(serverAddress)
            configuration.setPort(serverPort)
            configuration.setServiceName(serverAddress)
            configuration.setResource("xmpp-sample-app")
            configuration.setUsernameAndPassword(username + "@" + serverAddress, password)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                configuration.setKeystoreType("AndroidCAStore")
                configuration.setKeystorePath(null)
            } else {
                configuration.setKeystoreType("BKS")
                var path: String? = System.getProperty("javax.net.ssl.trustStore")
                if (path == null) {
                    path = System.getProperty("java.home") + File.separator + "etc" + File.separator + "security" + File.separator + "cacerts.bks"
                }
                configuration.setKeystorePath(path)
            }

            try {
                val sslContext = SSLContext.getInstance("TLS")
                sslContext.init(null, arrayOf<TrustManager>(TLSUtils.AcceptAllTrustManager()), null)
                configuration.setCustomSSLContext(sslContext)
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            } catch (e: KeyManagementException) {
                e.printStackTrace()
            }

            configuration.setSecurityMode(ConnectionConfiguration.SecurityMode.required)
            configuration.setCompressionEnabled(true)
            return configuration.build()
        }
    }
}
