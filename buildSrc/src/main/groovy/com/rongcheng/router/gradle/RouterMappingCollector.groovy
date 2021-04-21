package com.rongcheng.router.gradle

import java.util.jar.JarEntry
import java.util.jar.JarFile

class RouterMappingCollector {
    private static final String PACKAGE_NAME = 'com/rong/cheng/router/mapping'
    private static final String CLASS_NAME_PREFIX = 'RouterMapping_'
    private static final String CLASS_NAME_SUFFIX = '.class'

    private final Set<String> mappingClassNames = new HashSet<>()


    /**
     * 获取收集好的映射表的类名
     * @return
     */
    Set<String> getMappingClassName() {
        return mappingClassNames
    }

    /**
     * 收集class文件或者class文件目录中的映射表类
     * @param classFile
     */
    void collectFromFile(File classFile) {
        if (classFile == null || !classFile.exists()) return
        if (classFile.isFile()) {
            if (classFile.absolutePath.contains(PACKAGE_NAME)
                    && classFile.name.startsWith(CLASS_NAME_PREFIX)
                    && classFile.name.endsWith(CLASS_NAME_SUFFIX)) {
                //去掉.class后缀
                String className = classFile.name.replace(CLASS_NAME_SUFFIX, "")
                mappingClassNames.add(className)
            }
        } else {
            classFile.listFiles().each {
                collectFromFile(it)
            }
        }

    }
    /**
     * 收集JAR包中的目标类
     * @param jarFile
     */
    void collectFromJarFile(File jarFile) {

        Enumeration enumeration =
                new JarFile(jarFile).entries()
        while (enumeration.hasMoreElements()) {
            JarEntry jarEntry =
                    enumeration.nextElement()
            String entryName = jarEntry.getName()
            if (entryName.contains(PACKAGE_NAME)
                    && entryName.contains(CLASS_NAME_PREFIX)
                    && entryName.contains(CLASS_NAME_SUFFIX)) {
                //去掉.class后缀
                String className = entryName
                        .replace(PACKAGE_NAME,"")
                        .replace(CLASS_NAME_SUFFIX, "")
                        .replace("/","")
                mappingClassNames.add(className)
            }

        }

    }
}