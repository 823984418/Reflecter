package net.mock.reflecter;

public class JavaClass{
	public static Class<?> getJavaClass(ClassLoader loader,String name) throws ClassNotFoundException{
		if(name == null){
			throw new NullPointerException();
		}else if(name.equals("void")){
			return void.class;
		}else if(name.equals("boolean")){
			return boolean.class;
		}else if(name.equals("byte")){
			return byte.class;
		}else if(name.equals("char")){
			return char.class;
		}else if(name.equals("short")){
			return short.class;
		}else if(name.equals("int")){
			return int.class;
		}else if(name.equals("long")){
			return long.class;
		}else if(name.equals("float")){
			return float.class;
		}else if(name.equals("double")){
			return double.class;
		}else{
			String[] ns = name.split("\\[\\]",-1);
			String c = ns[0];
			if(ns.length == 1){
				return loader.loadClass(c);
			}
			if(c.equals("")){
				throw new IllegalArgumentException();
			}else if(c.equals("void")){
				c = "V";
			}else if(c.equals("boolean")){
				c = "Z";
			}else if(c.equals("byte")){
				c = "B";
			}else if(c.equals("char")){
				c = "C";
			}else if(c.equals("short")){
				c = "S";
			}else if(c.equals("int")){
				c = "I";
			}else if(c.equals("long")){
				c = "J";
			}else if(c.equals("float")){
				c = "F";
			}else if(c.equals("double")){
				c = "D";
			}else{
				c = "L" + c + ";";
			}
			for(int i = 1;i < ns.length;i++){
				if(!ns[i].equals("")){
					throw new IllegalArgumentException();
				}
				c = "[" + c;
			}
			return loader.loadClass(c);
		}
	}
	public static String getJavaName(Class<?> cla){
		if(cla == null){
			return null;
		}
		StringBuilder t = new StringBuilder();
		while(cla.isArray()){
			cla = cla.getComponentType();
			t.append("[]");
		}
		return cla.getName() + t.toString();
	}
}
