package jsonthemer.util

import android.util.Log
import kotlin.reflect.*
import kotlin.reflect.full.declaredMembers
import kotlin.reflect.jvm.isAccessible

/**
 * Created by Manne Öhlund on 2018-02-28.
 * Copyright © 2018. All rights reserved.
 */

object ReflectionUtil {
    private fun getValue(item: KCallable<*>, instance: Any): Any {
        item.isAccessible = true
        if (item is KFunction) {
            Log.e("", "setupTheme: FUNCTION")
            return (item as KFunction<Any>).call(instance)
        } else if (item is KProperty) {
            Log.e("", "setupTheme: PROPERTY")
            return (item as KProperty1<Any, Any>).get(instance)
        } else {
            throw Exception("item is not a function or var/val")
        }
    }

    fun getValue(themeModel: Any, annotationClass: KClass<*>): Any {
        val members = themeModel.javaClass.kotlin.declaredMembers.filter {
            it.annotations.any { it.annotationClass == annotationClass }
        }
        if (members.size > 1) {
            throw Exception("Error! Add @${annotationClass.simpleName} annotation to only one field or function")
        } else if (members.isEmpty()) {
            throw Exception("${annotationClass.simpleName} annotation not found! Add @${annotationClass.simpleName} annotation to only one field or function")
        }
        return getValue(members.first(), themeModel)
    }
}