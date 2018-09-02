package io.palette.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.net.Uri
import io.palette.data.models.Response
import io.palette.data.models.Sources
import io.palette.repository.Repository
import io.palette.utility.ActionLiveData
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class PickViewModel @Inject constructor(
        application: Application,
        val repository: Repository
) : AndroidViewModel(application) {

    val image = ActionLiveData<Response<Uri>>()

    fun openImagePicker(sources: Sources) {
        repository.pickRepository.getImage(getApplication(), sources)
                .subscribeBy(
                        onNext = {
                            image.sendAction(Response(status = Response.ViewState.SUCCESS, data = it, error = null))
                        },
                        onError = {
                            image.sendAction(Response(status = Response.ViewState.ERROR, data = null, error = it))
                        }
                )
    }
}