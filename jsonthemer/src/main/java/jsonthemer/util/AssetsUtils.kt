package jsonthemer.util

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.io.InputStreamReader


/**
 * Created by Manne Öhlund on 2018-02-23.
 * Copyright © 2018 Frost. All rights reserved.
 */

class AssetsUtils {
    companion object {
        @Throws(IOException::class)
        inline fun <reified T : Any> loadStyleAssets(context: Context, fileName: String): T {
            val assetManager = context.assets
            val ims = assetManager.open(fileName)

            val gson = Gson()
            val reader = InputStreamReader(ims)

            val type = object : TypeToken<T>() {}.type
            return gson.fromJson(reader, type)
        }
    }
}