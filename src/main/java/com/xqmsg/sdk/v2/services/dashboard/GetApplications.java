package com.xqmsg.sdk.v2.services.dashboard;

import com.xqmsg.sdk.v2.*;
import com.xqmsg.sdk.v2.utils.Destination;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

/**
 * A service to create a new Contact for the dashboard
 *
 */
public class GetApplications extends XQModule {

  private final Logger logger = Logger.getLogger(getClass().getName(), null);

  public static final String APPS = "apps";

  private static final String SERVICE_NAME = "devapps";

  private GetApplications(XQSDK sdk) {
    assert sdk != null : "An instance of the XQSDK is required";
     super.sdk = sdk;
     super.cache = sdk.getCache();
  }

  /**
   * @param sdk App Settings
   * @returns AddContact
   */
  public static GetApplications with(XQSDK sdk) {
    return new GetApplications(sdk);
  }

  @Override
  public List<String> requiredFields() {
    return List.of();
  }

  /**
   * @param maybeArgs Map of request parameters supplied to this method.
   *                  <pre>parameter details:<br>
   *                                   String user! - Email of the user to be authorized.<br>
   *                                   String firstName?  - First name of the user.<br>
   *                                   String lastName? - Last name of the user.<br>
   *                                   Boolean newsLetter? [false] - Should the user receive a newsletter.<br>
   *                                   NotificationEnum notifications? [0] - Enum Value to specify Notification Settings.<br>
   *                                   </pre>
   * @returns CompletableFuture&lt;ServerResponse#payload:{data:String}>>
   * @apiNote !=required ?=optional [...]=default {...} map
   */
  @Override
  public CompletableFuture<ServerResponse> supplyAsync(Optional<Map<String, Object>> maybeArgs) {

    return CompletableFuture.completedFuture(
            authorize
                    .andThen((dashboardAccessToken) -> {
                              Map<String, String> headerProperties = Map.of("Authorization", String.format("Bearer %s", dashboardAccessToken));
                              return sdk.call(sdk.DASHBOARD_SERVER_URL,
                                      Optional.of(SERVICE_NAME),
                                      CallMethod.Get,
                                      Optional.of(headerProperties),
                                      Optional.of(Destination.DASHBOARD),
                                      Optional.empty());
                            }
                    ).apply(Optional.of(Destination.DASHBOARD), maybeArgs)
    )
    .exceptionally(e -> new ServerResponse(CallStatus.Error, Reasons.LocalException, e.getMessage()));

  }

  @Override
  public String moduleName() {
    return "GetApplications";
  }

}