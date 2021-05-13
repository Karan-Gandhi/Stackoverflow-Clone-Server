FROM gradle
WORKDIR /app
COPY . .
# RUN gradle build
# RUN gradle run --no-daemon
# CMD [ "gradle run" ]
ENTRYPOINT [ "gradle", "run" ]