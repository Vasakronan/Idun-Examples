# Authentication
![ProptechOS logo](images/ProptechOS-logotype.png)  
Authentication in ProptechOS uses OAuth 2.0 protocol.
It can be separated into two categories:
* interactive authentication (for applications accessing the API on behalf of user, like web apps and UIs)
* application (daemon) authentication, for applications working without user interaction.

In both cases, the final goal is to obtain an Access Token, which is used in HTTP header for every call.

Authentication is performed in Microsoft Identity Platform (former Active Directory (AD)), so the user need to have an account in Idun AD. In case of application authentication, the application need to have it's own ID in Idun AD. After the user and/or application has authenticated itself, it obtains a token in JWT format, which is then put into the Authorization header for subsequent calls to the ProptechOS REST API.  
It has to be prepended by Bearer clause:
```
--header 'Authorization: Bearer XXXXXX_TOKEN_BODY_XXXXXXXXXX'  
```

## 1. Interactive authentication uses OAuth Implicit Grant flow.

For Web UI application that users are interacting with, there is MSAL for Javascript Library provided by Microsoft,
that allows to perform authorization of the user in the frame of a UI. In this case, the application (usually, web page) is acting on behalf of the user that was authenticated.
The library exists for several JS frameworks as well as framework-agnostic version.
Details can be found here:
https://github.com/AzureAD/microsoft-authentication-library-for-js

Depending on type of usage, the refreshing of the token is handled by MSAL library.

The application that wants to use the ProptechOS API with Implicit flow authentication need to have its own identity in Idun's Active Directory, and the correct Redirect URI registered. Registering the application and redirect URI is done by Idun admin.

An example of how to use it can be found in examples folder.


```
// Line breaks for legibility only
GET  https://login.microsoftonline.com/{tenant}/oauth2/v2.0/authorize?
client_id=535fb089-9ff3-47b6-9bfb-4f1264799865
&response_type=id_token
&redirect_uri=http%3A%2F%2{ myApp.com }%2F{ myRedirectEndpoint }%2F
&scope=Api.Use
&response_mode=fragment
&state=12345
&nonce=678910
```

Read more: [Microsoft Docs - MSAL Authentication Flows Authorization Code](https://docs.microsoft.com/en-us/azure/active-directory/develop/msal-authentication-flows#authorization-code)

## 2. Application Authentication.

In case of this type of authorization, a 'Client Credentials' OAuth flow is used. The application has its own id _and password_ ("secret"), and uses it to obtain a token.

The application that wants to use Idun API with Application authentication need to have its own identity in Idun's Active Directory. As opposed to Implicit auth (above) the application will _not need to register a Redirect URI_, but _need to ask for a Client Secret to be generated_. This is done by Idun Admin once for each application. After the creation of identity, the ID and Secret of the application is obtained.

The most straightforward and universal (language-independent) way for application to authenticate itself
is directly using HTTP POST method on Microsoft authentication endpoint.

```
//Line breaks for clarity
POST https://login.microsoftonline.com/{tenant}/oauth2/v2.0/token?
client_id={client ID}
&client_secret={client secret}}
&scope=https%3A%2F%2F{ myApp }%2F.default
&grant_type=client_credentials
```

important fields here are:
* client_id: ID of the application, obtained after its creation in Idun AD;
* client_secret: also generated after the application creation;
* scope: **this has to be application name followed by '/.default';**
* grant_type: indicates flow, must be 'client_credentials'.


Another way is to use MSAL Library provided by Microsoft. An example of how to use it can be found in the **authentication/examples folder**.

The token provided has an expiration time. Usually it is one hour, and it is up to an application to track this timeout
and/or expiration errors, and to retrieve new access tokens in time.

Current version is  '0.5.0-preview'   
Details how to use the lib in the project can be found here:
https://github.com/AzureAD/microsoft-authentication-library-for-java



### Migration from old Authentication method.

In previous version, Idun authentication was done by putting predefined Authorization code into Authorization header.
In a new version, the procedure is basically the same, the only difference is that the token is not statically defined,
but obtained during the authentication process described above.
Another difference is that the token is preceded by 'Bearer ' keyword in Authorization header.
Also, in case of 'Daemon application', the token refreshment must be coded and daemon must take care that the token is not expired.
Additionally, proper error handling in case the token validation failed, must be performed.

In case of MSAL libraries for Javascript (Angular or other frameworks), the token refreshment is performed by the library itself.

### Resources

For more information about Microsoft Identity Platform, OAuth2 and MSAL Libraries for different languages, it is recommended to
visit Microsoft official documentation

```text
https://docs.microsoft.com/en-us/azure/active-directory/develop/authentication-scenarios
```