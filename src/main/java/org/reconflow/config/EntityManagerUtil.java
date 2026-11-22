package org.reconflow.config;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.flywaydb.core.Flyway;

import java.util.HashMap;
import java.util.Map;

public class EntityManagerUtil {

    private static final EntityManagerFactory emf;

    static {
        Dotenv dotenv = Dotenv.load();

        String url = "jdbc:postgresql://localhost:5436/"
                + dotenv.get("POSTGRES_DB");

        String user = dotenv.get("POSTGRES_USER");
        String password = dotenv.get("POSTGRES_PASSWORD");

        // Run database migrations
        Flyway.configure()
                .dataSource(url, user, password)
                .locations("classpath:db/migration")
                .load()
                .migrate();

        // Configure Hibernate
        Map<String, Object> properties = new HashMap<>();

        properties.put("jakarta.persistence.jdbc.url", url);
        properties.put("jakarta.persistence.jdbc.user", user);
        properties.put("jakarta.persistence.jdbc.password", password);
        properties.put("jakarta.persistence.jdbc.driver",
                "org.postgresql.Driver");

        emf = Persistence.createEntityManagerFactory(
                "rfPU",
                properties
        );
    }


    public static EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }
}
