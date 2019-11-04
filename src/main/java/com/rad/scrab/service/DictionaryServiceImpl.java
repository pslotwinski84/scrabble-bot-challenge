package com.rad.scrab.service;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service("dictionaryService")
public class DictionaryServiceImpl implements DictionaryService {

	private HashMap<String, ArrayList<String>> alphabet2 = null;
	private HashMap<String, Boolean> dict;

	@SuppressWarnings("unchecked")
	public void readAlphabet() {

		if (alphabet2 == null) {
			try {

				InputStream in = new ClassPathResource("alpha.txt").getInputStream();

				ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(in));

				alphabet2 = (HashMap<String, ArrayList<String>>) ois.readObject();
				ois.close();

				in.close();

			} catch (IOException ioe) {
				ioe.printStackTrace();
				return;
			} catch (ClassNotFoundException c) {
				System.out.println("Class not found");
				c.printStackTrace();
				return;
			}

		}
	}

	public void readDict() throws Exception {

		InputStream is = new ClassPathResource("slowa.txt").getInputStream();

		BufferedReader br = new BufferedReader(new BufferedReader(new InputStreamReader(is)));

		String st = new String();
		HashMap<String, Boolean> dict2 = new HashMap<String, Boolean>();
		while (st != null) {
			st = br.readLine();
			dict2.put(st, true);
		}
		this.dict = dict2;
		br.close();
		System.out.println(dict.size());
	}

	@Override
	public HashMap<String, Boolean> getDict() {

		return dict;
	}

	@Override
	public HashMap<String, ArrayList<String>> getAlphabet() {
		// TODO Auto-generated method stub
		return alphabet2;
	}

}
