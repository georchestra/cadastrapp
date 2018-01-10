<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:java="http://xml.apache.org/xslt/java" xmlns:date="http://exslt.org/dates-and-times"
	exclude-result-prefixes="java">

	<!-- Page layout information -->
	<xsl:template match="/">
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

			<fo:layout-master-set>
				<fo:simple-page-master master-name="portrait"
					page-height="21cm" page-width="29.7cm" font-family="sans-serif"
					margin-top="0.5cm" margin-bottom="0.5cm" margin-left="0.5cm"
					margin-right="0.5cm">
					<fo:region-body />
				</fo:simple-page-master>
			</fo:layout-master-set>

			<fo:page-sequence master-reference="portrait">
				<fo:flow flow-name="xsl-region-body">
					<xsl:apply-templates select="relevePropriete" />
				</fo:flow>
			</fo:page-sequence>

		</fo:root>
	</xsl:template>

	<!-- Definition des styles -->
	<!-- Bordure de tableau -->
	<xsl:attribute-set name="bordure-title">
		<xsl:attribute name="border-start-width">thin</xsl:attribute>
		<xsl:attribute name="border-start-style">solid</xsl:attribute>
		<xsl:attribute name="border-start-color">black</xsl:attribute>
		<xsl:attribute name="text-align">center</xsl:attribute>
		<xsl:attribute name="font-size">8pt</xsl:attribute>
		<xsl:attribute name="padding-top">2pt</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="bordure-title-end">
		<xsl:attribute name="border-start-width">thin</xsl:attribute>
		<xsl:attribute name="border-start-style">solid</xsl:attribute>
		<xsl:attribute name="border-start-color">black</xsl:attribute>
		<xsl:attribute name="border-end-width">medium</xsl:attribute>
		<xsl:attribute name="border-end-style">solid</xsl:attribute>
		<xsl:attribute name="border-end-color">black</xsl:attribute>
		<xsl:attribute name="text-align">center</xsl:attribute>
		<xsl:attribute name="font-size">8pt</xsl:attribute>
		<xsl:attribute name="padding-top">2pt</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="bordure">
		<xsl:attribute name="border">solid medium black</xsl:attribute>
		<xsl:attribute name="text-align">center</xsl:attribute>
		<xsl:attribute name="font-size">8pt</xsl:attribute>
		<xsl:attribute name="padding-top">2pt</xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:attribute-set name="bordure-values">
		<xsl:attribute name="border-start-width">thin</xsl:attribute>
		<xsl:attribute name="border-start-style">solid</xsl:attribute>
		<xsl:attribute name="border-start-color">black</xsl:attribute>
		<xsl:attribute name="border-after-width">thin</xsl:attribute>
		<xsl:attribute name="border-after-style">dotted</xsl:attribute>
		<xsl:attribute name="border-after-color">black</xsl:attribute>
		<xsl:attribute name="text-align">center</xsl:attribute>
		<xsl:attribute name="font-size">8pt</xsl:attribute>
		<xsl:attribute name="padding-top">2pt</xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:attribute-set name="bordure-values-end">
		<xsl:attribute name="border-start-width">thin</xsl:attribute>
		<xsl:attribute name="border-start-style">solid</xsl:attribute>
		<xsl:attribute name="border-start-color">black</xsl:attribute>
		<xsl:attribute name="border-after-width">thin</xsl:attribute>
		<xsl:attribute name="border-after-style">dotted</xsl:attribute>
		<xsl:attribute name="border-after-color">black</xsl:attribute>
		<xsl:attribute name="border-end-width">medium</xsl:attribute>
		<xsl:attribute name="border-end-style">solid</xsl:attribute>
		<xsl:attribute name="border-end-color">black</xsl:attribute>
		<xsl:attribute name="text-align">center</xsl:attribute>
		<xsl:attribute name="font-size">8pt</xsl:attribute>
		<xsl:attribute name="padding-top">2pt</xsl:attribute>
	</xsl:attribute-set>

	<!-- Style Titre -->
	<xsl:attribute-set name="titre">
		<xsl:attribute name="text-align">center</xsl:attribute>
		<xsl:attribute name="padding-top">5pt</xsl:attribute>
		<xsl:attribute name="font-size">8pt</xsl:attribute>
		<xsl:attribute name="font-weight">bold</xsl:attribute>
	</xsl:attribute-set>
	
	<!--  Definition de variable -->
	<xsl:variable name="anneMiseAJour">
		<xsl:value-of select="relevePropriete/anneMiseAJour" />
	</xsl:variable>

	<xsl:variable name="service">
		<xsl:value-of select="relevePropriete/service" />
	</xsl:variable>

	<!-- Définition des informations communes -->
	<xsl:variable name="dateDeCreation">
		<xsl:value-of select="java:format(java:java.text.SimpleDateFormat.new('d MMMM yyyy'), java:java.util.Date.new())" />
	</xsl:variable>

	<!--  template global -->
	<xsl:template match="relevePropriete">
		
		<!-- Pour chaque compte communal une our plusieurs page(s) de pdf -->
		<xsl:for-each select="comptesCommunaux/compteCommunal">
			<!--  Entete comprenant le département la commune, et le numéro communal -->
			<xsl:call-template name="entete" />
			
			<!-- liste des proprietaires d'un compte communal -->
			<xsl:if test="proprietaires/proprietaire">
				<xsl:call-template name="proprietaire" />
			</xsl:if>
			
			<!-- liste des proprietes baties d'un compte communal -->
			<xsl:if test="proprietesBaties/proprietes/propriete">
				<xsl:call-template name="proprietesBaties" />
			</xsl:if>
			
			<!-- liste des proprietes non baties d'un compte communal -->
			<xsl:if test="proprietesNonBaties/proprietes/propriete">
				<xsl:call-template name="proprietesNonBaties" />			
			</xsl:if>
			
			<xsl:if test="proprietesNonBaties/imposition">
				<xsl:call-template name="revenuImposableNonBaties" />
			</xsl:if>
		</xsl:for-each>
	</xsl:template>

	<!-- Cartouche d'entête de chaque relevé, présent uniquement sur la premiere page   -->
	<xsl:template name="entete">
		<fo:block xsl:use-attribute-sets="titre" page-break-before="always">Relevé de propriété</fo:block>
		<fo:table table-layout="fixed">
			<fo:table-column column-width="30%" />
			<fo:table-column column-width="40%" />
			<fo:table-column column-width="30%" />
			<fo:table-body>
				<fo:table-row>
					<fo:table-cell>
						<fo:block text-align="start" font-size="8">
							Date de mise à jour des données :
							<xsl:value-of select="$anneMiseAJour" />
						</fo:block>
						<fo:block text-align="start">
							Département :
							<xsl:value-of select="@codeDepartement" />
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block text-align="center" padding-top="10pt">
							Commune :
							<xsl:value-of select="@codeCommune" />
							<xsl:value-of select="@libelleCommune" />
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block text-align="end">
							Compte communal :
							<xsl:value-of select="@compteCommunal" />
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
	</xsl:template>


	<!-- Liste des propriétaires comprenant Droit, Nom, Adresse et information de naissance  -->
	<xsl:template name="proprietaire">
		<fo:block xsl:use-attribute-sets="titre">Propriétaire(s)</fo:block>
		<fo:table table-layout="fixed">
			<fo:table-column column-width="50%" />
			<fo:table-column column-width="50%" />
			<fo:table-body>

				<xsl:for-each select="proprietaires/proprietaire">
					<fo:table-row>
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:block font-size="8">
								<xsl:value-of select="@droitReel" />
							</fo:block>
							<fo:block>
								<xsl:value-of select="@nom" />
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:block padding-top="5pt">
								<xsl:value-of select="@adresse" />
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</xsl:for-each>

			</fo:table-body>
		</fo:table>
	</xsl:template>

	<!-- Liste des propriétés baties -->
	<xsl:template name="proprietesBaties">
		<fo:block xsl:use-attribute-sets="titre">Propriété(s) batie(s)</fo:block>
		<fo:table table-layout="fixed">
			<fo:table-column column-width="3%" />
			<fo:table-column column-width="4%" />
			<fo:table-column column-width="4%" />
			<fo:table-column column-width="14%" />
			<fo:table-column column-width="3%" />
			<fo:table-column column-width="3%" />
			<fo:table-column column-width="3%" />
			<fo:table-column column-width="6%" />
			<fo:table-column column-width="14%" />
			<fo:table-column column-width="8%" />
			<fo:table-column column-width="6%" />
			<fo:table-column column-width="12%" />
			<fo:table-column column-width="4%" />
			<fo:table-column column-width="10%" />
			<fo:table-column column-width="6%" />
			<fo:table-header>
				<fo:table-row background-color="yellow" font-weight="bold">
					<fo:table-cell xsl:use-attribute-sets="bordure-title-end" number-columns-spanned="4">
						<fo:block>
							Désignation des propriétés
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure-title-end" number-columns-spanned="5">
						<fo:block>
							Identification du local
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure-title-end" number-columns-spanned="6">
						<fo:block>
							Evaluation du local
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row background-color="silver">
					<fo:table-cell xsl:use-attribute-sets="bordure-title">
						<fo:block>
							M
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure-title">
						<fo:block>
							N° section
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure-title">
						<fo:block>
							N° plan
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure-title-end">
						<fo:block>
							Adresse
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure-title">
						<fo:block>
							Bât
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure-title">
						<fo:block>
							Ent
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure-title">
						<fo:block>
							Niv
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure-title">
						<fo:block>
							N° porte
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure-title-end">
						<fo:block>
							N° invar
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure-title">
						<fo:block>
							M.EVAL
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure-title">
						<fo:block>
							AF
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure-title">
						<fo:block>
							Nat Loc
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure-title">
						<fo:block>
							Cat
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure-title">
						<fo:block>
							Coll
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure-title-end">
						<fo:block>
							Tx Om
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-header>
			<fo:table-body>
				<xsl:call-template name="proprietesBatiesValue" />
			</fo:table-body>
		</fo:table>
	</xsl:template>

	<!-- Liste des propriétés non baties  -->
	<xsl:template name="proprietesNonBaties">
		<fo:block xsl:use-attribute-sets="titre">Propriété(s) non batie(s)
		</fo:block>
		<fo:table table-layout="fixed">

			<!-- Alsace Moselle Special case add reference to livre foncier -->
			<xsl:choose>
				<xsl:when
					test="starts-with(@codeDepartement, '57') or starts-with(@codeDepartement, '67') or starts-with(@codeDepartement, '68')">
					<fo:table-column column-width="3%" />
					<fo:table-column column-width="4%" />
					<fo:table-column column-width="3%" />
					<fo:table-column column-width="11%" />
					<fo:table-column column-width="4%" />
					<fo:table-column column-width="6%" />
					<fo:table-column column-width="4%" />
					<fo:table-column column-width="12%" />
					<fo:table-column column-width="10%" />
					<fo:table-column column-width="11%" />
					<fo:table-column column-width="9%" />
					<fo:table-column column-width="8%" />
					<fo:table-column column-width="15%" />
				</xsl:when>
				<xsl:otherwise>
					<fo:table-column column-width="3%" />
					<fo:table-column column-width="4%" />
					<fo:table-column column-width="3%" />
					<fo:table-column column-width="11%" />
					<fo:table-column column-width="4%" />
					<fo:table-column column-width="6%" />
					<fo:table-column column-width="4%" />
					<fo:table-column column-width="16%" />
					<fo:table-column column-width="16%" />
					<fo:table-column column-width="15%" />
					<fo:table-column column-width="10%" />
					<fo:table-column column-width="8%" />
				</xsl:otherwise>
			</xsl:choose>
			<fo:table-header>
				<fo:table-row background-color="yellow" font-weight="bold">
					<fo:table-cell xsl:use-attribute-sets="bordure-title" number-columns-spanned="4">
						<fo:block>
							Désignation des propriétés
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure-title-end" number-columns-spanned="8">
						<fo:block>
							Evaluation
						</fo:block>
					</fo:table-cell>
					<!-- Alsace Moselle Special case -->
					<xsl:if	test="starts-with(@codeDepartement, '57') or starts-with(@codeDepartement, '67') or starts-with(@codeDepartement, '68')">
						<fo:table-cell xsl:use-attribute-sets="bordure-title-end" number-columns-spanned="1">
							<fo:block>
								Livre foncier
							</fo:block>
						</fo:table-cell>
					</xsl:if>
				</fo:table-row>
				<fo:table-row background-color="silver">
					<fo:table-cell xsl:use-attribute-sets="bordure-title">
						<fo:block>
							M
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure-title">
						<fo:block>
							N° section
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure-title">
						<fo:block>
							N° plan
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure-title-end">
						<fo:block>
							Adresse
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure-title">
						<fo:block>
							N° parc Prim
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure-title">
						<fo:block>
							FP / DP
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure-title">
						<fo:block>
							Suf
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure-title">
						<fo:block>
							GR/SS GR
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure-title">
						<fo:block>
							ref lot
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure-title">
						<fo:block>
							Nat Cult
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure-title">
						<fo:block>
							Contenance en CA
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure-title-end">
						<fo:block>
							Coll
						</fo:block>
					</fo:table-cell>
					<xsl:if	test="starts-with(@codeDepartement, '57') or starts-with(@codeDepartement, '67') or starts-with(@codeDepartement, '68')">
						<fo:table-cell xsl:use-attribute-sets="bordure-title-end">
							<fo:block>
								Feuillet
							</fo:block>
						</fo:table-cell>
					</xsl:if>
				</fo:table-row>
			</fo:table-header>
			<fo:table-body>
				<xsl:call-template name="proprietesNonBatiesValues" />
			</fo:table-body>
		</fo:table>
	</xsl:template>

	<!-- Designation des proprietes -->
	<xsl:template name="proprietesBatiesValue">
		<xsl:for-each select="proprietesBaties/proprietes/propriete">
			<fo:table-row>
				<fo:table-cell xsl:use-attribute-sets="bordure-values">
					<fo:block>
						<xsl:value-of select="@jdatat" />
					</fo:block>
				</fo:table-cell>
				<fo:table-cell xsl:use-attribute-sets="bordure-values">
					<fo:block>
					<xsl:value-of select="@ccopre" />
						<xsl:value-of select="@ccosec" />
					</fo:block>
				</fo:table-cell>
				<fo:table-cell xsl:use-attribute-sets="bordure-values">
					<fo:block>
						<xsl:value-of select="@dnupla" />
					</fo:block>
				</fo:table-cell>
				<fo:table-cell xsl:use-attribute-sets="bordure-values-end">
					<fo:block font-size="6pt">
						<xsl:value-of select="@dvoilib" />
					</fo:block>
				</fo:table-cell>
				<fo:table-cell xsl:use-attribute-sets="bordure-values">
					<fo:block>
						<xsl:value-of select="@dnubat" />
					</fo:block>
				</fo:table-cell>
				<fo:table-cell xsl:use-attribute-sets="bordure-values">
					<fo:block>
						<xsl:value-of select="@descr" />
					</fo:block>
				</fo:table-cell>
				<fo:table-cell xsl:use-attribute-sets="bordure-values">
					<fo:block>
						<xsl:value-of select="@dniv" />
					</fo:block>
				</fo:table-cell>
				<fo:table-cell xsl:use-attribute-sets="bordure-values">
					<fo:block>
						<xsl:value-of select="@dpor" />
					</fo:block>
				</fo:table-cell>
				<fo:table-cell xsl:use-attribute-sets="bordure-values-end">
					<fo:block>
						<xsl:value-of select="@invar" />
					</fo:block>
				</fo:table-cell>
				<fo:table-cell xsl:use-attribute-sets="bordure-values">
					<fo:block>
						<xsl:value-of select="@ccoeva" />
					</fo:block>
				</fo:table-cell>
				<fo:table-cell xsl:use-attribute-sets="bordure-values">
					<fo:block>
						<xsl:value-of select="@ccoaff" />
					</fo:block>
				</fo:table-cell>
				<fo:table-cell xsl:use-attribute-sets="bordure-values">
					<fo:block>
						<xsl:value-of select="@cconlc" />
					</fo:block>
				</fo:table-cell>
				<fo:table-cell xsl:use-attribute-sets="bordure-values">
					<fo:block>
						<xsl:value-of select="@dcapec" />
						<xsl:value-of select="@ccocac" />
					</fo:block>
				</fo:table-cell>
				<fo:table-cell xsl:use-attribute-sets="bordure-values">
					<fo:block>
						<xsl:value-of select="@ccolloc" />
					</fo:block>
				</fo:table-cell>
				<fo:table-cell xsl:use-attribute-sets="bordure-values-end">
					<fo:block>
						<xsl:value-of select="@gtauom" />
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
		</xsl:for-each>
	</xsl:template>


	<xsl:template name="proprietesNonBatiesValues">
		<xsl:for-each select="proprietesNonBaties/proprietes/propriete">
			<fo:table-row height="20pt">
				<fo:table-cell xsl:use-attribute-sets="bordure-values">
					<fo:block>
						<xsl:value-of select="@jdatat" />
					</fo:block>
				</fo:table-cell>
				<fo:table-cell xsl:use-attribute-sets="bordure-values">
					<fo:block>
						<xsl:value-of select="@ccopre" />
						<xsl:value-of select="@ccosec" />
					</fo:block>
				</fo:table-cell>
				<fo:table-cell xsl:use-attribute-sets="bordure-values">
					<fo:block>
						<xsl:value-of select="@dnupla" />
					</fo:block>
				</fo:table-cell>
				<fo:table-cell xsl:use-attribute-sets="bordure-values-end">
					<fo:block>
						<xsl:value-of select="@dvoilib" />
					</fo:block>
				</fo:table-cell>
				<fo:table-cell xsl:use-attribute-sets="bordure-values">
					<fo:block>
						<xsl:value-of select="@dparpi" />
					</fo:block>
				</fo:table-cell>
				<fo:table-cell xsl:use-attribute-sets="bordure-values">
					<fo:block>
						<xsl:value-of select="@gparnf" />
					</fo:block>
				</fo:table-cell>
				<fo:table-cell xsl:use-attribute-sets="bordure-values">
					<fo:block>
						<xsl:value-of select="@ccosub" />
					</fo:block>
				</fo:table-cell>
				<fo:table-cell xsl:use-attribute-sets="bordure-values">
					<fo:block>
						<xsl:value-of select="@cgrnum" />
						/
						<xsl:value-of select="@dsgrpf" />
					</fo:block>
				</fo:table-cell>
				<fo:table-cell xsl:use-attribute-sets="bordure-values">
					<fo:block>
						<xsl:value-of select="@dnulot" />
					</fo:block>
				</fo:table-cell>
				<fo:table-cell xsl:use-attribute-sets="bordure-values">
					<fo:block>
						<xsl:value-of select="@cnatsp" />
					</fo:block>
				</fo:table-cell>
				<fo:table-cell xsl:use-attribute-sets="bordure-values">
					<fo:block>
						<xsl:value-of select="@dcntsf" />
					</fo:block>
				</fo:table-cell>
				<fo:table-cell xsl:use-attribute-sets="bordure-values-end">
					<fo:block>
						<xsl:value-of select="@ccolloc" />
					</fo:block>
				</fo:table-cell>
				<xsl:if	test="starts-with(@codeDepartement, '57') or starts-with(@codeDepartement, '67') or starts-with(@codeDepartement, '68')">
					<fo:table-cell xsl:use-attribute-sets="bordure-values-end">
						<fo:block>
							<xsl:value-of select="@dreflf" />
						</fo:block>
					</fo:table-cell>
				</xsl:if>
			</fo:table-row>
		</xsl:for-each>
	</xsl:template>

	<xsl:template name="revenuImposableNonBaties">
		<fo:block>&#160;</fo:block>
		<fo:table table-layout="fixed" xsl:use-attribute-sets="bordure">
			<fo:table-column column-width="8%" />
			<fo:table-column column-width="8%" />
			<fo:table-column column-width="8%" />
			<fo:table-body>
				<fo:table-row>
					<fo:table-cell>
						<fo:block padding-top="5pt">
							Surface Totale :
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block padding-top="5pt">
							<xsl:value-of select="proprietesNonBaties/imposition/@surface" /> CA
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block padding-top="5pt">
							MAJ POS : <xsl:value-of select="proprietesNonBaties/imposition/@majorationTerraion" />
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
	</xsl:template>

</xsl:stylesheet>