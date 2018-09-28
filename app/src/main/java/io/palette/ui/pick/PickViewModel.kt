package io.palette.ui.pick

import android.app.Application
import android.net.Uri
import android.support.v4.app.FragmentManager
import io.palette.data.models.Response
import io.palette.data.models.Source
import io.palette.repository.Repository
import io.palette.ui.base.BaseAndroidViewModel
import io.palette.viewmodel.ActionLiveData
import io.palette.utility.extentions.fromWorkerToMain
import io.palette.utility.rx.Scheduler
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class PickViewModel @Inject constructor(
        application: Application,
        val repository: Repository,
        val scheduler: Scheduler
) : BaseAndroidViewModel(application) {

    val image = ActionLiveData<Response<Uri>>()

    fun openImagePicker(fragmentManager: FragmentManager, source: Source) {
        repository.pickRepository.getImage(fragmentManager, source)
                .fromWorkerToMain(scheduler)
                .subscribeBy(
                        onNext = {
                            image.sendAction(Response.success(it))
                        },
                        onError = {
                            image.sendAction(Response.error(it))
                        }
                )
                .addTo(getCompositeDisposable())
    }
}