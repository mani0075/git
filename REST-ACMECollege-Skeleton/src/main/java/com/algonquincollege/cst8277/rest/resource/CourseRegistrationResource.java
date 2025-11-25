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
import com.algonquincollege.cst8277.entity.CourseRegistration;
import com.algonquincollege.cst8277.entity.Professor;
import com.algonquincollege.cst8277.utility.MyConstants; // Imported for prefix

@Path(MyConstants.COURSE_REGISTRATION_RESOURCE_NAME)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CourseRegistrationResource {

    @EJB
    protected ACMECollegeService service;
    
    @Inject
    protected SecurityContext sc;

    @GET
    @RolesAllowed({MyConstants.ADMIN_ROLE})
    @Path("/grades")
    public Response getLetterGrades() {
        List<String> grades = service.getAllLetterGrades();
        return Response.ok(grades).build();
    }
    
    @GET
    @RolesAllowed({MyConstants.ADMIN_ROLE})
    public Response getCourseRegistrations() {
        List<CourseRegistration> registrations = service.getAllCourseRegistrations();
        return Response.ok(registrations).build();
    }

    @POST
    @RolesAllowed({MyConstants.ADMIN_ROLE})
    public Response addCourseRegistration(CourseRegistration newRegistration) {
        CourseRegistration reg = service.persistCourseRegistration(newRegistration);
        return Response.ok(reg).build();
    }

    @DELETE
    @RolesAllowed({MyConstants.ADMIN_ROLE})
    @Path("/student/{studentId}/course/{courseId}")
    public Response deleteCourseRegistration(
        @PathParam("studentId") int studentId, 
        @PathParam("courseId") int courseId) 
    {
        service.deleteCourseRegistrationById(studentId, courseId);
        return Response.ok().build();
    }
    
    @PUT
    @RolesAllowed({MyConstants.ADMIN_ROLE})
    @Path("/student/{studentId}/course/{courseId}/grade/{grade}")
    public Response assignGrade(
        @PathParam("studentId") int studentId, 
        @PathParam("courseId") int courseId, 
        @PathParam("grade") String grade) 
    {
        CourseRegistration updated = service.assignGradeToCourseRegistration(studentId, courseId, grade);
        if (updated == null) return Response.status(Status.NOT_FOUND).build();
        return Response.ok(updated).build();
    }
    
    @PUT
    @RolesAllowed({MyConstants.ADMIN_ROLE})
    @Path("/student/{studentId}/course/{courseId}/professor/{professorId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response assignProfessor(
        @PathParam("studentId") int studentId, 
        @PathParam("courseId") int courseId, 
        @PathParam("professorId") int professorId) 
    {
        Professor professor = service.getProfessorById(professorId); 
        
        if (professor == null) {
            return Response.status(Status.BAD_REQUEST).entity("Professor ID not found.").build();
        }
        
        CourseRegistration updated = service.assignProfessorToCourseRegistration(studentId, courseId, professor);
        
        if (updated == null) return Response.status(Status.NOT_FOUND).build();
        return Response.ok(updated).build();
    }
}