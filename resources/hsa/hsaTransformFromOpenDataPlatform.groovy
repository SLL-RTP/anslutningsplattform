//=============================================================================
// Transform HSA data from the Open Data platform (JSON-format) to the
// XML-format used internally in Anslutningsplattform.
//
// Hakan Dahl
//=============================================================================

import groovy.json.JsonSlurper
import groovy.xml.MarkupBuilder

inFile = new File("hsa-extracted-data/hsa-extract-from-opendataplatform.json")
outFile = new File("hsa-extracted-data/hsacache.xml")

// Usage: <script> <input file> <output file>
if (this.args.length == 2) {
  inFile = new File(this.args[0])
  outFile = new File(this.args[1])
  println "Using input args for files: ${inFile}, ${outFile}"
}
else {
  println "Wrong number of input args given, falling back to default in- and out files: ${inFile}, ${outFile}"
}

doTransformFile(inFile, outFile)

def doTransformFile(File inputOpenDataPlatformJsonFile, File outputXmlFile) {
  def jsonSlurper = new JsonSlurper()
  def jsonRoot = jsonSlurper.parseText(inputOpenDataPlatformJsonFile.getText("UTF-8"))

  def writer = new StringWriter()
  def xml = new MarkupBuilder(writer)
  // use last modified for input file as start/end date
  def lastModified = new Date(inputOpenDataPlatformJsonFile.lastModified()).format("yyyy-MM-dd'T'HH:mm:ss")
  writer.write("<!--\n${lastModified} file produced with script: anslutningsplattform/resources/hsa/hsa-extract-from-opendataplatform.sh \n-->\n")  
  xml.HsaUnitsResponse("xmlns": "urn:riv:hsa:HsaWsResponder:3") {
    startDate(lastModified)
    endDate(lastModified)
    hsaUnits() {
      jsonRoot.each { fromRecord -> 
        hsaUnit() {
          hsaIdentity(fromRecord.hsaId)
          DN(fromRecord.dn)
          name(fromRecord.relativeDistinguishedName)
        }
      }      
    }
  }
  println writer.toString()
  outputXmlFile.setText(writer.toString(), "UTF-8")
}
