package io.palette.ui.profile

import android.arch.lifecycle.ViewModelProvider
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import io.palette.R
import io.palette.data.models.Response
import io.palette.di.FragmentScoped
import io.palette.ui.base.BaseFragment
import io.palette.utility.extentions.getViewModel
import io.palette.utility.extentions.observe
import io.palette.utility.extentions.toast
import io.palette.utility.extentions.visible
import kotlinx.android.synthetic.main.fragment_profile.*
import timber.log.Timber
import javax.inject.Inject

@FragmentScoped
class ProfileFragment @Inject constructor() : BaseFragment() {

    @Inject lateinit var mAuth: FirebaseAuth
    @Inject lateinit var mGoogleSignInClient: GoogleSignInClient
    private var mGoogleApiClient: GoogleApiClient? = null
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var viewModel: ProfileViewModel

    private val RC_SIGN_IN = 9001

    companion object {
        fun newInstance() = ProfileFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fragment_profile, container, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        Firebase.setAndroidContext(activity)

        viewModel = getViewModel(ProfileViewModel::class.java, viewModelFactory)

        observe(viewModel.user) {
            it ?: return@observe
            when (it.status) {
                Response.Status.LOADING -> toast("Login Loading ../")
                Response.Status.SUCCESS -> toast("Login success")
                Response.Status.ERROR -> toast("Login Error")
            }
        }

        //GOOGLE
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        mGoogleApiClient = GoogleApiClient.Builder(context!!)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()

        if (mAuth.currentUser == null) {
            btnLogin.visible = true
            btnLogin.setOnClickListener {
                val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
                startActivityForResult(signInIntent, RC_SIGN_IN)
            }
        } else {
            btnLogin.visible = false
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result.isSuccess) {
                val account = result.signInAccount
                viewModel.login(requireActivity(), account!!)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (mGoogleApiClient != null)
            mGoogleApiClient?.connect()
    }

    override fun onStop() {
        if (mGoogleApiClient != null && mGoogleApiClient?.isConnected!!) {
            mGoogleApiClient?.disconnect()
        }
        super.onStop()
    }
}