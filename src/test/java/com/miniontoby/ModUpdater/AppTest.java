package com.miniontoby.ModUpdater;
//import com.miniontoby.ModUpdater;

import org.junit.Assert;
import org.junit.Test;

import com.miniontoby.ModUpdater.App;

/**
 * Unit test for simple App.
 */
public class AppTest {
	@Test
	public void testCheckMod() {
		//check simple-voice-chat
		int result = App.checkMod(new String[]{"checkMod", "416089", "1.18"}, false);
		if (result == 0) Assert.assertTrue(false);
		else Assert.assertTrue(true);
	}

	//@Test
	public void testInstallMod() {
		//check simple-voice-chat
//		int result = App.installMod(new String[]{"installMod", "416089", "1.18", "/tmp"});
//		if (result == 0) Assert.assertTrue(false);
//		else Assert.assertTrue(true);
	}
}
