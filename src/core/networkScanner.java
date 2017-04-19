package core;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import main.Main;

public class networkScanner {
	private List<String> addresses = new ArrayList<String>();

	public List<String> scanNetwork() throws UnknownHostException, IOException {
		InetAddress localhost = InetAddress.getLocalHost();
		byte[] ip = localhost.getAddress();
		for (int i = 1; i <= 256; i++) {
			try {
				ip[3] = (byte) i;
				InetAddress address = InetAddress.getByAddress(ip);
				if (address.isReachable(100)) {
					String output = address.toString().substring(1);
					addresses.add(output);
					Main.logger.info(output + " is on the network");
				}
			} catch (Exception e) {
//				addresses = null;
				e.printStackTrace();
			}
		}
		return addresses;
	}
}
