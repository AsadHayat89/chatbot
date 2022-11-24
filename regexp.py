import re

quantRegex = re.compile(r'^[1-9]\d*$')
dateRegex = re.compile(r'^(0[1-9]|[12][0-9]|3[01])[- /.](0[1-9]|1[012])[- /.](19|20)\d\d$')
timeRegex = re.compile(r'^([01]?[0-9]|2[0-3]):[0-5][0-9]$')
optionRegex = re.compile(r'^[1-3]\d*$')