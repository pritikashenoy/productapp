package com.example.product_listing_app.rate_limiting;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.Refill;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Duration;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalUnit;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;


@Component
public class RateLimiterInterceptor  implements HandlerInterceptor{

    @Value("${rate.limit.bucket.tokens}")
    private long tokenCapacity;

    @Value ("${rate.limit.refill.tokens}")
    private long refillTokens;

    @Value("${rate.limit.refill.duration}")
    private int refillDurationValue;

    @Value("${rate.limit.refill.duration.unit}")
    private String refillDurationUnit;


    Logger logger = LoggerFactory.getLogger(RateLimiterInterceptor.class);
    // Using ConcurrentHashMap to store the buckets
    private final ConcurrentHashMap<String, Bucket> cache = new ConcurrentHashMap<>();

    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        // Use the client IP as the key
        String clientKey = request.getRemoteAddr();


        Duration refillDuration = getRefillDuration(refillDurationValue, refillDurationUnit);
        // Create or get the bucket for the client
        Bucket bucket = cache.computeIfAbsent(clientKey, k -> createBucket(tokenCapacity, refillTokens, refillDuration));

        logger.debug("Client IP: {}, Tokens: {}, Refill Tokens: {}, Refill Duration: {}", clientKey, tokenCapacity, refillTokens, refillDuration);

        // Try to consume a token from the bucket
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);

        // If a token is available, update the remaining tokens and return true
        if (probe.isConsumed()) {
            logger.debug("Remaining tokens for client IP: {} = {}", clientKey, probe.getRemainingTokens());
            response.addHeader("X-Rate-Limit-Remaining", Long.toString(probe.getRemainingTokens()));
            return true;
        } else {
            // If no tokens are available, reject the request with a 429 status code and the time to wait for the next token
            String retryAfter = Long.toString(Duration.ofNanos(probe.getNanosToWaitForRefill()).toMillis());
            logger.error("Rate limit exceeded for client IP: {}, retry after {} ms", clientKey,
                   retryAfter);
            response.setStatus(429);
            response.addHeader("X-Rate-Limit-Retry-After-Milliseconds", retryAfter);
            return false;
        }

}

    // Create a bucket with a rate limit of configured tokens per minute
    private Bucket createBucket(long tokens, long refillTokens, Duration refillDuration) {
        logger.info("Creating bucket with capacity: {}, refill tokens: {}, refill duration: {}",
                tokens, refillTokens, refillDuration);

        Bandwidth limit = Bandwidth.builder().capacity(tokens)
                .refillGreedy(refillTokens, refillDuration)
                .build();
        return  Bucket.builder()
                .addLimit(limit)
                .build();
    }

    // Get the refill duration based on the configured duration value and unit. If the unit is not recognized, default to minutes
    private Duration getRefillDuration(int durationValue, String durationUnit) {
        return switch (TimeUnit.valueOf(durationUnit.toUpperCase())) {
            case SECONDS -> Duration.ofSeconds(durationValue);
            case MINUTES -> Duration.ofMinutes(durationValue);
            case HOURS -> Duration.ofHours(durationValue);
            // Use minutes as the default unit
            default -> Duration.ofMinutes(durationValue);
        };
    }

}