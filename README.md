# ViewTrace
取当前界面所有view,onMeasure,onLayout,onDraw的耗时

## 项目地址
https://github.com/CSnowStack/ViewTrace

## 抄了这些项目
- http://www.jianshu.com/p/b25be692f885
- http://www.jianshu.com/p/0fa8073fd144
> 真的是抄的,你看完这两个项目也能写出来

## 效果图
![order](https://github.com/CSnowStack/ViewTrace/blob/master/pics/pic_order.png)

![filter](https://github.com/CSnowStack/ViewTrace/blob/master/pics/pic_filter.png)
## 其实并没有什么卵用

## 使用说明

- `project`的`build.gradle`里增加
```java
 classpath "org.aspectj:aspectjtools:$aspectjtools_version"
```
- `import modeule aspectlib`
- `app`的 `build.gradle`里增加代码
```java
final def log = project.logger
final def variants = project.android.applicationVariants

variants.all { variant ->
    if (!variant.buildType.isDebuggable()) {
        log.debug("Skipping non-debuggable build type '${variant.buildType.name}'.")
        return;
    }

    JavaCompile javaCompile = variant.javaCompile
    javaCompile.doLast {
        String[] args = ["-showWeaveInfo",
                         "-1.5",
                         "-inpath", javaCompile.destinationDir.toString(),
                         "-aspectpath", javaCompile.classpath.asPath,
                         "-d", javaCompile.destinationDir.toString(),
                         "-classpath", javaCompile.classpath.asPath,
                         "-bootclasspath", project.android.bootClasspath.join(File.pathSeparator)]
        log.debug "ajc args: " + Arrays.toString(args)

        MessageHandler handler = new MessageHandler(true);
        new Main().run(args, handler);
        for (IMessage message : handler.getMessages(null, true)) {
            switch (message.getKind()) {
                case IMessage.ABORT:
                case IMessage.ERROR:
                case IMessage.FAIL:
                    log.error message.message, message.thrown
                    break;
                case IMessage.WARNING:
                    log.warn message.message, message.thrown
                    break;
                case IMessage.INFO:
                    log.info message.message, message.thrown
                    break;
                case IMessage.DEBUG:
                    log.debug message.message, message.thrown
                    break;
            }
        }
    }
}

```

- 在需要查看耗时界面的`onResume`方法上加个`DebugTrace`注解
```java
@DebugTrace
@Override protected void onResume() {
    super.onResume();
}
```

- 编译运行(`需要读写SD卡的权限`)
- 在项目里新建 `captures`目录
- 执行命令
```
//如csnowstack.viewtrace.MainActivity
adb pull /storage/emulated/0/你DebugTrace注解的方法所在类的名字(包括包名).trace captures/cqtrace.trace

```
- 在右边的`gradle`命令的`other`里先执行`AppOutPutOrder`再执行`AppFilterMethodOrder`
- 在`captures`文件夹里就会生成结果
