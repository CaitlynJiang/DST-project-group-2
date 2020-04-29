package DST2.Group2.bean;

public class VepBean {

    private String location;
    private String allele;
    private String feature;
    private String feature_type;
    private String consequence;
    private String extra;

    public VepBean(String location, String allele, String feature, String feature_type, String consequence, String extra) {
        this.location = location;
        this.allele = allele;
        this.feature = feature;
        this.feature_type = feature_type;
        this.consequence = consequence;
        this.extra = extra;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAllele() {
        return allele;
    }

    public void setAllele(String allele) {
        this.allele = allele;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getFeature_type() {
        return feature_type;
    }

    public void setFeature_type(String feature_type) {
        this.feature_type = feature_type;
    }

    public String getConsequence() {
        return consequence;
    }

    public void setConsequence(String consequence) {
        this.consequence = consequence;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
}
