package com.java.doc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.java.doc.dao.AttachmentDAO;
import com.java.doc.model.Attachment;

@Service("attachmentService")
public class AttachmentServiceImpl implements AttachmentService {

	@Autowired
	@Qualifier("attachmentDao")
	private AttachmentDAO attachments;
	
	public AttachmentDAO getAttachments() {
		return attachments;
	}

	public void setAttachments(AttachmentDAO attachmentDAO) {
		this.attachments = attachmentDAO;
	}

	@Override
	public List<Attachment> listAttachment(int objectId, String objectName) {
		return attachments.listAttachment(objectId, objectName);
	}

	@Override
	public int save(Attachment attachment) {
		return attachments.save(attachment);
	}

	@Override
	public Attachment getAttachment(int attachmentId) {
		return attachments.getAttachment(attachmentId);
	}

	@Override
	public boolean delete(int attachmentId) {
		return attachments.delete(attachmentId);
	}

	@Override
	public void updateObjectId(String attachmentList, int objectId, String objectName) {
		attachments.updateObjectId(attachmentList, objectId, objectName);
	}

}
