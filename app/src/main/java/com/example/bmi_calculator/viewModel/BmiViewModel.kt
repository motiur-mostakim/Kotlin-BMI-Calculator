package com.example.bmi_calculator.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.ViewModel

class BmiViewModel: ViewModel() {

    val bmiUiState = mutableStateOf(BmiUiState())


    fun updateWeight(weight: String){
        bmiUiState.value = bmiUiState.value.copy(weight= weight)
    }
    fun updateHeight(height: String){
        bmiUiState.value = bmiUiState.value.copy(height= height)
    }


   companion object {
        const val UNDERWEIGHT_SEVERE = "Underweight (Sever thinness)"
        const val UNDERWEIGHT_MODERATE = "Underweight (Moderate thinness)"
        const val UNDERWEIGHT = "Underweight (Mid thinness)"
        const val NORMAL = "Normal"
        const val OVERWEIGHT = "Overweight (Pre-obese)"
        const val OBESE_CLASS_I = "Obese (Class I)"
        const val OBESE_CLASS_II = "Underweight (Class II)"
        const val OBESE_CLASS_III = "Underweight ((Class III)"

       val statusMap = mapOf(
           UNDERWEIGHT_SEVERE to "Less then 16.0",
           UNDERWEIGHT_MODERATE to "16.0 - 16.9",
           UNDERWEIGHT to "17.0 - 18.4",
           NORMAL to "18.5 - 24.9",
           OVERWEIGHT to "25.0 - 29.9",
           OBESE_CLASS_I to "30.0 - 34.9",
           OBESE_CLASS_II to "35.0 - 39.9",
           OBESE_CLASS_III to "40 and above"
       )
    }


    fun calculateBMI() {
        val weight = bmiUiState.value.weight.toDoubleOrNull() ?: 0.0
        val height = bmiUiState.value.height.toDoubleOrNull() ?: 0.0
        val bmiDouble = weight / (height * height)
        bmiUiState.value = bmiUiState.value.copy(bmi = String.format("%.2f", bmiDouble))
        bmiUiState.value = bmiUiState.value.copy(status = getStatus(bmiDouble))
    }

    private fun getStatus(bmi: Double): String {
        return when (bmi) {
            in Double.NEGATIVE_INFINITY..15.9 -> UNDERWEIGHT_SEVERE
            in 16.0..16.9 -> UNDERWEIGHT_MODERATE
            in 17.0..18.4 -> UNDERWEIGHT
            in 18.5..24.9 -> NORMAL
            in 25.0..29.9 -> OVERWEIGHT
            in 30.0..34.9 -> OBESE_CLASS_I
            in 35.0..39.9 -> OBESE_CLASS_II
            else -> OBESE_CLASS_III
        }
    }
}

data class BmiUiState(
    val  weight: String = "",
    val  height: String = "",
    val  bmi: String = "",
    val  status: String = ""
)