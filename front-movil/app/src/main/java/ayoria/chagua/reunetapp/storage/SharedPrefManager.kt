package ayoria.chagua.reunetapp.storage

import android.content.Context
import ayoria.chagua.reunetapp.models.User


class SharedPrefManager private constructor(private val mCtx: Context) {

    val isLoggedIn: Boolean
        get() {
            val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getInt("id", -1) != -1
        }

    val user: User
        get() {
            val sharedPreferences =  mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return User(
                sharedPreferences.getInt("id", -1),
                sharedPreferences.getString("email", null).toString(),
                sharedPreferences.getString("avatar", null).toString(),
                sharedPreferences.getString("firstname", null).toString(),
                sharedPreferences.getString("lastname", null).toString(),
                sharedPreferences.getString("role", null).toString(),
                sharedPreferences.getString("token", null).toString()
            )
        }

    fun saveUser(user: User) {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("avatar", user.avatar)
        editor.putString("email", user.email)
        editor.putString("firstname", user.firstname)
        editor.putInt("id", user.id)
        editor.putString("lastname", user.lastname)
        editor.putString("role", user.role)
        editor.putString("token", user.token)
        editor.apply()
    }


    fun clear() {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    companion object {
        private const val SHARED_PREF_NAME = "my_shared_preff"
        private var mInstance: SharedPrefManager? = null
        @Synchronized
        fun getInstance(mCtx: Context): SharedPrefManager{
            if (mInstance == null) {
                mInstance = SharedPrefManager(mCtx)
            }
            return mInstance as SharedPrefManager
        }
    }
}