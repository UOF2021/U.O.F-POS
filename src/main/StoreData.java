package main;

public class StoreData {
	private String storeName = "����ŷ";
	private String storeLocation = "255 Seongam-ro, Sangam-dong, Mapo-gu, Seoul";
	private Menu menu;
	
	public String getStoreName() {
		return storeName;
	}

	public String getStoreLocation() {
		return storeLocation;
	}
	
	public String getMenu() {
		return menu.getMenu();
	}
	
	
}
