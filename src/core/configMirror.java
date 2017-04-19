package core;

import java.io.IOException;
import java.io.PrintWriter;

import main.Main;

public class configMirror {

	public boolean createConfig(boolean time, boolean news, boolean weather) {
		int TIME = 0, NEWS = 0, WEATHER = 0;
		if (time)
			TIME = 1;
		if (news)
			NEWS = 1;
		if (weather)
			WEATHER = 1;
		Main.logger.error("Creating new config file");
		try {
			PrintWriter writer = new PrintWriter("config.txt", "UTF-8");
			writer.println("TIME " + TIME);
			writer.println("NEWS " + NEWS);
			writer.println("WEATHER " + WEATHER);
			writer.close();
		} catch (IOException e) {
			Main.logger.error("Unable to create new config file.");
			return false;
		}
		Main.logger.error("Config file created");
		return true;
	}
}
