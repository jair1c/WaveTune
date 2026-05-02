package com.wavetune.ui.screens.welcome

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun WelcomeScreen(onGetStarted: () -> Unit) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(200)
        visible = true
    }

    val infiniteTransition = rememberInfiniteTransition(label = "bg")
    val blob1Scale by infiniteTransition.animateFloat(
        initialValue = 0.9f, targetValue = 1.1f,
        animationSpec = infiniteRepeatable(tween(4000, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "b1"
    )
    val blob2Scale by infiniteTransition.animateFloat(
        initialValue = 1.05f, targetValue = 0.92f,
        animationSpec = infiniteRepeatable(tween(3500, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "b2"
    )
    val blob3Scale by infiniteTransition.animateFloat(
        initialValue = 0.95f, targetValue = 1.08f,
        animationSpec = infiniteRepeatable(tween(5000, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "b3"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Decorative blobs
        Box(
            modifier = Modifier
                .size(280.dp)
                .offset((-60).dp, (-40).dp)
                .scale(blob1Scale)
                .background(
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.06f),
                    CircleShape
                )
                .blur(30.dp)
        )
        Box(
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.TopEnd)
                .offset(40.dp, 80.dp)
                .scale(blob2Scale)
                .background(
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.04f),
                    CircleShape
                )
                .blur(20.dp)
        )
        Box(
            modifier = Modifier
                .size(240.dp)
                .align(Alignment.BottomCenter)
                .offset(60.dp, 40.dp)
                .scale(blob3Scale)
                .background(
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
                    CircleShape
                )
                .blur(40.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top: App name
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(600)) + slideInVertically(tween(600)) { -20 }
            ) {
                Text(
                    text = "WaveTune",
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontWeight = FontWeight.Black,
                        letterSpacing = (-2).sp
                    ),
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            // Center: Visual element (music note orbs)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(tween(800, delayMillis = 300)) + scaleIn(tween(800, delayMillis = 300))
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        // Outer ring
                        Box(
                            modifier = Modifier
                                .size(260.dp)
                                .scale(blob1Scale)
                                .background(
                                    MaterialTheme.colorScheme.surfaceVariant,
                                    CircleShape
                                )
                        )
                        // Inner content
                        Box(
                            modifier = Modifier
                                .size(200.dp)
                                .background(
                                    MaterialTheme.colorScheme.surface,
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "♪",
                                fontSize = 80.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        // Small orbiting circles
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .offset(110.dp, (-30).dp)
                                .scale(blob2Scale)
                                .background(
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                                    CircleShape
                                )
                        )
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .offset((-120).dp, 50.dp)
                                .scale(blob3Scale)
                                .background(
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                                    CircleShape
                                )
                        )
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .offset(80.dp, 110.dp)
                                .scale(blob1Scale)
                                .background(
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                    CircleShape
                                )
                        )
                    }
                }
            }

            // Bottom: Headline + CTA
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(700, delayMillis = 500)) + slideInVertically(tween(700, delayMillis = 500)) { 30 }
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(SpanStyle(fontWeight = FontWeight.Light)) { append("Elevate Every\nMoment With ") }
                            withStyle(SpanStyle(fontWeight = FontWeight.Black)) { append("Music") }
                        },
                        style = MaterialTheme.typography.displayMedium.copy(fontSize = 34.sp),
                        color = MaterialTheme.colorScheme.onBackground,
                        lineHeight = 40.sp
                    )
                    Text(
                        text = "Your local music, beautifully organized. No account needed.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(8.dp))
                    Button(
                        onClick = onGetStarted,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text(
                            text = "Start Listening",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                        )
                    }
                    Spacer(Modifier.height(16.dp))
                }
            }
        }
    }
}
