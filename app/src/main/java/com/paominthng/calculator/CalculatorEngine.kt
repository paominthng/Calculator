package com.paominthng.calculator

import kotlin.math.abs

/** Pure calculation logic, with no Android dependencies. */
object CalculatorEngine {

    enum class Op(val symbol: String) {
        ADD("+"), SUBTRACT("−"), MULTIPLY("×"), DIVIDE("÷");

        companion object {
            fun fromSymbol(symbol: String): Op? = entries.find { it.symbol == symbol }
        }
    }

    sealed class CalcResult {
        data class Value(val value: Double) : CalcResult()
        data object DivideByZero : CalcResult()
    }

    fun apply(op: Op, left: Double, right: Double): CalcResult = when (op) {
        Op.ADD -> CalcResult.Value(left + right)
        Op.SUBTRACT -> CalcResult.Value(left - right)
        Op.MULTIPLY -> CalcResult.Value(left * right)
        Op.DIVIDE -> if (right == 0.0) CalcResult.DivideByZero else CalcResult.Value(left / right)
    }

    /** Format a Double the way a calculator would: 19.0 -> "19", 0.30000000000000004 -> "0.3". */
    fun format(value: Double): String {
        if (value.isNaN() || value.isInfinite()) return "Error"
        val rounded = "%.10f".format(value).trimEnd('0').trimEnd('.')
        return if (rounded.isEmpty() || rounded == "-0") "0" else rounded
    }

    fun parseOrNull(text: String): Double? =
        text.replace("−", "-").toDoubleOrNull()
}
