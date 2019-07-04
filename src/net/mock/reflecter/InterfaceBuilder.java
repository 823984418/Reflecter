package net.mock.reflecter;

import java.lang.reflect.*;

public class InterfaceBuilder{
	
	public static String toString(Class<?>[] types){
		if(types.length == 0){
			return "";
		}
		StringBuilder sb = new StringBuilder("\"");
		sb.append(JavaClass.getJavaName(types[0]));
		sb.append("\"");
		for(int i = 1;i < types.length;i++){
			sb.append(",\"");
			sb.append(JavaClass.getJavaName(types[i]));
			sb.append("\"");
		}
		return sb.toString();
	}
	
	public InterfaceBuilder(String name) throws ClassNotFoundException{
		this(JavaClass.getJavaClass(Thread.currentThread().getContextClassLoader(),name));
	}
	
	public InterfaceBuilder(Class<?> cla){
		buildClass = cla;
		publicMember = false;
		packageName = "net.mock.reflecter.interfaces";
		className = cla.getSimpleName() + "Reflect";
	}
	private final Class<?> buildClass;
	public boolean publicMember;
	public String packageName;
	public String className;
	public String build(){
		StringBuilder sb = new StringBuilder("package ");
		sb.append(packageName);
		sb.append(";\n\nimport net.mock.reflecter.annotation.*;\n\n@TargetClass(\"");
		sb.append(buildClass.getName());
		sb.append("\")\n//extends \"");
		sb.append(JavaClass.getJavaName(buildClass.getSuperclass()));
		sb.append("\" implements {");
		sb.append(toString(buildClass.getInterfaces()));
		sb.append("}\npublic interface ");
		sb.append(className);
		sb.append("{\n");
		sb.append("\t//construct\n\n");
		for(Constructor c : buildClass.getDeclaredConstructors()){
			if(Modifier.isPublic((c.getModifiers()))){
				if(!publicMember){
					continue;
				}
			}else{
				sb.append("\t@Violence\n");
			}
			sb.append("\t@Signature({");
			Class<?>[] ts = c.getParameterTypes();
			sb.append(toString(ts));
			sb.append("})\n\tpublic ");
			sb.append(JavaClass.getJavaName(buildClass));
			sb.append(" newInstance(");
			for(int i = 0;i < ts.length;i++){
				sb.append("Object arg");
				sb.append(i);
				if(i + 1 != ts.length){
					sb.append(",");
				}
			}
			sb.append(");\n\n");
		}
		sb.append("\n");
		
		sb.append("\t//method\n\n");
		for(Method m : buildClass.getDeclaredMethods()){
			if(Modifier.isPublic((m.getModifiers()))){
				if(!publicMember){
					continue;
				}
			}else{
				sb.append("\t@Violence\n");
			}
			sb.append("\t//\"");
			sb.append(JavaClass.getJavaName(m.getReturnType()));
			sb.append("\"\n\t@Signature({");
			Class<?>[] ts = m.getParameterTypes();
			sb.append(toString(ts));
			sb.append("})\n\tpublic Object method_");
			sb.append(m.getName());
			sb.append("(Object self");
			if(ts.length != 0){
				sb.append(",");
			}
			for(int i = 0;i < ts.length;i++){
				sb.append("Object arg");
				sb.append(i);
				if(i + 1 != ts.length){
					sb.append(",");
				}
			}
			sb.append(");\n\n");
		}
		sb.append("\n");
		
		sb.append("\t//field\n");
		for(Field f : buildClass.getDeclaredFields()){
			if(Modifier.isPublic((f.getModifiers()))){
				if(!publicMember){
					continue;
				}
			}else{
				sb.append("\t@Violence\n");
			}
			sb.append("\t//\"");
			sb.append(JavaClass.getJavaName(f.getType()));
			sb.append("\"\n\tpublic Object field_");
			sb.append(f.getName());
			sb.append("(Object self);\n\tpublic void field_");
			sb.append(f.getName());
			sb.append("(Object self,Object value);\n\n");
		}
		sb.append("\n");
		
		sb.append("}");
		return sb.toString();
	}
}
