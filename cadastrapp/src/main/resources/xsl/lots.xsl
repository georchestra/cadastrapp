<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:java="http://xml.apache.org/xslt/java" xmlns:date="http://exslt.org/dates-and-times"
	exclude-result-prefixes="java">
	<xsl:decimal-format name="euro" decimal-separator="," grouping-separator=" " NaN=" "/>
	
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
					<xsl:apply-templates select="informationLots" />
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
	
	<!-- Style Titre -->
	<xsl:attribute-set name="titre">
		<xsl:attribute name="text-align">center</xsl:attribute>
		<xsl:attribute name="padding-top">5pt</xsl:attribute>
		<xsl:attribute name="font-size">10pt</xsl:attribute>
		<xsl:attribute name="font-weight">bold</xsl:attribute>
	</xsl:attribute-set>
	
	<!-- Bordure de tableau -->
	<xsl:attribute-set name="bordure">
		<xsl:attribute name="border">solid 1 black</xsl:attribute>
		<xsl:attribute name="text-align">center</xsl:attribute>
		<xsl:attribute name="font-size">6pt</xsl:attribute>
		<xsl:attribute name="padding">2pt</xsl:attribute>
	</xsl:attribute-set>
	
	<!-- Style attributions -->
	<xsl:attribute-set name="attributions">
		<xsl:attribute name="text-align">center</xsl:attribute>
		<xsl:attribute name="padding-top">5pt</xsl:attribute>
		<xsl:attribute name="font-size">5pt</xsl:attribute>
		<xsl:attribute name="font-style">italic</xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:variable name="anneMiseAJour">
		<xsl:value-of select="informationLots/anneMiseAJour" />
	</xsl:variable>

	<xsl:variable name="service">
		<xsl:value-of select="informationLots/service" />
	</xsl:variable>

	<!-- Définition des informations communes -->
	<xsl:variable name="dateDeCreation">
		<xsl:value-of
			select="java:format(java:java.text.SimpleDateFormat.new('d MMMM yyyy'), java:java.util.Date.new())" />
	</xsl:variable>
	
	<!--  template global -->
	<xsl:template match="informationLots">
		<!-- Pour description de la parcelle -->
		<xsl:if test="parcelle">
			<xsl:call-template name="entete" />
		</xsl:if>
			
		<!-- liste des Lot si  au moins un lot -->
		<xsl:if test="lots/Lot">
			<xsl:call-template name="lot" />
		</xsl:if>
					
		<fo:block>&#160;</fo:block>
		<fo:block xsl:use-attribute-sets="attributions">
			<xsl:value-of select="$service" />
		</fo:block>
		<fo:block xsl:use-attribute-sets="attributions">
			Ce document est donné à titre indicatif - Il n'a pas de valeur légale
		</fo:block>
	</xsl:template>
	
	<!-- Cartouche d'entête de chaque relevé, présent uniquement sur la premiere page   -->
	<xsl:template name="entete">
		<fo:block xsl:use-attribute-sets="titre" page-break-before="always">Lots de copropriété</fo:block>
		<fo:table table-layout="fixed">
			<fo:table-column column-width="30%" />
			<fo:table-column column-width="40%" />
			<fo:table-column column-width="30%" />
			<fo:table-body>
				<fo:table-row>
					<fo:table-cell>
						<fo:block text-align="start" font-size="8" padding-top="8pt">
							Date de mise à jour des données :
							<xsl:value-of select="$anneMiseAJour" />
						</fo:block>
						<fo:block text-align="start" font-size="8">
							Date de création du document :
							<xsl:value-of select="$dateDeCreation" />
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block text-align="center" padding-top="8pt">
							Parcelle :
							<xsl:value-of select="parcelle" />
						</fo:block>
						<fo:block text-align="center">
							Batiment :
							<xsl:value-of select="batiment" />
						</fo:block>
						<fo:block text-align="center">
							Adresse :
							<xsl:value-of select="adresse" />
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block text-align="end" padding-top="8pt">
							Nombre de lots :
							<xsl:value-of select="nbLots" />
						</fo:block>
						<fo:block text-align="end">
							Somme des parts :
							<xsl:value-of select="sumPart" />
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
	</xsl:template>
	
	<!-- Liste des propriétaires comprenant Droit, Nom, Adresse et information de naissance  -->
	<xsl:template name="proprietaire">
		<fo:table table-layout="fixed">
			<fo:table-column column-width="20%" />
			<fo:table-column column-width="40%" />
			<fo:table-column column-width="40%" />
			<fo:table-body>
				<xsl:for-each select="proprietaires/Proprietaire">
					<fo:table-row>
						<fo:table-cell>
							<fo:block>
								<xsl:value-of select="@compteCommunal" />
							</fo:block>
						</fo:table-cell>
						<fo:table-cell>
							<fo:block>
								<xsl:value-of select="@droitReel" />
							</fo:block>
							<fo:block>
								<xsl:value-of select="@nom" />
							</fo:block>
						</fo:table-cell>
								<fo:table-cell>
							<fo:block>
								<xsl:value-of select="@adresse" />
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</xsl:for-each>
			</fo:table-body>
		</fo:table>
	</xsl:template>
	
	<!-- Liste des propriétés baties -->
	<xsl:template name="lot">
		<fo:block xsl:use-attribute-sets="titre">Description des lots</fo:block>
		<fo:table table-layout="fixed">
			<fo:table-column column-width="10%" />
			<fo:table-column column-width="10%" />
			<fo:table-column column-width="10%" />
			<fo:table-column column-width="10%" />
			<fo:table-column column-width="10%" />
			<fo:table-column column-width="10%" />
			<fo:table-column column-width="40%" />
			<fo:table-header background-color="yellow" font-weight="bold">
				<fo:table-row>
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<fo:block>
							Numéro invariant
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<fo:block>
							Numéro de lot
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<fo:block>
							Parts
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<fo:block>
							Logement
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<fo:block>
							Dépendance
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<fo:block>
							Local commercial
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<fo:block>
							Proprietaire
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-header>
			<fo:table-body>
				<xsl:for-each select="lots/Lot">
					<fo:table-row>
						<!-- Adresses des propriétés -->
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:block>
								<!--  invar -->
								<xsl:value-of select="Propriete/@invar" />
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:block>
								<xsl:value-of select="@lotId" />
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:block>
								<xsl:value-of select="@numerateur" />/<xsl:value-of select="@denominateur" />
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:block font-size="6pt">
								<xsl:value-of select="Propriete/@cconlc" />
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:block>
								<xsl:value-of select="Propriete/@cconad" />
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:block>
								<xsl:value-of select="Propriete/@ccocac" />
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:block>
								<!--  Proprietaire -->
								<xsl:call-template name="proprietaire" />
							</fo:block>					
						</fo:table-cell>
					</fo:table-row>
				</xsl:for-each>
			</fo:table-body>
		</fo:table>
	</xsl:template>
	

</xsl:stylesheet>
