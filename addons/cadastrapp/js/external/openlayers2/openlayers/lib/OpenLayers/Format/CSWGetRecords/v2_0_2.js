/* Copyright (c) 2006-2013 by OpenLayers Contributors (see authors.txt for
 * full list of contributors). Published under the 2-clause BSD license.
 * See license.txt in the OpenLayers distribution or repository for the
 * full text of the license. */

/**
 * @requires OpenLayers/Format/XML.js
 * @requires OpenLayers/Format/CSWGetRecords.js
 * @requires OpenLayers/Format/Filter/v1_0_0.js
 * @requires OpenLayers/Format/Filter/v1_1_0.js
 * @requires OpenLayers/Format/OWSCommon/v1_0_0.js
 */

/**
 * Class: OpenLayers.Format.CSWGetRecords.v2_0_2
 *     A format for creating CSWGetRecords v2.0.2 transactions. 
 *     Create a new instance with the
 *     <OpenLayers.Format.CSWGetRecords.v2_0_2> constructor.
 *
 * Inherits from:
 *  - <OpenLayers.Format.XML>
 */
OpenLayers.Format.CSWGetRecords.v2_0_2 = OpenLayers.Class(OpenLayers.Format.XML, {
    
    /**
     * Property: namespaces
     * {Object} Mapping of namespace aliases to namespace URIs.
     */
    namespaces: {
        csw: "http://www.opengis.net/cat/csw/2.0.2",
        dc: "http://purl.org/dc/elements/1.1/",
        dct: "http://purl.org/dc/terms/",
        gco: "http://www.isotc211.org/2005/gco",
        geonet: "http://www.fao.org/geonetwork",
        gmd: "http://www.isotc211.org/2005/gmd",
        gml: "http://www.opengis.net/gml",
        ogc: "http://www.opengis.net/ogc",
        ows: "http://www.opengis.net/ows",
        srv: "http://www.isotc211.org/2005/srv",
        xlink: "http://www.w3.org/1999/xlink",
        xsi: "http://www.w3.org/2001/XMLSchema-instance"
    },
    
    /**
     * Property: defaultPrefix
     * {String} The default prefix (used by Format.XML).
     */
    defaultPrefix: "csw",
    
    /**
     * Property: version
     * {String} CSW version number.
     */
    version: "2.0.2",
    
    /**
     * Property: schemaLocation
     * {String} http://www.opengis.net/cat/csw/2.0.2
     *   http://schemas.opengis.net/csw/2.0.2/CSW-discovery.xsd
     */
    schemaLocation: "http://www.opengis.net/cat/csw/2.0.2 http://schemas.opengis.net/csw/2.0.2/CSW-discovery.xsd",

    /**
     * APIProperty: requestId
     * {String} Value of the requestId attribute of the GetRecords element.
     */
    requestId: null,

    /**
     * APIProperty: resultType
     * {String} Value of the resultType attribute of the GetRecords element,
     *     specifies the result type in the GetRecords response, "hits" is
     *     the default.
     */
    resultType: null,

    /**
     * APIProperty: outputFormat
     * {String} Value of the outputFormat attribute of the GetRecords element,
     *     specifies the format of the GetRecords response,
     *     "application/xml" is the default.
     */
    outputFormat: null,

    /**
     * APIProperty: outputSchema
     * {String} Value of the outputSchema attribute of the GetRecords element,
     *     specifies the schema of the GetRecords response.
     */
    outputSchema: null,

    /**
     * APIProperty: startPosition
     * {String} Value of the startPosition attribute of the GetRecords element,
     *     specifies the start position (offset+1) for the GetRecords response,
     *     1 is the default.
     */
    startPosition: null,

    /**
     * APIProperty: maxRecords
     * {String} Value of the maxRecords attribute of the GetRecords element,
     *     specifies the maximum number of records in the GetRecords response,
     *     10 is the default.
     */
    maxRecords: null,

    /**
     * APIProperty: DistributedSearch
     * {String} Value of the csw:DistributedSearch element, used when writing
     *     a csw:GetRecords document.
     */
    DistributedSearch: null,

    /**
     * APIProperty: ResponseHandler
     * {Array({String})} Values of the csw:ResponseHandler elements, used when
     *     writting a csw:GetRecords document.
     */
    ResponseHandler: null,

    /**
     * APIProperty: Query
     * {String} Value of the csw:Query element, used when writing a csw:GetRecords
     *     document.
     */
    Query: null,

    /**
     * Property: regExes
     * Compiled regular expressions for manipulating strings.
     */
    regExes: {
        trimSpace: (/^\s*|\s*$/g),
        removeSpace: (/\s*/g),
        splitSpace: (/\s+/),
        trimComma: (/\s*,\s*/g)
    },
    
    parsers: {
        "gmd": {
            // readAttributes
            "CI_RoleCode": "readAttributes",
            "MD_ProgressCode": "readAttributes",
            "MD_KeywordTypeCode": "readAttributes",
            "MD_RestrictionCode": "readAttributes",
            "MD_SpatialRepresentationTypeCode": "readAttributes",
            "MD_CharacterSetCode": "readAttributes",
            "CI_DateTypeCode": "readAttributes",
            "MD_TopologyLevelCode": "readAttributes",
            "MD_GeometricObjectTypeCode": "readAttributes",
            "MD_DimensionNameTypeCode": "readAttributes",
            "MD_CellGeometryCode": "readAttributes",
            "MD_MaintenanceFrequencyCode": "readAttributes",
            "MD_ScopeCode": "readAttributes",
            // pass
            "CI_ResponsibleParty": "pass",
            "CI_Contact": "pass",
            "CI_Telephone": "pass",
            "CI_Address": "pass",
            "MD_DataIdentification": "pass",
            "MD_Keywords": "pass",
            "MD_Constraints": "pass",
            "MD_LegalConstraints": "pass",
            "MD_Resolution": "pass",
            "MD_RepresentativeFraction": "pass",
            "EX_Extent": "pass",
            "EX_GeographicBoundingBox": "pass",
            "EX_TemporalExtent": "pass",
            "EX_GeographicDescription": "pass",
            "MD_Distribution": "pass",
            "MD_Distributor": "pass",
            "MD_DigitalTransferOptions": "pass",
            "MD_Format": "pass",
            "MD_Medium": "pass",
            "CI_OnlineResource": "pass",
            "MD_ReferenceSystem": "pass",
            "RS_Identifier": "pass",
            "CI_Citation": "pass",
            "CI_Date": "pass",
            "MD_GridSpatialRepresentation": "pass",
            "MD_VectorSpatialRepresentation": "pass",
            "MD_GeometricObjects": "pass",
            "MD_Dimension": "pass",
            "MD_MaintenanceInformation": "pass",
            "MD_ScopeDescription": "pass",
            "MD_BrowseGraphic": "pass",
            "DQ_DataQuality": "pass",
            "DQ_Scope": "pass",
            "DQ_Element": "pass",
            "LI_Lineage": "pass",
            "LI_Source":"pass",
            "LI_ProcessStep":"pass",
            "MD_ReferenceSystem":"pass",
            "PT_FreeText": "pass",
            "MD_Identifier": "pass",
            "MD_Georectified": "pass",
            // createManyChildren
            "contact": "createManyChildren",
            "voice": "createManyChildren",
            "facsimile": "createManyChildren",
            "deliveryPoint": "createManyChildren",
            "electronicMailAddress": "createManyChildren",
            "identificationInfo": "createManyChildren",
            "pointOfContact": "createManyChildren",
            "status": "createManyChildren",
            "descriptiveKeywords": "createManyChildren",
            "keyword": "createManyChildren",
            "resourceConstraints": "createManyChildren",
            "useLimitation": "createManyChildren",
            "accessConstraints": "createManyChildren",
            "useConstraints": "createManyChildren",
            "otherConstraints": "createManyChildren",
            "spatialResolution": "createManyChildren",
            "spatialRepresentationType": "createManyChildren",
            "language": "createManyChildren",
            "characterSet": "createManyChildren",
            "topicCategory": "createManyChildren",
            "geographicElement": "createManyChildren",
            "temporalElement": "createManyChildren",
            "distributor": "createManyChildren",
            "transferOptions": "createManyChildren",
            "distributionFormat": "createManyChildren",
            "distributionOrderProcess": "createManyChildren",
            "distributorFormat": "createManyChildren",
            "formatDistributor": "createManyChildren",
            "distributorTransferOptions": "createManyChildren",
            "onLine": "createManyChildren",
            "referenceSystemInfo": "createManyChildren",
            "alternateTitle": "createManyChildren",
            "date": "createManyChildren",
            "identifier": "createManyChildren",
            "citedResponsibleParty": "createManyChildren",
            "presentationForm": "createManyChildren",
            "spatialRepresentationInfo": "createManyChildren",
            "geometricObjects": "createManyChildren",
            "axisDimensionProperties": "createManyChildren",
            "resourceMaintenance": "createManyChildren",
            "updateScope": "createManyChildren",
            "updateScopeDescription": "createManyChildren",
            "maintenanceNote": "createManyChildren",
            "attributes": "createManyChildren",
            "features": "createManyChildren",
            "featureInstances": "createManyChildren",
            "attributeInstances": "createManyChildren",
            "graphicOverview": "createManyChildren",
            "dataQualityInfo": "createManyChildren",
            "report": "createManyChildren",
            "levelDescription":"createManyChildren",
            "processStep":"createManyChildren",
            "source":"createManyChildren",
            "sourceExtent":"createManyChildren",
            "sourceStep":"createManyChildren",
            "processor":"createManyChildren",
            "textGroup": "createManyChildren",
            "geographicElement": "createManyChildren",
            // createOneChild
            "dateTime":"createOneChild",
            "rationale":"createOneChild",
            "description":"createOneChild",
            "statement":"createOneChild",
            "scaleDenominator":"createOneChild",
            "sourceReferenceSystem":"createOneChild",
            "sourceCitation":"createOneChild",
            "level": "createOneChild",
            "scope": "createOneChild",
            "lineage": "createOneChild",
            "fileName":"createOneChild",
            "fileDescription":"createOneChild",
            "fileType":"createOneChild",
            "other":"createOneChild",
            "maintenanceAndUpdateFrequency":"createOneChild",
            "userDefinedMaintenanceFrequency":"createOneChild",
            "dataset":"createOneChild",
            "citation":"createOneChild",
            "dateOfNextUpdate":"createOneChild",
            "dimensionName":"createOneChild",
            "dimensionSize":"createOneChild",
            "resolution":"createOneChild",
            "numberOfDimensions":"createOneChild",
            "cellGeometry":"createOneChild",
            "transformationParameterAvailability":"createOneChild",
            "geometricObjectType":"createOneChild",
            "geometricObjectCount":"createOneChild",
            "topologyLevel":"createOneChild",
            "dateType":"createOneChild",
            "title":"createOneChild",
            "edition":"createOneChild",
            "editionDate":"createOneChild",
            "series":"createOneChild",
            "otherCitationDetails":"createOneChild",
            "collectiveTitle":"createOneChild",
            "ISBN":"createOneChild",
            "ISSN":"createOneChild",
            "authority":"createOneChild",
            "code":"createOneChild",
            "referenceSystemIdentifier":"createOneChild",
            "linkage":"createOneChild",
            "protocol":"createOneChild",
            "unitsOfDistribution":"createOneChild",
            "transferSize":"createOneChild",
            "offLine":"createOneChild",
            "name":"createOneChild",
            "version":"createOneChild",
            "amendmentNumber":"createOneChild",
            "specification":"createOneChild",
            "fileDecompressionTechnique":"createOneChild",
            "distributorContact":"createOneChild",
            "distributionInfo":"createOneChild",
            "westBoundLongitude":"createOneChild",
            "eastBoundLongitude":"createOneChild",
            "southBoundLatitude":"createOneChild",
            "northBoundLatitude":"createOneChild",
            "supplementalInformation":"createOneChild",
            "denominator":"createOneChild",
            "equivalentScale":"createOneChild",
            "type":"createOneChild",
            "purpose":"createOneChild",
            "abstract":"createOneChild",
            "city":"createOneChild",
            "administrativeArea":"createOneChild",
            "postalCode":"createOneChild",
            "country":"createOneChild",
            "address":"createOneChild",
            "phone":"createOneChild",
            "contactInfo":"createOneChild",
            "role": "createOneChild",
            "fileIdentifier": "createOneChild",
            "language": "createOneChild",
            "dateStamp": "createOneChild",
            "metadataStandardName": "createOneChild",
            "metadataStandardVersion": "createOneChild",
            "individualName": "createOneChild", 
            "organisationName":"createOneChild",
            "positionName":"createOneChild",
            "thesaurusName":"createOneChild",
            "pointInPixel":"createOneChild",
            "distance":"createOneChild",
            "geographicIdentifier":"createOneChild"
        },
        "srv": {
            // readAttributes
            // pass
            "SV_CoupledResource": "pass",
            "SV_ServiceIdentification": "pass",
            "SV_OperationMetadata": "pass",
            // createManyChildren
            "coupledResource": "createManyChildren",
            "connectPoint": "createManyChildren",
            "containsOperations": "createManyChildren",
            "serviceTypeVersion": "createManyChildren",
            // createOneChild
            "identifier": "createOneChild",
            "operationName": "createOneChild",
            "serviceType": "createOneChild"
        }
    },

    /**
     * Constructor: OpenLayers.Format.CSWGetRecords.v2_0_2
     * A class for parsing and generating CSWGetRecords v2.0.2 transactions.
     *
     * Parameters:
     * options - {Object} Optional object whose properties will be set on the
     *     instance.
     *
     * Valid options properties (documented as class properties):
     * - requestId
     * - resultType
     * - outputFormat
     * - outputSchema
     * - startPosition
     * - maxRecords
     * - DistributedSearch
     * - ResponseHandler
     * - Query
     */
    initialize: function(options) {
        OpenLayers.Format.XML.prototype.initialize.apply(this, [options]);
    },

    /**
     * APIMethod: read
     * Parse the response from a GetRecords request.
     */
    read: function(data) {
        if(typeof data == "string") { 
            data = OpenLayers.Format.XML.prototype.read.apply(this, [data]);
        }
        if(data && data.nodeType == 9) {
            data = data.documentElement;
        }
        var obj = {};
        this.readNode(data, obj);
        return obj;
    },
    
    /**
     * Property: readers
     * Contains public functions, grouped by namespace prefix, that will
     *     be applied when a namespaced node is found matching the function
     *     name.  The function will be applied in the scope of this parser
     *     with two arguments: the node being read and a context object passed
     *     from the parent.
     */
    readers: {
        "csw": {
            "GetRecordsResponse": function(node, obj) {
                obj.records = [];
                this.readChildNodes(node, obj);
                var version = this.getAttributeNS(node, "", 'version');
                if (version != "") {
                    obj.version = version;
                }
            },
            "GetRecordByIdResponse": function(node, obj) {
                obj.records = [];
                this.readChildNodes(node, obj);
            },
            "RequestId": function(node, obj) {
                obj.RequestId = this.getChildValue(node);
            },
            "SearchStatus": function(node, obj) {
                obj.SearchStatus = {};
                var timestamp = this.getAttributeNS(node, "", 'timestamp');
                if (timestamp != "") {
                    obj.SearchStatus.timestamp = timestamp;
                }
            },
            "SearchResults": function(node, obj) {
                this.readChildNodes(node, obj);
                var attrs = node.attributes;
                var SearchResults = {};
                for(var i=0, len=attrs.length; i<len; ++i) {
                    if ((attrs[i].name == "numberOfRecordsMatched") ||
                        (attrs[i].name == "numberOfRecordsReturned") ||
                        (attrs[i].name == "nextRecord")) {
                        SearchResults[attrs[i].name] = parseInt(attrs[i].nodeValue);
                    } else {
                        SearchResults[attrs[i].name] = attrs[i].nodeValue;
                    }
                }
                obj.SearchResults = SearchResults;
            },
            "SummaryRecord": function(node, obj) {
                var record = {type: "SummaryRecord"};
                this.readChildNodes(node, record);
                obj.records.push(record);
            },
            "BriefRecord": function(node, obj) {
                var record = {type: "BriefRecord"};
                this.readChildNodes(node, record);
                obj.records.push(record);
            },
            "DCMIRecord": function(node, obj) {
                var record = {type: "DCMIRecord"};
                this.readChildNodes(node, record);
                obj.records.push(record);
            },
            "Record": function(node, obj) {
                var record = {type: "Record"};
                this.readChildNodes(node, record);
                obj.records.push(record);
            },
            "*": function(node, obj) {
                var name = node.localName || node.nodeName.split(":").pop();
                obj[name] = this.getChildValue(node);
            }
        },
        "gco": {
            "Boolean": function(node, obj) {
                obj["boolean"] = this.getChildValue(node);
            },
            "Decimal": function(node, obj) {
                obj.decimal = this.getChildValue(node);
            },
            "CharacterString": function(node, obj) {
                obj.characterString = this.getChildValue(node);
            },
            "Date": function(node, obj) {
                obj.date = this.getChildValue(node);
            },
            "DateTime": function(node, obj) {
                obj.dateTime = this.getChildValue(node);
            },
            "ScopedName": function(node, obj) {
                obj.scopedName = this.getChildValue(node);
            },
            "LocalName": function(node, obj) {
                obj.localName = this.getChildValue(node);
            },
            "*": function(node, obj) {
                var name = node.localName || node.nodeName.split(":").pop();
                obj[name] = {value: this.getChildValue(node)};
                var attrs = node.attributes;
                for(var i=0, len=attrs.length; i<len; ++i) {
                    obj[name][attrs[i].name] = attrs[i].nodeValue;
                }
            }
        },
        "geonet": {
            "info": function(node, obj) {
                var gninfo = {};
                this.readChildNodes(node, gninfo);
                obj.gninfo = gninfo;
            }
        },
        "srv": {
            "extent": function(node, obj) {
                if (!(obj.extent instanceof Array)) {
                    obj.extent = [];
                }
                obj.extent.push(this.readChildNodes(node));
            },
            "*": function(node, obj) {
                var name = node.localName || node.nodeName.split(":").pop();
                switch (this.parsers["srv"][name]) {
                    case "readAttributes":
                        var attrs = node.attributes;
                        for(var i=0, len=attrs.length; i<len; ++i) {
                            obj[attrs[i].name] = attrs[i].nodeValue;
                        }
                        break;
                    case "pass":
                        this.readChildNodes(node, obj);
                        break;
                    case "createManyChildren":
                        if (!(obj[name] instanceof Array)) {
                            obj[name] = [];
                        }
                        obj[name].push(this.readChildNodes(node));
                        break;
                    case "createOneChild":
                        obj[name] = this.readChildNodes(node);
                        break;
                }
            }
        },
        "gmd": {
            "MD_Metadata": function(node, obj) {
                var record = {type: "MD_Metadata"};
                this.readChildNodes(node, record);
                obj.records.push(record);
            },
            "MD_TopicCategoryCode": function(node, obj) { 
                obj.topicCategoryCode = this.getChildValue(node);
            },
            "MD_PixelOrientationCode": function(node, obj) { 
                obj.pixelOrientationCode = this.getChildValue(node);
                // TODO: new parser method for "MD_TopicCategoryCode" & "MD_PixelOrientationCode"
            },
            "URL": function(node, obj) { 
                obj.URL = this.getChildValue(node);
            },
            "extent": function(node, obj) {
                // if parent is DQ_Scope_Type, cardinality is 0..1 else if MD_DataIdentification_Type 0..infty
                if (node.parentNode.localName == "MD_DataIdentification" || 
                    node.parentNode.nodeName.split(":").pop() == "MD_DataIdentification") {
                    if (!(obj.extent instanceof Array)) {
                        obj.extent = [];
                    }
                    obj.extent.push(this.readChildNodes(node));
                } else {
                    obj.extent = this.readChildNodes(node);
                }
            },
            "LocalisedCharacterString": function(node, obj) {
                var attrs = node.attributes, t = {};
                for(var i=0, len=attrs.length; i<len; ++i) {
                    t[attrs[i].name] = attrs[i].nodeValue;
                }
                obj["LocalisedCharacterString"] = OpenLayers.Util.extend({
                    value: this.getChildValue(node)
                }, t);
            },
            "*": function(node, obj) {
                var name = node.localName || node.nodeName.split(":").pop();
                switch (this.parsers["gmd"][name]) {
                    case "readAttributes":
                        var attrs = node.attributes;
                        for(var i=0, len=attrs.length; i<len; ++i) {
                            obj[attrs[i].name] = attrs[i].nodeValue;
                        }
                        break;
                    case "pass":
                        this.readChildNodes(node, obj);
                        break;
                    case "createManyChildren":
                        if (!(obj[name] instanceof Array)) {
                            obj[name] = [];
                        }
                        obj[name].push(this.readChildNodes(node));
                        break;
                    case "createOneChild":
                        obj[name] = this.readChildNodes(node);
                        break;
                }
            }
        },
        "gml": { // TODO: should be elsewhere
            "TimePeriod": function(node, obj) {
                obj.TimePeriod = {};
                this.readChildNodes(node, obj.TimePeriod);
            },
            "beginPosition": function(node, obj) {
                obj.beginPosition = this.getChildValue(node);
            },
            "endPosition": function(node, obj) {
                obj.endPosition = this.getChildValue(node);
            }
        },
        "dc": {
            // audience, contributor, coverage, creator, date, description, format,
            // identifier, language, provenance, publisher, relation, rights,
            // rightsHolder, source, subject, title, type, URI
            "*": function(node, obj) {
                var name = node.localName || node.nodeName.split(":").pop();
                if (!(OpenLayers.Util.isArray(obj[name]))) {
                    obj[name] = [];
                }
                var dc_element = {};
                var attrs = node.attributes;
                for(var i=0, len=attrs.length; i<len; ++i) {
                    dc_element[attrs[i].name] = attrs[i].nodeValue;
                }
                dc_element.value = this.getChildValue(node);
                if (dc_element.value != "") {
                    obj[name].push(dc_element);
                }
            }
        },
        "dct": {
            // abstract, modified, spatial
            "*": function(node, obj) {
                var name = node.localName || node.nodeName.split(":").pop();
                if (!(OpenLayers.Util.isArray(obj[name]))) {
                    obj[name] = [];
                }
                obj[name].push(this.getChildValue(node));
            }
        },
        "ows": OpenLayers.Util.applyDefaults({
            "BoundingBox": function(node, obj) {
                if (obj.bounds) {
                    obj.BoundingBox = [{crs: obj.projection, value: 
                        [
                            obj.bounds.left, 
                            obj.bounds.bottom, 
                            obj.bounds.right, 
                            obj.bounds.top
                    ]
                    }];
                    delete obj.projection;
                    delete obj.bounds;
                }
                OpenLayers.Format.OWSCommon.v1_0_0.prototype.readers["ows"]["BoundingBox"].apply(
                    this, arguments);
            }
        }, OpenLayers.Format.OWSCommon.v1_0_0.prototype.readers["ows"])
    },
    
    /**
     * Method: write
     * Given an configuration js object, write a CSWGetRecords request. 
     *
     * Parameters:
     * options - {Object} A object mapping the request.
     *
     * Returns:
     * {String} A serialized CSWGetRecords request.
     */
    write: function(options) {
        var node = this.writeNode("csw:GetRecords", options);
        node.setAttribute("xmlns:gmd", this.namespaces.gmd);
        return OpenLayers.Format.XML.prototype.write.apply(this, [node]);
    },

    /**
     * Property: writers
     * As a compliment to the readers property, this structure contains public
     *     writing functions grouped by namespace alias and named like the
     *     node names they produce.
     */
    writers: {
        "csw": {
            "GetRecords": function(options) {
                if (!options) {
                    options = {};
                }
                var node = this.createElementNSPlus("csw:GetRecords", {
                    attributes: {
                        service: "CSW",
                        version: this.version,
                        requestId: options.requestId || this.requestId,
                        resultType: options.resultType || this.resultType,
                        outputFormat: options.outputFormat || this.outputFormat,
                        outputSchema: options.outputSchema || this.outputSchema,
                        startPosition: options.startPosition || this.startPosition,
                        maxRecords: options.maxRecords || this.maxRecords
                    }
                });
                if (options.DistributedSearch || this.DistributedSearch) {
                    this.writeNode(
                        "csw:DistributedSearch",
                        options.DistributedSearch || this.DistributedSearch,
                        node
                    );
                }
                var ResponseHandler = options.ResponseHandler || this.ResponseHandler;
                if (OpenLayers.Util.isArray(ResponseHandler) && ResponseHandler.length > 0) {
                    // ResponseHandler must be a non-empty array
                    for(var i=0, len=ResponseHandler.length; i<len; i++) {
                        this.writeNode(
                            "csw:ResponseHandler",
                            ResponseHandler[i],
                            node
                        );
                    }
                }
                this.writeNode("Query", options.Query || this.Query, node);
                return node;
            },
            "DistributedSearch": function(options) {
                var node = this.createElementNSPlus("csw:DistributedSearch", {
                    attributes: {
                        hopCount: options.hopCount
                    }
                });
                return node;
            },
            "ResponseHandler": function(options) {
                var node = this.createElementNSPlus("csw:ResponseHandler", {
                    value: options.value
                });
                return node;
            },
            "Query": function(options) {
                if (!options) {
                    options = {};
                }
                var node = this.createElementNSPlus("csw:Query", {
                    attributes: {
                        typeNames: options.typeNames || "csw:Record"
                    }
                });
                var ElementName = options.ElementName;
                if (OpenLayers.Util.isArray(ElementName) && ElementName.length > 0) {
                    // ElementName must be a non-empty array
                    for(var i=0, len=ElementName.length; i<len; i++) {
                        this.writeNode(
                            "csw:ElementName",
                            ElementName[i],
                            node
                        );
                    }
                } else {
                    this.writeNode(
                        "csw:ElementSetName",
                        options.ElementSetName || {value: 'summary'},
                        node
                    );
                }
                if (options.Constraint) {
                    this.writeNode(
                        "csw:Constraint",
                        options.Constraint,
                        node
                    );
                }
                if (options.SortBy) {
                    this.writeNode(
                        "ogc:SortBy",
                        options.SortBy,
                        node
                    );
                }
                return node;
            },
            "ElementName": function(options) {
                var node = this.createElementNSPlus("csw:ElementName", {
                    value: options.value
                });
                return node;
            },
            "ElementSetName": function(options) {
                var node = this.createElementNSPlus("csw:ElementSetName", {
                    attributes: {
                        typeNames: options.typeNames
                    },
                    value: options.value
                });
                return node;
            },
            "Constraint": function(options) {
                var node = this.createElementNSPlus("csw:Constraint", {
                    attributes: {
                        version: options.version
                    }
                });
                if (options.Filter) {
                    var format = new OpenLayers.Format.Filter({
                        version: options.version
                    });
                    node.appendChild(format.write(options.Filter));
                } else if (options.CqlText) {
                    var child = this.createElementNSPlus("CqlText", {
                        value: options.CqlText.value
                    });
                    node.appendChild(child);
                }
                return node;
            }
        },
        "ogc": OpenLayers.Format.Filter.v1_1_0.prototype.writers["ogc"]
    },
   
    CLASS_NAME: "OpenLayers.Format.CSWGetRecords.v2_0_2" 
});
