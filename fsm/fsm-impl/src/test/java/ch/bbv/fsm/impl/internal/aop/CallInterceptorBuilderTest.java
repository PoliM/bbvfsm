/*******************************************************************************
 *  Copyright 2010, 2011 bbv Software Services AG, Ueli Kurmann
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 * Contributors:
 *     bbv Software Services AG (http://www.bbv.ch), Ueli Kurmann
 *******************************************************************************/
package ch.bbv.fsm.impl.internal.aop;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class CallInterceptorBuilderTest {

	private static class Callee {

		private String p1;
		private Integer p2;

		Callee() {
			// Used by CallInterceptorBuilder
		}

		public Void foo(final String p1, final Integer p2) {
			this.p1 = p1;
			this.p2 = p2;
			return null;
		}
	}

	@Test
	public void callInterceptingWhenInterceptionTypeThenInstancePassedWithExecuteMustBeCalled() {
		Callee proxy = CallInterceptorBuilder.build(Callee.class);
		proxy.foo("This", 12);
		final MethodCall<Callee> methodCall = CallInterceptorBuilder.pop();

		Callee testee = new Callee();
		methodCall.execute(testee, null);

		assertThat(testee.p1, is(equalTo("This")));
		assertThat(testee.p2, is(equalTo(12)));
		assertThat(proxy.p1, is(nullValue()));
		assertThat(proxy.p2, is(nullValue()));
	}

	@Test
	public void callInterceptingWhenInterceptionTypeThenInstancePassedWithConstructorMustBeCalled() {
		Callee testee = new Callee();

		Callee proxy = CallInterceptorBuilder.build(testee);
		proxy.foo("This", 12);

		final MethodCall<Callee> methodCall = CallInterceptorBuilder.pop();
		Callee otherInstance = new Callee();
		methodCall.execute(otherInstance, null);

		assertThat(testee.p1, is(equalTo("This")));
		assertThat(testee.p2, is(equalTo(12)));
		assertThat(proxy.p1, is(nullValue()));
		assertThat(proxy.p2, is(nullValue()));
		assertThat(otherInstance.p1, is(nullValue()));
		assertThat(otherInstance.p2, is(nullValue()));
	}

	@Test
	public void callInterceptingWhenUsingTemplateParameterThenPassedArgumentsMustBeUsed() {
		Callee testee = new Callee();

		Callee proxy = CallInterceptorBuilder.build(testee);
		proxy.foo(CallInterceptorBuilder.any(String.class), CallInterceptorBuilder.any(Integer.class));

		final MethodCall<Callee> methodCall = CallInterceptorBuilder.pop();
		Callee otherInstance = new Callee();
		methodCall.execute(otherInstance, new Object[] { "Other", 13 });

		assertThat(testee.p1, is(equalTo("Other")));
		assertThat(testee.p2, is(equalTo(13)));
		assertThat(proxy.p1, is(nullValue()));
		assertThat(proxy.p2, is(nullValue()));
		assertThat(otherInstance.p1, is(nullValue()));
		assertThat(otherInstance.p2, is(nullValue()));
	}
}
