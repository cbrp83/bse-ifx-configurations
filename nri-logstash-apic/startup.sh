#!/bin/bash

set -e

sed "s/\!\!\!PORT\!\!\!/$PORT/" logstash_template.conf > logstash1.conf
sed "s/\!\!\!WHITELIST\!\!\!/$WHITELIST/" logstash1.conf > logstash2.conf
sed "s/\!\!\!DEBUG\!\!\!/$DEBUG/" logstash2.conf > logstash3.conf
sed "s/\!\!\!DEBUG_ALL\!\!\!/$DEBUG_ALL/" logstash3.conf > logstash4.conf
sed "s/\!\!\!HTTPS_PROXY_HOST\!\!\!/$HTTPS_PROXY_HOST/" logstash4.conf > logstash5.conf
sed "s/\!\!\!PROXY_PORT\!\!\!/$PROXY_PORT/" logstash5.conf > logstash6.conf
sed "s/\!\!\!SCHEMA\!\!\!/$SCHEMA/" logstash6.conf > logstash7.conf
sed "s/\!\!\!API_KEY\!\!\!/$API_KEY/" logstash7.conf > logstash.conf

bash -c "cp logstash.yml /usr/share/logstash/config"
bash -c "/usr/share/logstash/bin/logstash -f logstash.conf"
