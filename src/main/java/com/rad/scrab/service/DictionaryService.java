package com.rad.scrab.service;

import java.util.ArrayList;
import java.util.HashMap;

public interface DictionaryService {

	public void readAlphabet();

	public HashMap<String, ArrayList<String>> getAlphabet();

	public void readDict() throws Exception;

	public HashMap<String, Boolean> getDict();
}
