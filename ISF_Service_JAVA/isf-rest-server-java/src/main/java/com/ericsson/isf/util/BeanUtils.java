package com.ericsson.isf.util;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BeanUtils implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * This method trims all String variables defined in the bean if they have
	 * corresponding getter setter methods. <br/>
	 * Eg : BeanUtils beanUtils=new BeanUtils();<br/>
	 * StudentVO studentVO=new StudentVO();<br/>
	 * studentVO.setName(" foo ");<br/>
	 * studentVO.setAddress(" bar ");<br/>
	 * beanUtils.trimAllStrings(studentVO);<br/>
	 * System.out.println(studentVO.getName());//prints foo<br/>
	 * System.out.println(studentVO.getAddress());//prints bar<br/>
	 * 
	 * @param beanObject
	 * @throws Exception
	 *             If a variable has its getter method defined but not setter method
	 *             will throw NoSuchMethodException instance as Exception.
	 * @author visruth
	 */
	public static void trimAllStrings(Object beanObject) throws Exception {
		Exception noSuchMethodException = null;
		boolean throwNoSuchMethodException = false;
		if (beanObject != null) {

			Method[] methods = null;

			try {
				methods = beanObject.getClass().getMethods();
			} catch (SecurityException e) {
				throw new Exception(e);
			}

			if (methods != null) {

				for (Method method : methods) {

					String methodName = method.getName();

					if (!methodName.equals("getClass")) {

						String returnType = method.getReturnType().toString();
						String commonMethodName = null;

						if (methodName.startsWith("get") && "class java.lang.String".equals(returnType)) {

							commonMethodName = methodName.replaceFirst("get", "");
							String returnedValue = null;

							try {
								returnedValue = (String) method.invoke(beanObject);
							} catch (IllegalArgumentException e) {
								e.printStackTrace();
								throw e;
							} catch (IllegalAccessException e) {
								e.printStackTrace();
								throw e;
							} catch (InvocationTargetException e) {
								e.printStackTrace();
								throw e;
							}

							if (returnedValue != null) {

								StringBuilder setterMethodName = new StringBuilder();
								setterMethodName.append("set");
								setterMethodName.append(commonMethodName);
								Method setterMethod = null;

								try {
									setterMethod = beanObject.getClass().getMethod(setterMethodName.toString(),
											String.class);
									if (setterMethod != null) {
										setterMethod.invoke(beanObject, (returnedValue.trim()));
									}
								} catch (SecurityException e) {
									e.printStackTrace();
									throw e;
								} catch (NoSuchMethodException e) {
									e.printStackTrace();
									if (!throwNoSuchMethodException) {
										noSuchMethodException = e;
									}
									throwNoSuchMethodException = true;
								} catch (IllegalArgumentException e) {
									e.printStackTrace();
									throw e;
								} catch (IllegalAccessException e) {
									e.printStackTrace();
									throw e;
								} catch (InvocationTargetException e) {
									e.printStackTrace();
									throw e;
								}
							}
						}
					}
				}
			}
		}

		if (throwNoSuchMethodException && noSuchMethodException != null) {
			throw noSuchMethodException;
		}
	}

	/**
	 * converts an Object to String
	 * 
	 * @param object1
	 * @return String
	 * @author visruth
	 */
	public String toString(Object object) {
		if (object != null) {
			return object.toString();
		} else {
			return null;
		}
	}

}
