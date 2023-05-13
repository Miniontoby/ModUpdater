package com.miniontoby.ModUpdater;

import com.google.gson.*;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.FileUtils;

public class App {
	private static String settings_file = System.getProperty("user.home") + File.separator + ".ModUpdater" + File.separator + "settings.properties";
	private static String api_url = "https://api.curseforge.com/v1";
	public static String api_key = "";
	private static JsonObject getJSON(String url) {
		try {
			java.net.URLConnection uc = new java.net.URL(url).openConnection();
			uc.setRequestProperty("User-Agent", "ModUpdater - https://github.com/Miniontoby/ModUpdater");
			uc.setRequestProperty("x-api-key", api_key);

			// Java 8
			BufferedReader in = new BufferedReader(
				new InputStreamReader(
					uc.getInputStream()));
			StringBuilder response = new StringBuilder();
			String inputLine;
			while ((inputLine = in.readLine()) != null) response.append(inputLine);
			in.close();
			String contents = response.toString();


			// Java 11
			//InputStream is = uc.getInputStream();
			//String contents = new String(is.readAllBytes());


			JsonObject jsonObject = JsonParser.parseString(contents).getAsJsonObject();
			return jsonObject;
		} catch (java.io.FileNotFoundException e) {
			System.out.println("Error: Cannot find file at url: " + url);
		} catch (java.net.MalformedURLException e) {
			System.out.println("Error: " + e);
		} catch (java.io.IOException e) {
			System.out.println("Error: " + e);
		}
		return null;
	}
	private static JsonObject getFileForVersion(JsonObject rootObject, String version, String modloader) {
		int modLoader = modloaderToInt(modloader);
		JsonArray files = rootObject.getAsJsonObject("data").getAsJsonArray("latestFilesIndexes");
		JsonObject file;
		for (int i=0; i<files.size(); i++) {
			file = files.get(i).getAsJsonObject();
			if (modLoader != -1 && file.has("modLoader")) {
				try {
					int ml = file.get("modLoader").getAsInt();
					if (ml != modLoader) continue;
				} catch (Exception e) {
					continue;
				}
			}
			if (file.get("gameVersion").isJsonArray() ){
				JsonArray versionArray = file.get("ganeVersion").getAsJsonArray();
				for (int j=0; j < versionArray.size(); j++) {
					if (versionArray.get(j).getAsString().equals(version)){
						return file;
					}
				}
			} else if (file.get("gameVersion").getAsJsonPrimitive().isString()) {
				if (file.get("gameVersion").getAsString().equals(version)) {
					return file;
				}
			}
		}
		return null;
	}
	private static JsonObject readFileToJsonObject(String filename) {
		File file = new File(filename);
		try {
			FileReader fileR = new FileReader(file);
			JsonObject jsonObject = JsonParser.parseReader(fileR).getAsJsonObject();
			fileR.close();
			return jsonObject;
		} catch (java.io.FileNotFoundException e) {
			System.out.println("Error: Cannot find file " + filename);
		} catch (java.io.IOException e) {
			System.out.println("Error: " + e);
		}
		return null;
	}
	private static boolean validateModsJson(JsonObject jsonObject){
		Gson gson = new Gson();
		boolean returnv = true;
		if (jsonObject != null) {
			if (jsonObject.has("version") && jsonObject.has("modLoader") && jsonObject.has("mods") && jsonObject.get("mods").isJsonArray()) {
				JsonArray modsArray = jsonObject.get("mods").getAsJsonArray();
				for (int i = 0; i < modsArray.size(); i++) {
					if (modsArray.get(i).isJsonObject()) {
						JsonObject modObject = modsArray.get(i).getAsJsonObject();
						if (modObject.has("pid") && modObject.has("name")) {
							if (!(modObject.get("pid").getAsJsonPrimitive().isString() && modObject.get("name").getAsJsonPrimitive().isString())) {
								System.out.println("Error: Invalid json: 'pid' and/or 'mod' value is not a string at " + gson.toJson(jsonObject));
								returnv = false;
							}
						} else {
							System.out.println("Error: Invalid json: Could not find 'pid' and/or 'mod' key at " + gson.toJson(jsonObject));
							returnv = false;
						}
					} else {
						System.out.println("Error: Invalid json: NotAnJsonObject at " + gson.toJson(modsArray.get(i)));
						returnv = false;
					}
				}
			} else {
				System.out.println("Error: Missing 'version', 'modLoader' or 'mods' field in json!");
				returnv = false;
			}
		} else {
			System.out.println("Error: Invalid json: ArraySize is 0 or does not exist at " + gson.toJson(jsonObject));
			returnv = false;
		}
		return returnv;
	}
	private static boolean validateManifestJson(JsonObject jsonObject){
		Gson gson = new Gson();
		boolean returnv = true;
		if (jsonObject != null) {
			if (jsonObject.has("minecraft") && jsonObject.get("minecraft").isJsonObject() && jsonObject.has("files") && jsonObject.get("files").isJsonArray()) {
				JsonArray modsArray = jsonObject.getAsJsonArray("files");
				for (int i = 0; i < modsArray.size(); i++) {
					if (modsArray.get(i).isJsonObject()) {
						JsonObject modObject = modsArray.get(i).getAsJsonObject();
						if (modObject.has("projectID") && modObject.has("fileID")) {
							if (!(modObject.getAsJsonPrimitive("projectID").isNumber() && modObject.get("fileID").getAsJsonPrimitive().isNumber())) {
								System.out.println("Error: Invalid json: 'projectID' and/or 'fileID' value is not a number at " + gson.toJson(modObject));
								returnv = false;
							}
						} else {
							System.out.println("Error: Invalid json: Could not find 'projectID' and/or 'fileID' key at " + gson.toJson(modObject));
							// returnv = false;
						}
					} else {
						System.out.println("Error: Invalid json: NotAnJsonObject at " + gson.toJson(modsArray.get(i)));
						returnv = false;
					}
				}
			} else {
				System.out.println("Error: Missing 'minecraft' or 'files' field in json!");
				returnv = false;
			}
		} else {
			System.out.println("Error: Invalid json: ArraySize is 0 or does not exist at " + gson.toJson(jsonObject));
			returnv = false;
		}
		return returnv;
	}
	private static int modloaderToInt(String modLoader) {
		try {
			Integer.parseInt(modLoader);
			return Integer.parseInt(modLoader);
		} catch (NumberFormatException ex) {
			switch(modLoader) {
			case "forge":
				return 1;
			case "fabric":
				return 4;
			default:
				return -1;
			}
		}
	}
	public static String[] checkMod(String pid, String version, String modLoader, boolean debug){
		String url = api_url + "/mods/" + pid;
		if(debug) System.out.println("Checking mod with pid " + pid + " for " + version);

		JsonObject jsonObject = getJSON(url);
		if (jsonObject != null) {
			// int fid = getFileForVersion(jsonObject, version, modLoader);
			JsonObject file = getFileForVersion(jsonObject, version, modLoader);
			if (file != null) {
				String fid = file.get("fileId").getAsString();
				String fileName = file.get("filename").getAsString();
				if(debug) System.out.println("Found mod download for " + version);
				return new String[]{fid, fileName};
			}

			System.out.println("Error: No matching mod file found for " + version);
			return new String[]{"0"};
		}
		System.out.println("Error: No addon found for pid " + pid);
		return new String[]{"0"};
	}
	public static int installMod(String pid, String version, String modLoader, String location, String name) {
		return installMod(pid, version, modLoader, location, name, "");
	}
	public static int installMod(String pid, String version, String modLoader, String location, String name, String fid) {
		if (name == "") name = pid;
		System.out.println("Installing mod " + name + " for " + version + "...");

		String fn = "";
		if (fid == "") {
			String[] file = checkMod(pid, version, modLoader, false);
			if (file[0] != "0"){
				fid = file[0];
				fn = file[1];
			}
		}
		String url = api_url + "/mods/" + pid + "/files/" + fid + "/download-url";
		JsonObject jsonObject = getJSON(url);
		if (jsonObject != null){
			String dl = jsonObject.get("data").getAsString(); // download link

			if (fn == "") fn = dl.substring( dl.lastIndexOf('/')+1, dl.length() );

			if (!new File(location + "/mods/").exists()) new File(location + "/mods/").mkdir();
			File fnr = new File(location + "/mods/" + fn);
			System.out.println("Downloading " + dl + "...");
			try {
				java.net.URL urll = new java.net.URL(dl);
				FileUtils.copyURLToFile(urll, fnr);
			} catch (Exception e){
				System.out.println("Could not download file: " + e);
				return 0;
			}
			System.out.println("Downloaded " + dl + "!");
			System.out.println("Installed mod " + name + " for " + version + "!");
			return 1;
		}
		return 0;
	}
	public static int updateAllMods(String location) {
		int returnv = 1;

		JsonObject config = readFileToJsonObject(location + "/mods.json");
		if (validateModsJson(config)) {
			File modsFolder = new File(location + "/mods/");
			if (!modsFolder.exists()) {
				modsFolder.mkdir();
			} else {
				File backupFolder = new File(location + "/backup_mods/" + new SimpleDateFormat("yyyy_M_dd-hh_mm_ss").format(new Date()) + "/");
				try {
					FileUtils.moveDirectory(modsFolder, backupFolder);
				} catch (java.io.IOException e){
					System.out.println("Error: Couldn't backup mods folder! " + e);
				}
			}
			String modLoader = config.get("modLoader").getAsString();
			JsonArray mods = config.get("mods").getAsJsonArray();
			for (int i = 0; i < mods.size(); i++) {
				JsonObject mod = mods.get(i).getAsJsonObject();
				if (installMod(mod.get("pid").getAsString(), config.get("version").getAsString(), modLoader, location, mod.get("name").getAsString()) == 0){
					returnv = 0;
				}
			}
		} else {
			returnv = 0;
		}
		return returnv;
	}
	final static Pattern pattern = Pattern.compile("([a-z]+)(-([0-9\\.]+)|)");
	public static int installModpack(String location) {
		int returnv = 1;

		JsonObject manifest = readFileToJsonObject(location + "/manifest.json");
		if (validateManifestJson(manifest)) {
			File modsFolder = new File(location + "/mods/");
			if (!modsFolder.exists()) {
				modsFolder.mkdir();
			} else {
				File backupFolder = new File(location + "/backup_mods/" + new SimpleDateFormat("yyyy_M_dd-hh_mm_ss").format(new Date()) + "/");
				try {
					FileUtils.moveDirectory(modsFolder, backupFolder);
				} catch (java.io.IOException e){
					System.out.println("Error: Couldn't backup mods folder! " + e);
				}
			}
			String modLoader = manifest.get("minecraft").getAsJsonObject().get("modLoaders").getAsJsonArray().get(0).getAsJsonObject().get("id").getAsString();
			Matcher matcher = pattern.matcher(modLoader);
			modLoader = matcher.replaceFirst("$1");

			String version = manifest.get("minecraft").getAsJsonObject().get("version").getAsString();
			JsonArray files = manifest.get("files").getAsJsonArray();
			for (int i = 0; i < files.size(); i++) {
				JsonObject file = files.get(i).getAsJsonObject();
				if (installMod(file.get("projectID").getAsString(), version, modLoader, location, file.get("projectID").getAsString(), file.get("fileID").getAsString()) == 0){
					returnv = 0;
				}
			}
		} else {
			returnv = 0;
		}
		return returnv;
	}
	private static void handleArgs(String[] args){
		switch (args[0]) {
			case "checkMod":
				showVersion();
				if (args.length >= 4) {
					if (checkMod(args[1], args[2], args[3], true)[0] == "0") System.out.println("Error: Couldn't find mod!");
				} else {
					System.out.println("Usage: " + args[0] + " <ProjectId> <version> <modLoader>");
				}
				break;
			case "installMod":
				showVersion();
				if (args.length == 5){
					if (installMod(args[1], args[2], args[3], args[4], "") == 0) System.out.println("Error: Couldn't install mod!");
				} else {
					System.out.println("Usage: " + args[0] + " <ProjectId> <version> <modLoader> <.minecraft location>");
				}
				break;
			case "updateAllMods":
				showVersion();
				if (args.length >= 2) {
					if (updateAllMods(args[1]) == 0) System.out.println("Error: Couldn't update all mods!");
				} else {
					System.out.println("Usage: " + args[0] + " <.minecraft location>");
				}
				break;
			case "installModpack":
				showVersion();
				if (args.length >= 2) {
					if (installModpack(args[1]) == 0) System.out.println("Error: Couldn't install modpack!");
				} else {
					System.out.println("Usage: " + args[0] + " <.minecraft location>");
				}
				break;
			case "help":
				System.out.println("Commands: \n- checkMod <pid> <version>\n- installMod <pid> <version> <.minecraft folder>\n- updateAllMods <.minecraft folder>\n- installModpack <.minecraft folder>\n- version");
				break;
			case "version":
				showVersion();
				break;
			default:
				System.out.println("Usage: <action> [arguments]\nCheck 'help' for more info!");
				System.exit(1);
		}
	}
	private static boolean shownVersion = false;
	private static void showVersion() {
		if (!shownVersion) {
			String version = App.class.getPackage().getImplementationVersion();
			System.out.println("Powered by ModUpdater " + version);
			shownVersion = true;
		}
	}

	public static void main(String[] args) {
		if (SettingsMenu.main(new String[]{settings_file})){
			api_key = SettingsMenu.configProps.getProp("api_key");
		} else {
			System.out.println("Error: Config was not set!");
			System.exit(1);
		}
		if (args.length > 0){
			handleArgs(args);
		} else {
			showVersion();
			try {
				if (Gui.main(new String[]{})){
					String location = "";
					if (Gui.getFolder() != null) location = Gui.getFolder().getAbsolutePath();
					String[] inputs = {Gui.getSubmit(), Gui.getPid(), Gui.getVersion(), Gui.getModLoader(), location};
					if (Gui.getSubmit() == "updateAllMods") inputs[1] = location;
					if (Gui.getSubmit() == "installModpack") inputs[1] = location;
					handleArgs(inputs);
				}
			} catch (java.awt.HeadlessException e) {
				System.out.println("Error: Cannot show GUI!\n");
				System.out.println("Usage: <action> [arguments]\nCheck 'help' for more info!");
				System.exit(1);
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		System.exit(0);
	}
}
