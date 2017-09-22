package csnowstack.aspectlib;

import android.app.Activity;
import android.app.Fragment;
import android.os.Looper;
import android.os.MessageQueue;
import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

/**
 *
 */

@Aspect
public class AspectTrace {
    private static final String POINTCUT_METHOD =
            "execution(@csnowstack.aspectlib.DebugTrace * *(..))";
    private static final String POINTCUT_CONSTRUCTOR =
            "execution(@csnowstack.aspectlib.DebugTrace *.new(..))";


    /**
     * 筛选出debugTrace注解的method
     */
    @Pointcut(POINTCUT_METHOD)
    public void methodAnnotationWithDebugTrace() {
    }

    /**
     * 筛选出debugTrace注解的Constructor
     */
    @Pointcut(POINTCUT_CONSTRUCTOR)
    public void constructorAnnotationWithDebugTrace() {
    }


    /**
     * 是我们的注入在DebugTrace注解的地方生效
     *
     * 请给App读写手机存储的权限,否则会报错
     *
     */
    @Around("methodAnnotationWithDebugTrace() || constructorAnnotationWithDebugTrace()")
    public Object waveJoinPoint(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        Class declaringType = methodSignature.getDeclaringType();
        String className = declaringType.getName();
        String methodName = methodSignature.getName();
        Object object = proceedingJoinPoint.proceed();


        if (!Activity.class.isAssignableFrom(declaringType) &&
                !Fragment.class.isAssignableFrom(declaringType) &&
                !android.support.v4.app.Fragment.class.isAssignableFrom(declaringType)) {
            throw new RuntimeException(className + " 请在Activity,Fragment类里面使用");
        }

        if (!methodName.equals("onResume")) {
            throw new RuntimeException(className + "类里  注解必须在 onResume 方法上");
        }

        android.os.Debug.startMethodTracing(className);

        Log.e("-->>",className+".trace");
        Looper.myQueue().addIdleHandler(new MessageQueue.IdleHandler() {
            @Override
            public boolean queueIdle() {
                android.os.Debug.stopMethodTracing();

                return false;
            }
        });
        return object;
    }

    /**
     * Create a log message.
     *
     * @param methodName     A string with the method name.
     * @param methodDuration Duration of the method in milliseconds.
     * @return A string representing message.
     */
    private static String buildLogMessage(String methodName, long methodDuration) {
        StringBuilder message = new StringBuilder();
        message.append(methodName);
        message.append(" --> ");
        message.append("[");
        message.append(methodDuration);
        message.append("ms");
        message.append("]");

        return message.toString();
    }

}
