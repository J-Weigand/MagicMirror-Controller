package core;

import java.io.File;
import java.io.IOException;

import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;

public class mirrorGateway {
	static FTPClient client = new FTPClient();
	static String ftpUser = null;
	static String ftpPswd = null;
	static String ftpHost = null;
	static String ftpDir = "/home/pi/Desktop/";

	public static boolean testConnection() {
		try {
			client.connect(ftpHost);
			client.login(ftpUser, ftpPswd);
			client.disconnect(false);
		} catch (IllegalStateException | IOException | FTPIllegalReplyException | FTPException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static boolean uploadConfig() {
		try {
			client.connect(ftpHost);
			client.login(ftpUser, ftpPswd);
			client.changeDirectory(ftpDir);
			client.setType(FTPClient.TYPE_BINARY);
			System.out.println("Uploading File...");
			String configFile = "config.txt";
			client.upload(new File(configFile));
			System.out.println("Upload Successful!");
			client.disconnect(false);
		} catch (IllegalStateException | IOException | FTPIllegalReplyException | FTPException
				| FTPDataTransferException | FTPAbortedException e2) {
			e2.printStackTrace();
			return false;
		}
		return true;
	}

	public static String getFtpUser() {
		return ftpUser;
	}

	public static void setFtpUser(String ftpUser) {
		mirrorGateway.ftpUser = ftpUser;
	}

	public static String getFtpPswd() {
		return ftpPswd;
	}

	public static void setFtpPswd(String ftpPswd) {
		mirrorGateway.ftpPswd = ftpPswd;
	}

	public static String getFtpHost() {
		return ftpHost;
	}

	public static void setFtpHost(String ftpHost) {
		mirrorGateway.ftpHost = ftpHost;
	}
}
