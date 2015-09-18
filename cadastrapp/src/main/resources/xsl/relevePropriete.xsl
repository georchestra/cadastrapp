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
		<xsl:attribute name="font-size">8pt</xsl:attribute>
		<xsl:attribute name="padding-top">2pt</xsl:attribute>
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
		<xsl:for-each select="comptesCommunaux/compteCommunal">
			<xsl:call-template name="entete" />
			<xsl:if test="proprietaires">
				<xsl:call-template name="proprietaire" />
			</xsl:if>
			<xsl:call-template name="proprietesBaties" />
			<xsl:call-template name="revenuImposable" />
			<xsl:call-template name="proprietesNonBaties" />
			<xsl:call-template name="revenuImposableNonBaties" />
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
							<xsl:value-of select="@codeDepartement" />
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block>
							Commune :
							<xsl:value-of select="@codeCommune" />
							<xsl:value-of select="@libelleCommune" />
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block>
							Numéro communal :
							<xsl:value-of select="@compteCommunal" />
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
	</xsl:template>

	<xsl:template name="proprietaire">
		<fo:block xsl:use-attribute-sets="titre">Propriétaire(s)</fo:block>
		<fo:table>
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
							Né(e)
								<xsl:if test="@dateNaissance">	
								<xsl:variable name="dateNaissance">
									<xsl:value-of select="@dateNaissance" />
								</xsl:variable>				
									le <xsl:value-of select="java:format(java:java.text.SimpleDateFormat.new('d MMMM yyyy'), $dateNaissance)" />
								</xsl:if>
								<xsl:if test="@lieuNaissance">
									à <xsl:value-of select="@lieuNaissance" />
								</xsl:if>

							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</xsl:for-each>

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
					<!-- Adresses des propriétés -->
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<xsl:call-template name="designationProprietes" />
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<xsl:call-template name="identificationLocal" />
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<xsl:call-template name="evaluationLocal" />
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
						<xsl:call-template name="designationProprietes" />
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<xsl:call-template name="evaluationLocalNonBatie" />
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<xsl:call-template name="livreFoncier" />
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
	</xsl:template>

	<xsl:template name="designationProprietes">
		<fo:table>
			<fo:table-column column-width="10%" />
			<fo:table-column column-width="20%" />
			<fo:table-column column-width="20%" />
			<fo:table-column column-width="10%" />
			<fo:table-column column-width="20%" />
			<fo:table-column column-width="20%" />
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
							ccosec
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<fo:block>
							dnupla
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
	</xsl:template>

	<xsl:template name="identificationLocal">
		<fo:table>
			<fo:table-column column-width="15%" />
			<fo:table-column column-width="15%" />
			<fo:table-column column-width="15%" />
			<fo:table-column column-width="25%" />
			<fo:table-column column-width="30%" />
			<fo:table-header>
				<fo:table-row>
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<fo:block>
							Bat
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
				</fo:table-row>
			</fo:table-header>
			<fo:table-body>
				<fo:table-row>
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<fo:block>
							Bat
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
							porte
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<fo:block>
							Invar
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
	</xsl:template>

	<xsl:template name="evaluationLocal">
		<fo:table>
			<fo:table-column column-width="5%" />
			<fo:table-column column-width="5%" />
			<fo:table-column column-width="5%" />
			<fo:table-column column-width="10%" />
			<fo:table-column column-width="5%" />
			<fo:table-column column-width="10%" />
			<fo:table-column column-width="5%" />
			<fo:table-column column-width="10%" />
			<fo:table-column column-width="10%" />
			<fo:table-column column-width="10%" />
			<fo:table-column column-width="10%" />
			<fo:table-column column-width="5%" />
			<fo:table-column column-width="5%" />
			<fo:table-column column-width="5%" />
			<fo:table-header>
				<fo:table-row>
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<fo:block>
							S.TAR
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<fo:block>
							M.EVAL
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
							Revenu cadastral
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
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<fo:block>
							Coef
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-header>
			<fo:table-body>
				<fo:table-row>
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<fo:block>
							S.TAR
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<fo:block>
							M.EVAL
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
							Revenu cadastral
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
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<fo:block>
							Coef
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
	</xsl:template>

	<xsl:template name="revenuImposable">
		<fo:block />
		<fo:table xsl:use-attribute-sets="bordure">
			<fo:table-column column-width="20%" />
			<fo:table-column column-width="5%" />
			<fo:table-column column-width="5%" />
			<fo:table-column column-width="10%" />
			<fo:table-column column-width="5%" />
			<fo:table-column column-width="10%" />
			<fo:table-column column-width="5%" />
			<fo:table-column column-width="10%" />
			<fo:table-column column-width="10%" />
			<fo:table-column column-width="10%" />
			<fo:table-column column-width="10%" />
			<fo:table-body>
				<fo:table-row>
					<fo:table-cell>
						<fo:block>
							Revenu imposable
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block>
							valeur
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block>
							Commune
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block>
							R.exo
						</fo:block>
						<fo:block>
							R.imp
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block>
							value r.exo
						</fo:block>
						<fo:block>
							value r.imp
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block>
							Département
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block>
							R.exo
						</fo:block>
						<fo:block>
							R.imp
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block>
							value r.exo
						</fo:block>
						<fo:block>
							value r.imp
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block>
							R.
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block>
							R.exo
						</fo:block>
						<fo:block>
							R.imp
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block>
							value r.exo
						</fo:block>
						<fo:block>
							value r.imp
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
	</xsl:template>


	<xsl:template name="evaluationLocalNonBatie">
		<fo:table>
			<fo:table-column column-width="5%" />
			<fo:table-column column-width="5%" />
			<fo:table-column column-width="5%" />
			<fo:table-column column-width="10%" />
			<fo:table-column column-width="5%" />
			<fo:table-column column-width="10%" />
			<fo:table-column column-width="5%" />
			<fo:table-column column-width="10%" />
			<fo:table-column column-width="10%" />
			<fo:table-column column-width="5%" />
			<fo:table-column column-width="5%" />
			<fo:table-column column-width="5%" />
			<fo:table-column column-width="5%" />
			<fo:table-column column-width="5%" />
			<fo:table-column column-width="5%" />
			<fo:table-column column-width="5%" />
			<fo:table-header>
				<fo:table-row>
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
							Contenance HA A CA
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<fo:block>
							Revenu cadastral
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
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<fo:block>
							pos
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-header>
			<fo:table-body>
				<fo:table-row>
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
							Contenance HA A CA
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<fo:block>
							Revenu cadastral
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
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<fo:block>
							pos
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
	</xsl:template>

	<xsl:template name="livreFoncier">
		<fo:table>
			<fo:table-column column-width="100%" />
			<fo:table-header>
				<fo:table-row>
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<fo:block>
							Feuillet
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-header>
			<fo:table-body>
				<fo:table-row>
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<fo:block>
							value feuillet
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
	</xsl:template>

	<xsl:template name="revenuImposableNonBaties">
		<fo:block />
		<fo:table xsl:use-attribute-sets="bordure">
			<fo:table-column column-width="5%" />
			<fo:table-column column-width="10%" />
			<fo:table-column column-width="10%" />
			<fo:table-column column-width="5%" />
			<fo:table-column column-width="5%" />
			<fo:table-column column-width="5%" />
			<fo:table-column column-width="5%" />
			<fo:table-column column-width="10%" />
			<fo:table-column column-width="5%" />
			<fo:table-column column-width="10%" />
			<fo:table-column column-width="5%" />
			<fo:table-column column-width="10%" />
			<fo:table-column column-width="10%" />
			<fo:table-column column-width="5%" />
			<fo:table-body>
				<fo:table-row>
					<fo:table-cell>
						<fo:block>
							Cont
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block>
							HA a CA
						</fo:block>
						<fo:block>
							value
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block>
							Revenu imposable
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block>
							valeur
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block>
							Commune
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block>
							R.exo
						</fo:block>
						<fo:block>
							R.imp
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block>
							value r.exo
						</fo:block>
						<fo:block>
							value r.imp
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block>
							Département
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block>
							R.exo
						</fo:block>
						<fo:block>
							R.imp
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block>
							value r.exo
						</fo:block>
						<fo:block>
							value r.imp
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block>
							R.
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block>
							R.exo
						</fo:block>
						<fo:block>
							R.imp
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block>
							value r.exo
						</fo:block>
						<fo:block>
							value r.imp
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block>
							MAJ POS
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
	</xsl:template>

</xsl:stylesheet>