curl http://api.offentligdata.minavardkontakter.se/orgmaster-hsa/v1/hsaObjects?businessClassificationCode=1311,1312 > raw.json

python -m json.tool raw.json > pretty.json
