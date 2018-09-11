package io.palette.ui.profile

import android.app.Activity.RESULT_OK
import android.arch.lifecycle.ViewModelProvider
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import io.palette.R
import io.palette.data.models.Response
import io.palette.di.FragmentScoped
import io.palette.ui.base.BaseFragment
import io.palette.utility.extentions.getViewModel
import io.palette.utility.extentions.observe
import io.palette.utility.extentions.toast
import io.palette.utility.extentions.visible
import kotlinx.android.synthetic.main.fragment_profile.*
import javax.inject.Inject

@FragmentScoped
class ProfileFragment @Inject constructor() : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

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

        btnLogin.setOnClickListener {
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(listOf(AuthUI.IdpConfig.GoogleBuilder().build()))
                            .build(),
                    RC_SIGN_IN)
        }

        viewModel.setUser(null)
        observe(viewModel.user) {
            it ?: return@observe
            when (it.status) {
                Response.Status.LOADING -> toast("Login Loading ../")
                Response.Status.SUCCESS -> {
                    when (it.data == null) {
                        true -> {
                            ivProfile.visible = false
                            tvName.visible = false
                            tvEmail.visible = false
                            btnLogin.visible = true
                        }
                        false -> {
                            it.data ?: return@observe
                            Glide.with(this)
                                    .setDefaultRequestOptions(RequestOptions().apply {
                                        placeholder(R.color.colorBlackThree)
                                        error(R.color.colorBlackThree)
                                        circleCrop()
                                    })
                                    .load(it.data.photoUrl)
                                    .into(ivProfile)
                            tvName.text = it.data.displayName
                            tvEmail.text = it.data.email

                            ivProfile.visible = true
                            tvName.visible = true
                            tvEmail.visible = true
                            btnLogin.visible = false
                        }
                    }
                }
                Response.Status.ERROR -> {
                    ivProfile.visible = false
                    tvName.visible = false
                    tvEmail.visible = false
                    btnLogin.visible = true
                    toast("Error occurred while signing. Please try again.")
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN)
            viewModel.setUser(if (resultCode == RESULT_OK) null else IdpResponse.fromResultIntent(data))
    }
}