package edu.whu.bookshop.dbtool.data;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

public class EntityBuilder<T> {

    private Class<T> clazz;

    public EntityBuilder() {
        this.clazz = (Class<T>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public T build(Map data) {
        try {
            Field[] fields = clazz.getDeclaredFields();
            Object newInstance = clazz.newInstance();
            for (Field field : fields) {
                field.setAccessible(true);
                String name = field.getName();
                Class<?> type = field.getType();
                if(type == String.class){
                    field.set(newInstance,(String)data.get(name));
                } else if(type.getName().equals("float")){
                    field.set(newInstance,Float.parseFloat((String)data.get(name)));
                } else if(type.getName().equals("boolean")){
                    field.set(newInstance,Boolean.parseBoolean((String)data.get(name)));
                }
            }
            return (T)newInstance;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args){
        Map mp = new HashMap<>();
        mp.put("account","123456");
        mp.put("name","zangsan");
        mp.put("money","123");
        mp.put("isChanged","true");
        user u = new EntityBuilder<user>().build(mp);
        System.out.println(u);
    }
}
