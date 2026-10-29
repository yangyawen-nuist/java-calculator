# Use the lightweight nginx Alpine image to serve the calculator webpage.
FROM m.daocloud.io/docker.io/library/nginx:1.27-alpine

# Replace the default nginx server configuration with the Compose-aware config.
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Copy the static calculator webpage into the nginx document root.
COPY index.html styles.css app.js /usr/share/nginx/html/

# Document the HTTP port exposed by nginx.
EXPOSE 80
