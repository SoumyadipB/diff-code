// This file can be replaced during build by using the `fileReplacements` array.
// `ng build --prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

export const environment = {
  production: false,
  authConfig: {
    instance: 'https://login.microsoftonline.com/',
    tenant: '92e84ceb-fbfd-47ab-be52-080c6b87953f',
    tenentId: '92e84ceb-fbfd-47ab-be52-080c6b87953f',
    clientId: '6073edbe-6c9a-47b0-af09-0bacbe27f2fa',
    redirectUri: window.location.origin,
    navigateToLoginRequestUrl: false,
    cacheLocation: 'localStorage'
  },
  GLOBAL_API_URL : "https://isfsitservices.internal.ericsson.com:8443/isf-rest-server-java_sit_minor/",
  rootDir:"/",
  logoutRedirect:"https://isfsit.internal.ericsson.com/ISF_UI_SIT_MINOR/",
  LMTestingEmail:""
};

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/dist/zone-error';  // Included with Angular CLI.
