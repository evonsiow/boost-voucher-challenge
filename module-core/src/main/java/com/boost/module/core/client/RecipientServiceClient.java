package com.boost.module.core.client;

import com.boost.module.core.model.ApiResponse;
import com.boost.module.core.model.RecipientVM;
import com.boost.module.core.util.ExceptionConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class RecipientServiceClient {
    private final RestTemplate restTemplate;

    @Value("${recipient.ms.details.url}")
    private String retrieveRecipientDetailsByEmailURL;

    @Autowired
    public RecipientServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public RecipientVM getRecipientByEmail(String email) {
        try {
            String url = retrieveRecipientDetailsByEmailURL + email;
            ResponseEntity<ApiResponse<RecipientVM>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<ApiResponse<RecipientVM>>() {}
            );
            return response.getBody() != null ? response.getBody().getData() : null;
        } catch (HttpClientErrorException e) {
            // Treat any 4xx error as "not found"
            return null;
        } catch (Exception e) {
            throw new IllegalArgumentException(ExceptionConstant.Exception.ERROR_USER_MS_EXCEPTION.getExceptionMessage());
        }
    }
}
