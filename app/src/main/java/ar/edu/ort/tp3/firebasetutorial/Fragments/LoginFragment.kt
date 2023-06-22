package ar.edu.ort.tp3.firebasetutorial.Fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider


class LoginFragment : Fragment() {
   lateinit var v:View
   lateinit var mail:EditText
   lateinit var pass:EditText
   lateinit var btn:Button
   lateinit var btnRegistro:TextView
   lateinit var  btnGoogleSignIn :Button
    lateinit var btnRecuperarClave:TextView
    lateinit var viewModel:LoginViewModel
   //---Variables de fireBase
   private lateinit var fireBaseAuth:FirebaseAuth
   private lateinit var authStateListener: FirebaseAuth.AuthStateListener
   private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 62870

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
        btnGoogleSignIn = v.findViewById(R.id.button_google)
        fireBaseAuth = Firebase.auth
        btnRegistro.setOnClickListener {

            val action = LoginFragmentDirections.actionLoginFragmentToRegistroFragment3()
            v.findNavController().navigate(action)
        }

        btnRecuperarClave.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToRecuperoClaveFragment()
            v.findNavController().navigate(action)
        }

        btn.setOnClickListener {
            if(mail.text.toString().length> 2 && pass.text.toString().length > 2)  {
            //    signIn(mail.text.toString(), pass.text.toString())
                signInMilConfirmado(mail.text.toString(), pass.text.toString())
            }else {
                Toast.makeText(this.context,"Error. El mail y el pass tiene q tener un mínimo de 2 caracteres", Toast.LENGTH_SHORT).show()

            }

        }

        //Login google:
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN) // Esto crea un nuevo constructor de opciones de inicio de sesión de Google.
            .requestIdToken(getString(R.string.default_web_client_id)) //Aquí se solicita el token de identificación (idToken) del usuario. El método
            .requestEmail() //Esto es necesario para obtener el correo electrónico del usuario desde la cuenta de Google y utilizarlo en tu aplicación si es necesario.
            .build() // Finalmente, este método construye y devuelve la instancia de GoogleSignInOptions con la configuración proporcionada.

        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

        btnGoogleSignIn.setOnClickListener {
            signInGoogle()
        }



        return v

    }

    private fun signInGoogle() {
        val signInIntent: Intent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data) //Consiga la cuenta de google q se inicio sesion
            val exception = task.exception

            if (task.isSuccessful) {
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)!!
                    println( "firebaseAuthWithGoogle:" + account.id)
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                   // Log.w(TAG, "Google sign in failed", e)
                    Toast.makeText(requireContext(), "Fallo inicio de sesión de google:(", Toast.LENGTH_SHORT).show()
                }
            } else {
               // Log.w(TAG, exception.toString())
                Toast.makeText(requireContext(), "Hubo algun error con google:(", Toast.LENGTH_SHORT).show()

            }
        }
    }

    override fun onStart() {
        super.onStart()
        // Verifica si el usuario esta logueado con google para actualizar la vista.
        val currentUser = fireBaseAuth.currentUser //Obtengo el usuario authenticado
        updateUI(currentUser)
    }

    // se utiliza para autenticar al usuario con Firebase utilizando las credenciales de Google
    private fun firebaseAuthWithGoogle(idToken: String) {

        val credential = GoogleAuthProvider.getCredential(idToken, null)
        fireBaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {

                    // Sign in success, update UI with the signed-in user's information
                  //  Log.d(TAG, "signInWithCredential:success")
                    Toast.makeText(this.context, "Inicio de sesión con google correcto :)", Toast.LENGTH_SHORT).show()

                    val user = fireBaseAuth.currentUser

                    val action =  LoginFragmentDirections.actionLoginFragmentToFragmentHome()
                    v.findNavController().navigate(action)
                    //Aca voy a ir al HOME.


                } else {
                    // Error con el log
                   // Log.w(TAG, "Error con login de google", task.exception)
                    Toast.makeText(this.context, "Fallo el  login con google :(", Toast.LENGTH_SHORT).show()
                }
            }
    }


    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            val action =  LoginFragmentDirections.actionLoginFragmentToFragmentHome()
            v.findNavController().navigate(action)
        }
    }
//Este método verificaria si el usaurio sigue loguead y directo lo mando al HOME.



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