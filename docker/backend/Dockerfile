# ── Stage 1: Build ────────────────────────────────────────────────────────────
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /build

# 先复制 pom 文件，利用 Docker 层缓存
COPY pom.xml .
COPY sa-base/pom.xml sa-base/pom.xml
COPY sa-admin/pom.xml sa-admin/pom.xml

RUN mvn dependency:go-offline -B -P prod || true

# 复制源码并构建
COPY sa-base sa-base
COPY sa-admin sa-admin

RUN mvn clean package -Dmaven.test.skip=true -P prod

# ── Stage 2: Runtime ──────────────────────────────────────────────────────────
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

RUN addgroup -S appgroup && adduser -S appuser -G appgroup

COPY --from=builder /build/sa-admin/target/sa-admin-prod-*.jar app.jar

RUN chown appuser:appgroup app.jar

USER appuser

EXPOSE 1024

ENTRYPOINT ["java", \
  "-server", \
  "-Xms512m", "-Xmx1024m", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", "app.jar" \
]
