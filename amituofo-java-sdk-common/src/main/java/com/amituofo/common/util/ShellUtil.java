package com.amituofo.common.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShellUtil {
    /**
     * 返回String List
     * @param command
     * @return
     * @throws Exception 
     */
    public static List<String> toStringList(String string) throws Exception {
		String[] lines = string.split("\\n");
		return Arrays.asList(lines);
    }
    
    /**
     * 返回StringArray List
     * @param command
     * @return
     * @throws Exception 
     */
    public static List<String[]> toArrayList(String string, String...replace) throws Exception {
    	List<String> lines = toStringList(string);
		List<String[]> list = new ArrayList<>();
		
		for(String line : lines) {
			for(String rep : replace) {
				line = line.replace(rep, "$");
			}
//			line = line.replace(" ", "$");
//			line = line.replace("  ", "$");
//			line = line.replace("$ ", "$");
			while(line.indexOf("$$")> -1) {
				line = line.replace("$$", "$");
			}
			list.add(line.split("\\$"));
		}
		
		return list;
    }
    
//    public static void main(String[] args) {
//		System.out.println("Filesystem      Size  Used Avail Use% Mounted on".replace("Use% ", "Use%$"));
//	}
}
