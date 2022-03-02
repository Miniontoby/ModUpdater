package com.miniontoby.ModUpdater;

import com.google.gson.*;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;
import java.util.Scanner;
import org.apache.commons.io.FileUtils;

public class App {
	private static String api_url = "https://addons-ecs.forgesvc.net/api/v2";
	private static JsonObject getJSON(String url) {
		try (java.io.InputStream is = new java.net.URL(url).openStream()) {
			String contents = new String(is.readAllBytes());
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
	// private static int getFileForVersion(JsonObject rootObject, String version, int modLoader){
	private static int getFileForVersion(JsonObject rootObject, String version){
		JsonArray files = rootObject.getAsJsonArray("gameVersionLatestFiles");
		for (int i=0; i<files.size(); i++) {
			JsonObject file = files.get(i).getAsJsonObject();
			// if (file.get("modLoader").getAsInt() == modLoader){
				if (file.get("gameVersion").isJsonArray() ){
					JsonArray versionArray = file.get("ganeVersion").getAsJsonArray();
					for (int j=0; j < versionArray.size(); j++) {
						if (versionArray.get(j).getAsString().equals(version)){
							return file.get("projectFileId").getAsInt();
						}
					}
				} else if (file.get("gameVersion").getAsJsonPrimitive().isString()) {
					if (file.get("gameVersion").getAsString().equals(version)) {
						return file.get("projectFileId").getAsInt();
					}
				}
			// }
		}
		return -1;
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
	private static boolean valildateModsJson(JsonObject jsonObject){
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
				return 0;
			}
		}
	}
	public static int checkMod(String[] args, boolean debug){
		if (args.length < 3){
			System.out.println("Usage: " + args[0] + " <ProjectId> <version>");
			return -1;
		}
		String pid = args[1];
		String version = args[2];
		// int modLoader = modloaderToInt(args[3]);
		String url = api_url + "/addon/" + pid;
		if(debug) System.out.println("Checking mod with pid " + pid + " for " + version);

		JsonObject jsonObject = getJSON(url);
		if (jsonObject != null) {
			// int fid = getFileForVersion(jsonObject, version, modLoader);
			int fid = getFileForVersion(jsonObject, version);
			if (fid != -1) {
				if(debug) System.out.println("Found mod download for " + version);
				return fid;
			}

			System.out.println("Error: No matching mod file found for " + version);
			return 0;
		}
		System.out.println("Error: No addon found for pid " + pid);
		return 0;
	}
	public static int installMod(String[] args, String name) {
		if (args.length < 4){
			System.out.println("Usage: " + args[0] + " <ProjectId> <version> <path to .minecraft>");
			return -1;
		}
		String pid = args[1];
		String version = args[2];
		// int modLoader = modloaderToInt(args[3])
		String location = args[3];
		if (name == "") name = pid;
		System.out.println("Installing mod " + name + " for " + version + "...");

		int fid = checkMod(args, false);
		if (fid != 0){
			String url = api_url + "/addon/" + pid + "/file/" + fid;
			JsonObject jsonObject = getJSON(url);
			if (jsonObject != null){
				String fn = jsonObject.get("fileName").getAsString();
				String dl = jsonObject.get("downloadUrl").getAsString();

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
		}
		return 0;
	}
	public static int updateAllMods(String[] args){
		if (args.length < 2){
			System.out.println("Usage: " + args[0] + " <path to .minecraft>");
			return -1;
		}
		String location = args[1];
		int returnv = 1;

		JsonObject config = readFileToJsonObject(location + "/mods.json");
		if (valildateModsJson(config)) {
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
			JsonArray mods = config.get("mods").getAsJsonArray();
			for (int i = 0; i < mods.size(); i++) {
				JsonObject mod = mods.get(i).getAsJsonObject();
				if (installMod(new String[]{"", mod.get("pid").getAsString(), config.get("version").getAsString(), location}, mod.get("name").getAsString()) == 0){
					returnv = 0;
				}
			}
		} else {
			returnv = 0;
		}
		return returnv;
	}
	private static void handleArgs(String[] args){
		switch (args[0]){
			case "checkMod":
				showVersion();
				if (checkMod(args, true) == 0) System.out.println("Error: Couldn't find mod!");
				break;
			case "installMod":
				showVersion();
				if (installMod(args, "") == 0) System.out.println("Error: Couldn't install mod!");
				break;
			case "updateAllMods":
				showVersion();
				if (updateAllMods(args) == 0) System.out.println("Error: Couldn't update all mods!");
				break;
			case "help":
				System.out.println("Commands: \n- checkMod <pid> <version>\n- installMod <pid> <version> <.minecraft folder>\n- updateAllMods <.minecraft folder>\n- version");
				break;
			case "version":
				showVersion();
				break;
			default:
				System.out.println("Usage: <action> [arguments]\nCheck 'help' for more info!");
				System.exit(1);
		}
	}

	private static void showVersion() {
		String version = App.class.getPackage().getImplementationVersion();
		System.out.println("Powered by ModUpdater " + version);
	}

	public static void main(String[] args) {
		if (args.length > 0){
			handleArgs(args);
		} else {
			showVersion();
			try {
				if (UpdaterGui.main(new String[]{})){
					String location = "";
					if (UpdaterGui.getFolder() != null) location = UpdaterGui.getFolder().getAbsolutePath();
					String[] inputs = {UpdaterGui.getSubmit(), UpdaterGui.getPid(), UpdaterGui.getVersion(), location};
					if (UpdaterGui.getSubmit() == "updateAllMods") inputs[1] = location;
					handleArgs(inputs);
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		System.exit(0);
	}
}
