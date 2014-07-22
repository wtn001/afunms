/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.activiti.bpmn.converter.child;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.stream.XMLStreamReader;

import org.activiti.bpmn.constants.BpmnXMLConstants;
import org.activiti.bpmn.model.BaseElement;

/**
 * @author Tijs Rademakers
 */
public abstract class BaseChildElementParser implements BpmnXMLConstants {
  
  protected static final Logger LOGGER = Logger.getLogger(BaseChildElementParser.class.getName());

  protected BaseElement parentElement;
  
  public abstract String getElementName();
  
  public abstract void parseChildElement(XMLStreamReader xtr, BaseElement parentElement) throws Exception;
  
  protected void parseChildElements(XMLStreamReader xtr, BaseElement parentElement, BaseChildElementParser parser) {
  	this.parentElement = parentElement;
    boolean readyWithChildElements = false;
    try {
      while (readyWithChildElements == false && xtr.hasNext()) {
        xtr.next();
        if (xtr.isStartElement()) {
          if (parser.getElementName().equals(xtr.getLocalName())) {
            parser.parseChildElement(xtr, parentElement);
          }

        } else if (xtr.isEndElement() && getElementName().equalsIgnoreCase(xtr.getLocalName())) {
          readyWithChildElements = true;
        }
      }
    } catch (Exception e) {
      LOGGER.log(Level.WARNING, "Error parsing child elements for " + getElementName(), e);
    }
  }
}
