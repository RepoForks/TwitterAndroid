package be.kdg.twitterandroid.domain;

/**
 * Created by Maarten on 5/01/2016.
 */
public class User {
    private long id;

    private boolean contributors_enabled,
            default_profile,
            default_profile_image,
            follow_request_sent,
            following,
            geo_enabled,
            is_translator,
            notifications,
            profile_background_tile,
            profile_use_background_image,
            _protected, // "protected" is a forbidden variable name as it is a keyword
            show_all_inline_media,
            verified;

    private String id_str,
            created_at,
            description,
            lang,
            location,
            name,
            profile_background_color,
            profile_background_image_url,
            profile_background_image_url_https,
            profile_banner_url,
            profile_image_url,
            profile_image_url_https,
            profile_link_color,
            profile_sidebar_border_color,
            profile_sidebar_fill_color,
            profile_text_color,
            screen_name,
            time_zone,
            url,
            withheld_in_countries,
            withheld_scope;

    private int favourites_count,
            followers_count,
            friends_count,
            listed_count,
            statuses_count,
            utc_offset;

    private Entities entities;

    private Tweet status;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isContributors_enabled() {
        return contributors_enabled;
    }

    public void setContributors_enabled(boolean contributors_enabled) {
        this.contributors_enabled = contributors_enabled;
    }

    public boolean isDefault_profile() {
        return default_profile;
    }

    public void setDefault_profile(boolean default_profile) {
        this.default_profile = default_profile;
    }

    public boolean isDefault_profile_image() {
        return default_profile_image;
    }

    public void setDefault_profile_image(boolean default_profile_image) {
        this.default_profile_image = default_profile_image;
    }

    public boolean isFollow_request_sent() {
        return follow_request_sent;
    }

    public void setFollow_request_sent(boolean follow_request_sent) {
        this.follow_request_sent = follow_request_sent;
    }

    public boolean isFollowing() {
        return following;
    }

    public void setFollowing(boolean following) {
        this.following = following;
    }

    public boolean isGeo_enabled() {
        return geo_enabled;
    }

    public void setGeo_enabled(boolean geo_enabled) {
        this.geo_enabled = geo_enabled;
    }

    public boolean is_translator() {
        return is_translator;
    }

    public void setIs_translator(boolean is_translator) {
        this.is_translator = is_translator;
    }

    public boolean isNotifications() {
        return notifications;
    }

    public void setNotifications(boolean notifications) {
        this.notifications = notifications;
    }

    public boolean isProfile_background_tile() {
        return profile_background_tile;
    }

    public void setProfile_background_tile(boolean profile_background_tile) {
        this.profile_background_tile = profile_background_tile;
    }

    public boolean isProfile_use_background_image() {
        return profile_use_background_image;
    }

    public void setProfile_use_background_image(boolean profile_use_background_image) {
        this.profile_use_background_image = profile_use_background_image;
    }

    public boolean is_protected() {
        return _protected;
    }

    public void set_protected(boolean _protected) {
        this._protected = _protected;
    }

    public boolean isShow_all_inline_media() {
        return show_all_inline_media;
    }

    public void setShow_all_inline_media(boolean show_all_inline_media) {
        this.show_all_inline_media = show_all_inline_media;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public String getId_str() {
        return id_str;
    }

    public void setId_str(String id_str) {
        this.id_str = id_str;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile_background_color() {
        return profile_background_color;
    }

    public void setProfile_background_color(String profile_background_color) {
        this.profile_background_color = profile_background_color;
    }

    public String getProfile_background_image_url() {
        return profile_background_image_url;
    }

    public void setProfile_background_image_url(String profile_background_image_url) {
        this.profile_background_image_url = profile_background_image_url;
    }

    public String getProfile_background_image_url_https() {
        return profile_background_image_url_https;
    }

    public void setProfile_background_image_url_https(String profile_background_image_url_https) {
        this.profile_background_image_url_https = profile_background_image_url_https;
    }

    public String getProfile_banner_url() {
        return profile_banner_url;
    }

    public void setProfile_banner_url(String profile_banner_url) {
        this.profile_banner_url = profile_banner_url;
    }

    public String getProfile_image_url() {
        return profile_image_url.replace("_normal", "");
    }

    public void setProfile_image_url(String profile_image_url) {
        this.profile_image_url = profile_image_url;
    }

    public String getProfile_image_url_https() {
        return profile_image_url_https.replace("_normal", "");
    }

    public void setProfile_image_url_https(String profile_image_url_https) {
        this.profile_image_url_https = profile_image_url_https;
    }

    public String getProfile_link_color() {
        return profile_link_color;
    }

    public void setProfile_link_color(String profile_link_color) {
        this.profile_link_color = profile_link_color;
    }

    public String getProfile_sidebar_border_color() {
        return profile_sidebar_border_color;
    }

    public void setProfile_sidebar_border_color(String profile_sidebar_border_color) {
        this.profile_sidebar_border_color = profile_sidebar_border_color;
    }

    public String getProfile_sidebar_fill_color() {
        return profile_sidebar_fill_color;
    }

    public void setProfile_sidebar_fill_color(String profile_sidebar_fill_color) {
        this.profile_sidebar_fill_color = profile_sidebar_fill_color;
    }

    public String getProfile_text_color() {
        return profile_text_color;
    }

    public void setProfile_text_color(String profile_text_color) {
        this.profile_text_color = profile_text_color;
    }

    public String getScreen_name() {
        return screen_name;
    }

    public void setScreen_name(String screen_name) {
        this.screen_name = screen_name;
    }

    public String getTime_zone() {
        return time_zone;
    }

    public void setTime_zone(String time_zone) {
        this.time_zone = time_zone;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWithheld_in_countries() {
        return withheld_in_countries;
    }

    public void setWithheld_in_countries(String withheld_in_countries) {
        this.withheld_in_countries = withheld_in_countries;
    }

    public String getWithheld_scope() {
        return withheld_scope;
    }

    public void setWithheld_scope(String withheld_scope) {
        this.withheld_scope = withheld_scope;
    }

    public int getFavourites_count() {
        return favourites_count;
    }

    public void setFavourites_count(int favourites_count) {
        this.favourites_count = favourites_count;
    }

    public int getFollowers_count() {
        return followers_count;
    }

    public void setFollowers_count(int followers_count) {
        this.followers_count = followers_count;
    }

    public int getFriends_count() {
        return friends_count;
    }

    public void setFriends_count(int friends_count) {
        this.friends_count = friends_count;
    }

    public int getListed_count() {
        return listed_count;
    }

    public void setListed_count(int listed_count) {
        this.listed_count = listed_count;
    }

    public int getStatuses_count() {
        return statuses_count;
    }

    public void setStatuses_count(int statuses_count) {
        this.statuses_count = statuses_count;
    }

    public int getUtc_offset() {
        return utc_offset;
    }

    public void setUtc_offset(int utc_offset) {
        this.utc_offset = utc_offset;
    }

    public Entities getEntities() {
        return entities;
    }

    public void setEntities(Entities entities) {
        this.entities = entities;
    }

    public Tweet getStatus() {
        return status;
    }

    public void setStatus(Tweet status) {
        this.status = status;
    }
}