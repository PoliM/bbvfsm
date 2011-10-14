package ch.bbv.fsm.impl.internal.aop;

import java.util.Stack;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

/**
 * Defines a mock for detecting method calls.
 */
public final class CallInterceptorBuilder {

	private CallInterceptorBuilder() {
		// Tool class
	}

	@SuppressWarnings("rawtypes")
	private static ThreadLocal<Stack<MethodCall>> methodCalls = new ThreadLocal<Stack<MethodCall>>() {
		@Override
		protected Stack<MethodCall> initialValue() {
			return new Stack<MethodCall>();
		}
	};

	/**
	 * Pops a method call from the stack.
	 * 
	 * @param <TObject>
	 *            the type of the object to call
	 * 
	 * @return the method call on the top of the stack.
	 */
	@SuppressWarnings("unchecked")
	public static <TObject> MethodCall<TObject> pop() {
		return methodCalls.get().pop();
	}

	/**
	 * Pushes a method call instance to the stack.
	 * 
	 * @param <TObject>
	 *            the type of the object to call
	 * 
	 * @param methodCall
	 *            the method call.
	 */
	public static <TObject> void push(final MethodCall<TObject> methodCall) {
		methodCalls.get().push(methodCall);
	}

	/**
	 * Intercepts a type to simulate delegates.
	 * 
	 * @param <T>
	 *            the type
	 * @param type
	 *            the type to call
	 */
	@SuppressWarnings("unchecked")
	public static <T> T build(final Class<T> type) {
		final MethodInterceptor interceptor = new ClassMethodCallInterceptor<T>();
		final Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(type);
		enhancer.setCallback(interceptor);
		final T proxy = (T) enhancer.create();
		return proxy;
	}

	/**
	 * Intercepts an object to simulate delegates.
	 * 
	 * @param <T>
	 *            the type
	 * @param instance
	 *            the instance to call
	 */
	@SuppressWarnings("unchecked")
	public static <T> T build(final T instance) {
		final MethodInterceptor interceptor = new ObjectMethodCallInterceptor<T>(instance);
		final Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(instance.getClass());
		enhancer.setCallback(interceptor);
		final T proxy = (T) enhancer.create();
		return proxy;
	}

	/**
	 * Returns an argument mock for the call.
	 * 
	 * @param <T>
	 *            the type to use
	 * @param type
	 *            the type of the argument.
	 */
	public static <T> T any(final Class<T> type) {
		return null;
	}
}
