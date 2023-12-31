package ar.edu.ort.tp3.firebasetutorial.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.MenuProvider
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import ar.edu.ort.tp3.firebasetutorial.R
import ar.edu.ort.tp3.firebasetutorial.ViewModel.LoginViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class fragmentHome : Fragment() {
    lateinit var v:View
    lateinit var viewModel: LoginViewModel
    lateinit var mail:TextView
    lateinit var btnCierre:Button
    private lateinit var fireBaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fireBaseAuth = Firebase.auth

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_home, container, false)
        viewModel = ViewModelProvider(requireActivity()).get(LoginViewModel::class.java)
        mail = v.findViewById(R.id.mail_home)
        mail.text = viewModel.mail.value.toString()
        btnCierre = v.findViewById(R.id.button_home)

        btnCierre.setOnClickListener {
            singOut()
        }


        return v
    }


    private fun singOut(){
        fireBaseAuth.signOut()
        Toast.makeText(this.context,"Log Out Ok", Toast.LENGTH_SHORT).show()

        val action = fragmentHomeDirections.actionFragmentHomeToLoginFragment()
        v.findNavController().navigate(action)
    }







}