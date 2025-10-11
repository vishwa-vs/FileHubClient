package com.filehub.client.audit.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.filehub.client.audit.entity.AuditLog;

@Repository
public interface AuditRepo extends JpaRepository<AuditLog, Integer> {

}
