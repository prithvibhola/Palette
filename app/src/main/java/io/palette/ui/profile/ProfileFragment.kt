package io.palette.ui.profile

import android.app.Activity.RESULT_OK
import android.arch.lifecycle.ViewModelProvider
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
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

        viewModel = getViewModel(ProfileViewModel::class.java, viewModelFactory)

        observe(viewModel.user) {
            it ?: return@observe
            when (it.status) {
                Response.Status.LOADING -> toast("Login Loading ../")
                Response.Status.SUCCESS -> toast("Login success")
                Response.Status.ERROR -> toast("Login Error")
            }
        }

        when (mAuth.currentUser == null) {
            true -> {
                btnLogin.visible = true
                btnLogin.setOnClickListener {
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(listOf(AuthUI.IdpConfig.GoogleBuilder().build()))
                                    .build(),
                            RC_SIGN_IN)
                }
            }
            false -> btnLogin.visible = false
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)
            response ?: return
            if (resultCode == RESULT_OK) {
                val user = FirebaseAuth.getInstance().currentUser
            } else {
                Timber.e(response.error, "User could not sign in")
            }
        }
    }
}