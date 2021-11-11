FROM registry.access.redhat.com/ubi8/ubi

RUN yum update -y && \
    yum install -y wget java-1.8.0-openjdk && \ 
    yum clean all && \
    wget https://artifacts.elastic.co/downloads/logstash/logstash-7.9.3.rpm && \ 
    rpm -i logstash-7.9.3.rpm && \
    useradd -u 1000 -d /home/loguser loguser

WORKDIR /usr/share/logstash


RUN bin/logstash-plugin install logstash-output-newrelic && \
	mkdir -p /usr/share/logstash/config/ && \
    chgrp -R 0 /usr/share/logstash && chmod -R g=u /usr/share/logstash

WORKDIR /home/loguser

COPY logstash_template.conf logstash.yml startup.sh ./

EXPOSE 8080 9600

RUN chgrp -R 0 /home/loguser && chmod -R g=u /home/loguser

ENTRYPOINT ["/home/loguser/startup.sh"]
