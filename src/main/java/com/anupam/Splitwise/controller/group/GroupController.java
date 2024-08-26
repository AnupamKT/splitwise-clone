package com.anupam.Splitwise.controller.group;

import com.anupam.Splitwise.model.Response;
import com.anupam.Splitwise.model.group.Group;
import com.anupam.Splitwise.service.group.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("splitwise/group")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @PostMapping
    public ResponseEntity createGroup(@RequestBody Group group) throws Exception {
        Response response = groupService.createGroup(group);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/{name}")
    public ResponseEntity getGroupDetails(@PathVariable(name = "name", required = true) String name) throws Exception {
       Response response = groupService.getGroup(name);
       return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/{name}")
    public ResponseEntity deleteGroup(@PathVariable(name = "name", required = true) String name) throws Exception {
        Response response = groupService.deleteGroup(name);
        return ResponseEntity.status(response.getStatus()).build();
    }

    @PatchMapping
    public ResponseEntity updateGroupDetails(@RequestBody Group group) {
        return null;
    }
}
