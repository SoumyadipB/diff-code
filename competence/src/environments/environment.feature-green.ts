export const environment = {
  production: false,
  authConfig: {
    instance: 'https://login.microsoftonline.com/',
    tenant: '92e84ceb-fbfd-47ab-be52-080c6b87953f',
    tenentId: '92e84ceb-fbfd-47ab-be52-080c6b87953f',
    clientId: '18abfc4e-0da4-4b38-a020-c6cefeb0defb',
    redirectUri: 'https://isf-dev.internal.ericsson.com/COMPETENCE_UI_Feature_Green/',
    navigateToLoginRequestUrl: false,
    cacheLocation: 'localStorage'
  },
  GLOBAL_API_URL : "https://isfsitservices.internal.ericsson.com:8443/isf-rest-server-java_Feature_Green/",
 rootDir:"/COMPETENCE_UI_Feature_Green/",
 logoutRedirect:"https://isf-dev.internal.ericsson.com/ISF_UI_Feature_Green/",
 LMTestingEmail:""
};
