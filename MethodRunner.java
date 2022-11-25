package TickSystemThread;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodRunner {
    private final String className;
    private final String methodName;
    private final Class[] parameterTypes;
    private final Object objectToInvokeOn;
    private final Object[] params;

    public MethodRunner(String className,String methodName, Class[] parameterTypes, Object objectToInvokeOn, Object[] params){
        this.className = className;
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
        this.objectToInvokeOn = objectToInvokeOn;
        this.params = params;
    }

    public void run(){
        Class<?> c = null;
        try {
            c = Class.forName(this.className);
            Method method = c.getDeclaredMethod(this.methodName, this.parameterTypes);
            method.invoke(this.objectToInvokeOn, this.params);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

    }
}
