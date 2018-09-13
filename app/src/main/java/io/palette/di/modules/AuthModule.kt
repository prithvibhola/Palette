package io.palette.di.modules

import android.app.Application
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AuthModule {

    @Singleton
    @Provides
    fun provideGoogleSigInOptions(): GoogleSignInOptions {
        return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("997316412435-ic3sug0ob2k3fuhjlstqp4qc4u6u9lf2.apps.googleusercontent.com")
                .requestEmail()
                .build()
    }

    @Singleton
    @Provides
    fun providesGoogleSigInClient(application: Application, gso: GoogleSignInOptions): GoogleSignInClient {
        return GoogleSignIn.getClient(application, gso)
    }

    @Singleton
    @Provides
    fun providesFirebaseAuth() = FirebaseAuth.getInstance()

    @Singleton
    @Provides
    fun providesFirebaseDatabase() = FirebaseDatabase.getInstance().reference
}