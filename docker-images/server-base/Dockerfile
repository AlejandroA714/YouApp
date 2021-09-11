FROM azul/zulu-openjdk-alpine:15.0.0-jre
RUN apk --no-cache add curl rsync

ADD limits.conf /etc/security/limits.conf

RUN addgroup -S youapp && \
    adduser -S appuser -G youapp && \
    mkdir /app && \
    chown appuser:youapp /app

USER appuser:youapp