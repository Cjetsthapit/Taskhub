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
//    var userId: String? = null

    private val _userId = MutableStateFlow<String?>(null)
    val userId: StateFlow<String?> = _userId.asStateFlow()


    fun register(email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser

                val db = FirebaseFirestore.getInstance()

                user?.let {
                    val userData = hashMapOf("email" to it.email)
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

    fun login(email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _userId.value = auth.currentUser?.uid
                Log.d("Here", "User ID after login/register: $userId")// Update _userId upon successful login
                if (userId != null) {
                    onSuccess()
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
}

