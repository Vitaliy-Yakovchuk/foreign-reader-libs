/*******************************************************************************
 * Copyright 2013 Vitaliy Yakovchuk
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.sdconvertor.utility;

import java.io.*;
import java.util.*;

/**
 * Created:	January 31, 2010
 * License:	GPLv3
 * @author	I-Fan Chen
 * @version	1
 *
 */
public class Utility {

	public static void log(String message){
		System.out.println("[Convertor] " + message);
	}
	
	public static HashMap<String, File> prepareDictionaryFiles(String path){
		File dir = new File(path);
		
		if (!dir.isDirectory()){
			return null;
		}
		
		HashMap<String, File> map = new HashMap<String, File>();
		File[] files = dir.listFiles();
		
		for (File file : files){
			if (!file.isFile()){
				continue;
			}
			
			if (file.getName().endsWith("ifo")){
				map.put("ifo", file);
			}
			else if (file.getName().endsWith("idx")){
				map.put("idx", file);
			}
			else if (file.getName().endsWith("dz")){
				map.put("dict", file);
			}
		}
		
		return map;
	}

}
