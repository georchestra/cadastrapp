package org.georchestra.cadastrapp.model;

import java.util.List;

import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;

/**
 * This bean represents a ProjectTeam.
 */
public class RelevePropriete {

   
    /**
     * Resturns a Source object for this object so it can be used as input for
     * a JAXP transformation.
     * @return Source The Source object
     */
    public Source getSourceForRelevePropriete() {
        return new SAXSource(new ReleveProprieteXMLReader(),
                new ReleveProprieteInputSource(this));
    }


}