package com.miniontoby.ModUpdater;

import org.junit.Assert;
import org.junit.Test;
import com.miniontoby.ModUpdater.App;

/**
 * Unit test for simple App.
 */
public class AppTest {
	// @Test
	public void testCheckMod() {
		// check simple-voice-chat
		App.api_key = System.getenv("API_KEY");
		//String[] result = App.checkMod("416089", "1.18", "fabric", false);
		//if (result[0] == "0") Assert.assertTrue(false);
		//else Assert.assertTrue(true);
	}

	//@Test
	public void testInstallMod() {
		// check simple-voice-chat
		// App.api_key = System.getenv("API_KEY");
		//int result = App.installMod("416089", "1.18", "fabric", "/tmp", "simple-voice-chat");
		//if (result == 0) Assert.assertTrue(false);
		//else Assert.assertTrue(true);
	}
}
