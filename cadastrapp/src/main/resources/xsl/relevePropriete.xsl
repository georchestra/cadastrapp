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
					<xsl:apply-templates select="relevePropriete" />
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
			<xsl:if test="proprietesBaties/proprietes">
				<xsl:call-template name="proprietesBaties" />
				
				<xsl:if test="proprietesBaties/imposition">
					<fo:block>&#160;</fo:block>
					<xsl:call-template name="revenuImposable" />
				</xsl:if>
			</xsl:if>
			
			<!-- liste des proprietes non baties d'un compte communal -->
			<xsl:if test="proprietesNonBaties/proprietes">
				<xsl:call-template name="proprietesNonBaties" />
				
				<xsl:if test="proprietesNonBaties/imposition">
					<fo:block>&#160;</fo:block>
					<xsl:call-template name="revenuImposableNonBaties" />
				</xsl:if>
			</xsl:if>
		</xsl:for-each>
		
		<fo:block>&#160;</fo:block>
		<fo:block xsl:use-attribute-sets="attributions">
			<xsl:value-of select="$service" />
		</fo:block>
		<fo:block xsl:use-attribute-sets="attributions">
			Les informations contenues dans ce document sont réservées à l'usage personnel du demandeur (art L107 B du livre des procédures fiscales). Vous ne pouvez pas communiquer d'informations à caractère personnel à des tiers sans accord express des personnes concernées par ces données (chap II art 13 loi Informatique et Libertés de 1978 modifiée 2004).
