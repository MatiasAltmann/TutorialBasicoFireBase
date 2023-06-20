package ar.edu.ort.tp3.firebasetutorial.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import ar.edu.ort.tp3.firebasetutorial.R
import ar.edu.ort.tp3.firebasetutorial.ViewModel.LoginViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginFragment : Fragment() {
   lateinit var v:View
   lateinit var mail:EditText
   lateinit var pass:EditText
   lateinit var btn:Button
   lateinit var btnRegistro:TextView
    lateinit var btnRecuperarClave:TextView
    lateinit var viewModel:LoginViewModel
   //---Variables de fireBase
   private lateinit var fireBaseAuth:FirebaseAuth
   private lateinit var authStateListener: FirebaseAuth.AuthStateListener

    //---
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_login, container, false)
        viewModel = ViewModelProvider(requireActivity()).get(LoginViewModel::class.java)
        mail = v.findViewById(R.id.mail_login)
        pass = v.findViewById(R.id.pass_login)
        btn = v.findViewById(R.id.button_id)
        btnRegistro = v.findViewById(R.id.txt_boton_registrarme)
        btnRecuperarClave = v.findViewById(R.id.txt_botton_recuperarClave)

        btnRegistro.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToRegistroFragment3()
            v.findNavController().navigate(action)
        }

        btnRecuperarClave.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToRecuperoClaveFragment()
            v.findNavController().navigate(action)
        }

        fireBaseAuth = Firebase.auth
        btn.setOnClickListener {
            if(mail.text.toString().length> 2 && pass.text.toString().length > 2)  {
            //    signIn(mail.text.toString(), pass.text.toString())
                signInMilConfirmado(mail.text.toString(), pass.text.toString())
            }else {
                Toast.makeText(this.context,"Error. El mail y el pass tiene q tener un mínimo de 2 caracteres", Toast.LENGTH_SHORT).show()

            }

        }



        return v

    }


    private fun signIn(email:String, password:String){
        fireBaseAuth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener(this.requireActivity()){task ->
                if(task.isSuccessful) {
                    val user = fireBaseAuth.currentUser
                    Toast.makeText(this.context,"Authenticación exitosa", Toast.LENGTH_SHORT).show()
                    viewModel.guardarCredenciales(email, password)
                     val action =  LoginFragmentDirections.actionLoginFragmentToFragmentHome()
                    v.findNavController().navigate(action)
                    //Aca voy a ir al HOME.
                }else {
                    Toast.makeText(this.context,"Error de email y/o password", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun signInMilConfirmado(email:String, password:String){
        fireBaseAuth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener(this.requireActivity()){task ->
                if(task.isSuccessful) {
                    val user = fireBaseAuth.currentUser
                    val verificado = user?.isEmailVerified
                    if(verificado == true) {
                        Toast.makeText(this.context,"Authenticación exitosa", Toast.LENGTH_SHORT).show()
                        viewModel.guardarCredenciales(email, password)
                        val action =  LoginFragmentDirections.actionLoginFragmentToFragmentHome()
                        v.findNavController().navigate(action)
                        //Aca voy a ir al REGISTRO.
                    } else {
                        Toast.makeText(this.context,"Error. Falta confirmar cuenta. Revisá tu mail", Toast.LENGTH_SHORT).show()

                    }


                }else {
                    Toast.makeText(this.context,"Error de email y/o password", Toast.LENGTH_SHORT).show()
                }
            }
    }




}