package eu.gloria.gs.services.log.action;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

@XmlRootElement
@XmlSeeAlso({ Object[].class, LinkedHashMap.class, HashMap.class,
		LinkedHashMap.class })
public class Action extends LinkedHashMap<String, Object> implements
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2113204093482010938L;

	public Action() {
	}

	public Action(Class<?> cl, String method, Object... args) {
		
		Method[] methods = cl.getDeclaredMethods();
		for (Method m : methods) {
			if (m.getName().equals(method)) {
				this.setOperation(m, args);
				break;
			}
		}
	}

	public Action(Action action) {
		this.join(action);
	}

	public void join(Action action) {
		this.putAll(action);
	}

	public void child(String name, Action action) {
		this.put(name, action);
	}

	@SuppressWarnings("unchecked")
	public void setOperation(Method method, Object... args) {
		if (!this.containsKey("action")) {
			this.put("action", new LinkedHashMap<String, Object>());
		}

		String name = method.getName();

		if (method.isAnnotationPresent(ServiceOperation.class)) {
			ServiceOperation operation = method.getAnnotation(ServiceOperation.class);
			name = operation.name();
		}

		((LinkedHashMap<String, Object>) this.get("action")).put("name",
				name);

		Annotation[][] annotations = method.getParameterAnnotations();

		int i = 0;
		for (Annotation[] annotation : annotations) {
			if (annotation.length > 0 && args.length > i
					&& annotation[0].annotationType().equals(Param.class)) {
				
				String param = ((Param) annotation[0]).name();
				((LinkedHashMap<String, Object>) this.get("action")).put(
						param, args[i]);
			}

			i++;
		}
	}

	@SuppressWarnings("unchecked")
	public void setOperation(String operation) {
		if (!this.containsKey("action")) {
			this.put("action", new LinkedHashMap<String, Object>());
		}

		((LinkedHashMap<String, Object>) this.get("action")).put("name",
				operation);
	}

	@SuppressWarnings("unchecked")
	public void setArgument(String name, Object value) {
		if (this.containsKey("action")) {
			((LinkedHashMap<String, Object>) this.get("action")).put("name",
					value);
		}
	}
}
