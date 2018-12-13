package net.digitaltsunami.wheredigo.test;

import io.restassured.response.Response;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


@Getter
@Setter
@Component
public class World {
    private Set<URI> newSpendEntryUris = new HashSet<>();
    private Map<String,String> queryParameters = new HashMap<>();
    private Response response;
    private String currentId;
    public void clearWorld() {
        newSpendEntryUris = new HashSet<>();
        response = null;
        currentId = null;
        queryParameters.clear();
    }
}
