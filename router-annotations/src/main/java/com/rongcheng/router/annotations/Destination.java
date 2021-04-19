package com.rongcheng.router.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: frc
 * @description:
 * @date: 4/19/21 7:05 PM
 */
//注解可以修饰类上面
@Target({ElementType.TYPE})
//注解保留到什么时间点 : 保留到编译期
@Retention(RetentionPolicy.CLASS)
public @interface Destination {
    /**
     * 当前页面的URl，不能为空
     *
     * @return 页面URL
     */
    String url();

    /**
     * 页面的描述
     *
     * @return 标记页面，例如"个人主页"
     */
    String description();
}
