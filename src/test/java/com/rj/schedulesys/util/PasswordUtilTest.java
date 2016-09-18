package com.rj.schedulesys.util;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import org.junit.Test;

public class PasswordUtilTest {

	@Test
	public void test() throws NoSuchAlgorithmException, InvalidKeySpecException{
		PasswordHashUtil.validatePassword("secret", "1000:gJxZ3DKj1GIgB+ESgAkjy8M5VxVqMnpG:fIq6QneoBzx0oVmS5qQ2porDGEh0sVo/");
	}
}
