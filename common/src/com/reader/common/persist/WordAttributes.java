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
package com.reader.common.persist;

import java.io.Serializable;

import com.reader.common.ColorConstants;

public class WordAttributes implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8649014296761073022L;

	private String color = ColorConstants.DEFAULT;

	private boolean startsWithPhrase;
	
	private boolean transport;
	
	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public boolean isStartsWithPhrase() {
		return startsWithPhrase;
	}

	public void setStartsWithPhrase(boolean startsWithPhrase) {
		this.startsWithPhrase = startsWithPhrase;
	}

	public boolean isTransport() {
		return transport;
	}

	public void setTransport(boolean transport) {
		this.transport = transport;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		result = prime * result + (startsWithPhrase ? 1231 : 1237);
		result = prime * result + (transport ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WordAttributes other = (WordAttributes) obj;
		if (color == null) {
			if (other.color != null)
				return false;
		} else if (!color.equals(other.color))
			return false;
		if (startsWithPhrase != other.startsWithPhrase)
			return false;
		if (transport != other.transport)
			return false;
		return true;
	}

}
