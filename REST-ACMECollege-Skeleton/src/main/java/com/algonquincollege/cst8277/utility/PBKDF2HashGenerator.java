package com.algonquincollege.cst8277.utility;

import java.util.HashMap;
import java.util.Map;

import org.glassfish.soteria.identitystores.hash.Pbkdf2PasswordHashImpl;

import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;

public class PBKDF2HashGenerator {

    public static void main(String[] args) {
        
        Pbkdf2PasswordHash pbAndjPasswordHash = new Pbkdf2PasswordHashImpl();

        Map<String, String> pbAndjProperties = new HashMap<>();
        // Using MyConstants prefix to resolve "cannot find symbol" errors
        pbAndjProperties.put(MyConstants.PROPERTY_ALGORITHM, MyConstants.DEFAULT_PROPERTY_ALGORITHM);
        pbAndjProperties.put(MyConstants.PROPERTY_ITERATIONS, MyConstants.DEFAULT_PROPERTY_ITERATIONS);
        pbAndjProperties.put(MyConstants.PROPERTY_SALT_SIZE, MyConstants.DEFAULT_SALT_SIZE);
        pbAndjProperties.put(MyConstants.PROPERTY_KEY_SIZE, MyConstants.DEFAULT_KEY_SIZE);
        pbAndjPasswordHash.initialize(pbAndjProperties);
        String pwHash = pbAndjPasswordHash.generate(args[0].toCharArray());
        System.out.printf("Hash for %s is %s%n", args[0], pwHash);
    }
}