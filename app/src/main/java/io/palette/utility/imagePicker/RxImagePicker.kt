package io.palette.utility.imagePicker

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat

import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Date
import java.util.Locale

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

import android.app.Activity.RESULT_OK
import io.palette.data.models.Source

class RxImagePicker : Fragment() {

    private var attachedSubject: PublishSubject<Boolean>? = null
    private var publishSubject: PublishSubject<Uri>? = null
    private var publishSubjectMultipleImages: PublishSubject<List<Uri>>? = null
    private var canceledSubject: PublishSubject<Int>? = null

    private var allowMultipleImages = false
    private var imageSource: Source? = null

    fun requestImage(source: Source): Observable<Uri> {
        publishSubject = PublishSubject.create()
        attachedSubject = PublishSubject.create()
        canceledSubject = PublishSubject.create()
        allowMultipleImages = false
        imageSource = source
        requestPickImage()
        return publishSubject!!.takeUntil(canceledSubject!!)
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    fun requestMultipleImages(): Observable<List<Uri>> {
        publishSubjectMultipleImages = PublishSubject.create()
        attachedSubject = PublishSubject.create()
        canceledSubject = PublishSubject.create()
        imageSource = Source.GALLERY
        allowMultipleImages = true
        requestPickImage()
        return publishSubjectMultipleImages!!.takeUntil(canceledSubject!!)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        attachedSubject!!.onNext(true)
        attachedSubject!!.onComplete()
    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        attachedSubject!!.onNext(true)
        attachedSubject!!.onComplete()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            pickImage()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                SELECT_PHOTO -> handleGalleryResult(data)
                TAKE_PHOTO -> onImagePicked(cameraPictureUrl)
            }
        } else {
            canceledSubject!!.onNext(requestCode)
        }
    }

    private fun handleGalleryResult(data: Intent?) {
        if (allowMultipleImages) {
            val imageUris = ArrayList<Uri>()
            val clipData = data!!.clipData
            if (clipData != null) {
                for (i in 0 until clipData.itemCount) {
                    imageUris.add(clipData.getItemAt(i).uri)
                }
            } else {
                imageUris.add(data.data)
            }
            onImagesPicked(imageUris)
        } else {
            onImagePicked(data!!.data)
        }
    }

    private fun requestPickImage() {
        if (!isAdded) {
            attachedSubject!!.subscribe { pickImage() }
        } else {
            pickImage()
        }
    }

    private fun pickImage() {
        if (!checkPermission()) {
            return
        }

        var chooseCode = 0
        var pictureChooseIntent: Intent? = null

        when (imageSource) {
            Source.CAMERA -> {
                cameraPictureUrl = createImageUri()
                pictureChooseIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                pictureChooseIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraPictureUrl)
                chooseCode = TAKE_PHOTO
            }
            Source.GALLERY -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    pictureChooseIntent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                    pictureChooseIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, allowMultipleImages)
                    pictureChooseIntent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
                } else {
                    pictureChooseIntent = Intent(Intent.ACTION_GET_CONTENT)
                }
                pictureChooseIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
                pictureChooseIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                pictureChooseIntent.type = "image/*"
                chooseCode = SELECT_PHOTO
            }
        }

        startActivityForResult(pictureChooseIntent, chooseCode)
    }

    private fun checkPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(activity!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 0)
            }
            return false
        } else {
            return true
        }
    }

    private fun createImageUri(): Uri? {
        val contentResolver = activity!!.contentResolver
        val cv = ContentValues()
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        cv.put(MediaStore.Images.Media.TITLE, timeStamp)
        return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv)
    }

    private fun onImagesPicked(uris: List<Uri>) {
        if (publishSubjectMultipleImages != null) {
            publishSubjectMultipleImages!!.onNext(uris)
            publishSubjectMultipleImages!!.onComplete()
        }
    }

    private fun onImagePicked(uri: Uri?) {
        if (publishSubject != null) {
            publishSubject!!.onNext(uri!!)
            publishSubject!!.onComplete()
        }
    }

    companion object {

        private val SELECT_PHOTO = 100
        private val TAKE_PHOTO = 101

        private var cameraPictureUrl: Uri? = null

        fun with(fragmentManager: FragmentManager): RxImagePicker {
            var rxImagePickerFragment: RxImagePicker?
            rxImagePickerFragment = fragmentManager.findFragmentByTag(RxImagePicker::class.java.simpleName) as RxImagePicker?
            if (rxImagePickerFragment == null) {
                rxImagePickerFragment = RxImagePicker()
                fragmentManager.beginTransaction()
                        .add(rxImagePickerFragment, RxImagePicker::class.java.simpleName)
                        .commit()
            }
            return rxImagePickerFragment
        }
    }

}
