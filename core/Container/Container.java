package core.Container;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public abstract class Container {
    protected HashMap<String, Object> instances = new HashMap<>();
    protected HashMap<String, String> aliases = new HashMap<>();

    protected boolean resolved(String name) {
        if (this.isAlias(name))
            name = this.getAlias(name);

        return this.instances.get(name) != null;
    }

    protected boolean isAlias(String name) {
        return this.aliases.get(name) != null;
    }

    public void setAlias(String key, String value) {
        this.aliases.put(key, value);
    }

    protected String getAlias(String name) {
        return this.aliases.get(name);
    }

    public void binding(String name, Object obj) {
        name=this.isAlias(name)?this.getAlias(name):name;
        this.instances.put(name, obj);
    }

    protected boolean buildable(String name) {
        try {
            Constructor<?> cons = Class.forName(name).getDeclaredConstructors()[0];
            if (cons.getParameterTypes().length == 0)
                return true;

            for (Class<?> cls : cons.getParameterTypes()) {
                if (!this.resolved(cls.getName()))
                    return false;
            }
        } catch (SecurityException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return true;
    }

    private Object build(String name) {
        name=this.isAlias(name)?this.getAlias(name):name;
        try {
            Constructor<?> cons = Class.forName(name).getDeclaredConstructors()[0];

            if (cons.getParameterTypes().length == 0) {
                return cons.newInstance();
            }

            Object[] params = new Object[cons.getParameterTypes().length];
            for(int i=0;i<cons.getParameterTypes().length;i++){
                params[i] = this.instances.get(cons.getParameterTypes()[i].getName());
            }

            return cons.newInstance(params);
        }

        catch (SecurityException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        catch (InstantiationException | IllegalAccessException | IllegalArgumentException| InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Object resolve(String name) {
        name=this.isAlias(name)?this.getAlias(name):name;

        if(this.resolved(name)) return this.instances.get(name);

        if(this.buildable(name)) {
            this.instances.put(name,this.build(name));
            return this.instances.get(name);
        }
        
        try {
            Constructor<?> cons = Class.forName(name).getDeclaredConstructors()[0];

            for(Class<?> cls : cons.getParameterTypes()){
                this.resolve(cls.getName());
            }

            this.instances.put(name,this.build(name));

            return this.instances.get(name);
        } 
        catch (ClassNotFoundException e) {
            System.out.println("Class not Found");
        }
        return null;
    }

    public Object make(String name){
        name=this.isAlias(name)?this.getAlias(name):name;

        if(this.resolved(name)) return this.instances.get(name);
        
        if(this.buildable(name)) 
            return this.build(name);

        return this.resolve(name);
    }

    public Object rebuild(String name){
        return this.build(name);
    }
}