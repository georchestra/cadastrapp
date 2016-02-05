package org.georchestra.cadastrapp.service.constants;

public final class CadastrappConstants {

	//Relevé proprietaire partie batie
	
	public static final String PB_PREFIX_SECTION			= "ccopre";
	public static final String PB_LETTRE_SECTION			= "ccosec";
	public static final String PB_NUM_PLAN					= "dnupla";
	public static final String PB_ADRESSE 					= "voie";
	public static final String PB_CODE_RIVOLI_VOIE			= "ccoriv";
	
	public static final String PB_LETTRE_BAT				= "dnubat";
	public static final String PB_NUM_ENTREE				= "descr";
	public static final String PB_NIV_ETAGE					= "dniv";
	public static final String PB_NUM_PORTE_LOCAL			= "dpor";
	public static final String PB_NUM_INVARIANT				= "invar";
	
	public static final String PB_CODE_EVAL					= "ccoeva";
	public static final String PB_AFFECTATION_PEV			= "ccoaff";
	public static final String PB_NATURE_LOCAL				= "cconlc";
	public static final String PB_CATEGORIE					= "dcapec";
	public static final String PB_VAL_LOCAT_TOTAL			= "dvltrt";
	public static final String PB_CODE_COLL_EXO				= "ccolloc";
	
	public static final String PB_NATURE_EXO				= "gnextl";
	public static final String PB_ANNEE_RETOUR_IMPOSITION	= "janimp";
	public static final String PB_ANNEE_DEB_EXO				= "jandeb";
	public static final String PB_FRACTION_EXO				= "fcexb";
	public static final String PB_TAUX_EXO_ACCORDEE			= "pexb";
	public static final String PB_MONTANT_TIEOM				= "mvltieomx";
	
	//Relevé proprietaire partie non batie
	
	public static final String PNB_PREFIX_SECTION			= "ccopre";
	public static final String PNB_LETTRE_SECTION			= "ccosec";
	public static final String PNB_NUM_PLAN					= "dnupla";
	public static final String PNB_ADRESSE 					= "voie";
	public static final String PNB_CODE_RIVOLI_VOIE			= "ccoriv";
	
	public static final String PNB_NUM_PARC_PRIM			= "dparpi";
	public static final String PNB_SERIE_TARIF				= "ccostn";
	public static final String PNB_LETTRE_SUF				= "ccosub";
	public static final String PNB_GROUP_NATURE_CULTURE		= "cgrnum";
	public static final String PNB_SOUS_GROUP				= "dsgrpf";
	public static final String PNB_REF_LOT					= "dnulot";
	public static final String PNB_CLASSE					= "dclssf";
	public static final String PNB_CODE_NAT_CULT			= "cnatsp";
	public static final String PNB_CONTENANCE_CA			= "dcntsf";
	public static final String PNB_REVENU_CADASTRAL			= "drcsuba";
	public static final String PNB_CODE_COLL_EXO			= "ccolloc";
	public static final String PNB_NATURE_EXO				= "gnextl";
	public static final String PNB_ANNEE_RETOUR_IMPOSITION	= "jfinex";
	public static final String PNB_FRACTION_RC_EXO			= "fcexn";
	public static final String PNB_POURCENTAGE_EXO			= "pexn";
	
	
	//code collectivité accordant l'exonération
	public static final String CODE_COLL_EXO_TC			= "TC";
	public static final String CODE_COLL_EXO_R			= "R";
	public static final String CODE_COLL_EXO_GC			= "GC";
	public static final String CODE_COLL_EXO_D			= "D";
	public static final String CODE_COLL_EXO_C			= "C";
	public static final String CODE_COLL_EXO_A			= "A";
	
	
	//code pour type de demandeur
	public static final int CODE_DEMANDEUR_COMPTE_COMMUNAL		= 0;
	public static final int CODE_DEMANDEUR_PARCELLE_ID			= 1;
	public static final int CODE_DEMANDEUR_COPROPRIETE			= 2;
	public static final int CODE_DEMANDEUR_PARCELLE				= 3;
	public static final int CODE_DEMANDEUR_PROPRIETAIRE			= 4;
	public static final int CODE_DEMANDEUR_LOT_COPROPRIETE		= 5;
	
	public static final String CODE_DEMANDEUR_TIER		= "P3";
	

}
