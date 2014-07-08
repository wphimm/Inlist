package co.inlist.activities;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Application;

public class InListApplication extends Application {

	public static ArrayList<HashMap<String, String>> listEvents = new ArrayList<HashMap<String, String>>();
	public static ArrayList<HashMap<String, String>> list_music_types = new ArrayList<HashMap<String, String>>();
	public static ArrayList<HashMap<String, String>> gallery = new ArrayList<HashMap<String, String>>();
	public static ArrayList<HashMap<String, String>> party_area = new ArrayList<HashMap<String, String>>();
	public static ArrayList<HashMap<String, String>> pricing = new ArrayList<HashMap<String, String>>();

	public static ArrayList<HashMap<String, String>> getPricing() {
		return pricing;
	}

	public static void setPricing(ArrayList<HashMap<String, String>> pricing) {
		InListApplication.pricing = pricing;
	}

	public static ArrayList<HashMap<String, String>> getGallery() {
		return gallery;
	}

	public static ArrayList<HashMap<String, String>> getParty_area() {
		return party_area;
	}

	public static void setParty_area(
			ArrayList<HashMap<String, String>> party_area) {
		InListApplication.party_area = party_area;
	}

	public static void setGallery(ArrayList<HashMap<String, String>> gallery) {
		InListApplication.gallery = gallery;
	}

	public static ArrayList<HashMap<String, String>> getList_music_types() {
		return list_music_types;
	}

	public static void setList_music_types(
			ArrayList<HashMap<String, String>> list_music_types) {
		InListApplication.list_music_types = list_music_types;
	}

	public static ArrayList<HashMap<String, String>> getListEvents() {
		return listEvents;
	}

	public static void setListEvents(
			ArrayList<HashMap<String, String>> listEvents) {
		InListApplication.listEvents = listEvents;
	}

}
