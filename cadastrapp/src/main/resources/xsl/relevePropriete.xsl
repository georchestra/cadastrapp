<?xml version="1.0" encoding="iso-8859-1"?>

<xsl:stylesheet version="1.0"
	xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:java="http://xml.apache.org/xslt/java" exclude-result-prefixes="java">

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
	<xsl:attribute-set name="bordure">
		<xsl:attribute name="border">solid 0.1mm black</xsl:attribute>
		<xsl:attribute name="text-align">center</xsl:attribute>
	</xsl:attribute-set>

	<!-- Style Titre -->
	<xsl:attribute-set name="titre">
		<xsl:attribute name="text-align">center</xsl:attribute>
		<xsl:attribute name="padding-top">5pt</xsl:attribute>
		<xsl:attribute name="font-size">8pt</xsl:attribute>
	</xsl:attribute-set>

	<!-- Format de text simple -->
	<xsl:attribute-set name="text">
		<xsl:attribute name="text-align">center</xsl:attribute>
		<xsl:attribute name="padding-top">10pt</xsl:attribute>
		<xsl:attribute name="padding-bottom">10pt</xsl:attribute>
		<xsl:attribute name="font-size">8pt</xsl:attribute>
	</xsl:attribute-set>

	<!-- Format de text gras -->
	<xsl:attribute-set name="text-bold">
		<xsl:attribute name="text-align">center</xsl:attribute>
		<xsl:attribute name="padding-top">10pt</xsl:attribute>
		<xsl:attribute name="padding-bottom">10pt</xsl:attribute>
		<xsl:attribute name="font-size">10pt</xsl:attribute>
		<xsl:attribute name="font-weight">bold</xsl:attribute>
	</xsl:attribute-set>

	<xsl:variable name="anneMiseAJour">
		<xsl:value-of select="relevePropriete/anneMiseAJour" />
	</xsl:variable>

	<xsl:variable name="service">
		<xsl:value-of select="relevePropriete/service" />
	</xsl:variable>

	<!-- Définition des informations communes -->
	<xsl:variable name="dateDeCreation">
		<xsl:value-of
			select="java:format(java:java.text.SimpleDateFormat.new('d MMMM yyyy'), java:java.util.Date.new())" />
	</xsl:variable>

	<xsl:template match="relevePropriete">
		<!-- Pour chaque parcelle -->
		<xsl:for-each select="parcelles/parcelle">
			<xsl:call-template name="entete" />
			<xsl:if test="proprietaires">
				<xsl:call-template name="proprietaire" />
			</xsl:if>
			<xsl:call-template name="proprietesBaties" />
			<xsl:call-template name="proprietesNonBaties" />
		</xsl:for-each>
	</xsl:template>

	<xsl:template name="entete">
		<fo:block xsl:use-attribute-sets="titre">Relevé de propriété
		</fo:block>
		<fo:table>
			<fo:table-column column-width="25%" />
			<fo:table-column column-width="50%" />
			<fo:table-column column-width="25%" />
			<fo:table-body>
				<fo:table-row>
					<fo:table-cell>
						<fo:block>
							Année m.a.j :
							<xsl:value-of select="$anneMiseAJour" />
						</fo:block>
						<fo:block>
							Département :
							<xsl:value-of select="@parcelleId" />
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block>
							Commune :
							<xsl:value-of select="@parcelleId" />
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block>
							Numéro communal :
							<xsl:value-of select="@parcelleId" />
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
	</xsl:template>

	<xsl:template name="proprietaire">
		<fo:block xsl:use-attribute-sets="titre">Propriétaire</fo:block>
		<fo:table>
			<fo:table-column column-width="34%" />
			<fo:table-column column-width="33%" />
			<fo:table-column column-width="33%" />
			<fo:table-body>
				<fo:table-row>
					<xsl:for-each select="proprietaires/proprietaire">
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:block>
								<xsl:value-of select="@nom" />
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:block>
								<xsl:value-of select="@adresse" />
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:block>
								<xsl:value-of select="@dateNaissance" />
							</fo:block>
						</fo:table-cell>
					</xsl:for-each>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
	</xsl:template>

	<xsl:template name="proprietesBaties">
		<fo:block xsl:use-attribute-sets="titre">Propriété(s) batie(s)
		</fo:block>
		<fo:table>
			<fo:table-column column-width="20%" />
			<fo:table-column column-width="20%" />
			<fo:table-column column-width="60%" />
			<fo:table-header>
				<fo:table-row>
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<fo:block>
							Désignation des propriétés
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<fo:block>
							Identification du local
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<fo:block>
							Evaluation du local
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-header>
			<fo:table-body>
				<fo:table-row>
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<fo:table>
							<fo:table-column column-width="15%" />
							<fo:table-column column-width="15%" />
							<fo:table-column column-width="15%" />
							<fo:table-column column-width="10%" />
							<fo:table-column column-width="15%" />
							<fo:table-column column-width="15%" />
							<fo:table-column column-width="15%" />
							<fo:table-header>
								<fo:table-row>
									<fo:table-cell xsl:use-attribute-sets="bordure">
										<fo:block>
											M
										</fo:block>
									</fo:table-cell>
									<fo:table-cell xsl:use-attribute-sets="bordure">
										<fo:block>
											N° section
										</fo:block>
									</fo:table-cell>
									<fo:table-cell xsl:use-attribute-sets="bordure">
										<fo:block>
											N° plan
										</fo:block>
									</fo:table-cell>
									<fo:table-cell xsl:use-attribute-sets="bordure">
										<fo:block>
											X
										</fo:block>
									</fo:table-cell>
									<fo:table-cell xsl:use-attribute-sets="bordure">
										<fo:block>
											Adresse
										</fo:block>
									</fo:table-cell>
									<fo:table-cell xsl:use-attribute-sets="bordure">
										<fo:block>
											Code Rivoli
										</fo:block>
									</fo:table-cell>					
								</fo:table-row>
							</fo:table-header>
							<fo:table-body>
								<fo:table-row>
									<fo:table-cell xsl:use-attribute-sets="bordure">
										<fo:block>
											M
										</fo:block>
									</fo:table-cell>
									<fo:table-cell xsl:use-attribute-sets="bordure">
										<fo:block>
											N° section
										</fo:block>
									</fo:table-cell>
									<fo:table-cell xsl:use-attribute-sets="bordure">
										<fo:block>
											N° plan
										</fo:block>
									</fo:table-cell>
									<fo:table-cell xsl:use-attribute-sets="bordure">
										<fo:block>
											X
										</fo:block>
									</fo:table-cell>
									<fo:table-cell xsl:use-attribute-sets="bordure">
										<fo:block>
											Adresse
										</fo:block>
									</fo:table-cell>
									<fo:table-cell xsl:use-attribute-sets="bordure">
										<fo:block>
											Code Rivoli
										</fo:block>
									</fo:table-cell>					
								</fo:table-row>
							</fo:table-body>
						</fo:table>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:block>
								Données
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:block>
								Données
							</fo:block>
						</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
	</xsl:template>

	<xsl:template name="proprietesNonBaties">
		<fo:block xsl:use-attribute-sets="titre">Propriété(s) non batie(s)
		</fo:block>
		<fo:table>
			<fo:table-column column-width="25%" />
			<fo:table-column column-width="50%" />
			<fo:table-column column-width="25%" />
			<fo:table-header>
				<fo:table-row>
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<fo:block>
							Désignation des propriétés
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<fo:block>
							Evaluation
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<fo:block>
							Livre foncier
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-header>
			<fo:table-body>
				<fo:table-row>
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<fo:block>
							Données
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<fo:block>
							Données
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<fo:block>
							Données
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
	</xsl:template>

</xsl:stylesheet>