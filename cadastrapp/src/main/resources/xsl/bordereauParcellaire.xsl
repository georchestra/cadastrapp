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
					<fo:region-body/>
				</fo:simple-page-master>
			</fo:layout-master-set>

			<fo:page-sequence master-reference="portrait">
				<fo:flow flow-name="xsl-region-body">
					<xsl:apply-templates select="bordereauParcellaire" />
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
		<xsl:variable name="dateDeValidite">
			<xsl:value-of select="dateDeValidite" />
		</xsl:variable>

		<xsl:for-each select="parcelles/parcelle">
			<fo:table>
				<fo:table-column column-width="70%" />
				<fo:table-column column-width="30%" />
				<fo:table-body>
					<fo:table-row>
						<!-- Empreinte -->
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:block>
								<fo:external-graphic>
									<xsl:attribute name="src">http://localhost:8080/cadastrapp/services/getImageBordereau?parcelle=<xsl:value-of
										select="@parcelleId" /></xsl:attribute>
								</fo:external-graphic>
							</fo:block>
						</fo:table-cell>
						<!-- descriptif -->
						<fo:table-cell>
							<fo:table>
								<fo:table-column column-width="100%" />
								<fo:table-footer>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="bordure">
											<fo:block xsl:use-attribute-sets="text-bold">
												<xsl:value-of select="$service" />
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
								</fo:table-footer>
								<fo:table-body>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="bordure">
											<fo:block xsl:use-attribute-sets="text">
												Extrait du plan
												cadastral informatisé
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="bordure">
											<fo:block xsl:use-attribute-sets="text">
												Données foncières valides au
												<xsl:value-of select="$dateDeValidite" />
											</fo:block>
											<fo:block xsl:use-attribute-sets="text">
												Document créé le
												<xsl:value-of select="$dateDeCreation" />
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="bordure">
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
										<fo:table-cell xsl:use-attribute-sets="bordure">
											<fo:block xsl:use-attribute-sets="text">
												<xsl:value-of select="@adresseCadastrale" />
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="bordure">
											<fo:table>
												<fo:table-column column-width="15%" />
												<fo:table-column column-width="15%" />
												<fo:table-column column-width="35%" />
												<fo:table-column column-width="35%" />
												<fo:table-body>
													<fo:table-row>
														<fo:table-cell xsl:use-attribute-sets="bordure">
															<fo:block xsl:use-attribute-sets="text">
																section
															</fo:block>
														</fo:table-cell>
														<fo:table-cell xsl:use-attribute-sets="bordure">
															<fo:block xsl:use-attribute-sets="text">
																parcelle
															</fo:block>
														</fo:table-cell>
														<fo:table-cell xsl:use-attribute-sets="bordure">
															<fo:block xsl:use-attribute-sets="text">
																code de la
																voie
															</fo:block>
															<fo:block xsl:use-attribute-sets="text">
																(FANTOIR)
															</fo:block>
														</fo:table-cell>
														<fo:table-cell xsl:use-attribute-sets="bordure">
															<fo:block xsl:use-attribute-sets="text">
																surface
																cadastrale
															</fo:block>
														</fo:table-cell>
													</fo:table-row>
													<fo:table-row>
														<fo:table-cell xsl:use-attribute-sets="bordure">
															<fo:block xsl:use-attribute-sets="text">
																<xsl:value-of select="@section" />
															</fo:block>
														</fo:table-cell>
														<fo:table-cell xsl:use-attribute-sets="bordure">
															<fo:block xsl:use-attribute-sets="text">
																<xsl:value-of select="@parcelle" />
															</fo:block>
														</fo:table-cell>
														<fo:table-cell xsl:use-attribute-sets="bordure">
															<fo:block xsl:use-attribute-sets="text">
																<xsl:value-of select="@codeFantoir" />
															</fo:block>
														</fo:table-cell>
														<fo:table-cell xsl:use-attribute-sets="bordure">
															<fo:block xsl:use-attribute-sets="text">
																<xsl:value-of select="@surfaceCadastrale" />
															</fo:block>
														</fo:table-cell>
													</fo:table-row>
												</fo:table-body>
											</fo:table>
										</fo:table-cell>
									</fo:table-row>

									<xsl:if test="proprietaires">
										<fo:table-row>
											<fo:table-cell>
												<xsl:for-each select="proprietaires/proprietaire">
													<fo:block text-align="center" padding-top="5pt" font-size="10pt">
														<xsl:value-of select="@nom" />
													</fo:block>
													<fo:block text-align="center" padding-bottom="5pt" font-size="8pt">
														<xsl:value-of select="@adresse" />
													</fo:block>
												</xsl:for-each>
											</fo:table-cell>
										</fo:table-row>
									</xsl:if>

								</fo:table-body>
							</fo:table>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-body>
			</fo:table>
		</xsl:for-each>
	</xsl:template>

	<xsl:template match="parcelletTab">
		<fo:table>
			<fo:table-column column-width="20%" />
			<fo:table-column column-width="20%" />
			<fo:table-column column-width="30%" />
			<fo:table-column column-width="30%" />
			<fo:table-body>
				<fo:table-row>
					<fo:table-cell>
						<fo:block margin="12pt" font-size="8pt">
							Section
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block margin="12pt" font-size="8pt">
							<xsl:value-of select="section" />
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
					<fo:table-cell>
						<fo:block margin="12pt" font-size="8pt">
							parcelle
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block margin="12pt" font-size="8pt">
							<xsl:value-of select="parcelle" />
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
					<fo:table-cell>
						<fo:block margin="12pt" font-size="8pt">
							code de la voie
						</fo:block>
						<fo:block margin="12pt" font-size="8pt">
							(FANTOIR)
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block margin="12pt" font-size="8pt">
							<xsl:value-of select="codeFantoir" />
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
					<fo:table-cell>
						<fo:block margin="12pt" font-size="8pt">
							surface cadastrale
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block margin="12pt" font-size="8pt">
							<xsl:value-of select="surfaceCadastrale" />
							m²
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
	</xsl:template>

</xsl:stylesheet>