#!/bin/bash

#=============================================================================
#
# will download 'domains.xml' to ./rivta-downloaded-data/domains.xml
#
#=============================================================================

downloadDir="rivta-downloaded-data"
mkdir -p ${downloadDir}
curl http://rivta.se/domains/domains.xml > ${downloadDir}/domains.xml
echo "Done. Data files are in: ${downloadDir}"
