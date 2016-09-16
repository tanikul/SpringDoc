package com.java.doc.dao;
// master 5
import java.util.List;

import com.java.doc.model.Attachment;

public interface AttachmentDAO {

	public List<Attachment> listAttachment(int objectId, String objectName);
	public Attachment getAttachment(int attachmentId);
	public int save(Attachment attachment);
	public void updateObjectId(String attachmentList, int objectId, String objectName);
	public boolean delete(int attachmentId);
}
