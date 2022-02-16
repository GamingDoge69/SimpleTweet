package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class UserData {

    public String name;
    public String screenName;
    public String profileImageUrl;

    public UserData() { /* Parsable Library Constructor */}

    public static UserData fromJSON(JSONObject jsonObject) throws JSONException {
        UserData userData = new UserData();
        userData.name = jsonObject.getString("name");
        userData.screenName = jsonObject.getString("screen_name");
        userData.profileImageUrl = jsonObject.getString("profile_image_url_https");
        return userData;
    }

    public String getName() {
        return name;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }
}


