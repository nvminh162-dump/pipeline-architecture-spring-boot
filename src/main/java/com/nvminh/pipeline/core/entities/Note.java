package com.nvminh.pipeline.core.entities;

public class Note {

	private final String content;

	public Note(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "Note{" +
				"content='" + content + '\'' +
				'}';
	}
}
