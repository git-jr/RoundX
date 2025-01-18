package com.kmp.hango.network.firebase


import com.kmp.hango.model.User
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.database.DataSnapshot
import dev.gitlive.firebase.database.DatabaseReference
import dev.gitlive.firebase.database.database
import kotlinx.coroutines.flow.first


class UserFireStore {
    private val firebaseRealtimeDatabase = Firebase.database
    private val dbUsers = firebaseRealtimeDatabase.reference("users")

    suspend fun saveUser(
        user: User,
        onSuccess: () -> Unit = {},
        onError: () -> Unit = {},
    ) {
        val usersRef: DatabaseReference = dbUsers.child(user.id)

        try {
            usersRef.setValue(user)
            println("Usuário salvo com sucesso")
            onSuccess()
        } catch (e: Exception) {
            println("Erro ao salvar usuário: ${e.message}")
            onError()
        }
    }


    suspend fun getUsersOrderedByScore(
        onSuccess: (List<User>) -> Unit,
        onError: (String) -> Unit = {},
    ) {
        try {
            val snapshot: DataSnapshot = dbUsers.orderByChild("score").valueEvents.first()
            val userList = mutableListOf<User>()

            for (userSnapshot in snapshot.children) {
                val user = userSnapshot.value<User>()
                user.let {
                    userList.add(it)
                }
            }
            println("Firebase getUsersOrderedByScore: Usuários obtidos com sucesso")
            // Ordene a lista pelo score em ordem descendente (maior score primeiro)
            userList.sortByDescending { it.score.toInt() }

            onSuccess(userList)

        } catch (e: Exception) {
            println("Firebase getUsersOrderedByScore: Erro ao obter usuários: ${e.message}")
            onError(e.message ?: "Erro ao obter usuários")
        }

    }


    suspend fun updateScoreForUser(
        userId: String,
        score: Int,
        onSuccess: () -> Unit = {},
        onError: () -> Unit = {},
    ) {
        val userRef = dbUsers.child(userId)

        // Crie um mapa que contém apenas o campo "score" a ser atualizado
        val scoreUpdates = hashMapOf<String, Any>("score" to score)

        try {
            // Atualize o campo "score" do usuário com o novo valor
            userRef.updateChildren(scoreUpdates)
            onSuccess()
        } catch (e: Exception) {
            println("Erro ao atualizar score: ${e.message}")
            onError()
        }
    }

    suspend fun getUserById(userId: String, onSuccess: (User) -> Unit, onError: () -> Unit) {
        val userRef = dbUsers.child(userId)

        try {
            userRef.valueEvents.first().let { snapshot ->
                val user = snapshot.value<User>()
                println("Usuário obtido com sucesso")
                onSuccess(user)
            }
        } catch (e: Exception) {
            println("Erro ao obter usuário: ${e.message}")
            onError()
        }
    }
}