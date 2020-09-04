package otus.appcontainer;

import otus.appcontainer.api.AppComponent;
import otus.appcontainer.api.AppComponentsContainer;
import otus.appcontainer.api.AppComponentsContainerConfig;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);
        Object configInstance = createConfigInstance(configClass);
        Map<Integer, Map<String, Method>> toCreateMap = scanConfiguration(configClass);
        createBeanInstances(configInstance, toCreateMap);
    }

    private Object createConfigInstance(Class<?> configClass) {
        Object configInstance;
        try {
            configInstance = configClass.getConstructor().newInstance();

        } catch (Exception e) {
            throw new RuntimeException("Cannot create instance of config", e);
        }
        return configInstance;
    }

    private void createBeanInstances(Object configInstance, Map<Integer, Map<String, Method>> toCreateMap) {
        toCreateMap.forEach((integer, stringMethodMap) -> {
            stringMethodMap.forEach((beanName, method) -> {
                Object beanInstance;
                try {
                    if (method.getParameterCount() == 0) {
                        beanInstance = method.invoke(configInstance);
                    } else {
                        beanInstance = method.invoke(configInstance, Arrays.stream(method.getParameters()).
                                map(parameter -> getAppComponent(parameter.getType())).toArray());
                    }
                } catch (Exception e) {
                    throw new RuntimeException("Error while create bean instance : ", e);
                }
                appComponentsByName.put(beanName, beanInstance);
                appComponents.add(beanInstance);

            });
        });
    }

    private Map<Integer, Map<String, Method>> scanConfiguration(Class<?> configClass) {
        return Arrays.stream(configClass.getMethods()).
                filter(method -> method.isAnnotationPresent(AppComponent.class)).
                collect(Collectors.toMap(
                    method -> method.getAnnotation(AppComponent.class).order(),
                    method -> {
                        Map<String, Method> beanMap = new HashMap<>();
                        beanMap.put(method.getAnnotation(AppComponent.class).name(), method);
                        return beanMap;
                    },
                    (map1, map2) ->  {
                        map1.putAll(map2);
                        return map1;
                    },
                    () -> new TreeMap<>()
                ));
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        return (C) appComponents.
            stream().
            filter(o -> componentClass.isInstance(o)).
            findAny().
            orElse(null);
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        return (C) appComponentsByName.get(componentName);
    }
}
