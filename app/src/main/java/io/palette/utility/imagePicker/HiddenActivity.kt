package io.palette.utility.imagePicker

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import io.palette.data.models.Sources
import java.text.SimpleDateFormat
import java.util.*

class HiddenActivity : Activity() {

    private var cameraPictureUrl: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            handleIntent(intent)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(KEY_CAMERA_PICTURE_URL, cameraPictureUrl)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        cameraPictureUrl = savedInstanceState.getParcelable(KEY_CAMERA_PICTURE_URL)
    }

    override fun onNewIntent(intent: Intent) {
        handleIntent(intent)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            handleIntent(intent)
        } else {
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                SELECT_PHOTO -> handleGalleryResult(data)
                TAKE_PHOTO -> RxImagePicker.with(this).onImagePicked(this.cameraPictureUrl!!)
            }
        }
        finish()
    }

    private fun handleGalleryResult(data: Intent) {
        if (intent.getBooleanExtra(ALLOW_MULTIPLE_IMAGES, false)) {
            val imageUris = ArrayList<Uri>()
            val clipData = data.clipData
            if (clipData != null) {
                for (i in 0 until clipData.itemCount) {
                    imageUris.add(clipData.getItemAt(i).uri)
                }
            } else {
                imageUris.add(data.data)
            }
            RxImagePicker.with(this).onImagesPicked(imageUris)
        } else {
            RxImagePicker.with(this).onImagePicked(data.data)
        }
    }

    private fun makePictureChooseIntent(): Intent {
        val pictureChooseIntent: Intent
        val manufacturer = Build.MANUFACTURER
        if (manufacturer != null && manufacturer.toLowerCase() == "xiaomi") {
            pictureChooseIntent = Intent(Intent.ACTION_GET_CONTENT)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                pictureChooseIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,
                        intent.getBooleanExtra(ALLOW_MULTIPLE_IMAGES, false))
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                pictureChooseIntent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
            }

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            pictureChooseIntent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            pictureChooseIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,
                    intent.getBooleanExtra(ALLOW_MULTIPLE_IMAGES, false))
            pictureChooseIntent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
        } else {
            pictureChooseIntent = Intent(Intent.ACTION_GET_CONTENT)
        }
        pictureChooseIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
        pictureChooseIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        pictureChooseIntent.type = "image/*"
        return pictureChooseIntent
    }


    private fun handleIntent(intent: Intent) {
        if (!checkPermission()) return
        val sourceType = Sources.values()[intent.getIntExtra(IMAGE_SOURCE, 0)]
        when (sourceType) {
            Sources.CAMERA -> {
                cameraPictureUrl = createImageUri()
                val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraPictureUrl)
                startActivityForResult(takePhotoIntent, TAKE_PHOTO)
            }
            Sources.GALLERY -> {
                val pictureChooseIntent = makePictureChooseIntent()
                val manufacturer = Build.MANUFACTURER
                if (manufacturer != null && manufacturer.toLowerCase() == "xiaomi") {
                    val chooserIntent = Intent.createChooser(pictureChooseIntent, "Select images")
                    startActivityForResult(chooserIntent, SELECT_PHOTO)
                } else {
                    startActivityForResult(pictureChooseIntent, SELECT_PHOTO)
                }
            }
        }
    }

    private fun checkPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA), 0)
            return false
        } else {
            return true
        }
    }

    private fun createImageUri(): Uri? {
        val contentResolver = contentResolver
        val cv = ContentValues()
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        cv.put(MediaStore.Images.Media.TITLE, timeStamp)
        return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv)
    }

    companion object {

        private val KEY_CAMERA_PICTURE_URL = "cameraPictureUrl"

        val IMAGE_SOURCE = "image_source"
        val ALLOW_MULTIPLE_IMAGES = "allow_multiple_images"

        private val SELECT_PHOTO = 100
        private val TAKE_PHOTO = 101
    }

}