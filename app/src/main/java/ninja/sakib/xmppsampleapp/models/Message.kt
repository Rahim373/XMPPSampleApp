package ninja.sakib.xmppsampleapp.models

import kotlin.properties.Delegates

/**
 * := Coded with love by Sakib Sami on 8/8/16.
 * := s4kibs4mi@gmail.com
 * := www.sakib.ninja
 * := Coffee : Dream : Code
 */

class Message {
    var id: String by Delegates.notNull()
    var from: String by Delegates.notNull()
    var to: String by Delegates.notNull()
    var text: String by Delegates.notNull()
    var isMine: Boolean by Delegates.notNull()
}
