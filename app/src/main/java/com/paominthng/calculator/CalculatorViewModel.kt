package com.paominthng.calculator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.paominthng.calculator.CalculatorEngine.CalcResult
import com.paominthng.calculator.CalculatorEngine.Op

data class HistoryEntry(val expression: String, val result: String)

class CalculatorViewModel : ViewModel() {

    var display by mutableStateOf("0")
        private set
    var expression by mutableStateOf("")
        private set
    var history by mutableStateOf<List<HistoryEntry>>(emptyList())
        private set

    private var pendingOp: Op? = null
    private var leftOperand: Double? = null
    private var lastOp: Op? = null
    private var lastRight: Double? = null
    private var justEvaluated = false
    private var error = false

    fun onDigit(digit: String) {
        if (error) clearAll()
        if (justEvaluated) {
            display = digit
            expression = ""
            leftOperand = null
            pendingOp = null
            justEvaluated = false
            return
        }
        display = if (display == "0") digit else if (display.length < 15) display + digit else display
    }

    fun onDecimal() {
        if (error) clearAll()
        if (justEvaluated) {
            display = "0."
            justEvaluated = false
            return
        }
        if (!display.contains(".")) display += "."
    }

    fun onOperator(op: Op) {
        if (error) return
        val current = CalculatorEngine.parseOrNull(display) ?: return
        if (pendingOp != null && !justEvaluated) {
            val left = leftOperand ?: current
            when (val result = CalculatorEngine.apply(pendingOp!!, left, current)) {
                is CalcResult.Value -> {
                    leftOperand = result.value
                    display = CalculatorEngine.format(result.value)
                }
                CalcResult.DivideByZero -> {
                    showError()
                    return
                }
            }
        } else {
            leftOperand = current
        }
        pendingOp = op
        expression = "${CalculatorEngine.format(leftOperand!!)} ${op.symbol}"
        justEvaluated = false
        display = "0"
    }

    fun onEquals() {
        if (error) return
        val current = CalculatorEngine.parseOrNull(display) ?: return
        if (pendingOp != null) {
            val left = leftOperand ?: return
            val op = pendingOp!!
            when (val result = CalculatorEngine.apply(op, left, current)) {
                is CalcResult.Value -> {
                    val formatted = CalculatorEngine.format(result.value)
                    val expr = "${CalculatorEngine.format(left)} ${op.symbol} ${CalculatorEngine.format(current)} ="
                    expression = expr
                    display = formatted
                    history = listOf(HistoryEntry(expr, formatted)) + history
                    lastOp = op
                    lastRight = current
                    leftOperand = result.value
                    pendingOp = null
                    justEvaluated = true
                }
                CalcResult.DivideByZero -> showError()
            }
        } else if (lastOp != null && lastRight != null) {
            // Repeat-equals: 12 + 7 = 19, = again -> 26
            when (val result = CalculatorEngine.apply(lastOp!!, current, lastRight!!)) {
                is CalcResult.Value -> {
                    val formatted = CalculatorEngine.format(result.value)
                    val expr = "${CalculatorEngine.format(current)} ${lastOp!!.symbol} ${CalculatorEngine.format(lastRight!!)} ="
                    expression = expr
                    display = formatted
                    history = listOf(HistoryEntry(expr, formatted)) + history
                    leftOperand = result.value
                    justEvaluated = true
                }
                CalcResult.DivideByZero -> showError()
            }
        }
    }

    fun onPercent() {
        if (error) return
        val current = CalculatorEngine.parseOrNull(display) ?: return
        val value = current / 100.0
        display = CalculatorEngine.format(value)
    }

    fun onToggleSign() {
        if (error || display == "0") return
        display = if (display.startsWith("-")) display.drop(1) else "-$display"
    }

    fun onBackspace() {
        if (error) {
            clearAll()
            return
        }
        if (justEvaluated) return
        display = if (display.length <= 1) "0" else display.dropLast(1)
    }

    fun onClear() = clearAll()

    fun onHistoryTap(entry: HistoryEntry) {
        if (error) clearAll()
        display = entry.result
        expression = ""
        leftOperand = CalculatorEngine.parseOrNull(entry.result)
        pendingOp = null
        justEvaluated = false
    }

    fun onClearHistory() {
        history = emptyList()
    }

    private fun clearAll() {
        display = "0"
        expression = ""
        leftOperand = null
        pendingOp = null
        lastOp = null
        lastRight = null
        justEvaluated = false
        error = false
    }

    private fun showError() {
        display = "Error"
        expression = "Can't divide by zero"
        leftOperand = null
        pendingOp = null
        justEvaluated = false
        error = true
    }
}
