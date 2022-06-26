package com.application.weatherapp.view.ui.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CircleButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    colors: ButtonColors,
    enabled: Boolean = true,
    border: BorderStroke? = null,
    elevation: ButtonElevation? = null,
    content: @Composable
    (RowScope.() -> Unit)
) {
    Button(
        onClick = onClick,
        colors = colors,
        shape = CircleShape,
        elevation = elevation,
        border = border,
        enabled = enabled,
        contentPadding = PaddingValues(0.dp),
        modifier = modifier.size(50.dp),
        content = content
    )
}