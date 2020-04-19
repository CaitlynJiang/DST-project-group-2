package DST2.Group2.bean;

public class DrugLabel {
	private String variantGene;
	private String drugName;
	private String source;
	private boolean hasAlternativeDrug;
	private String summary_markdown;

	
	public DrugLabel(String variantGene, String drugName, String source, boolean hasAlternativeDrug,
			String summary_markdown) {
		
		this.variantGene = variantGene;
		this.drugName = drugName;
		this.source = source;
		this.hasAlternativeDrug = hasAlternativeDrug;
		this.summary_markdown = summary_markdown;
	}
	public boolean gethasAlternativeDrug() {
		return hasAlternativeDrug;
	}
	public void sethasAlternativeDrug(boolean hasAlternativeDrug) {
		this.hasAlternativeDrug = hasAlternativeDrug;
	}
	public String getvariantGene() {
		return variantGene;
	}
	public void setvariantGene(String variantGene) {
		this.variantGene = variantGene;
	}
	public String getDrugName() {
		return drugName;
	}
	public void setDrugName(String drugName) {
		this.drugName = drugName;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getSummary_markdown() {
		return summary_markdown;
	}
	public void setSummary_markdown(String summary_markdown) {
		this.summary_markdown = summary_markdown;
	}
	
	
	
} 
