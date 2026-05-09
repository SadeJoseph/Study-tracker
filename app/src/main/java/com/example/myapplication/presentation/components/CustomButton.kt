package com.example.myapplication.presentation.components

import com.example.myapplication.ui.theme.LightBlue

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.CardWhite
import com.example.myapplication.ui.theme.LightBlue

@Composable
fun CustomButton(
    text: String,
    clickButton: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = clickButton,
        modifier = modifier,
        enabled = enabled,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = LightBlue,
            contentColor = CardWhite
        )
    ) {
        Text(text = text)
    }
}