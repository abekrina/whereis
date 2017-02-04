package com.whereis.controller;

import com.whereis.exceptions.NoUserInGroup;
import com.whereis.exceptions.UserAlreadyInGroup;
import com.whereis.exceptions.UserAlreadyInvited;
import com.whereis.model.*;
import com.whereis.service.GroupService;
import com.whereis.service.InviteService;
import com.whereis.service.LocationService;
import com.whereis.service.UsersInGroupsService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

@RestController
@RequestMapping("/group")
public class ApiController extends AbstractController {
    @Autowired
    GroupService groupService;

    @Autowired
    InviteService inviteService;

    @Autowired
    UsersInGroupsService usersInGroupsService;

    @Autowired
    LocationService locationService;

    private static final Logger logger = LogManager.getLogger(ApiController.class);

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity createGroup(@RequestBody Group group) {
        try {
            // This method automatically adds current user in his new group
            groupService.save(group, getCurrentUser());

        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{identity}/invite", method = RequestMethod.POST)
    public ResponseEntity inviteUser(@PathVariable("identity") String identity, @RequestBody Invite invite) {
        invite.setGroupId(groupService.getByIdentity(identity).getId());
        try {
            inviteService.save(invite);
        } catch (UserAlreadyInvited userAlreadyInvited) {
            // TODO: make response object and custom exceptions
            // send message that user already invited
            return new ResponseEntity(HttpStatus.I_AM_A_TEAPOT);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/{identity}/join", method = RequestMethod.POST)
    public ResponseEntity joinGroup(@PathVariable("identity") String identity) {
        Group targetGroup = groupService.getByIdentity(identity);
        if (targetGroup == null) {
            //TODO: message about wrong group
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        User currentUser = getCurrentUser();
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
        try {
            usersInGroupsService.save(userInGroupPresence);
            inviteService.delete(inviteForUser);
        } catch (UserAlreadyInGroup userAlreadyInGroup) {
            logger.error("Trying to add user which alresdy in group " + userInGroupPresence.toString());
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/{identity}/leave", method = RequestMethod.DELETE)
    public ResponseEntity leaveGroup(@PathVariable("identity") String identity) {
        User currentUser = getCurrentUser();
        try {
            usersInGroupsService.leave(identity, currentUser);
        } catch (NoUserInGroup noUserInGroup) {
            logger.error("No user " + currentUser.getEmail() + " in group " + identity, noUserInGroup);
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/{identity}/savemylocation", method = RequestMethod.PUT)
    public ResponseEntity saveUsersLocation(@PathVariable("identity") String identity,
                                       @RequestBody Location location,
                                       HttpServletRequest request) {
        location.setTimestamp(new Timestamp(Calendar.getInstance().getTime().getTime()));
        if (request.getRemoteAddr() != null) {
            location.setIp(request.getRemoteAddr());
        }
        location.setGroupIdentity(identity);
        locationService.save(location);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/{identity}/getlocations", method = RequestMethod.GET)
    public List<Location> getLocationOfGroupMembers(@PathVariable("identity") String identity) {
        return locationService.getLocationsOfGroupMembers(groupService.getByIdentity(identity), getCurrentUser());
    }

    private User getCurrentUser() {
        SecurityContext context = (SecurityContext) httpSession.getAttribute("SPRING_SECURITY_CONTEXT");
        Authentication auth = context.getAuthentication();
        return (User) auth.getPrincipal();
    }
}
