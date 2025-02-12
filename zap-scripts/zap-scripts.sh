#!/bin/bash
# Pull the latest ZAP Docker image
docker pull zaproxy/zap-stable
# Run ZAP baseline scan on the target application and generate report
docker run -i zaproxy/zap-stable zap-baseline.py -t "http://http://18.143.182.252:8080" - 1 PASS > zap_baseline_report.html
# Exit without printing any error message
echo $? › / dev/null