package ch.bbv.fsm.impl;

import junit.framework.Assert;

import org.junit.Test;

public class ToolTest {
	@Test
	public void testAny() {
		Assert.assertEquals(null, Tool.any(String.class));
	}
}
