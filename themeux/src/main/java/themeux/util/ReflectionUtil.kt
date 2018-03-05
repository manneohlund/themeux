package themeux.util

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
            return (item as KFunction<Any>).call(instance)
        } else if (item is KProperty) {
            return (item as KProperty1<Any, Any>).get(instance)
        } else {
            throw Exception("item is not a function or var/val")
        }
    }

    @Throws(Exception::class)
    fun getValue(themeModel: Any, annotationClass: KClass<*>, isOptional: Boolean = false): Any? {
        val members = themeModel.javaClass.kotlin.declaredMembers.filter {
            it.annotations.any { it.annotationClass == annotationClass }
        }
        if (members.size > 1) {
            throw Exception("Error! Add @${annotationClass.simpleName} annotation to only one field or function")
        } else if (!isOptional && members.isEmpty()) {
             throw NoSuchElementException("${annotationClass.simpleName} annotation not found! Add @${annotationClass.simpleName} annotation to only one field or function")
        } else if (isOptional && members.isEmpty()) {
            Log.e(javaClass.name, "Optional ${annotationClass.simpleName} annotation not found!")
            return null
        } else {
            return getValue(members.first(), themeModel)
        }
    }
}