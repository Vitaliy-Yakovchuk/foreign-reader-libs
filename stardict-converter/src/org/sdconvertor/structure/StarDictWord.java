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
package org.sdconvertor.structure;

/**
 * Created:	January 31, 2010
 * License:	GPLv3
 * @author	I-Fan Chen
 * @version	1
 *
 */
public class StarDictWord{
	String name;
	int offset, size;

	public StarDictWord(String content, int offset, int size){
		setName(content);
		setOffset(offset);
		setSize(size);
	}

///////////////////////////////////////////////////////////////////////////////
// Getters and Setters
	
	public String getName(){return name;}
	public void setName(String content){this.name = content;}
	
	public int getOffset(){return offset;}
	public void setOffset(int offset){this.offset = offset;}
	
	public int getSize(){return size;}
	public void setSize(int size){this.size = size;}

}
