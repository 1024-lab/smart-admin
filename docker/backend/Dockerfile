# ── Stage 1: Build ────────────────────────────────────────────────────────────
FROM maven:3.9.14-eclipse-temurin-17-alpine AS builder

WORKDIR /build

# 使用阿里云 Maven 镜像加速
COPY docker/backend/settings.xml /root/.m2/settings.xml

# 先复制 pom 文件，利用 Docker 层缓存
COPY smart-admin-api-java17-springboot3/pom.xml .
COPY smart-admin-api-java17-springboot3/sa-base/pom.xml sa-base/pom.xml
COPY smart-admin-api-java17-springboot3/sa-admin/pom.xml sa-admin/pom.xml

RUN mvn dependency:go-offline -B -P prod || true

# 复制源码并构建
COPY smart-admin-api-java17-springboot3/sa-base sa-base
COPY smart-admin-api-java17-springboot3/sa-admin sa-admin

RUN mvn clean package -Dmaven.test.skip=true -P prod

# ── Stage 2: Runtime ──────────────────────────────────────────────────────────
FROM ubuntu/jre:17-24.04

WORKDIR /app

COPY --from=builder /build/sa-admin/target/sa-admin-prod-*.jar app.jar

EXPOSE 1024

ENTRYPOINT ["java", \
  "-server", \
  "-Xms512m", "-Xmx1024m", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", "app.jar" \
]
