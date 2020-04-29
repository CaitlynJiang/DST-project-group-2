package DST2.Group2.bean;

public class GeneBean {
    private String location;
    private String allele;
    private String symbol;
    private String ensembl_id;

    public GeneBean(String location, String allele, String symbol, String ensembl_id) {
        this.location = location;
        this.allele = allele;
        this.symbol = symbol;
        this.ensembl_id = ensembl_id;
    }

    public String getLocation() {
        return location;
    }

    public String getAllele() {
        return allele;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getEnsembl_id() {
        return ensembl_id;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setAllele(String allele) {
        this.allele = allele;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setEnsembl_id(String ensembl_id) {
        this.ensembl_id = ensembl_id;
    }
}
