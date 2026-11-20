package com.paominthng.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paominthng.calculator.CalculatorEngine.Op
import com.paominthng.calculator.ui.theme.CalculatorTheme

class MainActivity : ComponentActivity() {
    private val viewModel: CalculatorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculatorTheme {
                CalculatorScreen(viewModel)
            }
        }
    }
}

@Composable
fun CalculatorScreen(viewModel: CalculatorViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Display
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = viewModel.expression,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = viewModel.display,
                fontSize = 56.sp,
                fontWeight = FontWeight.Light,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // History
        if (viewModel.history.isNotEmpty()) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("History", fontWeight = FontWeight.Bold)
                    TextButton(onClick = { viewModel.onClearHistory() }) {
                        Text("Clear")
                    }
                }
                LazyColumn(modifier = Modifier.height(120.dp)) {
                    items(viewModel.history) { entry ->
                        Text(
                            text = "${entry.expression} ${entry.result}",
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.onHistoryTap(entry) }
                                .padding(vertical = 4.dp),
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }
        } else {
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Keypad
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            KeypadRow(
                listOf("AC" to { viewModel.onClear() }, "⌫" to { viewModel.onBackspace() },
                    "%" to { viewModel.onPercent() }, "÷" to { viewModel.onOperator(Op.DIVIDE) }),
                viewModel
            )
            KeypadRow(
                listOf("7" to { viewModel.onDigit("7") }, "8" to { viewModel.onDigit("8") },
                    "9" to { viewModel.onDigit("9") }, "×" to { viewModel.onOperator(Op.MULTIPLY) }),
                viewModel
            )
            KeypadRow(
                listOf("4" to { viewModel.onDigit("4") }, "5" to { viewModel.onDigit("5") },
                    "6" to { viewModel.onDigit("6") }, "−" to { viewModel.onOperator(Op.SUBTRACT) }),
                viewModel
            )
            KeypadRow(
                listOf("1" to { viewModel.onDigit("1") }, "2" to { viewModel.onDigit("2") },
                    "3" to { viewModel.onDigit("3") }, "+" to { viewModel.onOperator(Op.ADD) }),
                viewModel
            )
            KeypadRow(
                listOf("+/-" to { viewModel.onToggleSign() }, "0" to { viewModel.onDigit("0") },
                    "." to { viewModel.onDecimal() }, "=" to { viewModel.onEquals() }),
                viewModel
            )
        }
    }
}

@Composable
private fun KeypadRow(
    keys: List<Pair<String, () -> Unit>>,
    viewModel: CalculatorViewModel
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        keys.forEach { (label, onClick) ->
            val isOperator = label in listOf("+", "−", "×", "÷", "=")
            Button(
                onClick = onClick,
                modifier = Modifier
                    .weight(1f)
                    .height(64.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isOperator) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.surface,
                    contentColor = if (isOperator) MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.onSurface
                )
            ) {
                Text(label, fontSize = 24.sp)
            }
        }
    }
}
