package be.kdg.twitterandroid.domain;

/**
 * Created by Maarten on 5/01/2016.
 */
public class Entities {
    private MediaEntity[] media;
    private UrlEntity[] urls;
    private UserMentionsEntity[] user_mentions;
    private HashTagEntity[] hashtags;
    private SymbolEntity[] symbols;
    private ExtendedEntity[] extended_entities;

    public MediaEntity[] getMedia() {
        return media;
    }

    public void setMedia(MediaEntity[] media) {
        this.media = media;
    }

    public UrlEntity[] getUrls() {
        return urls;
    }

    public void setUrls(UrlEntity[] urls) {
        this.urls = urls;
    }

    public UserMentionsEntity[] getUser_mentions() {
        return user_mentions;
    }

    public void setUser_mentions(UserMentionsEntity[] user_mentions) {
        this.user_mentions = user_mentions;
    }

    public HashTagEntity[] getHashtags() {
        return hashtags;
    }

    public void setHashtags(HashTagEntity[] hashtags) {
        this.hashtags = hashtags;
    }

    public SymbolEntity[] getSymbols() {
        return symbols;
    }

    public void setSymbols(SymbolEntity[] symbols) {
        this.symbols = symbols;
    }

    public ExtendedEntity[] getExtended_entities() {
        return extended_entities;
    }

    public void setExtended_entities(ExtendedEntity[] extended_entities) {
        this.extended_entities = extended_entities;
    }

    public class MediaEntity {
        private long id;

        private String id_str,
                media_url,
                media_url_https,
                url,
                display_url,
                expanded_url,
                type;

        private int[] indices;

        public long getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getId_str() {
            return id_str;
        }

        public void setId_str(String id_str) {
            this.id_str = id_str;
        }

        public String getMedia_url() {
            return media_url;
        }

        public void setMedia_url(String media_url) {
            this.media_url = media_url;
        }

        public String getMedia_url_https() {
            return media_url_https;
        }

        public void setMedia_url_https(String media_url_https) {
            this.media_url_https = media_url_https;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getDisplay_url() {
            return display_url;
        }

        public void setDisplay_url(String display_url) {
            this.display_url = display_url;
        }

        public String getExpanded_url() {
            return expanded_url;
        }

        public void setExpanded_url(String expanded_url) {
            this.expanded_url = expanded_url;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int[] getIndices() {
            return indices;
        }

        public void setIndices(int[] indices) {
            this.indices = indices;
        }
    }

    public class UrlEntity {
        private String url,
                display_url,
                expanded_url;

        private int[] indices;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getDisplay_url() {
            return display_url;
        }

        public void setDisplay_url(String display_url) {
            this.display_url = display_url;
        }

        public String getExpanded_url() {
            return expanded_url;
        }

        public void setExpanded_url(String expanded_url) {
            this.expanded_url = expanded_url;
        }

        public int[] getIndices() {
            return indices;
        }

        public void setIndices(int[] indices) {
            this.indices = indices;
        }
    }

    public class UserMentionsEntity {
        private long id;

        private String id_str,
                screen_name,
                name;

        private int[] indices;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getId_str() {
            return id_str;
        }

        public void setId_str(String id_str) {
            this.id_str = id_str;
        }

        public String getScreen_name() {
            return screen_name;
        }

        public void setScreen_name(String screen_name) {
            this.screen_name = screen_name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int[] getIndices() {
            return indices;
        }

        public void setIndices(int[] indices) {
            this.indices = indices;
        }
    }

    public class HashTagEntity {
        private String text;

        private int[] indices;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public int[] getIndices() {
            return indices;
        }

        public void setIndices(int[] indices) {
            this.indices = indices;
        }
    }

    public class SymbolEntity {
        private String text;

        private int[] indices;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public int[] getIndices() {
            return indices;
        }

        public void setIndices(int[] indices) {
            this.indices = indices;
        }
    }

    public class ExtendedEntity {
        private long id;

        private int duration_millis;

        private String id_str,
                media_url,
                media_url_https,
                url,
                display_url,
                expanded_url,
                type;

        private int[] indices;
        private int[] video_info;

        public long getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getDuration_millis() {
            return duration_millis;
        }

        public void setDuration_millis(int duration_millis) {
            this.duration_millis = duration_millis;
        }

        public String getId_str() {
            return id_str;
        }

        public void setId_str(String id_str) {
            this.id_str = id_str;
        }

        public String getMedia_url() {
            return media_url;
        }

        public void setMedia_url(String media_url) {
            this.media_url = media_url;
        }

        public String getMedia_url_https() {
            return media_url_https;
        }

        public void setMedia_url_https(String media_url_https) {
            this.media_url_https = media_url_https;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getDisplay_url() {
            return display_url;
        }

        public void setDisplay_url(String display_url) {
            this.display_url = display_url;
        }

        public String getExpanded_url() {
            return expanded_url;
        }

        public void setExpanded_url(String expanded_url) {
            this.expanded_url = expanded_url;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int[] getIndices() {
            return indices;
        }

        public void setIndices(int[] indices) {
            this.indices = indices;
        }

        public int[] getVideo_info() {
            return video_info;
        }

        public void setVideo_info(int[] video_info) {
            this.video_info = video_info;
        }
    }
}