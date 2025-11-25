package com.algonquincollege.cst8277.rest.resource;

import java.util.List;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.inject.Inject;
import jakarta.security.enterprise.SecurityContext;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import org.glassfish.soteria.WrappingCallerPrincipal;

import com.algonquincollege.cst8277.ejb.ACMECollegeService;
import com.algonquincollege.cst8277.entity.SecurityUser;
import com.algonquincollege.cst8277.entity.Student;
import com.algonquincollege.cst8277.utility.MyConstants; // Imported for prefix

@Path(MyConstants.STUDENT_RESOURCE_NAME)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class StudentResource {

    @EJB
    protected ACMECollegeService service;

    @Inject
    protected SecurityContext sc;

    @GET
    @RolesAllowed({MyConstants.ADMIN_ROLE})
    public Response getStudents() {
        List<Student> students = service.getAllStudents();
        Response response = Response.ok(students).build();
        return response;
    }

    @GET
    @RolesAllowed({MyConstants.ADMIN_ROLE, MyConstants.USER_ROLE})
    @Path(MyConstants.RESOURCE_PATH_ID_PATH)
    public Response getStudentById(@PathParam(MyConstants.RESOURCE_PATH_ID_ELEMENT) int id) {
        Response response = null;
        Student student = null;

        if (sc.isCallerInRole(MyConstants.ADMIN_ROLE)) {
        	student = service.getStudentById(id);
            response = Response.status(student == null ? Status.NOT_FOUND : Status.OK).entity(student).build();
        } else if (sc.isCallerInRole(MyConstants.USER_ROLE)) {
            WrappingCallerPrincipal wCallerPrincipal = (WrappingCallerPrincipal) sc.getCallerPrincipal();
            SecurityUser sUser = (SecurityUser) wCallerPrincipal.getWrapped();
            student = sUser.getStudent();
            if (student != null && student.getId() == id) {
                response = Response.status(Status.OK).entity(student).build();
            } else {
                throw new ForbiddenException("User trying to access resource it does not own (wrong userid)");
            }
        } else {
            response = Response.status(Status.BAD_REQUEST).build();
        }
        return response;
    }

    @POST
    @RolesAllowed({MyConstants.ADMIN_ROLE})
    public Response addStudent(Student newStudent) {
        Response response = null;
        Student newStudentWithIdTimestamps = service.persistStudent(newStudent);
        service.buildUserForNewStudent(newStudentWithIdTimestamps);
        response = Response.ok(newStudentWithIdTimestamps).build();
        return response;
    }

    @PUT
    @RolesAllowed({MyConstants.ADMIN_ROLE})
    @Path(MyConstants.RESOURCE_PATH_ID_PATH)
    public Response updateStudentById(@PathParam(MyConstants.RESOURCE_PATH_ID_ELEMENT) int id, Student studentWithUpdates) {
    	Response response = null;
    	Student updatedStudent = service.updateStudentById(id, studentWithUpdates);
    	response = Response.ok(updatedStudent).build();
    	return response;
    }
    
    @DELETE
    @RolesAllowed({MyConstants.ADMIN_ROLE})
    @Path(MyConstants.RESOURCE_PATH_ID_PATH)
    public Response deleteStudentById(@PathParam(MyConstants.RESOURCE_PATH_ID_ELEMENT) int id) {
    	Response response = null;
    	Student studentDeleted = service.deleteStudentById(id);
    	response = Response.ok(studentDeleted).build();
      	return response;
    }

    @GET
    @RolesAllowed({MyConstants.ADMIN_ROLE, MyConstants.USER_ROLE})
    @Path(MyConstants.PROGRAM_RESOURCE_PATH)
    public Response getPrograms() {
    	Response response = null;
    	List<String> programs = service.getAllPrograms();
    	response = Response.ok(programs).build();
    	return response;
    }
}