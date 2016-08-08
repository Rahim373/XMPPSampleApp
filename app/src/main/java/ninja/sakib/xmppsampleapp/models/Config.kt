package ninja.sakib.xmppsampleapp.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * := Coded with love by Sakib Sami on 8/8/16.
 * := s4kibs4mi@gmail.com
 * := www.sakib.ninja
 * := Coffee : Dream : Code
 */

open class Config() : RealmObject() {
    @PrimaryKey
    private var username: String = ""
    private var password: String = ""
    private var serverAddress: String = ""
    private var isPlainAuthentication: Int = 0

    fun setUsername(username: String) {
        this.username = username
    }

    fun getUsername(): String {
        return username
    }

    fun setPassword(password: String) {
        this.password = password
    }

    fun getPassword(): String {
        return password
    }

    fun setServerAddress(serverAddress: String) {
        this.serverAddress = serverAddress
    }

    fun getServerAddress(): String {
        return serverAddress
    }

    fun setIsPLainAuthentication(isPlainAuthentication: Int) {
        this.isPlainAuthentication = isPlainAuthentication
    }

    fun isPlainAuthentication(): Int {
        return isPlainAuthentication
    }
}
