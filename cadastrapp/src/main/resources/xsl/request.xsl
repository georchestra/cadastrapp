<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:java="http://xml.apache.org/xslt/java" xmlns:date="http://exslt.org/dates-and-times"
	exclude-result-prefixes="java" xmlns:svg="http://www.w3.org/2000/svg">

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

	<!-- Style Titre -->
	<xsl:attribute-set name="titre">
		<xsl:attribute name="text-align">center</xsl:attribute>
		<xsl:attribute name="padding-top">5pt</xsl:attribute>
		<xsl:attribute name="font-size">12pt</xsl:attribute>
		<xsl:attribute name="font-weight">bold</xsl:attribute>
	</xsl:attribute-set>

	<!-- Style Titre -->
	<xsl:attribute-set name="soustitre">
		<xsl:attribute name="text-align">left</xsl:attribute>
		<xsl:attribute name="padding-top">5pt</xsl:attribute>
		<xsl:attribute name="font-size">10pt</xsl:attribute>
		<xsl:attribute name="font-weight">bold</xsl:attribute>
		<xsl:attribute name="padding-left">4pt</xsl:attribute>
	</xsl:attribute-set>

	<!-- texte normal -->
	<xsl:attribute-set name="text">
		<xsl:attribute name="text-align">left</xsl:attribute>
		<xsl:attribute name="font-size">8pt</xsl:attribute>
		<xsl:attribute name="padding-top">10pt</xsl:attribute>
		<xsl:attribute name="padding-left">4pt</xsl:attribute>
	</xsl:attribute-set>

	<!-- texte normal -->
	<xsl:attribute-set name="text-bold">
		<xsl:attribute name="text-align">left</xsl:attribute>
		<xsl:attribute name="font-size">9pt</xsl:attribute>
		<xsl:attribute name="padding-top">10pt</xsl:attribute>
		<xsl:attribute name="font-weight">bold</xsl:attribute>
		<xsl:attribute name="padding-left">4pt</xsl:attribute>
	</xsl:attribute-set>

	<!-- Bordure de tableau -->
	<xsl:attribute-set name="bordure">
		<xsl:attribute name="border">solid 0.1mm black</xsl:attribute>
		<xsl:attribute name="text-align">center</xsl:attribute>
		<xsl:attribute name="font-size">8pt</xsl:attribute>
		<xsl:attribute name="padding-top">2pt</xsl:attribute>
	</xsl:attribute-set>



	<!-- Global template with the two same pages -->
	<xsl:template match="informationRequest">
		<xsl:call-template name="globalpage" />
		<xsl:call-template name="globalpage" />
	</xsl:template>

	<xsl:template name="checkbox">
		<fo:inline font-family="ZapfDingbats" font-size="10pt">&#x274F;</fo:inline>
	</xsl:template>

	<xsl:template name="informationDemandeur">
		<fo:block xsl:use-attribute-sets="bordure">
			<fo:table>
				<fo:table-column column-width="50%" />
				<fo:table-column column-width="50%" />
				<fo:table-body>
					<fo:table-row>
						<fo:table-cell>
							<fo:block xsl:use-attribute-sets="text">
								Nom :
								<xsl:value-of select="userRequest/@lastName" />
							</fo:block>
						</fo:table-cell>
						<fo:table-cell>
							<fo:block xsl:use-attribute-sets="text">
								Prénom :
								<xsl:value-of select="userRequest/@firstName" />
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row>
						<fo:table-cell>
							<fo:block xsl:use-attribute-sets="text">
								Adresse :
								<xsl:value-of select="userRequest/@adress" />
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row>
						<fo:table-cell>
							<fo:block xsl:use-attribute-sets="text">
								Code Postal :
								<xsl:value-of select="userRequest/@codepostal" />
							</fo:block>
						</fo:table-cell>
						<fo:table-cell>
							<fo:block xsl:use-attribute-sets="text">
								Ville :
								<xsl:value-of select="userRequest/@commune" />
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-body>
			</fo:table>
		</fo:block>
	</xsl:template>

	<xsl:template name="numeroDeDemande">
		<fo:block xsl:use-attribute-sets="text">
			Demande numéro :
			<xsl:value-of select="@requestId" />
		</fo:block>
	</xsl:template>

	<xsl:template name="informationDemande">
		<fo:block xsl:use-attribute-sets="bordure">
			<fo:block xsl:use-attribute-sets="text">
				Compte commnual :
				<xsl:value-of select="@comptecommunal" />
			</fo:block>
			<fo:block xsl:use-attribute-sets="text">
				Identifiant(s) de parcelle(s) :
				<xsl:for-each select="parcelles/parcelle">
					<xsl:value-of select="@parcelle" />
				</xsl:for-each>
			</fo:block>
		</fo:block>
	</xsl:template>

	<xsl:template name="signatureDemandeur">
		<fo:table>
			<fo:table-column column-width="50%" />
			<fo:table-column column-width="50%" />
			<fo:table-body>
				<fo:table-row>
					<fo:table-cell>
						<fo:block xsl:use-attribute-sets="text-bold">
							Date :
							<xsl:value-of select="@requestDate" />
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block xsl:use-attribute-sets="text-bold">
							Signature :
						</fo:block>
						<fo:block xsl:use-attribute-sets="text">
							Précédée de la mention
							"lu et approuvé""
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
	</xsl:template>


	<xsl:template name="typedinformation">

		<fo:block xsl:use-attribute-sets="soustitre">
			<fo:inline font-style="italic" font-family="serif"
				text-decoration="underline">
				Informations transmises :
			</fo:inline>
		</fo:block>
		<fo:block xsl:use-attribute-sets="bordure">
			<fo:table>
				<fo:table-column column-width="50%" />
				<fo:table-column column-width="50%" />
				<fo:table-body>
					<fo:table-row>
						<fo:table-cell>
							<fo:block xsl:use-attribute-sets="text">
								<xsl:call-template name="checkbox" />
								références cadastrales
							</fo:block>
						</fo:table-cell>
						<fo:table-cell>
							<fo:block xsl:use-attribute-sets="text">
							<xsl:call-template name="checkbox" />
								nom et prénom du ou des
								propriétaire(s)
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row>
						<fo:table-cell>
							<fo:block xsl:use-attribute-sets="text">
							<xsl:call-template name="checkbox" />
							superficie de la parcelle</fo:block>
						</fo:table-cell>
						<fo:table-cell>
							<fo:block xsl:use-attribute-sets="text">
							<xsl:call-template name="checkbox" />
							adresse du ou des propriétaire(s)</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row>
						<fo:table-cell>
							<fo:block xsl:use-attribute-sets="text">
							<xsl:call-template name="checkbox" />adresse du bien</fo:block>
						</fo:table-cell>
						<fo:table-cell>
							<fo:block xsl:use-attribute-sets="text">
							<xsl:call-template name="checkbox" />
								date et lieu de
								naissance du ou des propriétaire(s)</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row>
						<fo:table-cell>
							<fo:block xsl:use-attribute-sets="text">
							<xsl:call-template name="checkbox" />
							valeur locative</fo:block>
						</fo:table-cell>
						<fo:table-cell>
							<fo:block xsl:use-attribute-sets="text">
							<xsl:call-template name="checkbox" />
							relevé de propriété	complet</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row>
						<fo:table-cell>
							<fo:block xsl:use-attribute-sets="text">
							<xsl:call-template name="checkbox" />
								liste des parcelles
								contiguës avec les références
								cadastrales, les surfaces et les
								nom, prénom adresse du ou
								des propriétaire(s)</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row>
						<fo:table-cell>
							<fo:block xsl:use-attribute-sets="soustitre" padding-bottom="30pt">
								<fo:inline font-style="italic" font-family="serif"
									text-decoration="underline" padding-bottom="15mm">
									Autres :
								</fo:inline>

							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-body>
			</fo:table>
		</fo:block>

	</xsl:template>

	<xsl:template name="globalpage">
		<fo:block xsl:use-attribute-sets="titre" page-break-before="always">
			Information préalable à la consultation ou à la délivrance d'extraits
			d'informations cadastrales
		</fo:block>

		<fo:block xsl:use-attribute-sets="text">
			Vous avez souhaité
			obtenir
			des informations cadastrales relatives à
			une propriété
			déterminée, à
			partir de son identification cadastrale
			(n° de parcelle) ou du compte
			commnunal de son propriétaires.
			Les informations vous
			seront remises,
			sous forme
			papier, par
			l'intermédiaire de l'agent
			municipal habilité à
			cette fin.
			Pour
			pouvoir répondre à votre
			demande, nous vous relire et
			compléter le présent
			document.
		</fo:block>

		<fo:block xsl:use-attribute-sets="soustitre">
			<fo:inline font-style="italic" font-family="serif"
				text-decoration="underline">
				Information sur le
				demandeur:
			</fo:inline>
		</fo:block>

		<xsl:call-template name="informationDemandeur" />

		<fo:block xsl:use-attribute-sets="soustitre">
			<fo:inline font-style="italic" font-family="serif"
				text-decoration="underline">
				Objet de la demande :
			</fo:inline>
		</fo:block>

		<xsl:call-template name="informationDemande" />

		<fo:block xsl:use-attribute-sets="soustitre">
			<fo:inline font-style="italic" font-family="serif"
				text-decoration="underline">
				Conditions de communication
				des informations
				cadastrales :
			</fo:inline>
		</fo:block>

		<fo:block xsl:use-attribute-sets="text">
			Si vous êtes propriétaire de
			la parcelle et que vous avez justifié de
			cette qualité, ou que vous
			avez désigné un mandataire qui pourra
			attester de cette qualité,
			l'ensemble des données vous concernant
			peuvent vous être délivrées.
		</fo:block>

		<fo:block xsl:use-attribute-sets="text">
			En tant que tiers demandeur,
			vous pouvez avoir communication des
			références cadastrales et de
			l'adresse d'un bien, de son évaluation
			pour la détermination de la
			taxe foncière (valeur locative), ainsi
			que des nom, prénom et adresse
			du ou des propriétaires. Vous ne
			pouvez pas avoir accès aux date et
			lieu de naissance du propriétaire,
			ni aux mentions relatives aux
			motifs d'exonération fiscale.
		</fo:block>

		<fo:block xsl:use-attribute-sets="soustitre">
			<fo:inline font-style="italic" font-family="serif"
				text-decoration="underline">
				Conditions de réutilisation
				des informations
				cadastrales :
			</fo:inline>
		</fo:block>

		<fo:block xsl:use-attribute-sets="text">
			La réutilisation des
			informations cadastrales est soumise, en l'état
			actuel de la
			législation, au consentement de la personne concernée
			(le
			propriétaire), ou à l'anonymisation préalable des informations
			par
			l'autorité détentrice de ces données, conformément à l'article 13
			de
			la loi du 17 juillet 1978 modifiée relative à l'accès aux
			documents
			administratifs et à la réutilisation des informations
			publiques.
		</fo:block>

		<fo:block xsl:use-attribute-sets="text">
			En outre, tout traitement
			ultérieur ou constitution d'un fichier
			comportant des données à
			caractère personnel est soumis aux
			dispositions de la loi du 6 janvier
			1978 modifiée relative à
			l'informatique, aux fichiers et aux libertés
		</fo:block>

		<xsl:call-template name="typedinformation" />

		<xsl:call-template name="signatureDemandeur" />
	</xsl:template>

</xsl:stylesheet>