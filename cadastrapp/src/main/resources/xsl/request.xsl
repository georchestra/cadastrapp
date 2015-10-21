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
					page-height="29.7cm" page-width="21cm" font-family="sans-serif"
					margin-top="0.5cm" margin-bottom="0.5cm" margin-left="0.5cm"
					margin-right="0.5cm">
					<fo:region-body />
				</fo:simple-page-master>
			</fo:layout-master-set>

			<fo:page-sequence master-reference="portrait">
				<fo:flow flow-name="xsl-region-body">
					<xsl:apply-templates select="informationRequest" />
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


	<!-- template global -->
	<xsl:template match="informationRequest">
		<fo:table>
			<fo:table-column column-width="100%" />
			<fo:table-body>
				<fo:table-row>
					<fo:table-cell>
						<fo:block xsl:use-attribute-sets="bordure">
							Information préalable à
							la consultation ou à la délivrance d'extraits
							d'informations
							cadastrales
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
					<fo:table-cell>
						<fo:block xsl:use-attribute-sets="bordure">
							Vous avez souhaité
							obtenir des informations cadastrales relatives à
							une propriété
							déterminée, à partir de sa localisation (adresse)
							ou de don
							identification cadastrale (n° de parcelle). Les
							informations vous
							seront remises, sous forme papier, par
							l'intermédiaire de l'agent
							municipal habilité à cette fin. Pour
							pouvoir répondre à votre
							demande, nous vous prions de lire et
							compléter le présent
							document.
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
					<fo:table-cell>
						<fo:block xsl:use-attribute-sets="bordure">
							Information sur le demandeur:
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
					<fo:table>
						<fo:table-column column-width="50%" />
						<fo:table-column column-width="50%" />
						<fo:table-row>
							<fo:table-cell>
								<fo:block xsl:use-attribute-sets="bordure">
									Nom : <xsl:value-of select="userRequest/lastName" />
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block xsl:use-attribute-sets="bordure">
									Prénom : <xsl:value-of select="userRequest/firstName" />
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						<fo:table-row>
							<fo:table-cell>
								<fo:block xsl:use-attribute-sets="bordure">
									Adresse : <xsl:value-of select="userRequest/adress" />
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block xsl:use-attribute-sets="bordure">
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						<fo:table-row>
							<fo:table-cell>
								<fo:block xsl:use-attribute-sets="bordure">
									Code Postal : <xsl:value-of select="userRequest/codePostal" />
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block xsl:use-attribute-sets="bordure">
									Ville : <xsl:value-of select="userRequest/commune" />
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</fo:table>
				</fo:table-row>
				<fo:table-row>
					<fo:table-cell>
						<fo:block xsl:use-attribute-sets="bordure">
							Objet de la demande (indiquez l'adresse ou la référence cadastrale de
							la propriété que vous souhaitez consulter)
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
					<fo:table-cell>
						<fo:block xsl:use-attribute-sets="bordure">
							Conditions de communication des informations cadastrales:
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
					<fo:table-cell>
						<fo:block xsl:use-attribute-sets="bordure">
							Si vous êtes propriétaire de la parcelle et que vous avez justifié de cette qualité, ou que vous avez désigné un mandataire qui pourra attester de cette qualité, l'ensemble des données vous concernant peuvent vous être délivrées.
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
					<fo:table-cell>
						<fo:block xsl:use-attribute-sets="bordure">
							En tant que tiers demandeur, vous pouvez avoir communication des références cadastrales et de l'adresse d'un bien, de son évaluation pour la détermination de la taxe foncière (valeur locative), ainsi que des nom, prénom et adresse du ou des propriétaires. Vous ne pouvez pas avoir accès aux date et lieu de naissance du propriétaire, ni aux mentions relatives aux motifs d'exonération fiscale.
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
					<fo:table-cell>
						<fo:block xsl:use-attribute-sets="bordure">
							Conditions de réutilisation des informations cadastrales:
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
					<fo:table-cell>
						<fo:block xsl:use-attribute-sets="bordure">
							La réutilisation des informations cadastrales est soumise, en l'état actuel de la législation, au consentement de la personne concernée (le propriétaire), ou à l'anonymisation préalable des informations par l'autorité détentrice de ces données, conformément à l'article 13 de la loi du 17 juillet 1978 modifiée relative à l'accès aux documents administratifs et à la réutilisation des informations publiques.
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
					<fo:table-cell>
						<fo:block xsl:use-attribute-sets="bordure">
							En outre, tout traitement ultérieur ou constitution d'un fichier comportant des données à caractère personnel est soumis aux dispositions de la loi du 6 janvier 1978 modifiée relative à l'informatique, aux fichiers et aux libertés
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
					<fo:table>
						<fo:table-column column-width="50%" />
						<fo:table-column column-width="50%" />
						<fo:table-row>
							<fo:table-cell>
								<fo:block xsl:use-attribute-sets="bordure">
									Date :
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block xsl:use-attribute-sets="bordure">
									Signature
								</fo:block>
								<fo:block xsl:use-attribute-sets="bordure">
									Précédée de la mention lu et approuvé
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</fo:table>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
	</xsl:template>
	
</xsl:stylesheet>