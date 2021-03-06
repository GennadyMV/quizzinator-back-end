package app.config;

import java.net.URI;
import java.net.URISyntaxException;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("heroku")
@Configuration
public class HerokuDbConfig {
    @Bean
    public DataSource dataSource()  {    

        URI dbUri;
        try {
            // herokun dburl format:
            // postgres://username:passssword@data.base.server:6212/databasename
            String username;
            String password;
            String url;
            String dbProperty = System.getenv("DATABASE_URL");
            
            dbUri = new URI(dbProperty);

            username = dbUri.getUserInfo().split(":")[0];
            password = dbUri.getUserInfo().split(":")[1];
            url = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
            
            
            BasicDataSource basicDataSource = new BasicDataSource();
            basicDataSource.setDriverClassName("org.postgresql.Driver");
            basicDataSource.setUrl(url);
            basicDataSource.setUsername(username);
            basicDataSource.setPassword(password);
            
            return basicDataSource;
        } catch (URISyntaxException e) {
            //Deal with errors here.
        }
        return null;
    }
}