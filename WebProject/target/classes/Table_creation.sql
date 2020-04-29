-- vep data (uploaded by user)
CREATE TABLE vep(
	sample_id INT,
	Uploaded_variation VARCHAR,
	Location VARCHAR,
	Allele VARCHAR,
	Gene VARCHAR,
	Feature VARCHAR,
	Feature_type VARCHAR,
	Consequence VARCHAR,
	cDNA_position VARCHAR,
	CDS_position VARCHAR,
	Protein_position VARCHAR,
	Amino_acids VARCHAR,
	Codons VARCHAR,
	Existing_variation VARCHAR,
	Extra TEXT
);

-- clinic metadata.tsv
CREATE TABLE clinic_meta(
	ID INT PRIMARY KEY,
	location VARCHAR,
	gene VARCHAR,
	evidenceLevel VARCHAR,
	clinical_annotation_types VARCHAR,
	genotype_phenotype_ids VARCHAR,
	annotation_text TEXT,
	variant_annotation_ids TEXT,
	variant_annotations TEXT,
	PMIDs TEXT,
	evidence_count INT,
	related_chemicals VARCHAR,
	related_diseases TEXT,
	biogeographical_groups VARCHAR,
	chromosome VARCHAR
)

-- variant.tsv
CREATE TABLE variant(
	variant_id VARCHAR,
	variant_name VARCHAR,
	gene_ids VARCHAR,
	gene_symbols VARCHAR,
	location VARCHAR,
	variant_annotation_count INT,
	clinical_annotation_count INT,
	high_level_clinical_annotation INT,
	guidance_annotation_count INT,
	label_annotation_count INT,
	synonyms TEXT
);

-- gene_treated.tsv
CREATE TABLE gene(
	pgkb_id VARCHAR,
	ncbi_id VARCHAR,
	hgnc_id VARCHAR,
	es_id_old VARCHAR,
	name VARCHAR,
	symbol VARCHAR,
	alternative_names TEXT,
	alternative_symbols TEXT,
	isVIP VARCHAR,
	hasVariantAnnotation VARCHAR,
	cross_reference TEXT,
	hasCPICguidline VARCHAR,
	chromosome VARCHAR,
	ch37_start VARCHAR,
	ch37_stop VARCHAR,
	ch38_start VARCHAR,
	ch38_stop VARCHAR,
	ensembl_id VARCHAR
);

-- drug
create table drug
(
    id varchar(100) not null,
    name varchar(500) null,
    obj_cls varchar(100) null,
    drug_url varchar(100) null,
    biomarker boolean null,
    constraint drug_id_uindex
        unique (id)
);

alter table drug
    add primary key (id);

-- drug label
create table drug_label
(
    id varchar(100) not null,
    name varchar(200) null,
    obj_cls varchar(100) null,
    alternate_drug_available boolean null,
    dosing_information boolean null,
    prescribing_markdown varchar(2000) null,
    source varchar(100) null,
    text_markdown varchar(4000) null,
    summary_markdown varchar(1000) null,
    raw text null,
    drug_id varchar(100) null,
    constraint drug_label_id_uindex
        unique (id)
);

alter table drug_label
    add primary key (id);

-- dosing guideline
create table dosing_guideline
(
    id varchar(250) not null,
    obj_cls varchar(100) null,
    name varchar(100) null,
    recommendation boolean null,
    drug_id varchar(250) null,
    source varchar(1000) null,
    summary_markdown varchar(2000) null,
    text_markdown text null,
    raw text null,
    constraint dosing_guideline_id_uindex
        unique (id)
);

alter table dosing_guideline
    add primary key (id);

-- drugNames from drug & drug label
select drug_label.id,drug_label.alternate_drug_available,
drug_label.source,drug_label.summary_markdown,
drug_label.drug_id,drug.name into drugNames 
from drug right outer join drug_label on drug.id=drug_label.drug_id;

-- dosing_guideline_name from drug & dosing_guideline
select dosing_guideline.name,drug.name as drug ,dosing_guideline.source,
dosing_guideline.recommendation,dosing_guideline.summary_markdown 
into dosing_guideline_name 
from drug right outer join dosing_guideline 
on drug.id=dosing_guideline.drug_id;

-- var-drug-ann.tsv
create table var_drug_ann(
annotation_id varchar(250),
variant varchar(250),
gene varchar(250),
chemical varchar(2500),
pmid varchar(250),
phenotype_category varchar(250),
significance varchar(250),
notes varchar -- not long enough?,
sentence varchar, -- not long enough?
studyparam varchar(250),
allels varchar(250),
choromosome varchar(250))

-- location_annvar: created from variant.tsv & variant.tsv
SELECT var_drug_ann.variant, var_drug_ann.gene,
var_drug_ann.chemical,var_drug_ann.significance,
var_drug_ann.notes,var_drug_ann.sentence,
variant.location
INTO location_annvar FROM variant right outer join var_drug_ann 
on variant.variant_name = var_drug_ann.variant;
select * from location_annvar;