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
package ch.bbv.fsm.impl.internal;

import static ch.bbv.fsm.impl.Tool.*;

import java.util.EmptyStackException;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ch.bbv.fsm.dsl.MethodCall;
import ch.bbv.fsm.impl.internal.aop.MethodCallImpl;

public class ToolTest {

    private boolean isFooExecuted;
    private String p1;
    private Integer p2;

    public Void foo(final String p1, final Integer p2) {
        this.isFooExecuted = true;
        this.p1 = p1;
        this.p2 = p2;
        return null;
    }

    @Before
    public void setup() {
    	MethodCallImpl.reset();
        this.isFooExecuted = false;
    }
    
    @After
    public void teardown(){
    	MethodCallImpl.reset();
    }

    @Test
    public void testAny() {

        Assert.assertEquals(null, any(String.class));
    }

    @Test
    public void testFromExecution_doNotCallOriginalMethod() {
        from(this).foo("String1", new Integer(99));
        Assert.assertFalse(this.isFooExecuted);
    }
    
    
    @Test
    public void testFromExecution() {
        from(this).foo("String1", new Integer(99));
        final MethodCall methodCall = MethodCallImpl.pop();
        methodCall.execute();

        Assert.assertEquals("String1", this.p1);
        Assert.assertEquals(new Integer(99), this.p2);
        Assert.assertTrue(this.isFooExecuted);
    }

    @Test
    public void testFromMethodCallInstance() {
        from(this).foo("String1", new Integer(99));
        final MethodCall methodCall = MethodCallImpl.pop();

        Assert.assertEquals("String1", methodCall.getArguments()[0]);
        Assert.assertEquals(new Integer(99), methodCall.getArguments()[1]);
        Assert.assertEquals("foo", methodCall.getMethod().getName());
    }

    @Test(expected = EmptyStackException.class)
    public void testFromStack() {
        from(this).foo("String1", new Integer(99));
        MethodCallImpl.pop();
        MethodCallImpl.pop();
    }

}
