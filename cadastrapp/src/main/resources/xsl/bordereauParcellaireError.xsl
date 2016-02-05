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

	<!-- Definition des styles -->
	<!-- Format de text simple -->
	<xsl:attribute-set name="text">
		<xsl:attribute name="text-align">left</xsl:attribute>
		<xsl:attribute name="font-size">9pt</xsl:attribute>
	</xsl:attribute-set>

	<xsl:template match="bordereauParcellaire">
	
		<fo:block text-align="start" font-size="8">
			Aucune information pour ces données :
		</fo:block>
		<xsl:for-each select="fields/field">
			<fo:table table-layout="fixed">
				<fo:table-column column-width="30%" />
				<fo:table-body>
					<fo:table-row>
						<fo:table-cell>
							<fo:block xsl:use-attribute-sets="text" >
								<xsl:value-of select="." />
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-body>
			</fo:table>
		</xsl:for-each>

	</xsl:template>

</xsl:stylesheet>