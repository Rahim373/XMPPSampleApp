package ninja.sakib.xmppsampleapp.views

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import io.realm.Realm
import ninja.sakib.xmppsampleapp.R
import ninja.sakib.xmppsampleapp.adapters.MessageListAdapter
import ninja.sakib.xmppsampleapp.models.Config
import ninja.sakib.xmppsampleapp.utils.Utils
import org.jivesoftware.smack.AbstractXMPPConnection
import org.jivesoftware.smack.ConnectionListener
import org.jivesoftware.smack.StanzaListener
import org.jivesoftware.smack.XMPPConnection
import org.jivesoftware.smack.filter.MessageTypeFilter
import org.jivesoftware.smack.packet.Message
import org.jivesoftware.smack.packet.Stanza
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import kotlin.properties.Delegates

/**
 * := Coded with love by Sakib Sami on 8/8/16.
 * := s4kibs4mi@gmail.com
 * := www.sakib.ninja
 * := Coffee : Dream : Code
 */

class MainActivity : AppCompatActivity(), StanzaListener, ConnectionListener, View.OnClickListener {
    private var receiverAddress: EditText by Delegates.notNull()
    private var messageText: EditText by Delegates.notNull()
    private var sendBtn: Button by Delegates.notNull()
    private var messageList: ListView by Delegates.notNull()
    private var messageListAdapter: MessageListAdapter by Delegates.notNull()

    private var database: Realm by Delegates.notNull()
    private var connection: AbstractXMPPConnection by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        receiverAddress = findViewById(R.id.receiverAddress) as EditText
        messageText = findViewById(R.id.messageText) as EditText
        sendBtn = findViewById(R.id.sendBtn) as Button
        messageList = findViewById(R.id.messageList) as ListView

        messageListAdapter = MessageListAdapter(this, mutableListOf())
        messageList.adapter = messageListAdapter

        sendBtn.setOnClickListener(this)
        sendBtn.isEnabled = false

        database = Realm.getInstance(Utils.getRealmConfiguration(this))
        try {
            val config: Config = database.where(Config::class.java).findFirst()
            connection = XMPPTCPConnection(Utils.getConnectionConfiguration(config.getUsername(),
                    config.getPassword(), config.getServerAddress(), 5222))
            connection.addConnectionListener(this)
            connection.addAsyncStanzaListener(this, MessageTypeFilter.NORMAL_OR_CHAT_OR_HEADLINE)
        } catch (exception: Exception) {

        }

        ConnectionAsyncTask().execute()
    }

    override fun onClick(p0: View?) {
        if (receiverAddress.text.toString().isNotEmpty() && messageText.text.toString().isNotEmpty()) {
            try {
                val message: Message = Message()
                message.type = Message.Type.chat
                message.body = messageText.text.toString()
                message.to = receiverAddress.text.toString()
                connection.sendStanza(message)
                messageText.setText("")

                var m: ninja.sakib.xmppsampleapp.models.Message = ninja.sakib.xmppsampleapp.models.Message()
                m.text = message.body
                m.id = message.stanzaId
                m.to = message.to
                m.isMine = true

                messageListAdapter.addMessage(m)
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
        } else {
            Toast.makeText(this, "Empty Fields are not allowed", Toast.LENGTH_SHORT).show()
        }
    }

    override fun processPacket(packet: Stanza?) {
        Log.d("Packet", packet?.toXML().toString())

        runOnUiThread {
            val message: Message = packet as Message
            if (!message.body.isNullOrEmpty()) {
                var m: ninja.sakib.xmppsampleapp.models.Message = ninja.sakib.xmppsampleapp.models.Message()
                m.text = message.body
                m.id = message.stanzaId
                m.from = message.from
                m.to = message.to
                m.isMine = false

                messageListAdapter.addMessage(m)
            }
        }
    }

    override fun authenticated(connection: XMPPConnection?, resumed: Boolean) {
        runOnUiThread {
            sendBtn.isEnabled = true
            Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show()
        }
    }

    override fun connected(connection: XMPPConnection?) {
        this.connection.login()
    }

    override fun connectionClosed() {
        onDisconnect()
    }

    override fun connectionClosedOnError(e: Exception?) {
        onDisconnect()
    }

    override fun reconnectingIn(seconds: Int) {

    }

    override fun reconnectionFailed(e: Exception?) {

    }

    override fun reconnectionSuccessful() {

    }

    fun onDisconnect() {
        runOnUiThread {
            sendBtn.isEnabled = false
            Toast.makeText(this, "Disconnected", Toast.LENGTH_SHORT).show()
        }

        try {
            Thread.sleep(1000)
            connection.connect()
        } catch (exception: Exception) {

        }
    }

    override fun onPause() {
        finish()
    }

    override fun onBackPressed() {
        finish()
    }

    inner class ConnectionAsyncTask : AsyncTask<Unit, Unit, Unit>() {
        override fun doInBackground(vararg p0: Unit?) {
            connection.connect()
        }
    }
}
