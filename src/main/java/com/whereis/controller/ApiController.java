package com.whereis.controller;

import com.whereis.exceptions.groups.NoUserInGroup;
import com.whereis.exceptions.groups.UserAlreadyInGroup;
import com.whereis.exceptions.invites.NoInviteForUserToGroup;
import com.whereis.exceptions.invites.UserAlreadyInvited;
import com.whereis.model.*;
import com.whereis.service.GroupService;
import com.whereis.service.InviteService;
import com.whereis.service.LocationService;
import com.whereis.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/group")
public class ApiController extends AbstractController {
    @Autowired
    GroupService groupService;

    @Autowired
    InviteService inviteService;

    @Autowired
    UserService userService;

    @Autowired
    LocationService locationService;

    private static final Logger logger = LogManager.getLogger(ApiController.class);

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity createGroup(@RequestBody Group group) {
        try {
            groupService.save(group);
            userService.joinGroup(group, getCurrentUser());

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

        try {
            userService.joinGroup(targetGroup, currentUser);
        } catch (UserAlreadyInGroup userAlreadyInGroup) {
            logger.error(userAlreadyInGroup.getMessage(), userAlreadyInGroup);
            //TODO: message about trying to add dublicate user
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } catch (NoInviteForUserToGroup noInviteForUserToGroup) {
            //TODO: message about adding user without invite
            logger.error(noInviteForUserToGroup.getMessage(), noInviteForUserToGroup);
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/{identity}/leave", method = RequestMethod.DELETE)
    public ResponseEntity leaveGroup(@PathVariable("identity") String identity) {
        User currentUser = getCurrentUser();
        try {
            userService.leaveGroup(groupService.getByIdentity(identity), currentUser);
        } catch (NoUserInGroup noUserInGroup) {
            //TODO: message about user is not in the group
            logger.error(noUserInGroup.getMessage(), noUserInGroup);
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/{identity}/savemylocation", method = RequestMethod.PUT)
    public ResponseEntity saveUsersLocation(@PathVariable("identity") String identity,
                                       @RequestBody Location location,
                                       HttpServletRequest request) {
        if (request.getRemoteAddr() != null) {
            location.setIp(request.getRemoteAddr());
        }
        location.setGroup(groupService.getByIdentity(identity));
        locationService.save(location);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/{identity}/getlocations", method = RequestMethod.GET)
    public List<Location> getLocationOfGroupMembers(@PathVariable("identity") String identity) {
        return locationService.getLocationsOfGroupMembers(groupService.getByIdentity(identity), getCurrentUser());
    }
}
