# ── Stage 1: Build ────────────────────────────────────────────────────────────
FROM node:18-alpine AS builder

WORKDIR /app

COPY smart-admin-web-javascript/package.json ./
RUN npm install --legacy-peer-deps

COPY smart-admin-web-javascript/ .
RUN npm run build:prod

# ── Stage 2: Nginx ────────────────────────────────────────────────────────────
FROM nginx:1.25-alpine

COPY --from=builder /app/dist /usr/share/nginx/html
COPY docker/frontend/nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
