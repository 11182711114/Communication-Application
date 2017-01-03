package util;

import java.io.File;
import java.io.IOException;

public class DeviceIdExtractor {
	/** Extracts from Folder
	 * @param parent - the parent folder
	 * @param folder - the folder from which to extract
	 * @return the deviceId
	 * @throws IOException
	 */
	public static String extractFromFolder(File parent, File folder) throws IOException{
		System.out.println(parent.getCanonicalPath());
		System.out.println(folder.getCanonicalPath());
		String[] parentPath = parent.getCanonicalPath().split(File.separator);
		for(String s : parentPath){
			System.out.print(s + ",");
		}
		String[] folderPath = folder.getCanonicalPath().split(File.separator);
		for(String s : folderPath){
			System.out.print(s + ",");
		}
		String output = folderPath[parentPath.length];
		
		return output;
	}
}
