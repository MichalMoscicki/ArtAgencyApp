package com.immpresariat.ArtAgencyApp.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ContactPersonTests {

    @DisplayName("JUnit test for phone number regex (positive scenario)")
    @Test
    public void shouldReturnTrue() {
        String regex = "^[\\\\+]?[(]?[0-9]{2}[)]?-?[0-9]{3}[-\\\\s\\\\.]?[0-9]{4,6}$";
        Pattern pattern = Pattern.compile(regex);

        assertTrue(matches(pattern, "111222333"));
        assertTrue(matches(pattern, "+48111222333"));
        assertTrue(matches(pattern, "+(48)111222333"));
        assertTrue(matches(pattern, "+(48)-111222333"));

    }

    private boolean matches(Pattern pattern, String input) {
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }
}
