## ===== Build stage =====
FROM gradle:8.9-jdk17-alpine AS build
WORKDIR /workspace

# Gradle 캐시 최적화를 위해 래퍼/설정 먼저 복사
COPY gradle gradle
COPY gradlew settings.gradle.kts build.gradle.kts ./
RUN chmod +x gradlew

# 소스 복사 후 빌드 (테스트는 CI에서 돌리므로 -x test)
COPY src ./src
RUN ./gradlew --no-daemon clean bootJar -x test

## ===== Runtime stage =====
FROM eclipse-temurin:17-jre-alpine

RUN addgroup -S spring && adduser -S spring -G spring
USER spring

WORKDIR /app
COPY --from=build /workspace/build/libs/*.jar /app/app.jar

# 런타임 환경
ENV TZ=Asia/Seoul \
    JAVA_OPTS="-XX:MaxRAMPercentage=75.0 -XX:+UseG1GC" \
    SPRING_PROFILES_ACTIVE=dev

# 실제 개방 포트는 Railway가 PORT로 지정할 수 있음
EXPOSE 8080

# 별도 스크립트 없이 바로 실행
ENTRYPOINT [ "sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar" ]
