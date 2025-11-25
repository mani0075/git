package com.algonquincollege.cst8277.rest.resource;

import java.util.List;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.inject.Inject;
import jakarta.security.enterprise.SecurityContext;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import com.algonquincollege.cst8277.ejb.ACMECollegeService;
import com.algonquincollege.cst8277.entity.StudentClub;
import com.algonquincollege.cst8277.utility.MyConstants; // <-- CRITICAL: Imports the necessary class

@Path(MyConstants.STUDENT_CLUB_RESOURCE_NAME)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class StudentClubResource {
    
    @EJB
    protected ACMECollegeService service;
    
    @Inject
    protected SecurityContext sc;

    @GET
    @RolesAllowed({MyConstants.ADMIN_ROLE, MyConstants.USER_ROLE}) // Fully Qualified Constants
    public Response getStudentClubs() {
        List<StudentClub> clubs = service.getAllStudentClubs();
        return Response.ok(clubs).build();
    }

    @GET
    @RolesAllowed({MyConstants.ADMIN_ROLE})
    @Path(MyConstants.RESOURCE_PATH_ID_PATH) // Fully Qualified Constants
    public Response getStudentClubById(@PathParam(MyConstants.RESOURCE_PATH_ID_ELEMENT) int id) { // Fully Qualified
        StudentClub club = service.getStudentClubById(id);
        if (club == null) return Response.status(Status.NOT_FOUND).build();
        return Response.ok(club).build();
    }

    @POST
    @RolesAllowed({MyConstants.ADMIN_ROLE})
    public Response createStudentClub(StudentClub newClub) {
        StudentClub club = service.persistStudentClub(newClub);
        return Response.ok(club).build();
    }

    @PUT
    @RolesAllowed({MyConstants.ADMIN_ROLE})
    @Path(MyConstants.RESOURCE_PATH_ID_PATH) // Fully Qualified Constants
    public Response updateStudentClub(@PathParam(MyConstants.RESOURCE_PATH_ID_ELEMENT) int id, StudentClub clubWithUpdates) { // Fully Qualified
        StudentClub updatedClub = service.updateStudentClub(id, clubWithUpdates);
        if (updatedClub == null) return Response.status(Status.NOT_FOUND).build();
        return Response.ok(updatedClub).build();
    }

    @DELETE
    @RolesAllowed({MyConstants.ADMIN_ROLE})
    @Path(MyConstants.RESOURCE_PATH_ID_PATH) // Fully Qualified Constants
    public Response deleteStudentClub(@PathParam(MyConstants.RESOURCE_PATH_ID_ELEMENT) int id) { // Fully Qualified
        service.deleteStudentClubById(id);
        return Response.ok().build();
    }
}