package com.whereis.controller;

import com.whereis.authentication.GoogleAuthentication;
import com.whereis.model.Group;
import com.whereis.model.Invite;
import com.whereis.model.User;
import com.whereis.model.UsersInGroup;
import com.whereis.service.GroupService;
import com.whereis.service.InviteService;
import com.whereis.service.UserService;
import com.whereis.service.UsersInGroupsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;

@RestController
@RequestMapping("/group")
public class GroupController extends AbstractController {
    @Autowired
    GroupService groupService;

    @Autowired
    InviteService inviteService;

    @Autowired
    UsersInGroupsService usersInGroupsService;

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity createGroup(@RequestBody Group group) {
        try {
            groupService.save(group);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{identity}/invite", method = RequestMethod.POST)
    public ResponseEntity inviteUser(@PathVariable("identity") String identity, @RequestBody Invite invite) {
        invite.setGroupId(groupService.getByIdentity(identity).getId());
            if (inviteService.getSameInvite(invite) == null) {
                inviteService.save(invite);
            } else {
                // TODO: make response object and custom exceptions
                // send message that user already invited
                return new ResponseEntity(HttpStatus.I_AM_A_TEAPOT);
            }
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/join/{identity}", method = RequestMethod.POST)
    public ResponseEntity joinGroup(@PathVariable("identity") String identity) {
        Group targetGroup = groupService.getByIdentity(identity);
        if (targetGroup == null) {
            //TODO: message about wrong group
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        SecurityContext context = (SecurityContext) httpSession.getAttribute("SPRING_SECURITY_CONTEXT");
        Authentication auth = context.getAuthentication();
        User currentUser = (User) auth.getPrincipal();
        Invite inviteForUser = inviteService.getPendingInviteFor(currentUser, targetGroup);
        if (inviteForUser == null) {
            //TODO: message about no invite
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        UsersInGroup userInGroupPresence = new UsersInGroup();
        userInGroupPresence.setUserId(currentUser.getId());
        userInGroupPresence.setGroupId(targetGroup.getId());
        //TODO: move this to postgres
        userInGroupPresence.setJoinedAt(new Timestamp(System.currentTimeMillis()));
        usersInGroupsService.save(userInGroupPresence);
        inviteService.delete(inviteForUser);
        return new ResponseEntity(HttpStatus.OK);
    }
}
