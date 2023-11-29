package com.dicoding.thestoryapp.ui.auth

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import com.dicoding.thestoryapp.databinding.ActivityRegisterBinding
import com.dicoding.thestoryapp.ui.auth.viewmodel.AuthViewModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var viewbinding: ActivityRegisterBinding
    private val authRegisterViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewbinding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = viewbinding.root
        setContentView(view)
        hideActionBar()
        isLoading(false)

        viewbinding.registerName.addTextChangedListener(watcher())
        viewbinding.registerEmail.addTextChangedListener(watcher())
        viewbinding.registerPassword.addTextChangedListener(watcher())
        viewbinding.registerPasswordConfirm.addTextChangedListener(watcher())

        viewbinding.btnRegister.setOnClickListener {
            register()
        }

        viewbinding.txtLogin.setOnClickListener {
            super.onBackPressed()
        }
    }

    private fun register() {
        val name = viewbinding.registerName.text.toString().trim()
        val email = viewbinding.registerEmail.text.toString().trim()
        val password = viewbinding.registerPassword.text.toString().trim()
        val confirmPassword = viewbinding.registerPasswordConfirm.text.toString()

        if (password != confirmPassword) {
            viewbinding.registerPasswordConfirm.error = "Passwords are not the same"
            Toast.makeText(this, "Passwords are not the same", Toast.LENGTH_SHORT).show()
            return
        }

        isLoading(true)

        authRegisterViewModel.register(name, email, password)

        authRegisterViewModel.dataRegister.observe(this) { registerResponse ->
            if (registerResponse != null) {
                isLoading(false)
                if (!registerResponse.error) {
                    Toast.makeText(this, "Registered successfully", Toast.LENGTH_LONG).show()
                    finish()
                }
                else {
                    Toast.makeText(this, registerResponse.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun hideActionBar() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun watcher() : TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                viewbinding.btnRegister.isEnabled =
                    viewbinding.registerEmail.text.toString().trim().isNotEmpty() &&
                            viewbinding.registerEmail.error == null &&
                            viewbinding.registerPassword.text.toString().trim().isNotEmpty() &&
                            viewbinding.registerPassword.error == null &&
                            viewbinding.registerPasswordConfirm.text.toString().trim().isNotEmpty() &&
                            viewbinding.registerName.text.toString().trim().isNotEmpty()
            }

        }
    }

    private fun isLoading(isL: Boolean) {
        if (isL) {
            viewbinding.rlLoading.visibility = View.VISIBLE
        } else {
            viewbinding.rlLoading.visibility = View.GONE
        }
    }

}