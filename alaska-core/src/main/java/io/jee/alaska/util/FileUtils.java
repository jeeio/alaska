package io.jee.alaska.util;

import java.io.File;

public class FileUtils {
	
	public static void removes(String path, long beforeTime){
		File dir = new File(path);
		if(dir.exists()){
			File[] childFiles = dir.listFiles();
			if(childFiles==null){
				dir.delete();
			}else{
				for (File file : childFiles) {
					if(file.isDirectory()){
						removes(file.getPath(), beforeTime);
					}else{
						if(file.lastModified()<beforeTime){
							file.delete();
						}
					}
				}
			}
		}
	}
	
	public static String convertFileSize(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;
 
        if (size >= gb) {
            return String.format("%.1f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else
            return String.format("%d B", size);
    }
	
    public static String getExtensionName(String filename) { 
        if ((filename != null) && (filename.length() > 0)) { 
            int dot = filename.lastIndexOf('.'); 
            if ((dot >-1) && (dot < (filename.length() - 1))) { 
                return filename.substring(dot + 1); 
            } 
        } 
        return null; 
    } 

    public static String getFileNameNoEx(String filename) { 
        if ((filename != null) && (filename.length() > 0)) { 
            int dot = filename.lastIndexOf('.'); 
            if ((dot >-1) && (dot < (filename.length()))) { 
                return filename.substring(0, dot); 
            } 
        } 
        return null; 
    }
}
