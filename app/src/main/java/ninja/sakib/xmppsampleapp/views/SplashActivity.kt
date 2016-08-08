package ninja.sakib.xmppsampleapp.views

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import io.realm.Realm
import ninja.sakib.xmppsampleapp.R
import ninja.sakib.xmppsampleapp.models.Config
import ninja.sakib.xmppsampleapp.utils.Utils
import kotlin.properties.Delegates

/**
 * := Coded with love by Sakib Sami on 8/8/16.
 * := s4kibs4mi@gmail.com
 * := www.sakib.ninja
 * := Coffee : Dream : Code
 */

class SplashActivity : AppCompatActivity(), View.OnClickListener {
    private var userName: EditText by Delegates.notNull()
    private var userPassword: EditText by Delegates.notNull()
    private var serverAddress: EditText by Delegates.notNull()
    private var isPlainAuthentication: CheckBox by Delegates.notNull()
    private var saveBtn: Button by Delegates.notNull()

    private var database: Realm by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        userName = findViewById(R.id.userName) as EditText
        userPassword = findViewById(R.id.userPassword) as EditText
        serverAddress = findViewById(R.id.serverAddress) as EditText
        isPlainAuthentication = findViewById(R.id.isPlainAuthentication) as CheckBox
        saveBtn = findViewById(R.id.saveDataBtn) as Button

        database = Realm.getInstance(Utils.getRealmConfiguration(this))

        try {
            val oldConfig: Config = database.where(Config::class.java).findFirst()
            userName.setText(oldConfig.getUsername())
            userPassword.setText(oldConfig.getPassword())
            serverAddress.setText(oldConfig.getServerAddress())
            if (oldConfig.isPlainAuthentication() == 1)
                isPlainAuthentication.isEnabled = true

        } catch (exception: Exception) {

        }

        saveBtn.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        if (!TextUtils.isEmpty(userName.text.toString()) && !TextUtils.isEmpty(userPassword.text.toString())
                && !TextUtils.isEmpty(serverAddress.text.toString())) {

            database.beginTransaction()

            var config: Config = Config()
            config.setUsername(userName.text.toString())
            config.setPassword(userPassword.text.toString())
            config.setServerAddress(serverAddress.text.toString())
            if (isPlainAuthentication.isChecked)
                config.setIsPLainAuthentication(1)
            else config.setIsPLainAuthentication(0)

            database.insertOrUpdate(config)
            database.commitTransaction()

            Toast.makeText(this, "Data Saved", Toast.LENGTH_LONG).show()

            val intent: Intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Empty Fields are not allowed !!!", Toast.LENGTH_SHORT).show()
        }
    }
}
