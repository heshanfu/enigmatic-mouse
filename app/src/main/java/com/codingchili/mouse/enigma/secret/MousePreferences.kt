package com.codingchili.mouse.enigma.secret

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import java.util.*

private const val TEE_GEN : String = "TEE_GEN"
private const val TEE_IV : String = "TEE_IV"
private const val MASTER_SALT : String = "MASTER_SALT"
private const val fileName = "mouse.prefs"

class MousePreferences(application: Application) {
    private var preferences : SharedPreferences =
            application.getSharedPreferences(fileName, MODE_PRIVATE)

    fun getTeeIv(): ByteArray {
        val iv: String = preferences.getString(TEE_IV, "")!!

        if (!iv.isBlank()) {
            return Base64.getDecoder().decode(iv)
        } else {
            throw Error("No IV is present.")
        }
    }

    fun getMasterSalt(): ByteArray {
        val salt: String = preferences.getString(MASTER_SALT, "")!!

        if (!salt.isBlank()) {
            return Base64.getDecoder().decode(salt)
        } else {
            throw Error("No master salt is present.")
        }
    }

    fun isTeeGenerated(): Boolean {
        return preferences.getBoolean(TEE_GEN, false)
    }

    fun setTeeIV(iv: ByteArray) {
        preferences.edit()
                .putString(TEE_IV, Base64.getEncoder().encodeToString(iv))
                .apply()
    }

    fun setMasterSalt(salt: ByteArray) {
        preferences.edit()
                .putString(MASTER_SALT, Base64.getEncoder().encodeToString(salt))
                .apply()
    }

    fun setTeeGenerated() {
        preferences.edit()
                .putBoolean(TEE_GEN, true)
                .apply()
    }

    fun unsetTeeGenerated() {
        preferences.edit()
                .putBoolean(TEE_GEN, false)
                .apply()
    }
}