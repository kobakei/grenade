package io.github.kobakei.grenadesample.entity;

import org.parceler.Parcel;

/**
 * Created by keisuke on 16/04/11.
 */
@Parcel
public class User {
    public String firstName;
    public String lastName;
    public int age;

    public User() {

    }

    public User(String firstName, String lastName, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }
}
