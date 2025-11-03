package com.example.inputtext

import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var editTextFirstName: EditText
    private lateinit var editTextLastName: EditText
    private lateinit var radioGroupGender: RadioGroup
    private lateinit var editTextBirthday: EditText
    private lateinit var buttonSelectDate: Button
    private lateinit var calendarView: CalendarView
    private lateinit var editTextAddress: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var checkBoxAgree: CheckBox
    private lateinit var buttonRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Map Views
        editTextFirstName = findViewById(R.id.firstName)
        editTextLastName = findViewById(R.id.lastName)
        radioGroupGender = findViewById(R.id.genderGroup)
        editTextBirthday = findViewById(R.id.birthday)
        buttonSelectDate = findViewById(R.id.btnSelect)
        calendarView = findViewById(R.id.calendarView)
        editTextAddress = findViewById(R.id.address)
        editTextEmail = findViewById(R.id.email)
        checkBoxAgree = findViewById(R.id.agree)
        buttonRegister = findViewById(R.id.btnRegister)

        setupCalendar()
        setupRegistration()
    }

    private fun setupCalendar() {
        // Khởi tạo calendar với ngày hiện tại
        val calendar = Calendar.getInstance()
        calendarView.date = calendar.timeInMillis

        buttonSelectDate.setOnClickListener {
            if (calendarView.visibility == View.VISIBLE) {
                calendarView.visibility = View.GONE
            } else {
                // Nếu đã có ngày trong EditText, set calendar về ngày đó
                val currentText = editTextBirthday.text.toString().trim()
                if (currentText.isNotEmpty()) {
                    try {
                        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        val date = sdf.parse(currentText)
                        if (date != null) {
                            calendarView.date = date.time
                        }
                    } catch (e: Exception) {
                        // Nếu parse lỗi thì dùng ngày hiện tại
                    }
                }
                calendarView.visibility = View.VISIBLE
            }
        }

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedCalendar = Calendar.getInstance()
            selectedCalendar.set(year, month, dayOfMonth)
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val selectedDate = sdf.format(selectedCalendar.time)
            editTextBirthday.setText(selectedDate)
            resetFieldColor(editTextBirthday)
            calendarView.visibility = View.GONE
        }

        // Thêm listener để ẩn calendar khi user gõ vào EditText
        editTextBirthday.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && calendarView.visibility == View.VISIBLE) {
                calendarView.visibility = View.GONE
            }
        }
    }

    private fun setupRegistration() {
        buttonRegister.setOnClickListener {
            resetAllValidationColors()

            if (validateInputs()) {
                Toast.makeText(this, "Register sucessfully!", Toast.LENGTH_SHORT).show()
                // Handle registration logic here
            } else {
                Toast.makeText(this, "Please fill in all the information", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateInputs(): Boolean {
        var isValid = true

        if (editTextFirstName.text.toString().trim().isEmpty()) {
            setFieldErrorColor(editTextFirstName)
            isValid = false
        }

        if (editTextLastName.text.toString().trim().isEmpty()) {
            setFieldErrorColor(editTextLastName)
            isValid = false
        }

        if (radioGroupGender.checkedRadioButtonId == -1) {
            Toast.makeText(this, "Please select Gender", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        if (editTextBirthday.text.toString().trim().isEmpty()) {
            setFieldErrorColor(editTextBirthday)
            isValid = false
        }

        if (editTextAddress.text.toString().trim().isEmpty()) {
            setFieldErrorColor(editTextAddress)
            isValid = false
        }

        if (editTextEmail.text.toString().trim().isEmpty()) {
            setFieldErrorColor(editTextEmail)
            isValid = false
        }

        if (!checkBoxAgree.isChecked) {
            Toast.makeText(this, "You must agree to Terms of Use", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        return isValid
    }

    private fun setFieldErrorColor(editText: EditText) {
        val drawable = editText.background.mutate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            drawable.colorFilter = BlendModeColorFilter(Color.RED, BlendMode.SRC_ATOP)
        } else {
            @Suppress("DEPRECATION")
            drawable.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP)
        }
    }

    private fun resetFieldColor(editText: EditText) {
        editText.background.clearColorFilter()
    }

    private fun resetAllValidationColors() {
        resetFieldColor(editTextFirstName)
        resetFieldColor(editTextLastName)
        resetFieldColor(editTextBirthday)
        resetFieldColor(editTextAddress)
        resetFieldColor(editTextEmail)
    }
}
