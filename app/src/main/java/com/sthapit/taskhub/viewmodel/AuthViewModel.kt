import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _userId = MutableStateFlow<String?>(null)
    val userId: StateFlow<String?> = _userId.asStateFlow()

    private val _status = MutableStateFlow<String?>(null)
    val status: StateFlow<String?> = _status.asStateFlow()


    fun register(email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser

                val db = FirebaseFirestore.getInstance()

                user?.let {
                    val userData = hashMapOf("email" to it.email,"membershipStatus" to "0")
                    db.collection("users").document(it.uid).set(userData)
                        .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
                        .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
                }
                onSuccess()
            } else {
                onError(task.exception?.message ?: "An error occurred")
            }
        }
    }

    fun login(email: String, password: String, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val currentUserID = auth.currentUser?.uid
                _userId.value = currentUserID  // Update _userId upon successful login
                Log.d("Here", "User ID after login/register: ${_userId.value}")
                if (currentUserID != null) {
                    val db = FirebaseFirestore.getInstance()
                    db.collection("users").document(currentUserID).get()
                        .addOnSuccessListener { document ->
                            if (document.exists()) {
                                _status.value = document.getString("membershipStatus") ?: "0"
                                Log.d(TAG, "Membership status fetched: ${_status.value}")
                                onSuccess(_status.value!!)  // Call onSuccess only after all data is successfully fetched
                            } else {
                                Log.d(TAG, "No such document")
                                onError("No such user document.")
                            }
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error fetching user document", e)
                            onError("Failed to fetch user data.")
                        }
                } else {
                    onError("User ID is null after login.")
                }
            } else {
                onError(task.exception?.message ?: "Login failed")
            }
        }
    }
    fun logout() {
        auth.signOut()
        _userId.value = null // Clear the user ID upon logout
        Log.d("AuthViewModel", "User logged out")
    }

    fun changeMembershipStatus() {
        val user = auth.currentUser
        val db = FirebaseFirestore.getInstance()

        user?.let {
            val userData = mapOf("membershipStatus" to "1")  // Use mapOf for immutable map

            db.collection("users").document(it.uid).update(userData)
                .addOnSuccessListener {
                    Log.d(TAG, "Membership status successfully updated to true!")
                }
                .addOnFailureListener { e ->
                        Log.w(TAG, "Error updating membership status", e)
                }
        } ?: Log.w(TAG, "No user logged in to update membership status")
    }
}