Les informations contenues dans ce document sont les plus à jour dans la mesure des capacités des responsables du logiciel à les maintenir à jour.
		</fo:block>
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
							<xsl:value-of select="@codeCommune" />&#160;
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
			<fo:table-column column-width="34%" />
			<fo:table-column column-width="33%" />
			<fo:table-column column-width="33%" />
			<fo:table-body>

				<xsl:for-each select="proprietaires/proprietaire">
					<fo:table-row>
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:block>
								<xsl:value-of select="@droitReel" />
							</fo:block>
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
								<xsl:if test="@dateNaissance or @lieuNaissance">
									Né(e)
								</xsl:if>
								<xsl:if test="@dateNaissance">
									le
									<xsl:value-of select="@dateNaissance" />
								</xsl:if>
								<xsl:if test="@lieuNaissance">
									à
									<xsl:value-of select="@lieuNaissance" />
								</xsl:if>
								<xsl:if test="@nomNaissance">
									&#x2028;
									<xsl:value-of select="@nomNaissance" />
								</xsl:if>
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
			<fo:table-column column-width="2%" />
			<fo:table-column column-width="3%" />
			<fo:table-column column-width="3%" />
			<fo:table-column column-width="12%" />
			<fo:table-column column-width="4%" />
			<fo:table-column column-width="2%" />
			<fo:table-column column-width="2%" />
			<fo:table-column column-width="2%" />
			<fo:table-column column-width="6%" />
			<fo:table-column column-width="8%" />
			<fo:table-column column-width="2%" />
			<fo:table-column column-width="2%" />
			<fo:table-column column-width="6%" />
			<fo:table-column column-width="4%" />
			<fo:table-column column-width="8%" />
			<fo:table-column column-width="2%" />
			<fo:table-column column-width="4%" />
			<fo:table-column column-width="6%" />
			<fo:table-column column-width="6%" />
			<fo:table-column column-width="6%" />
			<fo:table-column column-width="4%" />
			<fo:table-column column-width="6%" />
			<fo:table-header background-color="yellow" font-weight="bold">
				<fo:table-row>
					<fo:table-cell xsl:use-attribute-sets="bordure" number-columns-spanned="5">
						<fo:block>
							Désignation des propriétés
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure" number-columns-spanned="5">
						<fo:block>
							Identification du local
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure" number-columns-spanned="12">
						<fo:block>
							Evaluation du local
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row background-color="silver">
					<!-- Adresses des propriétés -->
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
							Adresse
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<fo:block>
							Code Fantoir
						</fo:block>
					</fo:table-cell>
					<!-- Identifiants des locaux -->
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<fo:block>
							Bât
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<fo:block>
							Ent
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<fo:block>
							Niv
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<fo:block>
							N° porte
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<fo:block>
							N° invar
						</fo:block>
					</fo:table-cell>
					<!-- Infos de taxation des propriétés -->
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<fo:block>
							M. EVAL
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<fo:block>
							AF
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<fo:block>
							Nat Loc
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<fo:block>
							Cat
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<fo:block>
							Base d'imposition
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<fo:block>
							Coll
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<fo:block>
							Nat Exo
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<fo:block>
							An ret
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<fo:block>
							An deb
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<fo:block>
							Fraction exo
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<fo:block>
							Exo
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<fo:block>
							Tx Om
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-header>
			<fo:table-body>
				<xsl:for-each select="proprietesBaties/proprietes/propriete">
					<fo:table-row>
						<!-- Adresses des propriétés -->
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:block>
								<xsl:value-of select="@jdatat" />
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:block>
								<xsl:value-of select="@ccopre" />
								<xsl:value-of select="@ccosec" />
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:block>
								<xsl:value-of select="@dnupla" />
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:block font-size="6pt">
								<xsl:value-of select="@dnvoiri" /><xsl:text> </xsl:text>
								<xsl:value-of select="@dindic" /><xsl:text> </xsl:text>
								<xsl:value-of select="@dvoilib" />
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:block>
								<xsl:value-of select="@ccoriv" />
							</fo:block>
						</fo:table-cell>
						<!-- Identifiants des locaux -->
						<fo:table-cell xsl:use-attribute-sets="bordure" number-columns-spanned="5">
							<fo:table table-layout="fixed">
								<fo:table-column column-width="10%" />
								<fo:table-column column-width="10%" />
								<fo:table-column column-width="10%" />
								<fo:table-column column-width="30%" />
								<fo:table-column column-width="40%" />
								<fo:table-body>
									<fo:table-row>
										<fo:table-cell>
											<fo:block>
												<xsl:value-of select="@dnubat" />
											</fo:block>
										</fo:table-cell>
										<fo:table-cell>
											<fo:block>
												<xsl:value-of select="@descr" />
											</fo:block>
										</fo:table-cell>
										<fo:table-cell>
											<fo:block>
												<xsl:value-of select="@dniv" />
											</fo:block>
										</fo:table-cell>
										<fo:table-cell>
											<fo:block>
												<xsl:value-of select="@dpor" />
											</fo:block>
										</fo:table-cell>
										<fo:table-cell>
											<fo:block>
												<xsl:value-of select="@invar" />
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<xsl:if test="lots/lot">
										<fo:table-row>
											<fo:table-cell number-columns-spanned="5">
												<xsl:for-each select="lots/lot">
													<fo:block>
														Lot <xsl:value-of select="@lotId" /> - <xsl:value-of select="@numerateur" />/<xsl:value-of select="@denominateur" />
													</fo:block>
												</xsl:for-each>
											</fo:table-cell>
										</fo:table-row>
									</xsl:if>
								</fo:table-body>
							</fo:table>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:block>
								<xsl:value-of select="@ccoeva" />
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:block>
								<xsl:value-of select="@ccoaff" />
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:block>
								<xsl:value-of select="@cconlc" />
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:block>
								<xsl:value-of select="@dcapec" />
								<xsl:value-of select="@ccocac" />
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:table table-layout="fixed">
								<fo:table-column column-width="20%" />
								<fo:table-column column-width="80%" />
								<fo:table-body>
									<fo:table-row>
										<fo:table-cell>
											<fo:block>
												<xsl:if test="number(@communeRevenuImposable) = @communeRevenuImposable">
													C 
												</xsl:if>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell>
											<fo:block>
												<xsl:if test="number(@communeRevenuImposable) = @communeRevenuImposable">
													<xsl:value-of select="format-number(@communeRevenuImposable, '### ##0,00', 'euro')" /> €
												</xsl:if>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>	
									<fo:table-row>
										<fo:table-cell>
											<fo:block>
												<xsl:if test="number(@groupementCommuneRevenuImposable) = @groupementCommuneRevenuImposable">
													GC 
												</xsl:if>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell>
											<fo:block>
												<xsl:if test="number(@groupementCommuneRevenuImposable) = @groupementCommuneRevenuImposable">
													<xsl:value-of select="format-number(@groupementCommuneRevenuImposable, '### ##0,00', 'euro')" /> €
												</xsl:if>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>	
									<fo:table-row>
										<fo:table-cell>	
											<fo:block>
												<xsl:if test="number(@departementRevenuImposable) = @departementRevenuImposable">
													D 
												</xsl:if>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell>	
											<fo:block>
												<xsl:if test="number(@departementRevenuImposable) = @departementRevenuImposable">
													<xsl:value-of select="format-number(@departementRevenuImposable, '### ##0,00', 'euro')" /> €
												</xsl:if>
											</fo:block>
									</fo:table-cell>
									</fo:table-row>	
									<fo:table-row>
										<fo:table-cell>	
											<fo:block>
												<xsl:if test="number(@tseRevenuImposable) = @tseRevenuImposable">
													TSE 
												</xsl:if>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell>	
											<fo:block>
												<xsl:if test="number(@tseRevenuImposable) = @tseRevenuImposable">
													<xsl:value-of select="format-number(@tseRevenuImposable, '### ##0,00', 'euro')" /> €
												</xsl:if>
											</fo:block>
									</fo:table-cell>
									</fo:table-row>	
								</fo:table-body>
							</fo:table>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="bordure" number-columns-spanned="6">
							<fo:table table-layout="fixed">
								<fo:table-column column-width="7%" />
								<fo:table-column column-width="14%" />
								<fo:table-column column-width="22%" />
								<fo:table-column column-width="22%" />
								<fo:table-column column-width="21%" />
								<fo:table-column column-width="14%" />
								<fo:table-body>
									<xsl:for-each select="exonerations/exoneration">
										<fo:table-row>
											<fo:table-cell>
												<fo:block>
													<xsl:value-of select="@ccolloc" />
												</fo:block>
											</fo:table-cell>
											<fo:table-cell>
												<fo:block>
													<xsl:value-of select="@gnextl" />
												</fo:block>
											</fo:table-cell>
											<fo:table-cell>
												<fo:block>
													<xsl:value-of select="@janimp" />
												</fo:block>
											</fo:table-cell>
											<fo:table-cell>
												<fo:block>
													<xsl:value-of select="@jandeb" />
												</fo:block>
											</fo:table-cell>
											<fo:table-cell>
												<fo:block>
													<xsl:if test="number(@fcexn) = @fcexn">
														<xsl:value-of select="format-number(@fcexn, '### ##0,00', 'euro')" /> €
													</xsl:if>
												</fo:block>
											</fo:table-cell>
											<fo:table-cell>
												<fo:block>
													<xsl:value-of select="@pexb" />
												</fo:block>
											</fo:table-cell>
										</fo:table-row>
									</xsl:for-each>
								</fo:table-body>
							</fo:table>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:block>
								<xsl:value-of select="@gtauom" />
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</xsl:for-each>
			</fo:table-body>
		</fo:table>
	</xsl:template>
	
	<!-- Revenus imposable/exonerations sur le bati -->
	<xsl:template name="revenuImposable">
		<fo:table table-layout="fixed" xsl:use-attribute-sets="bordure">
			<fo:table-column column-width="15%" />
			<fo:table-column column-width="7%" />
			<fo:table-column column-width="9%" />
			<fo:table-column column-width="8%" />
			<fo:table-column column-width="9%" />
			<fo:table-column column-width="9%" />
			<fo:table-column column-width="8%" />
			<fo:table-column column-width="9%" />
			<fo:table-column column-width="9%" />
			<fo:table-column column-width="8%" />
			<fo:table-column column-width="9%" />
			<fo:table-body>
				<fo:table-row>
					<fo:table-cell>
						<fo:block padding-top="5pt" text-align="end">
							Revenu imposable :
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block padding-top="5pt" text-align="center">
							<xsl:value-of select="format-number(proprietesBaties/imposition/@revenuImposable, '### ##0,00', 'euro')" /> €
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block padding-top="5pt" text-align="end">
							Commune -
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="end">
						<fo:block>
							Revenu exonéré :
						</fo:block>
						<fo:block>
							Revenu imposable :
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="center">
						<fo:block>
							<xsl:value-of select="format-number(proprietesBaties/imposition/@communeRevenuExonere, '### ##0,00', 'euro')" /> €
						</fo:block>
						<fo:block>
							<xsl:value-of select="format-number(proprietesBaties/imposition/@communeRevenuImposable, '### ##0,00', 'euro')" /> €
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block padding-top="5pt" text-align="end">
							Grp. Com. -
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="end">
						<fo:block>
							Revenu exonéré :
						</fo:block>
						<fo:block>
							Revenu imposable :
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="center">
						<fo:block>
							<xsl:value-of select="format-number(proprietesBaties/imposition/@groupementCommuneRevenuExonere, '### ##0,00', 'euro')" /> €
						</fo:block>
						<fo:block>
							<xsl:value-of select="format-number(proprietesBaties/imposition/@groupementCommuneRevenuImposable, '### ##0,00', 'euro')" /> €
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block padding-top="5pt" text-align="end">
							Département -
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="end">
						<fo:block>
							Revenu exonéré :
						</fo:block>
						<fo:block>
							Revenu imposable :
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="center">
						<fo:block>
							<xsl:value-of select="format-number(proprietesBaties/imposition/@departementRevenuExonere, '### ##0,00', 'euro')" /> €
						</fo:block>
						<fo:block>
							<xsl:value-of select="format-number(proprietesBaties/imposition/@departementRevenuImposable, '### ##0,00', 'euro')" /> €
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
	</xsl:template>
	
	<!-- Liste des propriétés non baties  -->
	<xsl:template name="proprietesNonBaties">
		<fo:block xsl:use-attribute-sets="titre">Propriété(s) non batie(s)</fo:block>
		<fo:table table-layout="fixed">
			<fo:table-column column-width="2%" />
			<fo:table-column column-width="3%" />
			<fo:table-column column-width="3%" /> 
			<fo:table-column column-width="8%" />
			<fo:table-column column-width="4%" />
			<fo:table-column column-width="4%" /> 
			<fo:table-column column-width="4%" />
			<fo:table-column column-width="4%" />
			<fo:table-column column-width="4%" />
			<fo:table-column column-width="9%" />
			<fo:table-column column-width="7%" /> 
			<fo:table-column column-width="4%" />
			<fo:table-column column-width="8%" />
			<fo:table-column column-width="6%" />
			<fo:table-column column-width="8%" />
			<fo:table-column column-width="4%" />
			<fo:table-column column-width="4%" /> 
			<fo:table-column column-width="4%" />
			<fo:table-column column-width="6%" />
			<fo:table-column column-width="4%" />
			<fo:table-header background-color="yellow" font-weight="bold">
				<fo:table-row>
					<fo:table-cell xsl:use-attribute-sets="bordure" number-columns-spanned="5">
						<fo:block>
							Désignation des propriétés
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure" number-columns-spanned="15">
						<fo:block>
							Evaluation
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row background-color="silver">
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
						<fo:block font-size="6pt">
							Adresse
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<fo:block>
							Code Fantoir
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<fo:block>
							N° parc Prim
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<fo:block>
							FP / DP
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<fo:block>
							S.TAR
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<fo:block>
							Suf
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<fo:block>
							GR/SS GR
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<fo:block>
							ref lot
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<fo:block>
							Classe
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<fo:block>
							Nat Cult
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<fo:block>
							Contenance en CA
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<fo:block>
							Base d'imposition
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<fo:block>
							Coll
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<fo:block>
							Nat Exo
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<fo:block>
							AN RET
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<fo:block>
							Fraction RC Exo
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<fo:block>
							% Exo
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-header>
			<fo:table-body>
				<xsl:for-each select="proprietesNonBaties/proprietes/propriete">
					<fo:table-row>
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:block>
								<xsl:value-of select="@jdatat" />
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:block>
								<xsl:value-of select="@ccopre" />
								<xsl:value-of select="@ccosec" />
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:block>
								<xsl:value-of select="@dnupla" />
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:block font-size="6pt">
								<xsl:value-of select="@dnvoiri" /><xsl:text> </xsl:text>
								<xsl:value-of select="@dindic" /><xsl:text> </xsl:text>
								<xsl:value-of select="@dvoilib" />
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:block>
								<xsl:value-of select="@ccoriv" />
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:block>
								<xsl:value-of select="@dparpi" />
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:block>
								<xsl:value-of select="@gparnf" />
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:block>
								<xsl:value-of select="@ccostn" />
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:block>
								<xsl:value-of select="@ccosub" />
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:block>
								<xsl:value-of select="@cgrnum" />
								/
								<xsl:value-of select="@dsgrpf" />
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:block>
								<xsl:value-of select="@dnulot" />
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:block>
								<xsl:value-of select="@dclssf" />
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:block>
								<xsl:value-of select="@cnatsp" />
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:block>
								<xsl:value-of select="@dcntsf" />
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:block>
								<xsl:value-of select="format-number(@drcsuba, '### ##0,00', 'euro')" /> €
							</fo:block>
						</fo:table-cell>
						<fo:table-cell number-columns-spanned="5" xsl:use-attribute-sets="bordure">
							<fo:table table-layout="fixed">
								<fo:table-column column-width="18%" />
								<fo:table-column column-width="18%" />
								<fo:table-column column-width="18%" />
								<fo:table-column column-width="28%" />
								<fo:table-column column-width="18%" />
								<fo:table-body>
									<xsl:choose>
										<xsl:when test="exonerations/exoneration">
											<xsl:for-each select="exonerations/exoneration">
												<fo:table-row>
													<fo:table-cell>
														<fo:block>
															<xsl:value-of select="@ccolloc" />
														</fo:block>
													</fo:table-cell>
													<fo:table-cell>
														<fo:block>
															<xsl:value-of select="@gnextl" />
														</fo:block>
													</fo:table-cell>
													<fo:table-cell>
														<fo:block>
															<xsl:value-of select="@janimp" />
														</fo:block>
													</fo:table-cell>
													<fo:table-cell>
														<fo:block>
															<xsl:value-of select="format-number(@fcexn, '### ##0,00', 'euro')" /> €
														</fo:block>
													</fo:table-cell>
													<fo:table-cell>
														<fo:block>
															<xsl:value-of select="@pexb" />
														</fo:block>
													</fo:table-cell>
												</fo:table-row>
											</xsl:for-each>
										</xsl:when>
										<xsl:otherwise>
											<fo:table-row>
												<fo:table-cell number-columns-spanned="5">
													<fo:block text-align="center">-</fo:block>
												</fo:table-cell>
											</fo:table-row>
										</xsl:otherwise>
									</xsl:choose>
								</fo:table-body>
							</fo:table>
						</fo:table-cell>
					</fo:table-row>
				</xsl:for-each>
			</fo:table-body>
		</fo:table>
	</xsl:template>
	
	<xsl:template name="revenuImposableNonBaties">
		<fo:table table-layout="fixed" xsl:use-attribute-sets="bordure">
			<fo:table-column column-width="7%" />
			<fo:table-column column-width="4%" />
			<fo:table-column column-width="7%" />
			<fo:table-column column-width="8%" />
			<fo:table-column column-width="6%" />
			<fo:table-column column-width="8%" />
			<fo:table-column column-width="8%" />
			<fo:table-column column-width="6%" />
			<fo:table-column column-width="8%" />
			<fo:table-column column-width="8%" />
			<fo:table-column column-width="6%" />
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
						<fo:block padding-top="5pt" text-align="end">
							Revenu imposable :
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block padding-top="5pt">
							<xsl:value-of select="format-number(proprietesNonBaties/imposition/@revenuImposable, '### ##0,00', 'euro')" /> €
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block padding-top="5pt">
							Commune -
						</fo:block>
					</fo:table-cell>
					<fo:table-cell  text-align="end">
						<fo:block>
							Revenu exonéré :
						</fo:block>
						<fo:block>
							Revenu imposable :
						</fo:block>
					</fo:table-cell>
					<fo:table-cell  text-align="center">
						<fo:block>
							<xsl:value-of select="format-number(proprietesNonBaties/imposition/@communeRevenuExonere, '### ##0,00', 'euro')" /> €
						</fo:block>
						<fo:block>
							<xsl:value-of select="format-number(proprietesNonBaties/imposition/@communeRevenuImposable, '### ##0,00', 'euro')" /> €
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block padding-top="5pt">
							Grp. Com -
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="end">
						<fo:block>
							Revenu exonéré :
						</fo:block>
						<fo:block>
							Revenu imposable :
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="center">
						<fo:block>
							<xsl:value-of select="format-number(proprietesNonBaties/imposition/@groupementCommuneRevenuExonere, '### ##0,00', 'euro')" /> €
						</fo:block>
						<fo:block>
							<xsl:value-of select="format-number(proprietesNonBaties/imposition/@groupementCommuneRevenuImposable, '### ##0,00', 'euro')" /> €
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block padding-top="5pt">
							Département -
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="end">
						<fo:block>
							Revenu exonéré :
						</fo:block>
						<fo:block>
							Revenu imposable :
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="center">
						<fo:block>
							<xsl:value-of select="format-number(proprietesNonBaties/imposition/@departementRevenuExonere, '### ##0,00', 'euro')" /> €
						</fo:block>
						<fo:block>
							<xsl:value-of select="format-number(proprietesNonBaties/imposition/@departementRevenuImposable, '### ##0,00', 'euro')" /> €
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block padding-top="5pt">
							MAJ POS : <xsl:value-of select="format-number(proprietesNonBaties/imposition/@majorationTerrain, '### ##0,00', 'euro')" /> €
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
	</xsl:template>
</xsl:stylesheet>
