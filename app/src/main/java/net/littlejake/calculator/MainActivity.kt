package net.littlejake.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import net.littlejake.calculator.ui.theme.CalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                InputBox()
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Pad(number = 7)
                    Pad(number = 8)
                    Pad(number = 9)
                    Pad(oper = "÷")
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Pad(number = 4)
                    Pad(number = 5)
                    Pad(number = 6)
                    Pad(oper = "×")
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Pad(number = 1)
                    Pad(number = 2)
                    Pad(number = 3)
                    Pad(oper = "-")
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Pad(oper = "C")
                    Pad(number = 0)
                    Pad(oper = "=")
                    Pad(oper = "+")
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Pad(oper = ".")
                    Pad(oper = "-/+")
                }
            }
        }
    }
}

val inputBox = mutableStateOf("")
val reload = mutableStateOf(false)
val c = Calc()

@Composable
fun Pad(number: Int) {
    Button(
        onClick = {
            if (reload.value) {
                inputBox.value = number.toString()
                reload.value = false
            }
            else{
                inputBox.value += number
            }
        },
        content = @Composable { Text(text = number.toString())}
    )
}

@Composable
fun Pad(oper: String) {
    Button(
        onClick = {
            when(oper) {
                "." -> {
                    if (inputBox.value.contains("."))
                        return@Button
                    inputBox.value += oper
                    return@Button
                }
                "=" -> {
                    c.current = inputBox.value.toDouble()
                    inputBox.value = c.eval()
                    reload.value = true
                }
                "-/+" -> {
                    try {
                        if (inputBox.value.contains("-"))
                            inputBox.value = inputBox.value.replace("-", "")
                        else
                            inputBox.value = "-" + inputBox.value
                    } catch (e: Exception){}
                }
                else-> {
                    c.current = inputBox.value.toDouble()
                    c.oper = oper
                    inputBox.value = c.eval()
                    reload.value = true
                }
            }
        },
    ) {
        Text(text = oper)
    }
}

@Composable
fun InputBox() {
    val inputBox by inputBox
    Text(text = inputBox, fontSize = 24.sp)
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        InputBox()
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Pad(number = 7)
            Pad(number = 8)
            Pad(number = 9)
            Pad(oper = "÷")
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Pad(number = 4)
            Pad(number = 5)
            Pad(number = 6)
            Pad(oper = "×")
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Pad(number = 1)
            Pad(number = 2)
            Pad(number = 3)
            Pad(oper = "-")
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Pad(oper = "C")
            Pad(number = 0)
            Pad(oper = "=")
            Pad(oper = "+")
        }
    }
}