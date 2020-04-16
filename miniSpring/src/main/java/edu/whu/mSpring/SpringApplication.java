package edu.whu.mSpring;

import edu.whu.mSpring.annotation.*;
import edu.whu.mSpring.interceptor.HandlerInterceptor;
import edu.whu.mSpring.servlet.DispatcherServlet;
import edu.whu.mTomcat.connector.HttpConnector;
import edu.whu.mTomcat.container.SimpleContainer;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SpringApplication {
    public static Map<String,Object> iocContainer = new ConcurrentHashMap<>();

    public static Map<String, Map<RequestMethod,Method>> handlerMappingMethod = new  HashMap<>();

    public static Map<String, Object> handlerMappingController = new  HashMap<>();

    public static List<HandlerInterceptor> interceptors = new ArrayList<>();

    public void run(Class primarySource){
        System.out.println("asd");
        // IOC容器初始化，自动注入
        doScanner(primarySource.getPackage().toString().split(" ")[1]);
        doInstance();
        doAutowire();
        // url映射初始化
        doHandlerMapping();

        HttpConnector connector = new HttpConnector();
        SimpleContainer container = new SimpleContainer(new DispatcherServlet());
        connector.setContainer(container);
        try {
            connector.initialize();
            connector.start();

            // make the application wait until we press any key.
            System.in.read();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<String> classNames = new ArrayList<>();
    private void doScanner(String packageName) {
        //把所有的.替换成/
        String resource = packageName.replaceAll("\\.", "/");
        URL url  =Thread.currentThread().getContextClassLoader().getResource(resource);
        File dir = new File(url.getFile());
        for (File file : dir.listFiles()) {
            if(file.isDirectory()){
                //递归读取包
                doScanner(packageName+"."+file.getName());
            }else{
                String className =packageName +"." +file.getName().replace(".class", "");
                classNames.add(className);
            }
        }
    }


    private void doInstance() {
        if (classNames.isEmpty()) {
            return;
        }
        for (String className : classNames) {
            try {
                Class<?> clazz =Class.forName(className);
                // 注入Component
                if(clazz.isAnnotationPresent(Component.class)){
                    Component annotation = clazz.getAnnotation(Component.class);
                    if(!annotation.name().equals("")) {
                        iocContainer.put(annotation.name(), clazz.newInstance());
                    } else {
                        iocContainer.put(className,clazz.newInstance());
                    }
                }
                // 注入Interceptor
                else if(clazz.isAnnotationPresent(Interceptor.class)){
                    Class<?>[] interfaces = clazz.getInterfaces();
                    // 必须实现HandlerInterceptor接口
                    boolean flag = false;
                    for(Class i : interfaces){
                        if(i == HandlerInterceptor.class){
                            flag = true;
                        }
                    }
                    if(flag){
                        interceptors.add((HandlerInterceptor) clazz.newInstance());
                    }
                }
                else{
                    continue;
                }
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }
    }

    private void doAutowire() {
        for(String key : iocContainer.keySet()){
            Object instance = iocContainer.get(key);
            Class<?> aClass = instance.getClass();
            Field[] fields = aClass.getDeclaredFields();
            for(Field field : fields){
                if(field.isAnnotationPresent(Autowired.class)){
                    Autowired annotation = field.getAnnotation(Autowired.class);
                    field.setAccessible(true);
                    try {
                        String s = annotation.name();
                        Object object = iocContainer.get(annotation.name());
                        if(object == null){
                            String ss = field.getType().getName();
                            object = iocContainer.get(field.getType().getName());
                        }
                        field.set(instance,object);
                    } catch (IllegalAccessException e) {
                        System.out.println("autowired fail");
                        e.printStackTrace();
                    }
                }
            }
            System.out.println(aClass);
        }
    }

    private void doHandlerMapping() {
        if (iocContainer.isEmpty()) {
            return;
        }
        try {
            for (Map.Entry<String, Object> entry : iocContainer.entrySet()) {
                Class<?> clazz = entry.getValue().getClass();
                // 只有在Controller下的方法才会被扫描
                if (!clazz.isAnnotationPresent(Controller.class)) {
                    continue;
                }

                //拼url时,是controller头的url拼上方法上的url
                String baseUrl = "";
                if (clazz.isAnnotationPresent(RequestMapping.class)) {
                    RequestMapping requestMapping = clazz.getAnnotation(RequestMapping.class);
                    baseUrl = requestMapping.value();
                }
                Method[] methods = clazz.getMethods();
                for (Method method : methods) {
                    if (!method.isAnnotationPresent(RequestMapping.class)) {
                        continue;
                    }
                    RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                    String url = requestMapping.value();

                    url = (baseUrl + "/" + url).replaceAll("/+", "/");
                    // 填充方法映射
                    Map<RequestMethod, Method> requestMethodMethodMap = handlerMappingMethod.get(url);
                    if (requestMethodMethodMap == null) {
                        requestMethodMethodMap = new HashMap<>();
                    }
                    for (RequestMethod requestMethod : requestMapping.method()) {
                        requestMethodMethodMap.put(requestMethod, method);
                    }
                    handlerMappingMethod.put(url, requestMethodMethodMap);

                    // 填充实体映射
                    Component component = clazz.getAnnotation(Component.class);
                    if(component.name().equals("")){
                        handlerMappingController.put(url,iocContainer.get(clazz.getName()));
                    } else {
                        handlerMappingController.put(url, iocContainer.get(clazz.getAnnotation(Component.class).name()));
                    }
                    System.out.println(url + "," + method);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
