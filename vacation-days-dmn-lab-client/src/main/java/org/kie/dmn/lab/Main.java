package org.kie.dmn.lab;

import org.kie.api.builder.KieScannerFactoryService;
import org.kie.api.internal.weaver.KieWeaverService;
import org.kie.dmn.api.core.DMNContext;
import org.kie.dmn.api.core.DMNDecisionResult;
import org.kie.dmn.api.core.DMNResult;
import org.kie.server.api.model.ServiceResponse;
import org.kie.server.client.CredentialsProvider;
import org.kie.server.client.DMNServicesClient;
import org.kie.server.client.KieServicesClient;
import org.kie.server.client.KieServicesConfiguration;
import org.kie.server.client.KieServicesFactory;
import org.kie.server.client.credentials.EnteredCredentialsProvider;

/**
 * Vacation Days DMN Client
 */
public class Main {

    private static final String KIE_SERVER_URL = "http://localhost:8080/kie-server/services/rest/server";

    private static final String CONTAINER_ID = "vacation-days-decisions_1.0.0";

    private static final String USERNAME = "pamAdmin";

    private static final String PASSWORD = "redhatpam1!";

    private static final String DMN_MODEL_NAMESPACE = "https://github.com/kiegroup/drools/kie-dmn/_D0E62587-C08C-42F3-970B-8595EA48BEEE";
    
    private static final String DMN_MODEL_NAME = "vacation-days";

    public static void main(String[] args) {
        CredentialsProvider credentialsProvider = new EnteredCredentialsProvider(USERNAME, PASSWORD);
        KieServicesConfiguration kieServicesConfig = KieServicesFactory.newRestConfiguration(KIE_SERVER_URL, credentialsProvider);
        KieServicesClient kieServicesClient = KieServicesFactory.newKieServicesClient(kieServicesConfig);

        DMNServicesClient dmnServicesClient = kieServicesClient.getServicesClient(DMNServicesClient.class);

        DMNContext dmnContext = dmnServicesClient.newContext();
        dmnContext.set("Age", 16);
        dmnContext.set("Years of Service", 1);

        ServiceResponse<DMNResult> dmnResultResponse = dmnServicesClient.evaluateAll(CONTAINER_ID, DMN_MODEL_NAMESPACE, DMN_MODEL_NAME, dmnContext);

        DMNDecisionResult decisionResult = dmnResultResponse.getResult().getDecisionResultByName("Total Vacation Days");
        System.out.println("Total vacation days: " + decisionResult.getResult());
    }
}
