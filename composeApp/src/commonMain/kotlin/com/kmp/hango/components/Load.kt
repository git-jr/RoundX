package com.kmp.hango.components


import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.kmp.hango.constant.DEFAULT_BG_COLOR_DARK
import hango.composeapp.generated.resources.Res
import hango.composeapp.generated.resources.ic_o
import hango.composeapp.generated.resources.ic_x
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun LoadScreen(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .graphicsLayer {
                    scaleX = 1.5f
                    scaleY = 1.5f
                }
        ) {
            Box(
                contentAlignment = Alignment.Center,
            ) {
                var position1 by remember { mutableIntStateOf(3) }
                var position2 by remember { mutableIntStateOf(2) }
                var position3 by remember { mutableIntStateOf(1) }
                var position4 by remember { mutableIntStateOf(0) }

                val positionAnimated1 = animateIntOffsetAsState(
                    targetValue = getNextOffset(position1),
                    label = "positionAnimated1",
                )

                val positionAnimated2 = animateIntOffsetAsState(
                    targetValue = getNextOffset(position2),
                    label = "positionAnimated2",
                )

                val positionAnimated4 = animateIntOffsetAsState(
                    targetValue = getNextOffset(position4),
                    label = "positionAnimated4",
                )

                val positionAnimated3 = animateIntOffsetAsState(
                    targetValue = getNextOffset(position3),
                    label = "positionAnimated3",
                )

                LaunchedEffect(position1) {
                    delay(1000)
                    position1 = (position1 + 1) % 4
                    position2 = (position2 + 1) % 4
                    position4 = (position4 + 1) % 4
                    position3 = (position3 + 1) % 4
                }

                val images = listOf(
                    Triple(Res.drawable.ic_o, positionAnimated1.value, Color(0XFF0a2579)),
                    Triple(Res.drawable.ic_x, positionAnimated4.value, Color(0XFF8c1c3a)),
                    Triple(Res.drawable.ic_x, positionAnimated2.value, Color(0XFF8c1c3a)),
                    Triple(Res.drawable.ic_o, positionAnimated3.value, Color(0XFF0a2579))
                )

                images.forEach { (drawable, position, color) ->
                    GameImage(drawableRes = drawable, position = position, borderColor = color)
                }
            }

            Spacer(modifier = Modifier.height(56.dp))
            Text(
                text = "Carregando...",
                fontSize = MaterialTheme.typography.titleSmall.fontSize,
                color = Color.White
            )
        }
    }
}

@Composable
private fun getNextOffset(
    mPosition: Int
): IntOffset {
    val mapOfDirections: List<Pair<String, IntOffset>> = listOf(
        "topStart" to IntOffset(-75, 0),
        "bottomStart" to IntOffset(-75, 150),
        "bottomEnd" to IntOffset(75, 150),
        "topEnd" to IntOffset(75, 0),
    )

    return when (mPosition) {

        0 -> mapOfDirections[1].second
        1 -> mapOfDirections[2].second
        2 -> mapOfDirections[3].second
        3 -> mapOfDirections[0].second
        else -> mapOfDirections[0].second
    }
}


@Composable
fun GameImage(
    drawableRes: DrawableResource,
    position: IntOffset,
    borderColor: Color
) {
    Image(
        painter = painterResource(drawableRes),
        contentDescription = null,
        modifier = Modifier
            .size(50.dp)
            .padding(2.dp)
            .offset { position }
            .clip(RoundedCornerShape(10.dp))
            .border(
                width = 3.dp,
                color = borderColor,
                shape = RoundedCornerShape(10.dp)
            )
    )
}