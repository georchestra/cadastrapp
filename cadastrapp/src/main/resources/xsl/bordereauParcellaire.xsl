<?xml version="1.0" encoding="UTF-8"?>

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
					<xsl:apply-templates select="bordereauParcellaire" />
				</fo:flow>
			</fo:page-sequence>

		</fo:root>
	</xsl:template>

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

	<xsl:template match="bordereauParcellaire">

		<xsl:variable name="dateDeCreation">
			<xsl:value-of
				select="java:format(java:java.text.SimpleDateFormat.new('d MMMM yyyy'), java:java.util.Date.new())" />
		</xsl:variable>
		<xsl:variable name="service">
			<xsl:value-of select="service" />
		</xsl:variable>
		<xsl:variable name="serviceUrl">
			<xsl:value-of select="serviceUrl" />
		</xsl:variable>
		<xsl:variable name="dateDeValiditeMajic">
			<xsl:value-of select="dateDeValiditeMajic" />
		</xsl:variable>
		<xsl:variable name="dateDeValiditeEdigeo">
			<xsl:value-of select="dateDeValiditeEdigeo" />
		</xsl:variable>

		<xsl:for-each select="parcelles/parcelle">
			<fo:table table-layout="fixed" page-break-before="always">
				<fo:table-column column-width="70%" />
				<fo:table-column column-width="30%" />
				<fo:table-body>
					<fo:table-row>
						<!-- Empreinte -->
						<fo:table-cell>
							<fo:block>
								<fo:external-graphic>
									<xsl:attribute name="src"><xsl:value-of
										select="$serviceUrl" />/getImageBordereau?parcelle=<xsl:value-of
										select="@parcelleId" /></xsl:attribute>
								</fo:external-graphic>
							</fo:block>
						</fo:table-cell>
						<!-- descriptif -->
						<fo:table-cell>
							<fo:table table-layout="fixed">
								<fo:table-column column-width="100%" />
								<fo:table-body>
									<fo:table-row>
										<fo:table-cell>
											<fo:block xsl:use-attribute-sets="text">
												Extrait du plan
												cadastral informatisé
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell>
											<fo:block xsl:use-attribute-sets="text">
												Données MAJIC valides au
												<xsl:value-of select="$dateDeValiditeMajic" />
											</fo:block>
											<fo:block xsl:use-attribute-sets="text">
												Données EDIGEO valides au
												<xsl:value-of select="$dateDeValiditeEdigeo" />
											</fo:block>
											<fo:block xsl:use-attribute-sets="text">
												Document créé le
												<xsl:value-of select="$dateDeCreation" />
											</fo:block>
											<fo:block xsl:use-attribute-sets="text">
												Fond de plan
												origine DGFiP - Reproduction interdite - Mesures données à
												titre indicatif
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell>
											<fo:block xsl:use-attribute-sets="text-bold">
												<xsl:value-of select="@libelleCommune" />
											</fo:block>
											<fo:block xsl:use-attribute-sets="text">
												Parcelle cadastrale
												<xsl:value-of select="@parcelleId" />
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell>
											<fo:block xsl:use-attribute-sets="text">
												<xsl:value-of select="@adresseCadastrale" />
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell>
											<fo:table table-layout="fixed">
												<fo:table-column column-width="15%" />
												<fo:table-column column-width="15%" />
												<fo:table-column column-width="35%" />
												<fo:table-column column-width="35%" />
												<fo:table-body>
													<fo:table-row>
														<fo:table-cell>
															<fo:block xsl:use-attribute-sets="text">
																section
															</fo:block>
														</fo:table-cell>
														<fo:table-cell>
															<fo:block xsl:use-attribute-sets="text">
																parcelle
															</fo:block>
														</fo:table-cell>
														<fo:table-cell>
															<fo:block xsl:use-attribute-sets="text">
																code de la
																voie
															</fo:block>
														</fo:table-cell>
														<fo:table-cell>
															<fo:block xsl:use-attribute-sets="text">
																surface
																cadastrale
															</fo:block>
														</fo:table-cell>
													</fo:table-row>
													<fo:table-row>
														<fo:table-cell>
															<fo:block xsl:use-attribute-sets="text">
																<xsl:value-of select="@section" />
															</fo:block>
														</fo:table-cell>
														<fo:table-cell>
															<fo:block xsl:use-attribute-sets="text">
																<xsl:value-of select="@parcelle" />
															</fo:block>
														</fo:table-cell>
														<fo:table-cell>
															<fo:block xsl:use-attribute-sets="text">
																<xsl:value-of select="@codeFantoir" />
															</fo:block>
														</fo:table-cell>
														<fo:table-cell>
															<fo:block xsl:use-attribute-sets="text">
																<xsl:value-of select="@surfaceCadastrale" />
															</fo:block>
														</fo:table-cell>
													</fo:table-row>
												</fo:table-body>
											</fo:table>
										</fo:table-cell>
									</fo:table-row>

									<fo:table-row height="10cm">
										<xsl:if test="proprietaires">
											<fo:table-cell>
												<xsl:for-each select="proprietaires/proprietaire">
													<fo:block text-align="left" padding-top="5pt" padding-left="5pt"
														font-size="10pt">
														<xsl:value-of select="@nom" />
													</fo:block>
													<fo:block text-align="left" padding-bottom="5pt" padding-left="5pt" 
														font-size="8pt">
														<xsl:value-of select="@adresse" />
													</fo:block>
												</xsl:for-each>
											</fo:table-cell>
										</xsl:if>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell >
											<fo:block xsl:use-attribute-sets="text-bold">
												<xsl:value-of select="$service" />
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
								</fo:table-body>
							</fo:table>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-body>
			</fo:table>
		</xsl:for-each>
	</xsl:template>

</xsl:stylesheet>