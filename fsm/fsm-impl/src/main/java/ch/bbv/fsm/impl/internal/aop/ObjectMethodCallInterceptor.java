package ch.bbv.fsm.impl.internal.aop;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * Intercepts a call to a proxy of an instance.
 * 
 * @param <TObject>
 *            the type of object to call
 */
public class ObjectMethodCallInterceptor<TObject> implements MethodInterceptor {

	private final Object instance;

	/**
	 * Constructor.
	 * 
	 * @param instance
	 *            the instance to call
	 */
	public ObjectMethodCallInterceptor(final Object instance) {
		this.instance = instance;
	}

	@Override
	public Object intercept(final Object object, final Method method, final Object[] args, final MethodProxy methodProxy) throws Throwable {
		if (!method.isAccessible()) {
			method.setAccessible(true);
		}
		final MethodCall<TObject> methodCall = new ObjectMethodCallImpl<TObject>(instance, method, args);
		CallInterceptorBuilder.push(methodCall);
		return null;
	}
}
