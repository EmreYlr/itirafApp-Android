package com.itirafapp.android.util.manager

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.itirafapp.android.BuildConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GoogleAuthManager @Inject constructor(
    @param:ApplicationContext private val appContext: Context
) {
    private val WEB_CLIENT_ID = BuildConfig.WEB_CLIENT_ID

    suspend fun signIn(context: Context): String? {
        val credentialManager = CredentialManager.create(context)

        try {
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(WEB_CLIENT_ID)
                .setAutoSelectEnabled(false)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val result = credentialManager.getCredential(
                request = request,
                context = context
            )

            return when (val credential = result.credential) {
                is CustomCredential -> {
                    if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                        val googleIdTokenCredential =
                            GoogleIdTokenCredential.createFrom(credential.data)
                        googleIdTokenCredential.idToken
                    } else {
                        Log.e("GoogleAuth", "Beklenmeyen credential tipi: ${credential.type}")
                        null
                    }
                }

                else -> {
                    Log.e("GoogleAuth", "Credential tipi Custom değil")
                    null
                }
            }
        } catch (e: GetCredentialException) {
            Log.w("GoogleAuth", "Giriş başarısız veya iptal edildi: ${e.message}")
            return null
        } catch (e: Exception) {
            Log.e("GoogleAuth", "Bilinmeyen hata: ${e.message}")
            return null
        }
    }
}


