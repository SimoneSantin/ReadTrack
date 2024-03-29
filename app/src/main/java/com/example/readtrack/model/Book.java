package com.example.readtrack.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity
public class Book implements Parcelable {

    @Ignore
    private String kind;

    @PrimaryKey(autoGenerate = false)
    @NonNull
    private String id;
    @Ignore
    private String etag;
    @Ignore
    private String selfLink;
    @Ignore
    private VolumeInfo volumeInfo;
    @Ignore
    private SaleInfo saleInfo;
    @Ignore
    private AccessInfo accessInfo;
    @Ignore
    private SearchInfo searchInfo;

    @ColumnInfo(name = "TITLE")
    private String titleBookDB; //variabile d'appoggio per database dato che non accetta VolumeInfo
    @ColumnInfo(name = "bookMarker")
    private int bookMarker;

    @Ignore
    private boolean favorite=false;

    public Book(String id, String thumbnail, String title, int numPages, int bookMarcker){
        this.id = id;
        this.bookMarker=bookMarcker;
        this.volumeInfo = new VolumeInfo(); // Inizializza volumeInfo
        this.saleInfo = new SaleInfo();     // Inizializza saleInfo
        this.accessInfo = new AccessInfo(); // Inizializza accessInfo
        this.searchInfo = new SearchInfo(); // Inizializza searchInfo
        this.titleBookDB=title;
        this.volumeInfo.setImageLinks(new ImageLinks());
        this.volumeInfo.getImageLinks().setThumbnail(thumbnail);
        this.volumeInfo.setTitle(title);
        this.volumeInfo.setPageCount(numPages);
    }

    public Book(){

    }
    protected Book(Parcel in) {
        kind = in.readString();
        id = in.readString();
        etag = in.readString();
        selfLink = in.readString();
        favorite = in.readByte() != 0;
    }

    public boolean isFavorite(){return favorite;}

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public void setBookMarker(int bookMarker){this.bookMarker=bookMarker;}
    public int getBookMarker(){return this.bookMarker;}

