package com.algonquincollege.cst8277.utility;

import static jakarta.faces.application.ProjectStage.Development;

public interface MyConstants {
    
    public static final String ADMIN_ROLE = "ADMIN_ROLE";
    public static final String USER_ROLE = "USER_ROLE";
    public static final String SLASH = "/";
    public static final String DEVELOPMENT_STAGE = Development.name();

    public static final String APPLICATION_API_VERSION = "/api/v1";
    public static final String REST_APPLICATION_PATH = SLASH + "api" + SLASH + "v1";

    public static final String APPLICATION_CONTEXT_ROOT = "/REST-ACMECollege-Skeleton";
    public static final String RESOURCE_PATH_ID_ELEMENT =  "id";
    public static final String RESOURCE_PATH_ID_PATH =  "/{" + RESOURCE_PATH_ID_ELEMENT + "}";
    public static final String CREDENTIAL_RESOURCE_NAME = "credential";
    public static final String STUDENT_RESOURCE_NAME =  "student";
    public static final String PROGRAM_RESOURCE_PATH = SLASH + "program";
    
    public static final String COURSE_RESOURCE_NAME = "course";
    public static final String PROFESSOR_RESOURCE_NAME = "professor";
    public static final String COURSE_REGISTRATION_RESOURCE_NAME = "courseregistration";
    public static final String STUDENT_CLUB_RESOURCE_NAME = "studentclub";

    public static final String ACCESS_REQUIRES_AUTHENTICATION = "Access requires authentication";
    public static final String ACCESS_TO_THE_SPECIFIED_RESOURCE_HAS_BEEN_FORBIDDEN = "Access to the specified resource has been forbidden";
    
    public static final String DEFAULT_ADMIN_USER_PROPNAME = "default-admin-user";
    public static final String DEFAULT_ADMIN_USER = "admin";
    public static final String DEFAULT_ADMIN_USER_PASSWORD_PROPNAME = "default-admin-user-password";
    public static final String DEFAULT_ADMIN_USER_PASSWORD = "admin";
    public static final String DEFAULT_USER = "cst8277";
    public static final String DEFAULT_USER_PASSWORD = "8277";
    public static final String DEFAULT_USER_PREFIX = "user";

    public static final String PROPERTY_ALGORITHM  = "Pbkdf2PasswordHash.Algorithm";
    public static final String DEFAULT_PROPERTY_ALGORITHM  = "PBKDF2WithHmacSHA256";
    public static final String PROPERTY_ITERATIONS = "Pbkdf2PasswordHash.Iterations";
    public static final String DEFAULT_PROPERTY_ITERATIONS = "2048";
    public static final String PROPERTY_SALT_SIZE = "Pbkdf2PasswordHash.SaltSizeBytes";
    public static final String DEFAULT_SALT_SIZE = "32";
    public static final String PROPERTY_KEY_SIZE = "Pbkdf2PasswordHash.KeySizeBytes";
    public static final String DEFAULT_KEY_SIZE = "32";

    public static final String PU_NAME = "acmecollege-PU";
    public static final String PARAM1 = "param1";
}