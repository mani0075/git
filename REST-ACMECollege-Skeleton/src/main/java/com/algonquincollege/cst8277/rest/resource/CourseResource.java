package com.algonquincollege.cst8277.rest.resource;

import java.util.List;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.inject.Inject;
import jakarta.security.enterprise.SecurityContext;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import com.algonquincollege.cst8277.ejb.ACMECollegeService;
import com.algonquincollege.cst8277.entity.Course;
import com.algonquincollege.cst8277.utility.MyConstants; // Import class to use prefix

@Path(MyConstants.COURSE_RESOURCE_NAME) // Fully Qualified
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CourseResource {
    
    @EJB
    protected ACMECollegeService service;
    @Inject
    protected SecurityContext sc;

    @GET
    @RolesAllowed({MyConstants.ADMIN_ROLE}) // Fully Qualified
    public Response getCourses() {
        List<Course> courses = service.getAllCourses();
        return Response.ok(courses).build();
    }

    @GET
    @RolesAllowed({MyConstants.ADMIN_ROLE})
    @Path(MyConstants.RESOURCE_PATH_ID_PATH) // Fully Qualified
    public Response getCourseById(@PathParam(MyConstants.RESOURCE_PATH_ID_ELEMENT) int id) {
        Course course = service.getCourseById(id);
        if (course == null) return Response.status(Status.NOT_FOUND).build();
        return Response.ok(course).build();
    }

    @POST
    @RolesAllowed({MyConstants.ADMIN_ROLE})
    public Response createCourse(Course newCourse) {
        Course course = service.persistCourse(newCourse);
        return Response.ok(course).build();
    }

    @PUT
    @RolesAllowed({MyConstants.ADMIN_ROLE})
    @Path(MyConstants.RESOURCE_PATH_ID_PATH)
    public Response updateCourse(@PathParam(MyConstants.RESOURCE_PATH_ID_ELEMENT) int id, Course courseWithUpdates) {
        Course updatedCourse = service.updateCourse(id, courseWithUpdates);
        if (updatedCourse == null) return Response.status(Status.NOT_FOUND).build();
        return Response.ok(updatedCourse).build();
    }

    @DELETE
    @RolesAllowed({MyConstants.ADMIN_ROLE})
    @Path(MyConstants.RESOURCE_PATH_ID_PATH)
    public Response deleteCourse(@PathParam(MyConstants.RESOURCE_PATH_ID_ELEMENT) int id) {
        service.deleteCourseById(id);
        return Response.ok().build();
    }
}