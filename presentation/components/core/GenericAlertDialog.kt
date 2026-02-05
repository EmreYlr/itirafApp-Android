package com.itirafapp.android.presentation.components.core

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.itirafapp.android.presentation.ui.theme.ItirafAppTheme
import com.itirafapp.android.presentation.ui.theme.ItirafTheme

@Composable
fun GenericAlertDialog(
    onDismissRequest: () -> Unit,
    title: String,
    text: String,
    confirmButtonText: String,
    onConfirmClick: () -> Unit,
    dismissButtonText: String? = null,
    onDismissClick: (() -> Unit)? = null,
    confirmButtonColor: Color = ItirafTheme.colors.brandPrimary,
    isDestructive: Boolean = false,
    checkboxText: String? = null,
    isCheckboxChecked: Boolean = false,
    onCheckboxCheckedChange: ((Boolean) -> Unit)? = null
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = ItirafTheme.colors.textPrimary,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Column {
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyMedium,
                    color = ItirafTheme.colors.textSecondary,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )

                if (checkboxText != null && onCheckboxCheckedChange != null) {
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onCheckboxCheckedChange(!isCheckboxChecked)
                            }
                    ) {
                        Checkbox(
                            checked = isCheckboxChecked,
                            onCheckedChange = onCheckboxCheckedChange,
                            colors = CheckboxDefaults.colors(
                                checkedColor = ItirafTheme.colors.brandPrimary,
                                uncheckedColor = ItirafTheme.colors.textTertiary
                            )
                        )

                        Text(
                            text = checkboxText,
                            style = MaterialTheme.typography.bodyMedium,
                            color = ItirafTheme.colors.textPrimary
                        )
                    }
                }
            }
        },
        confirmButton = {
            val buttonContent = @Composable {
                Button(
                    onClick = onConfirmClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isDestructive) ItirafTheme.colors.statusError else confirmButtonColor,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = confirmButtonText)
                }
            }

            if (dismissButtonText == null) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    buttonContent()
                }
            } else {
                buttonContent()
            }
        },
        dismissButton = if (dismissButtonText != null && onDismissClick != null) {
            {
                TextButton(
                    onClick = onDismissClick,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = ItirafTheme.colors.textSecondary
                    )
                ) {
                    Text(text = dismissButtonText)
                }
            }
        } else null,
        containerColor = ItirafTheme.colors.backgroundCard,
        textContentColor = ItirafTheme.colors.textSecondary,
        titleContentColor = ItirafTheme.colors.textPrimary
    )
}

@Preview(name = "1. Tek Buton (Info)", showBackground = true)
@Composable
private fun PreviewSingleButtonAlert() {
    ItirafAppTheme {
        GenericAlertDialog(
            onDismissRequest = {},
            title = "Başarılı",
            text = "Profil bilgileriniz başarıyla güncellendi.",
            confirmButtonText = "Tamam",
            onConfirmClick = {},
        )
    }
}

@Preview(name = "2. İki Buton (Normal)", showBackground = true)
@Composable
private fun PreviewTwoButtonAlert() {
    ItirafAppTheme {
        GenericAlertDialog(
            onDismissRequest = {},
            title = "Çıkış Yap",
            text = "Uygulamadan çıkış yapmak istediğinize emin misiniz?",
            confirmButtonText = "Evet, Çık",
            onConfirmClick = {},
            dismissButtonText = "İptal",
            onDismissClick = {}
        )
    }
}

@Preview(name = "3. Kritik İşlem (Silme)", showBackground = true)
@Composable
private fun PreviewDestructiveAlert() {
    ItirafAppTheme {
        GenericAlertDialog(
            onDismissRequest = {},
            title = "İtirafı Sil",
            text = "Bu itirafı kalıcı olarak silmek üzeresiniz. Bu işlem geri alınamaz.",
            confirmButtonText = "Sil",
            onConfirmClick = {},
            dismissButtonText = "Vazgeç",
            onDismissClick = {},
            isDestructive = true,
            checkboxText = "Bu itirafi gizle",
            isCheckboxChecked = false,
            onCheckboxCheckedChange = {}
        )
    }
}

@Preview(
    name = "4. Dark Mode (Silme)",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun PreviewDarkModeAlert() {
    ItirafAppTheme {
        GenericAlertDialog(
            onDismissRequest = {},
            title = "Hesabı Sil",
            text = "Hesabınızı silmek istediğinize emin misiniz? Tüm verileriniz kaybolacak.",
            confirmButtonText = "Hesabı Sil",
            onConfirmClick = {},
            dismissButtonText = "İptal",
            onDismissClick = {},
            isDestructive = true,
        )
    }
}
