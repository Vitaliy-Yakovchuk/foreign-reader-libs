package com.reader.common.impl;

import java.util.Arrays;
import java.util.HashMap;

import com.reader.common.AbstractDatabase;
import com.reader.common.persist.WordAttributes;

public class MemoryDatabase extends AbstractDatabase {

	private HashMap<Bytes, Bytes> attributes = new HashMap<Bytes, Bytes>();

	private class Bytes{
		final byte[] bs;
		
		public Bytes(byte[] bs) {
			this.bs = bs;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + Arrays.hashCode(bs);
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
			Bytes other = (Bytes) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (!Arrays.equals(bs, other.bs))
				return false;
			return true;
		}

		private MemoryDatabase getOuterType() {
			return MemoryDatabase.this;
		}
	}
	
	@Override
	public void putA(String word, WordAttributes wordAttributes) {
		byte[] bytes = bytes(word);
		attributes.put(new Bytes(bytes), new Bytes(bytes(wordAttributes)));
	}

	@Override
	public WordAttributes getA(String word) {
		byte[] bytes = bytes(word);
		Bytes bytes2 = attributes.get(new Bytes(bytes));
		if(bytes2==null)
			return null;
		return fromBytes(bytes2.bs);
	}

	@Override
	public void removeA(String word) {
		attributes.remove(bytes(word));
	}

}
