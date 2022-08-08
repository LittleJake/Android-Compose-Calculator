package net.littlejake.calculator

import android.os.Bundle
import android.util.Range
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.littlejake.calculator.ui.theme.*
import kotlin.math.exp
import kotlin.math.log
import kotlin.math.pow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CalculatorPad()
        }
    }
}

fun Int.factorial(): Int {
    return if (this <= 1 || this >= 102)
        1
    else
        this * (this - 1).factorial()
}

val inputBox = mutableStateOf("0")
val reload = mutableStateOf(false)
val c = Calc()

@Composable
fun Pad(number: Int, modifier: Modifier, fontSize: TextUnit = 24.sp) {
    Box(
        Modifier
            .then(modifier)
            .fillMaxHeight(1f)
//            .aspectRatio(1f)
            .padding(2.dp)
            .clip(CircleShape)
            .background(color = DarkGray)
            .clickable {
                if (reload.value) {
                    inputBox.value = "$number"
                    reload.value = false
                } else {
                    inputBox.value += number
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Text(text = "$number", color = Color.White, fontSize = fontSize)
    }
}

@Composable
fun Pad(oper: String, modifier: Modifier, fontSize: TextUnit = 24.sp) {
    Box(
        modifier = Modifier
            .then(modifier)
            .fillMaxHeight(1f)
//            .aspectRatio(1f)
            .padding(2.dp)
            .clip(CircleShape)
            .background(
                color =
                when (oper){
                    in arrayOf("+", "-", "×", "÷", "=") -> Orange
                    in arrayOf("AC", "+/-", "%") -> LightGray
                    "." -> DarkGray
                    else -> DarkDarkGray
                }
            )
            .clickable {
                //符号处理逻辑
                when (oper) {
                    "." -> {
                        if (inputBox.value.contains("."))
                            return@clickable
                        inputBox.value += oper
                        return@clickable
                    }
                    "=" -> {
                        c.current = inputBox.value.toDouble()
                        inputBox.value = c.eval()
                        reload.value = true
                    }
                    "+/-" -> {
                        try {
                            if (inputBox.value.contains("-"))
                                inputBox.value = inputBox.value.replace("-", "")
                            else
                                inputBox.value = "-" + inputBox.value
                        } catch (e: Exception) {
                        }
                    }
                    "%" -> {
                        try {
                            inputBox.value = "${inputBox.value.toDouble() / 100}"
                        } catch (e: Exception) {
                        }
                    }
                    "1/x" -> { inputBox.value = "${1.0 / inputBox.value.toDouble()}" }
                    "x²" -> { inputBox.value = "${inputBox.value.toDouble() * inputBox.value.toDouble()}" }
                    "x³" -> { inputBox.value = "${inputBox.value.toDouble() * inputBox.value.toDouble() * inputBox.value.toDouble()}" }
                    "eˣ" -> {inputBox.value = "${exp(inputBox.value.toDouble())}"}
                    "10ˣ" -> {inputBox.value = "${(10.0).pow(inputBox.value.toDouble())}"}
                    "ln" -> {inputBox.value = "${log(Math.E, inputBox.value.toDouble())}"}
                    "lg" -> {inputBox.value = "${log(10.0, inputBox.value.toDouble())}"}
                    "e" -> {inputBox.value = "${Math.E}"}
                    "π" -> {inputBox.value = "${Math.PI}"}
                    "x!" -> {inputBox.value = "${inputBox.value.toInt().factorial()}" }
                    else -> {
                        c.current = inputBox.value.toDouble()
                        c.oper = oper
                        inputBox.value = c.eval()
                        reload.value = true
                    }
                }
            },

        contentAlignment = Alignment.Center
    ) {
        Text(text = oper, color = Color.White, fontSize = fontSize)
    }
}

var padKey = arrayOf(
    arrayOf("AC", "+/-", "%", "÷"),
    arrayOf(7, 8, 9, "×"),
    arrayOf(4, 5, 6, "-"),
    arrayOf(1, 2, 3, "+"),
    arrayOf(0, ".", "="),
)


var padKey_wide = arrayOf(
    arrayOf("(", ")", "mc", "m+", "m-", "mr", "AC", "+/-", "%", "÷"),
    arrayOf("2ⁿᵈ", "x²", "x³", "xʸ", "eˣ", "10ˣ", 7, 8, 9, "×"),
    arrayOf("1/x", "²√x", "³√x", "ʸ√x", "ln", "lg", 4, 5, 6, "-"),
    arrayOf("x!", "sin", "cos", "tan", "e", "EE", 1, 2, 3, "+"),
    arrayOf("Rad", "sinh", "cosh", "tanh", "π", "Rand", 0, ".", "="),
)

@Composable
fun InputBox() {
    val inputBox by inputBox
    Text(
        text = inputBox,
        fontSize = 48.sp,
        modifier = Modifier
            .fillMaxHeight(0.3f)
            .fillMaxWidth(),
        textAlign = TextAlign.Right,

        )
}

@Preview(showBackground = true)
@Composable
fun CalculatorPad() {
    BoxWithConstraints {
        val maxWidth = this.maxWidth
        Column(
            Modifier.fillMaxSize(),
        ) {
            InputBox()

            if (maxWidth < 400.dp)
                padKey.forEach {
                    Row(
                        Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        it.forEach {
                            when(it) {
                                is Int -> Pad(
                                    number = it,
                                    Modifier.weight(if (it == 0) 2f else 1f),
                                    fontSize = 32.sp
                                )
                                is String -> Pad(
                                    oper = it,
                                    Modifier.weight(1f),
                                    fontSize = 32.sp
                                )
                            }
                        }
                    }
                }
            else
                padKey_wide.forEach {
                    Row(
                        Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        it.forEach {
                            when(it) {
                                is Int -> Pad(number = it, Modifier.weight(if (it == 0) 2f else 1f))
                                is String -> Pad(oper = it, Modifier.weight(1f))
                            }
                        }
                    }
                }
        }
    }
}