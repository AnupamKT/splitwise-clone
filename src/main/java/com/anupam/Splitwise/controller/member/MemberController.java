package com.anupam.Splitwise.controller.member;

import com.anupam.Splitwise.model.Response;
import com.anupam.Splitwise.service.member.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("splitwise/member")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @PostMapping("/{groupId}/{email}")
    public ResponseEntity addMember(@PathVariable UUID groupId,@PathVariable String email) throws Exception {
        Response response = memberService.createGroupMember(groupId,email);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/{groupId}/{email}")
    public ResponseEntity removeMember(@PathVariable UUID groupId,@PathVariable String email) throws Exception {
        Response response = memberService.removeGroupMember(groupId,email);
        return ResponseEntity.status(response.getStatus()).body(response);
    }


}
