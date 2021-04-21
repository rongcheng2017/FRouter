package com.rong.cheng.router_runtime

import android.util.Log

/**
 * @author: frc
 * @description:
 * @date:  4/21/21 6:55 PM
 *
 */

object Router {

    private const val TAG = "RouterTAG"

    //编译期间生成的总映射表
    private const val GENERATED_MAPPING =
        "com.rong.cheng.router.mapping.generated.RouterMapping"

    //存储所有映射表信息
    private val mapping: HashMap<String, String> = HashMap();


    fun init() {
        try {
            val clazz = Class.forName(GENERATED_MAPPING)
            val method = clazz.getMethod("get")
            val allMapping = method.invoke(null) as Map<String, String>
            if (allMapping.isNotEmpty()) {
                Log.i(TAG, "init: get all mapping:")
                allMapping.onEach {
                    Log.i(TAG, "     ${it.key} -> ${it.value}")
                }
                mapping.putAll(allMapping)
            }
        } catch (e: Throwable) {
            Log.e(TAG, "init: error while init router : $e")
        }
    }


}