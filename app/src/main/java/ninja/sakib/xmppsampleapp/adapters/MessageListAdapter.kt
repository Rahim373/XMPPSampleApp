package ninja.sakib.xmppsampleapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import ninja.sakib.xmppsampleapp.R
import ninja.sakib.xmppsampleapp.models.Message

/**
 * := Coded with love by Sakib Sami on 8/8/16.
 * := s4kibs4mi@gmail.com
 * := www.sakib.ninja
 * := Coffee : Dream : Code
 */

class MessageListAdapter(context: Context, messages: MutableList<Message>) : ArrayAdapter<Message>(context, R.layout.message_row) {
    private var messages: MutableList<Message> = messages

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val message: Message = messages[position]
        var cview: View = LayoutInflater.from(context).inflate(R.layout.message_row, parent, false)
        if (message.isMine) {
            var fromText: TextView = cview.findViewById(R.id.fromText) as TextView
            var toText: TextView = cview.findViewById(R.id.toText) as TextView

            toText.text = message.text
            fromText.visibility = View.GONE
        } else {
            var fromText: TextView = cview.findViewById(R.id.fromText) as TextView
            var toText: TextView = cview.findViewById(R.id.toText) as TextView

            fromText.text = message.text
            toText.visibility = View.GONE
        }
        return cview
    }

    fun addMessage(message: Message) {
        messages.add(message)
        add(message)
        notifyDataSetChanged()
    }
}
