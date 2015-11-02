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
		<xsl:attribute name="padding-top">10pt</xsl:attribute>
		<xsl:attribute name="padding-bottom">20pt</xsl:attribute>
		<xsl:attribute name="font-size">16pt</xsl:attribute>
		<xsl:attribute name="font-weight">bold</xsl:attribute>
	</xsl:attribute-set>

	<!-- Style Titre -->
	<xsl:attribute-set name="soustitre">
		<xsl:attribute name="text-align">left</xsl:attribute>
		<xsl:attribute name="padding-top">5pt</xsl:attribute>
		<xsl:attribute name="font-size">12pt</xsl:attribute>
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
	</xsl:attribute-set>



	<!-- Global template with the two same pages -->
	<xsl:template match="informationRequest">
		<xsl:call-template name="globalpage" />
	</xsl:template>

	<xsl:template name="checkbox">
		<fo:inline font-family="ZapfDingbats" font-size="10pt">&#x274F;</fo:inline>
	</xsl:template>

	<xsl:template name="informationDemandeur">
		<fo:block xsl:use-attribute-sets="soustitre">
			Identification du demandeur
		</fo:block>
		<fo:block xsl:use-attribute-sets="bordure">
			<fo:table>
				<fo:table-column column-width="50%" />
				<fo:table-column column-width="50%" />
				<fo:table-body>
					<fo:table-row>
						<fo:table-cell>
							<fo:block xsl:use-attribute-sets="text">
								<xsl:if test="userRequest/@type='A'">
									Administration
								</xsl:if>
								<xsl:if test="userRequest/@type='P1'">
									Particulier détenteur des droits
								</xsl:if>
								<xsl:if test="userRequest/@type='P2'">
									Particulier agissant en qualité de mandataire
								</xsl:if>
								<xsl:if test="userRequest/@type='P3'">
									Particulier tiers
								</xsl:if>
								<fo:block>
									<xsl:value-of select="userRequest/@firstName" /> <xsl:value-of select="userRequest/@lastName" />
								</fo:block>
								<fo:block>
									<xsl:value-of select="userRequest/@adress" />
								</fo:block>
								<fo:block>
									<xsl:value-of select="userRequest/@codePostal" />
									<xsl:value-of select="userRequest/@commune" />
								</fo:block>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell>
							<fo:block xsl:use-attribute-sets="text">
								Demande :
								<xsl:if test="@askby='1'">
									guichet
								</xsl:if>
								<xsl:if test="@askby='2'">
									courrier
								</xsl:if>
								<xsl:if test="@askby='3'">
									courriel
								</xsl:if>
								Transmission des documents :
								<xsl:if test="@responseby='1'">
									guichet
								</xsl:if>
								<xsl:if test="@responseby='2'">
									courrier
								</xsl:if>
								<xsl:if test="@responseby='3'">
									courriel
								</xsl:if>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-body>
			</fo:table>
		</fo:block>
	</xsl:template>

	<xsl:template name="informationDemande">
		<fo:block xsl:use-attribute-sets="soustitre">
			Objet de la demande
		</fo:block>

		<fo:table>
			<fo:table-column column-width="10%" />
			<fo:table-column column-width="90%" />
			<fo:table-body>
				<fo:table-row>
					<fo:table-cell>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block xsl:use-attribute-sets="bordure">
							<fo:block xsl:use-attribute-sets="text">
								<xsl:for-each select="comptecommnuaux/comptecommunal">
									Propriétaire <xsl:value-of select="text()" />
								</xsl:for-each>
							</fo:block>
							<fo:block xsl:use-attribute-sets="text">
								<xsl:for-each select="parcelles/parcelle">
									Parcelle <xsl:value-of select="text()" />
								</xsl:for-each>
							</fo:block>
							<fo:block xsl:use-attribute-sets="text">
								<xsl:for-each select="coproprietes/copropriete">
									Lot de copropriété <xsl:value-of select="text()" />
								</xsl:for-each>
							</fo:block>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
	</xsl:template>

	<xsl:template name="signatureDemandeur">
		<fo:table>
			<fo:table-column column-width="50%" />
			<fo:table-column column-width="50%" />
			<fo:table-body>
				<fo:table-row>
					<fo:table-cell>
						<fo:block xsl:use-attribute-sets="text">
							<xsl:call-template name="checkbox" />
							J'ai pris connaissance de mes droits et obligations
						</fo:block>
						<fo:block xsl:use-attribute-sets="text">
							<xsl:call-template name="checkbox" />
							Les informations ci-dessous sont correctes
						</fo:block>
						<fo:block xsl:use-attribute-sets="text-bold">
							Signature :
						</fo:block>
						<fo:block xsl:use-attribute-sets="text-bold">
							Le :
							<xsl:value-of select="@requestDate" />
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block xsl:use-attribute-sets="text-bold">
							Le fonctionnaire
							territorial habilité :
						</fo:block>
						<!-- TODO take information from user session -->
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
	</xsl:template>

	<xsl:template name="globalpage">

		<fo:block xsl:use-attribute-sets="bordure">
			<fo:block xsl:use-attribute-sets="titre" page-break-before="always">
				Délivrance d'informations cadastrales au public
			</fo:block>

			<fo:block xsl:use-attribute-sets="soustitre">
				Rappel de la réglementation
			</fo:block>

			<fo:block xsl:use-attribute-sets="text">
				Les articles R* 107 A-1 et
				suivants du livre des procédures fiscales fixent les modalités de
				communication des informations fiscales aux contribuables.
				L'article
				5
				de la délibération 2012-088 prise par la Commission nationale de
				l'informatique et des libertés répertorie les informations
				auxquelles
				vous avez accès selon que vous êtes titulaire de droits
				réels
				immobiliers, mandataire du titulaire de droits réels
				immobiliers ou
				un tiers demandeur.

			</fo:block>

			<fo:table>
				<fo:table-column column-width="30%" />
				<fo:table-column column-width="10%" />
				<fo:table-column column-width="60%" />
				<fo:table-body>
					<fo:table-row>
						<fo:table-cell>
							<fo:block>Titulaire ou mandataire</fo:block>
						</fo:table-cell>
						<fo:table-cell>
							<fo:block></fo:block>
						</fo:table-cell>
						<fo:table-cell>
							<fo:block>Tiers demandeur</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row>
						<fo:table-cell>
							<fo:block xsl:use-attribute-sets="text-bold">Vos Droits</fo:block>
						</fo:table-cell>
						<fo:table-cell>
							<fo:block></fo:block>
						</fo:table-cell>
						<fo:table-cell>
							<fo:block></fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row>
						<fo:table-cell  xsl:use-attribute-sets="text">
							<fo:block>Accès à l'ensemble des informations contenues dans la
								matrice cadastrale sous la forme d'un relevé de propriété et/ou
								d'un bordereau parcellaire.</fo:block>
						</fo:table-cell>
						<fo:table-cell>
							<fo:block></fo:block>
						</fo:table-cell>
						<fo:table-cell>
							<fo:block  xsl:use-attribute-sets="text">Accès aux seules informations suivantes : « les
								références cadastrales [...] ; l'évaluation du bien pour la
								détermination de sa base d'imposition à la taxe foncière ainsi
								que les nom, prénom et adresse du ou des propriétaires, à
								l'exclusion de toute autre information touchant au secret de la
								vie privée, en particulier les date et lieu de naissance du
								propriétaire ou les éléments liés au calcul de l'impôt. »</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row>
						<fo:table-cell>
							<fo:block xsl:use-attribute-sets="text-bold">Vos obligations</fo:block>
						</fo:table-cell>
						<fo:table-cell>
							<fo:block></fo:block>
						</fo:table-cell>
						<fo:table-cell>
							<fo:block></fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row>
						<fo:table-cell>
							<fo:block  xsl:use-attribute-sets="text">Sans objet.</fo:block>
						</fo:table-cell>
						<fo:table-cell>
							<fo:block></fo:block>
						</fo:table-cell>
						<fo:table-cell>
							<fo:block  xsl:use-attribute-sets="text">Les informations transmises ce jour le sont pour votre
								usage strictement personnel. Vous ne pouvez pas les communiquer
								à
								des tiers.

								Les informations cadastrales communiquées ne peuvent
								faire l'objet
								d'une réutilisation que si la personne intéressée y
								a consenti ou
								si l'autorité détentrice est en mesure de les
								rendre
								anonymes, ou
								à défaut d'anonymisation, si une disposition
								législative ou
								réglementaire le permet, conformément aux
								conditions fixées par
								l'article 13 de la loi du 17 juillet 1978
								susvisée. La
								réutilisation des informations comportant des
								données
								à caractère
								personnel est également subordonnée au respect
								des
								dispositions
								de la loi du 6 janvier 1978 modifiée.
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-body>
			</fo:table>
		</fo:block>

		<fo:block xsl:use-attribute-sets="bordure">
			<xsl:call-template name="informationDemandeur" />

			<xsl:call-template name="informationDemande" />

			<xsl:call-template name="signatureDemandeur" />

			<fo:block>Information relative au recueil de données à caractère
				personnel</fo:block>
			<fo:block  xsl:use-attribute-sets="text">L'identité du demandeur fait l'objet d'un recueil et d'un
				traitement informatique. Ces informations sont stockées durant une
				période maximale de 2 mois afin de satisfaire aux obligations
				légales
				en matière de contrôle de la délivrance de ces informations
				cadastrales. Au-delà de cette période ces informations sont
				détruites. L'administration ne conserve qu'une information anonyme à
				but statistique pour l'évaluation du service fourni à l'administré.</fo:block>
		</fo:block>


	</xsl:template>

</xsl:stylesheet>