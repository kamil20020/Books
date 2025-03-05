FROM postgres:13.5
COPY /scripts/schema /docker-entrypoint-initdb.d/
COPY /scripts/data /docker-entrypoint-initdb.d/