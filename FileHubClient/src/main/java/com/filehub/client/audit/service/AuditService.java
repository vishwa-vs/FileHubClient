package com.filehub.client.audit.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.filehub.client.audit.entity.AuditLog;
import com.filehub.client.audit.repo.AuditRepo;

@Service
public class AuditService {
	
	@Autowired
	AuditRepo auditRepo;
	
	public void setValue(String userName, String module, String action) 
	{
		AuditLog auditLog = new AuditLog();
		auditLog.userName = userName;
		auditLog.module = module;
		auditLog.action = action;
		auditLog.time = LocalDateTime.now();
		
		auditRepo.save(auditLog);
	}
}
