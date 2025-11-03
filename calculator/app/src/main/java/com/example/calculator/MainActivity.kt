package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

    private lateinit var display: TextView

    private var currentNumber = "0"      // Số đang nhập
    private var firstNumber: Double = 0.0
    private var currentOperator: String? = null
    private var waitingForSecondNumber = false

    // Biến lưu toàn bộ biểu thức để hiển thị khi nhập
    private var expressionText = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        display = findViewById(R.id.textView2)
        display.text = "0"

        setupNumberButtons()
        setupOperatorButtons()
        setupActionButtons()
    }

    // ========================
    // Nút số 0-9
    // ========================
    private fun setupNumberButtons() {
        val numberButtons = mapOf(
            R.id.button28 to "7", R.id.button29 to "8", R.id.button30 to "9",
            R.id.button32 to "4", R.id.button33 to "5", R.id.button34 to "6",
            R.id.button36 to "1", R.id.button37 to "2", R.id.button38 to "3",
            R.id.button41 to "0"
        )
        for ((id, number) in numberButtons) {
            findViewById<Button>(id).setOnClickListener { onNumberPressed(number) }
        }
    }

    // ========================
    // Nút toán tử + - * /
    // ========================
    private fun setupOperatorButtons() {
        val operatorButtons = mapOf(
            R.id.button39 to "+", R.id.button35 to "-",
            R.id.button31 to "*", R.id.button27 to "/"
        )
        for ((id, op) in operatorButtons) {
            findViewById<Button>(id).setOnClickListener { onOperatorPressed(op) }
        }
    }

    // ========================
    // Nút chức năng
    // ========================
    private fun setupActionButtons() {
        findViewById<Button>(R.id.button25).setOnClickListener { clearAll() }    // C
        findViewById<Button>(R.id.button24).setOnClickListener { clearEntry() }  // CE
        findViewById<Button>(R.id.button26).setOnClickListener { onBackspacePressed() } // BS
        findViewById<Button>(R.id.button43).setOnClickListener { onEqualsPressed() }    // =
        findViewById<Button>(R.id.button40).setOnClickListener { onPlusMinusPressed()}
        findViewById<Button>(R.id.button42).setOnClickListener { onDecimalPressed()}
    }

    // ========================
    // Nhấn số
    // ========================
    private fun onNumberPressed(number: String) {
        if (waitingForSecondNumber) {
            currentNumber = number
            waitingForSecondNumber = false
        } else {
            currentNumber = if (currentNumber == "0") number else currentNumber + number
        }

        // Cập nhật biểu thức hiển thị
        expressionText += number
        display.text = expressionText
    }

    // ========================
    // Nhấn toán tử
    // ========================
    private fun onOperatorPressed(operator: String) {
        if (!waitingForSecondNumber) {
            if (currentOperator != null) {
                onEqualsPressed() // thực hiện phép tính trước
            }
            firstNumber = currentNumber.toDoubleOrNull() ?: 0.0
        }
        currentOperator = operator
        waitingForSecondNumber = true

        // Cập nhật biểu thức hiển thị
        expressionText += operator
        display.text = expressionText
    }

    // ========================
    // Nhấn =
    // ========================
    private fun onEqualsPressed() {
        if (currentOperator == null || waitingForSecondNumber) return

        val secondNumber = currentNumber.toDoubleOrNull() ?: 0.0

        // Xử lý chia cho 0
        if (currentOperator == "/" && secondNumber == 0.0) {
            display.text = "Error"
            resetCalculator()
            return
        }

        val result = when (currentOperator) {
            "+" -> firstNumber + secondNumber
            "-" -> firstNumber - secondNumber
            "*" -> firstNumber * secondNumber
            "/" -> firstNumber / secondNumber
            else -> secondNumber
        }

        // Hiển thị **chỉ kết quả**
        currentNumber = DecimalFormat("#.##########").format(result)
        display.text = currentNumber

        // Reset trạng thái cho phép tính tiếp theo
        firstNumber = result
        currentOperator = null
        waitingForSecondNumber = true
        expressionText = currentNumber  // sẵn sàng nhập phép tính mới
    }

    // ========================
    // Nút C
    // ========================
    private fun clearAll() {
        currentNumber = "0"
        firstNumber = 0.0
        currentOperator = null
        waitingForSecondNumber = false
        expressionText = ""
        display.text = "0"
    }

    // ========================
    // Nút CE
    // ========================
    private fun clearEntry() {
        currentNumber = "0"
        display.text = "0"
        // Xóa phần số cuối cùng khỏi biểu thức
        if (expressionText.isNotEmpty()) {
            val lastOperatorIndex = expressionText.lastIndexOfAny(charArrayOf('+','-','*','/'))
            expressionText = if (lastOperatorIndex != -1) {
                expressionText.substring(0, lastOperatorIndex + 1)
            } else {
                ""
            }
        }
    }

    // ========================
    // Backspace
    // ========================
    private fun onBackspacePressed() {
        if (currentNumber.length > 1) currentNumber = currentNumber.dropLast(1) else currentNumber = "0"
        if (expressionText.isNotEmpty()) expressionText = expressionText.dropLast(1)
        display.text = if (expressionText.isEmpty()) "0" else expressionText
    }

    // ========================
    // +/- đổi dấu
    // ========================
    private fun onPlusMinusPressed() {
        if (currentNumber != "0") {
            currentNumber = if (currentNumber.startsWith("-")) currentNumber.removePrefix("-") else "-$currentNumber"

            // Cập nhật biểu thức
            val lastOperatorIndex = expressionText.lastIndexOfAny(charArrayOf('+','-','*','/'))
            expressionText = if (lastOperatorIndex != -1) {
                expressionText.substring(0, lastOperatorIndex + 1) + currentNumber
            } else {
                currentNumber
            }
            display.text = expressionText
        }
    }

    // ========================
    // Nhấn dấu chấm
    // ========================
    private fun onDecimalPressed() {
        if (!currentNumber.contains(".")) {
            currentNumber += "."
            expressionText += "."
            display.text = expressionText
        }
    }
    // ========================
    // Reset toàn bộ
    // ========================
    private fun resetCalculator() {
        currentNumber = "0"
        firstNumber = 0.0
        currentOperator = null
        waitingForSecondNumber = false
        expressionText = ""
    }
}
