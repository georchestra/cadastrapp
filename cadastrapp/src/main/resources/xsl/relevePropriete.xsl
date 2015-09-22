<?xml version="1.0" encoding="iso-8859-1"?>
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
	<xsl:attribute-set name="bordure">
		<xsl:attribute name="border">solid 0.1mm black</xsl:attribute>
		<xsl:attribute name="text-align">center</xsl:attribute>
		<xsl:attribute name="font-size">8pt</xsl:attribute>
		<xsl:attribute name="padding-top">2pt</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="bordure-left">
		<xsl:attribute name="border">solid 0.1mm black</xsl:attribute>
		<xsl:attribute name="padding-left">2pt</xsl:attribute>
		<xsl:attribute name="text-align">start</xsl:attribute>
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
			<xsl:if test="proprietesBaties/proprieteBatie">
				<xsl:call-template name="proprietesBaties" />
				<xsl:call-template name="revenuImposable" />
			</xsl:if>
			
			<!-- liste des proprietes non baties d'un compte communal -->
			<xsl:if test="proprietesNonBaties/proprieteNonBatie">
				<xsl:call-template name="proprietesNonBaties" />
				<xsl:call-template name="revenuImposableNonBaties" />
			</xsl:if>
		</xsl:for-each>
	</xsl:template>

	<!-- Cartouche d'entête de chaque relevé, présent uniquement sur la premiere page   -->
	<xsl:template name="entete">
		<fo:block xsl:use-attribute-sets="titre">Relevé de propriété
		</fo:block>
		<fo:table>
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
							Numéro communal :
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
		<fo:table>
			<fo:table-column column-width="34%" />
			<fo:table-column column-width="33%" />
			<fo:table-column column-width="33%" />
			<fo:table-body>

				<xsl:for-each select="proprietaires/proprietaire">
					<fo:table-row>
						<fo:table-cell xsl:use-attribute-sets="bordure-left">
							<fo:block font-size="8">
								<xsl:value-of select="@droitReel" />
							</fo:block>
							<fo:block>
								<xsl:value-of select="@nom" />
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="bordure-left">
							<fo:block padding-top="5pt">
								<xsl:value-of select="@adresse" />
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="bordure-left">
							<fo:block padding-top="5pt">
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
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</xsl:for-each>

			</fo:table-body>
		</fo:table>
	</xsl:template>

	<!-- Liste des propriétés baties -->
	<xsl:template name="proprietesBaties">
		<fo:block xsl:use-attribute-sets="titre">Propriété(s) batie(s)
		</fo:block>
		<fo:table>
			<fo:table-column column-width="20%" />
			<fo:table-column column-width="20%" />
			<fo:table-column column-width="60%" />
			<fo:table-header background-color="yellow" font-weight="bold">
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

	<!-- Liste des propriétés non baties  -->
	<xsl:template name="proprietesNonBaties">
		<fo:block xsl:use-attribute-sets="titre">Propriété(s) non batie(s)
		</fo:block>
		<fo:table>

			<!-- Alsace Moselle Special case add reference to livre foncier -->
			<xsl:choose>
				<xsl:when
					test="starts-with(@codeDepartement, '57') or starts-with(@codeDepartement, '67') or starts-with(@codeDepartement, '68')">
					<fo:table-column column-width="20%" />
					<fo:table-column column-width="65%" />
					<fo:table-column column-width="15%" />
				</xsl:when>
				<xsl:otherwise>
					<fo:table-column column-width="20%" />
					<fo:table-column column-width="80%" />
				</xsl:otherwise>
			</xsl:choose>
			<fo:table-header background-color="yellow"
				font-weight="bold">
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
					<!-- faire un test, uniquement valable pour l'alsace moselle -->
					<!-- Alsace Moselle Special case -->
					<xsl:if
						test="starts-with(@codeDepartement, '57') or starts-with(@codeDepartement, '67') or starts-with(@codeDepartement, '68')">
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:block>
								Livre foncier
							</fo:block>
						</fo:table-cell>
					</xsl:if>
				</fo:table-row>
			</fo:table-header>
			<fo:table-body>
				<fo:table-row>
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<xsl:call-template name="designationProprietesNonBaties" />
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<xsl:call-template name="evaluationLocalNonBatie" />
					</fo:table-cell>
					<xsl:if
						test="starts-with(@codeDepartement, '57') or starts-with(@codeDepartement, '67') or starts-with(@codeDepartement, '68')">
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<xsl:call-template name="livreFoncier" />
						</fo:table-cell>
					</xsl:if>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
	</xsl:template>

	<!-- Designation des proprietes -->
	<xsl:template name="designationProprietes">
		<fo:table>
			<fo:table-column column-width="10%" />
			<fo:table-column column-width="20%" />
			<fo:table-column column-width="15%" />
			<fo:table-column column-width="35%" />
			<fo:table-column column-width="20%" />
			<fo:table-header background-color="silver">
				<fo:table-row height="20pt">
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
							Code Rivoli
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-header>
			<fo:table-body>
				<xsl:for-each select="proprietesBaties/proprieteBatie">
					<fo:table-row height="20pt">
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:block>
								<!--  TODO M -->
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
								<xsl:value-of select="@dvoilib" />
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:block>
								<xsl:value-of select="@ccoriv" />
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</xsl:for-each>
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
			<fo:table-header background-color="silver">
				<fo:table-row height="20pt">
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
				<xsl:for-each select="proprietesBaties/proprieteBatie">
					<fo:table-row height="20pt">
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:block>
								<xsl:value-of select="@dnubat" />
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:block>
								<xsl:value-of select="@descr" />
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:block>
								<xsl:value-of select="@dniv" />
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:block>
								<xsl:value-of select="@dpor" />
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:block>
								<xsl:value-of select="@invar" />
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</xsl:for-each>
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
			<fo:table-header background-color="silver">
				<fo:table-row height="20pt">
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
				<xsl:for-each select="proprietesBaties/proprieteBatie">
					<fo:table-row height="20pt">
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:block>
								<xsl:value-of select="@ccostn" />
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:block>
								<xsl:value-of select="@ccoeva" />
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:block>
								<xsl:value-of select="@ccoafa" />
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:block>
								<xsl:value-of select="@ccoeva" />
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:block>
								<xsl:value-of select="@dcapec" />
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:block>
								<xsl:value-of select="@dvltrt" />
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:block>
								<xsl:value-of select="@ccolloc" />
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:block>
								<!--  TODO Nat Exo -->
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:block>
								<xsl:value-of select="@janret" />
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:block>
								<xsl:value-of select="@janimp" />
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:block>
								<xsl:value-of select="@fcexb" />
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:block>
								<!--  TODO Exo -->
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:block>
								<xsl:value-of select="@mvltieomx" />
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:block>
								<!--  TODO Coef -->
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</xsl:for-each>
			</fo:table-body>
		</fo:table>
	</xsl:template>

	<xsl:template name="revenuImposable">
		<fo:block>&#160;</fo:block>
		<fo:table xsl:use-attribute-sets="bordure">
			<fo:table-column column-width="15%" />
			<fo:table-column column-width="5%" />
			<fo:table-column column-width="10%" />
			<fo:table-column column-width="10%" />
			<fo:table-column column-width="5%" />
			<fo:table-column column-width="10%" />
			<fo:table-column column-width="10%" />
			<fo:table-column column-width="5%" />
			<fo:table-column column-width="10%" />
			<fo:table-column column-width="10%" />
			<fo:table-column column-width="10%" />
			<fo:table-body>
				<fo:table-row>
					<fo:table-cell>
						<fo:block padding-top="5pt" text-align="end">
							Revenu imposable
							totale :
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block padding-top="5pt" text-align="center">
							A définir
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
							r.exo
						</fo:block>
						<fo:block>
							r.imp
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
							value r.exo
						</fo:block>
						<fo:block>
							value r.imp
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block padding-top="5pt" text-align="end">
							Région -
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

	<xsl:template name="designationProprietesNonBaties">
		<fo:table>
			<fo:table-column column-width="10%" />
			<fo:table-column column-width="20%" />
			<fo:table-column column-width="15%" />
			<fo:table-column column-width="35%" />
			<fo:table-column column-width="20%" />
			<fo:table-header background-color="silver">
				<fo:table-row height="20pt">
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
							Code Rivoli
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-header>
			<fo:table-body>
				<xsl:for-each select="proprietesNonBaties/proprieteNonBatie">
					<fo:table-row height="20pt">
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:block>
								<xsl:value-of select="@ccopre" />
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
								<xsl:value-of select="@dvoilib" />
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:block>
								<xsl:value-of select="@ccoriv" />
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</xsl:for-each>
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
			<fo:table-header background-color="silver">
				<fo:table-row height="20pt">
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
				<xsl:for-each select="proprietesNonBaties/proprieteNonBatie">
					<fo:table-row height="20pt">
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:block>
								<xsl:value-of select="@dparpi" />
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:block>
								<!--  TODO FP / DP -->
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
								<xsl:value-of select="@drcsuba" />
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:block>
								<xsl:value-of select="@ccolloc" />
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:block>
								<!-- TODO Nat Exo-->
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:block>
								<xsl:value-of select="@janimp" />
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:block>
								<xsl:value-of select="fcexb" />
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:block>
								<!-- TODO % Exo-->
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:block>
								<!--  TODO pos -->
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</xsl:for-each>
			</fo:table-body>
		</fo:table>
	</xsl:template>

	<xsl:template name="livreFoncier">
		<fo:table>
			<fo:table-column column-width="100%" />
			<fo:table-header background-color="silver">
				<fo:table-row height="20pt">
					<fo:table-cell xsl:use-attribute-sets="bordure">
						<fo:block>
							Feuillet
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-header>
			<fo:table-body>
				<xsl:for-each select="proprietesNonBaties/proprieteNonBatie">
					<fo:table-row height="20pt">
						<fo:table-cell xsl:use-attribute-sets="bordure">
							<fo:block>
								value feuillet
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</xsl:for-each>
			</fo:table-body>
		</fo:table>
	</xsl:template>

	<xsl:template name="revenuImposableNonBaties">
		<fo:block>&#160;</fo:block>
		<fo:table xsl:use-attribute-sets="bordure">
			<fo:table-column column-width="8%" />
			<fo:table-column column-width="7%" />
			<fo:table-column column-width="15%" />
			<fo:table-column column-width="5%" />
			<fo:table-column column-width="5%" />
			<fo:table-column column-width="10%" />
			<fo:table-column column-width="5%" />
			<fo:table-column column-width="5%" />
			<fo:table-column column-width="10%" />
			<fo:table-column column-width="5%" />
			<fo:table-column column-width="5%" />
			<fo:table-column column-width="10%" />
			<fo:table-column column-width="5%" />
			<fo:table-column column-width="5%" />
			<fo:table-body>
				<fo:table-row>
					<fo:table-cell>
						<fo:block padding-top="5pt">
							Surface Totale :
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
						<fo:block padding-top="5pt" text-align="end">
							Revenu imposable
							totale :
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block padding-top="5pt">
							valeur
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block padding-top="5pt">
							Commune -
						</fo:block>
					</fo:table-cell>
					<fo:table-cell  text-align="end">
						<fo:block>
							Revenu éxonéré :
						</fo:block>
						<fo:block>
							Revenu imposable :
						</fo:block>
					</fo:table-cell>
					<fo:table-cell  text-align="center">
						<fo:block>
							r.exo
						</fo:block>
						<fo:block>
							r.imp
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block padding-top="5pt">
							Département -
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="end">
						<fo:block>
							Revenu éxonéré :
						</fo:block>
						<fo:block>
							Revenu imposable :
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="center">
						<fo:block>
							r.exo
						</fo:block>
						<fo:block>
							r.imp
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block padding-top="5pt">
							Région -
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
							r.exo
						</fo:block>
						<fo:block>
							r.imp
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block padding-top="5pt">
							MAJ POS
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
	</xsl:template>

</xsl:stylesheet>