# ── Stage 1: Build ────────────────────────────────────────────────────────────
FROM node:20.20.1-alpine3.23 AS builder

WORKDIR /app

COPY smart-admin-web-javascript/package.json ./
RUN npm config set registry https://registry.npmmirror.com && \
    npm install --legacy-peer-deps

COPY smart-admin-web-javascript/ .
RUN npm run build:prod

# ── Stage 2: Nginx ────────────────────────────────────────────────────────────
FROM nginx:1.25-alpine

COPY --from=builder /app/dist /usr/share/nginx/html
COPY docker/frontend/nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
