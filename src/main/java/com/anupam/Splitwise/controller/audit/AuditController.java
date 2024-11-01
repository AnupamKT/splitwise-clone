package com.anupam.Splitwise.controller.audit;

import com.anupam.Splitwise.exception.audit.AuditException;
import com.anupam.Splitwise.model.Response;
import com.anupam.Splitwise.service.audit.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("splitwise/activity")
public class AuditController {

    @Autowired
    private AuditService auditService;

    @GetMapping("/{email}")
    public ResponseEntity getActivityForEmail(@PathVariable String email) throws AuditException {
        Response response= auditService.getActivityForEmail(email);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
