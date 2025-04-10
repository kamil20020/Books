FROM node:23.0.0-alpine AS build
WORKDIR /app
COPY ./package.json .
COPY ./package-lock.json .
RUN npm config set legacy-peer-deps true
RUN npm install
COPY . .
RUN npm run build

FROM nginx:1.27.4
COPY /nginx/nginx.conf ./etc/nginx/conf.d/default.conf
COPY --from=build ./app/build ./usr/share/nginx/html
CMD ["nginx", "-g", "daemon off;"] 