package ch.css.learning.jobrunr.sso;

import com.nimbusds.jwt.JWTClaimsSet;
import jakarta.annotation.Priority;
import jakarta.enterprise.inject.Alternative;
import jakarta.inject.Singleton;
import org.jobrunr.dashboard.server.security.JobRunrUserAuthorizationRules;
import org.jobrunr.dashboard.server.security.JobRunrUserAuthorizationRulesBuilder;
import org.jobrunr.dashboard.server.security.openidconnect.OpenIdConnectSettings;
import org.jobrunr.dashboard.server.security.openidconnect.authorization.JobRunrUserProvider;
import org.jobrunr.dashboard.server.security.openidconnect.authorization.JobRunrUserUsingJWTAccessTokenProvider;
import org.jobrunr.utils.reflection.ReflectionUtils;

import java.util.List;

public class JwtUserProvider extends JobRunrUserUsingJWTAccessTokenProvider {

    public enum UserRole {
        MANAGER,
        DEVELOPER;
    }

    public JwtUserProvider(OpenIdConnectSettings openIdConnectSettings) {
        super(openIdConnectSettings);
    }

    @Override
    protected JobRunrUserAuthorizationRules authorizationRules(JWTClaimsSet claimsSet) {
        var roles = getRoles(claimsSet);
        if (roles.contains("superuser".toLowerCase()))
            return JobRunrUserAuthorizationRulesBuilder.allowAll().build();
        if (roles.contains(UserRole.DEVELOPER.name().toLowerCase()))
            return JobRunrUserAuthorizationRulesBuilder.readOnly().canEnqueueJobs(true).canUploadLicense(true).build();

        return JobRunrUserAuthorizationRulesBuilder.readOnly().build();
    }

    private List<String> getRoles(JWTClaimsSet claimsSet) {
        return ReflectionUtils.cast(claimsSet.getClaim("groups"));
    }

    @Alternative
    @Priority(1)
    @Singleton
    public JobRunrUserProvider jobRunrUserProvider(OpenIdConnectSettings openIdConnectSettings) {
        return new JwtUserProvider(openIdConnectSettings);
    }
}