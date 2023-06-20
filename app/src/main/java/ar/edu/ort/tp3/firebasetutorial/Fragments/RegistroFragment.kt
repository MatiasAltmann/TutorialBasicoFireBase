package ar.edu.ort.tp3.firebasetutorial.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import ar.edu.ort.tp3.firebasetutorial.R
import ar.edu.ort.tp3.firebasetutorial.ViewModel.LoginViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class RegistroFragment : Fragment() {
    lateinit var v:View
    lateinit var mail:EditText
    lateinit var pass1:EditText
    lateinit var pass2:EditText
    lateinit var btnRegistro: Button
    lateinit var viewModel:LoginViewModel
    //---Variables de fireBase
    private lateinit var fireBaseAuth: FirebaseAuth
  //  private lateinit var authStateListener: FirebaseAuth.AuthStateListener
    //---

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fireBaseAuth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_registro, container, false)

        mail = v.findViewById(R.id.mail_registro1)
        pass1 = v.findViewById(R.id.pass_registro1)
        pass2 = v.findViewById(R.id.pass_registro2)
        btnRegistro =  v.findViewById(R.id.button_registro_id)
        viewModel = ViewModelProvider(requireActivity()).get(LoginViewModel::class.java)

        btnRegistro.setOnClickListener {
            if(mail.text.toString().length> 2 && pass1.text.toString().length > 2  && pass2.text.toString().length > 2) {
                if(pass1.text.toString().equals(pass2.text.toString())) {
                    //createAccount(mail.text.toString(), pass1.text.toString())
                    createAccountConMailDeCofirmacion(mail.text.toString(), pass1.text.toString())
                }else {
                    Toast.makeText(this.context, "Error, las claves no son iguales", Toast.LENGTH_SHORT).show()
                    pass1.requestFocus()
                    pass2.requestFocus()
                }
            }else {
                Toast.makeText(this.context, "Error, los campos no cumplen con criterios de longitud mínima", Toast.LENGTH_SHORT).show()
                println(mail.text.toString())
                println(pass1.text.toString().toString())
                println(pass2.text.toString().toString())
            }

        }

        return v
    }

    private fun createAccount(mail:String, pass:String) {
        fireBaseAuth.createUserWithEmailAndPassword(mail, pass)
            .addOnCompleteListener(this.requireActivity()){task ->
                if(task.isSuccessful) {
                    val user = fireBaseAuth.currentUser //es el usuario actual
                  Toast.makeText(this.context, "Cuenta creada", Toast.LENGTH_SHORT).show()
                    println("x ahora no hay error")
                   val action =  RegistroFragmentDirections.actionRegistroFragmentToLoginFragment()
                   v.findNavController().navigate(action)
                }else {
                    Toast.makeText(this.context,"Algo salió mal" + task.exception,Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun createAccountConMailDeCofirmacion(mail:String, pass:String) {
        fireBaseAuth.createUserWithEmailAndPassword(mail, pass)
            .addOnCompleteListener(this.requireActivity()){task ->
                if(task.isSuccessful) {
                    sentEmailVerification()
                    Toast.makeText(this.context, "Cuenta creada correctamente. Se requiere verificación", Toast.LENGTH_SHORT).show()
                    println("x ahora no hay error")
                    val action =  RegistroFragmentDirections.actionRegistroFragmentToLoginFragment()
                    v.findNavController().navigate(action)
                }else {
                    Toast.makeText(this.context,"Algo salió mal" + task.exception,Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun sentEmailVerification(){
        val user = fireBaseAuth.currentUser!!
        user.sendEmailVerification().addOnCompleteListener(this.requireActivity()){task ->
            if(task.isSuccessful){
                //En caso de q no se pueda enviar el mail.
            }else {

            }
        }
    }


}