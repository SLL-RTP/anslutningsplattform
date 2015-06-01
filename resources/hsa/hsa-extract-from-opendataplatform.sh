#!/bin/bash

#=============================================================================
# Prepare HSA-data for Anslutningsplattform.
#
# Extract HSA-data from the Open Data platform (using service
# "Lista HSA-objekt" with JSON-output) and then transform data to the XML
# format used internally in Anslutningsplattform.
# 
# Ref: http://sdk.minavardkontakter.se/oppen-data/domain/?id=5141bf7ee211b84875000010#lista-hsa-objekt-1
#
# Hakan Dahl
#=============================================================================

hsaDataDir="hsa-extracted-data"
mkdir -p ${hsaDataDir}
curl http://api.offentligdata.minavardkontakter.se/orgmaster-hsa/v1/hsaObjects > ${hsaDataDir}/hsa-extract-from-opendataplatform.json
groovy hsaTransformFromOpenDataPlatform.groovy ${hsaDataDir}/hsa-extract-from-opendataplatform.json ${hsaDataDir}/hsacache.xml

echo "Done. Data files are in: ${hsaDataDir}"
