package bean;

public class ClinicAnnBean {
    private String id;
    private String location;
    private String gene;
    private String evidencelevel;
    private String clinical_annotation_types;
    private String annotation_text;
    private String related_chemicals;
    private String related_diseases;
    private String biogeographical_groups;
    private String chromosome;

    public ClinicAnnBean(String id, String location, String gene, String evidencelevel, String clinical_annotation_types, String annotation_text, String related_chemicals, String related_diseases, String biogeographical_groups, String chromosome) {
        this.id = id;
        this.location = location;
        this.gene = gene;
        this.evidencelevel = evidencelevel;
        this.clinical_annotation_types = clinical_annotation_types;
        this.annotation_text = annotation_text;
        this.related_chemicals = related_chemicals;
        this.related_diseases = related_diseases;
        this.biogeographical_groups = biogeographical_groups;
        this.chromosome = chromosome;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getGene() {
        return gene;
    }

    public void setGene(String gene) {
        this.gene = gene;
    }

    public String getEvidencelevel() {
        return evidencelevel;
    }

    public void setEvidencelevel(String evidencelevel) {
        this.evidencelevel = evidencelevel;
    }

    public String getClinical_annotation_types() {
        return clinical_annotation_types;
    }

    public void setClinical_annotation_types(String clinical_annotation_types) {
        this.clinical_annotation_types = clinical_annotation_types;
    }

    public String getAnnotation_text() {
        return annotation_text;
    }

    public void setAnnotation_text(String annotation_text) {
        this.annotation_text = annotation_text;
    }

    public String getRelated_chemicals() {
        return related_chemicals;
    }

    public void setRelated_chemicals(String related_chemicals) {
        this.related_chemicals = related_chemicals;
    }

    public String getRelated_diseases() {
        return related_diseases;
    }

    public void setRelated_diseases(String related_diseases) {
        this.related_diseases = related_diseases;
    }

    public String getBiogeographical_groups() {
        return biogeographical_groups;
    }

    public void setBiogeographical_groups(String biogeographical_groups) {
        this.biogeographical_groups = biogeographical_groups;
    }

    public String getChromosome() {
        return chromosome;
    }

    public void setChromosome(String chromosome) {
        this.chromosome = chromosome;
    }

    @Override
    public boolean equals(Object obj) {
        try {
            ClinicAnnBean external = (ClinicAnnBean) obj;
            return this.id.equals(external.getId());
        } catch (Exception e) {
            return false;
        }
    }
}
