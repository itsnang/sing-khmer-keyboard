package com.singkhmer.keyboard

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {
    
    private lateinit var btnOpenSettings: MaterialButton
    private lateinit var btnEnableKeyboard: MaterialButton
    private lateinit var testInput: TextInputEditText
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        initViews()
        setupClickListeners()
    }
    
    private fun initViews() {
        btnOpenSettings = findViewById(R.id.btn_open_settings)
        btnEnableKeyboard = findViewById(R.id.btn_enable_keyboard)
        testInput = findViewById(R.id.test_input)
    }
    
    private fun setupClickListeners() {
        btnOpenSettings.setOnClickListener {
            openInputMethodSettings()
        }
        
        btnEnableKeyboard.setOnClickListener {
            showInputMethodPicker()
        }
    }
    
    private fun openInputMethodSettings() {
        try {
            val intent = Intent(Settings.ACTION_INPUT_METHOD_SETTINGS)
            startActivity(intent)
        } catch (e: Exception) {
            // Fallback to general language settings
            try {
                val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(intent)
            } catch (e2: Exception) {
                // Last fallback to main settings
                val intent = Intent(Settings.ACTION_SETTINGS)
                startActivity(intent)
            }
        }
    }
    
    private fun showInputMethodPicker() {
        val inputMethodManager = getSystemService<InputMethodManager>()
        inputMethodManager?.showInputMethodPicker()
        
        // Focus on the test input field
        testInput.requestFocus()
    }
    
    override fun onResume() {
        super.onResume()
        updateKeyboardStatus()
    }
    
    private fun updateKeyboardStatus() {
        val inputMethodManager = getSystemService<InputMethodManager>()
        val enabledInputMethods = inputMethodManager?.enabledInputMethodList
        
        val isKeyboardEnabled = enabledInputMethods?.any { 
            it.packageName == packageName 
        } ?: false
        
        if (isKeyboardEnabled) {
            btnEnableKeyboard.isEnabled = true
            btnEnableKeyboard.text = getString(R.string.test_keyboard)
        } else {
            btnEnableKeyboard.isEnabled = false
            btnEnableKeyboard.text = "Enable keyboard first"
        }
    }
}