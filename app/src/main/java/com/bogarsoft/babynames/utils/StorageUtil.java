package com.bogarsoft.babynames.utils;

import android.content.Context;
import android.content.SharedPreferences;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by mohan on 7/16/17.
 */
public class StorageUtil {
	private final String STORAGE = "com.bogarsoft.babynames.STORAGE";
	private SharedPreferences preferences;
	private Context context;
	public StorageUtil(Context context) {
		this.context = context;
	}

	public void setPreferredLanguage(String language) {
		preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("language", language);
		editor.commit();
	}
	public Locale getPreferredLanguage() {
		preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
		String value =  preferences.getString("language","None");
		if (value.equals("None")){
			return Locale.getDefault();
		}else {
			return new Locale(value);
		}
	}

	public String getSavedLanguage(){
		preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
		return preferences.getString("language",Locale.getDefault().getLanguage());

	}

	public void prefferedLyricsLanguage(String language) {
		preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("lyrics_language", language);
		editor.commit();
	}
	public String getPreferredLyricsLanguage() {
		preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
		return preferences.getString("lyrics_language", getPrefferedLanguageBasedOnLocale());
	}

	public void clearAll() {
		preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.clear();
		editor.commit();
	}

	private String getPrefferedLanguageBasedOnLocale(){
		if (getSavedLanguage().equals("en")){
			return Constants.Language.ENGLISH.toString();
		}else {
			return Constants.Language.TAMIL.toString();
		}
	}


	//store updatecancel with verison code
	public void setUpdateCancel(boolean isCancel) {
		preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean("update_cancel", isCancel);
		editor.apply();
	}

	public boolean getUpdateCancel() {
		preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
		return preferences.getBoolean("update_cancel", false);
	}

	//store updatecancel with verison code
	public void setUpdateCancelVersion(int version) {
		preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt("update_cancel_version", version);
		editor.apply();
	}

	public int getUpdateCancelVersion() {
		preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
		return preferences.getInt("update_cancel_version", 0);
	}

	//store updatecancel count
	public void setUpdateCancelCount(int count) {
		preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt("update_cancel_count", count);
		editor.apply();
	}

	public int getUpdateCancelCount() {
		preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
		return preferences.getInt("update_cancel_count", 0);
	}


	//store selected gender array
	public void setGenderArray(ArrayList<String> genders){
		preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		Gson gson = new Gson();
		String json = gson.toJson(genders);
		editor.putString(Constants.GENDER_FILTER, json);
		editor.apply();
	}

	public ArrayList<String> getGenderArray(){
		preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
		Gson gson = new Gson();
		String json = preferences.getString(Constants.GENDER_FILTER, "");
		if (json.isEmpty()) {
			ArrayList<String> arrayList = new ArrayList<>();
			arrayList.add("Boy");
			arrayList.add("Girl");
			arrayList.add("Unisex");
			return arrayList;
		} else {
			Type type = new TypeToken<ArrayList<String>>() {
			}.getType();
			return gson.fromJson(json, type);
		}
	}


}