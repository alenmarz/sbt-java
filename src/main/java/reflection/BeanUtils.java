package reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class BeanUtils {
    /**
     * Scans object "from" for all getters. If object "to"
     * contains correspondent setter, it will invoke it
     * to set property value for "to" which equals to the property
     * of "from".
     * <p/>
     * The type in setter should be compatible to the value returned
     * by getter (if not, no invocation performed).
     * Compatible means that parameter type in setter should
     * be the same or be superclass of the return type of the getter.
     * <p/>
     * The method takes care only about public methods.
     *
     * @param to   Object which properties will be set.
     * @param from Object which properties will be used to get values.
     */
    public static void assign(Object to, Object from) {
        Method[] toMethods = to.getClass().getMethods();
        Method[] fromMethods = from.getClass().getMethods();
        Map<String, Method> getters = new HashMap<>();

        for (Method method : fromMethods) {
            if (isGetter(method)) {
                getters.put(getFieldName(method), method);
            }
        }

        for (Method method : toMethods) {
            if (isSetter(method) && getters.containsKey(getFieldName(method))) {
                Method suitableGetter = getters.get(getFieldName(method));
                Class setterParameterType = method.getParameterTypes()[0];
                try {
                    if (areCompatible(setterParameterType, suitableGetter.getReturnType())) {
                        method.invoke(to, suitableGetter.invoke(from));
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Checks if the method is getter.
     * <p/>
     * A getter method have its name start with "get",
     * take 0 parameters, and returns a value.
     *
     * @param method Tested method
     * @return {@code true} if getter, {@code false} otherwise.
     */
    private static boolean isGetter(Method method) {
        if (!method.getName().startsWith("get")) {
            return false;
        }

        if (method.getParameterTypes().length != 0) {
            return false;
        }

        if (void.class.equals(method.getReturnType())) {
            return false;
        }

        return true;
    }

    /**
     * Checks if the method is setter.
     * <p/>
     * A getter method have its name start with "set"
     * and take only 1 parameter.
     *
     * @param method Tested method
     * @return {@code true} if setter, {@code false} otherwise.
     */
    private static boolean isSetter(Method method) {
        if (!method.getName().startsWith("set")) {
            return false;
        }

        if (method.getParameterTypes().length != 1) {
            return false;
        }

        return true;
    }

    /**
     * Removes first three letters (the get or set words)
     * from the method name.
     *
     * @param method A getter or setter
     * @return Name of class field using in method
     */
    private static String getFieldName(Method method) {
        return method.getName().substring(3);
    }

    /**
     * Checks if classes are equal or
     * class a is a superclass for b.
     *
     * @param a Class that can be a superclass
     * @param b Class that can be a child
     * @return {@code true} if classes are compatible,
     * {@code false} otherwise.
     */
    private static boolean areCompatible(Class a, Class b) {
        boolean isSuperclass = a.equals(b.getSuperclass());
        return  (isSuperclass || a.equals(b));
    }
}
