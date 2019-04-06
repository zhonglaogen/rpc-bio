package com.zlx.remoterpc.entity;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 用户请求
 */
public class RPCRequest implements Serializable {

    private static final long serialVersionUID = 6763692383213184607L;
    private String className;
    private String MethodName;
    private Object[] parameters;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return MethodName;
    }

    public void setMethodName(String methodName) {
        MethodName = methodName;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        return "RPCRequest{" +
                "className='" + className + '\'' +
                ", MethodName='" + MethodName + '\'' +
                ", parameters=" + Arrays.toString(parameters) +
                '}';
    }
}
