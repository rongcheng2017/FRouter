package com.rongcheng.router.gradle

import jdk.internal.org.objectweb.asm.ClassWriter
import jdk.internal.org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

/**
 * 通过字节码来生成RouterMapping
 */
class RouterMappingByteCoBuilder implements Opcodes {
    public static final String CLASS_NAME = "com/rong/cheng/router/mapping/generated/RouterMapping"

    static byte[] get(Set<String> allMappingNames) {
        //1. 创建一个类
        //2. 创建一个构造方法
        //3. 创建get方法
        //    创意一个map
        //    塞入所有映射表内容
        //    返回map


        //COMPUTE_MAXS自动计算局部变量表的大小
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS)

        //创建一个类
        cw.visit(V1_8, ACC_PUBLIC | ACC_SUPER,
                CLASS_NAME,
                null,
                "java/lang/Object",
                null)
        //生成或者编辑方法
        MethodVisitor mv
        //创建构造方法

        mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null)
        //开启字节码的生成和访问
        mv.visitCode()
        mv.visitVarInsn(ALOAD, 0)
        //调用父类构造方法
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false)
        mv.visitInsn(RETURN)
        //设置局部变量大小，自动计算
        mv.visitMaxs(1, 1)
        mv.visitEnd()

        //创建get()方法
        mv = cw.visitMethod(ACC_PUBLIC | ACC_STATIC,
                "get",
                "()Ljava/util/Map;",
                "()Ljava/util/Map<Ljava/lang/String;Ljava/lang/String;>;",
                null)

        mv.visitCode()
        //创建HashMap
        mv.visitTypeInsn(NEW, "java/util/HashMap")
        //入栈
        mv.visitInsn(DUP)
        mv.visitMethodInsn(INVOKESPECIAL,
                "java/util/HashMap",
                "<init>", "()V", false
        )
        mv.visitVarInsn(ASTORE,0)

        //向Map中，逐个塞入映射表内容
        allMappingNames.each {
            mv.visitVarInsn(ALOAD,0)
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESTATIC,
                    "com/rong/cheng/router/mapping/$it",
                    "get", "()Ljava/util/Map;", false)
            mv.visitMethodInsn(INVOKEINTERFACE,
                    "java/util/Map",
                    "putAll",
                    "(Ljava/util/Map;)V", true)
        }

        //返回 Map
        mv.visitVarInsn(ALOAD,0)
        mv.visitInsn(ARETURN)
        mv.visitMaxs(2,2)

        mv.visitEnd()

        return  cw.toByteArray()

    }
}