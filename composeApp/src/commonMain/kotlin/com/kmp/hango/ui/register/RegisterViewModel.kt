package com.kmp.hango.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kmp.hango.model.User
import com.kmp.hango.network.firebase.UserFireStore
import com.kmp.hango.toFirebaseFile
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.storage.storage
import io.github.vinceglb.filekit.core.FileKit
import io.github.vinceglb.filekit.core.PickerMode
import io.github.vinceglb.filekit.core.PickerType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class RegisterViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    var uiState = _uiState.asStateFlow()
    private val firebaseAuth: FirebaseAuth = Firebase.auth
    private val firebaseStorage = Firebase.storage
    private var userFirestore = UserFireStore()

    fun updateUsername(value: String) {
        _uiState.value = uiState.value.copy(userName = value)
    }

    fun updateEmail(value: String) {
        _uiState.value = uiState.value.copy(email = value)
    }

    fun updatePassword(value: String) {
        _uiState.value = uiState.value.copy(password = value)
    }

    fun updateConfirmPassword(value: String) {
        _uiState.value = uiState.value.copy(confirmPassword = value)
    }

    fun selectImage() {
        viewModelScope.launch {
            FileKit.pickFile(
                type = PickerType.Image,
                mode = PickerMode.Single,
                title = "Selecione a imagem de perfil",
            ).let { image ->
                _uiState.value = uiState.value.copy(
                    imageByteArray = image?.readBytes(),
                    imageProfile = image
                )
            }
        }
    }

    fun register() {
        val userName = uiState.value.userName
        val email = uiState.value.email.trim()
        val password = uiState.value.password
        val confirmPassword = uiState.value.confirmPassword

        if (userName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            _uiState.value = uiState.value.copy(
                loginError = true,
                loginErrorMessage = "Preencha todos os campos"
            )
            return
        }

        if (password.length < 6) {
            _uiState.value = uiState.value.copy(
                loginError = true,
                loginErrorMessage = "A senha deve ter no mínimo 6 caracteres"
            )
            return
        }

        if (password != confirmPassword) {
            _uiState.value =
                uiState.value.copy(loginError = true, loginErrorMessage = "As senhas não coincidem")
            return
        }

        viewModelScope.launch {
            _uiState.value = uiState.value.copy(load = true)
            try {
                val authResult = firebaseAuth.createUserWithEmailAndPassword(
                    email,
                    password
                )

                authResult.user?.let { user ->
                    createUser(user)
                } ?: run {
                    _uiState.value = uiState.value.copy(
                        loginError = true,
                        loginErrorMessage = "Erro ao criar conta, tente novamente"
                    )
                }
            } catch (e: Exception) {
                val errorMessage = if (e.message?.contains("email address is already") == true) {
                    "Email já cadastrado"
                } else {
                    "Erro ao criar conta, tente novamente"
                }
                _uiState.value = uiState.value.copy(
                    loginError = true,
                    loginErrorMessage = errorMessage
                )
                println("Erro ao criar conta: ${e.message}")
            } finally {
                _uiState.value = uiState.value.copy(load = false)
            }
        }
    }

    private fun createUser(user: FirebaseUser) {
        viewModelScope.launch {
            _uiState.value = uiState.value.copy(load = true)

            try {
                uiState.value.imageProfile?.let {
                    firebaseStorage.reference.child("images/users/${user.uid}/profile.jpg")
                        .putFile(it.toFirebaseFile())
                }

                val imageByteArray = uiState.value.imageByteArray
                val imageUrlPath = if (imageByteArray != null) {
                    firebaseStorage.reference.child("images/users/${user.uid}/profile.jpg")
                        .getDownloadUrl()
                } else {
                    "https://raw.githubusercontent.com/git-jr/sample-files/refs/heads/main/profile%20pics/profile_pic_emoji_1.png"
                }

                userFirestore.saveUser(
                    user = User(
                        id = user.uid,
                        name = _uiState.value.userName,
                        imageProfileUrl = imageUrlPath,
                        score = 0,
                    ),
                    onSuccess = {
                        _uiState.value = uiState.value.copy(
                            goToInit = true,
                            loginError = false,
                        )
                    },
                    onError = {
                        _uiState.value = uiState.value.copy(
                            loginError = true,
                            loginErrorMessage = "Erro ao salvar usuário, tente novamente"
                        )
                    }
                )
            } catch (e: Exception) {
                _uiState.value = uiState.value.copy(
                    loginError = true,
                    loginErrorMessage = "Erro ao enviar imagem, tente novamente",
                    loginErrorMessageDetail = e.message.toString()
                )
            }
            finally {
                _uiState.value = uiState.value.copy(load = false)
            }
        }
    }
}