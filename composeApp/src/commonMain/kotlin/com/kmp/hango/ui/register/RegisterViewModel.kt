package com.kmp.hango.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kmp.hango.toFirebaseFile
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.storage.storage
import io.github.vinceglb.filekit.core.FileKit
import io.github.vinceglb.filekit.core.PickerMode
import io.github.vinceglb.filekit.core.PickerType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    var uiState = _uiState.asStateFlow()
    private val firebaseAuth: FirebaseAuth = Firebase.auth
    private val firebaseStorage = Firebase.storage

    fun updateUsername(value: String) {
        _uiState.value = uiState.value.copy(userName = value)
    }

    fun updatePassword(value: String) {
        _uiState.value = uiState.value.copy(password = value)
    }

    fun login() {
        val username = uiState.value.userName
        val password = uiState.value.password


        viewModelScope.launch {
            try {
                val authResult = firebaseAuth.signInWithEmailAndPassword(
                    username,
                    password
                )

                if (authResult.user != null) {
                    _uiState.value = uiState.value.copy(
                        goToInit = true,
                        loginError = false,
                    )
                }
            } catch (e: Exception) {
                _uiState.value = uiState.value.copy(loginError = true)
            }
        }
    }

    fun selectImage() {
        viewModelScope.launch {
            FileKit.pickFile(
                type = PickerType.Image,
                mode = PickerMode.Single,
                title = "Selecione a imagem de perfil",
            ).let { image ->
                _uiState.value = uiState.value.copy(
                    imageProfile = image,
                    imageByteArray = image?.readBytes()
                )
            }
        }
    }

    fun register() {
        sendImage()
    }

    fun sendImage() {
        viewModelScope.launch {
            print("Sending image: ${uiState.value.imageProfile}")
            uiState.value.imageProfile?.let {
                firebaseStorage.reference.child("images/mountains.jpg").putFile(it.toFirebaseFile())
            }
        }
    }
}