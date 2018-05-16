FROM openjdk:8-jre-alpine
MAINTAINER Hai Nguyen <hainguyen@mycollab.com>

ENTRYPOINT ["/usr/bin/java", "-jar", "/usr/share/mycollab/executor.jar", "-noverify"]

ADD staging/bin              /usr/share/mycollab/bin
ADD staging/config           /usr/share/mycollab/config
ADD staging/i18n             /usr/share/mycollab/i18n
# Add Maven dependencies (not shaded into the artifact; Docker-cached)
ADD target/staging/lib              /usr/share/mycollab/lib
# Add the service itself
ADD staging/executor.jar     /usr/share/mycollab/executor.jar

EXPOSE 8080