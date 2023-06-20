package ar.edu.ort.tp3.firebasetutorial.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel: ViewModel() {
    val mail = MutableLiveData<String>()
    val pass = MutableLiveData<String>()


    fun guardarCredenciales(email: String, password: String) {
        mail.value = email
        pass.value = password
    }
}