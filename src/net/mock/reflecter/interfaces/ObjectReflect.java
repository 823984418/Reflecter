package net.mock.reflecter.interfaces;

import net.mock.reflecter.annotation.*;

@TargetClass("java.lang.Object")
//extends "null" implements {}
public interface ObjectReflect{
	//construct


	//method

	@Violence
	//"int"
	@Signature({"java.lang.Object"})
	public int method_identityHashCode(Object self,Object arg0);

	@Violence
	//"int"
	@Signature({"java.lang.Object"})
	public int method_identityHashCodeNative(Object self,Object arg0);

	@Violence
	//"java.lang.Object"
	@Signature({})
	public Object method_internalClone(Object self);

	@Violence
	//"java.lang.Object"
	@Signature({})
	public Object method_clone(Object self);

	@Violence
	//"void"
	@Signature({})
	public void method_finalize(Object self);


	//field
	@Violence
	//"java.lang.Class"
	public Class<?> field_shadow$_klass_(Object self);
	public void field_shadow$_klass_(Object self,Class<?> value);

	@Violence
	//"int"
	public Object field_shadow$_monitor_(Object self);
	public void field_shadow$_monitor_(Object self,int value);


}
