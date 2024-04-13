import android.content.Context
import androidx.core.content.edit

object MembershipManager {
    private const val PREF_NAME = "MembershipPrefs"
    private const val KEY_MEMBERSHIP_STATUS = "membershipStatus"
    private const val PREF_MEMBERSHIP_STATUS = "pref_membership_status"

    fun saveMembershipStatus(context: Context, hasMembership: Boolean) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit {
            putBoolean(KEY_MEMBERSHIP_STATUS, hasMembership)
        }
    }

    fun getMembershipStatus(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(KEY_MEMBERSHIP_STATUS, false)
    }

    fun clearMembershipStatus(context: Context) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit {
            remove(KEY_MEMBERSHIP_STATUS)
        }
    }
}
