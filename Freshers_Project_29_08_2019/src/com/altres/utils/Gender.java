package com.altres.utils;

/**
 * Enum for gender
 *
 */
public enum Gender{

    M("Male"), 
    F("Female");

    private final String gender;
    
    private Gender(String gender){
        this.gender = gender;
    }

    public String getGender(){
        return this.gender;
    }
}
