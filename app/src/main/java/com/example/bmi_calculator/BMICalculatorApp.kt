package com.example.bmi_calculator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BMICalculator(modifier: Modifier = Modifier) {
    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text("BMI Calculator")
            }
        )
    }) {
        val weights = remember { mutableStateOf(value = "") }
        val height = remember { mutableStateOf(value = "") }
        val bmi = remember { mutableStateOf(value = "") }
        val status = remember { mutableStateOf(value = "") }
        Column(
            modifier = modifier.padding(it),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            EditNumberField(
                modifier = modifier,
                label = "Weight (in kg)",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                onValueChange = {
                    weights.value = it
                },
                value = weights.value,
            )
            EditNumberField(
                modifier = modifier,
                label = "Height (in meter)",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                onValueChange = {
                    height.value = it
                },
                value = height.value,
            )
            Spacer(modifier = modifier.height(20.dp))
            Button(onClick = {
                bmi.value = calculateBMI(
                    width = weights.value.toDoubleOrNull() ?: 0.0,
                    height = height.value.toDoubleOrNull() ?: 0.0,
                )
                status.value = getStatus(bmi = bmi.value.toDoubleOrNull() ?: 0.0)
            }) {
                Text("Calculate")
            }
            Spacer(modifier = modifier.height(20.dp))
            BMIResult(bmi = bmi.value, status = status.value)

        }
    }
}


@Composable
fun EditNumberField(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    keyboardOptions: KeyboardOptions,
    onValueChange: (String) -> Unit,
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = keyboardOptions,
        colors = TextFieldDefaults.colors(
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        label = {
            Text(label)
        }
    )
}

@Composable
fun BMIResult(modifier: Modifier = Modifier, status: String, bmi: String) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            "BMI: $bmi", style = MaterialTheme.typography.titleLarge,
            modifier = modifier.padding(bottom = 8.dp)
        )
        if(status.isNotBlank()){
            for (key in statusMap.keys) {
                val color = if(status == key) Color.LightGray else Color.Transparent
                val fontWidth = if(status == key) FontWeight.Bold else FontWeight.Normal
                Row(modifier = modifier
                    .padding(horizontal = 16.dp, vertical = 2.dp)
                    .fillMaxWidth().background(color = color)) {
                    Text(text = key, fontWeight = fontWidth, modifier = Modifier.weight(1f))
                    Text(text = statusMap[key]!! , fontWeight = fontWidth)

                }
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun BMICalculatorPreview() {
    BMICalculator()
}


object BMIStatus {
    const val UNDERWEIGHT_SEVERE = "Underweight (Sever thinness)"
    const val UNDERWEIGHT_MODERATE = "Underweight (Moderate thinness)"
    const val UNDERWEIGHT = "Underweight (Mid thinness)"
    const val NORMAL = "Normal"
    const val OVERWEIGHT = "Overweight (Pre-obese)"
    const val OBESE_CLASS_I = "Obese (Class I)"
    const val OBESE_CLASS_II = "Underweight (Class II)"
    const val OBESE_CLASS_III = "Underweight ((Class III)"
}

val statusMap = mapOf(
    BMIStatus.UNDERWEIGHT_SEVERE to "Less then 16.0",
    BMIStatus.UNDERWEIGHT_MODERATE to "16.0 - 16.9",
    BMIStatus.UNDERWEIGHT to "17.0 - 18.4",
    BMIStatus.NORMAL to "18.5 - 24.9",
    BMIStatus.OVERWEIGHT to "25.0 - 29.9",
    BMIStatus.OBESE_CLASS_I to "30.0 - 34.9",
    BMIStatus.OBESE_CLASS_II to "35.0 - 39.9",
    BMIStatus.OBESE_CLASS_III to "40 and above"
)

fun calculateBMI(width: Double, height: Double): String {
    val bmi = width / (height * height)
    return String.format("%.1f", bmi)
}

fun getStatus(bmi: Double): String {
    return when (bmi) {
        in Double.NEGATIVE_INFINITY..15.9 -> BMIStatus.UNDERWEIGHT_SEVERE
        in 16.0..16.9 -> BMIStatus.UNDERWEIGHT_MODERATE
        in 17.0..18.4 -> BMIStatus.UNDERWEIGHT
        in 18.5..24.9 -> BMIStatus.NORMAL
        in 25.0..29.9 -> BMIStatus.OVERWEIGHT
        in 30.0..34.9 -> BMIStatus.OBESE_CLASS_I
        in 35.0..39.9 -> BMIStatus.OBESE_CLASS_II
        else -> BMIStatus.OBESE_CLASS_III
    }
}