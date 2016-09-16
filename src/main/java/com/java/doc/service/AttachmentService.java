package com.java.doc.service;

import java.util.List;

import com.java.doc.model.Attachment;

public interface AttachmentService {
	public List<Attachment> listAttachment(int objectId, String objectName);
	public Attachment getAttachment(int attachmentId);
	public int save(Attachment attachment);
	public void updateObjectId(String attachmentList, int objectId, String objectName);
	public boolean delete(int attachmentId);
}
