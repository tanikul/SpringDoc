package com.java.doc.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.java.doc.dao.AttachmentDAO;
import com.java.doc.model.Attachment;

@Service
public class AttachmentServiceImpl implements AttachmentService {

	private AttachmentDAO attachments;
	
	public AttachmentDAO getAttachments() {
		return attachments;
	}

	public void setAttachments(AttachmentDAO attachmentDAO) {
		this.attachments = attachmentDAO;
	}

	@Override
	@Transactional
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
