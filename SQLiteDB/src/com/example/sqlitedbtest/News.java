package com.example.sqlitedbtest;

import java.io.Serializable;

public class News implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8542909293315608153L;
	private String title;
	private String content;

	public News() {
	}

	public News(String title, String content) {
		this.title = title;
		this.content = content;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
