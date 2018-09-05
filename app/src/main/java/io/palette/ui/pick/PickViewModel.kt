package io.palette.ui.pick

import android.app.Application
import android.net.Uri
import io.palette.data.models.Response
import io.palette.data.models.Source
import io.palette.repository.Repository
import io.palette.ui.base.BaseAndroidViewModel
import io.palette.utility.ActionLiveData
import io.palette.utility.extentions.fromWorkerToMain
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import prithvi.io.mvvmstarter.utility.rx.Scheduler
import javax.inject.Inject

class PickViewModel @Inject constructor(
        application: Application,
        val repository: Repository,
        val scheduler: Scheduler
) : BaseAndroidViewModel(application) {

    val image = ActionLiveData<Response<Uri>>()

    fun openImagePicker(source: Source) {
        repository.pickRepository.getImage(getApplication(), source)
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