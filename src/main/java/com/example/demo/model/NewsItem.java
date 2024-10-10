package com.example.demo.model;

public class NewsItem {
	private String title;
	private String link;
	private String imageUrl;

	public NewsItem(String title, String link, String imageUrl) {
		this.title = title;
		this.link = link;
		this.imageUrl = imageUrl;
	}

	public String getTitle() {
		return title;
	}

	public String getLink() {
		return link;
	}

	public String getImageUrl() {
		return imageUrl;
	}
}
