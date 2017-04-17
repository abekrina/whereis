package com.whereis.controller;

import com.whereis.exceptions.groups.GroupWithIdentityExistsException;
import com.whereis.exceptions.groups.NoUserInGroupException;
import com.whereis.exceptions.groups.UserAlreadyInGroupException;
import com.whereis.exceptions.invites.NoInviteForUserToGroupException;
import com.whereis.exceptions.invites.UserAlreadyInvitedException;
import com.whereis.exceptions.users.UserWithEmailExistsException;
import com.whereis.model.*;
import com.whereis.service.GroupService;
import com.whereis.service.InviteService;
import com.whereis.service.LocationService;
import com.whereis.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
    public Group createGroup(@RequestBody Group group) {

        groupService.save(group);
        try {
            inviteService.saveInviteForUser(new Invite(getCurrentUser(), group, getCurrentUser()));
        } catch (UserAlreadyInvitedException e) {
            logger.error("Invite for new group already exists, probably it is a collision", e);
        }
        try {
            userService.joinGroup(group, getCurrentUser());
        } catch (UserAlreadyInGroupException | NoInviteForUserToGroupException e) {
            logger.error("Error during creation of new group", e);
            groupService.delete(group);
            return new Group();
        }
        return groupService.get(group.getId());
    }

    @RequestMapping(value = "/{identity}/invite", method = RequestMethod.POST)
    public ResponseEntity inviteUser(@PathVariable("identity") String identity, @RequestBody User invitedUser) {
        if (userService.getByEmail(invitedUser.getEmail())==null) {
            try {
                userService.save(invitedUser);
            } catch (UserWithEmailExistsException e) {
                //TODO: write error message
                logger.error("Error during savin user from invite ", e);
            }
        }
        Invite invite = new Invite();
        invite.setGroup(groupService.getByIdentity(identity));
        invite.setSentToUser(userService.getByEmail(invitedUser.getEmail()));
        invite.setSentByUser(getCurrentUser());
        try {
            inviteService.saveInviteForUser(invite);
        } catch (UserAlreadyInvitedException userAlreadyInvitedException) {
            // TODO: make response object and custom exceptions
            // send message that user already invited
            return new ResponseEntity(HttpStatus.I_AM_A_TEAPOT);
        }
        return new ResponseEntity(HttpStatus.OK);
    }
//sdf
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
        } catch (UserAlreadyInGroupException userAlreadyInGroupException) {
            logger.error(userAlreadyInGroupException.getMessage(), userAlreadyInGroupException);
            //TODO: message about trying to add dublicate user
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } catch (NoInviteForUserToGroupException noInviteForUserToGroupException) {
            //TODO: message about adding user without invite
            logger.error(noInviteForUserToGroupException.getMessage(), noInviteForUserToGroupException);
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/{identity}/leave", method = RequestMethod.DELETE)
    public ResponseEntity leaveGroup(@PathVariable("identity") String identity) {
        User currentUser = getCurrentUser();
        try {
            userService.leaveGroup(groupService.getByIdentity(identity), currentUser);
        } catch (NoUserInGroupException noUserInGroupException) {
            //TODO: message about user is not in the group
            logger.error(noUserInGroupException.getMessage(), noUserInGroupException);
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
        location.setUser(getCurrentUser());
        userService.saveUserLocation(location);
        return new ResponseEntity(HttpStatus.OK);
    }

    /*
     * Return locations of all members without location of current user
     */
    @RequestMapping(value = "/{identity}/getlocations", method = RequestMethod.GET)
    public List<Location> getLocationOfGroupMembers(@PathVariable("identity") String identity) {
        Group targetGroup = groupService.getByIdentity(identity);
        if (userService.checkUserInGroup(targetGroup, getCurrentUser())) {
            return locationService.getLastLocationsForGroupMembers(targetGroup, getCurrentUser());
        } else {
            return new ArrayList<>();
        }
    }

    //TODO: make test for this
    @RequestMapping(value = "/getforcurrentuser", method = RequestMethod.GET)
    public Set<Group> getGroups() {
        User currentUser = getCurrentUser();
        return userService.getGroupsForUser(currentUser);
    }
}
