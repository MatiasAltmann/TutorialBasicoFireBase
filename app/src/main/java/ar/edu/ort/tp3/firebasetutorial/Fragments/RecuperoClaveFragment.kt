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
import androidx.navigation.findNavController
import ar.edu.ort.tp3.firebasetutorial.R
import ar.edu.ort.tp3.firebasetutorial.ViewModel.LoginViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RecuperoClaveFragment : Fragment() {
    lateinit var viewModel: LoginViewModel
    lateinit var v:View
    lateinit var btnRecupero:Button
    lateinit var mail:EditText
    //---Variables de fireBase
    private lateinit var fireBaseAuth: FirebaseAuth
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener
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
        v = inflater.inflate(R.layout.fragment_recupero_clave, container, false)
        mail = v.findViewById(R.id.mail_recuperarClave)
        btnRecupero = v.findViewById(R.id.button_recuperoClave)

        btnRecupero.setOnClickListener {
            if(mail.text.toString().length > 0) {
               enviarPassNueva(mail.text.toString())
            }
        }


        return v
    }

    private fun enviarPassNueva(mail:String){
        fireBaseAuth.sendPasswordResetEmail(mail)
            .addOnCompleteListener(){task->
                if(task.isSuccessful) {
                    Toast.makeText(this.context, "Mail enviado", Toast.LENGTH_SHORT).show()
                    val action =  RecuperoClaveFragmentDirections.actionRecuperoClaveFragmentToLoginFragment()
                    v.findNavController().navigate(action)
                }else {
                  Toast.makeText(this.context,"Error. No se pudo procesar el pedido", Toast.LENGTH_SHORT).show()
                }
            }
    }

}