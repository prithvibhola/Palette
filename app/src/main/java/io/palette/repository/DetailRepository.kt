package io.palette.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import android.os.Environment
import android.support.v4.content.FileProvider
import android.support.v4.util.LruCache
import android.support.v7.graphics.Palette
import android.support.v7.widget.RecyclerView
import android.view.View
import io.palette.data.models.GeneratedPalette
import io.palette.utility.extentions.toHex
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DetailRepository @Inject constructor() {

    @Inject lateinit var context: Context

    private val PALETTE_FOLDER = File(Environment.getExternalStorageDirectory(), "Palette")

    fun getPalette(bitmap: Bitmap): Flowable<List<GeneratedPalette>> =
            Flowable.just(bitmap)
                    .map { Palette.from(bitmap).generate().swatches }
                    .flatMap { Flowable.fromIterable(it) }
                    .map { GeneratedPalette(hexCode = it.rgb.toHex(), population = it.population) }
                    .toList()
                    .map { it.sortedByDescending { it.population } }
                    .toFlowable()

    fun generateBitmap(view: RecyclerView, imageBitmap: Bitmap): Bitmap? {
        val adapter = view.adapter
        lateinit var bitmap: Bitmap
        if (adapter != null) {
            val bitmapCache: LruCache<String, Bitmap> = LruCache((Runtime.getRuntime().maxMemory() / 1024).toInt() / 8)
            var height = 0
            var iHeight = 0F

            for (i in 0 until adapter.itemCount) {
                val holder = adapter.createViewHolder(view, adapter.getItemViewType(i))
                adapter.onBindViewHolder(holder, i)
                holder.itemView.apply {
                    measure(View.MeasureSpec.makeMeasureSpec(view.width, View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(150, View.MeasureSpec.EXACTLY))
                    layout(0, 0, measuredWidth, measuredHeight)
                    isDrawingCacheEnabled = true
                    buildDrawingCache()
                    if (drawingCache != null) bitmapCache.put(i.toString(), drawingCache)
                    height += measuredHeight
                }
            }

            bitmap = Bitmap.createBitmap(view.measuredWidth, height, Bitmap.Config.ARGB_8888)
            val bigCanvas = Canvas(bitmap)
            bigCanvas.drawColor(Color.WHITE)

            for (i in 0 until adapter.itemCount) {
                val bitmap1 = bitmapCache.get(i.toString())
                bigCanvas.drawBitmap(bitmap1, 0f, iHeight, Paint())
                iHeight += bitmap1.height
                bitmap1.recycle()
            }
        }
        return combineBitmap(imageBitmap, bitmap)
    }

    private fun combineBitmap(bitmap1: Bitmap, bitmap2: Bitmap): Bitmap {
        val width: Int
        val height: Int
        val cs: Bitmap?

        if (bitmap1.width > bitmap2.width) {
            width = bitmap1.width
            height = bitmap1.height + bitmap2.height
        } else {
            width = bitmap2.width
            height = bitmap1.height + bitmap2.height
        }

        cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        val comboImage = Canvas(cs)

        comboImage.drawBitmap(bitmap1, 0f, 0f, null)
        comboImage.drawBitmap(bitmap2, 0f, bitmap1.height.toFloat(), null)
        return cs
    }

    fun saveBitmap(bitmap: Bitmap): Flowable<Uri> = Flowable.create<Uri>({ emitter ->
        if (!PALETTE_FOLDER.exists())
            PALETTE_FOLDER.mkdirs()
        val newsFile = File(PALETTE_FOLDER, "palette_${System.currentTimeMillis()}.jpg")
        if (newsFile.exists()) newsFile.delete()
        newsFile.createNewFile()
        try {
            val out = FileOutputStream(newsFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            val uri = FileProvider.getUriForFile(
                    context,
                    context.packageName + ".fileprovider", newsFile)
            emitter.onNext(uri)
            out.flush()
            out.close()
            emitter.onComplete()
        } catch (exception: Exception) {
            emitter.onError(exception)
        }
    }, BackpressureStrategy.BUFFER)
}