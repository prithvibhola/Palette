package io.palette.ui.profile

import android.app.Activity.RESULT_OK
import android.arch.lifecycle.ViewModelProvider
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import io.palette.R
import io.palette.data.models.Response
import io.palette.data.models.Unsplash
import io.palette.di.FragmentScoped
import io.palette.ui.base.BaseFragment
import io.palette.ui.detail.DetailActivity
import io.palette.ui.settings.SettingsActivity
import io.palette.utility.extentions.*
import io.palette.utility.preference.PreferenceUtility
import kotlinx.android.synthetic.main.fragment_profile.*
import javax.inject.Inject

@FragmentScoped
class ProfileFragment @Inject constructor() : BaseFragment(), ProfileAdapter.Callback {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject lateinit var preference: PreferenceUtility

    lateinit var viewModel: ProfileViewModel
    lateinit var profileAdapter: ProfileAdapter

    private val RC_SIGN_IN = 9001

    companion object {
        fun newInstance() = ProfileFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

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

        ivStaggered.setOnClickListener {
            val invert = preference.prefLikedStaggered == 1
            preference.prefLikedStaggered = if (invert) 2 else 1
            ivStaggered.setImage(if (invert) R.drawable.ic_view_compact_black_24dp else R.drawable.ic_view_agenda_black_24dp)
            rvLikedPalettes.layoutManager = StaggeredGridLayoutManager(preference.prefLikedStaggered, StaggeredGridLayoutManager.VERTICAL)
            profileAdapter.notifyDataSetChanged()
        }

        profileAdapter = ProfileAdapter(requireContext(), this)
        rvLikedPalettes.apply {
            layoutManager = StaggeredGridLayoutManager(preference.prefLikedStaggered, StaggeredGridLayoutManager.VERTICAL)
            adapter = profileAdapter
        }

        viewModel.setUser(null)
        viewModel.getPalettes()
        observe(viewModel.user) {
            it ?: return@observe
            when (it.status) {
                Response.Status.LOADING -> { //Nothing to show
                }
                Response.Status.SUCCESS -> {
                    when (it.data == null) {
                        true -> {
                            grpUserInfo.visible = false
                            grpSignIn.visible = true
                        }
                        false -> {
                            it.data ?: return@observe
                            viewModel.getPalettes()
                            ivStaggered.setImage(if (preference.prefUnsplashStaggered == 1) R.drawable.ic_view_agenda_black_24dp else R.drawable.ic_view_compact_black_24dp)
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

                            grpUserInfo.visible = true
                            grpSignIn.visible = false
                        }
                    }
                }
                Response.Status.ERROR -> {
                    grpUserInfo.visible = false
                    grpSignIn.visible = true
                    rootLayout.snackBar(R.string.error_sign_in)
                }
            }
        }

        observe(viewModel.palettes) {
            it ?: return@observe
            when (it.status) {
                Response.Status.LOADING -> { //Nothing to show
                }
                Response.Status.SUCCESS -> {
                    it.data ?: return@observe
                    if (it.data.isNotEmpty()) {
                        profileAdapter.palettes = it.data
                        tvEmptyList.visible = false
                    } else {
                        tvEmptyList.visible = true
                    }
                }
                Response.Status.ERROR -> {
                    tvEmptyList.visible = true
                    rootLayout.snackBar(R.string.error_getting_palettes)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN)
            viewModel.setUser(if (resultCode == RESULT_OK) null else IdpResponse.fromResultIntent(data))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_profile, menu)
        menu.findItem(R.id.action_settings)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                startActivity(SettingsActivity.newInstance(requireContext()))
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun openDetail(view: View, unsplash: Unsplash) =
            startActivity(DetailActivity.newInstance(requireContext(), unsplash, true, true, true),
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                            requireActivity(),
                            view,
                            getString(R.string.transition_image_unsplash)
                    ).toBundle())
}