    public void setTitleBookDB(String titleBookDB){this.titleBookDB=titleBookDB;}
    public String getTitleBookDB(){return this.titleBookDB;}
    public void setFavorite(Boolean favorite){
        this.favorite=favorite;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public String getSelfLink() {
        return selfLink;
    }

    public void setSelfLink(String selfLink) {
        this.selfLink = selfLink;
    }

    public VolumeInfo getVolumeInfo() {
        return volumeInfo;
    }

    public void setVolumeInfo(VolumeInfo volumeInfo) {
        this.volumeInfo = volumeInfo;
    }

    public SaleInfo getSaleInfo() {
        return saleInfo;
    }

    public void setSaleInfo(SaleInfo saleInfo) {
        this.saleInfo = saleInfo;
    }

    public AccessInfo getAccessInfo() {
        return accessInfo;
    }

    public void setAccessInfo(AccessInfo accessInfo) {
        this.accessInfo = accessInfo;
    }

    public SearchInfo getSearchInfo() {
        return searchInfo;
    }

    public void setSearchInfo(SearchInfo searchInfo) {
        this.searchInfo = searchInfo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(kind);
        dest.writeString(id);
        dest.writeString(etag);
        dest.writeString(selfLink);
        dest.writeByte((byte) (favorite ? 1 : 0));
    }


    public static class VolumeInfo {
        private String title;
        private List<String> authors;
        private String publisher;
        private String publishedDate;
        private String description;
        private List<IndustryIdentifier> industryIdentifiers;
        private ReadingModes readingModes;
        private int pageCount;
        private String printType;
        private List<String> categories;
        private String maturityRating;
        private boolean allowAnonLogging;
        private String contentVersion;
        private PanelizationSummary panelizationSummary;
        private ImageLinks imageLinks;
        private String language;
        private String previewLink;
        private String infoLink;
        private String canonicalVolumeLink;

        // Metodi getter e setter per VolumeInfo
        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<String> getAuthors() {
            return authors;
        }

        public void setAuthors(List<String> authors) {
            this.authors = authors;
        }

        public String getPublisher() {
            return publisher;
        }

        public void setPublisher(String publisher) {
            this.publisher = publisher;
        }

        public String getPublishedDate() {
            return publishedDate;
        }

        public void setPublishedDate(String publishedDate) {
            this.publishedDate = publishedDate;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public List<IndustryIdentifier> getIndustryIdentifiers() {
            return industryIdentifiers;
        }

        public void setIndustryIdentifiers(List<IndustryIdentifier> industryIdentifiers) {
            this.industryIdentifiers = industryIdentifiers;
        }

        public ReadingModes getReadingModes() {
            return readingModes;
        }

        public void setReadingModes(ReadingModes readingModes) {
            this.readingModes = readingModes;
        }

        public int getPageCount() {
            return pageCount;
        }

        public void setPageCount(int pageCount) {
            this.pageCount = pageCount;
        }

        public String getPrintType() {
            return printType;
        }

        public void setPrintType(String printType) {
            this.printType = printType;
        }

        public List<String> getCategories() {
            return categories;
        }

        public void setCategories(List<String> categories) {
            this.categories = categories;
        }

        public String getMaturityRating() {
            return maturityRating;
        }

        public void setMaturityRating(String maturityRating) {
            this.maturityRating = maturityRating;
        }

        public boolean isAllowAnonLogging() {
            return allowAnonLogging;
        }

        public void setAllowAnonLogging(boolean allowAnonLogging) {
            this.allowAnonLogging = allowAnonLogging;
        }

        public String getContentVersion() {
            return contentVersion;
        }

        public void setContentVersion(String contentVersion) {
            this.contentVersion = contentVersion;
        }

        public PanelizationSummary getPanelizationSummary() {
            return panelizationSummary;
        }

        public void setPanelizationSummary(PanelizationSummary panelizationSummary) {
            this.panelizationSummary = panelizationSummary;
        }

        public ImageLinks getImageLinks() {
            return imageLinks;
        }

        public void setImageLinks(ImageLinks imageLinks) {
            this.imageLinks = imageLinks;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getPreviewLink() {
            return previewLink;
        }

        public void setPreviewLink(String previewLink) {
            this.previewLink = previewLink;
        }

        public String getInfoLink() {
            return infoLink;
        }

        public void setInfoLink(String infoLink) {
            this.infoLink = infoLink;
        }

        public String getCanonicalVolumeLink() {
            return canonicalVolumeLink;
        }

        public void setCanonicalVolumeLink(String canonicalVolumeLink) {
            this.canonicalVolumeLink = canonicalVolumeLink;
        }
    }

    public static class IndustryIdentifier {
        private String type;
        private String identifier;

        // Metodi getter e setter per IndustryIdentifier
        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getIdentifier() {
            return identifier;
        }

        public void setIdentifier(String identifier) {
            this.identifier = identifier;
        }
    }

    public static class ReadingModes {
        private boolean text;
        private boolean image;

        // Metodi getter e setter per ReadingModes
        public boolean isText() {
            return text;
        }

        public void setText(boolean text) {
            this.text = text;
        }

        public boolean isImage() {
            return image;
        }

        public void setImage(boolean image) {
            this.image = image;
        }
    }

    public static class PanelizationSummary {
        private boolean containsEpubBubbles;
        private boolean containsImageBubbles;

        // Metodi getter e setter per PanelizationSummary
        public boolean isContainsEpubBubbles() {
            return containsEpubBubbles;
        }

        public void setContainsEpubBubbles(boolean containsEpubBubbles) {
            this.containsEpubBubbles = containsEpubBubbles;
        }

        public boolean isContainsImageBubbles() {
            return containsImageBubbles;
        }

        public void setContainsImageBubbles(boolean containsImageBubbles) {
            this.containsImageBubbles = containsImageBubbles;
        }
    }

    public static class ImageLinks {
        private String smallThumbnail;
        private String thumbnail;

        // Metodi getter e setter per ImageLinks
        public String getSmallThumbnail() {
            return smallThumbnail;
        }

        public void setSmallThumbnail(String smallThumbnail) {
            this.smallThumbnail = smallThumbnail;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }
    }

    public static class SaleInfo {
        private String country;
        private String saleability;
        private boolean isEbook;

        // Metodi getter e setter per SaleInfo
        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getSaleability() {
            return saleability;
        }

        public void setSaleability(String saleability) {
            this.saleability = saleability;
        }

        public boolean isEbook() {
            return isEbook;
        }

        public void setEbook(boolean ebook) {
            isEbook = ebook;
        }
    }

    public static class AccessInfo {
        private String country;
        private String viewability;
        private boolean embeddable;
        private boolean publicDomain;
        private String textToSpeechPermission;
        private Epub epub;
        private Pdf pdf;
        private String webReaderLink;
        private String accessViewStatus;
        private boolean quoteSharingAllowed;

        // Metodi getter e setter per AccessInfo
        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getViewability() {
            return viewability;
        }

        public void setViewability(String viewability) {
            this.viewability = viewability;
        }

        public boolean isEmbeddable() {
            return embeddable;
        }

        public void setEmbeddable(boolean embeddable) {
            this.embeddable = embeddable;
        }

        public boolean isPublicDomain() {
            return publicDomain;
        }

        public void setPublicDomain(boolean publicDomain) {
            this.publicDomain = publicDomain;
        }

        public String getTextToSpeechPermission() {
            return textToSpeechPermission;
        }

        public void setTextToSpeechPermission(String textToSpeechPermission) {
            this.textToSpeechPermission = textToSpeechPermission;
        }

        public Epub getEpub() {
            return epub;
        }

        public void setEpub(Epub epub) {
            this.epub = epub;
        }

        public Pdf getPdf() {
            return pdf;
        }

        public void setPdf(Pdf pdf) {
            this.pdf = pdf;
        }

        public String getWebReaderLink() {
            return webReaderLink;
        }

        public void setWebReaderLink(String webReaderLink) {
            this.webReaderLink = webReaderLink;
        }

        public String getAccessViewStatus() {
            return accessViewStatus;
        }

        public void setAccessViewStatus(String accessViewStatus) {
            this.accessViewStatus = accessViewStatus;
        }

        public boolean isQuoteSharingAllowed() {
            return quoteSharingAllowed;
        }

        public void setQuoteSharingAllowed(boolean quoteSharingAllowed) {
            this.quoteSharingAllowed = quoteSharingAllowed;
        }

        public static class Epub {
            private boolean isAvailable;
            private String acsTokenLink;

            // Metodi getter e setter per Epub
            public boolean isAvailable() {
                return isAvailable;
            }

            public void setAvailable(boolean available) {
                isAvailable = available;
            }

            public String getAcsTokenLink() {
                return acsTokenLink;
            }

            public void setAcsTokenLink(String acsTokenLink) {
                this.acsTokenLink = acsTokenLink;
            }
        }

        public static class Pdf {
            private boolean isAvailable;
            private String acsTokenLink;

            // Metodi getter e setter per Pdf
            public boolean isAvailable() {
                return isAvailable;
            }

            public void setAvailable(boolean available) {
                isAvailable = available;
            }

            public String getAcsTokenLink() {
                return acsTokenLink;
            }

            public void setAcsTokenLink(String acsTokenLink) {
                this.acsTokenLink = acsTokenLink;
            }
        }
    }

    public static class SearchInfo {
        private String textSnippet;

        // Metodi getter e setter per SearchInfo
        public String getTextSnippet() {
            return textSnippet;
        }

        public void setTextSnippet(String textSnippet) {
            this.textSnippet = textSnippet;
        }
    }
}
