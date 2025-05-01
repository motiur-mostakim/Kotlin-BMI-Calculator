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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bmi_calculator.viewModel.BmiViewModel

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
        val bmiViewModel: BmiViewModel = viewModel()
        val  uiState = bmiViewModel.bmiUiState.value
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
                onValueChange = bmiViewModel::updateWeight,
                value = uiState.weight,
            )
            EditNumberField(
                modifier = modifier,
                label = "Height (in meter)",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                onValueChange = bmiViewModel::updateHeight,
                value = uiState.height,
            )
            Spacer(modifier = modifier.height(20.dp))
            Button(onClick = {
                bmiViewModel.calculateBMI()
            }) {
                Text("Calculate")
            }
            Spacer(modifier = modifier.height(20.dp))
            BMIResult(
                bmi = uiState.bmi,
                status = uiState.status,
                statusMap = BmiViewModel.statusMap
            )

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
fun BMIResult(
    modifier: Modifier = Modifier,
    status: String,
    bmi: String,
    statusMap: Map<String, String>
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            "BMI: $bmi", style = MaterialTheme.typography.titleLarge,
            modifier = modifier.padding(bottom = 8.dp)
        )
        if (status.isNotBlank()) {
            for (key in statusMap.keys) {
                val color = if (status == key) Color.LightGray else Color.Transparent
                val fontWidth = if (status == key) FontWeight.Bold else FontWeight.Normal
                Row(
                    modifier = modifier
                        .padding(horizontal = 16.dp, vertical = 2.dp)
                        .fillMaxWidth()
                        .background(color = color)
                ) {
                    Text(text = key, fontWeight = fontWidth, modifier = Modifier.weight(1f))
                    Text(text = statusMap[key]!!, fontWeight = fontWidth)

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


