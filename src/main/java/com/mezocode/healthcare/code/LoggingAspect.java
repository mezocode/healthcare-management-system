package com.mezocode.healthcare.code;

import com.mezocode.healthcare.shared.annotation.Loggable;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);


    // Pointcut for all methods within classes annotated with @Loggable
    @Pointcut("within(@com.mezocode.healthcare.shared.annotation.Loggable *)")
    public void beanAnnotatedWithLoggable() {
    }

    // Pointcut for all methods which are annotated with @Loggable
    @Pointcut("@annotation(com.mezocode.healthcare.shared.annotation.Loggable)")
    public void methodAnnotatedWithLoggable() {
    }

    @Around("@annotation(com.mezocode.healthcare.shared.annotation.LogExecution)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();

        logger.info("Execution time of {} is {}ms", joinPoint.getSignature().getName(), (endTime - startTime));
        return result;
    }

    // Advice that combines both pointcuts
    @Before("beanAnnotatedWithLoggable() || methodAnnotatedWithLoggable()")
    public void logMethodDetails(JoinPoint joinPoint) throws NoSuchMethodException {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();

        Loggable annotation = method.getAnnotation(Loggable.class) != null ? method.getAnnotation(Loggable.class)
                : joinPoint.getTarget().getClass().getAnnotation(Loggable.class);

        // If the method is coming from a proxy, get the real method from the target class
        if (method.getDeclaringClass().isInterface()) {
            method = joinPoint.getTarget().getClass().getDeclaredMethod(method.getName(), method.getParameterTypes());
        }

        // Get attributes from the annotation
        boolean showValues = annotation.showValues();
        Set<String> showOnlyParameters = new HashSet<>(Arrays.asList(annotation.showOnlyParameters()));
        Set<String> hideParameters = new HashSet<>(Arrays.asList(annotation.hideParameters()));

        // Get method details
        String methodName = methodSignature.getName();
        String[] parameterNames = methodSignature.getParameterNames();
        Object[] parameterValues = joinPoint.getArgs();

        StringBuilder signatureBuilder = new StringBuilder();
        signatureBuilder.append(method.getDeclaringClass().getSimpleName())
                .append("->")
                .append(methodName)
                .append("(");

        for (int i = 0; i < parameterNames.length; i++) {
            // Skip parameters that are in the excluded list
            if (hideParameters.contains(parameterNames[i])) {
                continue;
            }
            // If included parameters are specified, skip those not on the list
            if (!showOnlyParameters.isEmpty() && !showOnlyParameters.contains(parameterNames[i])) {
                continue;
            }

            // Append the parameter name and potentially its value
            signatureBuilder.append(parameterNames[i]);
            if (showValues) {
                signatureBuilder.append(" = ").append(parameterValues[i]);
            }
            signatureBuilder.append(", ");
        }

        // Trim the trailing comma if necessary
        int len = signatureBuilder.length();
        if (len >= 2 && signatureBuilder.substring(len - 2).equals(", ")) {
            signatureBuilder.delete(len - 2, len);
        }

        signatureBuilder.append(")");

        logger.info(signatureBuilder.toString());
    }

}