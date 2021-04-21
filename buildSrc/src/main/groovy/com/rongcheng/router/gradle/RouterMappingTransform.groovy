package com.rongcheng.router.gradle

import com.android.build.api.transform.Format
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.utils.FileUtils

class RouterMappingTransform extends Transform {

    /**
     * 当前Transform的名称
     * @return
     */
    @Override
    String getName() {
        return "RouterMappingTransform"
    }
    /**
     * 告知编译器，当前Transform需要消费的输入类型
     * @return
     */
    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }
    /**
     * 告知编译器，当前Transform需要手机的范围
     * @return
     */
    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        //手机整个工程的
        return TransformManager.SCOPE_FULL_PROJECT
    }

    /**
     * 是否支持增量
     * 通常返回False
     * @return
     */
    @Override
    boolean isIncremental() {
        return false
    }

    /**
     * 所有class收集好后，会被打包传入此方法
     * @param transformInvocation
     * @throws TransformException* @throws InterruptedException* @throws IOException
     */
    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
//        super.transform(transformInvocation)
        //1.变量所有的Input(必须)
        //2.对Input进行二次处理
        //3.将Input拷贝到目标目录(必须)

        RouterMappingCollector routerMappingCollector= new RouterMappingCollector()
        //遍历所有的输入
        transformInvocation.inputs.each {
            //文件夹类型
            it.directoryInputs.each { directoryInput ->
                def destDir = transformInvocation.outputProvider.getContentLocation(
                        directoryInput.name,
                        directoryInput.contentTypes,
                        directoryInput.scopes,
                        Format.DIRECTORY
                )
                routerMappingCollector.collectFromFile(directoryInput.file)
                FileUtils.copyDirectory(directoryInput.file, destDir)
            }
            //jar
            it.jarInputs.each {jarInput->
                def destDir = transformInvocation.outputProvider.getContentLocation(
                        jarInput.name,
                        jarInput.contentTypes,
                        jarInput.scopes,
                        Format.JAR
                )
                routerMappingCollector.collectFromJarFile(jarInput.file)
                FileUtils.copyFile(jarInput.file, destDir)
            }
        }

        println(" \n ${getName()}收集到的类 ： ${routerMappingCollector.getMappingClassName()}")

    }
}