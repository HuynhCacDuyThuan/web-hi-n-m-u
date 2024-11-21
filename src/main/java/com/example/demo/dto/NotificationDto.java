package com.example.demo.dto;

import java.math.BigInteger;

public class NotificationDto {
    private BigInteger id;
    private String title;
    private String content;
    private String noticeDate;

    public NotificationDto(BigInteger id, String title, String content, String noticeDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.noticeDate = noticeDate;
    }

	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
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

	public String getNoticeDate() {
		return noticeDate;
	}

	public void setNoticeDate(String noticeDate) {
		this.noticeDate = noticeDate;
	}

   
}
