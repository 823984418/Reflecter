package net.mock.reflecter;

import java.lang.reflect.*;
import java.util.*;
import net.mock.reflecter.annotation.*;

public class Reflecter<E> implements InvocationHandler{
	private static Class<?>[] getSignture(Signature signature,ClassLoader loader) throws ClassNotFoundException{
		String[] names = signature.value();
		Class<?>[] result = new Class[names.length];
		for(int i = 0;i < names.length;i++){
			result[i] = JavaClass.getJavaClass(loader,names[i]);
		}
		return result;
	}
	private static Class<?>[] getConstructorSignature(Method method,ClassLoader loader) throws ClassNotFoundException{
		Signature signature = method.getAnnotation(Signature.class);
		if(signature != null){
			return getSignture(signature,loader);
		}else{
			return method.getParameterTypes();
		}
	}
	private static Class<?>[] getMethodSignature(Method method,ClassLoader loader) throws ClassNotFoundException{
		Signature signature = method.getAnnotation(Signature.class);
		if(signature != null){
			return getSignture(signature,loader);
		}else{
			Class<?>[] args = method.getParameterTypes();
			return Arrays.copyOfRange(args,1,args.length);
		}
	}
	public Reflecter(Class<E> interfaceClass) throws ReflectiveOperationException{
		this(interfaceClass,interfaceClass.getClassLoader());
	}
	public Reflecter(Class<E> interfaceClass,ClassLoader loader) throws ReflectiveOperationException{
		this.interfaceClass = interfaceClass;
		String reflectName = interfaceClass.getAnnotation(TargetClass.class).value();
		Class<?> reflectClass = this.reflectClass = JavaClass.getJavaClass(loader,reflectName);
		for(Method method : interfaceClass.getMethods()){
			String name = method.getName();
			boolean open = method.getAnnotation(Violence.class) != null;
			AccessibleObject m = null;
			if(name.equals("newInstance")){
				m = reflectClass.getDeclaredConstructor(getConstructorSignature(method,loader));
			}else if(name.startsWith("method_")){
				m = reflectClass.getDeclaredMethod(name.substring("method_".length()),getMethodSignature(method,loader));
			}else if(name.startsWith("field_")){
				m = reflectClass.getDeclaredField(name.substring("field_".length()));
			}else{
				throw new RuntimeException();
			}
			if(open){
				m.setAccessible(true);
			}
			map.put(method,m);
		}
	}
	private final Class<E> interfaceClass;
	private final Class<?> reflectClass;
	private final Map<Method,AccessibleObject> map = new HashMap<>();
	@Override
	public Object invoke(Object o, Method m, Object[] args) throws Throwable{
		AccessibleObject member = map.get(m);
		if(member == null){
			throw new RuntimeException();
		}else if(member instanceof Constructor){
			return ((Constructor) member).newInstance(args);
		}else if(member instanceof Method){
			Object[] nargs = Arrays.copyOfRange(args,1,args.length);
			return ((Method) member).invoke(args[0],nargs);
		}else if(member instanceof Field){
			if(args.length == 1){
				return ((Field) member).get(args[0]);
			}else if(args.length == 2){
				((Field) member).set(args[0],args[1]);
				return args[2];
			}else{
				throw new RuntimeException();
			}
		}else{
			throw new RuntimeException();
		}
	}
	public E build(){
		return (E) Proxy.newProxyInstance(interfaceClass.getClassLoader(),new Class<?>[]{interfaceClass},this);
	}
}
