# syntax=docker/dockerfile:1

FROM node:22-alpine AS base
WORKDIR /app
RUN corepack enable

FROM base AS development
COPY package.json yarn.lock ./
RUN yarn install --frozen-lockfile
COPY . .
EXPOSE 9000
CMD ["yarn", "dev", "--", "--hostname", "0.0.0.0", "--port", "9000"]

FROM base AS build
ARG VITE_APP_ENV=local
ARG VITE_API_BASE_URL=/api/v1
ENV VITE_APP_ENV=$VITE_APP_ENV
ENV VITE_API_BASE_URL=$VITE_API_BASE_URL
COPY package.json yarn.lock ./
RUN yarn install --frozen-lockfile
COPY . .
RUN yarn build

FROM nginx:1.27-alpine AS production
COPY --from=build /app/dist/spa /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
