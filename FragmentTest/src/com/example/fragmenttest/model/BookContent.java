package com.example.fragmenttest.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookContent {

	public static class Book {
		public Integer id;
		public String title;
		public String desc;

		public Book(Integer id, String title, String desc) {
			this.id = id;
			this.title = title;
			this.desc = desc;
		}

		public String toString() {
			return title;
		}
	}

	public static List<Book> ITEMS = new ArrayList<Book>();
	public static Map<Integer, Book> ITEM_MAP = new HashMap<Integer, Book>();

	static {
		addItem(new Book(1, "Readable code Book",
				"A book tells you how to enread the code"));
		addItem(new Book(2, "Thinking in java", "Basic Java Knowledge"));
		addItem(new Book(3, "Design Patterns", "Interface applications"));
	}

	private static void addItem(Book book) {
		ITEMS.add(book);
		ITEM_MAP.put(book.id, book);
	}

}
